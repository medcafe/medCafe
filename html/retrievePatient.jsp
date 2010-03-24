<%@ page import="org.mitre.medcafe.util.*" %>
<%@ page import="org.mitre.medcafe.model.*" %>
<%@ page import="org.json.JSONObject" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%	
	String patientId = request.getParameter("patient_id");
	if (patientId == null)
		patientId = "7";
	String user_name =  request.getRemoteUser();
	//Retrieve Widgets
	JSONObject widgetList =  Widget.listWidgets(user_name, patientId);
	session.setAttribute("patient", patientId);
%>
<%=widgetList.toString()%>



