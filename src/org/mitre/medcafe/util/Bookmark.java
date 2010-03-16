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
	private String name = "";
	private String description = "";
	private String notes = "";
	private String url = "";
	public static final String SELECT_BOOKMARK = "SELECT name, url, description, note from user_bookmark where username = ? and patientId = ? ";
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
		try 
		{
		   dbConn= new DbConnection();
		   System.out.println("BookmarK: getBookmarks : got connection " );
           
		   PreparedStatement prep = dbConn.prepareStatement(Bookmark.SELECT_BOOKMARK);
		   prep.setString(1, userid);
		   prep.setString(2, patientId);
		   
		   System.out.println("BookmarK: getBookmarks : query " + prep.toString());
           
		   ResultSet rs = prep.executeQuery();
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
			dbConn.close();
		}
		return bookmarks;
		
	}
}
