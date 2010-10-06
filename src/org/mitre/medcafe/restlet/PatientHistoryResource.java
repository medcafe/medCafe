package org.mitre.medcafe.restlet;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONObject;
import org.mitre.medcafe.model.History;
import org.mitre.medcafe.model.Patient;
import org.mitre.medcafe.util.WebUtils;
import org.mitre.medcafe.util.*;
import org.restlet.data.Form;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.ResourceException;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;


public class PatientHistoryResource extends ServerResource {

	 /** The underlying Item object. */
    //Patient item;

    /** The sequence of characters that identifies the resource. */
	private String id;
	private String category;

	private final static String PATIENT_ID = "id";
	private final static String USER_ID = "user";
	private final static String CATEGORY = "category";
    public final static String KEY = PatientHistoryResource.class.getName();
    public final static Logger log = Logger.getLogger( KEY );
	 private String repository;
    protected Date startDate = null;
    protected Date endDate =  null;
    
    // static{log.setLevel(Level.FINER);}

    protected void doInit() throws ResourceException 
    {
        // Get the "type" attribute value taken from the URI template
        Form form = getRequest().getResourceRef().getQueryAsForm();
        id = (String)getRequest().getAttributes().get(PATIENT_ID);
        this.repository = (String) getRequest().getAttributes().get("repository");
        category = (String)getRequest().getAttributes().get(CATEGORY);

        if (category == null)
        	category = "NONE";
        String startDateStr = form.getFirstValue("start_date");
        String endDateStr = form.getFirstValue("end_date");
   	
         	
        System.out.println("PatientHistoryResource JSON init startDate " +  startDateStr + " endDate " + endDateStr + " category " + category );
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        try {
        	if (startDateStr != null)
        		startDate = df.parse(startDateStr);
        	
        	if (endDateStr != null)
        		endDate = df.parse(endDateStr);
        }
        catch (ParseException e)
        {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @Get("html")
    public Representation toHtml(){

    	System.out.println("Found PatientHistoryResource html ");
    
    	StringBuffer stringBuf = new StringBuffer("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"tableBookmarks" + id + "\">" +
    											"<thead><tr><th></th><th></th><th></th></tr></thead><tbody>");
    
    	return new StringRepresentation( stringBuf.toString());

    }

    @Get("json")
    public JsonRepresentation toJson(){
        try
        {
             Repository r = Repositories.getRepository(repository);

        		if (r == null) {
            	return new JsonRepresentation(WebUtils.buildErrorJson("A repository named " + repository + " does not exist."));

        		}

            JSONObject obj = r.getHistory(id, category, startDate, endDate);
            
            log.finer( obj.toString());
            System.out.println("PatientHistoryResource JSON " +  obj.toString());
            return new JsonRepresentation(obj);
        }
        catch(Exception e)
        {
            log.throwing(KEY, "toJson()", e);
            return null;
        }
    }


}
