<%@ page import="java.util.Arrays" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ page import="org.mitre.medcafe.util.*" %>
<%@ page import="org.mitre.medcafe.model.*" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
 "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta name = "viewport" content = "user-scalable = yes, width =device-width">
<%
	String[] eventList =  Event.getEventList();
	
	String server = "http://" + Config.getServerUrl() ;
	String patient_id = request.getParameter(Constants.PATIENT_ID);
	PatientCache cache = (PatientCache) session.getAttribute(PatientCache.KEY);
    if( cache == null )
    {  //nobody is logged in
        response.sendRedirect("introPage.jsp");
        return;
    }
	patient_id = cache.getDatabasePatientId();
	System.out.println("timelineJSON.jsp patientId " + patient_id);
	String listEvents = server + "/listTimelineJSON.jsp?" + Constants.PATIENT_ID + "=" + patient_id;
	String refreshUrl = server + "/timelineJSON.jsp?" + Constants.PATIENT_ID + "=" + patient_id;
	String[] events  = request.getParameterValues("event");
	if (events == null)
	{
		Object eventsObj = session.getAttribute("timelineEvents");
		if (eventsObj != null)
			events = (String[])eventsObj;
			
	}
	if (events == null)
		events=new String[]{};
		
	for (String eventVal: events)
	{
		listEvents += "&event=" + eventVal;
	}
%>


<script>
 var tl;
	$(function(){
              
   
 		});
</script>

</head>
<body onload="onLoad();" onresize="onResize();">
<div id="my-timeline" style="height: 150px; border: 1px solid #aaa"></div>
<br/><br/>
<div class="controls" id="filter-controls"></div>

<div id="listTimelineRepositories">
  <div class="ui-widget top-panel" id="patient_bio">
        <div class="ui-state-highlight ui-corner-all" style="padding: .7em;">
            <p>
            	<form name="eventListingForm" id="eventForm" action="#">
            		<% for (String eventListVal: eventList) {
            				if (eventListVal == "Problems"  || eventListVal == "Immunizations" || eventListVal == "Encounters")
            				{
            		%>
            			<input type="checkbox" class="eventChkBox" value=<%=eventListVal%> name="event" 
            		<%
            			boolean checked= false;
            			if (Arrays.asList(events).contains(eventListVal))
            			{
            		%>
            		checked="<%=checked%>"
            		<%}%>
            		>
            		<%=eventListVal%></input><br/>
            		<% 
            			}
            		} %>
            		<br/>
            		
         		
            	</form>
            </p>
         </div>
  </div>
</div>

<noscript>
This page uses Javascript to show you a Timeline. Please enable Javascript in your browser to see the full page. Thank you.
</noscript>
</body>
</html>
