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
            
    var tl = Timeline.create(document.getElementById("my-timeline"), bandInfos);
  
  	//need this to overwrite the code for bubble
	Timeline.OriginalEventPainter.prototype._showBubble = function(x, y, evt) {
	  	    //alert("in original painter _showBubble  this._params.theme " +  this._params.theme);
	  	    var div = document.createElement("div");
		    var themeBubble = this._params.theme.event.bubble;
		    //evt.fillInfoBubble(div, this._params.theme, this._band.getLabeller());
		    //Method to be used to create code to bring up the details inside of medCafe
		    			//		 alert("check icons"); 
		    fillInfoBubbleCustom(evt, div, this._params.theme, this._band.getLabeller(),patientId,type);
		    var link = evt.getLink();
		    //Rewrite the link - so that 
		    SimileAjax.WindowManager.cancelPopups();
		    SimileAjax.Graphics.createBubbleForContentAndPoint(div, x, y,
	        themeBubble.width, null, themeBubble.maxHeight);
	};
	
 	var eventUrl ="listTimelineJSON.jsp?patient_id=" + patientId;
 	//"<%=listEvents%>";
 	
 	//$('div#myDiv form[name="myForm"] fieldset.myField input[name="myInput"]')
 	var timeLineEvents = getTimelineEvents();
  	eventUrl = eventUrl  + timeLineEvents;
  	$.getJSON(eventUrl, function(data)
 	{		
 			tl.loadJSON(eventUrl, function(json, url) {
                eventSource.loadJSON(json, url);
            });
              
   });
   
  
   
       
   //Submit the form on changes to checkbox     
   $(".eventChkBox").change(function ()
	{	
		//clearTimelineEvents(tl, [0,1]);
		//$("#eventForm").submit();
		//return false;	
		eventSource.clear();
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

//this is the code to be overriden to bring up the event inside of medCafe
//See Timeline.DefaultEventSource.Event in sources.js for more detail on evt object
function fillInfoBubbleCustom(evt, elmt, theme, labeller, patientId, type)
{
		var doc = elmt.ownerDocument;
       
        var title = evt.getText();
        var link = evt.getLink();
        var image = evt.getImage();
        var nodetype = evt.getProperty("type");
   
        
        //use the nodetype to determine what to do
        
        if (nodetype == "Images")
        {
	        if (image != null) {
	        
	        	//This is the code to display an Image in medCafe - may put this on the link click instead
	        	displayImage(image, patientId, -1);
	        	
	            var img = doc.createElement("img");
	            img.src = image;
	            
	            theme.event.bubble.imageStyler(img);
	            elmt.appendChild(img);
	        }
        }
        else if (nodetype == "Visits")
        {
        	//Put in code here to bring up visit detail data
        	var tab_num = addTab(imageTitle, "Visit " + title);
        	
        	//call createWidgetContent(patientId,link, label, type ,tab_num, params, repId, patientRepId)
			
        }
        else if (nodetype == "Immunizations")
        {
        	//Put in code here to bring up visit detail data
        //	var tab_num = addTab(imageTitle, "Immunization " + title);
        	
        	//call createWidgetContent(patientId,link, label, type ,tab_num, params, repId, patientRepId)
        }
        else if (nodetype == "Records")
        {
        }
        else if (nodetype == "Symptoms")
        {
        }
        else if (nodetype == "Problems")
        {
        }
        else if (nodetype == "Hospital")
        {
        }
        var divTitle = doc.createElement("div");
        var textTitle = doc.createTextNode(title);
        if (link != null) {
            var a = doc.createElement("a");
            a.href = link;
            a.appendChild(textTitle);
            divTitle.appendChild(a);
        } else {
            divTitle.appendChild(textTitle);
        }

        theme.event.bubble.titleStyler(divTitle);
        elmt.appendChild(divTitle);
        
        var divBody = doc.createElement("div");
        evt.fillDescription(divBody);
        theme.event.bubble.bodyStyler(divBody);
        elmt.appendChild(divBody);
        
        var divTime = doc.createElement("div");
        evt.fillTime(divTime, labeller);
        theme.event.bubble.timeStyler(divTime);
        elmt.appendChild(divTime);
        
        var divWiki = doc.createElement("div");
        evt.fillWikiInfo(divWiki);
        theme.event.bubble.wikiStyler(divWiki);
        elmt.appendChild(divWiki);
    }
