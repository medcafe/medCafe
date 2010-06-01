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

    private  DbConnection dbConn = null;
    
    private String SELECT_PATIENT_ADDRESS ="SELECT street, street2, city, state, zip, country from address where patient_id = ? ";
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

       	  	JSONObject obj = getAddress(patientId);
           
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
   
    private JSONObject getAddress(String patientId)
    {
    	 JSONObject ret = new JSONObject();
		 PreparedStatement prep;
		 int patient_id = Integer.parseInt(patientId);
		
		 try 
		 {
			 if (dbConn == null)
				 dbConn= new DbConnection();
			 String sql = SELECT_PATIENT_ADDRESS;
			     
			 prep = dbConn.prepareStatement(sql);				
			 prep.setInt(1, patient_id);
			
			 System.out.println("ListAddressResource: getAddress : query " + prep.toString());
				
			 ResultSet rs = prep.executeQuery();
			 boolean rtnResults = false;
			 
			 while (rs.next())
			 {
				 //SELECT patient_id, history, category_id, history_date, history_notes
				 
				  rtnResults = true;
		            
				  JSONObject o = new JSONObject();
				  String street = rs.getString("street");
			      String street2 = rs.getString("street2");
			      String city = rs.getString("city");
			      String state = rs.getString("state");
			      String zip = rs.getString("zip");
			      String country = rs.getString("country");
			        
			      o.put("street", street);
			      if (street2 != null)
			    	  o.put("street2", street);
			      
			      o.put("city", city);
			      o.put("state", state);
			      o.put("zip", zip);
			      o.put("country", country);
					      
			      //o_new.put("history", o);
			      ret.append("address", o);	
		     }    
			 
			 if (!rtnResults)
		      {
				 JSONObject o = new JSONObject();
				 o.put("street", "");
			     o.put("street2", "");
			      
			      o.put("city", "");
			      o.put("state", "");
			      o.put("zip", "");
			      o.put("country", "");
					      
			      //o_new.put("history", o);
			      ret.append("address", o);	
		      }
		 }
		 catch (SQLException e) 
		 {
				// TODO Auto-generated catch block
			 return WebUtils.buildErrorJson( "Problem on selecting data from database ." + e.getMessage());
	      	     
		 } catch (JSONException e) {
			// TODO Auto-generated catch block
			 return WebUtils.buildErrorJson( "Problem on building JSON Object ." + e.getMessage());		      	
		} 
		
		 return ret;
    }
   
	 
}