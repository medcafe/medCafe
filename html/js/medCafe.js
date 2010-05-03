$(document).ready( function() {

		var tabSelectedId;
												
		// create the OUTER LAYOUT
		outerLayout = $("body").layout({
			west__showOverflowOnHover: true
			,	closable:				true	// pane can open & close
			,	resizable:				true	// when open, pane can be resized 
			,	slidable:				true	// when closed, pane can 'slide' open over other panes - closes on mouse-out

			
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
		iNettuts.makeSortable();
		
		//Code for Treeview
		$("#browser").treeview({
			toggle: function() {
				console.log("%s was toggled.", $(this).find(">span").text());
			}
		});
				
		//Add on treeview		
		$("#add").click(function() {
			var branches = $("<li><span class='folder'>New Sublist</span><ul>" + 
				"<li><span class='file'>Item1</span></li>" + 
				"<li><span class='file'>Item2</span></li></ul></li>").appendTo("#browser");
			$("#browser").treeview({
					add: branches
				});
		});
		//End of code for treeview
			
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
		extendWidgets();
	});
	//End of code to initialize page
	
	//Code to create widgets content
	function createWidgetContent(patientId,link, label, type ,tab_num, params, repId)
	{
	 
	 $(this).delay(200,function()
	 {
	 	//alert("medcafe createWidgetContent: type " + type);
		if (type === "Chart")	
		{					
			addChart(this, link, tab_num, patientId);
		}
		else if  (type == "Images")
		{
			addCoverflow(this, link, tab_num, patientId);
		}
		else if  (type == "Repository")
		{
			$.getScript('js/medCafe.repository.js', function()
			{
				addRepository(this, link, tab_num, label, repId);
			});
		}
		else if  (type == "Bookmarks")
		{
			$.getScript('js/medCafe.bookmarks.js', function()
			{
				addBookmarks(this, link, tab_num, label, patientId, repId);
			});
		}
		else if  (type == "Medications")
		{
			
			$.getScript('js/medCafe.medications.js', function()
			{
				addMedications(this, link, tab_num, label, patientId, repId);
			});
		}
		else if  (type == "Allergies")
		{
			if (typeof addAllergies == 'undefined') 
			{
				
				$.getScript('js/medCafe.allergies.js', function()
				{
					addAllergies(this, link, tab_num, label, patientId, repId);
				});
			}
		}
		else
		{
			addChart(this, link, tab_num, patientId);
		}					
		
		populateWidgetSettings(patientId,link, label, type ,tab_num, params, repId);
		});
	}

	function populateWidgetSettings(patientId,link, label, type ,tab_num, params, repId)
	{
		//alert("CREATE WIDGET CONTENT medcafe adding the following widget: label " + label + " " + "type " + type  + "tab order " + tab_num + " rep " + repId + " server " + link);
		
		medCafeWidget.populateExtWidgetSettings(patientId,link, label, type ,tab_num, params, repId);
	}
	
	function addChart(callObj, server, tab_num, patientId)
	{
		//alert("medCafeTabs " +  tab_num + " about to call setHasContent  set to " + true  );
			
		//alert("callObj " + callObj);
		//Delay to let the DOM refresh
		$(callObj).delay(200,function()
		{
			//alert("image server " + server);
			
			iNettuts.refresh("yellow-widget" + tab_num);
									
			$("#aaa" + tab_num).append('<iframe id="iframe'+ tab_num+ '" name="iframe'+ tab_num+ '" width="800" height="400"/>');
			$(callObj).delay(100,function()
			{
				$('#iframe'+ tab_num).attr('src', server +"?tab_num=" + tab_num + "&patient_id=" + patientId); 
			} );				
				
			//iNettuts.makeSortable();
			setHasContent(tab_num);
		} );
	}
	
	function addCoverflow(callObj, server, tab_num, patientId)
	{
		
		//Delay to let the DOM refresh
		$(callObj).delay(100,function()
		{
			iNettuts.refresh("yellow-widget" + tab_num);
									
			$("#aaa" + tab_num).append('<iframe id="iframe'+ tab_num+ '" name="iframe'+ tab_num+ '" width="800" height="400"/>');
			$(callObj).delay(100,function()
			{
				$('#iframe'+ tab_num).attr('src', server +"?tab_num=" + tab_num + "&patient_id=" + patientId); 
			} );				
				
			//iNettuts.makeSortable();
			setHasContent(tab_num);
		} );
	}	
		
	function imageAnnotate(callObj, server, tab_num)
	{
		
		//Delay to let the DOM refresh
		$(callObj).delay(100,function()
		{
			iNettuts.refresh("yellow-widget" + tab_num);
									
			var html = "<img id=\"toAnnotate\" src=\"" + server + "\" alt=\""+  server + "\" width=\"600\" height=\"398\" />";
			var jspSvr = "annotate.jsp";
			
			$("#aaa" + tab_num).append('<iframe id="annotateiframe" width="800" height="400"/>');
			$('#annotateiframe').attr('src', jspSvr); 
			
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
				
	function triggerFilter(startDate, endDate)
	{
		//alert("medCafe.js triggerFilter - start date is " + startDate + " end Date is " + endDate);
		$(document).trigger('FILTER_DATE', [startDate, endDate]);
	}
	
	function triggerFilterCategory(filterCat)
	{
		$(document).trigger('FILTER_CATEGORY', [filterCat]);
	}
	
	function displayDialog( id)
	{
		var text = $("#aaa" + id).html();
	
		$("#modalaaa" + id).append(text);
		
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
						    
						     text = $("#modalaaa" + id).html();
						     $("#aaa" + id).load($link.attr('href') + ' #content');
						     $("#modalaaa" + id).empty();
						      //Put in code to goto saveText.jsp Delete
						      $(this).dialog("destroy");
						   } 
						}
					}); 	
		
		$("#dialog" + id).dialog("open");
	}
	
	function startWidgetDrag(test, frameId, e)
	{
	  
	    var iFramePos = $('#' + frameId).position();
	    //Need to replace this with better way to determine position
	  	iFramePos.left = 1300;
	  	iFramePos.top = 170;
	  	
	  	var cloneLeft = iFramePos.left + $(test).position().left;
	  	var cloneTop = iFramePos.top + $(test).position().top;
	  	$(test).clone().appendTo('#clone');
	  	$(test).clone().remove();
	  	var height = $('#clone').height();
	  	var width = $('#clone').width();
	  	$('#clone').css( { position: "absolute",  "z-index" : "100", "left": cloneLeft + "px", "top": cloneTop + "px" } );
	  	e.pageX = cloneLeft + width/2;
	   	e.pageY = cloneTop + height/2;
	    //make draggable element draggable
	    $("#clone").draggable().trigger(e);
	    $('#clone').show();
	
	}
	  
  function clearWidgets()
  {
  	$('#clone').html("");
	$('#clone').hide();
  }
  
function displayImage(imageName)
{
	//Delay to let the DOM refresh

	 var server = "http://" + imageName ;
		
	 var imageTitle = server;
	 var pos = server.lastIndexOf("/") + 1;	
	 if (pos > 0)
	 {
	 	imageTitle = imageTitle.substring(pos, imageTitle.length);
	 
	 }
	 var tab_num = addTab(imageTitle, "Image");
	
	 var html =$.ajax({
      url: server,
      global: false,
      type: "POST",
      dataType: "html",
      success: function(msg)
      {
      	 var text = "<div id=\"content\">\n<input id=\"viewerButton" + tab_num + "\" type=\"button\" value=\"Viewer\"/>\n" +
					 "<div id=\"content\">\n<input id=\"editButton" + tab_num + "\" type=\"button\" value=\"Annotate\"/>\n" +
					"<a href=\"" + server +"\" class=\"jqzoom" + tab_num + "\" style=\"\" title=\"" + imageTitle +"\">\n" +
					"<img src=\"" + server + "\"  title=\""+ imageTitle + "\" width=\"300\" style=\"border: 1px solid #666;\">\n" +
					"</a>" + "</div>\n";
					
      	 
        var viewerText =  "\n<div id=\"viewer\" class=\"viewer\"></div>\n";
          
         var viewerFrame = "<iframe height=\"400\" width=\"680\" name=\"imageFrame" + imageTitle + "\" id=\"frame" + imageTitle+ "\" src=\"viewer.jsp?image=" + server + "\"></iframe>";
					               
         
         iNettuts.refresh("yellow-widget" + tab_num);
		 //$("#aaa" + tab_num).append("<img src='" + server+ "?image=<%=server%>' alt='"+ imageName+ "' width='400'/>");
		 $("#aaa" + tab_num).append( text );
		 
		 $(this).delay(100,function()
		 {
		 	setHasContent(tab_num);
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
		 	
			$(".jqzoom" + tab_num).jqzoom(options);
			
			$("#viewerButton" + tab_num).bind("click",{},
			function(e)
			{
				
				var tab_num = addTab(imageTitle + "Viewer","Viewer");
				
				var link = "viewer.jsp?image=" + server;
				addChart(this, link, tab_num);
			});
			
			$("#editButton" + tab_num).bind("click",{},
			function(e)
			{
				
				var tab_num = addTab(imageTitle + "Annotate", "Annotate");
				
				var link = server;
				imageAnnotate(this, link, tab_num);
			});
		  } );
		}
      }).responseText;

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