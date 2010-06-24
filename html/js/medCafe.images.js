$(function(){

		initializeImages();
});
    
function initializeImages()
{
    	addImageButton("7");
}
    
function addImageButton( patient_id)
{
	
	 var buttonTxt = "<button id='addImageBtn'>Add An Image</button>";
	 $("#addImage").html("");
	 $("#addImage").append(buttonTxt);
	 //var url="setPatientImage.jsp"; 
	 var server="c/repositories/OurVista/patients/" +  patient_id+ "/images";
	 //var server = url + "?patient_id=" + patient_id + "&file=test";
	
	 $("#addImageBtn").click(function(event,patient_id){
	 
		$.ajax({
		    type: "PUT",
		    url: server,
		    contentType: "application/json",
		    data: {"data": "mydata"}
		});
		
	
	});
}

function filterImages( startDate, endDate, categories)
{
	var delim = "=";
	//var fileUrl = "contentflow/coverFeed.jsp?filter=patient_id"  + delim + patientId;
	var fileUrl = "contentflow/coverFeed.jsp";
	/*var append = "~";
	
	if (startDate != "null")
	{
		fileUrl = fileUrl + append + "dates" +  delim  + startDate;
	}
	if (endDate != "null")
	{
		fileUrl = fileUrl +  "_" + endDate;
	}
	
	if (! (categories == "") )
			fileUrl = fileUrl +  "~filterCat"+  delim  + categories;
	*/
	 
	$.get(fileUrl, function(data)
 	{	
 		$("#flowFile").html("");	
 		$("#flowFile").html(data);
 		
 		$("#flowFile").delay(2500,function()
		{
			var cf = new ContentFlow('contentFlow', {reflectionColor: "#000000"});	
 		});
   });
}

function processImages(repId, patientId, patientRepId, data, type)
{
		alert("medCafe.images.js processImages start");
		var startDate = $('#cfStartDate').text();
		var endDate = $('#cfEndDate').text();
		var categories = $('#cfCategories').text();
		filterImages(patientId, startDate, endDate, categories);
		
		 //alert("medCafe.images.js about to process Images");
		 //var cf = new ContentFlow('contentFlow', {reflectionColor: "#000000"});
		 //$("#contentFlow").removeClass("loadIndicator");
}