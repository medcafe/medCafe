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
 *  Representation of the Problem List Item
 *  @author: Gail Hamilton
 */
public class ProblemItem
{
	public final static String KEY = ProblemItem.class.getName();
	public final static Logger log = Logger.getLogger( KEY );
	//static{log.setLevel(Level.FINER);}

	//Parameters that are common to all Widgets
	private int patientId =0;
	private String title = "";
	private String note = "";
	private String priority = "";
	
	public static final String ID = "patient_id";
	
	public static final String SELECT_PROBLEM_LIST= "SELECT patient_id, problem, notes, date_entered, start_date, priority.priority, color from problem, priority where priority.id = problem.priority and patient_id = ? and username = ? ";
	public static final String SELECT_PROBLEM_EXT = " and start_date > ? and start_date < ? ";
	
	public static final String INSERT_PROBLEM = "INSERT INTO ";
	public static final String DELETE_PROBLEM= "DELETE FROM ";
	public static final String SELECT_ORDER_BY = " order by priority.priority ASC, date_entered DESC";	
	
	//private static DbConnection dbConn = null;
	public ProblemItem()	
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
		if (dbConn != null)
			dbConn.close();
	}
	
	public static JSONObject getProblemList(String patient_id, String username)
	{
		JSONObject o = new JSONObject();
		 
		return o;
	}
	
	public JSONObject toJSON() throws JSONException
	{
		 JSONObject o = new JSONObject();
		 o.put(ProblemItem.ID, this.getPatientId());
		 //To do fill in the rest 
				 
		 return o;
		 
	}
	
	public static JSONObject getProblemList(String patientId, String username,  Date startDate, Date endDate)
	 {
		 JSONObject ret = new JSONObject();
		 PreparedStatement prep = null;
		 DbConnection dbConn = null;
		 ResultSet rs = null;
		 int patient_id = Integer.parseInt(patientId);
		
		 try 
		 {
		 	 dbConn = setConnection();
			 String sql = ProblemItem.SELECT_PROBLEM_LIST;
			 	
			 if (startDate != null)
			 {
				sql = sql +  ProblemItem.SELECT_PROBLEM_EXT + SELECT_ORDER_BY;
				prep = dbConn.prepareStatement(sql);
				
				prep.setInt(1, patient_id);
				prep.setString(2, username);
				 
				prep.setDate(3, new java.sql.Date (startDate.getTime()));
				
				if (endDate == null)
				{
					endDate = new java.util.Date();
					prep.setDate(3, new java.sql.Date (endDate.getTime()));
					
				}
			 }
			 else if (endDate != null)
			 {
				 sql = sql +  ProblemItem.SELECT_PROBLEM_EXT + ProblemItem.SELECT_ORDER_BY;
					
				 prep = dbConn.prepareStatement(sql);
				 prep.setInt(1, patient_id);
				 prep.setString(2, username);
					 
				 Calendar cal = new GregorianCalendar();
				 cal.set(1920, 1, 1);
				 prep.setDate(3, new java.sql.Date (cal.getTime().getTime()));
				 prep.setDate(4, new java.sql.Date (endDate.getTime()));
					
			 }
			 else
			 {
				 sql = sql + SELECT_ORDER_BY;
				 prep = dbConn.prepareStatement(sql);				
				 prep.setInt(1, patient_id);
				 prep.setString(2, username);
							
			 }
			 log.finer("ProblemItem: getProblemList : query " + prep.toString());
			    
			 rs = prep.executeQuery();
			 boolean rtnResults = false;
			 
			 while (rs.next())
			 {
				 //patient_id, problem, problem_notes, date_entered, start_date, priority.priority, color 
				 rtnResults = true;
		            
				  JSONObject o = new JSONObject();
				    
			      String problem = rs.getString("problem");
			      String problem_notes = rs.getString("notes");
			      String priority = rs.getString("priority");
			      String color = rs.getString("color");
			     
			      Date date_entered = rs.getDate("date_entered");
			      Date start_date = rs.getDate("start_date");
			      
			      o.put("patient_id", patient_id);
			      o.put("title", problem);
			      o.put("priority", priority);
			      
			      if (color != null)
			    	  o.put("color", color);
			      
			      if (problem_notes != null)
			    	  o.put("note", problem_notes);
			      
			      if (start_date != null)					    
			    	  o.put("start_date", start_date);
				     
			      //o_new.put("history", o);
			      ret.append("patientProblem", o);	
		     }    
			 
			 if (!rtnResults)
		      {
		      	        DatabaseUtility.close(rs);
		      	        DatabaseUtility.close(prep);
		      	        dbConn.close();
		        	return WebUtils.buildErrorJson( "There is no patient history listed for patient " + patient_id );
		      	  
		      }
		 }
		 catch (SQLException e) 
		 {
				// TODO Auto-generated catch block
			 return WebUtils.buildErrorJson( "Problem on selecting data from database ." + e.getMessage());
	      	     
		 } catch (JSONException e) {
			// TODO Auto-generated catch block
			 return WebUtils.buildErrorJson( "Problem on building JSON Object ." + e.getMessage());		      	
		} 
		finally
		{
			DatabaseUtility.close(rs);
			DatabaseUtility.close(prep);
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
	
	
}
