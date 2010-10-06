$(document).ready( function() {

		var medCafeTabs;
		var tabSelectedId;

		//$('#south-tabs').tabs();

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


		$("#body")
				.tabs({change: function () {}})
				.find(".ui-tabs-nav")
				.sortable({})
			;

		 medCafeTabs = {

			closeTab : function(index, tabNum) {

							$("#tabs").tabs("remove",index);
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
			   				if (cf!=undefined)
			   					cf.resize();
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


	function addTab(label, type, iNettuts)
	{

		//alert("medCafeTabs addTab start");
		//First check if tab already exists
		var tab_num = 0;
		var tab_id = 0;
		if (label != "new" && label !="New")
		{
		$('.tabs').parent().find(".tabContent").each(function(i)
		{
			var tabObj = $(this).find(".id");
			var tabId = $(tabObj).attr("id");
			if (tabId == label)
			{
				var tab_id = $(this).attr('id');
				tab_num = tab_id.split("-")[1];
				$('#tabs').tabs('select', "#tabs-" + tab_num);
				if (cf != undefined)
					cf.resize();

			}

		});


		//If the tab_number is greater than 0 then it has been found already - just return	-1
		if (tab_num != 0) return -1;
		}
		$('.tabs').parent().find(".tabContent").each(function(i)
		{
			tab_id = $(this).attr('id');
		});
		if (tab_id!="0")
		{
		var curr_num = tab_id.split("-")[1];

		tab_num = curr_num*1 + 1;
		}
		else
			tab_num = 1;
		var hrefBase = "tabs-" + tab_num;

		//alert("medCafeTabs addTab current tab num " + tab_num + "  hrefBase " + hrefBase);

		//Add a new Tab
		$('#tabs').tabs("add","#" + hrefBase,label);
		//alert("medCafeTabs addTab current tab num " + tab_num + "  hrefBase " + hrefBase);

		$("#tabs-" + tab_num).addClass('tabContent');
		//Load the widget template
		if (iNettuts != false && iNettuts != "false")
		{
			$("#tabs-" + tab_num ).load("tabs-template.jsp?tab_num=" + tab_num + "&title=" + label + "&type=" + type);
		}
		else
		{
			$("#tabs-" + tab_num).addClass('no-iNettuts');
		}
		//alert("medCafeTabs: addTab tabs-template.jsp?tab_num=" + tab_num + "&title=" + label + "&type=" + type);
		//$("#tabs-" + tab_num).
	
		$('#tabs').tabs('select', "#tabs-" + tab_num);
		return tab_num;
	}

  function addWidgetNum(widgetInfo)
	{


		var widget_id = 0;
		var max_id = 0;
	
		//	$(this).find('.id').each(function()
			$('.widget').each(function()
			{
				var widgetLabel = $(this).find('.id').attr("id");
			//	var widgetLabel = $(this).attr("id");
				var testLabel  = widgetInfo.name+widgetInfo.rep_patient_id;
			   var yellows = $(this);
			//	var yellows= $(this).find('.widget');
				if (yellows!="undefined")
				{
				var widget_idName = yellows.attr('id');
				if (widget_idName != undefined)
				{
				var id = widget_idName.substring(13)*1;
				if (id > max_id)
				{
					max_id = id;
				}
				
				if (widgetLabel == testLabel)
				{
					var tabObject = $(this).closest('.tabContent');
					var tabname = tabObject.attr('id').split("-")[1];
					//alert("tabname " + tabname);
					if (tabname == widgetInfo.tab_num)
					{						
						var paneObject = $(this).closest('.ui-layout-center');
						paneObject.scrollTop(0);
						var wrapperObject = $(this).closest('.dataTables_wrapper');
						//alert(wrapperObject.attr('id'));
						var pos;
						if (wrapperObject.attr('id') != undefined)
							pos = wrapperObject.position();
						else
							 pos = $(this).position();

						//alert ("top " + pos.top + " left " + pos.left + " id " + $(this).attr('id') + " class " + $(this).attr('class'));
						paneObject.scrollTop(pos.top);
						widget_id = widget_idName.substring(13);

					}

				}
				}
				}

		});
		if (widget_id != 0)
			return -1;

		return max_id*1 + 1;
	}

	function createLink(widgetInfo)
	{
		//widgetInfo.tab_num = addTab(widgetInfo.name, type);
		//if (widgetInfo.tab_num < 0)
		//	return;
		createWidgetContent(widgetInfo);

	}

	function setHasContent(tab_num)
	{

			var widgetObj = $("#widget-content" + tab_num);
			var hasContentObj = widgetObj.find("#hasContent");
      		var hasContent = $(hasContentObj).attr("custom:hasContent");
     		$(hasContentObj).attr("custom:hasContent",true);

	}

	function setNoContent(tab_num)
	{

			var widgetObj = $("#widget-content" + tab_num);
			var hasContentObj = widgetObj.find("#hasContent");
      		var hasContent = $(hasContentObj).attr("custom:hasContent");
     		$(hasContentObj).attr("custom:hasContent",false);

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

	function addWidgetTab(callObj, widgetInfo, group)
	{
		var pos;
		var windowLabel="";
		if (widgetInfo.image != undefined)
		{
			pos = widgetInfo.image.lastIndexOf(".");
			windowLabel = widgetInfo.image;
			pos = windowLabel.lastIndexOf("/");
			if (pos > 0)
				windowLabel = windowLabel.substring(pos+1);
		}
		if (widgetInfo.type =="SingleImage" && pos > 0 && widgetInfo.image.substring(widgetInfo.image.lastIndexOf("." )+1) == 'pdf')
		{
		  
			widgetInfo.pdf = true;
			var windowLabel = widgetInfo.image;
			pos = windowLabel.lastIndexOf("/");
			if (pos > 0)
				windowLabel = windowLabel.substring(pos+1);
			widgetInfo.windowObj = window.open(widgetInfo.image, windowLabel);
			widgetInfo.windowObj.focus();
			medCafeWidget.populateExtWidgetSettings(widgetInfo);
		}
		else
		{
			widgetInfo.pdf = false;
            var height = '380';
          
           var tabWidth = $('.ui-tabs-panel').not('.ui-tabs-hide').width();
		    /* var width ='800'; */
			var isiPad = navigator.userAgent.match(/iPad/i) != null;

			if (isiPad)
			{
				height = '380';
				/* width = '400'; */
			}

			if (widgetInfo.tab_num == -1)
			{
				if (windowLabel == "")
					windowLabel = widgetInfo.name;
				widgetInfo.tab_num = addTab(windowLabel, widgetInfo.type, widgetInfo.iNettuts);
			}

			//iNettuts.refresh("yellow-widget" + widgetInfo.id);
			//alert("Server: " +widgetInfo.server + " url: " + widgetInfo.clickUrl);
			var serverLink =  widgetInfo.server + widgetInfo.clickUrl + "?repository=" + widgetInfo.repository;
			if (widgetInfo.type != "Repository")
			{
				serverLink =  serverLink + "&patient_id=" + widgetInfo.patient_id + "&patient_rep_id=" + widgetInfo.rep_patient_id + "&tab_num=" +
				widgetInfo.tab_num;
			}
			if (widgetInfo.params != undefined && widgetInfo.params !="")
			{
				serverLink = serverLink + "&" + widgetInfo.params.split(":")[0] + "="+widgetInfo.params.split(":")[1];
				//alert(serverLink); 
			}
			if (widgetInfo.image && widgetInfo.image!="") {
				serverLink = serverLink + "&image=" + widgetInfo.image;
			}

			//alert('serverLink ' + serverLink);

			$.get(serverLink, function(data)
			{


				//Check to see if any error message
				// $("#aaa" + tab_num).append(data);
				if (widgetInfo.template=="") {
                    if (!widgetInfo.column)
                        widgetInfo.column = "1";
                    if (!widgetInfo.tab_num)
                        widgetInfo.tab_num = "2";
                        if (widgetInfo.iNettuts != false && widgetInfo.iNettuts != 'false')
                        {
                    $("#tabs-"+ widgetInfo.tab_num + " #column" + widgetInfo.column).append(v2js_inettutsHead(widgetInfo) + data +v2js_inettutsTail(widgetInfo));
                    }
                    else
                    {
                 
                     	$("#tabs-"+ widgetInfo.tab_num).append(v2js_head(widgetInfo) + data +v2js_tail(widgetInfo));
                    }
				}
			// alert("should have added content now");
			//	iNettuts.makeSortable();
				setHasContent(widgetInfo.id);
			//	alert("tab_num " + widgetInfo.tab_num);
			  // alert (JSON.stringify(widgetInfo));
			  //alert("JSONProcess ? " + widgetInfo.jsonProcess);
				if (widgetInfo.jsonProcess == "true" || widgetInfo.jsonProcess == true)
				{
			//		alert(data);
					var dataObject = JSON.parse(  data);
					dataObject.widget_id = widgetInfo.id;
					//dataObject.tabNum = widgetInfo.tab_num
					processScripts(callObj, widgetInfo, dataObject);
				}
				else
				{
					processScripts(callObj, widgetInfo, data);
				}
			medCafeWidget.populateExtWidgetSettings(widgetInfo);
		
			//	data = JSON.stringify(dataObject);
			//	alert(data);
				//Run any scripts specific to this type
			 	//processScripts(callObj, repId, patientId, patientRepId, data, type, tab_num);
				//processScripts(callObj, widgetInfo, dataObject);
			    //Try to add a scroll
			    
			/*	$(callObj).delay(100,function()
				{
					if (typeof isScrollable == 'undefined')
					{

						$.getScript('js/jScrollTouch.js', function()
						{
							$("#tabs-"+ widgetInfo.tab_num).jScrollTouch({height:height,width:tabWidth});
						});
					}
					else
					{
						if (isiPad)
						{
						}
						$("#tabs-"+ widgetInfo.tab_num ).jScrollTouch({height:height,width:tabWidth});
					}

				} );  */
		});
		if (group != true && group != "true")
      {

		refreshYellowWidget(callObj, widgetInfo, 1, group);
      }
      }		

	}
	function refreshYellowWidget(callObj, widgetInfo, num, group)
	{
		if (num<50)
		{
			if ($("#yellow-widget"+widgetInfo.id).length<=0)
			{
				//alert("#yellow-widget"+widgetInfo.id + "  length:  " + $("#yellow-widget" + widgetInfo.id).length);
				$(callObj).delay(100,function()
				{
					num++;
					refreshYellowWidget(callObj, widgetInfo, num);
				});
			}
			else
			{
				iNettuts.refresh("yellow-widget" + widgetInfo.id);
				
				if (widgetInfo.collapsed == 'true' || widgetInfo.collapsed == true)
				{
 

	            	var collapseButton = $("#yellow-widget" + widgetInfo.id).find('.collapse');
						
						
	                $(collapseButton).click();
	          
    
     

				}
				if (group != true && group != "true")
					iNettuts.makeSortable();
			
			}
		}
		else
		{
			//alert("Took more than 5 seconds for the widget to return a value, timed out on extra display features");
		}
	}

	function callTemplate(type, data, patientId)
	{
		if (type == "Symptoms")
		{
			var html = v2js_listHistoryTemplate(data);
			var formHtml = "<form action=\"saveHistory.jsp?patient_id=" + patientId+"\"><input type=\"submit\" value=\"Save\"></input><div id=\"templateList\" >";
			html =  formHtml + html +  "</div></form>";
			return html;

		}
	}

	function processScripts(callObj, widgetInfo, data)
	{
		var type = widgetInfo.type;


			//if (typeof addAllergies == 'undefined')
			if (typeof window[widgetInfo.script] == 'undefined')
			{

                //	$.getScript('js/medCafe.allergies.js', function()
                $.getScript('js/' + widgetInfo.script_file, function()
                    {
                        window[widgetInfo.script](callObj, widgetInfo, data);
                    });
			}
			else
			{
				window[widgetInfo.script](callObj, widgetInfo, data);
			}


	}

