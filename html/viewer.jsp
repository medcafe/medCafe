<!DOCTYPE html>
<html>
<%
	String imageName = request.getParameter("image");
	if (imageName == null)
		imageName = "images/patient1/chest-xray.jpg";
%>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>jquery.iviewer test</title>
        <!--<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.3.2/jquery.min.js" ></script>-->
        <script type="text/javascript" src="js/jquery-1.3.2.js" ></script>
        <script type="text/javascript" src="js/jquery.mousewheel.js" ></script>
        <script type="text/javascript" src="js/jquery.iviewer.js" ></script>
        <script type="text/javascript">
            var $ = jQuery;
            $(document).ready(function(){
            	  var server = "<%=imageName%>";
            	 
                  $("#viewer").iviewer(
                       {
                       src: server
                  });
                  
                 
                
            });
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
    </body>
</html>
