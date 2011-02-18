<%-- Copyright 2010 The MITRE Corporation (http://www.mitre.org/). All Rights Reserved.  Licensed under the Apache License, Version 2.0 (the "License").  --%>
<%@ page import="org.mitre.medcafe.util.*" %>
<%@ page import="org.mitre.medcafe.model.*" %>
<%@ page import="org.json.JSONObject" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%
	//Get a list of the Symptoms in a checkbox
	String url = "listAddressJSON.jsp";
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
	<title>List Address</title>
	<script type="text/javascript" src="js/jquery-1.4.4.js"></script>
	<script type="text/javascript" src="js/jquery-ui-1.8.6.custom.min.js"></script>
 	<script type="text/javascript" src="js/vel2js.js"></script>
    <script type="text/javascript" src="js/vel2jstools.js"></script>

	<link type="text/css" href="css/custom-theme/jquery-ui-1.8.6.custom.css" rel="stylesheet" />
	<link type="text/css" href="${css}/custom.css" rel="stylesheet" />
	<link type="text/css" rel="stylesheet" href="${css}/treeview/screen.css" />
	<style>
		td {font-size:0.8em};
	</style>
	<script type="text/javascript">
		$(function(){

			$.getJSON("<%=url%>", function(data)
			{
  				var html = v2js_listAddress( data );

				$('#addressList').html(html);
			});


		});

	</script>
</head>

<body>
	<form action="saveAddress.jsp?patient_id=<%=patientId%>">
		<input type="submit" value="Save"></input><br/>
		<div class="ui-widget top-panel" style="width:300px" id="patient_address">
		<div class="ui-state-highlight ui-corner-all" style="padding: .3em;" >
		<div id="addressList"></div>
		</div>
		</div>
	</form>

</body>
</html>



