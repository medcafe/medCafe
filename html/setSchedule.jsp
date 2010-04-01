<%@ page import="org.mitre.medcafe.util.*,org.mitre.medcafe.model.*, org.json.*, java.util.*,java.text.*,java.sql.*" %><%@
    taglib uri="http://java.sun.com/jstl/core" prefix="c" %><%@
    taglib prefix="tags" tagdir="/WEB-INF/tags" %><%


	DbConnection conn = new DbConnection();
    
	//Get the patient Id
    String patientId = request.getParameter(Constants.PATIENT_ID);
	if (patientId == null)
		patientId = "1";
	
	//Check if a time- duration is specified if not	
	//Retrieve the earliest available appointment and give 30 minutes
	String apptDateStr = request.getParameter(Schedule.APPT_DATE);
	String apptTimeStr = request.getParameter(Schedule.APPT_TIME);
	
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
   	
	Schedule sched = new Schedule(conn);
		
	if (apptTimeStr == null)
	{
		apptTimeStr = Schedule.DEFAULT_TIME;
	}	
	
    if (apptDateStr == null)
	{
		String todayDateStr = format.format(new java.util.Date());
	
		JSONObject appointTime = sched.getNextAvailAppointment(patientId, todayDateStr, apptTimeStr);
		//Use this to set the value
		 out.write(String.valueOf(appointTime));
		 //sched.addAppointment(appointTime);
	}	
	else
	{
		java.util.Date apptDate = Schedule.parseDate(apptDateStr + " " + apptTimeStr, Schedule.DATE_TIME_FORMAT_TYPE);	
		String endTimeStr = request.getParameter(Schedule.END_TIME);
		String calcTimeStr  ="";
		java.sql.Date endTimeSQL = null;
		String duration = request.getParameter(Schedule.DURATION);
		if ((duration == null) && (endTimeStr == null))
		{
			duration = Schedule.APPT_DURATION;
			
			calcTimeStr = Schedule.addDuration(apptDate, duration);
			System.out.println("setSchedule.jsp no end date or duration specified " + calcTimeStr);
	   
		}
		else if (endTimeStr == null)
		{
			calcTimeStr = Schedule.addDuration(apptDate, duration);
			System.out.println("setSchedule.jsp no end date specified " + calcTimeStr);
	   
		}
		else
		{
			//calcTimeStr = Schedule.convertSQLDate(Schedule.parseDate(endTimeStr, Schedule.TIME_ONLY_FORMAT_TYPE  ));
			calcTimeStr = endTimeStr;
			System.out.println("setSchedule.jsp  end date and duration specified " + calcTimeStr);
	   
		}
		System.out.println("setSchedule.jsp got end Time " + calcTimeStr);
	   
		String name = "Tester";
		JSONObject appointTime = new JSONObject();
	    appointTime.put( "title", name);
	    appointTime.put( "start", apptDateStr + " " + apptTimeStr );
	    appointTime.put( "end",  apptDateStr + " "  + calcTimeStr );
	    appointTime.put( "allDay", false );
	    //sched.addAppointment(appointTime);
	    System.out.println("setSchedule.jsp added appointment " + appointTime.toString());
	   
    }
	
%>
