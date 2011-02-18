<%-- Copyright 2010 The MITRE Corporation (http://www.mitre.org/). All Rights Reserved.  Licensed under the Apache License, Version 2.0 (the "License").  --%>
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
	//session.setAttribute("patient", patientId);


%>
<%=widgetList.toString()%>



