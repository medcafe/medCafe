
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

function addProblemList(callObj, widgetInfo, data)
{

		var html = "<div class=\"" + widgetInfo.type +  widgetInfo.rep_patient_id + "\"></div>";
	//	$(callObj).delay(400,function()
	//	{

	//		 	iNettuts.refresh("yellow-widget" + widgetInfo.order);
	//			var serverLink =  widgetInfo.server + "?repository=" + widgetInfo.repository + "&patient_id=" + widgetInfo.rep_patient_id;
				//alert("server link " + serverLink);
	//			$.getJSON(serverLink, function(data)
	//			{
	//	var dataObject = JSON.parse(data);
	//	var dataObject = data;
							//Check to see if any error message
						if (data.announce)
						{
							if (retry)
							{
								addProblemList(callObj, widgetInfo, data);
								retry = false;
							}
							else
							{
								updateAnnouncements(data);
							}
							return;
						}
					//	var html = v2js_listProblemListTable( data );
					//	var html = window["v2js_listProblemListTable2"](data);
						var html = v2js_inettutsHead(widgetInfo) +window["v2js_" + widgetInfo.template](data) + v2js_inettutsTail(widgetInfo);
						//var tableObj;
						//var selectedRow=0;
						//$("#aaa" + tab_num).append(html);
						if (!widgetInfo.tab_num)
							widgetInfo.tab_num = "2";
						if (!widgetInfo.column)
							widgetInfo.column = "1";
					
						$("#tabs-" + widgetInfo.tab_num + " #column" + widgetInfo.column).append(html);
						//alert( $("#example" + repId).text());
							tableObj = $("#"+widgetInfo.type + widgetInfo.rep_patient_id).dataTable( {

						 	//Call back to put in headings
						 	"fnDrawCallback": function ( oSettings ) {
							 if ( oSettings.aiDisplay.length == 0 )
									{
										return;
									}

									var nTrs = $('#' + widgetInfo.type + widgetInfo.rep_patient_id+ ' tbody tr');
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


						var medUrl = widgetInfo.server + widgetInfo.clickUrl + "?repository=" + widgetInfo.repository + "&patient_id=" + widgetInfo.rep_patient_id;

						//Get the selected row if user clicks on <tr> object
						$("#" + widgetInfo.type + widgetInfo.rep_patient_id + " tbody tr").click( function() {

							var aPos = tableObj.fnGetPosition( this );
							selectedRow = aPos;
						});

						//Get the selected row if user clicks on <td> object
						$("#" + widgetInfo.type + widgetInfo.rep_patient_id + " tbody td").click( function() {

							var aPos = tableObj.fnGetPosition( this );
							selectedRow = aPos[0];
						});

						setHasContent(widgetInfo.order);
/*
//					} );
	//				setHasContent(widgetInfo.order);
//		});*/

}
