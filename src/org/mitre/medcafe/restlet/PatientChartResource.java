package org.mitre.medcafe.restlet;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;
import org.mitre.medcafe.util.Repository;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.ResourceException;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;


public class PatientChartResource extends ServerResource {

	 /** The underlying Item object. */
    //Patient item;

    /** The sequence of characters that identifies the resource. */
    String id;
    String repository;
    public final static String KEY = PatientChartResource.class.getName();
    public final static Logger log = Logger.getLogger( KEY );
    static{log.setLevel(Level.FINER);}
    

    @Get("html")
    public Representation toHtml(){
    	
    	System.out.println("Found PatientResource html ");
        
    	StringBuffer startBuf = new StringBuffer();
    	StringBuffer patientImages = new StringBuffer();
    	StringBuffer endBuf = new StringBuffer();
    	
    	return new StringRepresentation( startBuf.toString() + patientImages.toString() 
                 + endBuf.toString()); 
             
    }

    @Get("json")
    public JsonRepresentation toJson(){
        try
        {
        	System.out.println("PatientChartResource JSON start");
            
        	String[] xValues = new String[]{"1261380000000","1261385000000","1261425000000" +
					"1261480000000", "1261485000000","1261490000000","1261584970731"};
        	String[] yValues = new String[]{"100.1","101.7","101.7" +
					"101.5", "100.2","100.2","98.7"};
        	String[] label = new String[]{"Temperature" };
        	int i=0;
        	
            JSONObject obj = new JSONObject();
            obj.put("label",label[0]);
            
            for(String xValue: xValues)
            {
            	JSONArray arrayObj=new JSONArray();
            	 
            	arrayObj.put(xValue);
            	arrayObj.put(yValues[i]);
                obj.append("data", arrayObj);  //append creates an array for you
                i++;
            }
            
            log.finer( obj.toString());
            System.out.println("PatientChartResource JSON " +  obj.toString());
            return new JsonRepresentation(obj);
        }
        catch(Exception e)
        {
            log.throwing(KEY, "toJson()", e);
            return null;
        }
    }
}