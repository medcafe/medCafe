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
import java.util.List;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import com.sun.syndication.feed.synd.SyndCategoryImpl;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndLinkImpl;
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
    public static void parseAtom(String url)
    {
    	 try {
    		 System.out.println("GreenCDAFeedParser : Start: " );
    		  
    		 URL feedUrl = new URL(url);
    		 // URL feedUrl = new URL("http://feeds.bbci.co.uk/news/scotland/rss.xml");
     		
             SyndFeedInput input = new SyndFeedInput();
             SyndFeed feed = input.build(new XmlReader(feedUrl));
  
            
             List<SyndEntry> synEntries =  (List<SyndEntry>) feed.getEntries();
             // Get the entry items...
             for (SyndEntry entry : synEntries) {              
                 System.out.println("Title: " + entry.getTitle());
                 System.out.println("Unique Identifier: " + entry.getUri());
                 System.out.println("Updated Date: " + entry.getUpdatedDate());
  
                 // Get the Links
                 List<SyndLinkImpl> links = (List<SyndLinkImpl>) entry.getLinks();
				for (SyndLinkImpl link : links) {
                     System.out.println("Link: " + link.getHref());
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
