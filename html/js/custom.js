$(document).ready( function() {

		var tabSelectedId;
		//$('#example').dataTable( {
		//	"aaSorting": [[ 2, "desc" ]]
		//} );
												
		// create the OUTER LAYOUT
		outerLayout = $("body").layout({
			west__showOverflowOnHover: true
			,	closable:				true	// pane can open & close
			,	resizable:				true	// when open, pane can be resized 
			,	slidable:				true	// when closed, pane can 'slide' open over other panes - closes on mouse-out

			
		});

		//$('li').highlight();
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
    	
		var $tabs = $('#tabs').tabs({
		    add: function(event, ui)
		    {
		    	
		        var self = this;
		        var selfId = $(this).attr('id');
				if (ui.panel === undefined)
		        {
		        	//alert("ui panel is undefined");
		        	if (ui.tab === undefined)
		        	{
		        		alert("ui panel and tab is undefined");
						return false;
		        	}
					else
					{
						
						 count= 0;
						 $(this).find("li:has(a)").each(function(i)
						 {
						 	count = count + 1;
						 	if (!$(this).attr('id'))
						 	{
						 		//Temporary hard code this value
						 		var aObj = $(this).find('a');
			    				var href = aObj.attr('href');
			    				
			    				var hrefBase = href.split('#')[1], baseEl;
						 		$(this).attr('id',hrefBase + "-link");
						 		$(this).attr('custom:index', count);
						 		
						    }			    
		
			   			});
						
						return true;
					}
					alert("ui panel is undefined");
		        	return false;
		        }

		        var id = ui.panel.id;
			
		    	var tagObj = $("#" + selfId + " li:has(a) a[href*='" + id + "']");

		    	var li_obj = $(tagObj).parent().closest('li');

		    	$(li_obj).attr('id',id + "-link");
		    	$(li_obj).addClass("tabHeader");
				$(li_obj).prepend("<div class='close'></div>");
				
				
   				
				
		    	medCafe.initClose();
		    	
		    	var count = -1;
		    	//Reset the indexes of all the new tabs
		    	 $(this).find("li:has(a)").each(function(i)
				 {
				    var tempId = $(this).attr('id');
				    count = count + 1;

	     			var newLi = $("#" + tempId );
	     			$(newLi).attr('custom:index', count);
					
					
	   			});
					
				$(".tabHeader").hover(
				 function(){
      				tabSelectedId = $(this).attr("id");
   				});
   				
   				$(".tabHeader").droppable(
				 {
				 	drop: function()
				 	{
      					tabSelectedId = $(this).attr("id");
   					}
   				});
   				
   				
   				
	   			//

		    }
		});



  		var draggedId;
		// init the Sortables
		$(".tabs").sortable({
			connectWith:	$(".tabs")
		,	placeholder:	'tabs-placeholder'
		,	cursor:			'move'
		//	use a helper-clone that is append to 'body' so is not 'contained' by a pane
		,	helper:			function (evt, ui) {

			var newObject = $(ui).clone(true);
			return newObject.appendTo('body').show();

		}
		,	over:			function (evt, ui) {
								var
									$target_UL	= $(ui.placeholder).parent()
								,	targetWidth	= $target_UL.width()
								,	helperWidth	= ui.helper.width()
								,	padding		= parseInt( ui.helper.css('paddingLeft') )
												+ parseInt( ui.helper.css('paddingRight') )
												+ parseInt( ui.helper.css('borderLeftWidth') )
												+ parseInt( ui.helper.css('borderRightWidth') )
								;
								//if (( (helperWidth + padding) - targetWidth ) > 20)
									ui.helper
										.height('auto')
										.width( targetWidth - padding )
									;
									var id = $(ui.item);
									draggedId = $(ui.item).attr('id');
									
							}
		});


		

		$("#tabs").tabs();

		$("#accordion").accordion();
		
		$("#body")
				.tabs({change: function () {}})
				.find(".ui-tabs-nav")
				.sortable({})
			;


		$(document).bind('FILTER_DATE', function() 
		{	 
		   		 filterDate();
		});
			
		
		
		var medCafe = {
		
			closeTab : function(index) {
			
							$("#tabs").tabs("remove",index);
							var newIndex = index -1;
							if (newIndex < 0)
								newIndex = 0;
							//Cycle through the other tabs and reindex based on new order
							var count = 0;
							$("#tabs").find("li:has(a)").each(function(i)
						 	{
						 		
							 	var aObj = $(this).find('a');
				    			$(this).attr('custom:index', count);
							    var id = $(this).attr('id');''	
							    count = count + 1;	    
		
			   				});
			   				//$('#tabs').tabs('select', "#tabs-" + tab_num);
			   				$("#tabs").tabs('select', newIndex);
				},
				
			initClose : function() {
			
				$('.tabHeader').find('.close').bind("click",{},
			
						function(e){
							 var index = $(this).parent().attr('custom:index');
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
					                  medCafe.closeTab(index);
					             },
					             "No" : function() {
					                 $(this).dialog("destroy");
					              }  
					             }
					        }); 						
						    $("#dialog").dialog("open");
							
							} );},
							
				
			
		
				add : function (server, rep) 
				{
			 	//Method to cycle through all summary classes and allow for clicking to get details
			 
			 		listRepository(server, rep);
				}
	
			    ,
			    
			    clickRep : function () 
		    	{
		    		
		    		$('.repository').each(function ()
				 	{
				 		$(this).bind("click",{},
				 			function(e)
							{
								var server = $(this).find('.repList').attr("custom:server");
								var link = "http://" + server + "/treenode?relurl=/repositories&type=link";
								
								$("#listRepository").load(link);
								$(this).delay(1000,function()
								{
									medCafe.addRep();
								});
							});
							
				 	});
				 }
				 ,
				 clickChart : function (server) 
		    	{
		    		
		    		$('.chart').each(function ()
				 	{
				 		$(this).bind("click",{},
				 			function(e)
							{
								
								var patientId = $(this).text();
								var server = $(this).attr("custom:url");
								var link = server + "?patient_id=" + patientId;
								var label = "Chart " + patientId;
								var type ="chart";
								createLink(patientId, link, label, type);
								
							});
							
				 	});
				 }
				 ,
				addSlider : function () 
		    	{
		    	
			    	$('.addslider').each(function ()
					 	{
					 		$(this).bind("click",{},
					 			function(e)
								{
						    		$("#slider-range").slider({
										range: true,
										min: 0,
										max: 500,
										values: [75, 300],
										slide: function(event, ui) {
											$("#amount").val('$' + ui.values[0] + ' - $' + ui.values[1]);
										}
									});
									//$("#amount").val('$' + $("#slider-range").slider("values", 0) + ' - $' + $("#slider-range").slider("values", 1));
			    			});
			    		});
		    	}
				,
			    addRep : function () 
		    	{
		    		$("#repository").each(function ()
					{
						var repId = $(this).find('.repList').text();
				 		var server = $(this).find('.repList').attr("custom:server");
				 		var repButton = $(this).find('.repList');
				 		$(repButton).bind("click",{},
						 			
						function(e)
						{
						 		var tab_num = addTab(repId);
						 				
								//Delay to let the DOM refresh
								$(this).delay(500,function()
								{
										iNettuts.refresh("yellow-widget" + tab_num);
										
										 var serverLink = "http://" + server + "/repositories/" + repId + "/patients";
										 //alert("server link " + serverLink);
										 //Add the patient data
											
										 $("#aaa" + tab_num).load(serverLink);
										 //alert("example" + repId);
										 //Delay to let server get the results back
										 $(this).delay(10000,function()
										 {
													
											//alert( $("#example" + repId).text());
											$("#example" + repId).dataTable( {
												"aaSorting": [[ 0, "desc" ]]
											} );
											//$("#example" + patientId).dataTable();
											
											$(this).delay(1000,function()
											{
												medCafe.add(server, repId );
											} );
								         } );
											
								} );
						 			
						 } );
								
					} );
				}
		}
		
		
		$('#addButton').bind("click",{},
				
				function(e)
				{
					medCafe.add();
				}
		 );
		
		medCafe.add("127.0.0.1:8080/medcafe/c","OurVista");
		medCafe.clickRep();		
		medCafe.initClose();			
		medCafe.clickChart();
		medCafe.addSlider();
		processMenuClick("Add Tab");
		iNettuts.makeSortable();
		
			//Code for Treeview
			$("#browser").treeview({
				toggle: function() {
					console.log("%s was toggled.", $(this).find(">span").text());
				}
				});
				
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
	function renameTab(tab_num, label)
	{
		var tabObjLink = $("#tabs-" + tab_num + "-link");
		var tabObj = $("#tabs-" + tab_num);
		var aObj = $(tabObjLink).find("a");
		$(aObj).html(label);
		var idObj = $(tabObj).find(".id");
		$(idObj).attr("id", label);
	}
	
	function addTab(label)
	{
		
		//First check if tab already exists
		var tab_num = 0;	
		$('.tabs').parent().find(".tabContent").each(function(i)
		{
			var tabObj = $(this).find(".id");
			var tabId = $(tabObj).attr("id");
			if (tabId == label)
			{
				var tab_id = $(this).attr('id');
				tab_num = tab_id.split("-")[1];
				$('#tabs').tabs('select', "#tabs-" + tab_num);
				
			}
			
		});
	
		//If the tab_number is greater than 0 then it has been found already - just return	
		if (tab_num != 0) return tab_num;
	
		$('.tabs').parent().find(".tabContent").each(function(i)
		{
			tab_id = $(this).attr('id');
							 		
		});
		var curr_num = tab_id.split("-")[1];
		tab_num = curr_num*1 + 1;
							 
		var hrefBase = "tabs-" + tab_num;
													
		//Add a new Tab
		$('#tabs').tabs("add","#" + hrefBase,label);
		$("#tabs-" + tab_num).addClass('tabContent');
		//Load the widget template
		$("#tabs-" + tab_num ).load("tabs-template.jsp?tab_num=" + tab_num + "&title=" + label);
		//$("#tabs-" + tab_num).
		$('#tabs').tabs('select', "#tabs-" + tab_num);
		return tab_num;
	}
	
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
	
	function createLink(patientId, link, label, type) 
	{
		   					
		var tab_num = addTab(label);		
		createWidgetContent(patientId,link, label, type ,tab_num);
		
	}
	
	function setHasContent(tab_num)
	{
			
			var widgetObj = $("#widget-content" + tab_num);
			var hasContentObj = widgetObj.find("#hasContent");
      		var hasContent = $(hasContentObj).attr("custom:hasContent");
     		$(hasContentObj).attr("custom:hasContent",true);
     		
	}
	
	function addRepository(callObj, server, tab_num, label)
	{
	
		var repId = "OurVista";
			
		var html = "<div class=\"example" +  repId + "\"></div>"; 
		
		$(callObj).delay(200,function()
		{
			
				iNettuts.refresh("yellow-widget" + tab_num);
				
				var serverLink =  server + "c/repositories/" + repId + "/patients";
						    
				$("#aaa" + tab_num).load(serverLink);
				
				$(this).delay(10000,function()
				{
											
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
							
		} );
	}	
		
	function imageAnnotate(callObj, server, tab_num)
	{
		
		//Delay to let the DOM refresh
		$(callObj).delay(100,function()
		{
			iNettuts.refresh("yellow-widget" + tab_num);
									
			var html = "<img id=\"toAnnotate\" src=\"" + server + "\" alt=\""+  server + "\" width=\"600\" height=\"398\" />";
			
			//$("#aaa" + tab_num).append(html);
			var jspSvr = "annotate.jsp";
			
			$("#aaa" + tab_num).append('<iframe id="annotateiframe" width="800" height="400"/>');
			$('#annotateiframe').attr('src', jspSvr); 
			
			
			/*$(callObj).delay(100,function()
			{
				
				$("#toAnnotate").annotateImage({
						editable: true,
						useAjax: false,
						notes: [ { "top": 286,
								   "left": 161,
								   "width": 52,
								   "height": 37,
								   "text": "Of Interest",
								   "id": "e69213d0-2eef-40fa-a04b-0ed998f9f1f5",
								   "editable": true },
								 { "top": 134,
								   "left": 179,
								   "width": 68,
								   "height": 74,
								   "text": "What's here?",
								   "id": "e7f44ac5-bcf2-412d-b440-6dbb8b19ffbe",
								   "editable": true } ]
				});
			} );*/
			  
							
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
								var link =  server + "c/repositories/" + rep  +"/patients/" + detailId;
								//alert("server on clicked link " + link);
								$("#aaa" + tab_num).load(link);
								
								//Delay to let DOM refresh before adding table styling
								$(this).delay(500,function()
								{
									//alert( $("#example" + patientId).text());
									
									$("#example" + detailId).dataTable( {
										"aaSorting": [[ 0, "desc" ]]
									} );
									
								} );
								
							} );
						} );
				
				
					var patientId = $(this).text();
		 			var imageButton = $(this).find('.images');
		 			$(imageButton).bind("click",{},
					
						function(e)
						{
						
							var patientId = $(this).text();
							var link = $(this).attr("custom:url");
								
							var label = "Images ";
							var type ="images";
							createLink(patientId, link, label, type);
								
							
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
	/*$("#dialog" + id).dialog({
					 autoOpen: false,					
					 modal:true,
					 resizable: true,
					 title: "Editor Tab",
					 buttons : {
					    "Close" : function() {          
					     //Have to Destroy as otherwise 
					     //the Dialog will not be reinitialized on open    
					    
					     text = $("#modalaaa" + id).html();
					     alert("text: id  " + id + " is " + text);
					     $("#aaa" + id).empty();
						$("#aaa" + id).append(text);
					     $("#modalaaa" + id).empty();
					      //Put in code to goto saveText.jsp Delete
					      $(this).dialog("destroy");
					   } 
					}
				}); 			*/
				
				
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