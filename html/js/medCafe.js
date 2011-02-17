var addListeners = true;
$(document).ready( function() {
		var tabSelectedId;

	    var isiPad = navigator.userAgent.match(/iPad/i) != null;

		// create the OUTER LAYOUT
        outerLayout = $("body").layout({
            west: { closable: true, resizable: true, slidable: true, showOverflowOnHover: true },
            north: { size:120, resizable: true, slidable: true, showOverflowOnHover: true },
            south: { size: 600, initClosed: true, slideTrigger_open: "click" }
		});

		//Initialize dialogs for pop ups
 		$('#dialog').dialog();
 		$('#dialog').dialog('destroy');

 		//Make sure that any cloned draggable objects disappear when dragging ends
 		$("#clone").draggable({

			iframeFix : true,
			stop: function()
			{
				$(this).html("");
				$(this).hide();
			}

    	});

    	//Initialize Accordion with second accordion open
    	$("#accordion").accordion({
       		active: 1,
       		collapsible: true,
       		autoHeight: false
   		});
  		var draggedId;

		//Set up listener for Filter Date
		$(document).bind('FILTER_DATE', function()
		{
		   		 filterDate();
		});

		$(document).bind('FILTER', function()
		{
		   		 filterCategory();
		});

		$('#addTabBtn').click(function()
		{
			var tab_num = addTab("new","chart", true);

			//Make sure that tab is refreshed to add relevant scripts/ events
			//iNettuts.refresh("yellow-widget" + tab_num);
			//iNettuts.makeSortable();
		});

		var medCafe = {

				add : function (server, rep)
				{
			 	//Method to cycle through all summary classes and allow for clicking to get details

			 		$.getScript('js/medCafe.repository.js', function()
					{
						var widgetInf = { type: "Repository", params: "repository:"+ rep};
			 			listRepository(widgetInf);
			 		});
				}
		}

		medCafe.add("127.0.0.1:8080/medcafe/c","OurVista");
   		// alert("in medCafe.js onload(): break1");
		//iNettuts.makeSortable();
   		// alert("in medCafe.js onload(): break2");

		$("body").draggable({

			containment: 'window',
			iframeFix : true

    	});

		$('.fg-button').hover(
    		function(){ $(this).removeClass('ui-state-default').addClass('ui-state-focus'); },
    		function(){ $(this).removeClass('ui-state-focus').addClass('ui-state-default'); }
    	);

		$.get('menuContent.html', function(data){
			$('#flat').menu({
				content: data
			});
		});

		$.get('templateMenuContent.jsp', function(data){
			$('#template').menu({
				content: data
			});
		});
		
		$('#prefs').click(function () {
			
			processMenuClick("Preferences",1);
        });
            
		
		var isiPad = navigator.userAgent.match(/iPad/i) != null;
		$('.ui-layout-center').each(function()
	        {
	        	
		 		if (isiPad)
		 		{
					 this.addEventListener("touchmove", stopTouchMove, false);
				}
	            
	        });
		$('.ui-layout-east').each(function()
	        {
	           
		 		if (isiPad)
		 		{
					 this.addEventListener("touchmove", stopTouchMove, false);
				}
			});

		extendWidgets();
	});
	//End of code to initialize page

	function stopTouchMove(event)
	{
	   var isiPad = navigator.userAgent.match(/iPad/i) != null;
	   if (isiPad)
	   {
	     event.preventDefault();
	   }
	}
	//Code to create widgets content
	function createWidgetContent(widgetInfo, group, tab_set)
	{
	
		//this might not be set for case of drag/ drop
		if (tab_set == undefined) tab_set = "tabs";
		
	 $(this).delay(200,function()
	 {

			{
				addWidgetTab(this, widgetInfo, group, tab_set);
			}
		

		
		});
	
	
	}


	function addChart(callObj, widgetInfo, data, tab_set)
	{
		if (tab_set === undefined)
	{
		tab_set ="tabs";
	}
	var tab_key = tab_set + "-";
		var server =  widgetInfo.image ;
	 	//alert("image " + imageName + " patientID " + patientId + " tab-num " + tab_num);
	 	var imageTitle = server;
	 	var pos = server.lastIndexOf("/") + 1;
	 	if (pos > 0)
	 	{
	 		imageTitle = imageTitle.substring(pos, imageTitle.length);

	 	}

			medCafeWidget.populateExtWidgetSettings(widgetInfo);

	}

	function addCoverflow(callObj, server, tab_num, patientId, patientRepId)
	{

		//Delay to let the DOM refresh
		$(callObj).delay(100,function()
		{


			$("#aaa" + tab_num).append('<iframe id="iframe'+ tab_num+ '" name="iframe'+ tab_num+ '" width="800" height="400"/>');
			$(callObj).delay(100,function()
			{
				$('#iframe'+ tab_num).attr('src', server +"?tab_num=" + tab_num + "&patient_id=" + patientRepId);
			} );


		} );
	}

	function filterDate()
	{
		   //alert("Filter Date");
	}

	function filterCategory()
	{
		   //alert("Filter Date");
	}

	function triggerFilter(startDate, endDate, filterCat)
	{
		//alert("medCafe.js triggerFilter - start date is " + startDate + " end Date is " + endDate);
		$(document).trigger('FILTER_DATE', [startDate, endDate, filterCat]);
	}

	function triggerFilterCategory(filterCat)
	{
		$(document).trigger('FILTER_CATEGORY', [filterCat]);
	}

	function triggerFilterAll(startDate, endDate, filterCat)
	{
		$(document).trigger('FILTER_ALL', [startDate, endDate, filterCat]);
	}

	function triggerCloseTab(tabNum)
	{
		$(document).trigger('CLOSE_TAB', [tabNum]);
	}

	function displayDialog( id)
	{
		var text = $("#aaa" + id).html();

		$("#modalaaa" + id).html(text);

			$('#modalaaa' + id).find('a.collapse').toggle(function () {
						  $(this).text("Less");
                    $(this).css({backgroundPosition: '-52px 0'});
                     $(this).parent().find("#add_repos" +id).show();
                    return false;
                },function () {
                		$(this).text("More");
                      $(this).css({backgroundPosition: ''});
                     $(this).parent().find("#add_repos" +id).hide();
                    return false;
                });
                
         
                if ($('#modalaaa' + id).find('a.collapse').text() == "Less")
                
                	$('#modalaaa'+id).find('a.collapse').click();
       
		var $link = $('#aaa' + id);
		//Fill the screen
		var marginHDialog = 25; marginWDialog  = 25;
		marginHDialog = $(window).height()-marginHDialog;
		var marginWDialog = $(window.body).width()-marginWDialog;

		$("#dialog" + id).load($link.attr('href') + ' #content')
				.dialog({
						 autoOpen: false,
						 modal:true,
						 resizable: true,
						 title: "Editor Tab",
						 height: marginHDialog,
						 width: marginWDialog,
						 minWidth: 800,
						 buttons : {
						    "Close" : function() {
						     //Have to Destroy as otherwise
						     //the Dialog will not be reinitialized on open

						  //   text = $("#modalaaa" + id).html();
						     //$("#modalaaa" + id).load($link.attr('href') + ' #content');
						     //	$('#aaa' + id).empty();
						    //	 $("#aaa" + id).append(text);
						   //  $("#modalaaa" + id).empty();

						      //Put in code to goto saveText.jsp Delete
						      $(this).dialog("destroy");
						   }
						},
						close: function() {
                    		 $(this).dialog("destroy");			
						     
              			}
					});

		$("#dialog" + id).dialog("open");
	}

	function startWidgetDrag(test, frameId, isiPad, e)
	{

		if (isiPad)
		{
			//console.log('medCafe: startWidgetDrag : start isiPad ' + isiPad + " for patient " +patient_id) ;
		}

	    var iFramePos = $('#' + frameId).position();
	    //Need to replace this with better way to determine position


		if (isiPad)
		{
			iFramePos.left = 790;
			iFramePos.top = 200;
		}
		else
		{
			iFramePos.left = 1300;
	  		iFramePos.top = 170;
		}
	  	var cloneLeft = iFramePos.left + $(test).position().left;
	  	var cloneTop = iFramePos.top + $(test).position().top;
	  	$(test).clone().appendTo('#clone');
	  	$(test).clone().remove();
	  	var height = $('#clone').height();
	  	var width = $('#clone').width();
	  	//console.log('medCafe: startWidgetDrag : in here ' );

	  	$('#clone').css( { position: "absolute",  "z-index" : "100", "left": cloneLeft + "px", "top": cloneTop + "px" } );
	  	e.pageX = cloneLeft + width/2;
	   	e.pageY = cloneTop + height/2;
	    //make draggable element draggable
	    if (isiPad)
		{
			//console.log('medCafe.js startWidgetDrag  This is an iPad about to bind touch move ');
			$('#clone').show();
			$('#clone').medcafeTouch({addListeners : addListeners});
			addListeners = false;
		}
		else
		{
		    $("#clone").draggable().trigger(e);
		    $('#clone').show();
	    }


	}

  function clearWidgets()
  {
  	$('#clone').html("");
	$('#clone').hide();
  }


function displayImage(callObj, widgetInfo, data, tab_set)
{
	//Delay to let the DOM refresh
	if (tab_set == null)
		tab_set = "tabs";

	 var server =  widgetInfo.image ;
	 //alert("image " + imageName + " patientID " + patientId + " tab-num " + tab_num);
	 var imageTitle = server;
	 var pos = server.lastIndexOf("/") + 1;
	 if (pos > 0)
	 {
	 	imageTitle = imageTitle.substring(pos, imageTitle.length);

	 }


		  $(this).delay(500,function()
		 {
			// $("#tabs-" + widgetInfo.tab_num).html( text );

			 $(this).delay(100,function()
			 {
			 	//setHasContent(widgetInfo.tab_num);
				//Code for zoom
			 	var options =
	            {
	                zoomWidth: 300,
	                zoomHeight: 200,
	                position : 'right',
	                yOffset :100,
	                xOffset :100,
	                title :false

	            }

				$("."+tab_set + "jqzoom" + widgetInfo.id).jqzoom(options);

				$("#" + tab_set + "viewerButton" + widgetInfo.id).bind("click",{},
				function(e)
				{
				var newWidget = {
					"patient_id" : widgetInfo.patient_id,
					"type" : "Viewer",
					"name" : "Viewer",
					"server" : "",
					"tab_num": "",
					"image" : widgetInfo.image,
					"column" : 1,
					"jsonProcess" : false,
					"script_file" : "medCafe.viewer.js",
					"script" : "processViewerImages",
					"params" : "", 
					"isINettuts" : false,
					"template" : "",
					"nocache" : "false"
	

				};
					//var type= "Viewer";
					//var label = imageTitle +"Viewer";

					var newTab_num = addTab(newWidget.name+": " + imageTitle, newWidget.type, newWidget.isINettuts);
					if (newTab_num < 0)
						return;
					newWidget.tab_num = newTab_num;
					newWidget.id = addWidgetNum(newWidget);
					//var link = "viewer.jsp?tab_num=" + newTab_num + "&image=" + server;
					newWidget.clickUrl = "viewer.jsp";
					newWidget.image = server;
					createWidgetContent(newWidget, false, tab_set);


				});

				$("#" + tab_set + "editButton" + widgetInfo.id).bind("click",{},
				function(e)
				{
					var newWidget = {
						"patient_id" : widgetInfo.patient_id,
						"type" : "Annotate",
						"name" : "Annotate", 
						"server" : "",
						"tab_num": "",
						"column" : 1,
						"image" : widgetInfo.image,
						"jsonProcess" : false,
						"script_file" : "medCafe.js",
					   "script" : "addChart",
						"params" : "",
						"isINettuts" : false,
						"template" : ""


					};


					var newTab_num = addTab(newWidget.name + ": " + imageTitle, newWidget.type, newWidget.isINettuts);
					newWidget.tab_num = newTab_num;

					newWidget.id = addWidgetNum(newWidget);
				   newWidget.clickUrl = "viewerDrawFrame.jsp"

					createWidgetContent(newWidget, false, tab_set);


				});


			  } );


		  });

}

function initClose()
{
	 $("#dialog").dialog({
		autoOpen: false,
		modal:true,
		resizable: true,
		title: "Close Tab",
		buttons : {
			"Yes" : function() {
			//Have to Destroy as otherwise
			//the Dialog will not be reinitialized on open
			$(this).dialog("destroy");
					closeAllTabs("tabs");
			},
			"No" : function() {
				$(this).dialog("destroy");
			}
		}
	});
	$("#dialog").dialog("open");

}


function closeAllTabs(tab_name)
{

		var indexList = new Array();
		$("#" + tab_name).find("li:has(a)").each(function(i)
		{
			indexList[i] = $(this).attr('custom:index');

		});

		//Make sure that the last tab is closed first
		for (i=indexList.length-1; i > -1 ;i--)
		{
			$("#" + tab_name).tabs("remove",indexList[i]);
		}


}

function updateAnnouncements(data)
{

    if(data.announce)
    {
    	/*$.each(data.announce, function(i, item){
            alert("item " + i + " value " + item);
        });*/
    	var html = v2js_announcements(data);
        $('#announcements').html(html);
    }
    else
    {
        $('#announcements').html("");
    }
}

//Given a JSON Object with all the listings of assoc repositories
//Initialize a JSON Object holding all information about patients and the corresponding
//associated id in the host repository

function getAssocPatientRepositories(patientId)
{
	var serverLink = "retrievePatientRepositoryAssoc.jsp?patient_id=" + patientId;
	var repPatientJSON;
	$.getJSON(serverLink,function(data)
	{
			repPatientJSON = data;
	});
	return repPatientJSON;
}


