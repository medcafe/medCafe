/*
 *  Copyright 2008 The MITRE Corporation (http://www.mitre.org/). All Rights Reserved.
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


import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.PageContext;

/**
 *  Class to allow for saving of the information in each of the widgets
 *
 * @author     ghamilton	
 * @created    Feb 24, 2010
 */
public class TextProcesses
{

     
   private HashMap<String,Text> textList = new HashMap<String, Text>();
   
   public TextProcesses()
   {
	   
   }
   

   
   public Text getTextObject(String title) throws SQLException
   {
	   Text textObj = textList.get(title);
	   
	   return textObj;
   }
   
   public Text populateTextObject(String userid,  String patientId, String title) throws SQLException
   {
   	   DbConnection dbConn = null;
   	   PreparedStatement prep = null;
   	   ResultSet rs = null;
   	   Text textObj = null;
   	   try{
   	   	  dbConn = new DbConnection();
	 		
   	   	  textObj =  new Text(userid, patientId);
   	   	  prep = dbConn.prepareStatement(Text.SAVE_TEXT_SELECT);
   	   	  int patient_id = Integer.parseInt(patientId);
   	   	  prep.setString(1, userid);
   	   	  prep.setInt(2, patient_id);
   	   	  prep.setString(3, title);
		
   	   	  rs = prep.executeQuery();
   	   	  if (rs.next())
   	   	  {
		 	
   	   	  	  String note = rs.getString(1);
   	   	  	  textObj =  new Text(userid, patientId, title, note);
		  
   	   	  }
   	   }
   	   catch (SQLException e)
   	   {
   	     	  throw e;
   	   }
   	   finally {
   	   	   DatabaseUtility.close(rs);
   	   	   DatabaseUtility.close(prep);
   	   	   dbConn.close();
	   }
	   return textObj;
   }
   
   public TextTemplate populateTemplateObject(String userid,  String title) throws SQLException
   {
	   
	  DbConnection dbConn = null;
	  PreparedStatement prep = null;
	  ResultSet rs = null;
	  TextTemplate templateObj = null;
	  try {
	  	  dbConn = new DbConnection();
	  	  templateObj =  new TextTemplate(userid);
	  	  prep = dbConn.prepareStatement(TextTemplate.SAVE_TEXT_TEMPLATE_SELECT);
	   
	  	  prep.setString(1, userid);
	  	  prep.setString(2, title);
	  
	  	  rs = prep.executeQuery();
	  	  if (rs.next())
	  	  {
		 	
	  	  	  String note = rs.getString(1);
	  	  	  templateObj =  new TextTemplate(userid, title,  note);
		  
	  	  }
	  }
	   catch (SQLException e)
	   {
	   	   throw e;
	   }
	   finally {
	   	   DatabaseUtility.close(rs);
	   	   DatabaseUtility.close(prep);
	   	   dbConn.close();
	   }
	   
		
	   return templateObj;
   }
   
   public HashMap<String,Text> populateTextObjects(String userid,  String patientId) throws SQLException
   {
	   DbConnection dbConn = null;
	  PreparedStatement prep = null;
	  ResultSet rs = null;	
	  Text textObj = null;
	  try {
	  	  dbConn = new DbConnection();
	  	  textObj =  new Text(userid, patientId);
	  	  prep = dbConn.prepareStatement(Text.SAVE_TEXTS_SELECT);
	  	  int patient_id = Integer.parseInt(patientId);
	  	  prep.setString(1, userid);
	  	  prep.setInt(2, patient_id);
		
	  	  rs = prep.executeQuery();
	  	  while (rs.next())
	  	  {
		   
	  	  	  String title = rs.getString(1);
	  	  	  String note = rs.getString(2);
	  	  	  textObj =  new Text(userid, patientId, title, note);
	  	  	  textList.put(title, textObj);
	  	  }
	  }
	  catch (SQLException e)
	  {
	  	  throw e;
	  }
	  finally {
	  	  DatabaseUtility.close(rs);
	  	  DatabaseUtility.close(prep);
	  	  dbConn.close();
	  }	
	   return textList;
   }
   
   public HashMap<String,TextTemplate> populateTemplateObjects(String userid) throws SQLException
   {
	   HashMap<String,TextTemplate> templateList = new HashMap<String, TextTemplate>();
	   
	  DbConnection dbConn = null;
	  PreparedStatement prep = null;
	  ResultSet rs = null;
	  TextTemplate templateObj = null;
	  try {
	  	  dbConn = new DbConnection();
	   		
	  	  templateObj =  new TextTemplate(userid);
	  	  prep = dbConn.prepareStatement(TextTemplate.SAVE_TEXT_TEMPLATES_SELECT);
	   
	  	  prep.setString(1, userid);
	 	
	  	  rs = prep.executeQuery();
	  	  while (rs.next())
	  	  {
		   
	  	  	  String title = rs.getString(1);
	  	  	  String note = rs.getString(2);
	  	  	  templateObj =  new TextTemplate(userid,  title, note);
	  	  	  templateList.put(title, templateObj);
	  	  }
	  }
	  catch (SQLException e)
	  {
	  	  throw e;
	  }
	  finally {
	  	  DatabaseUtility.close(rs);
	  	  DatabaseUtility.close(prep);
	  	  dbConn.close();
	  }	
		
	   return templateList;
   }
   
   public void saveText(String userid,  String patientId, String title, String text) throws SQLException
   {
	  DbConnection dbConn = null;
	  PreparedStatement prep = null;
	  ResultSet rs = null;

	  try {
	  	  dbConn = new DbConnection();
	   	
	  	  prep = dbConn.prepareStatement(Text.SAVE_TEXT_SELECT_CNT);
	   
	  	  prep.setString(1, userid);
	  	  int patient_id = Integer.parseInt(patientId);
		  
	  	  prep.setInt(2, patient_id);
	  	  prep.setString(3, title);
	  	  int numberOfRecords = 0;
	   
	  	  rs = prep.executeQuery();
	  	  if (rs.next())
	  	  {
	  	  	  numberOfRecords = rs.getInt(1);
	  	  }
	   
	  	  if (numberOfRecords == 0)
	  	  	  insertText(userid, patientId, title, text);
	  	  else
	  	  	  updateText(userid, patientId, title, text);
	  }
	    catch (SQLException e)
	  {
	  	  throw e;
	  }
	  finally {
	  	  DatabaseUtility.close(rs);
	  	  DatabaseUtility.close(prep);
	  	  dbConn.close();
	  }
	  
   }
   
   
   private void insertText(String userid,  String patientId, String title, String text) throws SQLException
   {
	  DbConnection dbConn = null;
	  PreparedStatement prep = null;


	  try {
	  	  dbConn = new DbConnection();
	   
	  	  prep = dbConn.prepareStatement(Text.SAVE_TEXT_INSERT);
	  	  prep.setString(1, userid);
	  	  int patient_id = Integer.parseInt(patientId);
		
	  	  prep.setInt(2, patient_id);
	  	  prep.setString(3, title);
	  	  prep.setString(4, text);
	     
	  	  int noUpdated = prep.executeUpdate();
	  	  System.out.println("SaveData: InsertText: number updated  " + noUpdated );
	   }
	    catch (SQLException e)
	  {
	  	  throw e;
	  }
	  finally {

	  	  DatabaseUtility.close(prep);
	  	  dbConn.close();
	  }
   }
   
   private void updateText(String userid,  String patientId, String title, String text) throws SQLException
   {
	  DbConnection dbConn = null;
	  PreparedStatement prep = null;
	  ResultSet rs = null;

	  try {
	  	  dbConn = new DbConnection();
	  	  prep = dbConn.prepareStatement(Text.SAVE_TEXT_UPDATE);

	  	  prep.setString(1, text);
	  	  prep.setString(2, userid);
	  	  int patient_id = Integer.parseInt(patientId);
		
	  	  prep.setInt(3, patient_id);
	  	  prep.setString(4, title);
	      
	  	  int noUpdated = prep.executeUpdate();
	  	  System.out.println("SaveData: UpdateText: update: number updated  " + noUpdated );
	    }
	    catch (SQLException e)
	   {
	  	  throw e;
	   }
	  finally {
	  	  DatabaseUtility.close(rs);
	  	  DatabaseUtility.close(prep);
	  	  dbConn.close();
	  }
   }
   
   public void deleteText(String userid,  String patientId, String title, String text) throws SQLException
   {
	  DbConnection dbConn = null;
	  PreparedStatement prep = null;


	  try {
	  	  dbConn = new DbConnection();
	   
	  	  prep = dbConn.prepareStatement(Text.SAVE_TEXT_DELETE);

	  	  prep.setString(1, userid);
	  	  int patient_id = Integer.parseInt(patientId);
		
	  	  prep.setInt(2, patient_id);
	  	  prep.setString(3, title);
	      
	  	  int noUpdated = prep.executeUpdate();
	  	  System.out.println("SaveData: DeleteText: delete: number updated  " + noUpdated );
	   }
	    catch (SQLException e)
	   {
	  	  throw e;
	   }
	  finally {

	  	  DatabaseUtility.close(prep);
	  	  dbConn.close();
	  }
   }
   
   public static HashMap<String,String> getCSSFiles(File dir)
   {
	
	   FileFilter fileFilter = new FileFilter() {
	   	    public boolean accept(File file) {
	   	        return file.isDirectory();
	   	    }
	   	};
   	
	   	FilenameFilter filter = new FilenameFilter() {
	   	    public boolean accept(File dir, String name) {
	   	        return name.startsWith("jquery-ui-");
	   	    }
	   	};
	   	
	   	File[] subDirs = dir.listFiles(fileFilter);

	   	HashMap<String,String> cssFiles = new HashMap<String,String>();
	   	System.out.println("TextProcesses getCSSFiles no of files " + subDirs.length);
	   	if (subDirs == null) {
	   	    // Either dir does not exist or is not a directory
	   	} else {
   	    for (File subDir: subDirs)
   	    {
   	    	System.out.println("TextProcesses getCSSFiles subDir " + subDir.getName());
   		   	
   	        // Get filename of file or directory
   	    	String[] subFiles = subDir.list(filter);
   	    	System.out.println("TextProcesses getCSSFiles subDir # " + subFiles.length);
   		   	
   	    	for (String subFile: subFiles)
   	    	{
   	    		cssFiles.put(subDir.getName(), subDir.getName() + Constants.FILE_SEPARATOR  + subFile);
   	    	}
   	    	
   	       
   	    }
   	}

   	
   	for (String fileName: cssFiles.keySet())
   	{
   		System.out.println("TextProcesses getCSSFiles fileName " + fileName);
   	   	
   	}
   	
    return cssFiles;

   }
}

