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
        preventDefaultEvents: true
        
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
            addTab(event);
        }

        // Swipe was canceled
        function touchCancel(event) { 
            console.log('Canceling swipe gesture...')
           
        }

		function addTab(event) {
        
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
				var label = $(dragObj).find('img').attr("src");
				var imgHtml = $(dragObj).find('img').html();
				var type = $(dragObj).find('img').attr("custom:type");
				var html = $(dragObj).find('img').attr("custom:html");
				var method = $(dragObj).find('img').attr("custom:method");
				var params = $(dragObj).find('img').attr("custom:params");
				var repository = $(dragObj).find('img').attr("custom:repository");
				var text = $(dragObj).find('p').text();
				var repPatientId ;
				var link = $(dragObj).find('img').attr("custom:url");
				console.log("medCafe.touch.js addTab label " + label + " imgHtml " + imgHtml + " type " + type + " html " + html + " method " + method + " patient Id " + patientId);
            	var serverLink = "retrievePatientRepositoryAssoc.jsp?patient_id=" + patientId;
				var repPatientJSON;
				$.getJSON(serverLink,function(data)
				{		      	  	  
						repPatientJSON = data;	  
						var len = repPatientJSON.repositories.length;
						var x;
						for (x in repPatientJSON.repositories)
						{	  		
							test = repPatientJSON.repositories[x].repository;
							if (test == repository)
							{
								  repPatientId = repPatientJSON.repositories[x].id;
								  console.log("medCafe.touch.js getRepId rep id: " + repPatientId);
							}
								  		
						}
						//Tab already has content Create a new Tab
						createLink(patientId,link, text, type ,params, repository, repPatientId);
      					clearWidgets();
			   });
		   
			}
        }
        // Add gestures to all swipable areas
        this.addEventListener("touchstart", touchStart, false);
        this.addEventListener("touchmove", touchMove, false);
        this.addEventListener("touchend", touchEnd, false);
        this.addEventListener("touchcancel", touchCancel, false);

    });
};
})(jQuery);

