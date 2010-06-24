function processSymptoms(repId, patientId, patientRepId, data, type)
{

		var serverLink =  "listHistoryTemplateJSON.jsp?repository=" + repId + "&patient_id=" + patientId + "&patient_rep_id=" + patientRepId;
		$.getJSON(serverLink, function(data)
			{
					$('#templateList').html("");
		  			var html = v2js_listHistoryTemplate( data );  
		  				
					$('#templateList').html(html);
					$(this).delay(100,function()
					{
						$('#templateList').jScrollTouch({height:'380',width:'300'});
					});
						
					$('#saveButton').click(function() {
  							
  						var saveLink = "saveHistory.jsp?patient_id=" + patientId;
  						var checkedVals = $("input:checked");
		
  						for (i=0; i < checkedVals.length; i++)
  						{
  							saveLink = saveLink + "&symptom_check=" + checkedVals[i].value;
  						}
  						$.get(saveLink, function(data)
						{
								
						});
					});
			});
}