<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<%
	
	%>
<head>

	<meta http-equiv="Content-type" content="text/html; charset=utf-8" />

	<title>Droppable Between Panes</title>

    <link type="text/css" href="${css}/custom-theme/jquery-ui-1.7.2.custom.css" rel="stylesheet" />
  	<link type="text/css" href="${css}/custom.css" rel="stylesheet" />
	
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
	
	
	</script>
	
    
</head>
<body>

<div id="searchPatients" class="ui-widget-header ui-corner-all">
<form name="searchPatientForm"  >
		<table>
			<tr><td>First Name</td><td><input type="text" name="search_str_first" id="first_name"></input></td></tr>
			<tr><td>Last Name</td><td><input type="text" name="search_str_last" id="last_name"></input></td></tr>
	
		</table>	
		<!-- input type="submit" value="Search"></input-->	
		<br/>
		<center>
		<div id="list_names"><select><option value="No names selected">No names selected</option></select></div>
		</center>
		<br/>
		<div id="addSchedule"></div>
		
</form>
</div>
		
</body>     		
</html>