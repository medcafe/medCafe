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
			 			listRepository(server, rep);
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

		$('.ui-layout-center').each(function()
	        {
	             this.addEventListener("touchmove", stopTouchMove, false);
	        });
		$('.ui-layout-east').each(function()
	        {
	             this.addEventListener("touchmove", stopTouchMove, false);
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
	function createWidgetContent(widgetInfo, group)
	{
	 $(this).delay(200,function()
	 {




			{
				addWidgetTab(this, widgetInfo, group);
			}
		

		
		});
	
	
	}


	function addChart(callObj, widgetInfo, imageTitle)
	{
		var server =  widgetInfo.image ;
	 	//alert("image " + imageName + " patientID " + patientId + " tab-num " + tab_num);
	 	var imageTitle = server;
	 	var pos = server.lastIndexOf("/") + 1;
	 	if (pos > 0)
	 	{
	 		imageTitle = imageTitle.substring(pos, imageTitle.length);

	 	}
		//alert("medCafeTabs " +  tab_num + " about to call setHasContent  set to " + true  );

		//alert("callObj " + callObj);
		//Delay to let the DOM refresh
	/*	$(callObj).delay(200,function()
		{
			//iNettuts.refresh("yellow-widget" + widgetInfo.id);

			$("#tabs-" + widgetInfo.tab_num).append('<iframe frameborder="0" id="iframe'+ widgetInfo.tab_num+ '" name="iframe'+ widgetInfo.tab_num+ '" width="720" height="350"/>');
			$(callObj).delay(100,function()
			{
				$('#iframe'+ widgetInfo.tab_num).attr('src', widgetInfo.clickUrl +"?tab_num=" + widgetInfo.tab_num + "&patient_id=" + widgetInfo.patient_id + "&rep_patient_id="  + widgetInfo.rep_patient_id+ "&image=" + imageTitle);

			} );  */

			//iNettuts.makeSortable();
			setHasContent(widgetInfo.tab_num);

			//Try to add a scroll
			//$("#aaa" + tab_num).jScrollTouch({height:'380',width:'800'});
			medCafeWidget.populateExtWidgetSettings(widgetInfo);
		//} );
	}

	function addCoverflow(callObj, server, tab_num, patientId, patientRepId)
	{

		//Delay to let the DOM refresh
		$(callObj).delay(100,function()
		{
			//iNettuts.refresh("yellow-widget" + tab_num);

			$("#aaa" + tab_num).append('<iframe id="iframe'+ tab_num+ '" name="iframe'+ tab_num+ '" width="800" height="400"/>');
			$(callObj).delay(100,function()
			{
				$('#iframe'+ tab_num).attr('src', server +"?tab_num=" + tab_num + "&patient_id=" + patientRepId);
			} );

			//iNettuts.makeSortable();
			setHasContent(tab_num);
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


function displayImage(callObj, widgetInfo, data)
{
	//Delay to let the DOM refresh


	 var server =  widgetInfo.image ;
	 //alert("image " + imageName + " patientID " + patientId + " tab-num " + tab_num);
	 var imageTitle = server;
	 var pos = server.lastIndexOf("/") + 1;
	 if (pos > 0)
	 {
	 	imageTitle = imageTitle.substring(pos, imageTitle.length);

	 }
//	 var fileType = "";
//	 pos = server.lastIndexOf(".") + 1;
//	 if (pos > 0)
//	 {
//	 	fileType = server.substring(pos);
//	 }
//	 
	
	//if (fileType != "pdf")
//	{
//		 if ( tab_num == -1)
//		 {
//		  	tab_num = addTab(imageTitle, "Image", false);
//		 }
	 
	  /*   var text = "<div id=\"content\">\n<input id=\"viewerButton" + tab_num + "\" type=\"button\" value=\"Viewer\"/>\n" +

					 "<div id=\"content\">\n<input id=\"editButton" + tab_num + "\" type=\"button\" value=\"Annotate\"/>\n" +
					"<a href=\"" + server +"\" class=\"jqzoom" + tab_num + "\" style=\"\" title=\"" + imageTitle +"\">\n" +
					"<img src=\"" + server + "\"  title=\""+ imageTitle + "\" width=\"300\" style=\"border: 1px solid #666;\">\n" +
					"</a>" + 
     				 "</div>\n" +
					"</div>\n";

		
         var viewerText =  "\n<div id=\"viewer\" class=\"viewer\"></div>\n";  */

        // iNettuts.refresh("yellow-widget" + tab_num);
		 //$("#aaa" + tab_num).append("<img src='" + server+ "?image=<%=server%>' alt='"+ imageName+ "' width='400'/>");
		 //alert("medCafe.js addTab text to add to aaa" + tab_num + " " + text);

		  $(this).delay(500,function()
		 {
			// $("#tabs-" + widgetInfo.tab_num).html( text );

			 $(this).delay(100,function()
			 {
			 	setHasContent(widgetInfo.tab_num);
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

				$(".jqzoom" + widgetInfo.tab_num).jqzoom(options);

				$("#viewerButton" + widgetInfo.tab_num).bind("click",{},
				function(e)
				{
				var newWidget = {
					"patient_id" : widgetInfo.patient_id,
					"rep_patient_id" : widgetInfo.rep_patient_id,
					"repository" : "local",
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
					"iNettuts" : false,
					"template" : ""

				};
					//var type= "Viewer";
					//var label = imageTitle +"Viewer";

					var newTab_num = addTab(newWidget.name+": " + imageTitle, newWidget.type, newWidget.iNettuts);
					if (newTab_num < 0)
						return;
					newWidget.tab_num = newTab_num;
					newWidget.id = addWidgetNum(newWidget);
					//var link = "viewer.jsp?tab_num=" + newTab_num + "&image=" + server;
					newWidget.clickUrl = "viewer.jsp";
					newWidget.image = server;
					createWidgetContent(newWidget);


				});

				$("#editButton" + widgetInfo.tab_num).bind("click",{},
				function(e)
				{
					var newWidget = {
						"patient_id" : widgetInfo.patient_id,
						"rep_patient_id" : widgetInfo.rep_patient_id,
						"repository" : "local",
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
						"iNettuts" : false,
						"template" : ""

					};
				//	var type= "Annotate";
				//	var label = imageTitle +"Annotate";

					var newTab_num = addTab(newWidget.name + ": " + imageTitle, newWidget.type, newWidget.iNettuts);
					newWidget.tab_num = newTab_num;
					//var link =  "annotate.jsp?tab_num=" + newTab_num + "&imageName=" + server;
					//var link = "viewerDraw.jsp?tab_num=" + newTab_num + "&image=" + imageTitle + "&patient_id=" + patientId;
				   newWidget.clickUrl = "viewerDrawFrame.jsp"
				 //	addChart(this, newWidget, imageTitle);
					createWidgetContent(newWidget);
					//createWidgetContent(patientId,link, label, type ,newTab_num, "","","");

				});


			  } );


		  });//2nd delay
/*	
	}
	else
	{    */
			/*	var widgetInfo = {
					"patient_id" : patientId,
					"rep_patient_id" : patientId,
					"repository" : "local",
					"type" : "Viewer",
					"name" : imageTitle+"Viewer",
					"server" : "",
					"tab_num": "",
					"image" : imageName,
					"column" : 1,
					"jsonProcess" : false,
					"params" : ""

				};   */
					//var type= "Viewer";
					//var label = imageTitle +"Viewer";

			
		/*			widgetInfo.id = addWidgetNum(widgetInfo);
					//var link = "viewer.jsp?tab_num=" + newTab_num + "&image=" + server;
					widgetInfo.clickUrl = "viewer.jsp";
					widgetInfo.image = server;
					createWidgetContent(widgetInfo);   */
			/*		window.open(imageName, name);

	}   */
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


function closeAllTabs(tab_num)
{

		var indexList = new Array();
		$("#tabs").find("li:has(a)").each(function(i)
		{
			indexList[i] = $(this).attr('custom:index');

		});

		//Make sure that the last tab is closed first
		for (i=indexList.length-1; i > -1 ;i--)
		{
			$("#tabs").tabs("remove",indexList[i]);
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


