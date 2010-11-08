function processViewerImages(callObj, widgetInfo, data)
{
	//$("#tabs-" + widgetInfo.tab_num).html(data );
		var server = $('#viewerImageName'+widgetInfo.id).text();
		//alert (server);
           	 
        $("#viewer" + widgetInfo.tab_num).iviewer(
        {
            src: server
        });       

}

function initializeViewer(patientId, fileId, dir, server)
{

				  //var pos = $('#viewer').position();
				  var pos = $("#viewer").offset();  
  				  var width = $("#viewer").width();
  				  var height = $("#viewer").height();
				 var fullFile = dir + server;
				  var canvasObj = document.getElementById("canvas"); 
				  //$("#canvas").width(width);
				  canvasObj.setAttribute("width", width);
				  canvasObj.setAttribute("height", height);
				  var canvasInterfaceObj = document.getElementById("canvasInterface");
				  canvasInterfaceObj.setAttribute("width", width);
				  canvasInterfaceObj.setAttribute("height", height); 
                //  $("#canvasInterface").width(width);
                  
                  $("#canvas").css( { "left": (pos.left) + "px", "top":pos.top + "px" } );           
                  $("#canvasInterface").css( { "left": (pos.left) + "px", "top":pos.top + "px" } );
                  $("#chooserWidgets").css( { "left": (pos.left) + "px", "top":pos.top + "px" } );
                  
                  canvasPainter = new CanvasPainter("canvas", "canvasInterface", {x: pos.left , y: pos.top}, width, height);
				  var rtnObj;
				  var viewer = $("#viewer").iviewer(
                       {
                       src: fullFile,
                       canvas: canvasPainter,
                       initCallback: function ()
                       {
                           rtnObj = this;      
                           rtnObj.update_container_info();
                       }
                  });
                  		
                    $('#moveImageButton').click(function() 
                    {
                    	//Set up the mouse events
                    	canvasPainter.canvasInterface.removeEventListener("mousedown", canvasPainter.mouseDownActionPerformed.bindAsEventListener(canvasPainter), false);
  						$("<img>").mousedown(function(e){ return rtnObj.drag_start(e); })
                    	document.getElementById("canvas");
  					});
  					
		    		$('#saveViewButton').click(function() {
  							
  						//This delete has to happen first and outside the other functionality
  						var deleteUrl = "deleteImageAnnotations.jsp?patient_id=" +patientId + "&file_id=" + fileId + "&image=" + server;
    					
    					$.ajax({
	           				url: deleteUrl,
	           				type: 'POST',
	           				beforeSend: function() { $("#saveStatus").html("Saving").show(); },
	           				success: function(result) 
	           				{
		                   		var saveLink = "saveViewImage.jsp?patient_id=" + patientId + "&fileId=" + fileId + "&image=" + server; 
	  						
								//Get the list of shapes, and their associated settings.
								$('.shape').each(function ()
								{
									saveLink = "saveViewImage.jsp?patient_id=" + patientId + "&fileId=" + fileId + "&image=" +server; 
									//This will resize everything to fit back on the page - so that everything is normalized to the standard zoom
									//And has an origin for the image of 0,0
									rtnObj.fit();
									var x1 = $(this).attr("custom:x");
									var y1 = $(this).attr("custom:y");
									var type = $(this).attr("custom:type");
									var width = $(this).attr("custom:width");
									var height = $(this).attr("custom:height");
									var color = $(this).attr("custom:color");
									var note =  $(this).attr("custom:note");
									var curr_zoom = $(this).attr("custom:zoom")*100;


								//	var curr_zoom = rtnObj.current_zoom;
								//	alert("zoom " + zoom + " current_zoom " + curr_zoom);
									var origin= {x:-1,y:-1};
									origin.x = rtnObj.img_object.x;
									origin.y = rtnObj.img_object.y;
									if (isNaN(origin.x))
									{
										origin.x = 0;
									}
									if (isNaN(origin.y))
									{
										origin.y = 0;
									}
									
									//saveLink = saveLink + "&x=" + x1 + "&y=" + y + "&type=" +  type + "&width=" + width + "&height=" + height + "&color=" + color;
									saveLink = saveLink + "&x_origin=" + origin.x + "&y_origin=" + origin.y + "&zoom=" + curr_zoom + "&note=" + note;
									var currShape = new shape(x1 , y1, width, height, type, color);
								
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
							
	            			}
        				});

					}); 
				 
}

function retrieveViewerData(patientId, fileId, dir, server)
{

	var serverLink = "annotateImageJSON.jsp?patient_id=" + patientId + "&image=" +server;
	//Get the shapes in JSONFormat
	//populate the "shape objects with data"
	$.getJSON(serverLink, function(data)
	{
						
		//Check to see if any error message
		if (data.announce)
		{
			parent.updateAnnouncements(data);
			return;
		}
		var html = v2js_listImageTags( data );  
		$("#canvas").html(html);
		initializeViewer(patientId, fileId, dir, server);
	});
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
	if (canvasPainter.curDrawAction > -1)
	{
		document.getElementById("btn_"+canvasPainter.curDrawAction).style.background = "#FFFFFF";
	}
	document.getElementById("btn_"+action).style.background = "#CCCCCC";
	canvasPainter.setDrawAction(action);
}
