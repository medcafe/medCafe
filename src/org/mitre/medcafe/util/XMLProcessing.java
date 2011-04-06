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

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;
import java.util.logging.Level;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/**
 *  Description of the Class
 *
 *@author     ghamilton
 *@created    Jan 20th , 2005
 */

public class XMLProcessing
{
    public final static String KEY = XMLProcessing.class.getName();
    public final static Logger log = Logger.getLogger( KEY );
    // static{log.setLevel(Level.FINER);}

    /**
     *  Constructor for the AllTablesListTag object
     */
    public XMLProcessing()
    { }


    public static String getNodeValue(Document doc, String nodeName)
    {

    	Node node = doc.getFirstChild();
    	node = getNode( node, nodeName);
    	String registrationid = node.getTextContent();
    	return registrationid;
    }

    private static Node getNode(Node root, String name)
    {
    	NodeList ns = root.getChildNodes();
    	for (int i=0; i < ns.getLength(); i++)
    	{
    		Node child = ns.item(i);
    		if (child.getNodeName().equals(name))
    		{
    			return child;
    		}
    		else
    		{
    			child = getNode(child, name);
    			if (child.getNodeName().equals(name))
        		{
        			return child;
        		}
    		}
    	}
    	return root;
    }

    public static Document createXMLDoc(File file) throws ParserConfigurationException, SAXException, IOException
    {

    	  DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    	  DocumentBuilder db = dbf.newDocumentBuilder();
    	  Document doc = db.parse(file);
    	  doc.getDocumentElement().normalize();

    	  return doc;
    }

    public static Document createXMLDoc(String response)
    {
    	log.finer("XMLProcessing about to process the following " + response);

    	InputStream is = new java.io.ByteArrayInputStream(response.getBytes());
    	javax.xml.parsers.DocumentBuilderFactory factory =
            javax.xml.parsers.DocumentBuilderFactory.newInstance();

    	factory.setNamespaceAware(true);
        javax.xml.parsers.DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        }
        catch (javax.xml.parsers.ParserConfigurationException ex) {
        }
        Document doc=null;
		try {
			doc = builder.parse(is);
		    is.close();

		}
		catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return doc;
    }
}
