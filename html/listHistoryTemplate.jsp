<%-- Copyright 2010 The MITRE Corporation (http://www.mitre.org/). All Rights Reserved.  Licensed under the Apache License, Version 2.0 (the "License").  --%>
<%@ page import="org.mitre.medcafe.util.*" %>
<%@ page import="org.mitre.medcafe.model.*" %>
<%@ page import="org.json.JSONObject" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%
	//Get a list of the Symptoms in a checkbox
	String url = "listHistoryTemplateJSON.jsp";
	String patientId = request.getParameter(Constants.PATIENT_ID);
	PatientCache cache = (PatientCache) session.getAttribute(PatientCache.KEY);
    if( cache == null )
    {  //nobody is logged in
        //log.warning("No patient selected");
        response.sendRedirect("introPage.jsp");
        return;
    }
    patientId = cache.getDatabasePatientId();

	if (patientId != null)
	{
		url = url + "?" +  Constants.PATIENT_ID + "=" + patientId;
	}
	else
	{
		patientId="";
	}
%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
	<head>
	<meta http-equiv="content-type" content="text/html; charset=utf-8" />
	<title>Symptom List</title>

	<script type="text/javascript">
		$(function(){

			$.getJSON("<%=url%>", function(data)
			{
				var html = v2js_listHistoryTemplate( data );

				$('#templateList').html(html);
	/*			$(this).delay(100,function()
				{
					$('#templateList').jScrollTouch({height:'380',width:'300'});
				});   */
			});


		});

	</script>
</head>

<body>
		<button value="Save" id="saveButton">Save</button>
		<div id="templateList" ></div>



</body>
</html>



