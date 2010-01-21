<%@ page import="org.mitre.medcafe.util.*" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
	String server = "http://" + Config.getServerUrl() ;
	String listWidgets = server + "/widgets-listJSON.jsp";
	System.out.println("listWidgets " + listWidgets);
%>
<script type="text/javascript" src="${js}/jquery-1.3.2.js"></script>
<script type="text/javascript" src="${js}/vel2js.js"></script>
<script type="text/javascript" src="${js}/vel2jstools.js"></script>
<script type="text/javascript" language="javascript" src="${js}/jquery.delay.js"></script>

<script type="text/javascript">

    $(document).ready( function() {
		    
		
		    $.getJSON("<%=listWidgets%>", function(data)
		    {
		    	
  				var html = v2js_listWidgets( data );  			
    	
    			$("#test").append(html);
    			
    			$(this).delay(500,function()
				{
		    			var imageButton = $("#test").find('.imageContain');
						$(imageButton).bind("click",{},
						function(e)
						{
							
								var text = $(this).find('p').text();
								
								var label = $(this).find('img').attr("src");
								var link = $(this).find('img').attr("custom:url");
							
								//alert("Image text " + text + " label " + label + " link " + link); 
								parent.createLink(1,link, text, "chart" );
								//function createLink(patientId, link, label, type) 
							  	
							
						});	
				});  		
			});
	});
</script>

<body>
<link type="text/css" href="css/custom-theme/jquery-ui-1.7.2.custom.css" rel="stylesheet" />
<link type="text/css" href="css/custom.css" rel="stylesheet" />
<div id="test"></div>
</body>
</html>