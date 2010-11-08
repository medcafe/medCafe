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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mitre.medcafe.util.DbConnection;
import org.mitre.medcafe.util.DatabaseUtility;
import org.mitre.medcafe.util.WebUtils;
import org.projecthdata.hdata.schemas._2009._06.condition.Condition;

/**
 *  Representation of the Problem List Item
 *  @author: Gail Hamilton
 */
public class Address
{
	public final static String KEY = Address.class.getName();
	public final static Logger log = Logger.getLogger( KEY );
	static{log.setLevel(Level.FINER);}

	//Parameters that are common to all Widgets
	private int patientId =0;
	
	public static final String ID = "patient_id";
	
	public static String  STREET ="street";
	public static String  STREET2 ="street2";
	public static String  ZIP ="zip";
	public static String  COUNTRY ="country";
	public static String  STATE ="state";
	public static String  CITY ="city";
	
	private String street = "";
	private String street2 = "";
	private String city = "";
	private String state = "";
	private String zip = "";
	private String country = "";
	
	private final static String SELECT_PATIENT_ADDRESS ="SELECT street, street2, city, state, zip, country from address where patient_id = ? ";
	public final static  String DELETE_ADDRESS = "Delete from address where patient_id = ?";
	private final static String INSERT_PATIENT_ADDRESS = "insert into address (patient_id, street, street2, city, state, zip, country ) values (?,?,?,?,?,?,?)";

		
	public Address()	
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
	
	
	public JSONObject toJSON() throws JSONException
	{
		JSONObject ret = new JSONObject();
		JSONObject o = new JSONObject();
		 o.put(Address.ID, this.getPatientId());
		 //To do fill in the rest 
		 String street = this.getStreet();
		  
	     String street2 = this.getStreet2();
	      
	     String city = this.getCity();
	     String state = this.getState();
	     String zip = this.getZip();	      
	     String country = this.getCountry();
      
	     o.put(Address.STREET, street);
	     if (street2 != null)
	    	  o.put(Address.STREET2, street);
	      
	     o.put(Address.CITY, city);
	     o.put(Address.STATE, state);
	     o.put(Address.ZIP, zip);
	     o.put(Address.COUNTRY, country);
		 
	     ret.append("address", o);	
		 
	     return ret;
	}
	
	public int getPatientId() {
		return patientId;
	}
	public void setPatientId(int patientId) {
		this.patientId = patientId;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getStreet2() {
		return street2;
	}
	public void setStreet2(String street2) {
		this.street2 = street2;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	
	 public static JSONObject getAddress(String patientId)
	    {
	    	 JSONObject ret = new JSONObject();
	    	 DbConnection dbConn = null;
			 PreparedStatement prep = null;
			 ResultSet rs = null;
			 int patient_id = Integer.parseInt(patientId);
			
			 try 
			 {
				 dbConn = setConnection();
				 String sql = Address.SELECT_PATIENT_ADDRESS;
				     
				 prep = dbConn.prepareStatement(sql);				
				 prep.setInt(1, patient_id);
				
				 System.out.println("ListAddressResource: getAddress : query " + prep.toString());
					
				 rs = prep.executeQuery();
				 boolean rtnResults = false;
				 Address address = new Address();
				 
				 while (rs.next())
				 {
					 //SELECT patient_id, history, category_id, history_date, history_notes
					 
					  rtnResults = true;
			            
					  JSONObject o = new JSONObject();
					  String street = rs.getString(Address.STREET);
				      String street2 = rs.getString(Address.STREET2);
				      String city = rs.getString(Address.CITY);
				      String state = rs.getString(Address.STATE);
				      String zip = rs.getString(Address.ZIP);
				      String country = rs.getString(Address.COUNTRY);
				        
				      address.setStreet(street);
				      
				      if (street2 != null)
				    	  address.setStreet2(street2);
				      
				      address.setCity(city);
				      address.setCountry(country);
				      address.setZip(zip);
				      address.setCountry(country);
				      address.setState(state);
					     
				      ret = address.toJSON();
				     
			     }    
				 
				 if (!rtnResults)
			      {
					 JSONObject o = new JSONObject();
					 o.put(Address.STREET, "");
				     o.put(Address.STREET2, "");
				      
				      o.put(Address.CITY, "");
				      o.put(Address.STATE, "");
				      o.put(Address.ZIP, "");
				      o.put(Address.COUNTRY, "");
						      
				      //o_new.put("history", o);
				      ret.append("address", o);	
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
			finally {
				DatabaseUtility.close(rs);
				DatabaseUtility.close(prep);
				dbConn.close();
			}
			
			 return ret;
	 }
	
	 public  JSONObject saveAddress(String patientId, JSONObject addresses)
	 {
			 JSONObject ret = new JSONObject();
			 
			 String sql = Address.DELETE_ADDRESS;
			DbConnection dbConn = null;
			PreparedStatement prep = null;
		
			 try {
				dbConn = setConnection();
					
				 prep = dbConn.prepareStatement(sql);	
				 int patient_id = Integer.parseInt(patientId);
					
				 prep.setInt(1, patient_id);
					
				 int rtn = prep.executeUpdate();
					
				 if (rtn < 0)
				 {
				 	 DatabaseUtility.close(prep);
				 	 dbConn.close();
						 return WebUtils.buildErrorJson( "Problem on deletion of current data from database ." );      
				 }
					
				 if (addresses == null)
					 System.out.println("Address: saveAddress : address is null ");
					
				 if (addresses != null)
				 {
						sql = INSERT_PATIENT_ADDRESS;
						//"insert into address (patient_id, street, street2, city, state, zip, country ) values (?,?,?,?,?,?,?)";

						prep = dbConn.prepareStatement(sql);	
						
						prep.setInt(1, patient_id);
						//JSONObject address = addresses.getJSONObject("address");
						Object test = addresses.get("address");
						JSONObject address = new JSONObject();
						
						if (test instanceof JSONArray)
						{
							JSONArray addressArray = ((JSONArray)test);
							Object firstAddress = addressArray.get(0);
							
							if (firstAddress instanceof JSONObject)
							{
								address = (JSONObject)firstAddress;
								
							}
						}
						else if (test instanceof JSONObject)
						{
							 address = ((JSONObject)test);
							
						}
						
						Object streetObj = address.get(Address.STREET);
						Object cityObj = address.get(Address.CITY);
						Object stateObj = address.get(Address.STATE);
						Object zipObj = address.get(Address.ZIP);
						Object cntryObj = address.get(Address.COUNTRY);
						
						String street = "";
						String city ="";
						String state="";
						String zip="";
						String country= "";
						
						if (streetObj != null)
							street = streetObj.toString();
						if (cityObj != null)
							city = cityObj.toString();
						if (stateObj != null)
							state = stateObj.toString();
						if (zipObj != null)
							zip = zipObj.toString();
						if (cntryObj != null)
							country = cntryObj.toString();
						
						prep.setInt(1, patient_id);
						prep.setString(2, street);
						prep.setString(3, "");
						prep.setString(4, city);
						prep.setString(5, state);
						prep.setString(6, zip);
						prep.setString(7, country);
						
						String query = prep.toString();
						 
						int rtnRes = prep.executeUpdate();
						
				}
					
			} catch (SQLException e) {
					// TODO Auto-generated catch block
				// TODO Auto-generated catch block
				 
				System.out.println("Address: saveAddress : problem on SQL  " + e.getMessage());		
				return WebUtils.buildErrorJson( "Problem on selecting data from database for address ." + e.getMessage());
		      
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return WebUtils.buildErrorJson( "Problem on processing JSON Object for address ." +addresses.toString() + " " + e.getMessage());
			}
			finally{

				DatabaseUtility.close(prep);
				dbConn.close();
			}
			 
			 return ret;
			 
		}
	 
	 
}
