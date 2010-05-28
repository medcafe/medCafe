<%@ page import="org.mitre.medcafe.util.*" %>
<%@ page import="org.mitre.medcafe.model.*" %>
<%@ page import="org.json.JSONObject" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%	
	//Get a list of the Symptoms in a checkbox 
	String url = "listHistoryTemplateJSON.jsp";
	String patientId = request.getParameter(Constants.PATIENT_ID);
	if (patientId == null)
	{
		Object patientObj = session.getAttribute("patient");
		if (patientObj != null)
		 	patientId = patientObj.toString();
	}
	
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
	<title>Demo Page: Using Progressive Enhancement to Convert a Select Box Into an Accessible jQuery UI Slider</title>
	<script type="text/javascript" src="js/jquery-1.3.2.js"></script>
	<script type="text/javascript" src="js/ui.all-1.7.1.js"></script>
 	<script type="text/javascript" src="js/vel2js.js"></script>
    <script type="text/javascript" src="js/vel2jstools.js"></script>
    
	<link type="text/css" href="css/custom-theme/jquery-ui-1.7.2.custom.css" rel="stylesheet" />	
	<link type="text/css" href="${css}/custom.css" rel="stylesheet" />
	<link type="text/css" rel="stylesheet" href="${css}/treeview/screen.css" />
	<style>
		td {font-size:0.8em};
	</style>
	<script type="text/javascript">
		$(function(){
		
			$.getJSON("<%=url%>", function(data)
			{
  				var html = v2js_listHistoryTemplate( data );  
  				
				$('#templateList').html(html);
			});
		
			
		});
		
	</script>
</head>

<body>
	<form action="saveHistory.jsp?patient_id=<%=patientId%>">
		<input type="submit" value="Save"></input>
		<div id="templateList"></div>
		
	</form>
	
</body>
</html>



