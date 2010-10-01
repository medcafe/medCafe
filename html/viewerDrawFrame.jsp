<!DOCTYPE html>
<html>
<%@ page import = "org.mitre.medcafe.util.*, org.mitre.medcafe.model.*"%>
<%
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
	String rep_patient_id = request.getParameter("rep_patient_id");

	String tab_num = request.getParameter("tab_num");	

	
%>

    <body>
     <iframe frameborder="0" id="iframe<%=tab_num%>" name="iframe<%=tab_num%>" width="720" height="350" 
     src="viewerDraw.jsp?<%=tab_num%>&patient_id=<%=patient_id%>&rep_patient_id=<%=rep_patient_id%>&image=<%=imageName%>"/>



    </body>
</html>
