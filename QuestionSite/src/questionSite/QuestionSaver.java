package questionSite;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;


//DAO for writing questions to database
public class QuestionSaver {
	private Connection connection;
	private PreparedStatement addRecord;
	
	public QuestionSaver(String username, String password) throws ClassNotFoundException, SQLException, IOException{
		Class.forName("com.mysql.jdbc.Driver");
		connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/question_db",
				username, password);
		addRecord = connection.prepareStatement("INSERT INTO questions "
				+ "(id, body, company) VALUES ( ?, ?, ? )");
	}
	public void addQuestion(Question q) throws SQLException{
		try{
			addRecord.setString(1, q.getId());
			addRecord.setString(2, q.getBody());
			addRecord.setString(3, q.getCompany());
			addRecord.executeUpdate();
		}catch(SQLIntegrityConstraintViolationException ex){
			
		}catch(Exception x){
			System.out.println("Unable to save " + q + " to database" + x.getMessage());
		}
	}
	public void finalize() throws SQLException{
		addRecord.close();
		connection.close();
	}
	//test code
	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
		// TODO Auto-generated method stub
		Question q = new Question("2", "hi", "there");
		QuestionSaver qs = new QuestionSaver("root", "");
		System.out.println("qs created");
		qs.addQuestion(q);
		System.out.println("complete");

	}

}