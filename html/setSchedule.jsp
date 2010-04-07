<%@ page import="org.mitre.medcafe.util.*,org.mitre.medcafe.model.*, org.json.*, java.util.*,java.text.*,java.sql.*" %><%@
    taglib uri="http://java.sun.com/jstl/core" prefix="c" %><%@
    taglib prefix="tags" tagdir="/WEB-INF/tags" %><%


	DbConnection conn = new DbConnection();
    
	//Get the patient Id
    String patientId = request.getParameter(Constants.PATIENT_ID);
    System.out.println("setSchedule.jsp patient id " + patientId);
	
	if (patientId == null)
		patientId = "1";
	
	//Check if a time- duration is specified if not	
	//Retrieve the earliest available appointment and give 30 minutes
	String apptDateStr = request.getParameter(Schedule.APPT_DATE);
	String apptTimeStr = request.getParameter(Schedule.APPT_TIME);
	
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
   	
	Schedule sched = new Schedule(conn);
	JSONObject appointTime = new JSONObject();
	
	if (apptTimeStr == null)
	{
		apptTimeStr = Schedule.DEFAULT_TIME;
	}	
	
    if (apptDateStr == null)
	{
		String todayDateStr = format.format(new java.util.Date());
		
		appointTime = sched.getNextAvailAppointment(patientId, todayDateStr, apptTimeStr);
					
		//Use this to set the value
		 //out.write(String.valueOf(appointTime));
		 
	}	
	else
	{
		java.util.Date apptDate = Schedule.parseDate(apptDateStr + " " + apptTimeStr, Schedule.DATE_TIME_FORMAT_TYPE);	
		String endTimeStr = request.getParameter(Schedule.END_TIME);
		String calcTimeStr  ="";
		java.sql.Date endTimeSQL = null;
		String durationStr = request.getParameter(Schedule.DURATION);
		
		if ((durationStr == null) && (endTimeStr == null))
		{
			int duration = Schedule.APPT_DURATION;
			
			calcTimeStr = Schedule.addDuration(apptDate, duration);
			
		}
		else if (endTimeStr == null)
		{
			int duration = Integer.parseInt(durationStr);
			calcTimeStr = Schedule.addDuration(apptDate, duration);
			
		}
		else
		{
			//calcTimeStr = Schedule.convertSQLDate(Schedule.parseDate(endTimeStr, Schedule.TIME_ONLY_FORMAT_TYPE  ));
			calcTimeStr = endTimeStr;
			
		}
		appointTime.put( Schedule.APPT_TIME,  apptTimeStr );
		appointTime.put( Schedule.APPT_DATE, apptDateStr );
		appointTime.put( Schedule.END_TIME, calcTimeStr );
		appointTime.put( "allDay", false );
		
	}
		
	int rtn = 0;
	int patient_id = Integer.parseInt(patientId);
			
	JSONObject patientData = Patient.getPatient(patient_id, conn);
			
	if (patientData.get(Patient.ID) == null)
	{
			System.out.println("Error on settign schedule  " + patientData.toString());	   
	}
	String fname="";
	String lname="";
	    
	Object fnameObj = patientData.get(Patient.FIRST_NAME);
	if (fnameObj != null)
		fname = fnameObj.toString();
			
	Object lnameObj = patientData.get(Patient.LAST_NAME);
	if (lnameObj != null)
		lname = lnameObj.toString();
		
			
	appointTime.put( Patient.ID, patient_id);
	appointTime.put( Patient.FIRST_NAME, fname);
	appointTime.put( Patient.LAST_NAME, lname);
	
	JSONObject rtnJson = sched.addAppointment(appointTime);
	System.out.println("Returned JSON from insert into database " + rtnJson.toString());
	System.out.println("setSchedule.jsp added appointment " + appointTime.toString());
	
%>
