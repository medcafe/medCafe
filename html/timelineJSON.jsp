<%@ page import="org.mitre.medcafe.util.*" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
 "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<%
	String server = "http://" + Config.getServerUrl() ;
	String patient_id = request.getParameter(Constants.PATIENT_ID);
	
	if (patient_id == null)
		patient_id = "1";
  
	String listEvents = server + "/listTimelineJSON.jsp?" + Constants.PATIENT_ID + "=" + patient_id;
	
%>
<link rel='stylesheet' href='js/timeline/styles/styles.css' type='text/css' />
<script src="js/timeline/examples.js" type="text/javascript"></script>
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
 	$.getJSON(eventUrl, function(data)
 	{
 			//alert("data" + data);
 			//eventSource.loadJSON(data, "");
   			tl.loadJSON(eventUrl, function(json, url) {
                eventSource.loadJSON(json, url);
            });
              
   });
            
   setupFilterHighlightControls(document.getElementById("filter-controls"), tl, [0,1], theme);
           
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
<noscript>
This page uses Javascript to show you a Timeline. Please enable Javascript in your browser to see the full page. Thank you.
</noscript>
</body>
</html>