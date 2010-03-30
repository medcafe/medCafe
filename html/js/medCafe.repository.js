function addRepository(callObj, server, tab_num, label, repId)
	{
	

		var html = "<div class=\"example" +  repId + "\"></div>"; 
		$(callObj).delay(200,function()
		{
			
				iNettuts.refresh("yellow-widget" + tab_num);
				
				var serverLink =  server + "repository-listJSON.jsp?repository=" + repId;
				
				$.getJSON(serverLink, function(data)
				{
						//Check to see if there was an error returned	
						updateAnnouncements(data);
						if (data.announce)
						{
							return;
						}
						var html = v2js_listPatientsTable( data );  	  					
								
						
						$("#aaa" + tab_num).append(html);
	  										
						//alert( $("#example" + repId).text());
						$("#example" + repId).dataTable( {
								"aaSorting": [[ 0, "desc" ]]
								,"bJQueryUI": true
								,"sPaginationType": "full_numbers"
						} );
											//$("#example" + patientId).dataTable();
									
						$(this).delay(100,function()
						{
							listRepository(server, repId );
							iNettuts.makeSortable();
							
						} );
						
						setHasContent(tab_num);
						
					} );
					setHasContent(tab_num);
		});
		
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
												"aaSorting": [[ 0, "desc" ]]
												,"bJQueryUI": true
											} );
											setHasContent(tab_num);
										} );
									});
									
								} );
							} );
								
		    	    });
		    
	}
	