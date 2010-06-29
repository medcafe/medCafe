<!DOCTYPE html>
<html>
<%
	String imageName = request.getParameter("image");
	if (imageName == null)
		imageName = "images/patient1/chest-xray.jpg";
	String tabNum = request.getParameter("tab_num");
	
%>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>jquery.iviewer test</title>
        <script type="text/javascript">
            var $ = jQuery;
            /*$(document).ready(function(){
            	  var server = "<%=imageName%>";
            	 
                  $("#viewer").iviewer(
                       {
                       src: server
                  });
                  
                 
                
            });*/
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
            
            <div id="viewerImageName"><%=imageName%></div>
            <div id="viewer<%=tabNum%>" class="viewer"></div>
            <br />
            
        </div>
    </body>
</html>
