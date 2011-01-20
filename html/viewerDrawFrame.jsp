<!DOCTYPE html>
<html>
<%@ page import = "org.mitre.medcafe.util.*, org.mitre.medcafe.model.*"%>
<%
	String tab_set = request.getParameter("tab_set");
	if (tab_set ==null)
		tab_set = "tabs";
	String widgetId = request.getParameter("widgetId");
	String imageName = request.getParameter("image");
	if (imageName == null)
		imageName = "images/patients/1/chest-xray-marked.jpg";
	String imageTitle = imageName;
	int pos = imageName.lastIndexOf("/") + 1;
	 if (pos > 0)
	 {
	 	imageTitle = imageTitle.substring(pos);

	 }
	
	 
	String patient_id = request.getParameter("patient_id");


	String tab_num = request.getParameter("tab_num");	

	
%>

    <body>
     <iframe frameborder="0" id="<%=tab_set%>iframe<%=widgetId%>" name="<%=tab_set%>iframe<%=widgetId%>" width="720" height="350" 
     src="viewerDraw.jsp?tab_num=<%=tab_num%>&patient_id=<%=patient_id%>&image=<%=imageName%>&tab_set=<%=tab_set%>&widgetId=<%=widgetId%>"/>



    </body>
</html>
