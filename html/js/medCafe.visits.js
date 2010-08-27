function processVisitList(callObj, widgetInfo)
{

		var html = "<div class=\"visitListing" +  widgetInfo.patient_id + "\"></div>";
		$(callObj).delay(200,function()
		{

				var idLabel = "visitList";
			 	iNettuts.refresh("yellow-widget" + widgetInfo.tab_num);
				var serverLink =  widgetInfo.server + "?repository=" + widgetInfo.repository + "&patient_id=" + widgetInfo.rep_patient_id;
				//alert("server link " + serverLink);
				$.getJSON(serverLink, function(data)
				{

						//Check to see if any error message
						if (data.announce)
						{	
							updateAnnouncements(data);	
							return;
						}
						var html = v2js_listVisitListTable( data );

						var tableObj;
						var selectedRow=0;
						$("#aaa" + tab_num).append(html);

						//alert( $("#example" + repId).text());
						 	tableObj = $("#"+ idLabel + widgetInfo.rep_patient_id).dataTable( {

						 	//Call back to put in headings
						 	"fnDrawCallback": function ( oSettings ) {
							 if ( oSettings.aiDisplay.length == 0 )
									{
										return;
									}

									var nTrs = $('#'+ idLabel + widgetInfo.rep_patient_id+ ' tbody tr');
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
						$("#" + idLabel + widgetInfo.rep_patient_id + " tbody tr").click( function() {

							var aPos = tableObj.fnGetPosition( this );
							selectedRow = aPos;
						});

						//Get the selected row if user clicks on <td> object
						$("#" +idLabel + widgetInfo.rep_patient_id + " tbody td").click( function() {

							var aPos = tableObj.fnGetPosition( this );
							selectedRow = aPos[0];
						});

						setHasContent(widgetInfo.tab_num);

					} );
					setHasContent(widgetInfo.tab_num);
		});

}
