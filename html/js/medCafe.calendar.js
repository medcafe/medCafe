		var date = new Date();
		var day = date.getDate();
		var month = date.getMonth();
		var year = date.getFullYear();

	$(document).ready(function() {
		$.fn.clearForm = function() {
			return this.each(function() {
				var type = this.type, tag = this.tagName.toLowerCase();
				if (tag == 'form'){
					return $(':input',this).clearForm();
				}
				if (type == 'text' || type == 'password' || tag == 'textarea' || type == 'hidden'){
					if(!$(this).hasClass('noreset')){
						this.value = '';
					}
				}else if (type == 'checkbox' || type == 'radio'){
					if(!$(this).hasClass('noreset')){
						this.checked = false;
					}
				}else if (tag == 'select'){
					if(!$(this).hasClass('noreset')){

						this.selectedIndex = (this.length > 1 && this.options[0].value == '')? 0: -1;
					}
				}
			});
		};

		$('#calendar').fullCalendar({
			header: {
				left: 'prev,next',
				center: '',
				right: 'today'
			},
			editable: true,
			defaultView: 'agendaDay',
			height: 500,
			events: "getSchedule.jsp",
			theme: true,
			dragToCreate: true,
			dragToCreateFn: function(calEvent){
			    // alert("dragToCreate called");
					resetActivityScheduleForm();
					$('#newCalendarEventID').val(calEvent._id);
					$('#activityStartTime').val(calEvent.start.getHours()+':'+calEvent.start.getMinutes());
					$('#activityEndTime').val(calEvent.end.getHours()+':'+calEvent.end.getMinutes());
					$('#activityStartDate').val((calEvent.start.getMonth() + 1) + '/' + calEvent.start.getDate() + '/' + calEvent.start.getFullYear());
					$('#activityEndDate').val((calEvent.end.getMonth() + 1) + '/' + calEvent.end.getDate() + '/' + calEvent.end.getFullYear());
                    $('#activityScheduleDiv').dialog("open");
				},
            eventDrop: function(event, dayDelta, minuteDelta) {
            	// clearPopUp();
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
            	// clearPopUp();
				var url = "resizeAppt.jsp?id=" + event.id + "&minutes=" + minuteDelta;
                $.getJSON(url, function(json){
                    if (json.announce)
                    {
                        updateAnnouncements(json);
                        return;
                    }
                });
			},
		});
		$('#activityScheduleDiv').hide().dialog({
				width: 630,
				modal: true,
				autoOpen: false,
				resizable: false,
				bgiframe:true,
				title: 'Schedule Activity',
				buttons: {"Ok":function(){

				    var data = { appoint_date: $("input#activityStartDate").val(),
				        appoint_time: $("select#activityStartTime option:selected").val(),
                        end_time: $("select#activityEndTime option:selected").val() };
                             // alert (data);
                            $.ajax({
                              type: "POST",
                              url: "setSchedule.jsp",
                              data: data,
                              error: function( xhr, textStatus, errorThrown ) {
                                  alert("Error saving appointment.");
                                  $('#calendar').fullCalendar('removeEvents' );
                                  $('#calendar').fullCalendar('refetchEvents' );
                              },
                              complete: function(xhr, textStatus) {
                                  $('#calendar').fullCalendar('removeEvents' );
                                  $('#calendar').fullCalendar('refetchEvents' );
                              }
                            });
                            $('#activityScheduleDiv').hide().dialog('close');
                            //alert("events should have been re-fetched");
                    },
                    "Cancel": function(){
                           $(this).dialog('close');
                           if($('#newCalendarEventID').val() != ''){
                               $('#calendar_box').fullCalendar( 'removeEvents' , $('#newCalendarEventID').val() );
                               $('#newCalendarEventID').val('');
                           }
                   }}
		});
		//Create controls
		$('#activityStartDate').datepicker({
				showOn: 'button',
				buttonImage: './smoothness/images/calendar.gif',
				dateFormat: 'mm/dd/yy',
				buttonImageOnly: true
			});
		$('#activityEndDate').datepicker({
				showOn: 'button',
				buttonImage: './smoothness/images/calendar.gif',
				dateFormat: 'mm/dd/yy',
				buttonImageOnly: true
			});

        $("#west-sections").addClass("ui-accordion ui-widget ui-helper-reset")
        .find("h6")
            .addClass("ui-accordion-header ui-helper-reset ui-state-default ui-corner-top ui-corner-bottom")
            .prepend('<span class="ui-icon ui-icon-triangle-1-e"/>')
            .click(function() {
                $(this).toggleClass("ui-accordion-header-active").toggleClass("ui-state-active")
                    .toggleClass("ui-state-default").toggleClass("ui-corner-bottom")
                .find("> .ui-icon").toggleClass("ui-icon-triangle-1-e").toggleClass("ui-icon-triangle-1-s")
                .end().next().toggleClass("ui-accordion-content-active").toggleClass("ui-accordion-content-inactive");
                //$('#calendar').fullCalendar('refetchEvents' );
                return false;
            })
            .next().addClass("ui-accordion-content ui-accordion-content-inactive ui-helper-reset ui-widget-content ui-corner-bottom");

        $("#schedule").click();
        $("#west-sections").prepend('<div id="popUpMenuWrap"></div>');
	});


//reset forms
function resetActivityScheduleForm(){
	$('#activityScheduleForm').clearForm();
	var date = new Date();

	$('#activityStartDate').val((date.getMonth() + 1) + "/" + date.getDate() + '/' + date.getFullYear());
	$('#activityEndDate').val((date.getMonth() + 1) + "/" + date.getDate() + '/' + date.getFullYear());
	collapseTimes();
}

function collapseTimes(){
	if($('#activityAllDay').is(':checked')){
		$('.time').each(function(n){$(this).hide();});
	}else{
		$('.time').each(function(n){$(this).show()});
	}
}

function deleteAppt(id)
{
    var data = "id=" + id;
     $.ajax({
        type: "POST",
        url: "deleteAppt.jsp",
        data: data,
        error: function( xhr, textStatus, errorThrown ) {
            alert("Error deleting appointment.");
            $('#calendar').fullCalendar('removeEvents' );
            $('#calendar').fullCalendar('refetchEvents' );
        },
        complete: function(xhr, textStatus) {
            $('#calendar').fullCalendar('removeEvents' );
            $('#calendar').fullCalendar('refetchEvents' );
        }
    });
    $('#calendar').fullCalendar('removeEvents', id );
    $('#activityScheduleDiv').hide().dialog('close');
}

// $(function(){

// 		var date = new Date();
// 		var d = date.getDate();
// 		var m = date.getMonth();
// 		var y = date.getFullYear();


// 		$('#calendar').fullCalendar({
// 			startParam: "start",
// 			endParam: "end",
// 			eventClick: function(calEvent, jsEvent, view)
// 			{
// 		        // change the border color just for fun
// 		        clearPopUp();
// 		        $(this).css('border-color', 'red');

// 			},
// 			 dayClick: function(date, allDay, jsEvent, view)
// 			{
// 				var d = $('#calendar').fullCalendar('minTime');
// 				//alert("date is " + d);
// 				//alert("date is " + date);
// 			},
// 			eventRightClick: function(calEvent, jsEvent, view)
// 			{
// 					//view.trigger('eventRightClick', this, event, ev);
// 		        	//popUpMenu(evt,this, event);
// 		        	popUpMenu(jsEvent,view, calEvent);
// 			}
// 		});


//     	$("td").mousedown( function(e)
//     	{

// 				var evt = e;
// 				evt.stopPropagation();
// 				$(this).mouseup( function(e)
// 				{
// 					if( evt.button == 2 )
// 					{
// 						//First get the time slot clicked
// 						popUpMenu(e, this);
// 					}
// 					else
// 					{
// 						clearPopUp();
// 					}
// 				});
// 		});

// 		$(document).bind("contextmenu",function(e){
//          return false;
//      	});

// });


// function dumpProps(obj, parent) {
//    // Go through all the properties of the passed-in object
//    for (var i in obj) {
//       // if a parent (2nd parameter) was passed in, then use that to
//       // build the message. Message includes i (the object's property name)
//       // then the object's property value on a new line
//       if (parent) { var msg = parent + "." + i + "\n" + obj[i]; } else { var msg = i + "\n" + obj[i]; }
//       // Display the message. If the user clicks "OK", then continue. If they
//       // click "CANCEL" then quit this level of recursion
//       if (!confirm(msg)) { return; }
//       // If this property (i) is an object, then recursively process the object
//       if (typeof obj[i] == "object") {
//          if (parent) { dumpProps(obj[i], parent + "." + i); } else { dumpProps(obj[i], i); }
//       }
//    }
// }

// function popUpMenu(evt, obj, eventObj)
// {
//     alert("obj deconstruction");
//     dumpProps(obj);
//     // alert(eventObj);
// 		evt.stopPropagation();
// 		var srcElement = $(obj);
// 		$(obj).unbind('mouseup');

// 		//Put the menu item here
// 		if( evt.button == 2 ) {
// 			var d = $('#calendar').fullCalendar('getDate');
// 			// alert("date is " + d);
// 			var inSpeed = 150;
// 			$('#popUpMenuWrap').html("");
// 			$('#popUpMenuWrap').css("position","absolute");
// 			$('#popUpMenuWrap').css("z-index","99");

// 			var html = '<li id="add">Add</li><li id="delete">Delete</li>';
// 			$("#popUpMenuWrap").addClass("fg-button fg-button-icon-right ui-widget ui-state-default ui-corner-all")
// 			.prepend('<a tabindex="0" href="#calendar-menu" id="cal-menu">' + html + '</a><div id="calendar-menu" class="hidden"></div>');

// 		 	$("#popUpMenuWrap").find("li").addClass("menuItem");

// 		 	$(".menuItem").hover(
// 				 	function()
// 				 	{
// 						$(this).addClass("ui-state-hover");
// 		    		},
// 					function ()
// 					{
// 					    $(this).removeClass("ui-state-hover");
// 					}
//     		);

// 			var n = parseInt( $(obj).attr('class').match(/fc\-slot(\d+)/)[1] );
// 			if (n > 0)
// 			{
// 				// alert("Slot id is: " + n + ". So the start time must be " + n/2);
// 			}

// 		 	$(".menuItem").click(
// 				 	function()
// 				 	{
// 				 	    alert( "menu item " + $(this).text() + " clicked." );
// 				 		if (eventObj != null)
// 						{
// 							if ($(this).text() == "Delete")
// 							{
// 								var id = eventObj.id;
// 								alert("medcafeCalendar delete event id "  +id);
// 								var url = "deleteAppt.jsp?id=" + id ;
// 				                $.getJSON(url, function(json){
// 				                    if (json.announce)
// 				                    {
// 				                        updateAnnouncements(json);
// 				                        return;
// 				                    }
// 				                    else //Only delete of there wasn't an error
// 				                    {
// 				                    	$("#calendar").fullCalendar( 'removeEvents', id);
// 				                    }
// 				                });
// 							}
// 						}
// 						clearPopUp();
// 		    		}
//     		);
// 			var d = {}, x, y;

// 			(evt.pageX) ? x = evt.pageX : x = evt.clientX + d.scrollLeft;
// 			(evt.pageY) ? y = evt.pageY : x = evt.clientY + d.scrollTop;

// 			// Show the menu
// 			$(document).unbind('click');

// 			var offsetX = 50;
// 			var offsetY = 100;
// 			$('#popUpMenuWrap').css({ top: y-offsetY, left: x-offsetX });
// 		}
// 		else //Remove the menu item
// 		{
// 			clearPopUp();
// 		}
// }

// function clearPopUp()
// {
// 	$('#popUpMenuWrap').removeClass("fg-button fg-button-icon-right ui-widget ui-state-default ui-corner-all").html("");
// 	$("#popUpMenuWrap").find("li").removeClass("menuItem");
// }
