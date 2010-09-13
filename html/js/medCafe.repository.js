function addRepository(callObj, widgetInfo, data)
	{
	

		//alert("medcafe.repository.js repository " + repId);
	//	var html = "<div class=\"example" +  widgetInfo.repository + "\"></div>"; 
	   var html = "<div class=\"" + widgetInfo.type +  widgetInfo.repository + "\"></div>";
	//	$(callObj).delay(200,function()
	//	{
			
	//			iNettuts.refresh("yellow-widget" + widgetInfo.order);
				
	//			var serverLink =  widgetInfo.server + "repository-listJSON.jsp?repository=" + widgetInfo.repository;
				
	//			$.getJSON(serverLink, function(data)
	//			{
						//Check to see if there was an error returned	
				//		var dataObject = eval('(' + data + ')');
						updateAnnouncements(data);
						if (data.announce)
						{
							return;
						}
						var html = v2js_inettutsHead(widgetInfo) +window["v2js_" + widgetInfo.template](data) + v2js_inettutsTail(widgetInfo);
					//	var html = v2js_listPatientsTable( data );  	  					
						if (!widgetInfo.tab_num)
							widgetInfo.tab_num = "2";
						if (!widgetInfo.column)
							widgetInfo.column = "1";
					
						$("#tabs-" + widgetInfo.tab_num + " #column" + widgetInfo.column).append(html);			
						
						//$("#aaa" + tab_num).append(html);
	  										
						//alert( $("#example" + repId).text());
						$("#" + widgetInfo.type + widgetInfo.repository).dataTable( {
								"aaSorting": [[ 0, "desc" ]]
								,"bJQueryUI": true
								,"sPaginationType": "full_numbers"
						} );
											//$("#example" + patientId).dataTable();
									
						$(this).delay(100,function()
						{

							listRepository(widgetInfo );

						//	iNettuts.makeSortable();
							
						} );
						
						setHasContent(widgetInfo.order);
						
			//		} );
			//		setHasContent(widgetInfo.order);
//		});
		
	}
	
	function listRepository(widgetInfo)
	{
		
					$("#" +widgetInfo.type + widgetInfo.repository + " .summary").each(function ()
				 	{
				 		var detailId = $(this).text();
				 		//alert("calling list repository for rep  " + rep + " detail " + detailId);
	
				 		var detailButton = $(this).find('.details');
			 			$(detailButton).bind("click",{},
						
							function(e)
							{
							
								//First check if the current detail tab exists
								//Then put focus on this tab
								if ($("#Detail" + detailId).attr('id') )
								{
									//Find closest tab
									
									var test = $("#Detail" + detailId).parent().parent().closest('.tabContent');
									var tabId = test.attr('id');
									
									$('#tabs').tabs('select', '#' + tabId);
									return false;
								}

							var link = widgetInfo.server + "widgets-listJSON.jsp?type=patient_widgets";
							$.getJSON(link, function(data)
							{
								var done = false;
								for (var i = 0; i< data.widgets.length && !done ; i++)
								{
									var widget = data.widgets[i];

									if (widget.name == "Details")
									{
										done = true;
										var newWidget = {
											"id" : "",
											"patient_id" : "",
											"rep_patient_id" : detailId,
											"location" : widgetInfo.location,
											"repository" : widgetInfo.repository,
											"type" : widget.type,
											"name" : "Details",
											"server" : widget.server,
											"tab_num": widgetInfo.tab_num,
											"params" : widget.params,
											"column" : widgetInfo.column,
											"script" : widget.script,
											"script_file" : widget.script_file,
											"template" :widget.template,
											"clickUrl": widget.clickUrl,
											"jsonProcess": widget.jsonProcess
											
										};
										newWidget.id = addWidgetNum(newWidget);
									//	alert("widget.id " + newWidget.id);
										if (newWidget.id != -1)
											addWidgetTab(this, newWidget);
									}
								}
							});
						
							/*	var tab_num = addTab(detailId, "Repository");
								//Delay to let the DOM refresh
								$(this).delay(500,function()
								{
									iNettuts.refresh("yellow-widget" + widgetInfo.id);
								
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
											"aaSortingFixed": [[ 0, 'asc' ]],
											"aoColumns": [
											{ "bVisible": false },
		
												null,
												null
														]

												,"bJQueryUI": true
											} );
											setHasContent(tab_num);
										} );
									});
									
								} ); */
							} );
								
		    	    });
		    
	}
	
	function listRepositories(isIntro)
	{
		
		    var link = "c/repositories";
			$("#listRepositories").html("");
					
			$.getJSON(link, function(data)
			{
				//alert("data " + data);
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
	
