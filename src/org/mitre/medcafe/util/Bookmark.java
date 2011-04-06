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
package org.mitre.medcafe.util;

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

/**
 *  Representation of the text data
 *  @author: Jeffrey Hoyt
 */
public class Bookmark
{

    public final static String KEY = Bookmark.class.getName();
    public final static Logger log = Logger.getLogger( KEY );
    //static{log.setLevel(Level.FINER);}
	private String name = "";
	private String description = "";
	private String notes = "";
	private String url = "";
	public static final String SELECT_BOOKMARK_ALL = "SELECT name, url, description, note from user_bookmark where username = ? and patientId = ? ";
	public static final String SELECT_BOOKMARK = "SELECT name, url, description, note from user_bookmark where username = ? and patientId = ? and name = ?";
	public static final String UPDATE_BOOKMARK = "UPDATE user_bookmark SET ( name=?, url=?, description=?) where username = ? and patientId = ? and name = ? ";
	public static final String INSERT_BOOKMARK = "INSERT INTO user_bookmark ( username, patientId, name, url, description, note ) values (?,?, ?,?,?,?) ";
	public static final String DELETE_BOOKMARK = "delete from user_bookmark where username = ? and patientId = ? ";
	
	public Bookmark(String name, String url, String description, String notes)	
	{
		this(name, url, description);		
		this.notes = notes;
	}
	
	public Bookmark(String name, String url, String description)	
	{
		this(name, url);		
		this.description = description;
	}
	
	public Bookmark(String name, String url)	
	{
		this(name);		
		this.url = url;
	}
	
	public Bookmark(String name)	
	{
		this();		
		this.name = name;
	}
	
	public Bookmark()	
	{
		super();
	}
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}



	public String getUrl() {
		return url;
	}



	public void setUrl(String url) {
		this.url = url;
	}
	
	public static ArrayList<Bookmark> getBookmarks(String userid, String patientId)
	{
		ArrayList<Bookmark> bookmarks = new ArrayList<Bookmark>();
		DbConnection dbConn = null;
		PreparedStatement prep = null;
		ResultSet rs = null;
		try 
		{
		   dbConn= new DbConnection();
		   log.finer("BookmarK: getBookmarks : got connection " );
           
		   prep = dbConn.prepareStatement(Bookmark.SELECT_BOOKMARK_ALL);
		   prep.setString(1, userid);
		   prep.setString(2, patientId);
		   
		   log.finer("BookmarK: getBookmarks : query " + prep.toString());
           
		   rs = prep.executeQuery();
		   Bookmark bookmark = new Bookmark();
		   
		   while (rs.next())
		   {
			   
			   String name = rs.getString(1);
			   String url = rs.getString(2);
			   String description = rs.getString(3);
			   bookmark =  new Bookmark(name, url, description);
			   bookmarks.add(bookmark);
		   }
			
			 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		finally {
			DatabaseUtility.close(rs);
			DatabaseUtility.close(prep);
			if (dbConn != null)
				dbConn.close();
		}
		
		return bookmarks;
		
	}
	
	public static boolean deleteBookmarks(String userid, String patientId) throws SQLException
	{
		DbConnection dbConn = null;
		PreparedStatement prep = null;
		int rtnVal = -1;
		try {
			dbConn = new DbConnection();
			log.finer("Bookmark: deleteBookmarks : got connection " );
		  
			prep = dbConn.prepareStatement(Bookmark.DELETE_BOOKMARK);
			prep.setString(1, userid);
			prep.setString(2, patientId);
			rtnVal = prep.executeUpdate();
			log.finer("Bookmark: deleteBookmarks : rtnVal " + rtnVal );
		}
		catch (SQLException e)
		{
			throw e;
		}
		finally {
			DatabaseUtility.close(prep);
			dbConn.close();
		}
		 if (rtnVal > -1)
			 return true;
		 else
			 return false;
	}
	public static boolean updateBookmarks(String userid, String patientId, ArrayList<Bookmark> bookmarks)
	{
		DbConnection dbConn = null;
		PreparedStatement prep = null;
		try 
		{
		   dbConn= new DbConnection();
		   log.finer("Bookmark: updateBookmarks : got connection " );
		   boolean deleteSuccess = deleteBookmarks(userid, patientId);
		   if (!deleteSuccess)
		   {
		   	   if (dbConn != null)
		   	   	   dbConn.close();
			   return false;
		   }
		   //public static final String INSERT_BOOKMARK = "SELECT name, url, description, note from user_bookmark where username = ? and patientId = ? ";
			
		   prep = dbConn.prepareStatement(Bookmark.INSERT_BOOKMARK);  
		  
		   for (Bookmark bookmark: bookmarks)
		   {
			   prep.clearParameters();
			   prep.setString(1, userid);
			   prep.setString(2, patientId);
			   
			   
			   String name = bookmark.getName();
			   if (name == null)
				   name = "";
			   String url = bookmark.getUrl();
			   if (url == null)
				   url = "";
			   String desc = bookmark.getDescription();
			   if (desc == null)
				   desc = "";
			   
			   String note = bookmark.getNotes();
			   if (note == null)
				   note = "";
			  
			   prep.setString(3, name);
			   prep.setString(4, url);
			   prep.setString(5, desc);
			   prep.setString(6, note);
				 
			   log.finer("Bookmark: updateBookmarks : query  " + prep.toString() );
				
			   prep.addBatch();
		   }
		   int[] res = prep.executeBatch();
		   	
		   log.finer("Bookmark: update Bookmarks - Results for update " + res.toString()); 
			 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	
			return false;
		}
		finally {
			DatabaseUtility.close(prep);
			if (dbConn != null)
				dbConn.close();
		}
		return true;
		
	}
}
