<%@ page import="org.mitre.medcafe.util.*" %>
<%@ page import="org.mitre.medcafe.model.*" %>
<%@ page import="org.json.JSONObject" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%	
	
%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
	<head>
	<meta http-equiv="content-type" content="text/html; charset=utf-8" />
	<title>Associate Patient</title>
	<script type="text/javascript" src="js/jquery-1.3.2.js"></script>
	<script type="text/javascript" src="js/ui.all-1.7.1.js"></script>
 	<script type="text/javascript" src="js/vel2js.js"></script>
    <script type="text/javascript" src="js/vel2jstools.js"></script>
    <script type="text/javascript" src="${js}/medCafe.repository.js"></script>
	<link type="text/css" href="css/custom-theme/jquery-ui-1.7.2.custom.css" rel="stylesheet" />	
	<link type="text/css" href="${css}/custom.css" rel="stylesheet" />
	<link type="text/css" rel="stylesheet" href="${css}/treeview/screen.css" />
	<style>
		td {font-size:0.8em};
	</style>
	<script>
	var repository;
	$(function()
	{
		listPatientSearchRepositories();
		initializeSearchPatient();
		initializeAssociatePatient();
	});
	
	function listPatientSearchRepositories()
	{
		var link = "c/repositories";
			$("#listRepositories").html("");
					
			$.getJSON(link, function(data)
			{
				//alert("data " + data);
				var html = v2js_listRepositorySelect( data );  	  			
	  			$("#listRepositories").html(html);
	  				
				$("#repositorySelect").change(function() 
				{
					repository = $("option:selected", this).val();   
						  				
			    });
	  		});
	}
	
	function associatePatient()
	{
		var serverLink ="addPatientRepositoryAssoc.jsp";
		var inputVals = $("#returnedPatientlist :input");
		var append ="?";
  		for (i=0; i < inputVals.length; i++)
  		{
  				
  				serverLink = serverLink + append + inputVals[i].name + "=" + inputVals[i].value;
  				append = "&";
  		}
  		serverLink = append +  "repository=" + repository;
  		alert("searchRepositoryPatient : associatePatient server link " + serverLink);
  		$.get(serverLink,function(data)
		{
		    //Check to see if any error message

			if (data.announce)
			{
				var html = v2js_announcements(data);
				alert("searchRepositoryPatient : announce error "   + html);	
				return;
			}	     
		});
	}
	
	function searchPatientRepository()
	{
		var serverLink ="repositoryPatient-listJSON.jsp";
		var inputVals = $("#searchPatientParams :input");
		var append ="?";
  		for (i=0; i < inputVals.length; i++)
  		{
  				
  				serverLink = serverLink + append + inputVals[i].name + "=" + inputVals[i].value;
  				append = "&";
  		}
  		
		serverLink = serverLink + "&repository=" + repository;
		
		$.getJSON(serverLink,function(data)
		      {
		      	  //Check to see if any error message

				  if (data.announce)
				  {
				  	var html = v2js_announcements(data);
				  	alert("searchRepositoryPatient : announce error "   + html);	
					return;
				  }
				  var html = v2js_listPatientsSearchResults( data );

			      $("#patient_list").html( html);
			     
		      });
	
	}
	
	function initializeSearchPatient()
	{
		$("#patientSearchBtn").click(function(event,patient_id){
	 
	 		
			searchPatientRepository();
	
		});
		
	}
	
	function initializeAssociatePatient()
	{
		$("#associatePatientBtn").click(function(event,patient_id){
	 
	 		
			associatePatient();
	
		});
		
	}
	</script>
</head>

<body>
	<div class="ui-widget-header ui-corner-all">
         <center><h2 id="patient-search">Repository Patient Search</h6></center></h2>
    </div>
	<div class="ui-widget-content ui-corner-all">
         <p>
        	<div class="ui-state-highlight ui-corner-all" style="padding: .7em;" id="searchPatientParams">
			<table>
			<tr><td>Patient Repository Id</td><td><input type="text" name="patient_rep_id" value="10"></input></td></tr>
			<tr><td>Repository</td><td><p><div id="listRepositories"></div></td></tr>
			<tr><td>First Name</td><td><input type="text" name="first_name" value=""></input></td></tr>
			<tr><td>Middle Initial</td><td><input type="text" name="middle_initial" value=""></input></td></tr>
			<tr><td>Last Name</td><td><input type="text" name="last_name" value=""></input></td></tr>
			</table>
			<input type="button" value="Search" id="patientSearchBtn"></input><br/>
			
			</div>
		</p>
			
	</div>
	<div class="ui-widget-header ui-corner-all">
         <center><h2 id="patient-list_results">Repository Listing Results</h6></center></h2>
    </div>
	<div class="ui-widget-content ui-corner-all" id="returnedPatientlist">
		<div>
			<div id="patient_list"></div>
		</div>
		
		<input type="button" value="Add to medCafe" id="associatePatientBtn"></input><br/>
	
	</div>
</body>
</html>



