<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %><%@
    taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ page import = "java.util.*"%>
<%@ page import = "java.net.URLEncoder"%>
<%@ page import = "java.io.UnsupportedEncodingException"%>
<%@ page import = "org.mitre.medcafe.util.*"%>
<%
	String formData = request.getParameter("form[info1]");
	String action = request.getParameter("action");
	
	System.out.println("SaveText.jsp: action " + action);
	
  	String user =  request.getRemoteUser();
  	//Use the user login to save the text
  	String patientId = request.getParameter("patient_id");
	if (patientId == null)
		patientId = "1";
		
	String title = request.getParameter("title");
		
	TextProcesses textProcesses = new TextProcesses();	
	if (action.equals("Save"))
		textProcesses.saveText(user, patientId, title, formData);
	else if (action.equals("Delete"))
		textProcesses.deleteText(user, patientId, title, formData);	
		
	title = URLEncoder.encode(title,"UTF-8"); 
	System.out.println("SaveText.jsp: action " + action + " patient "  + patientId + " title " + title);
	
	response.sendRedirect("editor.jsp?patient_id=" + patientId + "&title=" + title);	
%>