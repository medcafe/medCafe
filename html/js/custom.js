$(document).ready( function() {

		
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
		$("#tabs1").tabs();

		$("#body")
				.tabs({change: function () {}})
				.find(".ui-tabs-nav")
				.sortable({})
			;

		
		//Remove the tab content from parent and add to new parent
		$('.tabHeader').droppable(
		{
			drop:			function (ev, ui)
		    {
		    	//Only allow this if this is a widget
				//if($(ui.draggable).hasClass('.widget-head'))
				//{
					alert("dropping ontop of this tab " +$(this).attr('id'));
				//}
			}
			,
		  	 activeClass: 'droppable-active',
		  	 hoverClass: 'droppable-hover'
		});

		
						
		//Remove the tab content from parent and add to new parent
		$('#tabs').droppable(
		{
		  	///accept: ".tabs",
		drop:			function (ev, ui)
		   {

		   		//Need to put in functionality for the widgets to be placed within tabs
				if(!$(ui.draggable).hasClass('.tabHeader'))
				{
					alert("this is not a tab header");
					return false;
				}

				
		   		var aObj = $(ui.draggable).closest('li').find('a');
			    var href = aObj.attr('href');
	  			var id =  $(ui.draggable).attr("id");

	  			var label = $(aObj).text();
	  			parentDrag = $(ui.draggable).parent().parent();
			    var parentId = parentDrag.attr('id');
			 	var divId = $(this).tabs().attr('id');
				
				var selected = $(ui.draggable).attr('custom:index');

				var hrefBase = href.split('#')[1], baseEl;
				
				var $old = $("#" + hrefBase);

		  		parentDrag.tabs('remove',selected);

				var layoutId = $('#' + divId + ' ul').attr('id');
				
				var newId = $('#' + layoutId).find('*[class^=ui-layout-content]').attr('id');
				
				var $newText = $old.appendTo('#' + layoutId);

				$(this).tabs("add","#" + hrefBase,label);
	
				$(this).tabs('select', "#" + hrefBase);
				var widgetId = $('#' + hrefBase).find("*[class^=widget]").attr('id');
				//alert('widget id ' + widgetId);
				iNettuts.refresh(widgetId);
				iNettuts.makeSortable();
				
			}

		});

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
				 
				 
				 ,initChart : function () 
		    	{
		    		
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
			
			
	
	});
	
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
	
	function createLink(patientId, link, label, type) 
	{
		   					
		
		var tab_num = addTab(label);
		
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
						} );
						
					} );
			
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
				$('#iframe'+ tab_num).attr('src', server); 
			} );					
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
		  } );
		}
      }).responseText;
   
	
	
}