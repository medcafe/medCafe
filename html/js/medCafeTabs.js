$(document).ready( function() {

		var medCafeTabs;
		var tabSelectedId;
   
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
						
		    	medCafeTabs.initClose();
		    	
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
		
		$("#body")
				.tabs({change: function () {}})
				.find(".ui-tabs-nav")
				.sortable({})
			;
		
		 medCafeTabs = {
		
			closeTab : function(index, tabNum) {
			
							$("#tabs").tabs("remove",index);
							alert("medCafeTabs closetab remove index " + index + " tabNum " + tabNum);
							triggerCloseTab(tabNum);
							
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
				}
				,
				closeAllTabs : function(tab_name) {
			
							$("#tabs").find("li:has(a)").each(function(i)
						 	{
						 		var index = $(this).attr('custom:index');
								$("#tabs").tabs("remove",index);
			   				});
			   				
				},
			initClose : function() {
			
				$('.tabHeader').find('.close').bind("click",{},
			
						function(e){
							 var index = $(this).parent().attr('custom:index');
							 var id = $(this).parent().attr('id');
							 var tab_num = id.substring(5,6);
							 
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
					                  medCafeTabs.closeTab(index, tab_num);
					             },
					             "No" : function() {
					                 $(this).dialog("destroy");
					              }  
					             }
					        }); 						
						    $("#dialog").dialog("open");
							
							} );}
	
		}
	
		medCafeTabs.initClose();
		processMenuClick("Add Tab");
	
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
	
	
	function addTab(label, type)
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
	
		//If the tab_number is greater than 0 then it has been found already - just return	-1
		if (tab_num != 0) return -1;
	
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
		$("#tabs-" + tab_num ).load("tabs-template.jsp?tab_num=" + tab_num + "&title=" + label + "&type=" + type);
		//$("#tabs-" + tab_num).
		$('#tabs').tabs('select', "#tabs-" + tab_num);
		return tab_num;
	}
	
	
	function createLink(patientId, link, label, type, params, repository) 
	{   					
		var tab_num = addTab(label, type);		
		if (tab_num < 0)
			return;
		createWidgetContent(patientId,link, label, type ,tab_num, params,repository);
		
	}
	
	function setHasContent(tab_num)
	{
			
			var widgetObj = $("#widget-content" + tab_num);
			var hasContentObj = widgetObj.find("#hasContent");
      		var hasContent = $(hasContentObj).attr("custom:hasContent");
     		$(hasContentObj).attr("custom:hasContent",true);
     		
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