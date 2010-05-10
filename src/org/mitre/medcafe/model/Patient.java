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

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import org.mitre.medcafe.util.DbConnection;
import org.mitre.medcafe.util.WebUtils;
import org.restlet.ext.json.JsonRepresentation;

/**
 *  Representation of the text data
 *  @author: Gail Hamilton
 */
public class Patient
{
	 public final static String KEY = Patient.class.getName();
	    public final static Logger log = Logger.getLogger( KEY );
	    static{log.setLevel(Level.FINER);}

	private String firstName = "";
	private String lastName = "";
	private ArrayList<String> repositories =null;
	
	public static final String SEARCH_USER_PATIENTS_BY_ID = "SELECT patient.id, first_name, last_name, role from patient , patient_user_assoc " +
	" where patient_user_assoc.patient_id = patient.id and patient_user_assoc.patient_id = ? and patient_user_assoc.username = ? ";

	public static final String SEARCH_USER_PATIENTS_BY_FIRST_NAME = "SELECT patient.id, first_name, last_name, role from patient , patient_user_assoc " +
	" where patient_user_assoc.patient_id = patient.id and patient_user_assoc.username = ? and first_name like ? ";
	
	public static final String SEARCH_USER_PATIENTS_BY_LAST_NAME = "SELECT patient.id, first_name, last_name, role from patient , patient_user_assoc " +
	" where patient_user_assoc.patient_id = patient.id and patient_user_assoc.username = ? and last_name like ?";

	public static final String SEARCH_USER_PATIENTS_BY_ALL = "SELECT patient.id, first_name, last_name, role from patient , patient_user_assoc " +
	" where patient_user_assoc.patient_id = patient.id and patient_user_assoc.username = ? and first_name like ? and first_name like ? ";

	public static final String SEARCH_PATIENTS_BY_ID = "SELECT id, first_name, last_name from patient where id = ? ";
	public static final String SEARCH_PATIENTS_BY_FIRST_NAME = "SELECT id, first_name, last_name from patient where first_name like ? ";
	public static final String SEARCH_PATIENTS_BY_LAST_NAME = "SELECT id, first_name, last_name from patient where last_name like ? ";
	public static final String SEARCH_PATIENTS_BY_ALL = "SELECT id, first_name, last_name from patient where last_name like ? and first_name like ?";
	
	public static final String SEARCH_RECENT_PATIENTS = "SELECT patient.id, patient_id,first_name,last_name from patient, recent_patients where patient.id = recent_patients.patient_id and recent_patients.username = ?";
	public static final String SELECT_RECENT_PATIENTS = "SELECT patient_id from  recent_patients where username = ? and patient_id = ? ";
	public static final String INSERT_RECENT_PATIENTS = "INSERT INTO recent_patients  (username, patient_id) values ( ?, ?)";
	public static final String UPDATE_RECENT_PATIENTS = "UPDATE recent_patients SET date_accessed = ? where username = ? and patient_id = ?";
	public static final String SEARCH_BY_REPOSITORY = " and repository = ? ";
	
	public static final String INSERT_ASSOCIATION = "INSERT INTO patient_user_assoc (patient_id, username, role) values (?,?,?) ";
	public static final String FIRST_NAME_TYPE = "first";
	public static final String LAST_NAME_TYPE = "last";
	
	public static final String FIRST_NAME= "first_name";
	public static final String LAST_NAME = "last_name";
	public static final String ID = "id";
	public static final String NO_PATIENT = "No Patient";
	private static DbConnection dbConn = null;
	
	public Patient(String firstName, String lastName)	
	{
		this();	
		this.firstName = firstName;
		this.lastName = lastName;
		
		
	}
	
	public Patient()	
	{
		super();
		repositories = new ArrayList<String>();
	}
	
	public Patient(DbConnection conn)	
	{
		super();
		dbConn = conn;
	
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
	}
	public JSONObject associatePatient( String userName, String patientId, String role)
	{
		JSONObject ret = new JSONObject();
		//INSERT_ASSOCIATION = "INSERT INTO patient_user_assoc (patient_id, user_id, role) values (?,?,?) ";
		String insertQuery = INSERT_ASSOCIATION;
		int rtn = 0;
		int patient_id = Integer.parseInt(patientId);
		String err_mess = "Could not insert association for patient  " + patient_id;
		
		rtn = dbConn.psExecuteUpdate(insertQuery, err_mess , patient_id, userName, role);	
		
		if (rtn < 0)
		{
			return WebUtils.buildErrorJson( "Problem on creating an association for patient."  + patient_id  );	
		}
		
		return ret;
		
	}
	
	public JSONObject isPatient( String userName, String patientId)
	{
		JSONObject ret = new JSONObject();
		String checkQuery = SEARCH_USER_PATIENTS_BY_ID;

		int patient_id = Integer.parseInt(patientId);
		String err_mess = "Could not check if there is an existing association for patient  " + patient_id;
		
		ResultSet rs = dbConn.psExecuteQuery(checkQuery, err_mess , patient_id, userName);	
		
		try {
			if (rs.next())
			{
				  String role = rs.getString("role");
				  int id = rs.getInt(1);
		          String fName = rs.getString(Patient.FIRST_NAME);
		          String lName = rs.getString(Patient.LAST_NAME);
		          ret.put("id", id);
		          ret.put(Patient.FIRST_NAME, fName);
		          ret.put(Patient.LAST_NAME, lName);
		          ret.put("role", role);
			}
			else
			{
				 ret.put(ID, NO_PATIENT);
			}
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			return WebUtils.buildErrorJson( "Problem on checking if patient currently is in list. "  + patient_id  + " Error " + e.getMessage() );		
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			return WebUtils.buildErrorJson( "Problem on JSON creation on checking if patient currently is in list. "  + patient_id  + " Error " + e.getMessage() );		
		}
		
		
		return ret;
		
	}
	 public JSONObject searchJson(String isPatient, String searchStringFirst, String searchStringLast, String userName, String server){
	        
		 	boolean rtnResults = false;
		 	JSONObject ret = new JSONObject();
	        
	        try
	        {
	        	
	        	ResultSet rs = getPatients( isPatient, searchStringFirst, searchStringLast, userName, server);
		        if( rs == null )
		        {
		            return WebUtils.buildErrorJson( "Could not establish a connection to the database  at this time.");
		        }
		        
		        while( rs.next())
		        {
		        
			        //convert to JSON		        
			        	rtnResults = true;
			            
			            JSONObject o = new JSONObject();
			           
			            int id = rs.getInt(1);
			            String fName = rs.getString(Patient.FIRST_NAME);
			            String lName = rs.getString(Patient.LAST_NAME);
			            o.put("id", id);
			            o.put(Patient.FIRST_NAME, fName);
			            o.put(Patient.LAST_NAME, lName);
			            ret.append("patients", o);	
		        }    
		    }
	        catch(org.json.JSONException e)
	        {
	            log.throwing(KEY, "toJson()", e);
	            try {
					return new JSONObject("{\"error\": \""+e.getMessage()+"\"}");
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					return WebUtils.buildErrorJson( "Problem on generating JSON error." + e.getMessage());
			      	   
				}
	        } 
	        catch (SQLException e) {
				// TODO Auto-generated catch block
	        	 return WebUtils.buildErrorJson( "Problem on selecting data from database ." + e.getMessage());
	      	   
			}
	        
	        if (!rtnResults)
	        {
	        	return WebUtils.buildErrorJson( "There are no patients currently listed for First Name " + searchStringFirst + " and Last Name " + searchStringLast );
	      	  
	        }
	        return ret ;
	    }
	
	 private ResultSet getPatients(String isPatient, String searchStringFirst, String searchStringLast, String userName, String server) throws SQLException
	 {
		 
		 setConnection();
		 
		 System.out.println("Patient: getPatients : got connection " );
		 
		 PreparedStatement prep= null;

		 boolean hasServer = false;
		 if (!server.equals("noServer"))
		 {
			 hasServer = true;
		 }
		 
		 if (isPatient.equals("true"))
		 {
			 if (searchStringFirst.length() == 0)
			 {   
				 String sql = Patient.SEARCH_USER_PATIENTS_BY_LAST_NAME;
				 if (hasServer)
				 {
					 sql = sql + Patient.SEARCH_BY_REPOSITORY;
				 }
				 System.out.println("Patient: getPatients : SQL " + sql);
				 prep = dbConn.prepareStatement(sql);
				 
				 prep.setString(1, userName);
				 prep.setString(2, "%"+searchStringLast+"%");
				 if (hasServer)
				 {
					 prep.setString(3,server);
				 }
			 }
			 else if (searchStringLast.length() == 0 )
			 {
				 String sql = Patient.SEARCH_USER_PATIENTS_BY_FIRST_NAME;
				 if (hasServer)
				 {
					 sql = sql + Patient.SEARCH_BY_REPOSITORY;
				 }
				 System.out.println("Patient: getPatients : SQL " + sql);
				 prep = dbConn.prepareStatement(sql);
					
				 prep.setString(1, userName);
				 prep.setString(2, "%"+searchStringFirst+"%");
				 if (hasServer)
				 {
					 prep.setString(3,server);
				 }
			 }
			 else
			 {
				 String sql = Patient.SEARCH_USER_PATIENTS_BY_ALL;
				 if (hasServer)
				 {
					 sql = sql + Patient.SEARCH_BY_REPOSITORY;
				 }
				 prep = dbConn.prepareStatement(sql);
				 System.out.println("Patient: getPatients : SQL " + sql);
				 prep.setString(1, userName);
				 prep.setString(2, "%"+searchStringLast+"%");
				 prep.setString(3, "%"+searchStringFirst+"%");
				 if (hasServer)
				 {
					 prep.setString(4,server);
				 }
			 }
		 }
		 else
		 {
			 if (searchStringFirst.length() == 0)
			 {   
				 //prep = dbConn.prepareStatement(Patient.SEARCH_PATIENTS_BY_LAST_NAME);
				 String sql = Patient.SEARCH_PATIENTS_BY_LAST_NAME;
				 if (hasServer)
				 {
					 sql = sql + Patient.SEARCH_BY_REPOSITORY;
				 }
				 System.out.println("Patient: getPatients : SQL " + sql);
				 prep = dbConn.prepareStatement(sql);
				 
				 prep.setString(1, "%"+searchStringLast+"%");
				 if (hasServer)
				 {
					 prep.setString(2,server);
				 }
			 }
			 else if (searchStringLast.length() == 0 )
			 {
				 //prep = dbConn.prepareStatement(Patient.SEARCH_PATIENTS_BY_FIRST_NAME);
				 String sql = Patient.SEARCH_PATIENTS_BY_FIRST_NAME;
				 if (hasServer)
				 {
					 sql = sql + Patient.SEARCH_BY_REPOSITORY;
				 }
				 System.out.println("Patient: getPatients : SQL " + sql);
				 prep = dbConn.prepareStatement(sql);
				 
				 prep.setString(1, "%"+searchStringFirst+"%");
				 if (hasServer)
				 {
					 prep.setString(2,server);
				 }
			 }
			 else
			 {
				 //prep = dbConn.prepareStatement(Patient.SEARCH_PATIENTS_BY_ALL);
				 String sql = Patient.SEARCH_PATIENTS_BY_ALL;
				 if (hasServer)
				 {
					 sql = sql + Patient.SEARCH_BY_REPOSITORY;
				 }
				 System.out.println("Patient: getPatients : SQL " + sql);
				 prep = dbConn.prepareStatement(sql);
				 
				 prep.setString(1, "%"+searchStringLast+"%");
				 prep.setString(2, "%"+searchStringFirst+"%");
				 if (hasServer)
				 {
					 prep.setString(3,server);
				 }
			 }
		 
		 }
		 System.out.println("Patient: getPatients : query " + prep.toString());
	     ResultSet rs = prep.executeQuery();
			
	     return rs;
			
	 }

	 
	 public static JSONObject getPatient(int id,  DbConnection dbConn) 
	 {
			
		
		 System.out.println("Patient: getPatients : got connection " );
		 boolean rtnResults = false;
		 JSONObject ret = new JSONObject();
		 	
		 PreparedStatement prep;
		 try 
		 {
			 if (dbConn == null)
				 dbConn= new DbConnection();

			 prep = dbConn.prepareStatement(Patient.SEARCH_PATIENTS_BY_ID);
			
			 prep.setInt(1,id);
			 ResultSet rs = prep.executeQuery();
			 while( rs.next())
		     {
			        //convert to JSON		        
			        rtnResults = true;
			            
			           
			        String fName = rs.getString("first_name");
			        String lName = rs.getString("last_name");
			        ret.put("id", id);
			        ret.put("first_name", fName);
			        ret.put("last_name", lName);
		     }    
			 
			 if (!rtnResults)
		      {
		        	return WebUtils.buildErrorJson( "There are no patients currently listed for patient id " + id );
		      	  
		      }
		 } 
		 catch (SQLException e) 
		 {
				// TODO Auto-generated catch block
			 return WebUtils.buildErrorJson( "Problem on selecting data from database ." + e.getMessage());
	      	     
		 } 
		 catch (JSONException e) 
		 {
			// TODO Auto-generated catch block
			return WebUtils.buildErrorJson( "Problem on generating JSON error." + e.getMessage());
			     
		 }
	     return ret;
			
	 }

	 public static JSONObject getRecentPatients(String userName)
	 {
		 System.out.println("Patient: getRecentPatients : got connection " );
		 boolean rtnResults = false;
		 JSONObject ret = new JSONObject();
		 	
		 PreparedStatement prep;
		 try 
		 {
			 if (dbConn == null)
				 dbConn= new DbConnection();

			 prep = dbConn.prepareStatement(Patient.SEARCH_RECENT_PATIENTS);
			
			 prep.setString(1,userName);
			 ResultSet rs = prep.executeQuery();
			 while( rs.next())
		     {
			        //convert to JSON		        
			        rtnResults = true;
			            
			        JSONObject o = new JSONObject();
			        String fName = rs.getString("first_name");
			        String lName = rs.getString("last_name");
			        int patient_id = rs.getInt("patient_id");
			        o.put("id", patient_id);
			        o.put("first_name", fName);
			        o.put("last_name", lName);
			        ret.append("patients", o);	
		     }    
			 
			 if (!rtnResults)
		      {
		        	return WebUtils.buildErrorJson( "There are no recent patients currently listed for user " + userName );
		      	  
		      }
		 } 
		 catch (SQLException e) 
		 {
				// TODO Auto-generated catch block
			 return WebUtils.buildErrorJson( "Problem on selecting data from database ." + e.getMessage());
	      	     
		 } 
		 catch (JSONException e) 
		 {
			// TODO Auto-generated catch block
			return WebUtils.buildErrorJson( "Problem on generating JSON error." + e.getMessage());
			     
		 }
	     return ret;
		 
	 }
	 
	 public static JSONObject addRecentPatients(String userName, String patientId )
	 {
		 System.out.println("Patient: addRecentPatients : got connection " );
		 
		 JSONObject ret = new JSONObject();
		 	
		 int patient_id = Integer.parseInt(patientId);
		 PreparedStatement prep;
		 String updateSql = Patient.INSERT_RECENT_PATIENTS;
		 try 
		 {
			 if (dbConn == null)
				 dbConn= new DbConnection();

			 prep = dbConn.prepareStatement(Patient.SELECT_RECENT_PATIENTS);
			
			 prep.setString(1,userName);
			 prep.setInt(2,patient_id);
			 boolean hasValue = false;
			 ResultSet rs = prep.executeQuery();
			 if( rs.next())
		     {
				 updateSql = Patient.UPDATE_RECENT_PATIENTS;
				 hasValue = true;
		     }    
			 
			 prep = dbConn.prepareStatement(updateSql);
			 //If no current value then insert
			 if (!hasValue)
			 {
				 prep.setString(1,userName);
				 prep.setInt(2,patient_id);
			 }
			 else
			 {
				 java.util.Date today_date = new java.util.Date();
				 prep.setDate(1, new Date (today_date.getTime()));
				 prep.setString(2,userName);
				 prep.setInt(3,patient_id);
			 }
			 prep.executeUpdate();
			 
		 } 
		 catch (SQLException e) 
		 {
				// TODO Auto-generated catch block
			 return WebUtils.buildErrorJson( "Problem on selecting data from database ." + e.getMessage());
	      	     
		 } 
		
	     return ret;
		 
	 }
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public ArrayList<String> getRepositories() {
		return repositories;
	}

	public void setRepositories(ArrayList<String> repositories) {
		this.repositories = repositories;
	}
}
