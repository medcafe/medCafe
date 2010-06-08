<%@ page import="java.util.Arrays" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ page import="org.mitre.medcafe.util.*" %>
<%@ page import="org.mitre.medcafe.model.*" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
 "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<%
	String[] eventList =  Event.getEventList();
	
	String server = "http://" + Config.getServerUrl() ;
	String patient_id = request.getParameter(Constants.PATIENT_ID);
	
	if (patient_id == null)
		patient_id = "1";
  
	String listEvents = server + "/listTimelineJSON.jsp?" + Constants.PATIENT_ID + "=" + patient_id;
	String refreshUrl = server + "/timelineJSON.jsp?" + Constants.PATIENT_ID + "=" + patient_id;
	String[] events  = request.getParameterValues("event");
	if (events == null)
		events=new String[]{};
		
	for (String eventVal: events)
	{
		listEvents += "&event=" + eventVal;
	}
%>
<link rel='stylesheet' href='js/timeline/styles/styles.css' type='text/css' />
<link type="text/css" href="${css}/custom-theme/jquery-ui-1.7.2.custom.css" rel="stylesheet" />
  	
<script src="js/timeline/examples.js" type="text/javascript"></script>
<script type="text/javascript" src="${js}/jquery-1.3.2.js"></script>
<script type="text/javascript" src="${js}/ui.all-1.7.1.js"></script>	
<script>
 var tl;
 function onLoad() {
              
 	var eventSource = new Timeline.DefaultEventSource();
 	var d = Timeline.DateTime.parseGregorianDateTime("2008");
 	var theme = Timeline.ClassicTheme.create();
 	theme.event.bubble.width = 320;
            theme.ether.backgroundColors = [
                "#FDE5A7",
                "#E7DFD6",
                "#E8E8F4",
                "#D0D0E8"
            ];
   var bandInfos = [
     Timeline.createBandInfo({
     
     	 eventSource:    eventSource,
         date:         	 d,
         width:          "70%", 
         intervalUnit:   Timeline.DateTime.MONTH, 
         intervalPixels: 100,
         theme:          theme
     }),
     Timeline.createBandInfo({
     	 overview:       true,
     	 eventSource:    eventSource,
         date:         	 d,
         width:          "30%", 
         intervalUnit:   Timeline.DateTime.YEAR, 
         intervalPixels: 200,
         theme:          theme
     })
   ];
   bandInfos[1].syncWith = 0;
   bandInfos[1].highlight = true;
   bandInfos[1].decorators = [
                new Timeline.SpanHighlightDecorator({
                    startDate:  "Nov 14 2006 00:00:00 GMT",
                    endDate:    "Dec 05 2010 00:00:00 GMT",
                    startLabel: "First Visit",
                    endLabel:   "",
                    color:      "#FDE5A7",
                    opacity:    20,
                    theme:      theme
                })
            ];
   tl = Timeline.create(document.getElementById("my-timeline"), bandInfos);
 	var eventUrl = "<%=listEvents%>";
 	//alert("timelineJSON.jsp event url " + eventUrl);
 	$.getJSON(eventUrl, function(data)
 	{
 			//alert("data" + data);
 			//eventSource.loadJSON(data, "");
   			tl.loadJSON(eventUrl, function(json, url) {
                eventSource.loadJSON(json, url);
            });
              
   });
            
   setupFilterHighlightControls(document.getElementById("filter-controls"), tl, [0,1], theme);
     
   //Submit the form on changes to checkbox     
   $(".eventChkBox").change(function ()
	{	
		$("#eventForm").submit();	
	});
				 	 
 }

 var resizeTimerID = null;
 function onResize() {
     if (resizeTimerID == null) {
         resizeTimerID = window.setTimeout(function() {
             resizeTimerID = null;
             tl.layout();
         }, 500);
     }
 }
</script>

<meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
<script src="js/timeline/timeline-api.js" type="text/javascript"></script>
<!-- script src="http://static.simile.mit.edu/timeline/api-2.3.0/timeline-api.js?bundle=true" type="text/javascript"></script-->
</head>
<body onload="onLoad();" onresize="onResize();">
<div id="my-timeline" style="height: 150px; border: 1px solid #aaa"></div>
<div class="controls" id="filter-controls"></div>

<div id="listTimelineRepositories">
  <div class="ui-widget top-panel" id="patient_bio">
        <div class="ui-state-highlight ui-corner-all" style="padding: .7em;">
            <p>
            	<form name="eventListingForm" id="eventForm" action="<%=refreshUrl%>">
            		<% for (String eventListVal: eventList) {
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
            		<%=eventListVal%></input>
            		<% } %>
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