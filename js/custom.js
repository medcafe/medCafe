$(document).ready( function() {


		
		// create the OUTER LAYOUT
		outerLayout = $("body").layout({
			
		});

		var $tabs = $('#tabs').tabs({
		    add: function(event, ui) 
		    {
		        var self = this; 
		        var selfId = $(this).attr('id');
		        
		        if (ui.panel === undefined)
		        {
		        	alert("ui panel is undefined");
		        	if (ui.tab === undefined)
		        	{
		        		alert("ui tab is also undefined");
		        		
		        	}
		        	
		        	return false;
		        }
		        
		        var id = ui.panel.id;
		        
		    	var tagObj = $("#" + selfId + " li:has(a) a[href*='" + id + "']"); 
	
		    	var li_obj = $(tagObj).parent().closest('li');
				
		    	$(li_obj).attr('id',id + "-link");
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
		
		var $tabs = $('#tabs1').tabs({
		    add: function(event, ui) 
		    {
		        var self = this; 
		        var selfId = $(this).attr('id');
		        
		        if (ui.panel === undefined)
		        {
		        	alert("ui panel is undefined");
		        	if (ui.tab === undefined)
		        	{
		        		alert("ui tab is also undefined");
		        		
		        	}
		        	
		        	return false;
		        }
		        
		        var id = ui.panel.id;
		        
		    	var tagObj = $("#" + selfId + " li:has(a) a[href*='" + id + "']"); 
	
		    	var li_obj = $(tagObj).parent().closest('li');
				
		    	$(li_obj).attr('id',id + "-link");
		    	var count = -1;
		    	
		    	//Reset the indexes of all the new tabs
		    	 $(this).find("li:has(a)").each(function(i) 
				 {
				    var tempId = $(this).attr('id');
				    count = count + 1;
				    
	     			var newLi = $("#" + tempId );
	     			$(newLi).attr('custom:index', count);
				  
	   			});
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
		$('#tabs1').droppable(
		{ 		
		  //accept: "#divDrag",    
		  drop:			function (ev, ui)
		   {
		    	//Set up variables
		    	var aObj = $(ui.draggable).closest('li').find('a'); 
			    var href = aObj.attr('href');
	  			var label = $(aObj).text();
	  			
	  			//Get the parent of the draggable item
	  			var parentDrag = $(ui.draggable).parent().parent();
	  			
	  			//Get the id of the tabs
			 	var divId = $(this).tabs().attr('id');
			 
			 	//Get the current index of the tab in the panel
				var selected = $(ui.draggable).attr('custom:index');
				
				//Split the href value 	   
				var hrefBase = href.split('#')[1], baseEl;		
				//Create link to old text		 
				var $old = $("#" + hrefBase);
				//end set up variables
		  		
		  		//Remove the tab item, only after creating a link to old  text item
		  		parentDrag.tabs('remove',selected);
					
				//Get id of the place where text item shoudl be added
				var layoutId = $('#' + divId +' ul div').attr('id');
			 //alert("layout id " + layoutId);
			 	//Append the text item in new location
				var $newText = $old.appendTo('#' + layoutId);

				//Append the new tab
				$(this).tabs("add","#" + hrefBase,label);
				
				
				//$newText.addClass('ui-tabs-hide');    
			
			 
		  },
		  activate: 		function (ev, ui)
		  {
		  
		  }
		}); 
		
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
					//alert("this is not a tab header");
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
				
				var layoutId = $('#' + divId +' ul div').attr('id');
			 
				var $newText = $old.appendTo('#' + layoutId);

				$(this).tabs("add","#" + hrefBase,label);
				 
				var widgetId = $('#' + hrefBase).find("*[class^=widget]").attr('id');
				//alert('widget id ' + widgetId);
				iNettuts.refresh(widgetId);
				
			} 
		  
		}); 
		
		
		
	});
