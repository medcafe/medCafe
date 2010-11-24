package org.mitre.medcafe.restlet;

import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import com.medsphere.ovid.model.domain.patient.*;
import java.util.*;
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
		     		     
	     	System.out.println("Repositories setDefaultRepositories got repositories");
	     
	     	if (repositoryNode.getNodeType() == Node.ELEMENT_NODE) 
	    	{
	    	  repositoryElmnt = (Element) repositoryNode;
	     	}
	     	else
	    	{
	    		throw new ParseException("Error on parsing xml document : could not find any elements", 0);
			}
			repositoryNodes = repositoryElmnt.getElementsByTagName(Repository.REPOSITORY_ITEM);
		     
			System.out.println("Repositories setDefaultRepositories number of components " + repositoryNodes.getLength());
			repos = new HashMap<String, Repository>();
			Repository r = null;
	     	System.out.println("Number of repositories: " + repositoryNodes.getLength());
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
	  		 						//System.out.println(credNode.getData());
	  		 						String key = credNode.getNodeName();
	  		 						String value = credElement.getElementsByTagName(key).item(0).getTextContent();
	  		 						System.out.println(j + ": " + key + ": " + value);
	  		 						credMap.put(key, value);
	  		 					}
	  		 					try{
								Class repClass = Class.forName(reposType);
   							r = (Repository) repClass.newInstance();
   							r.setName(reposName);
   							r.setCredentials(credMap);
			   				try
        						{
         	   				if( InetAddress.getByName(host).isReachable(TIMEOUT) )
         	   				{
         	       				repos.put(r.getName(), r);
         	       				System.out.println("Got " + reposName + " connection");
              
         		  				}
        						}catch (Exception e) {}
        						}
        						catch (ClassNotFoundException clasE)
        						{
        							System.out.println("Repositories: No class found for " + reposType);
        							System.out.println("Error message: " + clasE.getMessage());
        						}
        						catch (InstantiationException instantE)
        						{
        							System.out.println("Repositories: No default instantiation for " + reposType);
        							System.out.println("Error message: " + instantE.getMessage());
        						}
        						catch (IllegalAccessException illegalE)
        						{
        							System.out.println("Repositories: Instantiation failed for " + reposType);
        							System.out.println("Error message: " + illegalE.getMessage());
        						}
        						}
        						else
        						{
        							System.out.println("No credentials specified for repository " + reposName);
        						}
		    				}
		    				else
		    				{
		    					System.out.println("No host specified for repository " + reposName + ".");
		    				}
		    			}
		    			else
		    			{
		    				System.out.println("No repository name specified.");
		    			}
		    		}
		    		else
		    		{
		    			System.out.println("No repository type specified.");
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
