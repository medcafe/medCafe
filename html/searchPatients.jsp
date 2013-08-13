<%-- Copyright 2010 The MITRE Corporation (http://www.mitre.org/). All Rights Reserved.  Licensed under the Apache License, Version 2.0 (the "License").  --%>
<%@ page import="org.mitre.medcafe.util.*" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="org.mitre.medcafe.model.*" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<%
	String server = request.getParameter("server");
	if (server == null)
	{
		server = "noServer";
	}
	String isIntroPage = null;

	isIntroPage= request.getParameter("intro");
	if (isIntroPage == null)
		isIntroPage = "false";

	PatientCache cache = (PatientCache) session.getAttribute(PatientCache.KEY);
    String currentPatient = "-1";

    //This may be the case as may be on intro page where the patient has not, as yet been selected
    if (cache != null)
    {
    	currentPatient = cache.getDatabasePatientId();
    }
	//System.out.println("searchPatients.jsp isIntro " + isIntroPage);

%>
<head>

	<meta http-equiv="Content-type" content="text/html; charset=utf-8" />

	<title>Patient Search</title>
<link type="text/css" href="${css_theme}" rel="stylesheet" />

   	<link type="text/css" href="${css}/custom.css" rel="stylesheet" />
	<link type="text/css" href="${css}/demo_page.css" rel="stylesheet" />
	<link type="text/css" href="${css}/demo_table.css" rel="stylesheet" />
	<link type="text/css" rel="stylesheet" href="${css}/treeview/screen.css" />
	<link type="text/css" rel="stylesheet" href="${css}/jqzoom.css" />
	<link type="text/css" rel="stylesheet" href="${css}/jquery.iviewer.css" />

	<link type="text/css" rel="stylesheet" href="${css}/fg.menu.css" />
	<link type="text/css" rel="stylesheet" href="${css}/fullcalendar.css" />

	<script type="text/javascript" src="${js}/jquery-1.4.4.js"></script>
	<script type="text/javascript" src="${js}/jquery-ui-1.8.6.custom.min.js"></script>
	<script type="text/javascript" language="javascript" src="${js}/jquery.delay.js"></script>
	<script type="text/javascript" src="${js}/vel2jstools.js"></script>
	<script type="text/javascript" src="${js}/vel2js.js"></script>
	<script type="text/javascript" src="${js}/jquery.spinner.js"></script>
	<script type="text/javascript" src="${js}/medCafe.patients.js"></script>


	<script>
	var outerLayout;

    var tabID;
	/*
	*#######################
	*		 ON PAGE LOAD
	*#######################
	*/

	$(function(){

    	var serverLink =  "searchPatientsJSON.jsp?server=<%=server%>";
		setOnSelect("<%=isIntroPage%>","/medcafe", "<%=currentPatient%>","<%=server%>");
		initializePatient(serverLink, "<%=isIntroPage%>", "<%=server%>");
	});



	</script>
</head>
<body class="ui-widget-content">

<div id="searchPatients" class="ui-widget ui-corner-all">
<div class="ui-state-highlight ui-corner-all" style="padding: .7em;">

<form name="searchPatientForm"  >
		<center>
		<table>
			<tr><td>First Name</td><td><input type="text" name="search_str_first" id="first_name"></input></td></tr>
			<tr><td>Last Name</td><td><input type="text" name="search_str_last" id="last_name"></input></td></tr>

		</table>
		<!-- input type="submit" value="Search"></input-->
		<br/>

		<div id="list_names"><select><option value="No names selected">No names selected</option></select></div>
		</center>
		<br/>
		<div id="listPatients"><input id="isPatientChecked" value="isPatient" type="checkbox">Display only my Patients</input></div>
		<div id="addSchedule"></div>
		<div id="addPatient"></div>
		<div id="associatePatient"></div>


</form>
</div>
</div>

</body>
</html>
