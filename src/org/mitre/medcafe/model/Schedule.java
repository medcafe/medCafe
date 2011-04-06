/*
 *  Copyright 2010 The MITRE Corporation (http://www.mitre.org/). All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mitre.medcafe.model;

import java.io.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex .*;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

import org.json.JSONException;
import org.json.JSONObject;
import org.mitre.medcafe.util.DbConnection;
import org.mitre.medcafe.util.DatabaseUtility;
import org.mitre.medcafe.util.WebUtils;

/**
 *  Representation of the text data
 *  @author: Gail Hamilton
 */
public class Schedule
{
	public final static String KEY = Schedule.class.getName();
	public final static Logger log = Logger.getLogger( KEY );
	// static{log.setLevel(Level.FINER);}

	//Parameters that are common to all Widgets
	private int patientId =0;
	private String startTime ="";
	private String startDate ="";
	private String endTime ="";
	private String duration ="";


	//All the other parameters
	private  HashMap<String, String> params = new HashMap<String, String>();

	public static final int APPT_DURATION = 30;
	public static final String ID = "patient_id";
	public static final String APPT_TIME = "appoint_time";
	public static final String APPT_DATE = "appoint_date";
	public static final String END_TIME = "end_time";
	public static final String DURATION = "duration";
	public static final String DATE_FORMAT = "MM/dd/yyyy";
	public static final String TIME_FORMAT = "HH:mm:ss";
	public static final String SQL_TIME_FORMAT = "HH24:MI:SS";
	public static final String DATE_TIME_FORMAT = "M/d/yyyy H:m";
	public static final String USER = "username";

	public static final String DEFAULT_TIME = "08:00:00";
	//public static final String SELECT_AVAILABLE_APPOINTMENT= "select id,  last_name || ', ' || first_name as name, appoint_time, end_time from schedule  where appoint_date = ? order by appoint_date, appoint_time ";
	public static final String SELECT_AVAILABLE_APPOINTMENT= "select id,  last_name , first_name , appoint_time, end_time from schedule  where appoint_date =  ? order by appoint_date, appoint_time ";
	public static final String INSERT_APPOINTMENT = "INSERT INTO schedule  ( username, patient_id, first_name, last_name, appoint_date, appoint_time, end_time ) values (?,?,?,?,to_date(?, '" +  DATE_FORMAT +"'), 	to_timestamp(?,'" +  SQL_TIME_FORMAT +"'),	to_timestamp(?,'" + SQL_TIME_FORMAT + "') ) ";
	public static final String DELETE_APPOINTMENT= "DELETE FROM schedule where ( patient_id=? AND appoint_date=? AND appoint_time=? ) ";
	public static final String DELETE_APPOINTMENT_BY_ID= "DELETE FROM schedule where ( id=? ) ";
	//public static final String ADD_TIME = "update schedule set appoint_time = appoint_time+interval '" + minuteDelta + " minutes', end_time = end_time+interval '" + minuteDelta + " minutes' where id=?";


	public static final int DATE_ONLY_FORMAT_TYPE = 0;
	public static final int TIME_ONLY_FORMAT_TYPE = 1;
	public static final int DATE_TIME_FORMAT_TYPE = 2;


	public Schedule()
	{
		super();

	}

	public static DbConnection setConnection() throws SQLException
	{

		return new DbConnection();
	}

	public static DbConnection getConnection() throws SQLException
	{
		return new DbConnection();
	}

	public static void closeConnection(DbConnection dbConn) throws SQLException
	{
		dbConn.close();
	}

	public JSONObject toJSON() throws JSONException
	{
		 JSONObject o = new JSONObject();
		 o.put(Schedule.ID, this.getPatientId());
		 o.put(Schedule.APPT_TIME, this.getStartTime());
		 o.put(Schedule.APPT_DATE, this.getStartDate());
		 o.put(Schedule.END_TIME, this.getEndTime());
		 o.put(Schedule.DURATION, this.getDuration());

		 return o;

	}


	public static JSONObject addAppointment( JSONObject appointment) throws SQLException
	{
		log.finer("Schedule addAppointment start");
		JSONObject ret = new JSONObject();
		DbConnection dbConn = setConnection();

		//Get the patient Name
		try
		{
			//"INSERT INTO schedule  ( patient_id, first_name, last_name, appoint_date, appoint_time, end_time ) values (?,?,?,?,?,?) ";

			String insertQuery = Schedule.INSERT_APPOINTMENT;
			log.finer("Schedule addAppointment insert query " + insertQuery);

            String username = appointment.getString( Schedule.USER );
			String patientIdStr= appointment.getString(Patient.ID);
			int patient_id = Integer.parseInt(patientIdStr);
			String fname = appointment.getString(Patient.FIRST_NAME);
			String lname = appointment.getString( Patient.LAST_NAME);
			String appt_dateStr = appointment.getString( Schedule.APPT_DATE);
			String appt_timeStr = appointment.getString( Schedule.APPT_TIME);
			String end_timeStr = appointment.getString( Schedule.END_TIME);

			String err_mess = "Could not insert appointment for patient " + lname + ", " + fname;

			int rtn = dbConn.psExecuteUpdate(insertQuery, err_mess , username, patient_id, fname, lname, appt_dateStr, appt_timeStr, end_timeStr);

			if (rtn < 0 )
			{
				dbConn.close();		
				return WebUtils.buildErrorJson( "Problem on inserting schedule data into the database ." );
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			return WebUtils.buildErrorJson( "Add Appointment : Problem on patient id " + e.getMessage() );
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			return WebUtils.buildErrorJson( "Add Appointment : Problem on building JSON " + e.getMessage() );
		}
		finally
		{
			dbConn.close();
		}
		return ret;
	}
	public static JSONObject getNextAvailAppointment( String patientId, String dateStr, String timeStr) throws SQLException
	{

		DbConnection dbConn = setConnection();
		 JSONObject o = new JSONObject();
		 PreparedStatement prep = null;
		 ResultSet rs = null;
		//Get the patient Name
		try
		{
			//public static final String SELECT_AVAILABLE_APPOINTMENT= "select id,  last_name || ', ' || first_name as name, appoint_time, end_time from schedule order by appoint_date, appoint_time where appoint_date = ? and appoint_time > ?";

			String selectSql = Schedule.SELECT_AVAILABLE_APPOINTMENT;

			prep = dbConn.prepareStatement(selectSql);
	        try {

				Date date = new java.util.Date();

				Date  prevApptEnd, apptTime = null, apptEnd = null, possApptEndTime = null;

				date = Schedule.parseDate(dateStr, DATE_ONLY_FORMAT_TYPE);
				prevApptEnd = Schedule.parseDate(dateStr + " " + DEFAULT_TIME, DATE_TIME_FORMAT_TYPE);

				log.finer("Schedule: getNextAvailAppointment : prevAppt initialize  " + prevApptEnd.toString());

				//time = Schedule.parseDate(timeStr, TIME_ONLY_FORMAT_TYPE);
				java.sql.Date sqlDate = Schedule.convertSQLDate(date);

				prep.setDate(1, sqlDate);
				//prep.setDate(2, sqlTime);
				log.finer("Schedule: getNextAvailAppointment : query " + prep.toString());
				rs = prep.executeQuery();
				boolean hasAppoint = false;
				while (rs.next())
				{
					 hasAppoint = true;
					 Calendar cal = new GregorianCalendar();
					 log.finer("Schedule: getNextAvailAppointment : prevAppt  " + prevApptEnd.toString());

					 cal.setTime(prevApptEnd);
					 cal.add(Calendar.MINUTE, Schedule.APPT_DURATION );
					 possApptEndTime = getFullDate(date, cal.getTime());
					 log.finer("Schedule: getNextAvailAppointment : Appointment end time " + possApptEndTime.toString());

					 //check to see if the prev end Time + 30 mins is available
					 apptTime = rs.getTime(Schedule.APPT_TIME);
					 apptEnd = rs.getTime(Schedule.END_TIME);

					 //Get the full date to compare against
					 Date newAppt = getFullDate(date, apptTime);
					 cal.setTime(prevApptEnd);
					 cal.add(Calendar.MINUTE, 1 );

					 //Take a minute off
					 Date testDate = getFullDate(date, cal.getTime());

					log.finer("Schedule: getNextAvailAppointment : new Appointment  " + newAppt);

					 if (possApptEndTime.before(testDate))
					 {
						 log.finer("Schedule: getNextAvailAppointment : found a time  " + possApptEndTime.toString());

						 o.put( Patient.ID, patientId );
						 o.put( Schedule.APPT_DATE, dateStr );
						 String newApptStartStr = Schedule.parseDate(prevApptEnd,TIME_ONLY_FORMAT_TYPE) ;
						 String newApptEndStr = Schedule.parseDate(possApptEndTime,TIME_ONLY_FORMAT_TYPE) ;
						 o.put( Schedule.APPT_TIME,  newApptStartStr );
						 o.put( Schedule.END_TIME,   newApptEndStr );

					     o.put( "allDay", false );
					     return o;
					 }
					 prevApptEnd = apptEnd;
				}
				//If there is no scheduled appointments - just use the first one
				if (!hasAppoint)
				{
					log.finer("Schedule: getNextAvailAppointment : first appt of the day  ");

					 o.put( Patient.ID, patientId );
					 o.put( Schedule.APPT_DATE, dateStr );
					 o.put( Schedule.APPT_TIME,  DEFAULT_TIME );
					 Date defaultTime = Schedule.parseDate(DEFAULT_TIME,TIME_ONLY_FORMAT_TYPE);
					 o.put( Schedule.END_TIME,   addDuration(defaultTime, APPT_DURATION ) );

				     o.put( "allDay", false );
				}
				else
				{
					 String newApptStartStr = Schedule.parseDate(possApptEndTime,TIME_ONLY_FORMAT_TYPE) ;

					 Calendar cal = new GregorianCalendar();
					 cal.setTime(possApptEndTime);
					 cal.add(Calendar.MINUTE, Schedule.APPT_DURATION);
					 Date newApptEndTime = getFullDate(date, cal.getTime());
					 String newApptEndStr = Schedule.parseDate(newApptEndTime,TIME_ONLY_FORMAT_TYPE) ;

					 o.put( Patient.ID, patientId );
					 o.put( Schedule.APPT_DATE, dateStr );
					 o.put( Schedule.APPT_TIME,  newApptStartStr );
					 o.put( Schedule.END_TIME,   newApptEndStr );

				     o.put( "allDay", false );
				}
				//addAppointment(o);
				int rtn = 0;
				if (rtn < 0 )
				{
					DatabaseUtility.close(rs);
					DatabaseUtility.close(prep);
					dbConn.close();
					return WebUtils.buildErrorJson( "Problem on getting appointment data from database." );
				}
			}
			catch (ParseException e)
			{
				// TODO Auto-generated catch block
				return WebUtils.buildErrorJson( "Problem on parsing the date " + dateStr  + " for next available appointment ");
			}
			catch (JSONException e) {
				// TODO Auto-generated catch block
				return WebUtils.buildErrorJson( "Problem on creatig JSON data for ext available appointment " );
			}
		}
		finally
		{
			DatabaseUtility.close(rs);
			DatabaseUtility.close(prep);
			dbConn.close();
		}
		return o;
	}

	private static Date getFullDate(Date date, Date time)
	{
		Calendar newDate = new GregorianCalendar();
		Calendar newTime = new GregorianCalendar();

		newDate.setTime(date);
		newTime.setTime(time);

		newDate.set(Calendar.HOUR, newTime.get(Calendar.HOUR));
		newDate.set(Calendar.MINUTE, newTime.get(Calendar.MINUTE));
		newDate.set(Calendar.SECOND, newTime.get(Calendar.SECOND));
		return newDate.getTime();


	}

	public static JSONObject deleteAppointment( String patientId, String dateTime) throws SQLException
	{
		JSONObject ret = new JSONObject();
		DbConnection dbConn = setConnection();

		try
		{
			String deleteQuery = Schedule.DELETE_APPOINTMENT;
			int rtn = 0;
			int patient_id = Integer.parseInt(patientId);
			String err_mess = "Could not delete the widgets for patient  " + patient_id;

			rtn = dbConn.psExecuteUpdate(deleteQuery, err_mess , patient_id, dateTime);

			if (rtn < 0 )
			{
				dbConn.close();
				return WebUtils.buildErrorJson( "Problem on deleting widget data from database ." );
			}
		}
		finally
		{
			dbConn.close();
		}
		return ret;
	}

	public int getPatientId() {
		return patientId;
	}

	public void setPatientId(int patientId) {
		this.patientId = patientId;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public static java.sql.Date convertSQLDate(Date date) throws ParseException
	{
		java.sql.Date sqlDate = new java.sql.Date(date.getTime());
		return sqlDate;
	}

	public static String addDuration(Date startTime, int duration) throws ParseException
	{
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(startTime);

		cal.add(Calendar.MINUTE, duration);
		SimpleDateFormat format = new SimpleDateFormat(Schedule.TIME_FORMAT);

		String rtnDateStr = format.format(cal.getTime());

			//parseDate(format.format(cal.getTime()), TIME_ONLY_FORMAT_TYPE);

		return rtnDateStr;
	}

	public static String parseDate(Date date, int dateFormat) throws ParseException
	{
		if (dateFormat == DATE_ONLY_FORMAT_TYPE)
		{
			SimpleDateFormat format = new SimpleDateFormat(Schedule.DATE_FORMAT);
			String dateStr = format.format(date);
			return dateStr;
		}
		else if (dateFormat == TIME_ONLY_FORMAT_TYPE)
		{
			SimpleDateFormat format = new SimpleDateFormat(Schedule.TIME_FORMAT);
			String dateStr = format.format(date);

			return dateStr;
		}
		else
		{
			SimpleDateFormat format = new SimpleDateFormat(Schedule.DATE_TIME_FORMAT);
			String dateStr = format.format(date);
			//date = format.parse(dateStr);
			return dateStr;
		}
	}

	public static Date parseDate(String dateStr, int dateFormat) throws ParseException
	{
		if (dateFormat == DATE_ONLY_FORMAT_TYPE)
		{
			SimpleDateFormat format = new SimpleDateFormat(Schedule.DATE_FORMAT);
			Date date = format.parse(dateStr);
			return date;
		}
		else if (dateFormat == TIME_ONLY_FORMAT_TYPE)
		{
			SimpleDateFormat format = new SimpleDateFormat(Schedule.TIME_FORMAT);
			Date date = format.parse(dateStr);
			return date;
		}
		else
		{
			SimpleDateFormat format = new SimpleDateFormat(Schedule.DATE_TIME_FORMAT);
			Date date = format.parse(dateStr);
			return date;
		}
	}

	public static Date parseDate(Calendar cal, int dateFormat) throws ParseException
	{
		if (dateFormat == DATE_ONLY_FORMAT_TYPE)
		{
			SimpleDateFormat format = new SimpleDateFormat(Schedule.DATE_FORMAT);
			format.setCalendar(cal);
			Date date = format.getCalendar().getTime();
			return date;
		}
		else if (dateFormat == TIME_ONLY_FORMAT_TYPE)
		{
			SimpleDateFormat format = new SimpleDateFormat(Schedule.TIME_FORMAT);
			format.setCalendar(cal);
			Date date = format.getCalendar().getTime();
			return date;
		}
		else
		{
			SimpleDateFormat format = new SimpleDateFormat(Schedule.DATE_TIME_FORMAT);
			format.setCalendar(cal);
			Date date = format.getCalendar().getTime();
			return date;
		}
	}
}
