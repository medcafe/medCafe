<%@ page import="org.mitre.medcafe.util.*" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%

	System.out.println("listAddressJSON: url start");
		
	String jspUrl ="";
		
	String patientId = request.getParameter(Constants.PATIENT_ID);
	if (patientId == null)
	{
		Object patientObj = session.getAttribute("patient");
		if (patientObj != null)
		 	patientId = patientObj.toString();
	}
	
	if (patientId != null)
	{
		jspUrl = request.getParameter("uri");
		if (jspUrl == null)
			jspUrl =  "/patients/" + patientId + "/address";  
	}
	
	
	String user =  request.getRemoteUser();
	jspUrl = jspUrl + "?user=" + user;
	
	System.out.println("listAddressJSON: url " + jspUrl);
%>

<tags:IncludeRestlet relurl="<%=jspUrl%>" mediatype="json"/>



