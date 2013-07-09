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
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


import javax.servlet.ServletContext;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.hl7.greencda.c32.Codes;
import org.hl7.greencda.c32.Condition;
import org.hl7.greencda.c32.Encounter;
import org.hl7.greencda.c32.Immunization;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mitre.medcafe.restlet.MedcafeApplication;
import org.mitre.medcafe.restlet.PatientListResource;
import org.mitre.medcafe.restlet.Repositories;
import org.mitre.medcafe.util.Constants;
import org.mitre.medcafe.util.DbConnection;
import org.mitre.medcafe.util.DatabaseUtility;
import org.mitre.medcafe.util.Text;
import org.mitre.medcafe.util.WebUtils;
import org.restlet.Application;
import org.restlet.Client;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.ClientInfo;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Preference;
import org.restlet.data.Protocol;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 *  Representation of the text data
 *  @author: Gail Hamilton
 */
public class Event
{
	public final static String KEY = Event.class.getName();
	public final static Logger log = Logger.getLogger( KEY );
	//static{log.setLevel(Level.FINER);}

	//Parameters that are common to all MedCafe Files
	private int patientId =0;
	private int id =0;
	private String icon ="";
	private String title ="";
	private String fileUrl ="";
	private String type ="";
	private Date eventDate =new Date();
	private String link ="";
	private String repository =Constants.DEFAULT_REPOSITORY;
	private String repPatientId = "0";
	private String description = "";

	public static final String PATIENT_ID = "patient_id";
	public static final String TITLE = "title";
	public static final String ICON = "icon";
	public static final String DATE = "file_date";
	public static final String FILENAME = "filename";
	public static final String TYPE = "type";

	public static final String SORT_BY = " ORDER BY file_date DESC ";

	public static final String DATE_FORMAT = "yyyy,MM,dd";//2008,6,08
	public static final String SQL_DATE_FORMAT = "YYYY-MM-DD";

	public static final String PRIMARY_REPOSITORY = "PrimaryOnly";

	public static final String NOTE_TYPE = "Records";
	public static final String APPT_TYPE = "Visits";
	public static final String FILE_TYPE = "Images";
	public static final String SYMPTOMS_TYPE = "Symptoms";
	public static final String PROBLEMS_TYPE = "Problems";
	public static final String HOSPITAL_TYPE = "Hospital";
	public static final String IMMUNIZATION_TYPE = "Immunizations";
	public static final String ENCOUNTER_TYPE = "Encounters";
	public static final String LAB_TYPE = "LabResults";
	public final static String NA = "Resource not available";

	
	public static final String ENCOUNTER_TYPE_STRING = "reasonForVisit";
	public static final String FREE_TEXT_VALUE = "freeText";
	public static final String EVENT_DATE = "time";
	public static final String CODE_STRING ="codes";
	public static final String ICD9_CODE ="ICD-9-CM";
	public static final String SNOMED_CODE="SNOMED-CT";
	public static final String CPT = "CPT";
	public static final String RXNORM ="RxNorm"; 
	public static final String LOINC = "LOINC";
	public static final String PROBLEM_NAME = "problemName";
	public static final String DESCRIPTION = "description";
	public Event()
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

		public static void closeConnections(DbConnection dbConn, PreparedStatement prep, ResultSet rs) 
	{
		DatabaseUtility.close(prep);
		DatabaseUtility.close(rs);
		
		if (dbConn != null)
			dbConn.close();
		
	}
		public static void closeConnections(DbConnection dbConn) 
	{
		
		closeConnections(dbConn, null, null);
		
	}
	public static void closeConnections(DbConnection dbConn, PreparedStatement prep)
	{
		closeConnections(dbConn, prep, null);
	}
	public static void closeConnections(DbConnection dbConn, ResultSet rs)
	{
		closeConnections(dbConn, null, rs);
	}

	public JSONObject toJSON() throws JSONException
	{
		 JSONObject o = new JSONObject();
		 o.put(Event.PATIENT_ID, this.getPatientId());
		 return o;

	}

	public static String[] getEventList()
	{
		return new String[]{Event.SYMPTOMS_TYPE, Event.LAB_TYPE, Event.PROBLEMS_TYPE,Event.APPT_TYPE,Event.HOSPITAL_TYPE,Event.FILE_TYPE,Event.NOTE_TYPE, Event.IMMUNIZATION_TYPE, Event.ENCOUNTER_TYPE};
	}
	public static String[] getPatientCacheObjectList()
	{
		return new String[] {null, "resultList", "problemList", null, null, null, null, "immuneList", "encounterList"};
	}

	public static ArrayList<Event> retrieveEvents(String userName, String patientId, String startDateStr, String endDateStr, String[] eventTypes, Application application, JSONObject repositories) throws SQLException, ParseException
	{
		ArrayList<Event> eventList = new ArrayList<Event>();
		System.out.println("Event : retrieveEvents: getIcons start " );

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
					String url = "/repositories/<:repository:>/patients/<:patientId:>/problems";
					ArrayList<Event> newEventList = retrieveEventsFromRepositories(url, userName, patientId, startDateStr, endDateStr, eventTypes, icon, type, application, repositories);
					eventList.addAll(newEventList);
				}
				else if (type.equals(Event.SYMPTOMS_TYPE))
				{

				}
				else if (type.equals(Event.LAB_TYPE))
				{
					String url = "/repositories/<:repository:>/patients/<:patientId:>/results";
					ArrayList<Event> newEventList = retrieveEventsFromRepositories(url, userName, patientId, startDateStr, endDateStr, eventTypes, icon, type, application, repositories);
					eventList.addAll(newEventList);
				
				}
				else if (type.equals(Event.IMMUNIZATION_TYPE))
				{
					String url =  "/repositories/<:repository:>/patients/<:patientId:>/immunizations";

					ArrayList<Event> newEventList = retrieveEventsFromRepositories(url, userName,  patientId,  startDateStr,  endDateStr, eventTypes,  icon,  type, application, repositories) ;
					eventList.addAll(newEventList);
				}
				else if (type.equals(Event.ENCOUNTER_TYPE))
				{
					String url = "/repositories/<:repository:>/patients/<:patientId:>/encounters";
					ArrayList<Event> newEventList = retrieveEventsFromRepositories(url, userName, patientId, startDateStr, endDateStr, eventTypes, icon, type, application, repositories);
					eventList.addAll(newEventList);
				}
			}
			log.finer("Event : retrieveEvents: finished ");


		System.out.println("# of events returned: " + eventList.size());

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

	private static ArrayList<Event> retrieveEventsFromLocal(String sql,String userName, String patientId, String startDateStr, String endDateStr, String[] eventTypes, String icon, String type) throws SQLException
	{
		ArrayList<Event> eventList = new ArrayList<Event>();


			int patId = Integer.valueOf(patientId);
			DbConnection dbConn = null;
			PreparedStatement prep = null;
			ResultSet rs = null;

		try {
			dbConn = setConnection();

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
			rs =  prep.executeQuery();

			log.finer("Event JSON Events retrieveEvents sql" + prep.toString());

			//This lists all the paramaters - gather together into a HashMap - keyed on id
			Event event = new Event();
			while (rs.next())
			{
				event = new Event();

				String title = rs.getString(1);
				log.finer("Event JSON Events retrieveEvents retrieving values for title " + title);

				Date date = rs.getDate(2);
				event.setTitle(title);
				event.setEventDate(date);
				event.setIcon(icon);

				event.setType(type);

				if (type.equals(Event.FILE_TYPE))
				{
					String fileName = rs.getString("filename");
					event.setFileUrl(fileName);
				}

				event.setRepPatientId(patientId);
				event.setRepository(Constants.LOCAL_REPOSITORY);

				//Set the event values
				eventList.add( event);

			}
		}
		catch (SQLException e)
		{
			throw e;
		}

		finally {
			
		 closeConnections(dbConn, prep, rs);
		
		}
		 return eventList;

	}

	private static ArrayList<Event> retrieveEventsFromRepositories(String urlStr,String userName, String patientId, String startDateStr, String endDateStr, String[] eventTypes, String icon, String type, Application app, JSONObject repositories) throws SQLException, ParseException
	{
		ArrayList<Event> eventList = new ArrayList<Event>();
		try
		{
			System.out.println("Event retrieveEventsFromRepositories type start");
    		
			JSONArray reps = repositories.getJSONArray("repositories");
			MedcafeApplication medApp = (MedcafeApplication)app;
			if (repositories.has("repositories"))
			{
				for (int i=0; i < reps.length(); i++ )
		    	{
					String url = urlStr;
		    		JSONObject repObj;
					repObj = (JSONObject) reps.get(i);

		    		String repository = repObj.getString("repository");
		    		String repPatientId = repObj.getString("id");

		    		url = url.replaceAll("<:repository:>", repository);
		    		url = url.replaceAll("<:patientId:>", repPatientId);
		    		String results = getJsonContent( medApp, url );
		    		JSONObject jsonResults = new JSONObject(results);
		    		System.out.println("Event retrieveEventsFromRepositories type " +  type + " jsonObject " + jsonResults.toString());
		    		ArrayList<Event> eventsFromRestlet = getEventObject(jsonResults, userName, patientId, repPatientId, repository, type, icon);
		    		eventList.addAll(eventsFromRestlet);
		    	}
			}
		}
		catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();

		}

		return eventList;

	}


	public static ArrayList<Event> getEventObject(JSONObject jsonResults, String userName, String patientId, String repPatientId, String repository, String type, String icon) throws JSONException
	{
		ArrayList<Event> eventList = new ArrayList<Event>();
		if (jsonResults.has("announce"))
			return eventList;
		DatatypeFactory factory;
		if (type.equals(Event.IMMUNIZATION_TYPE))
		{
			/*{"repository":"OurVista","immunizations":
			 * [{"narrative":"Series: COMPLETE Reaction: NONE Contraindicated: NO (OK TO USE IN THE FUTURE)",
			 * 	"administeredDate":{"minute":0,"fractionalSecond":0,"timezone":-240,"second":0,"month":6,"year":2010,"day":30,"hour":0},
			 *  "performer":{"person":{"name":{"given":["BECKY"],"lastname":"BOUWENS"}}},
			 *  "medicationInformation":{"manufacturedMaterial":{"freeTextBrandName":"TETANUS DIPTHERIA (TD-ADULT)"}},"refusal":false},
			 *
			 * */
			
			
			if (!jsonResults.has("immunizations"))
			{
				return eventList;
			}
            try {
            	factory = DatatypeFactory.newInstance();
            
			 JSONArray jsonImms = jsonResults.getJSONArray("immunizations");

			 for (int i=0; i < jsonImms.length(); i++ )
		     {
				 Event event = new Event();
				 
				 event.setId(Integer.parseInt(patientId));
				 event.setRepPatientId(repPatientId);
				 event.setRepository(repository);
				 JSONObject immObj;
				 immObj = (JSONObject) jsonImms.get(i);

				 /*  "medicationInformation":{"manufacturedMaterial":{"freeTextBrandName":"TETANUS DIPTHERIA (TD-ADULT)"}},"refusal":false}*/

			/*	 JSONObject infoObj = immObj.getJSONObject("medicationInformation");
				 infoObj = infoObj.getJSONObject("manufacturedMaterial");*/
			//	 String immTitle = immObj.getString("description");
				 String immTitle = immObj.getString("description");
				 String desc = "";
				
			/*	 try {
				/*String descTemp = immObj.getString("narrative");
				 int index = 0;
				 while (index != -1)
				 {
				 	int newIndex = descTemp.indexOf("^", index);
				 	if (newIndex != -1)
				 	{
				 		desc = desc + descTemp.substring(index, newIndex) + "<br>";
				 		index = newIndex + 1;
				 	}
				 	else
				 	{
				 		desc = desc + descTemp.substring(index);
				 		index = newIndex;
				 	}
				 }*/
			/*	 }
				 catch (JSONException jsonE)
				 {
				 	log.finer("No narrative in immunization record of patient # " + repPatientId + " for " + immTitle);
				 }*/
			//	 try {
				 if (immObj.has("isRefused")){
				 boolean isRefused = Boolean.parseBoolean( immObj.getString("isRefused"));
					if (isRefused) {
				 	desc = desc+ "<br>Refused:  " + immObj.getJSONObject("refusalReason").getString("displayName");
					}
				 }
			
				 event.setDescription(desc);
				
				 event.setEventDate(getDateFromInterval(immObj));
		
				 event.setIcon(icon);
				 event.setTitle(immTitle);
				 event.setType(type);
				 eventList.add(event);
		     }
            }
            catch(Exception e)
            {
            	log.log(Level.SEVERE, "Error getting immunization events {0}",e.getMessage());
            }


		}
		else		if (type.equals(Event.PROBLEMS_TYPE))
		{
			/*{"repository":"OurVista","problem":[{"narrative":"A","problemCode":{"code":"250.00"},"problemDate":{"low":{"minute":0,"fractionalSecond":0,"timezone":-240,"second":0,"month":4,"year":2010,"hour":0,"day":1}},"problemName":"Diabetes"},{"narrative":"A","problemCode":{"code":"819.0"},"problemDate":{"low":{"minute":0,"fractionalSecond":0,"timezone":-240,"second":0,"month":9,"year":2008,"hour":0,"day":9}},"problemName":"Multiple fractures involving both upper limbs, and upper limb with rib(s) and st"}],"patient_id":"3"}

			 * */
			if (!jsonResults.has("problem"))
			{
				return eventList;
			}

			 JSONArray jsonProbs = jsonResults.getJSONArray("problem");

			 for (int i=0; i < jsonProbs.length(); i++ )
		     {
				 Event event = new Event();
				 event.setId(Integer.parseInt(patientId));
				 event.setRepPatientId(repPatientId);
				 event.setRepository(repository);
				 JSONObject probObj;
				 probObj = (JSONObject) jsonProbs.get(i);

				 String probTitle = probObj.getString("description");

		/*		 try {
					 if (probObj.has(FREE_TEXT_VALUE))
					 {
						 active = probObj.getString(FREE_TEXT_VALUE);
					 }
				 }
				 catch (JSONException jsonE)
				 {
				 	log.finer("No narrative for patient " + repPatientId + " for problem " + probTitle);
				 }*/
				 
				 	probTitle = probTitle + " - ACTIVE";
				 	String desc = getCodeDescriptions(probObj);
				 	
				 	
				
				 		
				 event.setDescription(desc);
				 event.setEventDate(getDateFromInterval(probObj));
	
				 event.setIcon(icon);
				 event.setTitle(probTitle);
				 event.setType(type);
				 eventList.add(event);
				 System.out.println(event.getEventDate());
		     }


		}
		else if (type.equals(Event.LAB_TYPE))
		{
			/*{"repository":"OurVista","problem":[{"narrative":"A","problemCode":{"code":"250.00"},"problemDate":{"low":{"minute":0,"fractionalSecond":0,"timezone":-240,"second":0,"month":4,"year":2010,"hour":0,"day":1}},"problemName":"Diabetes"},{"narrative":"A","problemCode":{"code":"819.0"},"problemDate":{"low":{"minute":0,"fractionalSecond":0,"timezone":-240,"second":0,"month":9,"year":2008,"hour":0,"day":9}},"problemName":"Multiple fractures involving both upper limbs, and upper limb with rib(s) and st"}],"patient_id":"3"}

			 * */
			if (!jsonResults.has("results"))
			{
				return eventList;
			}

			 JSONArray jsonLabs = jsonResults.getJSONArray("results");

			 for (int i=0; i < jsonLabs.length(); i++ )
		     {
				 Event event = new Event();
				 event.setId(Integer.parseInt(patientId));
				 event.setRepPatientId(repPatientId);
				 event.setRepository(repository);
				 JSONObject labObj;
				 labObj = (JSONObject) jsonLabs.get(i);
					
				 String labTitle = labObj.getString(DESCRIPTION);
				 event.setTitle(labTitle);
				
				 	try
				 	{
				 		String descText = "";
				 		if (labObj.has(FREE_TEXT_VALUE))
						 {
							descText = labObj.getString(FREE_TEXT_VALUE);
						 }
				 		
				 		String eventDesc = labTitle + "<br/>" + getCodeDescriptions(labObj);
				 		
				 		if (labObj.has("values"))
				 		{
				 			String scalar = "";
				 			String unit = "";
				 			JSONArray values = labObj.getJSONArray("values");
				 			if (values.length() > 0)
				 			{
				 				for (int j =0; j < values.length(); j++)
				 				{
					 				JSONObject valObj = values.getJSONObject(j);
					 				if (valObj.has("scalar"))
					 					scalar = valObj.getString("scalar");
					 				if (valObj.has("units"))
					 					unit = "(" + valObj.getString("units") + ")";
					 				eventDesc = eventDesc + "<br/><b>" + scalar + "</b>" + unit + "<br/>";				 				}
				 			}
				 		}
				 		event.setDescription(eventDesc);
				 		
				 	}
					catch (JSONException jsonE)
					{
						log.finer("No problem code for " + repPatientId + " and problem " + labTitle);
					}

				 String labDateStr = labObj.getString("time");
				 SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
				 Date labDate;
				try {
					labDate = dateFormat.parse(labDateStr);
					event.setEventDate(labDate);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					labDate = null;
				}
				
				 event.setIcon(icon);
				 event.setTitle(labTitle);
				 event.setType(type);
				 eventList.add(event);
		     }


		}
		else if (type.equals(Event.ENCOUNTER_TYPE))
		{
			log.finer(jsonResults.toString());

			if (!jsonResults.has("encounters"))
			{
				return eventList;
			}

			 JSONArray jsonEncs = jsonResults.getJSONArray("encounters");

			 for (int i=0; i < jsonEncs.length(); i++ )
		     {
				 Event event = new Event();
				 event.setId(Integer.parseInt(patientId));
				 event.setRepPatientId(repPatientId);
				 event.setRepository(repository);
				 JSONObject encObj;
				 encObj = (JSONObject) jsonEncs.get(i);
				 System.out.println("Event getEventObject line 702 json output " + encObj );
				
				
				 Gson gson = new Gson();

				
				 String encTitle;
				 String desc = "";
				 if (encObj.has("description") && !encObj.getString("description").equals(""))
				 {
					 encTitle = encObj.getString("description");
				 }
				 else
				 {
					 if (encObj.has("freeText") && !encObj.getString("freeText").equals(""))
					 {
						 encTitle = encObj.getString("freeText");
					 }
					 else
					 {
						 encTitle = "No entered description - see codes";
						
					 }
					 
				 }
				 desc = getCodeDescriptions(encObj);
						
			     String timeStr = (String)encObj.get(EVENT_DATE);
				 	
				 DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
				 
				 try
				 {
					Date encDate = df.parse(timeStr);
					event.setEventDate(encDate);
				 }
				 catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				 }
				
			
				 event.setDescription(desc);
				 event.setTitle(encTitle);
				 event.setIcon(icon);
				 event.setType(type);
				 eventList.add(event);
		     }


		}
		return eventList;
	}
	public static JSONObject getJsonEventsFromCache(PatientCache cache, String primary, String[] events, Date startDate, Date endDate, String server, String userName)
	{
		try{
			ArrayList<Event> eventList = getEventsFromCache(cache, primary, events, userName);
			JSONObject obj = new JSONObject();
			ArrayList<String> dates = new ArrayList<String>();
      	String dir = "patients/" + cache.getDatabasePatientId() + "/";
      	String imageDir = "images/" + dir;

      	int i=0;
      	DateFormat startFormat = new SimpleDateFormat(MedCafeFile.DATE_FORMAT);
      	
      	DateFormat eventDf = new SimpleDateFormat(Event.DATE_FORMAT);
			for(Event event: eventList)
      	{
      		Date date = event.getEventDate();
      		if (date.compareTo(startDate)>=0 && date.compareTo(endDate)<=0)
      	   {
      	  		JSONObject inner_obj = new JSONObject ();
      	   	inner_obj.put("start", date);
      	   	if (!event.getIcon().equals(""))
      	   		inner_obj.put("icon", "http://" + server + "/images/" + event.getIcon());
      	   	inner_obj.put("title", event.getTitle());
      	 		if (!event.getFileUrl().equals(""))
      	 			inner_obj.put("image", "http://" + server + "/" + imageDir +  event.getFileUrl());
      	 		if (!event.getLink().equals(""))
      	 			inner_obj.put("link", "http://" + server + "/images/" + event.getLink());
      	 		inner_obj.put("type", event.getType());
      	 		inner_obj.put("repository", event.getRepository());
      	 		inner_obj.put("rep_patient_id", event.getRepPatientId());
      	 		inner_obj.put("description", event.getDescription());
      	 		obj.append("events", inner_obj); 
					String dateStr = eventDf.format(date);
					dates.add(dateStr);
					i++;
				}
			}	
      //	String jsonStr = obj.toString();
      //	jsonStr = putInDates(jsonStr, dates);

      //	JsonRepresentation json = new JsonRepresentation(jsonStr);
      	return obj;
      }
      catch(org.json.JSONException e)
   	{
    		log.throwing(KEY, "toJson()", e);
    		try{
    		JSONObject errorObj = new JSONObject();
    		errorObj.put("error", e.getMessage());
    		return errorObj;
    		}
    		catch (JSONException jsonE)
    		{
    			log.log(Level.SEVERE, "Error creating event object!");
    			return null;
    		}
    		
    		 
		} 

	}
	public static ArrayList<Event> getEventsFromCache(PatientCache cache, String primary, String[] events, String userName)
	{
		ArrayList<Event> eventList = new ArrayList<Event>();
		String[] listOfEvents = getEventList();
		String[] objectNames = getPatientCacheObjectList();
		String primaryRepos = cache.getPrimaryRepos();
		try{

			for (String event : events)
			{
				for (int i = 0; i< listOfEvents.length; i++)
				{
					if (event.equals(listOfEvents[i]) && objectNames[i] != null)
					{
					
						JSONObject obj = cache.retrieveObjectList(objectNames[i]);
						JSONArray array = obj.getJSONArray("repositoryList");
						for (int j= 0; j<array.length(); j++)
						{
					
							JSONObject resObj = array.getJSONObject(j);
							String repository = resObj.getString("repository"); 
						
							if (!primary.equals("true") || repository.equals(primaryRepos))
							{
									ArrayList<Event> newList = getEventObject(resObj, userName, cache.getDatabasePatientId(), cache.getRepoPatientId(repository), repository, event, getIcons().get(event));
								eventList.addAll(newList);
						
							} 
						}
						break;		
					}
				}
			}
		}

		catch(JSONException jsonE)
		{
			jsonE.printStackTrace();
		}

			
		
		return eventList;
	}
	public static String getPersonName(JSONObject person)
	{
	  
	 	String personName = "";
	 	try {
	 		JSONObject nameObj = person.getJSONObject("name");
			JSONArray jsonNames = nameObj.getJSONArray("given");
			for (int j = 0; j< jsonNames.length(); j++)
			{
				personName = personName + (String) jsonNames.get(j) + " ";
			}
			personName = personName + nameObj.getString("lastname");
		}
		catch (JSONException jsonE)
		{
		}
		return personName;
	}
	

	public static HashMap<String,String> getIcons()
	{
	    	HashMap<String,String> icons = new HashMap<String,String>();
	    	String icon = "green-circle.png";
	    	icons.put(Event.NOTE_TYPE, icon);
	    	icons.put(Event.LAB_TYPE, icon);
	    	icon = "gray-circle.png";
	    	icons.put(Event.FILE_TYPE, icon);
	    	icon = "dull-blue-circle.png";
	    	icons.put(Event.APPT_TYPE, icon);
	    	icon = "dark-green-circle.png";
	    	//icon = "dull-blue-circle.png";
	    	icons.put(Event.IMMUNIZATION_TYPE, icon);
         icon = "dark-blue-circle.png";
         //      icon = "green-circle.png";
                icons.put(Event.ENCOUNTER_TYPE, icon);
         icon = "red-circle.png";
         icons.put(Event.PROBLEMS_TYPE, icon);

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

	public String getRepository() {
		return repository;
	}

	public void setRepository(String repository) {
		this.repository = repository;
	}

	public String getRepPatientId() {
		return repPatientId;
	}

	public void setRepPatientId(String repPatientId) {
		this.repPatientId = repPatientId;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getDescription()
	{
		return description;
	}

	 private static String getJsonContent( MedcafeApplication app, String endpoint )
	    {
	        log.finer( "Starting retrieval of " + endpoint );
	        Request req = new Request( Method.GET, endpoint );
	        Response resp = new Response( req );
	        ClientInfo clientInfo = req.getClientInfo();
	        List<Preference<MediaType>> mediaTypes = clientInfo.getAcceptedMediaTypes();
	        mediaTypes.add( new Preference<MediaType>( MediaType.APPLICATION_JSON, 1.0F) );
	        app.handle(req, resp);
	        StringWriter out = new StringWriter();
	        if (resp.getStatus().isSuccess() && resp.getEntity().isAvailable() ) {
	            try
	            { resp.getEntity().write(out); }
	            catch (IOException e)
	            {
	                log.throwing(KEY, "getJsonContent()", e);
	                return WebUtils.buildErrorJson( "Problem retrieving data from source." + e.getMessage()).toString();
	            }
	        }
	        else
	        {
	            out.write( NA );
	        }
	        log.finer( "Finished retrieval of " + endpoint );
	        return out.toString();
	    }
	  private static String putInDates(String jsonStr, ArrayList<String> dates)
    {
    	int i=0;
    	//new Date (" +  dates[i] + ")
    	for (String date: dates)
    	{
    		jsonStr = jsonStr.replaceAll("\"<:startDate" + i + ":>\"", "new Date (" +  date + ")");
    		i++;
    	}
    	return jsonStr;
    }

	  
	  private static String getCodeDescriptions(JSONObject jsonObj) throws JSONException
	  {
		  	StringBuffer strBuf = new StringBuffer();
		    if (!jsonObj.has("codes"))
		    {
		    	return "";
		    }
		  	JSONObject codes = jsonObj.getJSONObject("codes");
	 		if (codes.has(ICD9_CODE))
	 		{
	 			String jsonCode = codes.getString("ICD-9-CM");
	 			strBuf.append( "ICD9 Code: " + jsonCode.toString() + "<br/>");
	 		
	 		}
	 		if (codes.has(SNOMED_CODE))
	 		{
	 			String jsonCode = codes.getString(SNOMED_CODE);
	 			strBuf.append("Snomed Code: " + jsonCode.toString() + "<br/>");
	 		}
	 		
	 		if (codes.has(CPT))
	 		{
	 			String jsonCode = codes.getString("CPT");
	 			strBuf.append("CPT Code: " + jsonCode.toString() + "<br/>");
	 		}
	 		
	 		if (codes.has(RXNORM))
	 		{
	 			String jsonCode = codes.getString("RxNorm");
	 			strBuf.append("RxNorm: " + jsonCode.toString() + "<br/>");
	 		}
	 		
	 		if (codes.has(LOINC))
	 		{
	 			String jsonCode = codes.getString("LOINC");
	 			strBuf.append("LOINC : " + jsonCode.toString() + "<br/>");
	 		}
	 		return strBuf.toString();
	 	
	  }
	  private static Date getDateFromInterval(JSONObject obj) throws JSONException
	  {
		 
		  if (obj.getJSONObject("effectiveTime") == null)
		  {
			  return null;
		  } 
		  JSONObject intervalObject = obj.getJSONObject("effectiveTime");
		  JSONObject dateObj = null; 
		  if (intervalObject.has("value"))
		  {
			  dateObj = intervalObject.getJSONObject("value");
		  }
		  else{
			  
			  
			  if (intervalObject.has("start"))
			  {
				  dateObj = intervalObject.getJSONObject("start"); 
			  }
			  else
			  {
				  if (intervalObject.has("end"))
				  {
					  dateObj = intervalObject.getJSONObject("end");
				  }
				  else
				  {
					  return null;
				  }
			  }
		  }
		  String month = dateObj.getString("month");
				 int monthVal = Integer.parseInt(month) -1;
				 String day = dateObj.getString("day");
				 String year = dateObj.getString("year");
				 String dateStr = monthVal + "/" + day + "/" + year;
				 DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
				 try
				 {
					return df.parse(dateStr);
			
				 }
				 catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
					
				 }
	  }
}
