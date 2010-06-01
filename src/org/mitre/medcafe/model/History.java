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
import org.mitre.medcafe.util.WebUtils;

/**
 *  Representation of the Problem List Item
 *  @author: Gail Hamilton
 */
public class History
{
	public final static String KEY = History.class.getName();
	public final static Logger log = Logger.getLogger( KEY );
	static{log.setLevel(Level.FINER);}

	//Parameters that are common to all Widgets
	private int patientId =0;
	
	public static final String ID = "patient_id";
	public static final String SELECT_PATIENT_HISTORY = " SELECT patient_id, history, category, history_date, history_notes, priority.priority, color from medical_history, history_category, priority where medical_history.category_id = history_category.id and priority.id = medical_history.priority and  patient_id = ? and category = ? ";
	public static final String SELECT_PATIENT_HISTORY_EXT = " and history_date > ? and history_date < ? ";
	public static final String SELECT_PATIENT_HISTORY_ORDER_BY = " order by priority.priority ASC, history_date DESC";	
	
	public static final String DELETE_SYMPTOMS = "Delete from patient_symptom_list where patient_id = ?";
	
	private final static String SELECT_TEMPLATE_HISTORY = "select symptom_list.id, category, symptom from physical_category, symptom_list " + 
								" where physical_category.id = symptom_list.physical_category order by category";

	private final static String SELECT_PATIENT_SYMPTOM_HISTORY = "select symptom_id, category, symptom " +
								" from physical_category, symptom_list,patient_symptom_list " +
								" where physical_category.id = symptom_list.physical_category and patient_symptom_list.symptom_id = symptom_list.id and patient_id =?  order by category";

	private final static String SELECT_PATIENT_SYMPTOM_HISTORY_COUNT = "select count(*) " +
								" from physical_category, symptom_list,patient_symptom_list " +
								" where physical_category.id = symptom_list.physical_category and patient_symptom_list.symptom_id = symptom_list.id and patient_id =? ";

	private final static String INSERT_PATIENT_SYMPTOM_HISTORY = "insert into patient_symptom_list (patient_id, symptom_id, note) values (?, ?,'')";

	private static DbConnection dbConn = null;
	public History()	
	{
		super();
	
	}
	public History(DbConnection conn)	
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
	
	public static JSONObject getProblemList(String patient_id, String username)
	{
		JSONObject o = new JSONObject();
		 
		return o;
	}
	
	public JSONObject toJSON() throws JSONException
	{
		 JSONObject o = new JSONObject();
		 o.put(History.ID, this.getPatientId());
		 //To do fill in the rest 
				 
		 return o;
		 
	}
	
	public static JSONObject getHistory(String patientId, String category,  Date startDate, Date endDate)
	 {
		 JSONObject ret = new JSONObject();
		 PreparedStatement prep;
		 int patient_id = Integer.parseInt(patientId);
		
		 try 
		 {
			 if (dbConn == null)
				 dbConn= new DbConnection();
			 String sql = History.SELECT_PATIENT_HISTORY;
			 	
			 if (startDate != null)
			 {
				sql = sql +  History.SELECT_PATIENT_HISTORY_EXT + SELECT_PATIENT_HISTORY_ORDER_BY;
				prep = dbConn.prepareStatement(sql);
				
				prep.setInt(1, patient_id);
				prep.setString(2, category);
				 
				prep.setDate(3, new java.sql.Date (startDate.getTime()));
				
				if (endDate == null)
				{
					endDate = new java.util.Date();
					prep.setDate(3, new java.sql.Date (endDate.getTime()));
					
				}
			 }
			 else if (endDate != null)
			 {
				 sql = sql +  History.SELECT_PATIENT_HISTORY_EXT + SELECT_PATIENT_HISTORY_ORDER_BY;
					
				 prep = dbConn.prepareStatement(sql);
				 prep.setInt(1, patient_id);
				 prep.setString(2, category);
					 
				 Calendar cal = new GregorianCalendar();
				 cal.set(1920, 1, 1);
				 prep.setDate(3, new java.sql.Date (cal.getTime().getTime()));
				 prep.setDate(4, new java.sql.Date (endDate.getTime()));
					
			 }
			 else
			 {
				 sql = sql + SELECT_PATIENT_HISTORY_ORDER_BY;
				 prep = dbConn.prepareStatement(sql);				
				 prep.setInt(1, patient_id);
				 prep.setString(2, category);
							
			 }
			 System.out.println("Patient: getPatientHistory : query " + prep.toString());
			    
			 ResultSet rs = prep.executeQuery();
			 boolean rtnResults = false;
			 
			 while (rs.next())
			 {
				 //SELECT patient_id, history, category_id, history_date, history_notes
				 
				  rtnResults = true;
		            
				  JSONObject o = new JSONObject();
				  JSONObject o_new = new JSONObject();
			        
			      String history = rs.getString("history");
			      String history_note = rs.getString("history_notes");
			      String priority = rs.getString("priority");
			      String color = rs.getString("color");
			      category = rs.getString("category");
			      Date history_date = rs.getDate("history_date");
			      
			      o.put("patient_id", patient_id);
			      o.put("title", history);
			      o.put("category", category);
			      o.put("priority", priority);
			      
			      if (color != null)
			    	  o.put("color", color);
			      
			      if (history_note != null)
			    	  o.put("note", history_note);
			      
			      if (history_date != null)					    
			    	  o.put("date", history_date);
				     
			      //o_new.put("history", o);
			      ret.append("patient_history", o);	
		     }    
			 
			 if (!rtnResults)
		      {
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
		
		 return ret;
	 }
	
	public int getPatientId() {
		return patientId;
	}

	public void setPatientId(int patientId) {
		this.patientId = patientId;
	}
	
	public static JSONObject saveHistory(String patientId, String[] symptomIds)
	{
		 JSONObject ret = new JSONObject();
		 System.out.println("History: saveHistory : patient id " + patientId);
		 
		 String sql = DELETE_SYMPTOMS;
		
		 try {
			 if (dbConn == null)
				dbConn= new DbConnection();
				
			 PreparedStatement prep = dbConn.prepareStatement(sql);	
			 int patient_id = Integer.parseInt(patientId);
				
			 prep.setInt(1, patient_id);
				
			 int rtn = prep.executeUpdate();
				
			 if (rtn < 0)
			 {
					 return WebUtils.buildErrorJson( "Problem on deletion of current data from database ." );      
			 }
				
			 if (symptomIds != null)
			 {
					sql = INSERT_PATIENT_SYMPTOM_HISTORY;
					prep = dbConn.prepareStatement(sql);	
					
					for (String symptomIdStr: symptomIds)
					{ 
						int symptomId = Integer.parseInt(symptomIdStr);
						prep.setInt(1, patient_id);
						prep.setInt(2, symptomId);
						prep.addBatch();
						
					}
					String queries = prep.toString();
					System.out.println("History: saveHistory : about to execute queries " + queries);
					 
					int[] rtnRes = prep.executeBatch();
					
					 
			}
				
		} catch (SQLException e) {
				// TODO Auto-generated catch block
			// TODO Auto-generated catch block
			 
			System.out.println("History: saveHistory : problem on SQL  " + e.getMessage());
			
			return WebUtils.buildErrorJson( "Problem on selecting data from database ." + e.getMessage());
	      
		}
		 
		 return ret;
		 
	}
	
	 public static JSONObject getSymptomHistory(String patientId)
	 {
		 JSONObject ret = new JSONObject();
		 PreparedStatement prep;
		 DbConnection dbConn = null;
		 try 
		 {
			 if (dbConn == null)
				 dbConn= new DbConnection();

			 if (dbConn == null)
				 dbConn= new DbConnection();
			 ResultSet rs = null;
			 //First check if patient records already exist
			 String sql = SELECT_PATIENT_SYMPTOM_HISTORY_COUNT;
			 ArrayList<Integer> symptomIds = new ArrayList<Integer>();
			 
			 if (patientId == null)
			 {
				 System.out.println("History: getHistory : patient not found  ");
				 
				 sql = SELECT_TEMPLATE_HISTORY;
				 prep = dbConn.prepareStatement(sql);
				 
			 }
			 else
			 {
				 
				 System.out.println("ListHistoryTemplateResource: getHistory : patient found  " + patientId);
					
				 prep = dbConn.prepareStatement(sql);
				 int patient_id = Integer.parseInt(patientId);

				 prep.setInt(1,patient_id);
				 
				 int count = 0;
				 System.out.println("ListHistoryTemplateResource: getHistory : get the number of records " + count);
					
				 rs = prep.executeQuery();
				 if (rs.next())
				 {
					 count = rs.getInt(1);  
					 System.out.println("ListHistoryTemplateResource: getHistory : count is " + count);
						
				 }
				 if (count > 0)
				 {
					 sql = SELECT_PATIENT_SYMPTOM_HISTORY;
					 prep = dbConn.prepareStatement(sql);
					 prep.setInt(1, patient_id);
					 rs = prep.executeQuery();
					 while (rs.next())
					 {
						 int symptomId = rs.getInt("symptom_id");
						 symptomIds.add(symptomId);
					 }
				 }
				 
				 sql = SELECT_TEMPLATE_HISTORY;
				 prep = dbConn.prepareStatement(sql);
				 
			 }
			 System.out.println("ListHistoryTemplateResource: getHistory : about to execute query " + prep.toString());
				
			 rs = prep.executeQuery();
			 boolean rtnResults = false;
			
			 System.out.println("ListHistoryTemplateResource: getHistory : query " + prep.toString());
			 
			 JSONObject catObj =null;
			 
			 String prevCategory = "";
			 String category = "";
			 while (rs.next())
			 {
				  rtnResults = true;
		            
				  int symptomId = rs.getInt(1);

				  category = rs.getString("category");
				  //If this is a new category
				  if (!category.equals(prevCategory))
				  {
					  if (catObj != null)
					  {
						  catObj.put("category", category);
						  ret.append("categories", catObj);
					  }
					  catObj = new JSONObject();
					  
				  }
				  String symptom = rs.getString("symptom");
				  JSONObject symptomObj = new JSONObject();
				  symptomObj.put("name", symptom);
				  symptomObj.put("id", symptomId);
				
				  if (symptomIds.contains(symptomId))
				  {
					  symptomObj.put("hasSymptom", "true");
				  }
				  else
				  {
					  symptomObj.put("hasSymptom", "false");
				  }
				  
				  catObj.append("symptoms", symptomObj);
			     
				  prevCategory = category;
			      
		     }    
			 
			 catObj.put("category", category);
			 ret.append("categories", catObj);
			  
			 if (!rtnResults)
		      {
		        	return WebUtils.buildErrorJson( "There is no medical history listed " );
		      	  
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
		
		 return ret;
	 }
}