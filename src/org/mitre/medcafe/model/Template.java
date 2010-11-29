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
public class Template extends Widget
{
	public final static String KEY = Template.class.getName();
	public final static Logger log = Logger.getLogger( KEY );
	// static{log.setLevel(Level.FINER);}

	//Parameters that are common to all Widgets
	private int templateId =0;
	
	public static final String SELECT_TEMPLATE_WIDGET_PARAMS = "SELECT widget_id, param, value from template_widget_params where username = ? and template_id = ? and widget_id =? ";
	public static final String SELECT_TEMPLATE_WIDGETS = "SELECT id, widget_id, param, value from template_widget_params where username = ? and template_id = ? ORDER BY widget_id ";
	public static final String INSERT_TEMPLATE_WIDGETS = "INSERT INTO template_widget_params  ( widget_id, template_id, patient_id, username, param, value ) values (?,?,-1,?,?,?) ";
	public static final String DELETE_TEMPLATE_WIDGETS = "DELETE FROM template_widget_params where ( template_id = ?  AND username=?) ";

	public static final String COPY_TEMPLATES = "INSERT INTO widget_params (widget_id,patient_id,username, param, value)  ( SELECT widget_id, ? ,username, param, value from template_widget_params where username = ? and template_id = ? ) ";
	public static final String SELECT_TEMPLATES = "SELECT widget_id, param, value from template where username = ? and template_id = ? and widget_id =? ";
	public static final String INSERT_TEMPLATES = "INSERT INTO template  ( widget_id, template_id, patient_id, username, param, value ) values (?,?,-1,?,?,?) ";
	public static final String DELETE_TEMPLATES = "DELETE FROM template where ( template_id = ?  AND username=?) ";
	
	public Template()
	{
		super();

	}


	public JSONObject toJSON() throws JSONException
	{
		 JSONObject o = super.toJSON();
		 o.put(Template.ID, this.getTemplateId());		
		 return o;
	}

	public static JSONObject copyTemplate(String templateId, String patientId, String userName) throws SQLException 
	{
		//Copy the template table data into widget table 
		System.out.println("Template : copyTemplate about to execute copy  " );

		JSONObject o = new JSONObject();
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
			int template_id = Integer.parseInt(templateId);

			int patient_id = Integer.parseInt(patientId);
			String err_mess = "Could not update the widgets for patient  " + patient_id;
			//INSERT INTO widget_params (widget_id,patient_id,username, param, value)  ( SELECT widget_id, ? ,username, param, value from template_widget_params where username = ? and template_id = ? ) ";
			String copyQuery = Template.COPY_TEMPLATES;

			prep= dbConn.prepareStatement(copyQuery);
			prep.setInt(1, patient_id);
			prep.setString(2, userName);
			prep.setInt(3, template_id);
			
			System.out.println("Template : copyTemplate about to execute batch copy " + prep.toString());

			int rtn = prep.executeUpdate();
			if (rtn < 0 )
			{
				closeConnections(dbConn, null, null);
				dbConn = null;
				return WebUtils.buildErrorJson( "Problem on copying template widget data from database ." );
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			return WebUtils.buildErrorJson( "Problem on copying template widget data from database ." );
			
		}
		finally
		{

			closeConnections(dbConn, prep, null);
			dbConn = null;
			prep = null;
		}
		return o;
	}
	
	public static JSONObject retrieveTemplateCopy(String templateId, String patientId, String userName) throws SQLException
	{
		//Delete any existing widgets from widget table
		deleteWidgets(patientId,userName);
	
		//Copy the template table data into widget table 
		copyTemplate(templateId, patientId, userName);
		
		//Retrieve the new widget list
		JSONObject widgets = listWidgets( userName, patientId);
		
		//Retrieve the widgets
		return widgets;
	}
	
	public static JSONObject retrieveTemplate(String templateId, String patientId, String userName)
	{
		//Select the template Object
		//Copy the template table data into widget table 
		//Retrieve the widgets
		return null;
	}
	
	public static JSONObject deleteTemplateWidgets( String patientId, String userName) throws SQLException
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
			String deleteQuery = Template.DELETE_WIDGETS;
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

	public static JSONObject saveTemplateWidgets( String userName, JSONObject widgetJSON) throws SQLException
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
			String idStr = widgetJSON.getString(Template.WIDGET_ID);
			int id = Integer.parseInt(idStr);

			String patient_idStr = widgetJSON.getString(Template.ID);
			int patient_id = Integer.parseInt(patient_idStr);
			String err_mess = "Could not update the widgets for patient  " + patient_id;
			//public static final String DELETE_WIDGETS = "DELETE FROM widget_params where ( patient_id=?, username=?) ";

			String remove = widgetJSON.getString(Template.REMOVE);

			//This widget has been marked for removal
			if (remove.equals(Template.TRUE))
				return ret;

			String updateQuery = Template.INSERT_WIDGETS;

			prep= dbConn.prepareStatement(updateQuery);

			//INSERT_WIDGETS = "INSERT INTO widget_params  ( widget_id, patient_id, username, param, value ) values (?,?,?,?,?) ";

			Iterator iter = widgetJSON.keys();
			while (iter.hasNext())
			{
				String key = iter.next().toString();
				//Skip the key values
				if (key.equals(Template.WIDGET_ID) || key.equals(Template.ID) )
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

	

	public static HashMap<String, Template> retrieveTemplateWidgets(String userName, String patientId) throws SQLException
	{
		 HashMap<String, Template> widgetList = new HashMap<String, Template>();
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
			prep= dbConn.prepareStatement(Template.SELECT_TEMPLATE_WIDGETS);
			prep.setString(1, userName);
			prep.setInt(2, patId);

			System.out.println("Widget : retrieveWidgets : query " + prep.toString());

			rs =  prep.executeQuery();
			int lastId = 0;
			//This lists all the paramaters - gather together into a HashMap - keyed on id
			Template widget = new Template();
			HashMap<String, String> params = new HashMap<String, String>() ;

			while (rs.next())
			{
				int widgetId = rs.getInt("widget_id");
				int serial_id = rs.getInt("id");

				if (lastId != widgetId)
				{
					//Create new Widget
					widget = new Template();
					widget.setPatientId(patId);
					widget.setId(widgetId);
					params = new HashMap<String, String>();
					widget.setParams(params);
					widgetList.put(widgetId + "", widget);

				}
				
				String param = rs.getString("param");
				String value = rs.getString("value");
				
				//System.out.println("Widget ID: " + widgetId + " param: " + param + " value: " + value);
				
				if (param.equals(Template.TAB_ORDER))
				{
					int tabOrdInt = Integer.parseInt(value);
					widget.setTabOrder( tabOrdInt );
				}
				else if (param.equals(Template.TYPE))
					widget.setType(value);
				else if (param.equals(Template.LOCATION))
					widget.setLocation(value);
			//	else if (param.equals(Widget.REPOSITORY))
			//		widget.setRepository(value);
				else if (param.equals(Template.NAME))
					widget.setName(value);
				else if (param.equals(Template.SERVER))
					widget.setServer(value);
				else if (param.equals(Template.REP_PATIENT_ID))
					widget.setRepPatientId(value);
				else if (param.equals(Template.COLUMN))
					widget.setColumn(value);
				else if (param.equals(Template.TAB_NUMBER))
					widget.setTab_num(value);
				else if (param.equals(Template.INETTUTS))
					widget.setINettuts(value);
				else if (param.equals(Template.COLOR_NUM))
					widget.setColorNum(value);
				else if (param.equals(Template.LABEL))
					widget.setLabel(value);
				else if (param.equals(Template.COLLAPSED))
					widget.setCollapsed(value);
				else if (param.equals(Template.IMAGE))
					widget.setImage(value);
				else if (param.equals(Template.IN_FOCUS))
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



	public int getTemplateId() {
		return templateId;
	}



	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}



}
