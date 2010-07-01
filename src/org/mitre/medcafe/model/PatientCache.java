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
	protected String repository = null;
	protected String repoPatientId = null;

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
        DbConnection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            conn = new DbConnection();
            rs = conn.psExecuteQuery(query, "Looking up repository info",  new Integer(databasePatientId));
            while(rs.next())
            {
                repository = rs.getString("repository");
                repoPatientId = rs.getString("rep_patient_id");
                firstName = rs.getString("first_name");
                lastName = rs.getString("last_name");
            }
            DatabaseUtility.close(rs);
        }
        catch(SQLException e)
        {
            log.throwing( KEY, "Error looking up patient repository details", e);
            throw new RuntimeException(e);
        }
        finally
        {
            DatabaseUtility.close(rs);
            conn.close();
        }
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
        String results;
        try {
            results = getJsonContent( app, "/repositories/" + repository + "/patients/" + repoPatientId + "/medications" );
            medicineList = new JSONObject(results);
        }
        catch(JSONException e)
        {
            log.throwing(KEY, "constructor", e);
            medicineList = WebUtils.buildErrorJson( "Problem retrieving medication list from source." + e.getMessage());
        }
        log.finer("Done retrieving medication list");
        try {
            results = getJsonContent( app, "/repositories/" + repository + "/patients/" + repoPatientId + "/allergies" );
            alertList = new JSONObject(results);
        }
        catch(JSONException e)
        {
            log.throwing(KEY, "constructor", e);
            alertList = WebUtils.buildErrorJson( "Problem retrieving alert list from source." + e.getMessage());
        }
        log.finer("Done retrieving alert list");
        try {
            results = getJsonContent( app, "/repositories/" + repository + "/patients/" + repoPatientId + "/images" );
            images = new JSONObject(results);
         }
         catch(JSONException e)
         {
             log.throwing(KEY, "constructor", e);
             images = WebUtils.buildErrorJson( "Problem retrieving image list from source." + e.getMessage());
         }
         log.finer( "Done retrieving image list" );
         try {
             results = getJsonContent( app, "/patients/" + repoPatientId + "/history" );
             medicalHistory = new JSONObject( results );
         }
         catch(JSONException e)
         {
             log.throwing(KEY, "constructor", e);
             images = WebUtils.buildErrorJson( "Problem retrieving medical history from source." + e.getMessage());
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

    public JSONObject toJson() throws JSONException
    {
        JSONObject ret = new JSONObject();
        ret.put("localid", databasePatientId);
        ret.put("repository", repository);
        ret.put("repository patient id", repoPatientId);
        return ret;
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
	public String getRepository() { return this.repository; }
	public void setRepository(String repository) { this.repository = repository; }
	public String getRepoPatientId() { return this.repoPatientId; }
	public void setRepoPatientId(String repositoryId) { this.repoPatientId = repoPatientId; }
	public String getLastName() { return this.lastName; }
	public void setLastName(String lastName) { this.lastName = lastName; }
	public String getFirstName() { return this.firstName; }
	public void setFirstName(String firstName) { this.firstName = firstName; }
    //}}}

}
// :wrap=none:noTabs=true:collapseFolds=1:folding=explicit:

