$(function(){

		initialize();
});
    
function initialize()
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