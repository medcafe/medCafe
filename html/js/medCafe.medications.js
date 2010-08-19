function addMedications(callObj, widgetInfo)
{
		//For testing purposes
        // alert("entering addMedications");
		// var html = "<div class=\"medications" +  patient_id + "\"></div>";
		$(callObj).delay(100,function()
		{
			 	iNettuts.refresh("yellow-widget" + widgetInfo.order);
				var serverLink =  widgetInfo.server + "?repository=" + widgetInfo.repository + "&patient_id=" + widgetInfo.rep_patient_id;
				alert( serverLink);
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
						// $("#aaa" + widgetInfo.order).append(html);
						$("#tabs-2 #column1").append(html);

						alert( " should have added medications to column 1");
						 	tableObj = $("#medications" + widgetInfo.rep_patient_id).dataTable( {

						 	//Call back to put in headings
						 	"fnDrawCallback": function ( oSettings ) {
						 	        if ( oSettings.aiDisplay.length == 0 )
									{
										return;
									}
									var nTrs = $('#medications' + widgetInfo.rep_patient_id+ ' tbody tr');
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
						$("#medications" + widgetInfo.rep_patient_id + " tbody tr").click( function() {

							var aPos = tableObj.fnGetPosition( this );
							selectedRow = aPos;
						});

						//Get the selected row if user clicks on <td> object
						$("#medications" + widgetInfo.rep_patient_id + " tbody td").click( function() {

							var aPos = tableObj.fnGetPosition( this );
							selectedRow = aPos[0];
						});
						setHasContent(widgetInfo.order);
					} );
					setHasContent(widgetInfo.order);
		});
}
