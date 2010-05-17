<%@ page import="org.mitre.medcafe.util.*" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%

	System.out.println("historyJSON: url start");
	String patient_id = request.getParameter(Constants.PATIENT_ID);
	if (patient_id == null)
		patient_id = Constants.DEFAULT_PATIENT;
		
	//Just for testing purposes
	String repository = request.getParameter("repository");
	if (repository == null)
		repository = "OurVista";
		
	String type = request.getParameter("type");
	if (type == null)
		type = "Personal";
	
	String jspUrl =  "/patients/" + patient_id + "/history/" + type;
	
	String user =  request.getRemoteUser();
	jspUrl = jspUrl + "?user=" + user;
	
	System.out.println("historyJSON: url " + jspUrl);
%>

<tags:IncludeRestlet relurl="<%=jspUrl%>" mediatype="json"/>
