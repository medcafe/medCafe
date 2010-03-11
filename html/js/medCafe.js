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

		var medCafe = {
		
				add : function (server, rep) 
				{
			 	//Method to cycle through all summary classes and allow for clicking to get details
			 
			 		listRepository(server, rep);
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
	
	});
	//End of code to initialize page
	
	//Code to create widgets content
	function createWidgetContent(patientId,link, label, type ,tab_num)
	{
		if (type === "chart")	
		{					
			addChart(this, link, tab_num);
		}
		else if  (type == "repository")
		{
			
			addRepository(this, link, tab_num, label)
		}
		else
		{
			addChart(this, link, tab_num);
		}					
	}
	
	function addRepository(callObj, server, tab_num, label)
	{
	
		var repId = "OurVista";
			
		var html = "<div class=\"example" +  repId + "\"></div>"; 
		$(callObj).delay(200,function()
		{
			
				iNettuts.refresh("yellow-widget" + tab_num);
				
				var serverLink =  server + "repository-listJSON.jsp?repository=" + repId;
				
				$.getJSON(serverLink, function(data)
				{
						var html = v2js_listPatientsTable( data );  	  					
										
						$("#aaa" + tab_num).append(html);
	  										
						//alert( $("#example" + repId).text());
						$("#example" + repId).dataTable( {
								"aaSorting": [[ 0, "desc" ]]
						} );
											//$("#example" + patientId).dataTable();
									
						$(this).delay(1000,function()
						{
							listRepository(server, repId );
							iNettuts.makeSortable();
							
						} );
						
						setHasContent(tab_num);
						
					} );
					setHasContent(tab_num);
		});
		
	}
		
	function addChart(callObj, server, tab_num)
	{
		//alert("callObj " + callObj);
		//Delay to let the DOM refresh
		$(callObj).delay(200,function()
		{
			//alert("image server " + server);
			
			iNettuts.refresh("yellow-widget" + tab_num);
									
			$("#aaa" + tab_num).append('<iframe id="iframe'+ tab_num+ '" name="iframe'+ tab_num+ '" width="800" height="400"/>');
			$(callObj).delay(100,function()
			{
				$('#iframe'+ tab_num).attr('src', server +"?tab_num=" + tab_num); 
			} );				
				
			iNettuts.makeSortable();
			
			setHasContent(tab_num);
		} );
	}
	
	function addCoverflow(callObj, server, tab_num)
	{
		
		//Delay to let the DOM refresh
		$(callObj).delay(100,function()
		{
			iNettuts.refresh("yellow-widget" + tab_num);
									
			$("#aaa" + tab_num).append('<iframe id="chartsiframe" width="800" height="400"/>');
			$('#chartsiframe').attr('src', server); 
			
				iNettuts.makeSortable();
							
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
			
				
	function triggerFilter(startDate, endDate)
	{
		//alert("custom.js triggerFiler - start date is " + startDate + " end Date is " + endDate);
		$(document).trigger('FILTER_DATE', [startDate, endDate]);
	}
	
	function listRepository(server, rep)
	{
					$('.summary').each(function ()
				 	{
				 		var detailId = $(this).text();
				 		
				 		var detailButton = $(this).find('.details');
			 			$(detailButton).bind("click",{},
						
							function(e)
							{
							
								//First check if the current detail tab exists
								//Then put focus on this tab
								if ($("#example" + detailId).attr('id') )
								{
									//Find closest tab
									
									var test = $("#example" + detailId).parent().parent().closest('.tabContent');
									var tabId = test.attr('id');
									
									$('#tabs').tabs('select', '#' + tabId);
									return false;
								}
								
								var tab_num = addTab(detailId);
								//Delay to let the DOM refresh
								$(this).delay(500,function()
								{
									iNettuts.refresh("yellow-widget" + tab_num);
								
									//Add the patient data
									var link =  server + "repository-listJSON.jsp?repository=" + rep  +"&patient_id="  + detailId;
										
									$.getJSON(link, function(data)
									{
										var html = v2js_listPatientTable( data );  	  			
	  									$("#aaa" + tab_num).append(html);
	  									
										//Delay to let DOM refresh before adding table styling
										$(this).delay(500,function()
										{
											//alert( $("#example" + patientId).text());
											
											$("#example" + detailId).dataTable( {
												"aaSorting": [[ 0, "desc" ]]
											} );
											setHasContent(tab_num);
										} );
									});
									
								} );
							} );
								
		    	    });
		    
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
	 var tab_num = addTab(imageTitle);
	
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
				
				var tab_num = addTab(imageTitle + "Viewer");
				
				var link = "viewer.jsp?image=" + server;
				addChart(this, link, tab_num);
			});
			
			$("#editButton" + tab_num).bind("click",{},
			function(e)
			{
				
				var tab_num = addTab(imageTitle + "Annotate");
				
				var link = server;
				imageAnnotate(this, link, tab_num);
			});
		  } );
		}
      }).responseText;

}