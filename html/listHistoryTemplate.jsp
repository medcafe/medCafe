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
	<style>
		td {font-size:0.8em};
		
		.scroll {
		  height:900px;
		  width:400px;
		  overflow:auto;
		}
	</style>
	<script type="text/javascript">
		$(function(){
		
			$.getJSON("<%=url%>", function(data)
			{
  				var html = v2js_listHistoryTemplate( data );  
  				
				$('#templateList').html(html);
				$(this).delay(100,function()
				{
					$('#templateList').jScrollTouch({height:'380',width:'300'});
				});
			});
		
			
		});
		
	</script>
</head>

<body>
		<button value="Save" id="saveButton">Save</button>
		<div id="templateList" ></div>
		

	
</body>
</html>



