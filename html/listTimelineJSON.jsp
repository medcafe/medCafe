<%@ page import="org.mitre.medcafe.util.*" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ page import="org.mitre.medcafe.util.*" %>
<%@ page import="org.mitre.medcafe.model.*" %>
<%

	String patient_id = request.getParameter(Constants.PATIENT_ID);
	if (patient_id == null)
		patient_id = "1";
		PatientCache cache = (PatientCache) session.getAttribute(PatientCache.KEY);
    if( cache == null )
    {  //nobody is logged in
        response.sendRedirect("introPage.jsp");
        return;
    }
	patient_id = cache.getDatabasePatientId();
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
