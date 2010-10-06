function addMedications(callObj, widgetInfo, data)
{
		//For testing purposes
		var html = "<div class=\"" + widgetInfo.type +  widgetInfo.patient_id + "\"></div>";
        // alert("entering addMedications");
        
		// var html = "<div class=\"medications" +  patient_id + "\"></div>";
	/*	$(callObj).delay(100,function()
		{
			 	iNettuts.refresh("yellow-widget" + widgetInfo.id);
				var serverLink =  widgetInfo.server + "?repository=" + widgetInfo.repository + "&patient_id=" + widgetInfo.rep_patient_id;
				//alert( serverLink);
				$.getJSON(serverLink, function(data)
				{     */
				
				//		var toggleMinus = 'images/bullet_toggle_minus.png';
				//		var togglePlus = 'images/bullet_toggle_plus.png';
						//	var dataObject = eval('(' + data + ')');
						//Check to see if any error message
				//		if (data.announce)
				//		{
				//			updateAnnouncements(data);
				//			return;
				//		}
						//var html = v2js_listPatientMedsVert( data );
						var html = v2js_inettutsHead(widgetInfo) +window["v2js_" + widgetInfo.template](data) + v2js_inettutsTail(widgetInfo);
						var tableObj;
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
					//	$("#aaa" + tab_num).append(html);
	  										
						//alert( $("#example" + repId).text());
		/*				 	tableObj = $("#"+widgetInfo.type + widgetInfo.rep_patient_id).dataTable( {
						 	
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
				} );
					setHasContent(widgetInfo.order);
		});   */
}
