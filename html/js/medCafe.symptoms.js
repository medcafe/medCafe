function processSymptoms(widgetInfo, data)
{


					$('#templateList').html("");
		  			var html = v2js_listHistoryTemplate( data );  
		  				
					$('#templateList').html(html);
					$(this).delay(100,function()
					{
						$('#templateList').jScrollTouch({height:'380',width:'300'});
					});
						
					$('#saveButton').click(function() {
  							
  						var saveLink = "saveHistory.jsp?patient_id=" + widgetInfo.patient_id;
  						var checkedVals = $("input:checked");
		
  						for (i=0; i < checkedVals.length; i++)
  						{
  							saveLink = saveLink + "&symptom_check=" + checkedVals[i].value;
  						}
  						$.get(saveLink, function(data)
						{
								
						});
					});
		
}
