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
public class MedCafeComponent
{
	public final static String KEY = MedCafeComponent.class.getName();
	public final static Logger log = Logger.getLogger( KEY );
	static{log.setLevel(Level.FINER);}

	//Parameters that are common to all MedCafe Files
	private String name ="";
	private String order ="";
	private String type ="";
	private String image ="";
	private String server ="";
	private String clickUrl ="";
	private String method ="";
	private String repository ="";
	private String params ="";
	private String tempDir ="";
	private String template="";
	private String script="";
	private String scriptFile="";
	private boolean jsonProcess= false;
	
	public static final String NAME = "name";
	public static final String ORDER = "order";
	public static final String TYPE = "type";
	public static final String IMAGE = "image";
	public static final String SERVER = "server";
	public static final String CLICK_URL = "clickUrl";
	public static final String METHOD = "method";
	public static final String REPOSITORY = "repository";
	public static final String PARAMS = "params";
	public static final String PATIENT = "patientSpecific";
	public static final String GENERAL = "general";
	public static final String SCRIPT = "script";
	public static final String SCRIPT_FILE = "script_file";
	public static final String TEMPLATE = "template";
	public static final String JSON_PROCESS = "jsonProcess";
	
	public static final String XML_WIDGET = "medCafeWidget";
	
	public MedCafeComponent()	
	{
		super();
	
	}
	
	
	public static ArrayList<MedCafeComponent> retrieveComponents(String type, String tempDir) throws SQLException, ParseException, ParserConfigurationException, SAXException, IOException
	{
		ArrayList<MedCafeComponent> componentList = new ArrayList<MedCafeComponent>();
		
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
		     File file = new File(config_dir, "WidgetList.xml");
		     Document componentDoc = XMLProcessing.createXMLDoc(file);
		     NodeList typeNodes = null;
		     Node typeNode = null;  
		     Element typeElmnt = null;
		     if (type.equals(MedCafeComponent.PATIENT))
		     {
		    	 typeNodes = componentDoc.getElementsByTagName(MedCafeComponent.PATIENT);
		    	 if (typeNodes.getLength() > 0)
		    		 typeNode = typeNodes.item(0);
		    	 else
		    		 throw new ParseException("Error on parsing xml document : could not find any nodes", 0);
		     }
		     else
		     {

		    	 typeNodes = componentDoc.getElementsByTagName(MedCafeComponent.GENERAL);
		    	 if (typeNodes.getLength() > 0)
		    		 typeNode = typeNodes.item(0);
		    	 else
		    		 throw new ParseException("Error on parsing xml document : could not find any nodes", 0);
		    	 
		     }
		     
		     System.out.println("MedCafeComponent retrieveComponents got node type " + type);
		     
		     if (typeNode.getNodeType() == Node.ELEMENT_NODE) 
		    	  typeElmnt = (Element) typeNode;
		     else
		    	 throw new ParseException("Error on parsing xml document : could not find any elements", 0);
				
		     NodeList componentNodes = typeElmnt.getElementsByTagName(MedCafeComponent.XML_WIDGET);
		     
		     System.out.println("MedCafeComponent retrieveComponents number of components " + componentNodes.getLength());
		     
		     for (int i=0 ; i < componentNodes.getLength(); i++)
		     {
		    		 	Node node = componentNodes.item(i);
		    		 	if (node.getNodeType() == Node.ELEMENT_NODE) 
		    		 	{
		    		 		MedCafeComponent component = new MedCafeComponent();
		    		 		
		    		 		Element componentElmnt = (Element) node;
		    		 			    
		    		 		NodeList nameList = componentElmnt.getElementsByTagName(NAME);
		    		 		
		    		 		String name = nameList.item(0).getTextContent();
		    		 		
		    		 		NodeList typeList = componentElmnt.getElementsByTagName(TYPE);
		    		 		String componentType = typeList.item(0).getTextContent();
		    		 		
		    		 		NodeList imageList = componentElmnt.getElementsByTagName(IMAGE);
		    		 		String image = imageList.item(0).getTextContent();
		    		 		
		    		 		NodeList srvList = componentElmnt.getElementsByTagName(SERVER);
		    		 		String server = srvList.item(0).getTextContent();
		    		 		
		    		 		NodeList clickUrlList = componentElmnt.getElementsByTagName(CLICK_URL);
		    		 		String clickUrl = clickUrlList.item(0).getTextContent();
		    		 		
		    		 		NodeList methodList = componentElmnt.getElementsByTagName(METHOD);
		    		 		String method = methodList.item(0).getTextContent();
		    		 		
		    		 		NodeList repList = componentElmnt.getElementsByTagName(REPOSITORY);
		    		 		String repository = repList.item(0).getTextContent();
		    		 		
		    		 		NodeList scriptList = componentElmnt.getElementsByTagName(SCRIPT);
		    		 		String script = scriptList.item(0).getTextContent();
		    		 		
		    		 		NodeList scriptFileList = componentElmnt.getElementsByTagName(SCRIPT_FILE);
		    		 		String scriptFile = scriptFileList.item(0).getTextContent();
		    		 		
		    		 		NodeList jsonProcessList = componentElmnt.getElementsByTagName(JSON_PROCESS);
		    		 		boolean jsonProcess = Boolean.valueOf(jsonProcessList.item(0).getTextContent());
		    		 		
		    		 		String template = "";	    		 		
		    		 		NodeList templateList = componentElmnt.getElementsByTagName(TEMPLATE);
		    		 		if (templateList.getLength() > 0)
		    		 			template = templateList.item(0).getTextContent();
		    		 		
		    		 		
		    		 		NodeList paramsList = componentElmnt.getElementsByTagName(PARAMS);
		    		 		String params ="";
		    		 		if (paramsList.getLength() > 0)
		    		 			params = paramsList.item(0).getTextContent();
		    		 		
		    		 		component.setName(name);
		    		 		component.setType(componentType);
		    		 		component.setImage(image);
		    		 		component.setMethod(method);
		    		 		component.setRepository(repository);
		    		 		component.setClickUrl(clickUrl);
		    		 		component.setServer(server);
		    		 		component.setTempDir(tempDir);
		    		 		component.setParams(params);
		    		 		component.setScript(script);
		    		 		component.setScriptFile(scriptFile);
		    		 		component.setTemplate(template);
		    		 		component.setJsonProcess(jsonProcess);
		    		 		componentList.add(component);
		    		 		
		    		 	}
		     }
		 } 
		 finally
		 {
			 
			 
		 }
		 return componentList;
		
	}
	
	public static HashMap<String, MedCafeComponent> getComponentHash()
	{
		HashMap<String, MedCafeComponent> compHash = new HashMap<String, MedCafeComponent>();
	try{
		ArrayList<MedCafeComponent> compList = retrieveComponents(MedCafeComponent.PATIENT, "");
		for (MedCafeComponent comp : compList)
		{
			compHash.put(comp.getName(), comp);	
		}
		compList = retrieveComponents(MedCafeComponent.GENERAL, "");
		for (MedCafeComponent comp : compList)
		{
			compHash.put(comp.getName(), comp);
		}
		}
		catch (SQLException sqlE)
		{
			log.severe("Error getting widget information: " + sqlE.getMessage() );
		}
		catch (ParseException parseE)
		{
			log.severe("Error parsing widget information: " + parseE.getMessage());
		}
		catch (ParserConfigurationException parseConfE)
		{
			log.severe("Error with parser configuration when parsing widget information: " + parseConfE.getMessage());
		}
		catch (Exception e)
		{
			log.severe("Error while reading widget information: " + e.getMessage()); 
		}
		finally
		{
		}
		return compHash;
	
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getClickUrl() {
		return clickUrl;
	}

	public void setClickUrl(String clickUrl) {
		this.clickUrl = clickUrl;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getRepository() {
		return repository;
	}

	public void setRepository(String repository) {
		this.repository = repository;
	}

	public static String getCLICK_URL() {
		return CLICK_URL;
	}
	public boolean getJsonProcess() {
		return jsonProcess;
	}
	
	public void setJsonProcess(boolean jsonProcess) {
		this.jsonProcess = jsonProcess;
	}

	public JSONObject toJSON() throws JSONException 
	{
		 JSONObject jsonObj = new JSONObject ();
		 jsonObj.put("id", 1);
		 jsonObj.put(MedCafeComponent.NAME, name);
		 jsonObj.put(MedCafeComponent.IMAGE, tempDir + image);
		 jsonObj.put(MedCafeComponent.CLICK_URL, clickUrl);
		 jsonObj.put(MedCafeComponent.SERVER, server);
		 jsonObj.put(MedCafeComponent.METHOD, method);
		 jsonObj.put(MedCafeComponent.TYPE, type);
		 jsonObj.put(MedCafeComponent.REPOSITORY, repository);
		 jsonObj.put(MedCafeComponent.PARAMS, params);
		 jsonObj.put(MedCafeComponent.SCRIPT, script);
		 jsonObj.put(MedCafeComponent.SCRIPT_FILE, scriptFile);
		 jsonObj.put(MedCafeComponent.TEMPLATE, template);
   	 jsonObj.put(MedCafeComponent.JSON_PROCESS, String.valueOf(jsonProcess));
    	 return jsonObj;
	}

	public String getTempDir() {
		return tempDir;
	}

	public void setTempDir(String tempDir) {
		this.tempDir = tempDir;
	}


	public String getParams() {
		return params;
	}


	public void setParams(String params) {
		this.params = params;
	}
	
	public String getScript() {
		return script;
	}
	
	public void setScript(String script) {
		this.script = script;
	}
	
	public String getScriptFile() {
		return scriptFile;
	}
	
	public void setScriptFile(String scriptFile) {
		this.scriptFile = scriptFile;
	}
	
	public String getTemplate() {
		return template;
	}
	
	public void setTemplate(String template) {
		this.template = template;
	}
	
}
