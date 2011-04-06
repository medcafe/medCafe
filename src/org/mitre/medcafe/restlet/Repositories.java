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
package org.mitre.medcafe.restlet;

import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import com.medsphere.ovid.model.domain.patient.*;
import java.util.*;
import java.util.logging.Logger;
import java.util.logging.Level;

import org.mitre.medcafe.util.*;
import java.net.*;
import java.lang.reflect.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Attr;
import org.w3c.dom.Text;
import java.io.*;
import java.text.ParseException;
/**
 *  Collection of all Repositories
 */
public class Repositories
{

    protected static Map<String, Repository> repos = new HashMap<String, Repository>();
    protected static final int TIMEOUT = 8000; // I recommend 3 seconds at least
	 protected static final String REPOSITORY_SETUP = "Repositories.xml";
	     public final static String KEY = Repositories.class.getName();
    public final static Logger log = Logger.getLogger( KEY );
    // static{log.setLevel(Level.FINER);}
    public Repositories()
    {

    }

    public static Map<String, Repository> getRepositories()
    {
        return Collections.unmodifiableMap(repos);
    }

    /**
     *  Repositories for testing - until an external representation is arrived at, anyway
     */
    public static void setDefaultRepositories() throws ParseException, ParserConfigurationException, SAXException, IOException
    {
    	 try
    	 {

		 File file = new File(Constants.CONFIG_DIR, REPOSITORY_SETUP);
		 Document repositoryDoc = XMLProcessing.createXMLDoc(file);
		 NodeList repositoryNodes = null;
		 Node repositoryNode = null;
		 Element repositoryElmnt = null;


	    	 repositoryNodes = repositoryDoc.getElementsByTagName(Repository.REPOSITORIES);
	    	 if (repositoryNodes.getLength() > 0)
	    	 {
	    		 repositoryNode = repositoryNodes.item(0);
	    	 }
	    	 else
	    	 {
	    		 throw new ParseException("Error on parsing xml document : could not find any nodes", 0);
	     	 }

	     	log.finer("Repositories setDefaultRepositories got repositories");

	     	if (repositoryNode.getNodeType() == Node.ELEMENT_NODE)
	    	{
	    	  repositoryElmnt = (Element) repositoryNode;
	     	}
	     	else
	    	{
	    		throw new ParseException("Error on parsing xml document : could not find any elements", 0);
			}
			repositoryNodes = repositoryElmnt.getElementsByTagName(Repository.REPOSITORY_ITEM);

			log.finer("Repositories setDefaultRepositories number of components " + repositoryNodes.getLength());
			repos = new HashMap<String, Repository>();
			Repository r = null;
	     	log.finer("Number of repositories: " + repositoryNodes.getLength());
	     	for (int i=0 ; i < repositoryNodes.getLength(); i++)
	     	{

    		 	Node node = repositoryNodes.item(i);
    		 	if (node.getNodeType() == Node.ELEMENT_NODE)
    		 	{
    		 		repositoryElmnt = (Element) node;
    		 		NodeList typeList = repositoryElmnt.getElementsByTagName(Repository.REPOSITORY_TYPE);
    		 		String reposType = "";
    		 		String reposName = "";
    		 		String host = "";
    		 		if (typeList.getLength() > 0)
    		 		{
    		 			reposType = typeList.item(0).getTextContent();
    		 			NodeList nameList = repositoryElmnt.getElementsByTagName(Repository.REPOSITORY_NAME);
    		 			if (nameList.getLength()>0)
    		 			{
    		 				reposName = nameList.item(0).getTextContent();
   		 				NodeList hostList = repositoryElmnt.getElementsByTagName(Repository.HOST);
	  		 				if (hostList.getLength()>0)
	  		 				{
	  		 					host = hostList.item(0).getTextContent();
	  		 					HashMap<String, String> credMap = new HashMap<String, String>();
	  		 					NodeList creds = repositoryElmnt.getElementsByTagName(Repository.CREDENTIALS);
	  		 					if (creds.getLength()>0)
	  		 					{
								Element credElement = (Element) creds.item(0);
	  		 					NodeList credList = credElement.getChildNodes(); //returns text nodes
	  		 					                                                 // only odd ones contain names
	  		 					                                                 // of elements
	  		 					for (int j=1; j<credList.getLength(); j=j+2)
	  		 					{
	  		 						Node credNode =  credList.item(j);
	  		 						
	  		 						String key = credNode.getNodeName();
	  		 						String value = credElement.getElementsByTagName(key).item(0).getTextContent();
	  		 						log.finer(j + ": " + key + ": " + value);
	  		 						credMap.put(key, value);
	  		 					}
	  		 					try{
								Class repClass = Class.forName(reposType);
								
						

			   				try
        						{
         	   				if( InetAddress.getByName(host).isReachable(TIMEOUT) )
         	   				{
         	   					Class[] argList = new Class[1];
							
										argList[0] = credMap.getClass();
								
										Constructor repConst = repClass.getDeclaredConstructor(argList);

										Object[] objList = new Object[1];
										objList[0] = credMap;
         	   					r = (Repository) repConst.newInstance(objList);
   									r.setName(reposName);
         	       				repos.put(r.getName(), r);
         	       				log.info("Got " + reposName + " connection");

         		  				}
        						}
        						
        						catch (InstantiationException instantE)
        						{
        							log.severe("Repositories: No default instantiation for " + reposType);
        							log.severe("Error message: " + instantE.getMessage());
        						}
        						catch (IllegalAccessException illegalE)
        						{
        							log.severe("Repositories: Instantiation failed for " + reposType);
        							log.severe("Illegal Access Error message: " + illegalE.getMessage());
        						}
        						catch (InvocationTargetException invokeE)
        						{
        							log.severe("Repositories: Instantiation failed for " + reposType);
        							log.severe("Invocation Target Error message: " + invokeE.getMessage());

        						}
        						catch (NoSuchMethodException noSuchE)
        						{
        							log.severe("Repositories: Instantiation failed for " + reposType);
        							log.severe("No Such Method Error message: " + noSuchE.getMessage());
        						}catch (Exception e) {}
        						}

        						catch (ClassNotFoundException clasE)
        						{
        							log.severe("Repositories: No class found for " + reposType);
        							log.severe("Error message: " + clasE.getMessage());
        						}

        						}
        						else
        						{
        							log.severe("No credentials specified for repository " + reposName);
        						}
		    				}
		    				else
		    				{
		    					log.severe("No host specified for repository " + reposName + ".");
		    				}
		    			}
		    			else
		    			{
		    				log.severe("No repository name specified.");
		    			}
		    		}
		    		else
		    		{
		    			log.severe("No repository type specified.");
		    		}
		    	}

		   	}
		   	}finally{}

    }

    public static void onShutdown()
    {
        for( Repository r : repos.values() )
        {
            r.onShutdown();
        }

    }


    public static List<String> getRepositoryNames()
    {
        return new ArrayList<String>(repos.keySet());
    }


    public static Repository getRepository(String name)
    {
        return repos.get(name);
    }
}
