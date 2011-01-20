function addRepository(callObj, widgetInfo, data, tab_set)
	{
	if (tab_set === undefined)
	{
		tab_set ="tabs";
	}
	var tab_key = tab_set + "-";
		var html;
	
						updateAnnouncements(data);
						if (data.announce)
						{
							return;
						}
						html = v2js_inettutsHead(widgetInfo) +window["v2js_" + widgetInfo.template](data) + v2js_inettutsTail(widgetInfo);
 	  					
						if (!widgetInfo.tab_num)
							widgetInfo.tab_num = "2";
						if (!widgetInfo.column)
							widgetInfo.column = "1";
					
						$("#" + tab_key + widgetInfo.tab_num + " #column" + widgetInfo.column).append(html);		
						
	
						$(this).delay(100,function()
						{

							listRepository(widgetInfo );


							
						} );
						
						//setHasContent(widgetInfo.order);

		
	}
	
	function listRepository(widgetInfo)
	{
		
					$("#" +widgetInfo.type + widgetInfo.params.split(":")[1] + " .summary").each(function ()
				 	{
				 		var detailId = $(this).text();

					
				 		var detailButton = $(this).find('.details');
			 			$(detailButton).bind("click",{},
						
							function(e)
							{
							

						 
							var link =  "widgets-listJSON.jsp?type=patient_widgets";
							$.getJSON(link, function(data)
							{
								var done = false;
								for (var i = 0; i< data.widgets.length && !done ; i++)
								{
									var widget = data.widgets[i];

									if (widget.name == "Details")
									{
										done = true;
										var patReposWidget = {
											"id" : "",
											"patient_id" : detailId,
											"location" : widgetInfo.location,
											"type" : "OtherDetails",
											"name" : "DetailsOfOtherPatient",
											//"server" : widget.server,
											"tab_num": widgetInfo.tab_num,
											"params" : widgetInfo.params+";rep_patient_id:"+detailId,
											"column" : widgetInfo.column,
											"script" : widget.script,
											"script_file" : widget.script_file,
											"template" :widget.template,
											"clickUrl": widget.clickUrl,
											"jsonProcess": widget.jsonProcess,
											"color_num" :2,
											"isINettuts" : "true",
											"collapsed" : "false",
											"nocache" : "true",
											"label" : "Details for Other Patient"
										};
										patReposWidget.id = addWidgetNum(patReposWidget);

										if (patReposWidget.id != -1)
											addWidgetTab(this, patReposWidget);
									}
								}
							});
						

							} );
								
		    	    });
		    
	}
	
	function listRepositories(isIntro)
	{
		
		    var link = "c/repositories";
			$("#listRepositories").html("");
					
			$.getJSON(link, function(data)
			{

				var html = v2js_listRepositorySelect( data );  	  			
	  			$("#listRepositories").html(html);
	  			
	  			$("#repositorySelect").change(function() 
				{
					var source = $('#patient_searchFrame').attr("src");
		    		var server = $("option:selected", this).val();

		    		var pos = source.indexOf("?");
		    		if (pos > 0)
		    		{
		    			source = source.substring(0,pos);
		    		}
		    		source = source + "?server=" + server + "&intro=" +isIntro;
		    		
		    		//Get details for this patient
		    	
					$('#patient_searchFrame').attr('src', source); 	
		    		
	    		});
				
		    });
	}
	
