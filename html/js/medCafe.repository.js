function addRepository(callObj, widgetInfo)
	{
	

		//alert("medcafe.repository.js repository " + repId);
		var html = "<div class=\"example" +  widgetInfo.repository + "\"></div>"; 
		$(callObj).delay(200,function()
		{
			
				iNettuts.refresh("yellow-widget" + widgetInfo.order);
				
				var serverLink =  widgetInfo.server + "repository-listJSON.jsp?repository=" + widgetInfo.repository;
				
				$.getJSON(serverLink, function(data)
				{
						//Check to see if there was an error returned	
						updateAnnouncements(data);
						if (data.announce)
						{
							return;
						}
						var html = v2js_listPatientsTable( data );  	  					
						if (!widgetInfo.tab_num)
							widgetInfo.tab_num = "2";
						if (!widgetInfo.column)
							widgetInfo.column = "1";
					
						$("#tabs-" + widgetInfo.tab_num + " #column" + widgetInfo.column).append(html);			
						
						//$("#aaa" + tab_num).append(html);
	  										
						//alert( $("#example" + repId).text());
						$("#example" + repId).dataTable( {
								"aaSorting": [[ 0, "desc" ]]
								,"bJQueryUI": true
								,"sPaginationType": "full_numbers"
						} );
											//$("#example" + patientId).dataTable();
									
						$(this).delay(100,function()
						{
							listRepository(widgetInfo.server, widgetInfo.repository );
							iNettuts.makeSortable();
							
						} );
						
						setHasContent(widgetInfo.order);
						
					} );
					setHasContent(widgetInfo.order);
		});
		
	}
	
	function listRepository(server, rep)
	{
		
					$("#example" + rep + " .summary").each(function ()
				 	{
				 		var detailId = $(this).text();
				 		//alert("calling list repository for rep  " + rep + " detail " + detailId);
	
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
								
								var tab_num = addTab(detailId, "Repository");
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
									
								} );
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
	
