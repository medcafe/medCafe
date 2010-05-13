 
 function listHistory(id, patient_id, server, category)
{
	///patients/{id}/history/{category}
	var link = "c/patients/" + patient_id + "/history/" + category;
	  	
	$("#" + id).html("");
	//alert("medCafe.history.js listHistory " + link);				
	$.getJSON(link, function(data)
	{
				//alert("data " + data);
		var html = v2js_listPatientHistory( data );  
		//alert("medCafe.history.js listHistory " + html);				
		
		$("#" + id).html(html);
	  	
	  	//var detail_html = v2js_listPatientHistoryDetail( data );  
  	
  		$('#' + id + ' td').each( function(){
  		
  			var detail = $(this).find('#detail').text();
  			
			$(this).qtip({
				content: detail,
				show: 'mouseover',
				hide: 'mouseout'
			});
		});

	});
}
	
function addHistory(callObj, server, tab_num, label, patient_id, repId)
{	
		
		var html = "<div class=\"history" +  patient_id + "\"></div>"; 
		$(callObj).delay(400,function()
		{
			
			 	iNettuts.refresh("yellow-widget" + tab_num);
				var serverLink =  server + "?repository=" + repId + "&patient_id=" + patient_id;
				//alert("server link " + serverLink);
				$.getJSON(serverLink, function(data)
				{
						
						//Check to see if any error message
						if (data.announce)
						{
							if (retry)
							{
								addHistory(callObj, server, tab_num, label, patient_id, repId);
								retry = false;
							}
							else
							{
								updateAnnouncements(data);
							}
							return;
						}
						var html = v2js_listPatientHistoryTable( data );  	 
						
						var tableObj;
						var selectedRow=0;		
						$("#aaa" + tab_num).append(html);
	  										
						//alert( $("#example" + repId).text());
						 	tableObj = $("#history" + patient_id).dataTable( {
						 	
						 	//Call back to put in headings
						 	"fnDrawCallback": function ( oSettings ) {
							 if ( oSettings.aiDisplay.length == 0 )
									{
										return;
									}
									
									var nTrs = $('#history' + patient_id+ ' tbody tr');
									var iColspan = nTrs[0].getElementsByTagName('td').length;
									var sLastGroup = "";
									for ( var i=0 ; i<nTrs.length ; i++ )
									{
										var iDisplayIndex = oSettings._iDisplayStart + i;
										var sGroup = oSettings.aoData[ oSettings.aiDisplay[iDisplayIndex] ]._aData[0];
										if ( sGroup != sLastGroup )
										{
											var nGroup = document.createElement( 'tr' );
											
											var nCell = document.createElement( 'td' );
											nCell.colSpan = iColspan;
											nCell.className = "group";
											nCell.innerHTML = sGroup;
											
											nGroup.appendChild( nCell );
											nTrs[i].parentNode.insertBefore( nGroup, nTrs[i] );
											sLastGroup = sGroup;
										}
									}
								},
						 	
								"bJQueryUI": true,
								"aaSortingFixed": [[ 0, 'asc' ]],
								"aaSorting": [[ 1, 'asc' ]]
								
								
						} );
					
					
						var medUrl = serverLink;
					
						//Get the selected row if user clicks on <tr> object	 
						$("#history" + patient_id + " tbody tr").click( function() {
						
							var aPos = tableObj.fnGetPosition( this );
							selectedRow = aPos;
						});
						
						//Get the selected row if user clicks on <td> object
						$("#history" + patient_id + " tbody td").click( function() {
						
							var aPos = tableObj.fnGetPosition( this );
							selectedRow = aPos[0];
						});
						
						setHasContent(tab_num);
						
					} );
					setHasContent(tab_num);
		});
		
}
	