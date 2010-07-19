<%@ page import="org.mitre.medcafe.util.*" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%

	String patient_id = request.getParameter("patient_id");
	if (patient_id == null)
		patient_id = Constants.DEFAULT_PATIENT;
	//Just for testing purposes

	String repository = request.getParameter("repository");
	if (repository == null)
		repository = Constants.DEFAULT_REPOSITORY;
	
	String jspUrl =  "/repositories/" + repository + "/patients/" + patient_id + "/allergies";
	
	String user =  request.getRemoteUser();
	jspUrl = jspUrl + "?user=" + user;
	
	System.out.println("allergiesJSON: url " + jspUrl);
%>

<tags:IncludeRestlet relurl="<%=jspUrl%>" mediatype="json"/>
