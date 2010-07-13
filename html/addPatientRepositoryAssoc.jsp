<%@ page import="org.mitre.medcafe.util.*,org.mitre.medcafe.model.*, org.json.*, java.util.*,java.text.*,java.sql.*" %><%@
    taglib uri="http://java.sun.com/jstl/core" prefix="c" %><%@
    taglib prefix="tags" tagdir="/WEB-INF/tags" %><%


	DbConnection conn = new DbConnection();
    JSONObject rtnObj = new JSONObject();
	//Get the patient Id
    String patientId = request.getParameter(Constants.PATIENT_ID);
    String[] patientRepIds = request.getParameterValues("patient_rep_id");
    String repository = request.getParameter("repository");
    
    for (String patientRepId: patientRepIds)
    {
    	String fullName = request.getParameter("patient_" + patientRepId);
    	//String lastName = request.getParameter(Patient.LAST_NAME);
    	System.out.println("addPatientRepositoryAssoc.jsp patient id " + patientId + " repository " + patientRepId);
	
		Patient patient = new Patient(conn);
		//Set the name parts
		patient.parseFullName(fullName);
		
		//First check if this is a current patient
		String userName =  request.getRemoteUser();
	
		//No patient specified - so this is new
		if (patientId == null)
		{	
			rtnObj = patient.associatePatientRepository( patientId,  patientRepId, repository, userName);			
		}
		else //This patient may already exist
		{
		    rtnObj = patient.isPatient( patientId, patientRepId, repository); 
			boolean hasVal = rtnObj.has("rep_patient_id");
			Object id =null;
			if (hasVal)
			{
			 id = rtnObj.get("rep_patient_id");
			}
			
			if (id != null)
			{
				if (Patient.NO_PATIENT.equals(id))
				{
					rtnObj = patient.associatePatientRepository( patientId,  patientRepId, repository, userName);
					System.out.println("addPatientRepository add assoc for new patient " + rtnObj.toString());
				}	
			}
			else
			{
					rtnObj = patient.associatePatientRepository( patientId,  patientRepId, repository, userName);
					System.out.println("addPatientRepository add assoc for existing patient " + rtnObj.toString());
				
			}
		}
	    
	}
	//patient.closeConnection();
%>
