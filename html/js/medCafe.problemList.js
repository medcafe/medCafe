
function listProblemList(id, patient_id, server, repositories)
{
	///patients/{id}/history/{category}
	var link = "problemListJSON.jsp?patient_id=" + patient_id;

	var repositoryList = repositories.split(",");
	for(i = 0; i < repositoryList.length; i++)
	{
		link = link + "&repository=" + repositoryList[i];
	}
	
	$("#" + id).html("");
	$.getJSON(link, function(data)
	{
		var html = v2js_listProblemList( data );

		$("#" + id).html(html);

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

function addProblemList(callObj, server, tab_num, label, patient_id, repId, patientRepId)
{

		var html = "<div class=\"problemListing" +  patient_id + "\"></div>";
		$(callObj).delay(400,function()
		{

			 	iNettuts.refresh("yellow-widget" + tab_num);
				var serverLink =  server + "?repository=" + repId + "&patient_id=" + patientRepId;
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
						var html = v2js_listProblemListTable( data );

						var tableObj;
						var selectedRow=0;
						$("#aaa" + tab_num).append(html);

						//alert( $("#example" + repId).text());
						 	tableObj = $("#problemList" + patientRepId).dataTable( {

						 	//Call back to put in headings
						 	"fnDrawCallback": function ( oSettings ) {
							 if ( oSettings.aiDisplay.length == 0 )
									{
										return;
									}

									var nTrs = $('#problemList' + patientRepId+ ' tbody tr');
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
						$("#problemList" + patientRepId + " tbody tr").click( function() {

							var aPos = tableObj.fnGetPosition( this );
							selectedRow = aPos;
						});

						//Get the selected row if user clicks on <td> object
						$("#problemList" + patientRepId + " tbody td").click( function() {

							var aPos = tableObj.fnGetPosition( this );
							selectedRow = aPos[0];
						});

						setHasContent(tab_num);

					} );
					setHasContent(tab_num);
		});

}
