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
	
	String patientId = request.getParameter("patient_id");
	
	System.out.println("deleteWidget.jsp about to Delete Widget for patient " + patientId );
		
	Widget.deleteWidgets(patientId, userName);

%>