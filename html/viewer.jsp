<%-- Copyright 2010 The MITRE Corporation (http://www.mitre.org/). All Rights Reserved.  Licensed under the Apache License, Version 2.0 (the "License").  --%>
<!DOCTYPE html>
<html>
<%@ page import = "org.mitre.medcafe.util.*, org.mitre.medcafe.model.*"%>
<%
	String imageName = request.getParameter("image");
	String widgetId = request.getParameter("widgetId");
	String tab_set = request.getParameter("tab_set");
	if (tab_set == null)
		tab_set = "tabs";
	if (imageName == null)
		imageName = "chest-xray.jpg";
	else
	{
		int pos = imageName.lastIndexOf("/") + 1;
	 if (pos > 0)
	 {
	 	imageName = imageName.substring(pos);

	 }
	}
	String tabNum = request.getParameter("tab_num");
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

%>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>jquery.iviewer test</title>
        <script type="text/javascript">
            var $ = jQuery;
          /*  $(document).ready(function(){
            	  var server = "<%=dir%><%=imageName%>";

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

            <div id="<%=tab_set%>viewerImageName<%=widgetId%>"><%=dir%><%=imageName%></div>
            <div id="<%=tab_set%>viewer<%=widgetId%>"  class="viewer"></div>
            <br />

        </div>
    </body>
</html>
