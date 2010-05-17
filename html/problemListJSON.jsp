<%@ page import="org.mitre.medcafe.util.*" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%

	System.out.println("problemListJSON: url start");
	String patient_id = request.getParameter(Constants.PATIENT_ID);
	if (patient_id == null)
		patient_id = Constants.DEFAULT_PATIENT;
	
	String jspUrl =  "/patients/" + patient_id + "/problem";
	
	String user =  request.getRemoteUser();
	jspUrl = jspUrl + "?user=" + user;
	
	System.out.println("problemListJSON: url " + jspUrl);
%>

<tags:IncludeRestlet relurl="<%=jspUrl%>" mediatype="json"/>
