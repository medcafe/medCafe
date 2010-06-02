package org.mitre.medcafe.restlet;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;
import org.mitre.medcafe.model.Address;
import org.mitre.medcafe.model.History;
import org.mitre.medcafe.util.Constants;
import org.mitre.medcafe.util.DbConnection;
import org.mitre.medcafe.util.WebUtils;
import org.restlet.data.Form;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;


public class ListAddressResource extends ServerResource {

	 /** The underlying Item object. */
    //Patient item;

    /** The sequence of characters that identifies the resource. */
	private String patientId;
    String repository;
    public final static String KEY = ListAddressResource.class.getName();
    public final static Logger log = Logger.getLogger( KEY );
    // static{log.setLevel(Level.FINER);}
    protected void doInit() throws ResourceException {
        // Get the "type" attribute value taken from the URI template
        Form form = getRequest().getResourceRef().getQueryAsForm();
        patientId = (String) getRequest().getAttributes().get("id");
    }

    @Get("html")
    public Representation toHtml(){

    	System.out.println("Found ListAddressResource html ");

    	StringBuffer startBuf = new StringBuffer();
    	
    	return new StringRepresentation( startBuf.toString());
    	

    }
    
    @Get("json")
    public JsonRepresentation toJson(){
        try
        {
        	System.out.println("Found ListAddressResource json ");

       	  	JSONObject obj = Address.getAddress(patientId);
           
            log.finer( obj.toString());
            // System.out.println("ListWidgetResource JSON " +  obj.toString());
            return new JsonRepresentation(obj);
           
        }
        catch(Exception e)
        {
        	log.throwing(KEY, "toJson()", e);
        	return new JsonRepresentation(WebUtils.buildErrorJson( "Problem oncreation of JSON for component: Error " + e.getMessage() ));		
         
        }
    }
   
   
   
	 
    
	
}