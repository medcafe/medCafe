<%@ page import="org.mitre.medcafe.util.*" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
 "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<%

	String server = "http://" + Config.getServerUrl() ;
	String patient_id = request.getParameter("patient_id");
	
	if (patient_id == null)
		patient_id = "1";
  
	String listBookmarks = server + "/bookmarksJSON.jsp";
	
	String repository = request.getParameter("repository");
	if (repository == null)
		repository = "OurVista";
	
	listBookmarks = listBookmarks + "?repository=" + repository + "&patient_id=" + patient_id;
%>

<script>
 var tl;
 function onLoad() {
              
    var eventUrl = "<%=listBookmarks%>";
	$.getJSON(eventUrl, function(data)
 	{
 			var html = v2js_listPatientsBookmarksTable( data );  	  					
						
			var patientId = "<%=patient_id%>";				
			$("#bookmarks").append(html);
	  										
			$("#bookmarks" + patientId).dataTable( {
								"aaSorting": [[ 0, "desc" ]]
			} );
              
    });
            
 }
</script>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
	<script type="text/javascript" src="${js}/jquery-1.3.2.js"></script>
	<script type="text/javascript" src="js/vel2jstools.js"></script>
	<script type="text/javascript" src="js/vel2js.js"></script>
	<script type="text/javascript" language="javascript" src="js/jquery.dataTables.js"></script>
</head>
<body onload="onLoad();" onresize="onResize();">
<div id="bookmarks"></div>
<noscript>
This page uses Javascript to show you a Timeline. Please enable Javascript in your browser to see the full page. Thank you.
</noscript>
</body>
</html>