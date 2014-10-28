package questionSite;

import java.io.Serializable;

//java bean that represents questions
public class Question implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String body;
	private String company;
	
	
	public Question(String id, String body, String company){
		this.id = id;
		this.body = body;
		this.company = company;
	}
	public String getId() {
		return id;
	}
	public String getBody() {
		return body;
	}
	public String getCompany() {
		return company;
	}
	public String toString(){
		return id + " " + body + " " + company;
	}
}