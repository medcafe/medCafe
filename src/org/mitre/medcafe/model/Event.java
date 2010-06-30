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
import java.text.DateFormat;
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
import org.mitre.medcafe.restlet.PatientListResource;
import org.mitre.medcafe.restlet.Repositories;
import org.mitre.medcafe.util.Constants;
import org.mitre.medcafe.util.DbConnection;
import org.mitre.medcafe.util.Text;
import org.mitre.medcafe.util.WebUtils;
import org.restlet.ext.json.JsonRepresentation;

/**
 *  Representation of the text data
 *  @author: Gail Hamilton
 */
public class Event
{
	public final static String KEY = Event.class.getName();
	public final static Logger log = Logger.getLogger( KEY );
	static{log.setLevel(Level.FINER);}

	//Parameters that are common to all MedCafe Files
	private int patientId =0;
	private int id =0;
	private String icon ="";
	private String title ="";
	private String fileUrl ="";
	private String type ="";
	private Date eventDate =new Date();
	private String link ="";
	
	public static final String PATIENT_ID = "patient_id";
	public static final String TITLE = "title";
	public static final String ICON = "icon";
	public static final String DATE = "file_date";
	public static final String FILENAME = "filename";
	public static final String TYPE = "type";
	
	public static final String SORT_BY = " ORDER BY file_date DESC ";
	
	public static final String DATE_FORMAT = "yyyy,MM,dd";//2008,6,08
	public static final String SQL_DATE_FORMAT = "YYYY-MM-DD";
	
	public static final String NOTE_TYPE = "Records";
	public static final String APPT_TYPE = "Visits";
	public static final String FILE_TYPE = "Images";
	public static final String SYMPTOMS_TYPE = "Symptoms";
	public static final String PROBLEMS_TYPE = "Problems";
	public static final String HOSPITAL_TYPE = "Hospital";
	
	
	private static DbConnection dbConn = null;
	public Event()	
	{
		super();
	
	}
	
	public static DbConnection setConnection() throws SQLException
	{
		if (dbConn == null)
			dbConn= new DbConnection();
		return dbConn;
	}
	
	public static DbConnection getConnection() throws SQLException
	{
		return dbConn;
	}
	
	public static void closeConnection() throws SQLException
	{
		dbConn.close();
		dbConn = null;
	}
	
	public JSONObject toJSON() throws JSONException
	{
		 JSONObject o = new JSONObject();
		 o.put(Event.PATIENT_ID, this.getPatientId());	 		 
		 return o;
		 
	}
	
	public static String[] getEventList()
	{
		return new String[]{Event.SYMPTOMS_TYPE,Event.PROBLEMS_TYPE,Event.APPT_TYPE,Event.HOSPITAL_TYPE,Event.FILE_TYPE,Event.NOTE_TYPE};
	}
	
	public static ArrayList<Event> retrieveEvents(String userName, String patientId, String startDateStr, String endDateStr, String[] eventTypes) throws SQLException, ParseException
	{
		ArrayList<Event> eventList = new ArrayList<Event>();
		System.out.println("Event : retrieveEvents: getIcons start " );
    	
		try 
		 {
			HashMap<String,String> icons = getIcons();
			System.out.println("Event : retrieveEvents: getIcons " + icons.size());
        	
			for (String type: eventTypes)
			{
				System.out.println("Event : retrieveEvents: event type " + type);
	        	
				String icon = icons.get(type);
				
				if (type.equals(Event.FILE_TYPE))
				{
					String sql = getImageEventSQL();
					ArrayList<Event> newEventList = retrieveEventsFromLocal(sql, userName,  patientId,  startDateStr,  endDateStr, eventTypes,  icon,  type) ;	
					eventList.addAll(newEventList);
					
				}
				else if (type.equals(Event.APPT_TYPE))
				{
					String sql = getAppointmentEventSQL();
					ArrayList<Event> newEventList = retrieveEventsFromLocal(sql, userName,  patientId,  startDateStr,  endDateStr, eventTypes,  icon,  type) ;	
					eventList.addAll(newEventList);
					
				}
				else if (type.equals(Event.NOTE_TYPE))
				{
					String sql = getNoteEventSQL();
					ArrayList<Event> newEventList = retrieveEventsFromLocal(sql, userName,  patientId,  startDateStr,  endDateStr, eventTypes,  icon,  type) ;	
					eventList.addAll(newEventList);
					
				}
				else if (type.equals(Event.HOSPITAL_TYPE))
				{
					
				}
				else if (type.equals(Event.PROBLEMS_TYPE))
				{
					
				}
				else if (type.equals(Event.SYMPTOMS_TYPE))
				{
					
				}
			}
			System.out.println("Event : retrieveEvents: finished ");
        	
		 } 
		 finally
		 {
			 
			 //dbConn.close();
			 //dbConn = null;
		 }
		 return eventList;
		
	}
	
	private static String getNoteEventSQL()
	{
		String select = "SELECT subject as title,  note_added from user_text where username = ? and patient_id = ? ";
    	return select;
	}
	
	private static String getImageEventSQL()
	{
		String select = "SELECT title, file_date, filename, thumbnail  from file where username = ? and patient_id = ?  ";
    	return select;
	}
	
	private static String getAppointmentEventSQL()
	{
		String select = "select  title , appoint_date, appoint_time from schedule  where username = ? and patient_id = ?  ";
    	return select;
	}
	
	private static ArrayList<Event> retrieveEventsFromLocal(String sql,String userName, String patientId, String startDateStr, String endDateStr, String[] eventTypes, String icon, String type) throws SQLException, ParseException
	{
		ArrayList<Event> eventList = new ArrayList<Event>();
		
		
			int patId = Integer.valueOf(patientId);
			if (dbConn == null)
				dbConn= new DbConnection();
	
			PreparedStatement prep=null;
			DateFormat df = new SimpleDateFormat(DATE_FORMAT);
			Date startDate = null;
			Date endDate = null;
			
			int startDatePos = 2, endDatePos = 2;
			     
			prep= dbConn.prepareStatement(sql);
						
			if (startDate != null)
			{
						 java.sql.Date sqlStartDate = new java.sql.Date(startDate.getTime());			 
						 prep.setDate(startDatePos, sqlStartDate);
			}
					 
			if (endDate != null)
			{
						 java.sql.Date sqlEndDate = new java.sql.Date(endDate.getTime());
						 prep.setDate(endDatePos, sqlEndDate);
			}
	
			prep.setString(1, userName);
			prep.setInt(2, patId);
			ResultSet rs =  prep.executeQuery();
	
			System.out.println("Medcafe Event JSON Events retrieveEvents sql" + prep.toString());
	            
			//This lists all the paramaters - gather together into a HashMap - keyed on id
			Event event = new Event();
			while (rs.next())
			{	
				event = new Event();
				  
				String title = rs.getString(1);
				System.out.println("Medcafe Event JSON Events retrieveEvents retrieving values for title " + title);
			          
				Date date = rs.getDate(2);
				event.setTitle(title);
				event.setEventDate(date);
				event.setIcon(icon);
					
				if (type.equals(Event.FILE_TYPE))
				{
					String fileName = rs.getString("filename");
					event.setFileUrl(fileName);
				}
				//Set the event values
				eventList.add( event);
					
			}	 
		 return eventList;
		
	}
	
	public static HashMap<String,String> getIcons()
	{
	    	HashMap<String,String> icons = new HashMap<String,String>();
	    	String icon = "notes.png";
	    	icons.put(Event.NOTE_TYPE, icon);
	    	icon = "results.png";
	    	icons.put(Event.FILE_TYPE, icon);
	    	icon = "doctor-icon.png";
	    	icons.put(Event.APPT_TYPE, icon);
	    
	    	return icons;
	}
	
	public int getPatientId() {
		return patientId;
	}

	public void setPatientId(int patientId) {
		this.patientId = patientId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getEventDate() {
		return eventDate;
	}

	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
	
}
