<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %><%@
    taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ page import = "java.util.*"%>
<%@ page import = "java.net.URLEncoder"%>
<%@ page import = "java.io.UnsupportedEncodingException"%>
<%@ page import="org.json.JSONObject" %>
<%@ page import = "org.mitre.medcafe.util.*, org.mitre.medcafe.model.*"%>
<%
	
  	String user =  request.getRemoteUser();
  	//Use the user login to save the text
	String[] symptomIds = request.getParameterValues("symptom_check") ;
	System.out.println("History: saveHistory : symptomsId " + symptomIds.length);
					
  	String patientId = request.getParameter(Constants.PATIENT_ID);
	if (patientId == null)
	{
	  	Object patientIdObj = session.getAttribute("patient");
		if (patientIdObj != null)
			patientId = patientIdObj.toString();
	}
		
	if (patientId == null)
	{
		return;
	}

	JSONObject rtnObj = History.saveHistory(patientId, symptomIds);
	System.out.println("History: saveHistory : return " + rtnObj.toString());
	
	response.sendRedirect("listHistoryTemplate.jsp");
%>