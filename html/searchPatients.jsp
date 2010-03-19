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
	<link type="text/css" href="${css}/demo_page.css" rel="stylesheet" />
	<link type="text/css" href="${css}/demo_table.css" rel="stylesheet" />
  	
	<script type="text/javascript" src="${js}/jquery-1.3.2.js"></script>
	<script type="text/javascript" src="${js}/jquery.layout.js"></script>
	<script type="text/javascript" src="${js}/ui.all-1.7.1.js"></script>
	<script type="text/javascript" language="javascript" src="${js}/jquery.delay.js"></script>
	<script type="text/javascript" src="${js}/vel2jstools.js"></script>
	<script type="text/javascript" src="${js}/vel2js.js"></script>
	<script type="text/javascript" src="${js}/jquery.jeditable.js"></script>
	
	<script>
	var outerLayout;

    var tabID;
	/*
	*#######################
	*		 ON PAGE LOAD
	*#######################
	*/
	
	$(function(){
    
    	var serverLink =  "searchPatientsJSON.jsp?";
				
		$("#last_name").blur(function(){
		    $.getJSON(serverLink + "type=last&search_str=" + $(this).val(),
		      function(data)
		      {
		      	  //Check to see if any error message
				  if (data.announce)
				  {
				  	alert("data announce");
					parent.updateAnnouncements(data);
					return;
				  }
				  var html = v2js_listSearchPatientsSelect( data );  	 
				  alert("html output " + html);	
			      $("#list_names").html(html);
		      });
		  });
		
    });
	</script>
	
    
</head>
<body>

<div id="searchPatients">
<form name="searchPatientForm" action="searchPatientsJSON.jsp?type=exact" method="POST">
		<table>
			<tr><td>Last Name</td><td><input type="text" name="last_name" id="last_name"></input></td></tr>
			<tr><td>First Name</td><td><input type="text" name="first_name" id="first_name"></input></td></tr>
				
		</table>	
		<input type="submit" value="Search"></input>	
		<br/>
		<div id="list_names"><select><option value="No names selected">No names selected</option></select></div>
</form>
</div>
		
</body>     		
</html>