function addMedications(callObj, server, tab_num, label, patient_id, repId)
{
		patient_id = "7";
		var html = "<div class=\"medications" +  patient_id + "\"></div>"; 
		$(callObj).delay(200,function()
		{
			
			 	iNettuts.refresh("yellow-widget" + tab_num);
			
				var serverLink =  server + "?repository=" + repId + "&patient_id=" + patient_id;
				$.getJSON(serverLink, function(data)
				{
						var html = v2js_listPatientMedsVert( data );  	 
						
						//Check if there is an error message				
						updateAnnouncements(data);
						
						var tableObj;
						var selectedRow=0;		
						$("#aaa" + tab_num).append(html);
	  										
						//alert( $("#example" + repId).text());
						 	tableObj = $("#medications" + patient_id).dataTable( {
								"bJQueryUI": true,
								"aoColumns": [
										{ "bVisible": false },
										null,
										null
									],
								"aaSortingFixed": [[ 0, 'asc' ]],
								"aaSorting": [[ 1, 'asc' ]]
								
								
								
						} );
					
						//Add a button to add a new Row
						
						//Get the selected row if user clicks on <tr> object	 
						$("#medications" + patient_id + " tbody tr").click( function() {
						
							var aPos = tableObj.fnGetPosition( this );
							selectedRow = aPos;
						});
						
						//Get the selected row if user clicks on <td> object
						$("#medications" + patient_id + " tbody td").click( function() {
						
							var aPos = tableObj.fnGetPosition( this );
							selectedRow = aPos[0];
						});
						
						setHasContent(tab_num);
						
					} );
					setHasContent(tab_num);
		});
		
}