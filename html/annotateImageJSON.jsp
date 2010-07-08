<%@ page import="org.mitre.medcafe.model.*, org.mitre.medcafe.util.*, org.json.JSONObject" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%

	System.out.println("annotateJSON: url start");
	String patient_id = request.getParameter(Constants.PATIENT_ID);
	if (patient_id == null)
		patient_id = Constants.DEFAULT_PATIENT;
	
	String fileName = request.getParameter("image");
	System.out.println("annotateJSON: fileName " + fileName);
	
	String user =  request.getRemoteUser();
	JSONObject imageTags = ImageTag.retrieveAnnotations(user, patient_id, fileName);
	
%>
<%=imageTags.toString()%>
