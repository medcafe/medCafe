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
import java.util.logging.Logger;

import java.util.*;

import org.json.JSONException;
import org.json.JSONObject;

import org.mitre.medcafe.util.DbConnection;
import org.mitre.medcafe.util.DatabaseUtility;
import org.mitre.medcafe.util.WebUtils;
/**
 *  Representation of the text data
 *  @author: Gail Hamilton
 */
public class ImageTag
{
	public final static String KEY = ImageTag.class.getName();
	public final static Logger log = Logger.getLogger( KEY );
	// static{log.setLevel(Level.FINER);}

	//Parameters that are common to all Widgets
	private int patientId =0;
	private int fileId =0;
	private float xOrigin =0;
	private float yOrigin =0;
	private float shapeX =0.0f;
	private float shapeY =0.0f;
	private float width =0.0f;
	private float height =0.0f;
	private float zoom =0.0f;
	private String color ="";
	private String type ="";
	private String note ="";
	
	

	//All the other parameters
	private  HashMap<String, String > params = new HashMap<String, String >();

	public static final String ID = "patient_id";
	public static final String REP_PATIENT_ID = "rep_patient_id";
	
	public static final String FILE_ID = "file_id";
	public static final String X_ORIGIN = "x_origin";
	public static final String Y_ORIGIN = "y_origin";
	public static final String SHAPE_X = "shape_x";
	public static final String SHAPE_Y = "shape_y";
	public static final String WIDTH = "width";
	public static final String HEIGHT = "height";
	public static final String ZOOM = "zoom";
	public static final String COLOR = "color";
	public static final String TYPE = "shape_type";
	public static final String NOTE = "note";
	public static final String TRUE = "true";
	
	public static final String REMOVE = "remove";

	/*patient_id integer NOT NULL,
	username character varying(50) NOT NULL,
	file_id  integer NOT NULL,
	x_origin integer NULL,
	y_origin integer NULL,
	shape_x float NULL,
	shape_y float NULL,
	width float NULL,
	height float NULL,
	zoom integer NULL,
	color character varying(50) NULL,
	shape_type vcharacter varying(50) NOT NULL, 
	note character varying(500) NOT NULL*/
	
	public static final String INSERT_TAGS= "INSERT INTO file_annotations " + 
											" ( file_id, patient_id, username, x_origin, y_origin, shape_x, shape_y, width, height, zoom, color, shape_type, note  ) " +
											" values (?, ?,?,?,?,?,?,?,?,?,?,?,?) ";
	public static final String DELETE_TAGS = "DELETE FROM file_annotations where ( patient_id=? AND username=? AND file_id=?) ";
	public static final String SELECT_FILEIDS = "SELECT id, filename from file where ( patient_id=? AND filename=? ) ";
	public static final String SELECT_TAGS = "SELECT file_id, patient_id, username, x_origin, y_origin, shape_x, shape_y, width, height, zoom, color, shape_type, note from file_annotations where ( patient_id=? AND username=? AND file_id=?) ";

	public ImageTag()
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
		 JSONObject o = new JSONObject();
		 o.put(ImageTag.ID, this.getPatientId());
		 o.put(ImageTag.FILE_ID, this.getFileId());
		 o.put("x", this.getShapeX());
		 o.put("y", this.getShapeY());
		 o.put(ImageTag.X_ORIGIN, this.getXOrigin());
		 o.put(ImageTag.Y_ORIGIN, this.getYOrigin());
		 o.put(ImageTag.WIDTH, this.getWidth());
		 o.put(ImageTag.HEIGHT, this.getHeight());
		 o.put(ImageTag.ZOOM, this.getZoom());
		 o.put("type", this.getType());
		 o.put(ImageTag.COLOR, this.getColor());
		 o.put(ImageTag.NOTE, this.getNote());				 
						
		 return o;

	}

	public static int getFileId(DbConnection dbConn, int patId, String fileName) throws NumberFormatException, SQLException
	{
		ResultSet rs = null;
		int fileId = 0;
		try {
		String selectFileIDQuery = ImageTag.SELECT_FILEIDS;
		//public static final String SELECT_FILEIDS = "SELECT file_id, filename from file where ( patient_id=? AND filename=? ) ";
		
		String err_mess = "Could not get a file Id based on the filename  " + fileName;

		System.out.println("ImageTag getFileId patientId " + patId + " fileName " + fileName);
		 
		rs = dbConn.psExecuteQuery(selectFileIDQuery, err_mess , patId, fileName);

		if (rs.next())
		{
			String file_id = rs.getString("id");
			fileId = Integer.valueOf(file_id);
		}
		}
		catch (SQLException e)
		{
			throw e;
		}
		finally {
			DatabaseUtility.close(rs);
			rs = null;
		}
		return fileId;
	}
	
	public static JSONObject deleteAnnotations( String patientId, String userName, String fileName) throws SQLException
	{
		JSONObject ret = new JSONObject();
		DbConnection dbConn = null;

		try
		{
			dbConn = setConnection();
			String deleteQuery = ImageTag.DELETE_TAGS;
			//public static final String DELETE_TAGS = "DELETE FROM file_annotations where ( patient_id=? AND username=? AND file_id=?) ";
			
			int rtn = 0;
			int patient_id = Integer.parseInt(patientId);
			int fileId = getFileId(dbConn, patient_id, fileName);
			 
			String err_mess = "Could not delete the image tags for patient  " + patient_id;

			rtn = dbConn.psExecuteUpdate(deleteQuery, err_mess , patient_id, userName, fileId);

			if (rtn < 0 )
			{
				dbConn.close();
				return WebUtils.buildErrorJson( "Problem on deleting image tag data from database ." );
			}
		}
		catch (SQLException e)
		{
			throw e;
		}
		finally
		{
			dbConn.close();
		}
		return ret;
	}

	public JSONObject saveAnnotations( String userName, JSONObject annotationJSON) throws SQLException
	{
		JSONObject ret = new JSONObject();
		DbConnection dbConn = null;
		PreparedStatement prep = null;
		/*patient_id integer NOT NULL,
		username character varying(50) NOT NULL,
		file_id  integer NOT NULL,
		x_origin integer NULL,
		y_origin integer NULL,
		shape_x float NULL,
		shape_y float NULL,
		width float NULL,
		height float NULL,
		zoom integer NULL,
		color character varying(50) NULL,
		shape_type vcharacter varying(50) NOT NULL, 
		note character varying(500) NOT NULL*/
		
		try
		{
			  dbConn = setConnection();
			
			String fileName = annotationJSON.getString("image");
		
			String patient_idStr = annotationJSON.getString(ImageTag.ID);
			patientId = Integer.parseInt(patient_idStr);
			
			fileId =  getFileId(dbConn, patientId, fileName);
			
			String xOriginStr = annotationJSON.getString(ImageTag.X_ORIGIN);
			xOrigin = Float.parseFloat(xOriginStr);
			
			String yOriginStr = annotationJSON.getString(ImageTag.Y_ORIGIN);
			yOrigin = Float.parseFloat(yOriginStr);
			
			String shapeXStr = annotationJSON.getString("x");
			shapeX = Float.parseFloat(shapeXStr);
			
			String shapeYStr = annotationJSON.getString("y");
			shapeY= Float.parseFloat(shapeYStr);
			
			String widthStr = annotationJSON.getString(ImageTag.WIDTH);
			width= Float.parseFloat(widthStr);
			
			String heightStr = annotationJSON.getString(ImageTag.HEIGHT);
			height= Float.parseFloat(heightStr);
			
			String zoomStr = annotationJSON.getString(ImageTag.ZOOM);
			zoom= Float.parseFloat(zoomStr);
			
			color = annotationJSON.getString(ImageTag.COLOR);
			type = annotationJSON.getString("type");
			note = annotationJSON.getString("note");
			
			String err_mess = "Could not update the image tag for patient  " + patientId + " and file " + fileId;
			
			//public static final String DELETE_WIDGETS = "DELETE FROM widget_params where ( patient_id=?, username=?) ";
			String remove="false";
			
			if( annotationJSON.has(ImageTag.REMOVE))
			{
				remove = annotationJSON.getString(ImageTag.REMOVE);
			}
			//This tag has been marked for removal
			if (remove.equals(ImageTag.TRUE))
			{
				dbConn.close();
				return ret;
			}
			String updateQuery = ImageTag.INSERT_TAGS;
			//INSERT_WIDGETS = "INSERT INTO table  (file_id, patient_id, username, x_origin, y_origin, shape_x, shape_y, width, height, zoom, color, shape_type  ) values (?,?,?,?,?) ";

			prep= dbConn.prepareStatement(updateQuery);
			
			prep.setInt(1, fileId);
			prep.setInt(2, patientId);
			prep.setString(3, userName);
			prep.setFloat(4, xOrigin);
			prep.setFloat(5, yOrigin);
			prep.setFloat(6, shapeX);
			prep.setFloat(7, shapeY);
			prep.setFloat(8, width);
			prep.setFloat(9, height);
			prep.setFloat(10, zoom);
			prep.setString(11, color);
			prep.setString(12, type);
			prep.setString(13, note);
			//
			System.out.println("ImageTag saveAnnotations sql " + prep.toString() );
			
			int rtn = prep.executeUpdate();
			if (rtn < 0)
			{
				DatabaseUtility.close(prep);
				dbConn.close();
				return WebUtils.buildErrorJson( "Problem on updating annotation data into the database " + prep.toString());

			}
		}
		catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("ImageTag saveAnnotations Problem on updating annotation data to database ." + e.getMessage() );
			
			return WebUtils.buildErrorJson( "Problem on updating annotation data from database ." + e.getMessage());

		}
		catch (SQLException e) {
			throw e;
		}
		finally
		{
			DatabaseUtility.close(prep);
			dbConn.close();
			
		}
		return ret;
	}

	

	public static  JSONObject retrieveAnnotations(String userName, String patientId, String fileName) throws SQLException
	{
		 
		 int patId = Integer.valueOf(patientId);
		 int fileId = 0;
		 JSONObject o = new JSONObject();
		 DbConnection dbConn = null;
		 PreparedStatement prep = null;
		 ResultSet rs = null;
		 try
		 {
			dbConn= setConnection();

			fileId =  getFileId(dbConn, patId, fileName);
			
			if (fileId == 0)
			{
				//If no tags - just bring up image
				return new JSONObject();
			}
			prep= dbConn.prepareStatement(ImageTag.SELECT_TAGS);
			prep.setInt(1, patId);
			prep.setString(2, userName);
			prep.setInt(3, fileId);
			
			System.out.println("ImageTag : retrieveImageTags : query " + prep.toString());

			rs =  prep.executeQuery();
			//This lists all the paramaters - gather together into a HashMap - keyed on id
			ImageTag imageTag = new ImageTag();
			HashMap<String, String> params = new HashMap<String, String>() ;
			while (rs.next())
			{
				imageTag = new ImageTag();
				
				imageTag.setFileId(rs.getInt(ImageTag.FILE_ID)) ;
				imageTag.setPatientId(rs.getInt(ImageTag.ID)) ;
				imageTag.setType(rs.getString(ImageTag.TYPE));
				imageTag.setXOrigin(rs.getFloat(ImageTag.X_ORIGIN));
				imageTag.setYOrigin(rs.getFloat(ImageTag.Y_ORIGIN));
				imageTag.setShapeX(rs.getFloat(ImageTag.SHAPE_X));
				imageTag.setShapeY(rs.getFloat(ImageTag.SHAPE_Y));
				imageTag.setWidth(rs.getFloat(ImageTag.WIDTH));
				imageTag.setHeight(rs.getFloat(ImageTag.HEIGHT));
				imageTag.setZoom(rs.getInt(ImageTag.ZOOM));
				imageTag.setColor(rs.getString(ImageTag.COLOR));
				imageTag.setNote(rs.getString(ImageTag.NOTE));
				
				JSONObject jsonObj = imageTag.toJSON();
				o.append("imageTags", jsonObj);
			}

		 }
		 catch (SQLException e)
		 {
			 return WebUtils.buildErrorJson( "Problem on retrieving annotation data from database ." + e.getMessage());

		 } 
		 catch (JSONException e) {
			// TODO Auto-generated catch block
			 return WebUtils.buildErrorJson( "Problem on building annotation JSON data from database ." + e.getMessage());

		}
		 finally
		 {
		 	DatabaseUtility.close(rs);
		 	DatabaseUtility.close(prep);
		 	dbConn.close();
			 //dbConn.close();
		 }
		 return o;

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

	public int getFileId() {
		return fileId;
	}

	public void setFileId(int fileId) {
		this.fileId = fileId;
	}

	public float getXOrigin() {
		return xOrigin;
	}

	public void setXOrigin(float origin) {
		xOrigin = origin;
	}

	public float getYOrigin() {
		return yOrigin;
	}

	public void setYOrigin(float origin) {
		yOrigin = origin;
	}

	public float getShapeX() {
		return shapeX;
	}

	public void setShapeX(float shapeX) {
		this.shapeX = shapeX;
	}

	public float getShapeY() {
		return shapeY;
	}

	public void setShapeY(float shapeY) {
		this.shapeY = shapeY;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getZoom() {
		return zoom;
	}

	public void setZoom(float zoom) {
		this.zoom = zoom;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
}
