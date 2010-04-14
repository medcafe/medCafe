$(function(){
	
		var date = new Date();
		var d = date.getDate();
		var m = date.getMonth();
		var y = date.getFullYear();


		$('#calendar').fullCalendar({
			header: {
				left: 'prev,next',
				center: '',
				right: 'today'
			},
            defaultView:'agendaDay',
            height: 500,
            theme:true,
			editable: true,
			events: "getSchedule.jsp",
			startParam: "start",
			endParam: "end",
            eventDrop: function(event, dayDelta, minuteDelta) {
            	clearPopUp();
				var url = "moveAppt.jsp?id=" + event.id + "&minutes=" + minuteDelta;
                $.getJSON(url, function(json){
               
                    if (json.announce)
                    {
                        updateAnnouncements(json);
                        return;
                    }
                });
			},
            eventResize: function(event, dayDelta, minuteDelta) {
            	clearPopUp();
				var url = "resizeAppt.jsp?id=" + event.id + "&minutes=" + minuteDelta;
                $.getJSON(url, function(json){
                    if (json.announce)
                    {
                        updateAnnouncements(json);
                        return;
                    }
                });
			},
			eventClick: function(calEvent, jsEvent, view) 
			{
		        // change the border color just for fun
		        clearPopUp();
		        $(this).css('border-color', 'red');	

			},
			 dayClick: function(date, allDay, jsEvent, view) 
			{    
				var d = $('#calendar').fullCalendar('minTime');	
				//alert("date is " + d);
				//alert("date is " + date);
			},
			eventRightClick: function(calEvent, jsEvent, view) 
			{
					//view.trigger('eventRightClick', this, event, ev);
		        	//popUpMenu(evt,this, event);
		        	popUpMenu(jsEvent,view, calEvent);    
			}
		});

		
			
        $("#west-sections").addClass("ui-accordion ui-widget ui-helper-reset")
            .find("h6")
                .addClass("ui-accordion-header ui-helper-reset ui-state-default ui-corner-top ui-corner-bottom")
                .prepend('<span class="ui-icon ui-icon-triangle-1-e"/>')
                .click(function() {
                	
                    $(this).toggleClass("ui-accordion-header-active").toggleClass("ui-state-active")
                        .toggleClass("ui-state-default").toggleClass("ui-corner-bottom")
                    .find("> .ui-icon").toggleClass("ui-icon-triangle-1-e").toggleClass("ui-icon-triangle-1-s")
                    .end().next().toggleClass("ui-accordion-content-active").toggle();
                    $('#calendar').fullCalendar('refetchEvents' );
                    return false;
                })
                .next().addClass("ui-accordion-content ui-helper-reset ui-widget-content ui-corner-bottom").hide();
        $("#patient-search").click();
		$("#west-sections").prepend('<div id="popUpMenuWrap"></div>');
    	
    	$("td").mousedown( function(e) 
    	{
		
				var evt = e;
				evt.stopPropagation();
				$(this).mouseup( function(e) 
				{
					if( evt.button == 2 ) 
					{
						//First get the time slot clicked						
						popUpMenu(e, this);
					}
					else
					{
						clearPopUp();
					}
				});
		});
    	
		$(document).bind("contextmenu",function(e){  
         return false;  
     	});  
					
});

function popUpMenu(evt, obj, eventObj)
{
		evt.stopPropagation();
		var srcElement = $(obj);
		$(obj).unbind('mouseup');
						
		//Put the menu item here
		if( evt.button == 2 ) {
			var d = $('#calendar').fullCalendar('getDate');	
			alert("date is " + d);
			var inSpeed = 150;
			$('#popUpMenuWrap').html("");
			$('#popUpMenuWrap').css("position","absolute");
			$('#popUpMenuWrap').css("z-index","99");
									
			var html = '<li id="add">Add</li><li id="delete">Delete</li>';
			$("#popUpMenuWrap").addClass("fg-button fg-button-icon-right ui-widget ui-state-default ui-corner-all")
			.prepend('<a tabindex="0" href="#calendar-menu" id="cal-menu">' + html + '</a><div id="calendar-menu" class="hidden"></div>');
		 	
		 	$("#popUpMenuWrap").find("li").addClass("menuItem");
		 	
		 	$(".menuItem").hover(
				 	function()
				 	{
						$(this).addClass("ui-state-hover");
		    		},
					function () 
					{
					    $(this).removeClass("ui-state-hover");
					}	
    		);
		 	
			var n = parseInt( $(obj).attr('class').match(/fc\-slot(\d+)/)[1] );
			if (n > 0)
			{
				
			}
			
		 	$(".menuItem").click(
				 	function()
				 	{
				 		
				 		if (eventObj != null)
						{
							if ($(this).text() == "Delete")
							{
								var id = eventObj.id;				
								alert("medcafeCalendar delete event id "  +id);
								var url = "deleteAppt.jsp?id=" + id ;
				                $.getJSON(url, function(json){
				                    if (json.announce)
				                    {
				                        updateAnnouncements(json);
				                        return;
				                    }
				                    else //Only delete of there wasn't an error
				                    {
				                    	$("#calendar").fullCalendar( 'removeEvents', id);	
				                    }		
				                });
							}
						}			
						clearPopUp();
		    		}	
    		);
			var d = {}, x, y;
	
			(evt.pageX) ? x = evt.pageX : x = evt.clientX + d.scrollLeft;
			(evt.pageY) ? y = evt.pageY : x = evt.clientY + d.scrollTop;
									
			// Show the menu
			$(document).unbind('click');
									
			var offsetX = 50;
			var offsetY = 100;
			$('#popUpMenuWrap').css({ top: y-offsetY, left: x-offsetX });
			
			
		}
		else //Remove the menu item
		{
						 
			clearPopUp();
		}
		
}

function clearPopUp()
{

	$('#popUpMenuWrap').removeClass("fg-button fg-button-icon-right ui-widget ui-state-default ui-corner-all").html("");
	$("#popUpMenuWrap").find("li").removeClass("menuItem");
}