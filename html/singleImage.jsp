<!DOCTYPE html>
<html>
<%@ page import = "org.mitre.medcafe.util.*, org.mitre.medcafe.model.*"%>
<%
	String imageName = request.getParameter("image");
	String tab_set = request.getParameter("tab_set");
	if (tab_set == null)
		tab_set = "tabs";
	String widgetId = request.getParameter("widgetId");
	if (imageName == null)
		imageName = "images/patients/1/chest-xray-marked.jpg";
 	String imageTitle = imageName;
	int pos = imageName.lastIndexOf("/") + 1;
	 if (pos > 0)
	 {
	 	imageTitle = imageTitle.substring(pos);

	 }
	 imageName = imageTitle;
	String patientId =  request.getParameter("patient_id");
		
	PatientCache cache = (PatientCache) session.getAttribute(PatientCache.KEY);
    if( cache == null )
    {  //nobody is logged in
        //log.warning("No patient selected");
        response.sendRedirect("introPage.jsp");
        return;
    }
    patientId = cache.getDatabasePatientId();
  
	String dir = "images/patients/" + patientId + "/";	
	 



	String tab_num = request.getParameter("tab_num");	

	
%>

    <body>
     <div id="<%=tab_set%>content<%=widgetId%>"><input id="<%=tab_set%>viewerButton<%=widgetId%>" type="button" value="Viewer"/>
		 <input id="<%=tab_set%>editButton<%=widgetId%>" type="button" value="Annotate"/>
		 <br>
	<div>
			
					<a href="<%=dir%><%=imageName%>" class="<%=tab_set%>jqzoom<%=widgetId%> jqzoom" style="" title="<%=imageName%>">
					<img src="<%=dir%><%=imageName%>" width="300" style="border: 1px solid #666;" title="<%=imageName%>">
					</a>
     	
					</div>
</div>
    </body>
</html>
