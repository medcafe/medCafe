<%@ page import="org.mitre.medcafe.util.*" %>
<%@ page import="org.mitre.medcafe.model.*" %>
<%@ page import="org.json.JSONObject" %>
<%@ page import="java.util.HashMap" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%	
	
	//String patientId = request.getParameter("patient_id");
	String patientId = null;
	if (patientId == null)
	{
		Object patientObj = session.getAttribute("patient");
		if (patientObj != null)
		 	patientId = patientObj.toString();
	}
	if (patientId == null)
		patientId = "1";
	System.out.println("retrievePatientRepositoryAssoc.jsp patient id " + patientId );
		
	Object repositoryIdObjs = 	session.getAttribute("repPatientIds");
	JSONObject repositoryIds = new JSONObject();
	if (repositoryIdObjs == null)
	{
		Patient patient = new Patient();
		patient.setConnection();
		repositoryIds = patient.listRepositories(patientId);
		session.setAttribute("repPatientIds", repositoryIds);
	}
	else
	{
		if (repositoryIdObjs instanceof JSONObject)
		{
			repositoryIds = (JSONObject)repositoryIdObjs;
		}
		
	}
	System.out.println("retrievePatientRepositoryAssoc.jsp JSON Object " + repositoryIds.toString());
%>
<%=repositoryIds.toString()%>



