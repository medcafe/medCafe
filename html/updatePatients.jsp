<%@ page import="org.mitre.medcafe.util.*" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%

	String server = "http://" + Config.getServerUrl() + "/";
	String user =  request.getRemoteUser();
	String repository = request.getParameter("repository");	
	server = server + "repository-listJSON.jsp";
%>
<head>
<script type="text/javascript" src="${js}/jquery-1.3.2.js"></script>
<script type="text/javascript" src="${js}/vel2jstools.js"></script>
<script type="text/javascript" src="${js}/vel2js.js"></script>
<script>

$(document).ready( function() 
{
	update("<%=server%>", "<%=repository%>");
	
});

function update( server, repId)
{	
		//For testing purposes
		
		var serverLink =  server + "?repository=" + repId;
		$.getJSON(serverLink, function(data)
		{
		
			var output = v2js_listInsertStatements( data ); 
			$("#output").append(output);
		});
		
}
</script>
</head>
<body>
<h2>Insert Statements</h2>
<div id="output"></div>
</body>