 
 function listHistory(id, patient_id, server, category)
{
	///patients/{id}/history/{category}
	var link = "c/patients/" + patient_id + "/history/" + category;
	  	
	$("#" + id).html("");
	alert("medCafe.history.js listHistory " + link);				
	$.getJSON(link, function(data)
	{
				//alert("data " + data);
		var html = v2js_listPatientHistory( data );  
		alert("medCafe.history.js listHistory " + html);				
		
		$("#" + id).html(html);
	  						
	});
}
	