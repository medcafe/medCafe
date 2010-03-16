var paramStr="";
function fnClickAddRow(tableObj) {
	
		var aData = tableObj.fnGetData();		
		var rowNum = aData.length;
		tableObj.fnAddData( ['<input type="text" value="name" name="name' + rowNum + '" id="bookmarkName'+ rowNum+ '"></input>','<input type="text" value="url" name="url' + rowNum + '" id="bookmarkurl'+ rowNum+ '"></input>','<input type="text" name="desc' + rowNum + '" value="description" id="bookmarkDesc'+ rowNum+ '"></input>']);

}
	
function fnClickDeleteRow(tableObj, selectedRow) 
{
	
		tableObj.fnDeleteRow(selectedRow);
		/*var aTrs = tableObj.fnGetNodes();
	
		alert("no of rows " + aTrs.length);
		for ( var i=0 ; i<aTrs.length ; i++ )
		{
			if ( $(aTrs[i]).hasClass('row_selected') )
			{
				alert("want to delete row " + i);
				tableObj.fnDeleteRow(i);
			}
		}*/
		return false;
}
//Cycle through each row and save the data
function gatherData(tableObj, patient_id) 
{
		var aTrs = tableObj.fnGetNodes();

		for ( var i=0 ; i<aTrs.length ; i++ )
		{
			var aData = tableObj.fnGetData( i );
			//new rows will be dealt with seperately
			if (aData[0].indexOf("<input") > -1)
			{
			  paramStr = paramStr + processInput(aData, i);
			}
			else
			{
				paramStr = paramStr + "&name" + i + "=" +  aData[0] + "&url"+  i+ "=" + aData[1] + "&desc"+  i+ "=" + aData[2] ;
			}
		}
		
		var action = "saveBookmarks.jsp?action=Save&patient=" + patient_id;
		
		action = action + paramStr;
		 
		$.post(action, function(){
  			alert("done");
		});
		 
			 			
}

function processInput(aData, row_num)
{
	var name = "";
	var url = "";
	var desc = "";
	if (aData[0] != null)
	{
		name = $(aData[0]).serialize();
	}
	if (aData[1] != null)
	{
		url = $(aData[1]).serialize();
	}
	if (aData[2] != null)
	{
		desc = $(aData[2]).serialize();
	}
	paramStr = paramStr + "&" + name + "&"+  url + "&" + desc ;
	
	return paramStr;
}
function addBookmarks(callObj, server, tab_num, label, patient_id)
{
		var repId = "OurVista";
		//var onSubmit = 'onSubmit="$('#test').load('saveBookmarks.jsp?patient_id=<%=patient_id%>')'';"
		var html = "<div class=\"bookmarks" +  patient_id + "\"></div>"; 
		$(callObj).delay(200,function()
		{
			
			 	iNettuts.refresh("yellow-widget" + tab_num);
				
				var serverLink =  server + "?repository=" + repId + "&patient_id=" + patient_id;
				
				$.getJSON(serverLink, function(data)
				{
					
						var html = v2js_listPatientsBookmarksTable( data );  	  					
						var tableObj;
						var selectedRow;		
						$("#aaa" + tab_num).append(html);
	  										
						//alert( $("#example" + repId).text());
						 	tableObj = $("#bookmarks" + patient_id).dataTable( {
								"aaSorting": [[ 0, "desc" ]]
								,"bJQueryUI": true
						} );
					
						//Add a button to add a new Row
						var buttonText = '<p><button type="button" id="addRowButton">Add Row</button><button type="button" id="deleteRowButton">Delete Row</button></p>';
						$("#bookmarks" + patient_id).append(buttonText);
						
						//$("#bookmarks" + patient_id).append(saveButtonText);
						
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
	
						//Get the selected row if user clicks on <tr> object	 
						$("#bookmarks" + patient_id + " tbody tr").click( function() {
						
							var aPos = tableObj.fnGetPosition( this );
							selectedRow = aPos;
						});
						
						//Get the selected row if user clicks on <td> object
						$("#bookmarks" + patient_id + " tbody td").click( function() {
						
							var aPos = tableObj.fnGetPosition( this );
							selectedRow = aPos[0];
						});
						
						$("#addRowButton").bind("click",{table:tableObj},
								function(e)
								{
									fnClickAddRow(tableObj);
								
								});
								
						$("#deleteRowButton").bind("click",{table:tableObj},
								function(e)
								{
									fnClickDeleteRow(tableObj, selectedRow);	
								});
										
						//Put in code to call saveData		
						$('#bookmarkForm' + patient_id).submit( function() {
							
							gatherData(tableObj, patient_id);
							return false;
						} );
			
						setHasContent(tab_num);
						
					} );
					setHasContent(tab_num);
		});
		
}