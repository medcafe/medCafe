function addMedications(callObj, server, tab_num, label, patient_id, repId)
{	
		//For testing purposes
		patient_id = "7";
		var html = "<div class=\"medications" +  patient_id + "\"></div>"; 
		$(callObj).delay(200,function()
		{
			
			 	iNettuts.refresh("yellow-widget" + tab_num);
			
				var serverLink =  server + "?repository=" + repId + "&patient_id=" + patient_id;
				$.getJSON(serverLink, function(data)
				{
						var toggleMinus = 'images/bullet_toggle_minus.png';
						var togglePlus = 'images/bullet_toggle_plus.png';
						//Check to see if any error message
						if (data.announce)
						{
							updateAnnouncements(data);
							return;
						}
						var html = v2js_listPatientMedsVert( data );  	 
						
						var tableObj;
						var selectedRow=0;		
						$("#aaa" + tab_num).append(html);
	  										
						//alert( $("#example" + repId).text());
						 	tableObj = $("#medications" + patient_id).dataTable( {
						 	
						 	//Call back to put in headings
						 	"fnDrawCallback": function ( oSettings ) {
							 if ( oSettings.aiDisplay.length == 0 )
									{
										return;
									}
									
									var nTrs = $('#medications' + patient_id+ ' tbody tr');
									var iColspan = nTrs[0].getElementsByTagName('td').length;
									var sLastGroup = "";
									for ( var i=0 ; i<nTrs.length ; i++ )
									{
										var iDisplayIndex = oSettings._iDisplayStart + i;
										var sGroup = oSettings.aoData[ oSettings.aiDisplay[iDisplayIndex] ]._aData[0];
										if ( sGroup != sLastGroup )
										{
											var nGroup = document.createElement( 'tr' );
											
											//var collapseLines= '<img src="' + toggleMinus + '" alt="collapse this section" />';
											//nGroup.innerHTML = "<td>" + collapseLines+ "</td>";
											
											var nCell = document.createElement( 'td' );
											nCell.colSpan = iColspan;
											nCell.className = "group";
											nCell.innerHTML = sGroup;
											
											/*$('img', $(nGroup)).addClass('clickable').click(function() {

												    var toggleSrc = $(this).attr('src');				
												    if ( toggleSrc == toggleMinus ) {
												    
												      $(this).attr('src', togglePlus).parents('tr').siblings().fadeOut('fast');
												
												    } 
												    else{
												
												      $(this).attr('src', toggleMinus).parents('tr').siblings().fadeIn('fast');
												
												    };
												
											});*/
											
											nGroup.appendChild( nCell );
											nTrs[i].parentNode.insertBefore( nGroup, nTrs[i] );
											sLastGroup = sGroup;
										}
									}
								},
						 	
								"bJQueryUI": true,
								"aoColumns": [
										{ "bVisible": false },
										null,
										null
									],
								"aaSortingFixed": [[ 0, 'asc' ]],
								"aaSorting": [[ 1, 'asc' ]]
								
								
						} );
					
					
						var medUrl = serverLink;
						/*$(this).delay(100,function()
						{
							listMedication(medUrl );
							iNettuts.makeSortable();
							
						} );*/
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
