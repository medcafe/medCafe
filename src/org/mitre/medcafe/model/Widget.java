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
import org.mitre.medcafe.util.DatabaseUtility;
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
	protected String tab_num = null;
	protected String column = null;
	private String iNettuts = "true";
	private String collapsed = "false";
	private String label = "";
	private int colorNum = 1;
	private String image = "";
	private String inFocus = null;


	//All the other parameters
	private  HashMap<String, String > params = new HashMap<String, String >();

	public static final String ID = "patient_id";
	public static final String REP_PATIENT_ID = "rep_patient_id";

	public static final String WIDGET_ID = "id";
	public static final String TAB_ORDER = "order";
	public static final String LOCATION = "location";
	//public static final String REPOSITORY = "repository";
	public static final String TYPE = "type";
	public static final String NAME = "name";
	public static final String SERVER = "server";
	public static final String REMOVE = "remove";
	public static final String TRUE = "true";
	public static final String TAB_NUMBER = "tab_num";
	public static final String COLUMN = "column";
	public static final String INETTUTS = "iNettuts";
	public static final String LABEL = "label";
	public static final String COLLAPSED = "collapsed";
	public static final String COLOR_NUM = "color_num";
	public static final String IMAGE = "image";
	public static final String IN_FOCUS = "inFocus";


	public static final String SELECT_WIDGET_PARAMS = "SELECT widget_id, param, value from widget_params where username = ? and patient_id = ? and widget_id =? ";
	public static final String SELECT_WIDGETS = "SELECT id, widget_id, param, value from widget_params where username = ? and patient_id = ? ORDER BY widget_id ";
	public static final String INSERT_WIDGETS = "INSERT INTO widget_params  ( widget_id, patient_id, username, param, value ) values (?,?,?,?,?) ";
	public static final String DELETE_WIDGETS = "DELETE FROM widget_params where ( patient_id=? AND username=?) ";

	public Widget()
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

	public static void closeConnections(DbConnection dbConn, PreparedStatement prep, ResultSet rs) throws SQLException
	{
		DatabaseUtility.close(prep);
		DatabaseUtility.close(rs);
		if (dbConn != null)
			dbConn.close();

	}

	public JSONObject toJSON() throws JSONException
	{
		 JSONObject o = new JSONObject();
		 o.put(Widget.ID, this.getPatientId());
		 o.put(Widget.WIDGET_ID, this.getId());
		 o.put(Widget.TYPE, this.getType());
		// o.put(Widget.REPOSITORY, this.getRepository());
		 o.put(Widget.LOCATION, this.getLocation());
		 o.put(Widget.TAB_ORDER, this.getTabOrder());
		 o.put(Widget.NAME, this.getName());
		 o.put(Widget.SERVER, this.getServer());
		 o.put(Widget.REP_PATIENT_ID, this.getRepPatientId());
		 o.put(Widget.TAB_NUMBER, this.getTab_num());
		 o.put(Widget.COLUMN, this.getColumn());
		 o.put(Widget.INETTUTS, this.getINettuts());
		 o.put(Widget.COLOR_NUM, this.getColorNum());
		 o.put(Widget.COLLAPSED, this.getCollapsed());
		 o.put(Widget.LABEL, this.getLabel());
		 o.put(Widget.IMAGE, this.getImage());
		 if (getInFocus() != null)
		 	o.put(Widget.IN_FOCUS, this.getInFocus());
		 
		 return o;

	}

	public static JSONObject deleteWidgets( String patientId, String userName) throws SQLException
	{
		JSONObject ret = new JSONObject();

		DbConnection dbConn = null;
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
			dbConn = setConnection();
			String deleteQuery = Widget.DELETE_WIDGETS;
			int rtn = 0;
			int patient_id = Integer.parseInt(patientId);
			String err_mess = "Could not delete the widgets for patient  " + patient_id;

			rtn = dbConn.psExecuteUpdate(deleteQuery, err_mess , patient_id, userName);

			if (rtn < 0 )
			{
				closeConnections(dbConn, null, null);
				dbConn = null;
				return WebUtils.buildErrorJson( "Problem on deleting widget data from database ." );
			}
		}
		finally
		{
		closeConnections(dbConn, null, null);
		dbConn = null;
		}

		return ret;
	}

	public static JSONObject saveWidgets( String userName, JSONObject widgetJSON) throws SQLException
	{
		System.out.println("Widget : saveWidgets about to execute for JSONObject  " + widgetJSON.toString());

		JSONObject ret = new JSONObject();
		DbConnection dbConn = null;
      PreparedStatement prep = null;
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
			dbConn = setConnection();
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

			prep= dbConn.prepareStatement(updateQuery);

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

			closeConnections(dbConn, prep, null);
			dbConn = null;
			prep = null;
		}

		return ret;
	}

	public static JSONObject listWidgets(String userName, String patientId) throws SQLException
	{
		JSONObject ret = new JSONObject();
		try
		{
		

			HashMap<String, Widget> widgetList = retrieveWidgets(userName, patientId);
			//split out the tabs

			HashMap<String, MedCafeComponent> compList = MedCafeComponent.getComponentHash();

			//Sort by the tab_order
			TreeMap<Integer, Widget> sortedWidgets = new TreeMap<Integer, Widget>();
			for (Widget widget: widgetList.values())
				sortedWidgets.put(widget.getTabOrder(), widget);
			for (Widget widget: sortedWidgets.values())
			{
				JSONObject widgetJSON = widget.toJSON();
				if( widget.getType().equals("tab") )
                    ret.append("tabs", widgetJSON);
                else
                {
						  MedCafeComponent comp = compList.get(widget.getName());
						  if (comp!= null) {
						  widgetJSON.put(MedCafeComponent.SCRIPT, comp.getScript());
						  widgetJSON.put(MedCafeComponent.SCRIPT_FILE, comp.getScriptFile());
						  widgetJSON.put(MedCafeComponent.TEMPLATE, comp.getTemplate());
						  widgetJSON.put(MedCafeComponent.CLICK_URL, comp.getClickUrl());
						  widgetJSON.put(MedCafeComponent.JSON_PROCESS, comp.getJsonProcess());
						  widgetJSON.put(MedCafeComponent.INETTUTS, comp.getINettuts());
						  widgetJSON.put(MedCafeComponent.PARAMS, comp.getParams());
						  widgetJSON.put(MedCafeComponent.CACHE_KEY, comp.getCacheKey());
						  }                		
                    ret.append("widgets", widgetJSON);
					 }
			}
		}
		catch (SQLException e)
		{
			return WebUtils.buildErrorJson( "Problem on selecting data from database ." + e.getMessage());
		}
		catch (JSONException e)
		{
			return WebUtils.buildErrorJson( "Problem on building JSON Data ." + e.getMessage());
		}

		return ret;
	}

	public static HashMap<String, Widget> retrieveWidgets(String userName, String patientId) throws SQLException
	{
		 HashMap<String, Widget> widgetList = new HashMap<String, Widget>();
	//	 DbConnection dbConn = null;
		 
		 int patId = Integer.valueOf(patientId);
		 DbConnection dbConn = null;
		 ResultSet rs = null;
		 PreparedStatement prep = null;
		 try
		 {
		   log.severe("Connection being set . . .  waiting");
		   dbConn = setConnection();
		   log.severe("Connection is set, preparing statement");
			prep= dbConn.prepareStatement(Widget.SELECT_WIDGETS);
			prep.setString(1, userName);
			prep.setInt(2, patId);

			System.out.println("Widget : retrieveWidgets : query " + prep.toString());

			rs =  prep.executeQuery();
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
					//Create new Widget
					widget = new Widget();
					widget.setPatientId(patId);
					widget.setId(widgetId);
					params = new HashMap<String, String>();
					widget.setParams(params);
					widgetList.put(widgetId + "", widget);

				}
				
				String param = rs.getString("param");
				String value = rs.getString("value");
				
				//System.out.println("Widget ID: " + widgetId + " param: " + param + " value: " + value);
				
				if (param.equals(Widget.TAB_ORDER))
				{
					int tabOrdInt = Integer.parseInt(value);
					widget.setTabOrder( tabOrdInt );
				}
				else if (param.equals(Widget.TYPE))
					widget.setType(value);
				else if (param.equals(Widget.LOCATION))
					widget.setLocation(value);
			//	else if (param.equals(Widget.REPOSITORY))
			//		widget.setRepository(value);
				else if (param.equals(Widget.NAME))
					widget.setName(value);
				else if (param.equals(Widget.SERVER))
					widget.setServer(value);
				else if (param.equals(Widget.REP_PATIENT_ID))
					widget.setRepPatientId(value);
				else if (param.equals(Widget.COLUMN))
					widget.setColumn(value);
				else if (param.equals(Widget.TAB_NUMBER))
					widget.setTab_num(value);
				else if (param.equals(Widget.INETTUTS))
					widget.setINettuts(value);
				else if (param.equals(Widget.COLOR_NUM))
					widget.setColorNum(value);
				else if (param.equals(Widget.LABEL))
					widget.setLabel(value);
				else if (param.equals(Widget.COLLAPSED))
					widget.setCollapsed(value);
				else if (param.equals(Widget.IMAGE))
					widget.setImage(value);
				else if (param.equals(Widget.IN_FOCUS))
					widget.setInFocus(value);
				else
					params.put(param, value);

				lastId = widgetId;
			}

		 }
		 catch (SQLException e)
		 {
		   log.severe("Databse Connection ERRROR: Can't make connection " + e.getMessage());

			throw e;
		 }
		 finally
		 {

		 	closeConnections(dbConn, prep, rs);
		 	rs = null;
		 	prep = null;
		 	dbConn = null;
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

/*	public String getRepository() {
		return repository;
	}

	public void setRepository(String repository) {
		this.repository = repository;
	} */

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
	public String getINettuts() {
		return iNettuts;
	}
	public void setINettuts(String iNettuts)
	{
		this.iNettuts = iNettuts;
	}

	public void setRepPatientId(String repPatientId) {
		this.repPatientId = repPatientId;
	}
	public void setCollapsed(String collapsed)
	{
		this.collapsed = collapsed;
	}
	public String getCollapsed()
	{
		return collapsed;
	}
	public void setColorNum(String colorNum)
	{
		this.colorNum = Integer.parseInt(colorNum);
	}
	public int getColorNum()
	{
		return colorNum;
	}
	public String getLabel()
	{
		return label;
	}
	public void setLabel(String label)
	{
		this.label = label;
	}
	public void setImage(String image)
	{
		this.image = image;
	}
	public String getImage()
	{
		return image;
	}
	public String getInFocus()
	{
		return inFocus;
	}
	public void setInFocus(String inFocus)
	{
		this.inFocus = inFocus;
	}
	
    public String getColumn() { return this.column; }
	public void setColumn(String column) { this.column = column; }
	public String getTab_num() { return this.tab_num; }
	public void setTab_num(String tab_num) { this.tab_num = tab_num; }
	

}
