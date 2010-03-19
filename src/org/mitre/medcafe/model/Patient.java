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
	
	public static final String SEARCH_PATIENTS_BY_FIRST_NAME = "SELECT id, first_name, last_name from patient where first_name like ? ";
	public static final String SEARCH_PATIENTS_BY_LAST_NAME = "SELECT id, first_name, last_name from patient where last_name like ? ";
	public static final String SEARCH_PATIENTS_BY_ALL = "SELECT id, first_name, last_name from patient where last_name like ? and first_name like ?";
	public static final String FIRST_NAME_TYPE = "first";
	public static final String LAST_NAME_TYPE = "last";
	
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
	
	 public JSONObject searchJson(String searchStringFirst, String searchStringLast){
	        
		 	boolean rtnResults = false;
		 	JSONObject ret = new JSONObject();
	        
	        try
	        {
	        	
	        	ResultSet rs = getPatients( searchStringFirst, searchStringLast);
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
			            String fName = rs.getString("first_name");
			            String lName = rs.getString("last_name");
			            o.put("id", id);
			            o.put("first_name", fName);
			            o.put("last_name", lName);
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
	
	 private ResultSet getPatients(String searchStringFirst, String searchStringLast) throws SQLException
	 {
		 DbConnection dbConn = null;
			
		 dbConn= new DbConnection();

		 System.out.println("Patient: getPatients : got connection " );
		 
		 PreparedStatement prep= null;

		 if (searchStringFirst.length() == 0)
		 {   
			 prep = dbConn.prepareStatement(Patient.SEARCH_PATIENTS_BY_LAST_NAME);
			 prep.setString(1, "%"+searchStringLast+"%");
		 }
		 else if (searchStringLast.length() == 0 )
		 {
			 prep = dbConn.prepareStatement(Patient.SEARCH_PATIENTS_BY_FIRST_NAME);
			 prep.setString(1, "%"+searchStringFirst+"%");
		 }
		 else
		 {
			 prep = dbConn.prepareStatement(Patient.SEARCH_PATIENTS_BY_ALL);
			 prep.setString(1, "%"+searchStringLast+"%");
			 prep.setString(2, "%"+searchStringFirst+"%");
		 }
		 
		 
		 System.out.println("Patient: getPatients : query " + prep.toString());
	     ResultSet rs = prep.executeQuery();
			
	     return rs;
			
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
