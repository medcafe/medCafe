<%-- Copyright 2010 The MITRE Corporation (http://www.mitre.org/). All Rights Reserved.  Licensed under the Apache License, Version 2.0 (the "License").  --%>
<%@ page import="org.mitre.medcafe.util.*" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
	String server = "http://" + Config.getServerUrl() ;
	String listWidgets = server + "/widgets-listJSON.jsp";
	System.out.println("listWidgets " + listWidgets);
	String type = request.getParameter("type");

	if (type == null)
		type = Constants.GENERAL_WIDGETS;

	if (type.equals(Constants.PATIENT_WIDGETS))
		listWidgets = server + "/widgets-listJSON.jsp?type=" + Constants.PATIENT_WIDGETS;

	String frameId = type + "Frame";
	String patientId = request.getParameter("patient_id");
	if (patientId == null)
	{
		Object patientObj = session.getAttribute("patient");
		if (patientObj != null)
		 	patientId = patientObj.toString();
	}
	if (patientId == null)
		patientId = "1";

%>
<head>
<meta name = "viewport" content = "user-scalable = no, width =device-width">
<style>
.scroll {
  height:600px;
  width:120px;
  overflow:auto;
}
</style>

<script type="text/javascript" src="${js}/jquery-1.4.4.js"></script>
<script type="text/javascript" src="${js}/jquery-ui-1.8.6.custom.min.js"></script>
<script type="text/javascript" src="${js}/vel2js.js"></script>
<script type="text/javascript" src="${js}/vel2jstools.js"></script>
 <script type="text/javascript" src="${js}/jScrollTouch.js"></script>
<script type="text/javascript" language="javascript" src="${js}/jquery.delay.js"></script>

<script type="text/javascript">

    $(document).ready( function() {

			var isiPad = navigator.userAgent.match(/iPad/i) != null;
			//console.log('Is this an iPad...' + isiPad);

		    $.getJSON("<%=listWidgets%>", function(data)
		    {
		    	alert(JSON.stringify(data));
  				var html = v2js_listWidgets( data );

    			$("#test").append(html);

    			    		if (isiPad)
 		{
    			$('#test').each(function()
	        	{




	            	 this.addEventListener("touchmove", stopTouchMove, false);


	        	});
	        	 }
    			$(this).delay(500,function()
				{
		    			var imageButton = $("#test").find('.imageContain');

						$(imageButton).mousedown(function(event) {

								parent.clearWidgets();
	  							parent.startWidgetDrag($(this),"<%=frameId%>",  isiPad, event );
	  							return false;
							});

						$("#test").jScrollTouch({height:'380',width:'140'});
						alert("jScrollTouch");
				});
			});
	});
	function stopTouchMove(event)
	{
	   var isiPad = navigator.userAgent.match(/iPad/i) != null;
	   if (isiPad)
	   {
		  event.preventDefault();
	   }
	}
</script>
</head>
<body>
<link type="text/css" href="css/custom-theme/jquery-ui-1.8.6.custom.css" rel="stylesheet" />
<link type="text/css" href="css/custom.css" rel="stylesheet" />
<div id="test" ></div>
</body>
</html>
