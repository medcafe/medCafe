<%@ page import="org.mitre.medcafe.util.*" %>
<%@ page import="org.mitre.medcafe.model.*" %>
<%@ page import="org.json.JSONObject" %>
<%@ page import="java.util.HashMap" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%	
	String patientId = request.getParameter(Constants.PATIENT_ID);
	if (patientId == null)
		patientId = "7";
		
	System.out.println("retrievePatient.jsp getting patient id " + patientId );
	
	String user_name =  request.getRemoteUser();
	//Retrieve Widgets
	
	JSONObject widgetList =  Widget.listWidgets(user_name, patientId);
	Patient.addRecentPatients(user_name, patientId);
	session.setAttribute("patient", patientId);
	
	
	JSONObject repositoryIds = new JSONObject();
	Patient patient = new Patient();
	patient.setConnection();
	repositoryIds = patient.listRepositories(patientId);
	
	session.setAttribute("repPatientIds", repositoryIds);

%>
<%=widgetList.toString()%>



