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
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import com.sun.syndication.feed.WireFeed;
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
    
    private final static String ATOM_LINK_TYPE = "application/atom+xml";
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
    	List<String> results = new ArrayList<String>();
    	
    	List<SyndLinkImpl> foundEntries=  findPatient( firstName,  lastName,  type,  fileName);
    	List<SyndLinkImpl> returnEntries=  findHealthData(foundEntries, type);
    	List<String> urls = findHealthDetail(returnEntries, type);
    	StringBuffer strBuf = new StringBuffer();
    	
    	for (String url: urls)
    	{
    		results.add(getServerOutput(url));
    	}
    	return results;
    }
    
    public static List<SyndLinkImpl> findPatient(String firstName, String lastName, String type, String fileName)
    {
    	List<SyndLinkImpl> foundEntries =  new ArrayList<SyndLinkImpl>();
        
    	 try {
    		  
             SyndFeedInput input = new SyndFeedInput();
             fileName = Constants.CONFIG_DIR + fileName;
             System.out.println("GreenCDAFeedParser : FileName : " + fileName );
    		 
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
    
    public static List<SyndLinkImpl> findHealthData(List<SyndLinkImpl> foundEntries, String type)
    {
    	List<SyndLinkImpl> returnEntries = new ArrayList<SyndLinkImpl>();
    	try {
			
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
    public static void parseAtom(String url, String fileName)
    {
    	 try {
    		 System.out.println("GreenCDAFeedParser : Start: " );
    		  
    		 //URL feedUrl = new URL(url);
    		 
    		 //URL feedUrl = new URL("http://feeds.bbci.co.uk/news/scotland/rss.xml");
    		 //URLConnection urlCon = feedUrl.openConnection();
    		 //InputStream io = urlCon.getInputStream();
             
             SyndFeedInput input = new SyndFeedInput();
             fileName = Constants.CONFIG_DIR + fileName;
             System.out.println("GreenCDAFeedParser : FileName : " + fileName );
    		 
             XmlReader xmlRead = new XmlReader(new File(fileName));
            
             SyndFeed feed = input.build(xmlRead);
    
             List<SyndEntry> synEntries =  (List<SyndEntry>) feed.getEntries();
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
  
                 // Get the Contents
                 for (SyndContentImpl content : (List<SyndContentImpl>) entry.getContents()) {
                     System.out.println("Content: " + content.getValue());
                     
                 }
                 
                 // Get the Categories
                 for (SyndCategoryImpl category : (List<SyndCategoryImpl>) entry.getCategories()) {
                     System.out.println("Category: " + category.getName());
                 }
             }
         } catch (Exception ex) {
        	 ex.printStackTrace();
             System.out.println("Error: " + ex.getMessage());
         }
    }
}
