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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import junit.framework.Assert;

import org.hl7.greencda.c32.Code;
import org.hl7.greencda.c32.HealthObject;
import org.hl7.greencda.c32.Medication;
import org.mitre.medcafe.repositories.GreenCDARepository;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.syndication.feed.synd.SyndCategoryImpl;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndLinkImpl;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
 

/**
 *  Parser to read hData Atom Feed
 *  @author: Gail Hamilton
 */
public class GreenCDAFeedParser
{

    public final static String KEY = GreenCDAFeedParser.class.getName();
    public final static Logger log = Logger.getLogger( KEY );
    // static{log.setLevel(Level.FINER);}
    
    public final static String ATOM_LINK_TYPE = "application/atom+xml";
    public final static String JSON_LINK_TYPE ="application/json";
    public static void parseDom(String url)
    {
    	try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new URL(url).openStream());
	         
	        
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    
    public static List<String> findPatientDetails(String firstName, String lastName, String type, String fileName)
    {
    	List<String> urls = new ArrayList<String>();
    	List<String> results = new ArrayList<String>();
    	GreenCDARepository gcda = new GreenCDARepository("http://127.0.0.1:3000/");
    	List<SyndLinkImpl> foundEntries=  findPatient( firstName,  lastName,  type,  fileName);
    	if (foundEntries.size() > 0)
    	{
    		List<SyndLinkImpl> returnEntries=  findHealthData(foundEntries, type);
    		if (returnEntries.size() > 0)
    		{
    			urls = findHealthDetail(returnEntries, type);
    		
		    	for (String url: urls)
		    	{
		    		results.add(url);
		    		//testGetMedications(url);
		    		//results.add(getServerOutput(url));
		    	}
    		}
    	}
    	return results;
    }
    
    public static List<String> findPatientDetails(String firstName, String lastName, String type, String feedUrl, boolean net)
    {
    	List<String> urls = new ArrayList<String>();
    	List<String> results = new ArrayList<String>();
    	GreenCDARepository gcda = new GreenCDARepository("http://127.0.0.1:3000/");
    	List<SyndLinkImpl> foundEntries=  findPatient( firstName,  lastName,  type,  feedUrl, net);
    	if (foundEntries.size() > 0)
    	{
    		List<SyndLinkImpl> returnEntries=  findHealthData(foundEntries, type, net);
    		if (returnEntries.size() > 0)
    		{
    			urls = findHealthDetail(returnEntries, type, net);
    		
		    	for (String url: urls)
		    	{
		    		results.add(url);
		    		//testGetMedications(url);
		    		//results.add(getServerOutput(url));
		    	}
    		}
    	}
    	return results;
    }
    
    public static List<SyndLinkImpl> findPatient(String firstName, String lastName, String type, String fileName)
    {
    	List<SyndLinkImpl> foundEntries =  new ArrayList<SyndLinkImpl>();
        
    	 try {
    		  
             SyndFeedInput input = new SyndFeedInput();
           //  fileName = Constants.CONFIG_DIR + fileName;
             System.out.println("GreenCDAFeedParser : FileName is : " + fileName );
    		 
             XmlReader xmlRead = new XmlReader(new File(fileName));
            
             SyndFeed feed = input.build(xmlRead);
    
             List<SyndEntry> synEntries =  (List<SyndEntry>) feed.getEntries();
             
             // Get the entry items...
             for (SyndEntry entry : synEntries) { 
            	 
            	 if ( entry.getTitle().contains(firstName)   && 
            			 entry.getTitle().contains(lastName) )
	                {
            		 	List<SyndLinkImpl> links = (List<SyndLinkImpl>) entry.getLinks();
	            	
		 				for (SyndLinkImpl link : links) 
		 				{
		                      System.out.println("Link: " + link.getHref() + " Type: " + link.getType());
		                      if (link.getType().equals(ATOM_LINK_TYPE))
		                      {
					               foundEntries.add(link);

		                      }
		 				}
	                }
             }
             
            
         } catch (Exception ex) {
        	 ex.printStackTrace();
             System.out.println("Error: " + ex.getMessage());
         }
    	 finally
    	 {
    		
    	 }
    	 
    	 return  foundEntries;
         
    }
    
    
    
    public static  List<SyndLinkImpl>  findPatient(String firstName, String lastName, String type, String url, boolean net)
    {
    	List<SyndEntry> synEntries = null;
    	List<SyndLinkImpl> foundEntries =  new ArrayList<SyndLinkImpl>();
    	try
    	{
    		 synEntries = parseAtom(url);
    	
    		 for (SyndEntry entry : synEntries) { 
            	 
            	 if ( entry.getTitle().contains(firstName)   && 
            			 entry.getTitle().contains(lastName) )
	                {
            		 	List<SyndLinkImpl> links = (List<SyndLinkImpl>) entry.getLinks();
	            	
		 				for (SyndLinkImpl link : links) 
		 				{
		                      System.out.println("Link: " + link.getHref() + " Type: " + link.getType());
		                      if (link.getType().equals(ATOM_LINK_TYPE))
		                      {
					               foundEntries.add(link);

		                      }
		 				}
	                }
             }
             
         } catch (Exception ex) {
        	 ex.printStackTrace();
             System.out.println("Error: " + ex.getMessage());
         }
    	 finally
    	 {
    		
    	 }
    	 
    	 return  foundEntries;
         
    }
    
    public  List<SyndLinkImpl>  findPatientLinks(SyndEntry synEntry)
    {
    	List<SyndLinkImpl> foundEntries =  new ArrayList<SyndLinkImpl>();
    	try
    	{ 
            	List<SyndLinkImpl> links = (List<SyndLinkImpl>) synEntry.getLinks();
	            	
		 		for (SyndLinkImpl link : links) 
		 		{
		             System.out.println("Link: " + link.getHref() + " Type: " + link.getType());
		             if (link.getType().equals(ATOM_LINK_TYPE))
		             {
					      foundEntries.add(link);

		             }
		 		}
             
         } catch (Exception ex) {
        	 ex.printStackTrace();
             System.out.println("Error: " + ex.getMessage());
         }
    	 finally
    	 {
    		
    	 }
    	 
    	 return  foundEntries;
         
    }
    
    public  List<SyndEntry>  findPatientEntries(String firstName, String lastName, String url)
    {
    	List<SyndEntry> synEntries = null;
    	try
    	{
    		 synEntries = parseAtom(url);
    	
    		 for (SyndEntry entry : synEntries) { 
            	 
            	 if ( entry.getTitle().contains(firstName)   && 
            			 entry.getTitle().contains(lastName) )
	                {
            		 	synEntries.add(entry);
	                }
             }
             
         } catch (Exception ex) {
        	 ex.printStackTrace();
             System.out.println("Error: " + ex.getMessage());
         }
    	 finally
    	 {
    		
    	 }
    	 
    	 return  synEntries;
         
    }
    
    public  List<SyndEntry>  findAllPatientEntries( String url)
    {
    	List<SyndEntry> synEntries = null;
    	try
    	{
    		 synEntries = parseAtom(url);
             
         } catch (Exception ex) {
        	 ex.printStackTrace();
             System.out.println("Error: " + ex.getMessage());
         }
    	 finally
    	 {
    		
    	 }
    	 
    	 return  synEntries;
         
    }
    public static List<SyndLinkImpl> findHealthData(List<SyndLinkImpl> foundEntries, String type)
    {
    	List<SyndLinkImpl> returnEntries = new ArrayList<SyndLinkImpl>();
    	try {
    		System.out.println("GreenCDAFeedParser : findHealthData " + foundEntries.size());
    				
	    	 SyndFeedInput input = new SyndFeedInput();
	    	 SyndFeed feed = null;     	 
	    	 for (SyndLinkImpl foundLink: foundEntries)
	         {
	        	 //URL feedUrl = new URL(foundLink.getHref());
				
	        	 String patientFileName = "TestPatientAtom.xml";
	        	 XmlReader xmlReadPatient = new XmlReader(new File( Constants.CONFIG_DIR +  patientFileName));
	        	 //XmlReader xmlReadPatient = new XmlReader(feedUrl);
	        	 feed = input.build(xmlReadPatient);
	        	 List<SyndEntry> patientEntries =  (List<SyndEntry>) feed.getEntries();
	        	 {
	        		 for (SyndEntry entry : patientEntries) 
	        		 {     
	        			 if (entry.getTitle() == null)
	         			 	continue;
	         			
	            		 if ( entry.getTitle().contains(type)   )
	    	             {
	            			
	            			 	List<SyndLinkImpl> links = (List<SyndLinkImpl>) entry.getLinks();
	            			 	for (SyndLinkImpl link : links) 
	    		 				{
		            			 	if (link.getType().equals(ATOM_LINK_TYPE))
				                      {
							               returnEntries.add(link);
	
				                      }
	    		 				}
	    	              }
	        		 }
	        	 }
	         }
	    	 return returnEntries;
	    	 
    	} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FeedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnEntries;
    }
    
    public static List<SyndLinkImpl> findHealthData(List<SyndLinkImpl> foundEntries, String type, boolean net)
    {
    	List<SyndLinkImpl> returnEntries = new ArrayList<SyndLinkImpl>();
    	try {
    		System.out.println("GreenCDAFeedParser : findHealthData size " + foundEntries.size());
    				
	    	 SyndFeedInput input = new SyndFeedInput();
	    	 SyndFeed feed = null;     	 
	    	 for (SyndLinkImpl foundLink: foundEntries)
	         {
	        	 //URL feedUrl = new URL(foundLink.getHref());
				
	        	 System.out.println("GreenCDAParser findHealthData feedUrl " + foundLink.getHref());
	        	 List<SyndEntry> patientEntries =  parseAtom(foundLink.getHref());
	        	 {
	        		 
	        		 for (SyndEntry entry : patientEntries) 
	        		 {     
	        			 System.out.println("GreenCDAParser findHealthData patient entry " + entry.getTitle());
	        			 
	        			 if (entry.getTitle() == null)
	         			 	continue;
	         			
	        			 System.out.println("GreenCDAParser findHealthData type " + type);
	        			 
	        			 List<SyndLinkImpl> links1 = (List<SyndLinkImpl>) entry.getLinks();
         			 	for (SyndLinkImpl link : links1) 
 		 				{
         			 		System.out.println("GreenCDAParser findHealthData link " + link.getHref());
   	        			 
 		 				}
	            		 if ( entry.getTitle().toLowerCase().contains(type.toLowerCase())   )
	    	             {
	            			
	            			 	List<SyndLinkImpl> links = (List<SyndLinkImpl>) entry.getLinks();
	            			 	for (SyndLinkImpl link : links) 
	    		 				{
		            			 	if (link.getType().equals(ATOM_LINK_TYPE))
				                      {
							               returnEntries.add(link);
	
				                      }
		            			 	else if (link.getType().equals(JSON_LINK_TYPE))
		            			 	{ 
		            			 		returnEntries.add(link);
		            			 	}
	    		 				}
	    	              }
	        		 }
	        	 }
	         }
	    	 return returnEntries;
	    	 
    	}  catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return returnEntries;
    }
    public static List<String> findHealthDetail(List<SyndLinkImpl> foundEntries, String type)
    {
    	List<String> returnEntries = new ArrayList<String>();
    	try 
    	{
			
	    	 SyndFeedInput input = new SyndFeedInput();
	    	 SyndFeed feed = null;     	 
	    	 for (SyndLinkImpl foundLink: foundEntries)
	         {
	        	 //URL feedUrl = new URL(foundLink.getHref());
				
	        	 String healthFileName = "TestMedsAtom.xml";
	        	 XmlReader xmlReadPatient = new XmlReader(new File( Constants.CONFIG_DIR +  healthFileName));
	        	 feed = input.build(xmlReadPatient);
	        	 List<SyndEntry> healthEntries =  (List<SyndEntry>) feed.getEntries();
	        	 {
	        		 for (SyndEntry entry : healthEntries) 
	        		 { 
	        			 returnEntries.add(entry.getLink());
	        		 }
	        	 }
	         }
	    	 
	    } catch (MalformedURLException e) {
	 			// TODO Auto-generated catch block
	 			e.printStackTrace();
	    } catch (IOException e) {
	 			// TODO Auto-generated catch block
	 			e.printStackTrace();
	 	} catch (IllegalArgumentException e) {
	 			// TODO Auto-generated catch block
	 			e.printStackTrace();
	 	} catch (FeedException e) {
	 			// TODO Auto-generated catch block
	 			e.printStackTrace();
	 	}
	 	return returnEntries;
	    	 
    }
    
    public static List<String> findHealthDetail(List<SyndLinkImpl> foundEntries, String type, boolean net)
    {
    	List<String> returnEntries = new ArrayList<String>();
    	try 
    	{
			
	    	 SyndFeedInput input = new SyndFeedInput();
	    	 SyndFeed feed = null;  
	    	 List<SyndEntry> healthEntries = new ArrayList<SyndEntry>();
	    	 for (SyndLinkImpl foundLink: foundEntries)
	         {
	        	 //URL feedUrl = new URL(foundLink.getHref());
				
	        	 if (foundLink.getType().equals(ATOM_LINK_TYPE))
	        	 {
	        		 healthEntries.addAll(parseAtom(foundLink.getHref()) );
	        		 
	        	 }
	        	 else if (foundLink.getType().equals(JSON_LINK_TYPE) )
	        	 {
	        		 returnEntries.add(foundLink.getHref());
	        	 }
	        	 
	        	 for (SyndEntry entry : healthEntries) 
        		 { 
        			 returnEntries.add(entry.getLink());
        		 }
	         }
	    	 
	    } catch (IllegalArgumentException e) {
	 			// TODO Auto-generated catch block
	 			e.printStackTrace();
	 	} 
	 	return returnEntries;
	    	 
    }
    
    public static List<String> findHealthDetail(String url, String patientId, String type)
    {
    	List<String> returnEntries = new ArrayList<String>();
    	//href="/records/1/medications/
    	String heathDataUrl = url + "/records/" + patientId + "/" + type;
    	try 
    	{
			
	    	 SyndFeedInput input = new SyndFeedInput();
	    	 SyndFeed feed = null;  
	    	 List<SyndEntry> patientEntries = parseAtom(heathDataUrl);
	    			
	    	 List<SyndEntry> healthEntries =   new ArrayList<SyndEntry>();
        	 for (SyndEntry foundEntry : patientEntries)
        	 { 
        		System.out.println("GreenCDAParser findHealthDetail title " + foundEntry.getTitle());
	        			
	        	 //URL feedUrl = new URL(foundLink.getHref());
        		 List<SyndLinkImpl> links = (List<SyndLinkImpl>) foundEntry.getLinks();
        		 for (SyndLinkImpl foundLink : links)
            	 {
        			 System.out.println("GreenCDAParser findHealthDetail found link " + foundLink.getHref());
     	        	
		        	 if (foundLink.getType().equals(ATOM_LINK_TYPE))
		        	 {
		        		 healthEntries.addAll(parseAtom(foundLink.getHref()) );       		 
		        	 }
		        	 else if (foundLink.getType().equals(JSON_LINK_TYPE) )
		        	 {
		        		 returnEntries.add(foundLink.getHref());
		        	 }
		        	 
		        	 for (SyndEntry entry : healthEntries) 
	        		 { 
	        			 returnEntries.add(entry.getLink());
	        		 }
            	 }
	         }
	    	 
	    } catch (IllegalArgumentException e) {
	 			// TODO Auto-generated catch block
	 			e.printStackTrace();
	 	} 
	 	return returnEntries;
	    	 
    }
    private static List<Medication> testGetMedications(String url) {
		
    	String server = "http://1.1.22.110:3000" ;
    	
    	server = server + url;
		List<Medication> meds = new ArrayList<Medication>();
		
		try {
			String results = WebUtils.callServer(server, "GET", "application/json", new String[]{});
			Gson gson = new Gson();
			JsonParser parser = new JsonParser();
			JsonObject o = parser.parse(results).getAsJsonObject();
			Medication med = gson.fromJson(o,  Medication.class);
			HealthObject ho = gson.fromJson(o, HealthObject.class);
			String testJsonHo = gson.toJson(ho);
	           
			   
			System.out.println("GreenCDARepository Health Object " + testJsonHo );
			meds.add(med);
		
            Medication testMed = new Medication();
            testMed.setId("TestMed");
            testMed.setStatus("Active");
            Code delivMeth = new Code();
            delivMeth.setCode("By Mouth");
            testMed.setDeliveryMethod(delivMeth);
            
            Code code = new Code();
            code.setCode("Oral");
            testMed.setRoute(code);
            testMed.setFreeText("Free Text Value");
            
            String testJson = gson.toJson(testMed);
            Medication returnMed = gson.fromJson(testJson, Medication.class);
            meds.add(returnMed);
            
		/*} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			*/
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return meds;

	}
    public static String getServerOutput(String url)
    {
    	String server = "http://1.1.22.110:3000" ;
    	String tempResults = null;
    	
    	server = server + url;
		System.out.println("Get results from server " + server);
    	try {
			tempResults = WebUtils.callServer(server, "GET", "application/json", new String[]{});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tempResults;
		
    }
    public static List<SyndEntry>  parseAtom(String url)
    {
    	 List<SyndEntry> synEntries = null;
    	 try {
    		 System.out.println("GreenCDAFeedParser : url " + url );
    		 
             URL data_server = new URL(url);
             
             HttpURLConnection connection = (HttpURLConnection)data_server.openConnection();
             connection.setRequestProperty("Accept","*/*");
             connection.setRequestMethod("GET");
             connection.connect();
                          
             SyndFeedInput input = new SyndFeedInput();
             SyndFeed feed = input.build(
            		 new InputStreamReader(
            				 connection.getInputStream()));
    
             synEntries =  (List<SyndEntry>) feed.getEntries();
             System.out.println("GreenCDAParser parseAtom feedUrl size " + synEntries.size());
	        	
             // Get the entry items...
             for (SyndEntry entry : synEntries) {              
                 System.out.println("Title: " + entry.getTitle());
                 System.out.println("Unique Identifier: " + entry.getUri());
                 System.out.println("Updated Date: " + entry.getUpdatedDate());
  
                 // Get the Links
                 List<SyndLinkImpl> links = (List<SyndLinkImpl>) entry.getLinks();
				for (SyndLinkImpl link : links) {
                     System.out.println("Link: " + link.getHref() + " Type: " + link.getType());
                     if (link.getType().equals(ATOM_LINK_TYPE))
                     {
                    	 System.out.println("Link: " + link.getHref() + " Type: " + link.getType() + " is the one we want");
                     }
                 }            
				System.out.println("GreenCDAParser parseAtom now in here ");
		        
                
                 return synEntries;
             }
         } catch (Exception ex) {
        	 ex.printStackTrace();
             System.out.println("Error: " + ex.getMessage());
         }
		return synEntries;
    }
}
