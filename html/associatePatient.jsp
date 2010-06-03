<%@ page import="org.mitre.medcafe.util.*" %>
<%@ page import="org.mitre.medcafe.model.*" %>
<%@ page import="org.json.JSONObject" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%	
	//Get a list of the Symptoms in a checkbox 
	String url = "associatePatient.jsp";
	
%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
	<head>
	<meta http-equiv="content-type" content="text/html; charset=utf-8" />
	<title>Associate Patient</title>
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
	
</head>

<body>
	<form action="addPatientRepositoryAssoc.jsp">
		<input type="text" name="patient_rep_id" value="10"></input><br/>
		<input type="text" name="repository" value="OurVista"></input><br/>
		<input type="text" name="first_name" value=""></input><br/>
		<input type="text" name="last_name" value=""></input><br/>
		
		<input type="submit" value="Save"></input><br/>
		
		</div>
		</div>
	</form>
	
</body>
</html>



