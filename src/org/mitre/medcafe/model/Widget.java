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
import org.mitre.medcafe.util.Constants;
import org.mitre.medcafe.util.DbConnection;
import org.mitre.medcafe.util.WebUtils;
import org.restlet.ext.json.JsonRepresentation;

/**
 *  Representation of the text data
 *  @author: Gail Hamilton
 */
public class Widget
{
	public final static String KEY = Widget.class.getName();
	public final static Logger log = Logger.getLogger( KEY );
	// static{log.setLevel(Level.FINER);}

	//Parameters that are common to all Widgets
	private int patientId =0;
	private int id =0;
	private String repPatientId = "";
	
	private int tabOrder =0;
	private String location = Constants.CENTER_PANE;
	private String repository = "";
	private String type ="";
	private String name ="";
	private String server ="";

	//All the other parameters
	private  HashMap<String, String > params = new HashMap<String, String >();

	public static final String ID = "patient_id";
	public static final String REP_PATIENT_ID = "rep_patient_id";
	
	public static final String WIDGET_ID = "id";
	public static final String TAB_ORDER = "tab_order";
	public static final String LOCATION = "location";
	public static final String REPOSITORY = "repository";
	public static final String TYPE = "type";
	public static final String NAME = "name";
	public static final String SERVER = "server";
	public static final String REMOVE = "remove";
	public static final String TRUE = "true";

	public static final String SELECT_WIDGET_PARAMS = "SELECT widget_id, param, value from widget_params where username = ? and patient_id = ? and widget_id =? ";
	public static final String SELECT_WIDGETS = "SELECT id, widget_id, param, value from widget_params where username = ? and patient_id = ? ORDER BY widget_id ";
	public static final String INSERT_WIDGETS = "INSERT INTO widget_params  ( widget_id, patient_id, username, param, value ) values (?,?,?,?,?) ";
	public static final String DELETE_WIDGETS = "DELETE FROM widget_params where ( patient_id=? AND username=?) ";

	private static DbConnection dbConn = null;
	public Widget()
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
	}

	public JSONObject toJSON() throws JSONException
	{
		 JSONObject o = new JSONObject();
		 o.put(Widget.ID, this.getPatientId());
		 o.put(Widget.WIDGET_ID, this.getId());
		 o.put(Widget.TYPE, this.getType());
		 o.put(Widget.REPOSITORY, this.getRepository());
		 o.put(Widget.LOCATION, this.getLocation());
		 o.put(Widget.TAB_ORDER, this.getTabOrder());
		 o.put(Widget.NAME, this.getName());
		 o.put(Widget.SERVER, this.getServer());
		 o.put(Widget.REP_PATIENT_ID, this.getRepPatientId());

		 return o;

	}

	public static JSONObject deleteWidgets( String patientId, String userName) throws SQLException
	{
		JSONObject ret = new JSONObject();
		setConnection();

		/**
		key id value 1
		patient_id value 1
		server value http://127.0.0.1
		clickUrl value http://127.0.0.1:8080
		repository value OurVista
		type value images
		location value center
		tab_num value 1
		**/
		try
		{
			String deleteQuery = Widget.DELETE_WIDGETS;
			int rtn = 0;
			int patient_id = Integer.parseInt(patientId);
			String err_mess = "Could not delete the widgets for patient  " + patient_id;

			rtn = dbConn.psExecuteUpdate(deleteQuery, err_mess , patient_id, userName);

			if (rtn < 0 )
				return WebUtils.buildErrorJson( "Problem on deleting widget data from database ." );

		}
		finally
		{

		}
		return ret;
	}

	public static JSONObject saveWidgets( String userName, JSONObject widgetJSON) throws SQLException
	{
		System.out.println("Widget : saveWidgets about to execute for JSONObject  " + widgetJSON.toString());
		
		JSONObject ret = new JSONObject();
		setConnection();

		/**
		key id value 1
		patient_id value 1
		server value http://127.0.0.1
		clickUrl value http://127.0.0.1:8080
		repository value OurVista
		type value images
		location value center
		tab_num value 1
		**/
		try
		{
			String idStr = widgetJSON.getString(Widget.WIDGET_ID);
			int id = Integer.parseInt(idStr);

			String patient_idStr = widgetJSON.getString(Widget.ID);
			int patient_id = Integer.parseInt(patient_idStr);
			String err_mess = "Could not update the widgets for patient  " + patient_id;
			//public static final String DELETE_WIDGETS = "DELETE FROM widget_params where ( patient_id=?, username=?) ";

			String remove = widgetJSON.getString(Widget.REMOVE);
			
			//This widget has been marked for removal
			if (remove.equals(Widget.TRUE))
				return ret;
			
			String updateQuery = Widget.INSERT_WIDGETS;

			PreparedStatement prep= dbConn.prepareStatement(updateQuery);

			//INSERT_WIDGETS = "INSERT INTO widget_params  ( widget_id, patient_id, username, param, value ) values (?,?,?,?,?) ";

			Iterator iter = widgetJSON.keys();
			while (iter.hasNext())
			{
				String key = iter.next().toString();
				//Skip the key values
				if (key.equals(Widget.WIDGET_ID) || key.equals(Widget.ID) )
					continue;
				String value = widgetJSON.getString(key);
				//System.out.println("Widget : About to update id " + id + " patient_id " + patient_id + " userName " + userName + " key " + key + " value " + value);
				//dbConn.psExecuteUpdate(updateQuery, err_mess , id, patient_id, userName, key, value);
				prep.setInt(1, id);
				prep.setInt(2, patient_id);
				prep.setString(3, userName);
				prep.setString(4, key);
				prep.setString(5, value);
				prep.addBatch();

			}
			System.out.println("Widget : saveWidgets about to execute batch " + prep.toString());
			
			prep.executeBatch();
		}
		catch (JSONException e) {
			
			// TODO Auto-generated catch block
			System.out.println("Widget : saveWidgets Problem on updating widget data from database ." + e.getMessage());
			
			return WebUtils.buildErrorJson( "Problem on updating widget data from database ." + e.getMessage());

		}
		finally
		{

		}
		return ret;
	}

	public static JSONObject listWidgets(String userName, String patientId) throws SQLException
	{

		JSONObject ret = new JSONObject();
		try
		{

			HashMap<String, Widget> widgetList = retrieveWidgets(userName, patientId);
			//Sort by the tab_order
			TreeMap<Integer, Widget> sortedWidgets = new TreeMap<Integer, Widget>();
			//System.out.println("Widget : listWidgets : number of returned widgets " + widgetList.size());

			for (Widget widget: widgetList.values())
			{
				//System.out.println("Widget : listWidgets : id " + widget.getId() + " tab Order " + widget.getTabOrder());

				sortedWidgets.put(widget.getTabOrder(), widget);
			}

			for (Widget widget: sortedWidgets.values())
			{
				JSONObject widgetJSON = widget.toJSON();
				ret.append("widgets", widgetJSON);
			}

			//System.out.println("Widget : listWidgets : number of returned sorted widgets " + sortedWidgets.size());
			//System.out.println("Widget : listWidgets : Returned val " + ret.toString());

		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			return WebUtils.buildErrorJson( "Problem on selecting data from database ." + e.getMessage());
		}
		catch (JSONException e)
		{
			// TODO Auto-generated catch block
			return WebUtils.buildErrorJson( "Problem on building JSON Data ." + e.getMessage());
		}

		return ret;
	}

	public static HashMap<String, Widget> retrieveWidgets(String userName, String patientId) throws SQLException
	{
		 HashMap<String, Widget> widgetList = new HashMap<String, Widget>();
		 DbConnection dbConn = null;

		 int patId = Integer.valueOf(patientId);

		 try
		 {
			dbConn= new DbConnection();

			PreparedStatement prep= dbConn.prepareStatement(Widget.SELECT_WIDGETS);
			prep.setString(1, userName);
			prep.setInt(2, patId);

			System.out.println("Widget : retrieveWidgets : query " + prep.toString());

			ResultSet rs =  prep.executeQuery();
			int lastId = 0;
			//This lists all the paramaters - gather together into a HashMap - keyed on id
			Widget widget = new Widget();
			HashMap<String, String> params = new HashMap<String, String>() ;
			while (rs.next())
			{
				int widgetId = rs.getInt("widget_id");
				int serial_id = rs.getInt("id");

				if (lastId != widgetId)
				{
					//System.out.println("Widget: getWidgets : creating new widget " + widgetId );

					//Create new Widget
					widget = new Widget();

					widget.setPatientId(patId);
					widget.setId(widgetId);
					params = new HashMap<String, String>();
					widget.setParams(params);

					widgetList.put(widgetId + "", widget);
				}
				String param = rs.getString("param");
				//System.out.println("Widget: getWidgets : id " + widgetId + " value for param " + param );

				String value = rs.getString("value");
				if (param.equals(Widget.TAB_ORDER))
				{
					int tabOrdInt = Integer.parseInt(value);

					widget.setTabOrder( tabOrdInt );

				}
				else if (param.equals(Widget.TYPE))
				{
					widget.setType(value);
				}
				else if (param.equals(Widget.LOCATION))
				{
					widget.setLocation(value);
				}
				else if (param.equals(Widget.REPOSITORY))
				{
					widget.setRepository(value);
				}
				else if (param.equals(Widget.NAME))
				{
					widget.setName(value);
				}
				else if (param.equals(Widget.SERVER))
				{
					widget.setServer(value);
				}
				else if (param.equals(Widget.REP_PATIENT_ID))
				{
					widget.setRepPatientId(value);
				}
				else
				{
					params.put(param, value);
				}

				lastId = widgetId;
			}

		 }
		 catch (SQLException e)
		 {
			dbConn.close();

			throw e;
		 }
		 finally
		 {
			 dbConn.close();
		 }
		 return widgetList;

	}

	public int getTabOrder() {
		return tabOrder;
	}

	public void setTabOrder(int tabOrder) {
		this.tabOrder = tabOrder;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getRepository() {
		return repository;
	}

	public void setRepository(String repository) {
		this.repository = repository;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public HashMap<String, String> getParams() {
		return params;
	}


	public void setParams(HashMap<String, String> params) {
		this.params = params;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getRepPatientId() {
		return repPatientId;
	}

	public void setRepPatientId(String repPatientId) {
		this.repPatientId = repPatientId;
	}
}
