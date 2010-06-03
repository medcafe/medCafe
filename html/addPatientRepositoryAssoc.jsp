<%@ page import="org.mitre.medcafe.util.*,org.mitre.medcafe.model.*, org.json.*, java.util.*,java.text.*,java.sql.*" %><%@
    taglib uri="http://java.sun.com/jstl/core" prefix="c" %><%@
    taglib prefix="tags" tagdir="/WEB-INF/tags" %><%


	DbConnection conn = new DbConnection();
    JSONObject rtnObj = new JSONObject();
	//Get the patient Id
    String patientId = request.getParameter(Constants.PATIENT_ID);
    String patientRepId = request.getParameter("patient_rep_id");
    String repository = request.getParameter("repository");
    String firstName = request.getParameter(Patient.FIRST_NAME);
    String lastName = request.getParameter(Patient.LAST_NAME);
    System.out.println("addPatientRepositoryAssoc.jsp patient id " + patientId + " repository " + patientRepId);
	
	Patient patient = new Patient(conn);
	patient.setFirstName(firstName);
	patient.setLastName(lastName);
	//First check if this is a current patient
	
	String userName =  request.getRemoteUser();
	
	//No patient specified - so this is new
	if (patientId == null)
	{
		
		rtnObj = patient.associatePatientRepository( patientId,  patientRepId, repository, userName);			
	}
	else
	{
	    rtnObj = patient.isPatient( patientId, patientRepId, repository); 
		Object id = rtnObj.get("rep_patient_id");
		
		if (id != null)
		{
			if (Patient.NO_PATIENT.equals(id))
			{
				rtnObj = patient.associatePatientRepository( patientId,  patientRepId, repository, userName);
				out.write(rtnObj.toString());
			}	
		}
	}
	patient.closeConnection();
	
%>
