(function($) {
$.fn.medcafeTouch = function(options) {
    // Default thresholds & swipe functions
    var defaults = {
        threshold: {
            x: 30,
            y: 10
        },
        swipeLeft: function() {  },
        swipeRight: function() {  },
        preventDefaultEvents: true,
        addListeners: true
        
    };

    var options = $.extend(defaults, options);

	var dragObj;
	
    if (!this) return false;

    return this.each(function() {

        var me = $(this)

        // Private variables for each element
        var originalCoord = { x: 0, y: 0 }
        var finalCoord = { x: 0, y: 0 }

        // Screen touched, store the original coordinate
        function touchStart(event) {
           // console.log('Starting swipe gesture...')
            originalCoord.x = event.targetTouches[0].pageX
            originalCoord.y = event.targetTouches[0].pageY
            //console.log('Touch start..position x: ' + finalCoord.x +' y : ' + finalCoord.y);
        }

        // Store coordinates as finger is swiping
        function touchMove(event) {
        
            if (defaults.preventDefaultEvents)
                event.preventDefault();
            finalCoord.x = event.targetTouches[0].pageX // Updated X,Y coordinates
            finalCoord.y = event.targetTouches[0].pageY
            //console.log('Touch move..position x: ' + finalCoord.x +' y : ' + finalCoord.y);
            //console.log('medcafe.jswipe.js touchMove: Touch move..start');  
            //console.log('Touch move..position x: ' + finalCoord.x +' y : ' + finalCoord.y);
            $(this).css( { position: "absolute",  "z-index" : "100", "left": finalCoord.x + "px", "top": finalCoord.y + "px" } );
	  		dragObj = this;
        }

        // Done Swiping
        // Swipe should only be on X axis, ignore if swipe on Y axis
        // Calculate if the swipe was left or right
        function touchEnd(event) {
            //console.log('Ending swipe gesture...')
            //var changeY = originalCoord.y - finalCoord.y
            
            //console.log('Ending swipe gesture..position x: ' + finalCoord.x +' y : ' + finalCoord.y);
            addTab(event, finalCoord.x);
            removeEvents(event);
        }

        // Swipe was canceled
        function touchCancel(event) { 
            console.log('Canceling swipe gesture...')
           
        }

		function removeEvents(event) { 
		//This doesn't seem to actually remove the events
            console.log('medCafe.touch.js removing events...');
            this.removeEventListener("touchstart", touchStart, false);
        	this.removeEventListener("touchmove", touchMove, false);
        	this.removeEventListener("touchend", touchEnd, false);
        	this.removeEventListener("touchcancel", touchCancel, false);
        	console.log('medCafe.touch.js finished removing events...');
            
        }
        
		function addTab(event, xPos) {
        
        	var img = $(dragObj).find('img');
      		var patientId = options.patientId;
			if (img.length == 0)
			{
				console.log("this is not a draggable object");
				//This is not a droppable object
				return;
			}
			else
			{
				/*var label = $(dragObj).find('img').attr("src");
				var imgHtml = $(dragObj).find('img').html();
				var type = $(dragObj).find('img').attr("custom:type");
				var html = $(dragObj).find('img').attr("custom:html");
				var method = $(dragObj).find('img').attr("custom:method");
				var params = $(dragObj).find('img').attr("custom:params");
				var repository = $(dragObj).find('img').attr("custom:repository");
				var text = $(dragObj).find('p').text();
				var repPatientId ;
				var link = $(dragObj).find('img').attr("custom:url");
				console.log("medCafe.touch.js addTab label " + label + " imgHtml " + imgHtml + " type " + type + " html " + html + " method " + method + " patient Id " + patientId);  */
				var widgetInfo = {
			
				"patient_id" : patientId,
				"location" : "center",
				"type" : $(dragObj).find('img').attr("custom:type"),
				"name" : $(dragObj).find('p').text(),
				"clickUrl" : $(dragObj).find('img').attr("custom:url"),
				"server" : $(dragObj).find('img').attr("custom:server"),
				"tab_num": "",
				"params" : $(dragObj).find('img').attr("custom:params"),
				"column" : "",
				"script" : $(dragObj).find('img').attr("custom:script"),
				"script_file" : $(dragObj).find('img').attr("custom:script_file"),
				"template" : $(dragObj).find('img').attr("custom:template"),
				"jsonProcess" : $(dragObj).find('img').attr("custom:jsonProcess"),
				"isINettuts" : $(dragObj).find('img').attr("custom:isINettuts"),
				"collapsed" : 'false',
				"label" : $(dragObj).find('p').text(),
				"color_num" : 2
			};
			//	var tabObject = $(this).closest('.tabContent');
			$('.column').each(function()
			{	
				left = $(this).offset().left;
				right = left + $(this).width()
				if (xPos < right && xPos > left)
				{
					widgetInfo.column = $(this).attr('id').substring(6);
					widgetInfo.tab_num = $(this).closest('.tabContent').attr('id').substring(5);
					console.log("medCafe.touch.js " + widgetInfo.repository + " column " + widgetInfo.column + " tab_num "  + widgetInfo.tab_num + " clickUrl " + widgetInfo.clickUrl + " type " + widgetInfo.type + " patient Id " + widgetInfo.patient_id); 
					
					if (widgetInfo.isINettuts == false || widgetInfo.isINettuts == 'false')
						widgetInfo.tab_num = -1;
					widgetInfo.id = addWidgetNum(widgetInfo);

	

						//Tab already has content Create a new Tab
						createWidgetContent(widgetInfo, true);

      					clearWidgets();

				}
				
			});		   
			}
        }
        
        if (options.addListeners)
        {
        	 console.log("medCafe.touch.js addingListeners: ");
	        // Add gestures to all swipable areas
	        this.addEventListener("touchstart", touchStart, false);
	        this.addEventListener("touchmove", touchMove, false);
	        this.addEventListener("touchend", touchEnd, false);
	        this.addEventListener("touchcancel", touchCancel, false);
		}
    });
};
})(jQuery);

