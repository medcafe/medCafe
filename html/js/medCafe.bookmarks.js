function fnClickAddRow(tableObj) {
	
		var aData = tableObj.fnGetData();		
		var rowNum = aData.length;
		tableObj.fnAddData( ['<input type="text" value="name" id="bookmarkName'+ rowNum+ '"></input>','<input type="text" value="url" id="bookmarkurl'+ rowNum+ '"></input>','<input type="text" value="description" id="bookmarkDesc'+ rowNum+ '"></input>']);

}
	
function fnClickDeleteRow(tableObj) 
{
	
		var aTrs = tableObj.fnGetNodes();
	
		alert("no of rows " + aTrs.length);
		for ( var i=0 ; i<aTrs.length ; i++ )
		{
			if ( $(aTrs[i]).hasClass('row_selected') )
			{
				alert("want to delete row " + i);
				tableObj.fnDeleteRow(i);
			}
		}
		
}

function addBookmarks(callObj, server, tab_num, label, patient_id)
{
		var repId = "OurVista";
		
		var html = "<div class=\"bookmarks" +  patient_id + "\"></div>"; 
		$(callObj).delay(200,function()
		{
			
				iNettuts.refresh("yellow-widget" + tab_num);
				
				var serverLink =  server + "?repository=" + repId + "&patient_id=" + patient_id;
				
				$.getJSON(serverLink, function(data)
				{
					
						var html = v2js_listPatientsBookmarksTable( data );  	  					
								
						var tableObj;		
						$("#aaa" + tab_num).append(html);
	  										
						//alert( $("#example" + repId).text());
						 	tableObj = $("#bookmarks" + patient_id).dataTable( {
								"aaSorting": [[ 0, "desc" ]]
								,"bJQueryUI": true
						} );
					
						//Add a button to add a new Row
						var buttonText = '<p><button type="button" id="addRowButton">Add Row</button><button type="button" id="deleteRowButton">Delete Row</button></p>';
						
						$("#bookmarks" + patient_id).append(buttonText);
						
						//Make all the cells editable
						$("#bookmarks" + patient_id + " tbody td").editable( '', {
								"callback": function( sValue, y ) {
									var aPos = tableObj.fnGetPosition( this );
									tableObj.fnUpdate( sValue, aPos[0], aPos[1] );
								},
								"submitdata": function ( value, settings ) {
									return { "row_id": this.parentNode.getAttribute('id') };
								},
								"height": "14px"
							} );
	
						
						$("#addRowButton").bind("click",{table:tableObj},
								function(e)
								{
									fnClickAddRow(tableObj);
								
								});
								
						$("#deleteRowButton").bind("click",{table:tableObj},
								function(e)
								{
									fnClickDeleteRow(tableObj);	
								});
										
						//Put in code to call saveData		
						$('#bookmarkForm' + patient_id).submit( function() {
							var sData = $('input', tableObj.fnGetNodes()).serialize();
							alert( "The following data would have been submitted to the server: \n\n"+sData );
							return false;
						} );
								
						setHasContent(tab_num);
						
					} );
					setHasContent(tab_num);
		});
		
}