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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex .*;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONException;
import org.json.JSONObject;
import org.mitre.medcafe.restlet.PatientListResource;
import org.mitre.medcafe.restlet.Repositories;
import org.mitre.medcafe.util.Constants;
import org.mitre.medcafe.util.DbConnection;
import org.mitre.medcafe.util.Text;
import org.mitre.medcafe.util.WebUtils;
import org.mitre.medcafe.util.XMLProcessing;
import org.restlet.ext.json.JsonRepresentation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *  Representation of the text data
 *  @author: Gail Hamilton
 */
public class MedCafeDataSource implements Comparable<MedCafeDataSource>
{
	public final static String KEY = MedCafeComponent.class.getName();
	public final static Logger log = Logger.getLogger( KEY );
	static{log.setLevel(Level.SEVERE);}

	//Parameters that are common to all MedCafe Files
	private String restlet ="";
	private String cacheKey ="";
	private int priority =0;
	private String type = "";
	
   public static final int HIGH = 0;
   public static final int MEDIUM = 1;
   public static final int LOW = 2;

	public static final String RESTLET = "restlet";
	public static final String CACHE_KEY = "cacheKey";
	public static final String PRIORITY = "priority";
	public static final String TYPE = "type";
	public static final String PATIENT_CACHE = "PatientCache";
	public static final String LOCAL = "Local";
	public static final String DATA_SOURCE = "DataSource";
	
	public MedCafeDataSource()	
	{
		super();
	
	}
	
	
	public static PriorityQueue<MedCafeDataSource> retrieveDataSource(String type) throws SQLException, ParseException, ParserConfigurationException, SAXException, IOException
	{
		PriorityQueue<MedCafeDataSource> sourceQueue = new PriorityQueue<MedCafeDataSource>();
		
		try 
		 {
			/*
			 * /*
			 * 	<name>Images</name>
				<type>Image</type>
				<image>coverflow.png</image>
				<server></server>
				<clickUrl>coverflow-flash/index.jsp</clickUrl>
				<method></method>
				<repository>OurVista</repository>
			 */
		     String config_dir = Constants.BASE_PATH + "WEB-INF/";
		     File file = new File(config_dir, "CacheDataSource.xml");
		     Document dataSourceDoc = XMLProcessing.createXMLDoc(file);
		     NodeList typeNodes = null;
		     Node typeNode = null;  
		     Element typeElmnt = null;
		     if (type.equals(MedCafeDataSource.PATIENT_CACHE))
		     {
		    	 typeNodes = dataSourceDoc.getElementsByTagName(MedCafeDataSource.PATIENT_CACHE);
		    	 if (typeNodes.getLength() > 0)
		    		 typeNode = typeNodes.item(0);
		    	 else
		    		 throw new ParseException("Error on parsing xml document : could not find any nodes", 0);
		     }
		     else if (type.equals(MedCafeDataSource.LOCAL))
		     {

		    	 typeNodes = dataSourceDoc.getElementsByTagName(MedCafeDataSource.LOCAL);
		    	 if (typeNodes.getLength() > 0)
		    		 typeNode = typeNodes.item(0);
		    	 else
		    		 throw new ParseException("Error on parsing xml document : could not find any nodes", 0);
		    	 
		     }
		     
		     log.finer("MedCafeDataSource retrieveDataSource got node type " + type);
		     
		     if (typeNode.getNodeType() == Node.ELEMENT_NODE) 
		    	  typeElmnt = (Element) typeNode;
		     else
		    	 throw new ParseException("Error on parsing xml document : could not find any elements", 0);
				
		     NodeList dataSourceNodes = typeElmnt.getElementsByTagName(MedCafeDataSource.DATA_SOURCE);
		     
		     log.finer("MedCafeDataSource retrieveDataSource number of sources " + dataSourceNodes.getLength());
		     
		     for (int i=0 ; i < dataSourceNodes.getLength(); i++)
		     {
		    		 	Node node = dataSourceNodes.item(i);
		    		 	if (node.getNodeType() == Node.ELEMENT_NODE) 
		    		 	{
		    		 		int priority = LOW;
		    		 		MedCafeDataSource dataSource = new MedCafeDataSource();
		    		 		
		    		 		Element dataSourceElmnt = (Element) node;
		    		 			    
		    		 		NodeList restletList = dataSourceElmnt.getElementsByTagName(RESTLET);
		    		 		
		    		 		String restlet = restletList.item(0).getTextContent();
		    		 		
		    		 		NodeList cacheKeyList = dataSourceElmnt.getElementsByTagName(CACHE_KEY);
		    		 		String cacheKey = cacheKeyList.item(0).getTextContent();
		    		 		
		    		 		NodeList priorityList = dataSourceElmnt.getElementsByTagName(PRIORITY);
		    		 		String priorityString  = priorityList.item(0).getTextContent();
		    		 		if (priorityString.equals("high"))
		    		 			priority = HIGH;
		    		 		else if (priorityString.equals("medium"))
		    		 			priority = MEDIUM;
		    		 		else if (priorityString.equals("low"))
		    		 			priority = LOW;
							dataSource.setRestlet(restlet);
							dataSource.setCacheKey(cacheKey);
							dataSource.setPriority(priority);
							dataSource.setType(type);
		    		 		sourceQueue.add(dataSource);
		    		 		
		    		 	}
		     }
		 } 
		 finally
		 {

			 
		 }
		 return sourceQueue;
		
	}


	public String getRestlet() {
		return restlet;
	}

	public void setRestlet(String restlet) {
		this.restlet = restlet;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCacheKey() {
		return cacheKey;
	}

	public void setCacheKey(String cacheKey) {
		this.cacheKey = cacheKey;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}


	public int compareTo(MedCafeDataSource o)
	{
		return this.priority - o.getPriority();
	}
}
