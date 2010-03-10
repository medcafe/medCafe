<%@ page import="org.mitre.medcafe.util.*" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
	String server = "http://" + Config.getServerUrl() ;
	String listWidgets = server + "/widgets-listJSON.jsp";
	System.out.println("listWidgets " + listWidgets);
	String type = request.getParameter("type");
	
	if (type == null)
		type = Constants.GENERAL_WIDGETS;
		
	if (type.equals(Constants.PATIENT_WIDGETS))
		listWidgets = server + "/widgets-listJSON.jsp?type=" + Constants.PATIENT_WIDGETS;
	
	String frameId = type + "Frame";
	
%>
<script type="text/javascript" src="${js}/jquery-1.3.2.js"></script>
<script type="text/javascript" src="${js}/ui.core.js"></script>
<script type="text/javascript" src="${js}/ui.draggable.js"></script>
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
						/*$('.imageContain').draggable({
									//connectToSortable: '#sortable',
									containment: 'window',
									helper: 'clone',
									iframeFix : true,
       								start: function(event, ui) 
       								{
       									 //alert("start dragging " + $(this).html() );
       									 var iFrameFix = $(this).draggable('option','iframeFix');
       									 
       								}
    					});*/

						
		    			var imageButton = $("#test").find('.imageContain');
						/*$(imageButton).bind("click",{},
						function(e)
						{
							    
								parent.clearWidgets();
								var text = $(this).find('p').text();
								
								var label = $(this).find('img').attr("src");
								var link = $(this).find('img').attr("custom:url");
								var type = $(this).find('img').attr("custom:type");
								var html = $(this).find('img').attr("custom:html");
								var method = $(this).find('img').attr("custom:method");
								var patientId = $(this).find('img').attr("custom:Id");
							
								parent.createLink(patientId,link, text, type );
								$(this).unbind(e);
								
							
						});	*/
						
						
						$(imageButton).mousedown(function(event) {
							
							parent.clearWidgets();		
  							parent.startWidgetDrag($(this),"<%=frameId%>", event );
  							return false;
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