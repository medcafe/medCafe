<!DOCTYPE html>
<html>
<%
	String imageName = request.getParameter("image");
	if (imageName == null)
		imageName = "images/patients/1/chest-xray-marked.jpg";
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
        <!--<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.3.2/jquery.min.js" ></script>-->
        <script type="text/javascript" src="js/jquery-1.3.2.js" ></script>
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
			
		
        <script type="text/javascript">
            var $ = jQuery;
            var canvasPainter;
			var curAction = 0;
            $(document).ready(function(){
            	  var server = "<%=imageName%>";   
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
				  var viewer = $("#viewer").iviewer(
                       {
                       src: server,
                       canvas: canvasPainter
                  });
                  		 
				 
            });
            
            function printError(error) {
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
		    
        </script>
        <link rel="stylesheet" href="css/jquery.iviewer.css" />
        <style>
            .viewer
            {
                width: 100%;
                height: 390px;
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
            
            <div id="viewer" class="viewer"></div>
            <br />
            
        </div>
        
		
		</div>
        <canvas id="canvas" width="400" height="400"></canvas>
		<canvas id="canvasInterface" width="400" height="400"></canvas>
		<div id="chooserWidgets">
			<div id="controls">
			<div class="ctr_btn" id="btn_0" onclick="setCPDrawAction(0)" onMouseDown="setControlLook(0, '#CCCCCC')" onMouseOver="setControlLook(0, '#EEEEEE')" onMouseOut="setControlLook(0, '#FFFFFF')"><div class="rect"><img src="images/square.png" alt="rectangle" height="22" width="24"></img></div></div> 
			<div class="ctr_btn" id="btn_1" onclick="setCPDrawAction(1)" onMouseDown="setControlLook(1, '#CCCCCC')" onMouseOver="setControlLook(1, '#EEEEEE')" onMouseOut="setControlLook(1, '#FFFFFF')"><div class="circle"><img src="images/circle.png" alt="circle" height="22" width="22"></img></div></div> 
			<div class="ctr_btn" id="btn_2" onclick="setCPDrawAction(2)" onMouseDown="setControlLook(2, '#CCCCCC')" onMouseOver="setControlLook(2, '#EEEEEE')" onMouseOut="setControlLook(2, '#FFFFFF')">clear</div> 
			<br>
			<div class="ctr_btn" id="btn_9" onclick="canvasAnimator.newAnimation();" onMouseDown="setControlLook(9, '#CCCCCC')" onMouseOver="setControlLook(9, '#EEEEEE')" onMouseOut="setControlLook(9, '#FFFFFF')">new</div> 
			
			<div id="customWidget">
					<div id="colorSelector2"><div style="background-color: #00ff00"></div></div>
				    <div id="colorpickerHolder2">
			</div>
		</div>
    </body>
</html>
