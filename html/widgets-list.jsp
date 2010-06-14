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
		    
			var isiPad = navigator.userAgent.match(/iPad/i) != null;
			//console.log('Is this an iPad...' + isiPad);
			
		    $.getJSON("<%=listWidgets%>", function(data)
		    {
		    	
  				var html = v2js_listWidgets( data );  			
    	
    			$("#test").append(html);
    			
    			$(this).delay(500,function()
				{						
		    			var imageButton = $("#test").find('.imageContain');

						$(imageButton).mousedown(function(event) {
								
								parent.clearWidgets();		
	  							parent.startWidgetDrag($(this),"<%=frameId%>", isiPad, event );
	  							return false;
							});	
							
						if (isiPad)
						{
							console.log('This is an iPad binding touch start ');
								$(imageButton).bind( "touchstart", function(event)
										{
											//console.log('This is an iPad have binded touch start ');	
											parent.clearWidgets();		
  											parent.startWidgetDrag($(this),"<%=frameId%>",isiPad, event );
  											return false;								
										}
								);
								//$(imageButton).medcafeTouch();
								//parent.startWidgetDrag($(this),"<%=frameId%>",isiPad, event );
								
								//$(imageButton).addEventListener("touchmove", touchMove, false);
								//$(imageButton).bind( "touchmove", touchMove);
						}
						else
						{
							
						}
				});  		
			});
	});
	function touchMove(event) 
	{
			console.log('medCafe touchMove: Touch move..start');
			event.preventDefault();
            //console.log('medCafe touchMove: Touch move..start event ' + event.targetTouches.length);
            
			var finalCoord = { x: 0, y: 0 };
           
            finalCoord.x = event.targetTouches[0].pageX // Updated X,Y coordinates
            finalCoord.y = event.targetTouches[0].pageY
            console.log('Touch move..position x: ' + finalCoord.x +' y : ' + finalCoord.y);
            //$("#clone").css( { position: "absolute",  "z-index" : "100", "left": finalCoord.x + "px", "top": finalCoord.y + "px" } );
	  	
 	}
</script>

<body>
<link type="text/css" href="css/custom-theme/jquery-ui-1.7.2.custom.css" rel="stylesheet" />
<link type="text/css" href="css/custom.css" rel="stylesheet" />
<div id="test"></div>
</body>
</html>