<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %><%@
    taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ page import = "java.util.*"%>
<%@ page import = "org.mitre.medcafe.util.*"%>
<%
	String formData = request.getParameter("form[info1]");
	
  	System.out.println("Save the following text: " + formData ); 
  	DbConnection dbConn = new DbConnection();
  	String user =  request.getRemoteUser();
  	//Use the user login to save the text
  	
%>