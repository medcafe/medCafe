function addImmunizations(callObj, server, tab_num, label, patient_id, repId, patientRepId)
{	
		//For testing purposes
		
		var html = "<div class=\"immunizations" +  patient_id + "\"></div>"; 
		$(callObj).delay(100,function()
		{
			
			 	iNettuts.refresh("yellow-widget" + tab_num);
			
				var serverLink =  server + "?repository=" + repId + "&patient_id=" + patientRepId;
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
						var html = v2js_listPatientImmunizations( data );  	 
						
						var tableObj;
						var selectedRow=0;		
						$("#aaa" + tab_num).append(html);
	  										
						//alert( $("#example" + repId).text());
						 	tableObj = $("#immunizations" + patientRepId).dataTable( {
						 	
						 	//Call back to put in headings
						 	"fnDrawCallback": function ( oSettings ) {
							 if ( oSettings.aiDisplay.length == 0 )
									{
										return;
									}
									
									var nTrs = $('#immunizations' + patientRepId+ ' tbody tr');
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
											var nGroup = document.createElement( 'tr' );
											var nCell = document.createElement( 'td' );
											nCell.colSpan = iColspan;
											nCell.className = "group";
											nCell.innerHTML = oSettings.aoData[ oSettings.aiDisplay[iDisplayIndex] ]._aData[0];
											
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
								"aaSortingFixed": [[0, 'asc']],
								"aaSorting": [[ 1, 'asc' ]]
							
								
								
						} );
					
					
						var immunizationsUrl = serverLink;
						/*$(this).delay(100,function()
						{
							listImmunizations(immunizationsUrl );
							iNettuts.makeSortable();
							
						} );*/
						//Add a button to add a new Row
						
					
						
						setHasContent(tab_num);
						
					} );
					setHasContent(tab_num);
		});
		
}
