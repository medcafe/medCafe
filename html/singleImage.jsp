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
	
	 



	String tab_num = request.getParameter("tab_num");	

	
%>

    <body>
     <div id="content"><input id="viewerButton<%=tab_num%>" type="button" value="Viewer"/>

					 <div id="content">
					 <input id="editButton<%=tab_num%>" type="button" value="Annotate"/>
					<a href="<%=imageName%>" class="jqzoom<%=tab_num%>" style="" title="<%=imageTitle%>">
					<img src="<%=imageName%>"  title="<%=imageTitle%>" width="300" style="border: 1px solid #666;">
					</a>
     				 </div>
					</div>

    </body>
</html>
