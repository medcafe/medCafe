<%@ page import="org.mitre.medcafe.util.*" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%

	String patient_id = request.getParameter(Constants.PATIENT_ID);
	if (patient_id == null)
		patient_id = "1";
	String repository = request.getParameter("repository");
	if (repository == null)
		repository = Constants.DEFAULT_REPOSITORY;
	
	String jspUrl =  "/repositories/" + repository + "/patients/" + patient_id + "/events";
	//c/repositories/OurVista/patients/1/events
	String user =  request.getRemoteUser();
	jspUrl = jspUrl + "?user=" + user;
	
	String[] events  = request.getParameterValues("event");
	if (events == null)
		events=new String[]{};
		
	for (String eventVal: events)
	{
		jspUrl += "&event=" + eventVal;
	}
	session.setAttribute("timelineEvents", events);
	System.out.println("listTimelineJSON.jsp jspUrl " + jspUrl);
%>

<tags:IncludeRestlet relurl="<%=jspUrl%>" mediatype="json"/>
