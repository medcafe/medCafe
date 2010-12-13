function processSymptoms(callObj, widgetInfo, data, tab_set)
{
	if (tab_set === undefined)
	{
		tab_set ="tabs";
	}
	var tab_key = tab_set + "-";

				//	var dataObject = eval('(' + data + ')');
					//alert("data " + data);
					if (!widgetInfo.tab_num)
							widgetInfo.tab_num = "2";
					if (!widgetInfo.column)
							widgetInfo.column = "1";
					
					var html = v2js_inettutsHead(widgetInfo) + data +v2js_inettutsTail(widgetInfo);
					$("#" + tab_key + widgetInfo.tab_num + " #column" + widgetInfo.column).append(html);	
					$('#templateList').html("");
				
			  		//	var html = v2js_listHistoryTemplate( data );  
		  			//var html = v2js_inettutsHead(widgetInfo) +window["v2js_" + widgetInfo.template](data) + v2js_inettutsTail(widgetInfo);	
					$('#templateList').html(html);
		/*			$(callObj).delay(100,function()
					{
						$('#templateList').jScrollTouch({height:'380',width:'300'});
					});   */
						
					$('#saveButton').click(function() {
  							
  						var saveLink = "saveHistory.jsp?patient_id=" + widgetInfo.patient_id;
  						var checkedVals = $("#medCafeWidget-"+tab_set + widgetInfo.id).find("input:checked");
		
  						for (i=0; i < checkedVals.length; i++)
  						{
  							saveLink = saveLink + "&symptom_check=" + checkedVals[i].value;
  						}
  						$.get(saveLink, function(data)
						{
								
						});
					});
		
}
