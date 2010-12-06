<!DOCTYPE html>
<html>
<%@ page import = "org.mitre.medcafe.util.*, org.mitre.medcafe.model.*"%>
<%
	String imageName = request.getParameter("image");
	if (imageName == null)
		imageName = "images/patients/1/chest-xray-marked.jpg";
			String imageTitle = imageName;
	int pos = imageName.lastIndexOf("/") + 1;
	 if (pos > 0)
	 {
	 	imageTitle = imageTitle.substring(pos);

	 }
	String patientId =  request.getParameter("patient_id");
		
	PatientCache cache = (PatientCache) session.getAttribute(PatientCache.KEY);
    if( cache == null )
    {  //nobody is logged in
        //log.warning("No patient selected");
        response.sendRedirect("introPage.jsp");
        return;
    }
    patientId = cache.getDatabasePatientId();
  
	String dir = "images/patients/" + patientId + "/";
		
	String fileId = request.getParameter("file_id");
	
%>
    <head>
    <style type="text/css">
	body {
		font-family: arial, helvetica;
		font-size: 11px;
		margin: 0px;
		padding: 0px;
	}
	h1 {
		font-size: 14pt;
		font-style: italic;
		margin-bottom: 8px;
	}
	a {
		text-decoration: none;
		color: black;
	}
	canvas {
		border: 1px solid #AAAAAA;
	}
	#canvas {
		position: absolute;
		left: 90px;
		top: 10px;
	}
	#canvasInterface {
		position: absolute;
		left: 90px;
		top: 10px;
	}
	#noCanvas {
		position: absolute;
		left: 90px;
		top: 100px;
		width: 500px;
		height: 400px;
		font-size: 16px;
	}
	#chooserWidgets {
		display: block;
		position: absolute;
		left: 0px;
		width: 300px;
		top: 10px;
	}
	#chooserWidgets canvas {
		margin-bottom: 10px;
	}
	#controls {
		position: absolute;
		top: 10px;
		left: 8px;
		font-size: 12px;
		width: 70px;
	}
	.ctr_btn {
		overflow: hidden;
		width: 30px;
		height: 22px;
		cursor: pointer; 
		padding-left: 3px; 
		margin-bottom: 2px;
		border:1px solid #AAAAAA; 
		background:#FFFFFF;
	}
	#cpainterInfo {
		position: absolute;
		left: 500px;
		top: 320px;
	}
	#errorArea {
		position: absolute;
		width: 200px;
		left: 800px;
	}
	
	
</style>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>jquery.iviewer test</title>
        <link rel="stylesheet" media="screen" type="text/css" href="css/colorpicker-layout.css" />
        <link rel="stylesheet" href="css/colorpicker.css" type="text/css" />
        <script type="text/javascript" src="js/jquery-1.4.4.js" ></script>
        <script type="text/javascript" src="js/jquery.mousewheel.js" ></script>
        <script type="text/javascript" src="js/jquery.iviewer-draw.js" ></script>
        <script type="text/javascript" src="js/canvas/cp_depends.js"></script>
		<script type="text/javascript" src="js/canvas/excanvas.js"></script>
		<script src="js/canvas/CanvasWidget.js" type="text/javascript"></script>
		<script src="js/canvas/CanvasPainter.js" type="text/javascript"></script>
		<script src="js/canvas/CPWidgets.js" type="text/javascript"></script>
		<script src="js/canvas/CPAnimator.js" type="text/javascript"></script>
		<script type="text/javascript" src="js/color-picker/colorpicker.js"></script>
	    <script type="text/javascript" src="js/color-picker/eye.js"></script>
	    <script type="text/javascript" src="js/color-picker/utils.js"></script>
	    <script type="text/javascript" src="js/color-picker/colorpicker-layout.js"></script>
		<script type="text/javascript" src="js/medCafe.viewer.js"></script>
		<script type="text/javascript" src="${js}/vel2jstools.js"></script>
		<script type="text/javascript" src="${js}/vel2js.js"></script>	
		
        <script type="text/javascript">
            var $ = jQuery;
            var canvasPainter;
			var curAction = 0;
			
			
            $(document).ready(function(){
            	 retrieveViewerData('<%=patientId%>', '<%=fileId%>', '<%=dir%>', '<%=imageTitle%>');
            });	 
        </script>
        <link rel="stylesheet" href="css/jquery.iviewer.css" />
        <style>
            .viewer
            {
                width: 90%;
                height: 310px;
                border: 1px solid black;
                position: relative;
            }
            
            .wrapper
            {
                overflow: hidden;
            }
        </style>
    </head>
    <body>
        
        <!-- wrapper div is needed for opera because it shows scroll bars for reason -->
       <div class="wrapper">
            
            <div id="viewer" class="viewer"> 
            </div>	
            <br />
          				</div>
 		
     
   
        		<canvas id="canvas" width="300" height="310"></canvas>
				<canvas id="canvasInterface" width="300" height="310"></canvas>
		

				<div id="chooserWidgets">
					<div id="controls">
							
						<div id="customWidget">
							<div id="colorSelector2">
								<div style="background-color: #00ff00">
								</div>
							</div>
				    		<div id="colorpickerHolder2">
							</div>
			
						</div>
						<div class="ctr_btn" id="btn_0" onclick="setCPDrawAction(0)" onMouseDown="setControlLook(0, '#CCCCCC')" onMouseOver="setControlLook(0, '#EEEEEE')" onMouseOut="setControlLook(0, '#FFFFFF')">
							<div class="rect"><img src="images/square.png" alt="rectangle" height="22" width="24">
							</div>
						</div> 
						<div class="ctr_btn" id="btn_1" onclick="setCPDrawAction(1)" onMouseDown="setControlLook(1, '#CCCCCC')" onMouseOver="setControlLook(1, '#EEEEEE')" onMouseOut="setControlLook(1, '#FFFFFF')">
							<div class="circle"><img src="images/circle.png" alt="circle" height="22" width="22">
							</div>
						</div> 
						<div class="ctr_btn" id="btn_2" onclick="setCPDrawAction(2)" onMouseDown="setControlLook(2, '#CCCCCC')" onMouseOver="setControlLook(2, '#EEEEEE')" onMouseOut="setControlLook(2, '#FFFFFF')">clear
						</div> 
			<br>
						<div class="ctr_btn" id="btn_9" onclick="canvasAnimator.newAnimation();" onMouseDown="setControlLook(9, '#CCCCCC')" onMouseOver="setControlLook(9, '#EEEEEE')" onMouseOut="setControlLook(9, '#FFFFFF')">new
						</div> 
	
		 				<button value="Save" style="{z-index:999}" id="saveViewButton">Save</button>
				<!--		<button value="Move" style="{z-index:999}" id="moveImageButton">Move</button> -->
					</div>
					</div>


    </body>
</html>
