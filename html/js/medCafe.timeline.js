function processTimeline(repId, patientId, patientRepId, data, type, tab_num)
{
	$('#tabs').tabs('select', "#tabs-" + tab_num);
	
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
 	var eventUrl ="listTimelineJSON.jsp?patient_id=" + patientId;
 	//"<%=listEvents%>";
 	//alert("timelineJSON.jsp event url " + eventUrl);
 	
 	//$('div#myDiv form[name="myForm"] fieldset.myField input[name="myInput"]')
 	var timeLineEvents = getTimelineEvents();
  	eventUrl = eventUrl  + timeLineEvents;
  	$.getJSON(eventUrl, function(data)
 	{		
 			tl.loadJSON(eventUrl, function(json, url) {
                eventSource.loadJSON(json, url);
            });
              
   });
            
   setupFilterHighlightControls(document.getElementById("filter-controls"), tl, [0,1], theme);
     
   //Submit the form on changes to checkbox     
   $(".eventChkBox").change(function ()
	{	
		clearTimelineEvents(tl, [0,1]);
		//$("#eventForm").submit();
		//return false;	
		var listJSON = "listTimelineJSON.jsp?";
		var eventList = getTimelineEvents();
		listJSON = listJSON + eventList;
		$.getJSON(listJSON, function(data)
		{
			tl.loadJSON(listJSON, function(json, url) {
                eventSource.loadJSON(json, url);
            });				
		});
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
 
 function getTimelineEvents()
 {
	var events = $("input:checked[name='event']");
  	var eventUrl = "";
  	for (i=0; i < events.length; i++)
  	{
  		eventUrl = eventUrl + "&event=" + events[i].value;
  	}
  	return eventUrl;
 }
 
 function clearTimelineEvents(timeline, bandIndices) {
    
    for (var i = 0; i < bandIndices.length; i++) {
        var bandIndex = bandIndices[i];
        timeline.getBand(bandIndex).getEventPainter().setFilterMatcher(null);
        timeline.getBand(bandIndex).getEventPainter().setHighlightMatcher(null);
    }
    timeline.paint();
}