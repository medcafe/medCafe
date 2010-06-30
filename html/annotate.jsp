<%
	String imageName = request.getParameter("imageName");
	if (imageName == null)
		imageName = "images/patients/1/mri.jpg";

	String tabNum = request.getParameter("tab_num");

	%>
	<html>
	<head>
		<title>Image Annotations</title>
		
	</head>
	<body>
		<div>
		<img id="toAnnotate<%=tabNum%>" src="<%=imageName%>" alt="<%=imageName%>" width="600" height="398" />
			<!--  img id="toAnnotate" src="images/patients/1/mri.jpg" alt="MRI" width="600" height="398" /-->
		</div>
	</body>
</html>