<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %><%@
    taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ page import = "java.util.*"%>
<%@ page import = "java.net.URLEncoder"%>
<%@ page import = "java.io.UnsupportedEncodingException"%>
<%@ page import = "org.mitre.medcafe.util.*"%>
<%@ page import = "org.mitre.medcafe.model.*"%>
<%@ page import = "org.json.JSONObject" %>
<%@ page import = "org.json.JSONException" %>
<%
	
	String userName =  request.getRemoteUser();
	Object patient_idObj = session.getAttribute("patient");
	
	String patient_id = "1";
	if (patient_idObj != null)
		patient_id = patient_idObj.toString();
		
	System.out.println("deleteWidget.jsp about to Delete Widget for patient " + patient_id );
		
	Widget.deleteWidgets(patient_id, userName);

%>