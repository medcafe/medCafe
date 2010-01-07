<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
	String listWidgets = "c/treenode?relurl=/widgets";
%>
<script type="text/javascript" src="${js}/jquery-1.3.2.js"></script>
	
<script>
$(document).ready( function() {
		 				
	var imageButton = $(this).find('.imageContain');
	$(imageButton).bind("click",{},
	function(e)
	{
		var text = $(this).find('p').text();
		
		var label = $(this).find('img').attr("src");
		var link = $(this).find('img').attr("custom:url");
	
		alert("Image text " + text + " label " + label + " link " + link); 
		parent.createLink(1,link, text, "chart" );
		//function createLink(patientId, link, label, type) 
		    	
		
	});			
});

</script>

<body>
<link type="text/css" href="css/custom-theme/jquery-ui-1.7.2.custom.css" rel="stylesheet" />
<link type="text/css" href="css/custom.css" rel="stylesheet" />
<tags:IncludeRestlet relurl="<%=listWidgets%>"/>

</body>
</html>