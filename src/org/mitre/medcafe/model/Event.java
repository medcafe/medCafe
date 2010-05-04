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
	private Date eventDate =new Date();
	
	public static final String PATIENT_ID = "patient_id";
	public static final String TITLE = "title";
	public static final String ICON = "icon";
	public static final String DATE = "file_date";
	public static final String FILENAME = "filename";
	
	public static final String SORT_BY = " ORDER BY file_date DESC ";
	
	public static final String DATE_FORMAT = "yyyy,MM,dd";//2008,6,08
	public static final String SQL_DATE_FORMAT = "YYYY-MM-DD";
	
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
	
	
	public static ArrayList<Event> retrieveEvents(String userName, String patientId, String startDateStr, String endDateStr) throws SQLException, ParseException
	{
		ArrayList<Event> eventList = new ArrayList<Event>();
		
		try 
		 {
			int patId = Integer.valueOf(patientId);
			if (dbConn == null)
				dbConn= new DbConnection();
	
			PreparedStatement prep=null;
			DateFormat df = new SimpleDateFormat(DATE_FORMAT);
			Date startDate = null;
			Date endDate = null;
			ArrayList<String> sqlQueries =  getQueries();
			int startDatePos = 2, endDatePos = 2;
			
			ArrayList<String> icons = getIcons();
			ArrayList<String> types = getTypes();
			int i =0;
			for (String sqlQuery: sqlQueries)
			{
				/*if (startDateStr != null)
				{
						 sqlQuery = sqlQuery + " AND file_date > ? ";			 
						 startDate = df.parse(startDateStr);
						 startDatePos = 3; 
				}
					 
				if (endDateStr != null)
				{
						 endDate = df.parse(endDateStr);
						 sqlQuery = sqlQuery + " AND file_date < ? ";
						 endDatePos = startDatePos + 1;
				}
					 */
				
				System.out.println("Medcafe Event JSON Events retrieveEvents sql " + sqlQuery);
	            
				prep= dbConn.prepareStatement(sqlQuery);
						
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
					event.setIcon(icons.get(i));
					
					if (types.get(i).equals("file"))
					{
						String fileName = rs.getString("filename");
						event.setFileUrl(fileName);
					}
					//Set the event values
					eventList.add( event);
					
				}
				i++;
			}
		 }
		 catch (SQLException e) 
		 {
			dbConn.close();
			 dbConn = null;
			throw e;
		 } 
		 finally
		 {
			 
			 dbConn.close();
			 dbConn = null;
		 }
		 return eventList;
		
	}

	public static ArrayList<String> getQueries()
	{
	    	ArrayList<String> selects = new ArrayList<String>();
	    	String select = "SELECT subject as title,  note_added from user_text where username = ? and patient_id = ? ";
	    	selects.add(select);
	    	select = "SELECT title, file_date, filename, thumbnail  from file where username = ? and patient_id = ?  ";
	    	selects.add(select);
	    	select = "select  title , appoint_date, appoint_time from schedule  where username = ? and patient_id = ?  ";
	    	selects.add(select);
	    	
	    	return selects;
	}
	
	public static ArrayList<String> getIcons()
	{
	    	ArrayList<String> icons = new ArrayList<String>();
	    	String icon = "notes.png";
	    	icons.add(icon);
	    	icon = "results.png";
	    	icons.add(icon);
	    	icon = "doctor-icon.png";
	    	icons.add(icon);
	    
	    	return icons;
	}
	
	public static ArrayList<String> getTypes()
	{
	    	ArrayList<String> icons = new ArrayList<String>();
	    	String icon = "notes";
	    	icons.add(icon);
	    	icon = "file";
	    	icons.add(icon);
	    	icon = "schedule";
	    	icons.add(icon);
	    
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
	
}
