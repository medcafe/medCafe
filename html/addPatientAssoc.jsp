<%@ page import="org.mitre.medcafe.util.*,org.mitre.medcafe.model.*, org.json.*, java.util.*,java.text.*,java.sql.*" %><%@
    taglib uri="http://java.sun.com/jstl/core" prefix="c" %><%@
    taglib prefix="tags" tagdir="/WEB-INF/tags" %><%



    
	//Get the patient Id
    String patientId = request.getParameter(Constants.PATIENT_ID);
    System.out.println("addPatientAssoc.jsp patient id " + patientId);
	
	if (patientId == null)
		patientId = "1";
	
	String role = request.getParameter("role");
    if (role == null)
		role = "physician";
	Patient patient = new Patient();
	//First check if this is a current patient
	
	String userName =  request.getRemoteUser();
	JSONObject rtnObj = patient.isPatient(userName, patientId); 
	Object id = rtnObj.get("id");
	if (id != null)
	{
		if (Patient.NO_PATIENT.equals(id))
			rtnObj = patient.associatePatient( userName, patientId,  role);
			out.write(rtnObj.toString());
			
	}

	
%>
