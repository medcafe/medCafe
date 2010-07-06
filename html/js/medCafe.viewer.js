function processViewerImages(repId, patientId, patientRepId, data, type, tab_num)
{
		var server = $('#viewerImageName').text();
           	 
        $("#viewer" + tab_num).iviewer(
        {
            src: server
        });       

}

function initializeViewer(patientId, fileId, server)
{

                  //var pos = $('#viewer').position();
				  var pos = $("#viewer").offset();  
  				  var width = $("#viewer").width();
  				  var height = $("#viewer").height();
				
				  $("#canvas").width(width); 
                  $("#canvasInterface").width(width);
                  
                  $("#canvas").css( { "left": (pos.left) + "px", "top":pos.top + "px" } );           
                  $("#canvasInterface").css( { "left": (pos.left) + "px", "top":pos.top + "px" } );
                  $("#chooserWidgets").css( { "left": (pos.left + width) + "px", "top":pos.top + "px" } );
                  
                  canvasPainter = new CanvasPainter("canvas", "canvasInterface", {x: pos.left , y: pos.top}, width, height);
				  var rtnObj;
				  var viewer = $("#viewer").iviewer(
                       {
                       src: server,
                       canvas: canvasPainter,
                       initCallback: function ()
                       {
                           rtnObj = this;
                           
                           rtnObj.update_container_info();
                       }
                  });
                  		
                    
		    		$('#saveViewButton').click(function() {
  							
  						
  						var saveLink = "saveViewImage.jsp?patient_id=" + patientId + "&fileId=" + fileId; 
  						
							//Get the list of shapes, and their associated settings.
							$('.shape').each(function ()
							{
								saveLink = "saveViewImage.jsp?patient_id=" + patientId + "&fileId=" + fileId; 
								var x1 = $(this).attr("custom:x");
								var y1 = $(this).attr("custom:y");
								var type = $(this).attr("custom:type");
								var width = $(this).attr("custom:width");
								var height = $(this).attr("custom:height");
								var color = $(this).attr("custom:color");
								var note =  $(this).attr("custom:note");
								rtnObj.fit();
								var curr_zoom = rtnObj.current_zoom;
								var origin= {x:-1,y:-1};
								origin.x = rtnObj.img_object.x;
								origin.y = rtnObj.img_object.y;
								//saveLink = saveLink + "&x=" + x1 + "&y=" + y + "&type=" +  type + "&width=" + width + "&height=" + height + "&color=" + color;
								saveLink = saveLink + "&x_origin=" + origin.x + "&y_origin=" + origin.y + "&zoom=" + curr_zoom + "&note=" + note;
								var currShape = new shape(x1 , y1, width, height, type, color);
								
								/*$.get(saveLink, function(data)
								{
									
									//Get the level of current zoom.
									//Get the current origin values of image compared to container
									
								});*/ 
								$.ajax({
					                url: saveLink,
					                type: 'POST',
					                data: currShape,
					                beforeSend: function() { $("#saveStatus").html("Saving").show(); },
					                success: function(result) {
					                    //alert(result.Result);
					                    //$("#saveStatus").html(result.Result).show();
					                }
				            	});
							});
							
  						
					}); 
				 
}

function retrieveViewerData(patientId, fileId, server)
{

	//Get the shapes in JSONFormat
	//populate the "shape objects with data"
	
}

function printError(error)
{
	document.getElementById("errorArea").innerHTML += error +"<br>";
}
		
// used by the dhtml buttons
function setControlLook(id, color) {
	if(id != canvasPainter.curDrawAction)
	document.getElementById("btn_"+id).style.background = color;
}
		
function setCPDrawAction(action) {
	document.getElementById("btn_"+canvasPainter.curDrawAction).style.background = "#FFFFFF";
	document.getElementById("btn_"+action).style.background = "#CCCCCC";
	canvasPainter.setDrawAction(action);
}