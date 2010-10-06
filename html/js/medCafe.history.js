
 function listHistory(id, patient_id, repository, server, category)
{
	///patients/{id}/history/{category}
	var link = "c/repositories/" + repository + "/patients/" + patient_id + "/history/" + category;

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

function addHistory(callObj, widgetInfo, data)
{
		var html = "<div class=\"" + widgetInfo.type +  widgetInfo.patient_id + "\"></div>";
	//	var html = "<div class=\"history" +  patient_id + "\"></div>";
	//	$(callObj).delay(400,function()
	//	{

			// 	iNettuts.refresh("yellow-widget" + tab_num);
			//	var serverLink =  server + "?repository=" + repId + "&patient_id=" + patient_id;
				//alert("server link " + serverLink);
			//	$.getJSON(serverLink, function(data)
			//	{

						//Check to see if any error message
					//	var dataObject = eval('(' + data + ')');
					//	if (data.announce)
					//	{
					/*		if (retry)
							{
								addHistory(callObj, widgetInfo, data);
								retry = false;
							}
							else
							{     */
							//	updateAnnouncements(data);
						//	}
						//	return;
					//	}
		//				var html = v2js_listPatientHistoryTable( data );
						var html = v2js_inettutsHead(widgetInfo) +window["v2js_" + widgetInfo.template](data) + v2js_inettutsTail(widgetInfo);
						var tableObj;
						var selectedRow=0;
						// $("#aaa" + tab_num).append(html);
					  //	$("#tabs-2 #column1").append(html);
						if (!widgetInfo.tab_num)
							widgetInfo.tab_num = "2";
						if (!widgetInfo.column)
							widgetInfo.column = "1";
					
						$("#tabs-" + widgetInfo.tab_num + " #column" + widgetInfo.column).append(html);
						$(callObj).delay(500, function ()
						{ 
	
							$('#collapseInfo' + widgetInfo.id).find('a.collapse').toggle(function () {
						  		$(this).text("Less");
                    		$(this).css({backgroundPosition: '-52px 0'});
                     	$(this).parent().find("#add_repos" +widgetInfo.id).show();
                    		return false;
                		},function () {
                			$(this).text("More");
                      	$(this).css({backgroundPosition: ''});
                     	$(this).parent().find("#add_repos" +widgetInfo.id).hide();
                    	return false;
                		});
            	});
						//alert( $("#example" + repId).text());
						/* 	tableObj = $("#" + widgetInfo.type + widgetInfo.patient_id).dataTable( {

						 	//Call back to put in headings
						 	"fnDrawCallback": function ( oSettings ) {
							 if ( oSettings.aiDisplay.length == 0 )
									{
										return;
									}

									var nTrs = $('#' + widgetInfo.type + widgetInfo.patient_id+ ' tbody tr');
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


					//	var medUrl = serverLink;
						var medUrl = widgetInfo.server + widgetInfo.clickUrl+ "?repository=" + widgetInfo.repository + "&patient_id=" + widgetInfo.patient_id;	
						//Get the selected row if user clicks on <tr> object
						$("#" + widgetInfo.type + widgetInfo.patient_id + " tbody tr").click( function() {

							var aPos = tableObj.fnGetPosition( this );
							selectedRow = aPos;
						});

						//Get the selected row if user clicks on <td> object
						$("#" + widgetInfo.type + widgetInfo.patient_id + " tbody td").click( function() {

							var aPos = tableObj.fnGetPosition( this );
							selectedRow = aPos[0];
						}); 

						setHasContent(tab_num);

//					} );  */
	//				setHasContent(tab_num);
	//	});

}
