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
		        $(this).css('border-color', 'red');	
		        alert('Coordinates: ' + jsEvent.pageX + ',' + jsEvent.pageY);
		        $("#popUpMenuWrap").addClass("fg-button fg-button-icon-right ui-widget ui-state-default ui-corner-all")
		        .prepend('<a tabindex="0" href="#calendar-menu" id="cal-menu"><span class="ui-icon ui-icon-triangle-1-s"></span>Menu</a><div id="calendar-menu" class="hidden"></div>');
		 		
		 		$(this).delay(200,function()
				{
			 		$.get('menuContent.html', function(data)
			 		{
						$('#cal-menu').menu({
							width: 100,
							offsetX: 135,
							offsetY: 500,
							content: data
						});
					});
				} );
		        
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
    	
    	$("#calendar").mousedown( function(e) {
					var evt = e;
					evt.stopPropagation();
					$(this).mouseup( function(e) 
					{
						e.stopPropagation();
						var srcElement = $(this);
						$(this).unbind('mouseup');
						
						//Put the menu item here
						if( evt.button == 2 ) {
							var inSpeed = 150;
							$('#popUpMenuWrap').html("");
							
							 $("#popUpMenuWrap").addClass("fg-button fg-button-icon-right ui-widget ui-state-default ui-corner-all")
					        .prepend('<a tabindex="0" href="#calendar-menu" id="cal-menu"><span class="ui-icon ui-icon-triangle-1-s"></span>Menu</a><div id="calendar-menu" class="hidden"></div>');
					 		
					 		$(this).delay(200,function()
							{
						 		$.get('menuContent.html', function(data)
						 		{
						 			$('#popUpMenuWrap').css("position","absolute");
									$('#popUpMenuWrap').css("z-index","99");
									var d = {}, x, y;
									if( self.innerHeight ) {
										d.pageYOffset = self.pageYOffset;
										d.pageXOffset = self.pageXOffset;
										d.innerHeight = self.innerHeight;
										d.innerWidth = self.innerWidth;
									} else if( document.documentElement &&
										document.documentElement.clientHeight ) {
										d.pageYOffset = document.documentElement.scrollTop;
										d.pageXOffset = document.documentElement.scrollLeft;
										d.innerHeight = document.documentElement.clientHeight;
										d.innerWidth = document.documentElement.clientWidth;
									} else if( document.body ) {
										d.pageYOffset = document.body.scrollTop;
										d.pageXOffset = document.body.scrollLeft;
										d.innerHeight = document.body.clientHeight;
										d.innerWidth = document.body.clientWidth;
									}
									
									
									(e.pageX) ? x = e.pageX : x = e.clientX + d.scrollLeft;
									(e.pageY) ? y = e.pageY : x = e.clientY + d.scrollTop;
									
									// Show the menu
									$(document).unbind('click');
									
									var offsetX = 50;
									var offsetY = 100;
									$('#popUpMenuWrap').css({ top: y-offsetY, left: x-offsetX }).fadeIn(inSpeed);
									
									$('#cal-menu').menu({
										width: 100,
										content: data
									});
									
								});
							} );
						}
						else
						{
						 
							$('#popUpMenuWrap').removeClass("fg-button fg-button-icon-right ui-widget ui-state-default ui-corner-all").html("");
						}
					});
		});
		
		$(document).bind("contextmenu",function(e){  
         return false;  
     	});  
					
});