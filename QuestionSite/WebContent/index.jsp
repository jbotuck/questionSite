<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.List,questionSite.*,java.util.List, java.util.Properties"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<% 
	//this scriplet does the sort of thing that is usually in a control servlet
	//it has been included here mostly to make it easier to grade

	QuestionDoc qd;
	Properties props = new Properties();
	props.load(getServletContext().getResourceAsStream("/WEB-INF/properties"));
	String username = props.getProperty("username");
	String password = props.getProperty("password");
	QuestionSaver qs = new QuestionSaver(username, password);
	try{
		qd = new QuestionDoc(Integer.parseInt(request.getParameter("page")));
	}catch(NumberFormatException e){qd = new QuestionDoc();}
	List<Question> ql = qd.getQuestions();
	for (Question q: ql)
		qs.addQuestion(q);
	request.setAttribute("questions", ql);
	//would normally forward to the jsp here
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Question Site</title>
	</head>
	<body>
		<form action=".">
			Page Number <input type="text" name="page">
			<input type="submit" value="Load">
		</form>
		<ol>
			<c:forEach items="${questions}" var="q">
				<li>${q.body}</li>
			</c:forEach>
		</ol>
	</body>
</html>