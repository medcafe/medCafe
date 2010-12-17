<!DOCTYPE html>
<html>
<%
	String imageName = request.getParameter("image");
	String widgetId = request.getParameter("widgetId");
	String tab_set = request.getParameter("tab_set");
	if (tab_set == null)
		tab_set = "tabs";
	if (imageName == null)
		imageName = "images/patient1/chest-xray.jpg";
   imageName = imageName.replaceFirst("4", "35");
	String tabNum = request.getParameter("tab_num");
	
%>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>jquery.iviewer test</title>
        <script type="text/javascript">
            var $ = jQuery;
          /*  $(document).ready(function(){
            	  var server = "<%=imageName%>";
            	 
                  $("#<%=tab_set%>viewer<%=widgetId%>").iviewer(
                       {
                       src: server
                  });
                  
                 
                
            }); */
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
            
            <div id="<%=tab_set%>viewerImageName<%=widgetId%>"><%=imageName%></div>
            <div id="<%=tab_set%>viewer<%=widgetId%>" class="viewer"></div>
            <br />
            
        </div>
    </body>
</html>
