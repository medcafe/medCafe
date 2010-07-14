/* Copyright 2010 The MITRE Corporation.  ALL RIGHTS RESERVED. */
package org.mitre.medcafe.model;

import java.sql.*;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.*;
import org.mitre.medcafe.restlet.*;
import org.mitre.medcafe.util.*;
import org.restlet.*;
import org.restlet.data.*;
import org.restlet.representation.*;
import org.restlet.resource.*;

public class PatientCache extends TimerTask
{

    public final static String KEY = PatientCache.class.getName();
    public final static Logger log = Logger.getLogger( KEY );
    static{log.setLevel(Level.FINER);}

    //{{{ Members
	protected JSONObject medicalHistory = null;
	protected JSONObject familyHistory = null;
	protected JSONObject medicineList = null;
	protected JSONObject alertList = null;
	protected JSONObject images = null;
	protected String databasePatientId = null;
	protected javax.servlet.ServletContext application = null;
	public final static String NA = "Resource not available";
	protected JSONObject repositories = null;
	//protected String repoPatientId = null;

	protected String firstName = null;
	protected String lastName = null;
    //}}}

    //{{{ Constuctors
    public PatientCache(String databasePatientId, javax.servlet.ServletContext application)
    {
        this.databasePatientId = databasePatientId;
        this.application = application;
        loadLocalInfo();
    }
    //}}}

    //{{{ Methods

    public void loadLocalInfo()
    {
        //get repository/local IDs
        String query = "select * from patient where id=?";
        //String query = "select a.rep_patient_id, p.first_name, p.last_name, a.repository from patient p join patient_repository_assoc a on p.id=a.patient_id where p.id=?";
        DbConnection conn = null;
        PreparedStatement ps = null;
        
        ResultSet rs = null;
        try
        {
            conn = new DbConnection();
            loadRepositoryInfo(conn);
            
            rs = conn.psExecuteQuery(query, "Looking up repository info",  new Integer(databasePatientId));
            while(rs.next())
            {
                firstName = rs.getString("first_name");
                lastName = rs.getString("last_name");
            }
            
        }
        catch(SQLException e)
        {
            log.throwing( KEY, "Error looking up patient repository details", e);
            throw new RuntimeException(e);
        }
        finally
        {
        	//If this is closed - causes issues later
            //DatabaseUtility.close(rs);
            //conn.close();
        }
    }

    public void loadRepositoryInfo(DbConnection conn)
    {
        //get repository/local IDs
        //String query = "select * from patient where id=?";
        Patient patient = new Patient(conn);
        repositories = patient.listRepositories(databasePatientId);
        //System.out.println("PatientCache loadRepositoryInfo JSONObject " + repositories.toString());
	     
    }
    
    public void run()
    {
        log.entering(KEY, "run()");

        //get access point
        MedcafeApplication app = (MedcafeApplication)application.getAttribute("org.restlet.ext.servlet.ServerServlet.application");
        if( app == null )
        {
            log.severe("Could not connect to data restlets.");
            return;
        }
        //define and send request
        String results="";
        //{"repositories":[{"id":2,"repository":"OurVista"},{"id":2,"repository":"local"}]}
       
        JSONArray reps = new JSONArray();
		try 
		{
			System.out.println("PatientCache run repository JSONObject " + repositories.toString());
		    
			reps = repositories.getJSONArray("repositories");
		}
		catch (JSONException e1) {
				// TODO Auto-generated catch block
				log.throwing(KEY, "constructor", e1);
				repositories = WebUtils.buildErrorJson( "Problem retrieving list of repositories for patient." + e1.getMessage());			    
		 }
			
		 if (repositories.has("repositories"))
		 {
        	try {
        		
	        	medicineList = new JSONObject();
	        	for (int i=0; i < reps.length(); i++ )
	        	{
	        		JSONObject repObj = (JSONObject) reps.get(i);
	        		String repository = repObj.getString("repository");
	        		String repoPatientId = repObj.getString("id");
	        		
		            results = getJsonContent( app, "/repositories/" + repository + "/patients/" + repoPatientId + "/medications" );
		            JSONObject meds = new JSONObject(results);
		      	  	meds.put("repository", repository);
		            medicineList.append("medicines", meds);
	            }
	        	//System.out.println("PatientCache generating medicine list line 141 JSONObject " + medicineList.toString());
	        }
	        catch(JSONException e)
	        {
	            log.throwing(KEY, "constructor", e);
	            medicineList = WebUtils.buildErrorJson( "Problem retrieving medication list from source." + e.getMessage());
	        }
        
	        log.finer("Done retrieving medication list");
	        try {
	            alertList = new JSONObject();
	            for (int i=0; i < reps.length(); i++ )
	        	{
	        		JSONObject repObj = (JSONObject) reps.get(i);
	        		String repository = repObj.getString("repository");
	        		String repoPatientId = repObj.getString("id");
	        		
		            results = getJsonContent( app, "/repositories/" + repository + "/patients/" + repoPatientId + "/allergies" );
		            
		            JSONObject alerts = new JSONObject(results);
		            alerts.put("repository", repository);
		      	  	alertList.append("alerts", alerts);
	            }
	        	//System.out.println("PatientCache generating alert/allergy list line 175 JSONObject " + alertList.toString());
	   
	        }
	        catch(JSONException e)
	        {
	            log.throwing(KEY, "constructor", e);
	            alertList = WebUtils.buildErrorJson( "Problem retrieving alert list from source." + e.getMessage());
	        }
	        log.finer("Done retrieving alert list");
	        try {
	            images = new JSONObject();
	            for (int i=0; i < reps.length(); i++ )
	        	{
	        		JSONObject repObj = (JSONObject) reps.get(i);
	        		String repository = repObj.getString("repository");
	        		String repoPatientId = repObj.getString("id");
	        		
	        		results = getJsonContent( app, "/repositories/" + repository + "/patients/" + repoPatientId + "/images" );
		            
		            JSONObject imageObj = new JSONObject(results);
		            imageObj.put("repository", repository);
		      	  	images.append("imageList", imageObj);
	            }
	        	//System.out.println("PatientCache generating image list line 198 JSONObject " + images.toString());
	   
	         }
	         catch(JSONException e)
	         {
	             log.throwing(KEY, "constructor", e);
	             images = WebUtils.buildErrorJson( "Problem retrieving image list from source." + e.getMessage());
	         }
	         log.finer( "Done retrieving image list" );
	         try {
	        	 medicalHistory  = new JSONObject();
		         for (int i=0; i < reps.length(); i++ )
		         {
		        		JSONObject repObj = (JSONObject) reps.get(i);
		        		String repository = repObj.getString("repository");
		        		String repoPatientId = repObj.getString("id");
		        		
		        		results = getJsonContent( app, "/patients/" + repoPatientId + "/history" );
			              
			            JSONObject history = new JSONObject(results);
			            history.put("repository", repository);
			            medicalHistory.append("medicalHistory", history);
		         }
		         //System.out.println("PatientCache generating medical History list line 221 JSONObject " + medicalHistory.toString());
		  	   
	         }
	         catch(JSONException e)
	         {
	             log.throwing(KEY, "constructor", e);
	             images = WebUtils.buildErrorJson( "Problem retrieving medical history from source." + e.getMessage());
	         }
		 }
         log.exiting(KEY, "run()");
    }


    protected String getJsonContent( MedcafeApplication app, String endpoint )
    {
        log.finer( "Starting retrieval of " + endpoint );
        Request req = new Request( Method.GET, endpoint );
        Response resp = new Response( req );
        ClientInfo clientInfo = req.getClientInfo();
        List<Preference<MediaType>> mediaTypes = clientInfo.getAcceptedMediaTypes();
        mediaTypes.add( new Preference( MediaType.APPLICATION_JSON, 1.0F) );
        app.handle(req, resp);
        StringWriter out = new StringWriter();
        if (resp.getStatus().isSuccess() && resp.getEntity().isAvailable() ) {
            try
            { resp.getEntity().write(out); }
            catch (IOException e)
            {
                log.throwing(KEY, "getJsonContent()", e);
                return WebUtils.buildErrorJson( "Problem retrieving data from source." + e.getMessage()).toString();
            }
        }
        else
        {
            out.write( NA );
        }
        log.finer( "Finished retrieval of " + endpoint );
        return out.toString();
    }

   
    //}}}

    //{{{ Getters and Setters
	public JSONObject getAlertList() { return this.alertList; }
	public JSONObject getFamilyHistory() { return this.familyHistory; }
	public JSONObject getImages() { return this.images; }
	public JSONObject getMedicalHistory() { return this.medicalHistory; }
	public JSONObject getMedicineList() { return this.medicineList; }
	public void setAlertList(JSONObject alertList) { this.alertList = alertList; }
	public void setFamilyHistory(JSONObject familyHistory) { this.familyHistory = familyHistory; }
	public void setImages(JSONObject images) { this.images = images; }
	public void setMedicalHistory(JSONObject medicalHistory) { this.medicalHistory = medicalHistory; }
	public void setMedicineList(JSONObject medicineList) { this.medicineList = medicineList; }
    public String getDatabasePatientId() { return this.databasePatientId; }
	public void setDatabasePatientId(String databasePatientId) { this.databasePatientId = databasePatientId; }
	public JSONObject getRepositories() { return this.repositories; }
	public void setRepository(JSONObject repositories) { this.repositories = repositories; }
	
	public String getRepoPatientId(String repositoryName) 
	{
		//return this.repoPatientId; 
		return "1";
	}
	
	public void setRepoPatientId(String repositoryName, String repositoryId) 
	{
		//this.repoPatientId = repoPatientId; 
	}
	
	public String getLastName() { return this.lastName; }
	public void setLastName(String lastName) { this.lastName = lastName; }
	public String getFirstName() { return this.firstName; }
	public void setFirstName(String firstName) { this.firstName = firstName; }
    //}}}

}
// :wrap=none:noTabs=true:collapseFolds=1:folding=explicit:

