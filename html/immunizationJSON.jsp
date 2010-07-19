<%@ page import="org.mitre.medcafe.util.*" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%

	System.out.println("immunizationJSON: url start");
	String patient_id = request.getParameter(Constants.PATIENT_ID);
	if (patient_id == null)
		patient_id = Constants.DEFAULT_PATIENT;
	String repository = request.getParameter("repository");
	if (repository == null)
		repository = Constants.DEFAULT_REPOSITORY;
	
	String jspUrl =  "/repositories/" + repository + "/patients/" + patient_id + "/immunizations";
	
	String user =  request.getRemoteUser();
	jspUrl = jspUrl + "?user=" + user;
	
	System.out.println("immunizationJSON: url " + jspUrl);
%>

<tags:IncludeRestlet relurl="<%=jspUrl%>" mediatype="json"/>
