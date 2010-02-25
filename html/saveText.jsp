<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %><%@
    taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ page import = "java.util.*"%>
<%@ page import = "org.mitre.medcafe.util.*"%>
<%
	String formData = request.getParameter("form[info1]");
	
  	String user =  request.getRemoteUser();
  	//Use the user login to save the text
  	String patientId = request.getParameter("patient_id");
	if (patientId == null)
		patientId = "1";
		
	String title = request.getParameter("title");
	System.out.println("saveText.jsp Title : " + title ); 
  	
	TextProcesses textProcesses = new TextProcesses();	
	textProcesses.saveText(user, patientId, title, formData);
		
	response.sendRedirect("editor.jsp?patient_id=" + patientId + "&title=" + title);	
%>