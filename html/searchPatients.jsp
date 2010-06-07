<%@ page import="org.mitre.medcafe.util.*" %>
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
	String isIntroPage = "false";

	isIntroPage= request.getParameter("intro");
	if (isIntroPage == null)
		isIntroPage = "false";

	System.out.println("searchPatients.jsp isIntro " + isIntroPage);

%>
<head>

	<meta http-equiv="Content-type" content="text/html; charset=utf-8" />

	<title>Droppable Between Panes</title>

    <link type="text/css" href="${css}/custom-theme/jquery-ui-1.7.2.custom.css" rel="stylesheet" />
  	<link type="text/css" href="${css}/custom.css" rel="stylesheet" />
	<link type="text/css" href="${css}/demo_page.css" rel="stylesheet" />
	<link type="text/css" href="${css}/demo_table.css" rel="stylesheet" />
	<link type="text/css" rel="stylesheet" href="${css}/treeview/screen.css" />
	<link type="text/css" rel="stylesheet" href="${css}/jqzoom.css" />
	<link type="text/css" rel="stylesheet" href="${css}/jquery.iviewer.css" />
	<link type="text/css" rel="stylesheet" href="${css}/annotation.css" />
	<link type="text/css" rel="stylesheet" href="${css}/fg.menu.css" />
	<link type="text/css" rel="stylesheet" href="${css}/fullcalendar.css" />

	<script type="text/javascript" src="${js}/jquery-1.3.2.js"></script>
	<script type="text/javascript" language="javascript" src="${js}/jquery.delay.js"></script>
	<script type="text/javascript" src="${js}/vel2jstools.js"></script>
	<script type="text/javascript" src="${js}/vel2js.js"></script>
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
		setOnSelect("<%=isIntroPage%>","http://${server}");
		initialize(serverLink);
	});

	</script>


</head>
<body>

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

</form>
</div>
</div>

</body>
</html>