var cf;
$(function(){

		initializeImages();
});
    
function initializeImages(patient_id)
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

function filterImages( widgetInfo, startDate, endDate, categories)
{
	//alert("medCafe.images.js filterImages patientId " + patientId );
	var delim = "=";
	var fileUrl = "contentflow/coverFeed.jsp?patient_id=" + widgetInfo.patient_id;
	 
	$.get(fileUrl, function(data)
 	{	
 	
 		$("#flowFile").html("");	
 		$("#flowFile").html(data);
 		
 		$("#flowFile").delay(2500,function()
		{
			if (cf == undefined)
			{
				ContentFlowGlobal.setAddOnConf('medCafe', {widgetInfo: widgetInfo});
				cf = new ContentFlow('contentFlow', {reflectionColor: "#000000", widgetInfo: widgetInfo});	
				
			}
			else
			{
					//alert(JSON.stringify(widgetInfo));
				//alert("medCafe.images.js filterImages about to refresh through init");
				$("contentFlow").removeClass("mouseoverCheckElement");
				//cf = new ContentFlow('contentFlow', {reflectionColor: "#000000"});		
				//cf.resize();

				cf._init();
			}
 		});
   });
}

function processImages(callObj, widgetInfo, data)
{
		
		//alert("medCafe.images.js processImages start");
		var startDate = $('#cfStartDate').text();
		var endDate = $('#cfEndDate').text();
		var categories = $('#cfCategories').text();
		filterImages(widgetInfo, startDate, endDate, categories);
		
		 //alert("medCafe.images.js about to process Images");
		 //var cf = new ContentFlow('contentFlow', {reflectionColor: "#000000"});
		 //$("#contentFlow").removeClass("loadIndicator");
}
