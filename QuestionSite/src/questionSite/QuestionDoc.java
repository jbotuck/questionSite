package questionSite;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

//Object for downloading interview questions
//was originally implemented with only static methods
public class QuestionDoc{
	public static final int DEFAULT = 0;
	private static final String defaultURL = "http://www.careercup.com/page";
	private static final String baseURL = defaultURL + "?n=";
	private static Boolean connectToInternet = true; //read a locally saved test file when false
	private int page = DEFAULT;
	
	public int getPage() {
		return page;
	}
	
	public QuestionDoc(){
		
	}
	public QuestionDoc(int page){
		this.page = page;
	}
	//returns the question objects
	public List<Question> getQuestions() throws Exception{
		return getQuestions(page);
	}
	//Returns a list of question objects
	private static List<Question> getQuestions(int page) throws Exception{
		//get each entry in the page's unordered list as a jsoup Element
		Elements liElements = getLiElements(page);
		//convert the Elements into Questions
		List<Question> questions = new ArrayList<Question>();
		for (Element e: liElements){
			String id, body, company;
			id = getId(e);
			body = getBody(e);
			company = getCompany(e);
			questions.add(new Question(id, body, company));
		}
		return questions;
	}
	
	//returns all of the page's unordered list entries as jsoup Elements
	private static Elements getLiElements(int page) throws Exception {
		System.out.println("connect to internet? " + connectToInternet);
		Document doc = null;
		try {
			doc = connectToInternet ? Jsoup.parse(downloadQuestions(page)) 
					: Jsoup.parse(new File("quest.html"), null, "http://www.careercup.com/page?n=10");
		} catch (Exception e){
			if (page == DEFAULT)
				throw e;
			else
				return getLiElements(DEFAULT);
		}
		//find all the <li class=question> elements
		Elements liElements = doc.select("li.question");
		//if possible, throw out pages that have no results
		return liElements.isEmpty() && page != DEFAULT ? getLiElements(DEFAULT) : liElements;
	}
	
	//downloads the requested page, returns entire page as a string
	private static String downloadQuestions(int page) throws MalformedURLException, IOException  {
		String url = page == DEFAULT ? defaultURL: baseURL + Integer.toString(page);
		URLConnection connection = new URL(url).openConnection();
		connection.connect();
		System.out.println("getting Scanner");
		Scanner in = new Scanner(connection.getInputStream());
		System.out.println("scanning");
		String html = in.useDelimiter("\\A").next();
		in.close();
		return html;
	}
	//following 3 Methods all extract the important information from the <li class=question> elements
	private static String getCompany(Element e) {
		String[] title = e.select("span.company > img").first().attr("title").split("-");
		String result = "";
		for (int i = 0; i < title.length - 2; i++)
			result += title[i];
		return result.isEmpty() ? null : result;
	}

	private static String getBody(Element e) {
		return e.select("span.entry > a").first().html();
	}

	private static String getId(Element e) {
		return e.select("span.entry > a").first().attr("href").split("=")[1];
	}
	public static void main(String[] args) throws Exception {
		connectToInternet = false;
		testGetQuestions();
	}
	
	private static void testGetQuestions() throws Exception {
		List<Question> questions = getQuestions(1);
		for (Question q: questions)
			System.out.println(q);
	}
}