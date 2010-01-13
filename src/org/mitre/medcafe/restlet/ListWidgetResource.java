package org.mitre.medcafe.restlet;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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


public class ListWidgetResource extends ServerResource {

	 /** The underlying Item object. */
    //Patient item;

    /** The sequence of characters that identifies the resource. */
    String id;
    String repository;
    public final static String KEY = ListWidgetResource.class.getName();
    public final static Logger log = Logger.getLogger( KEY );
    static{log.setLevel(Level.FINER);}
    

    @Get("html")
    public Representation toHtml(){
    	
    	System.out.println("Found ListWidgetResource html ");
        
    	StringBuffer startBuf = new StringBuffer();
    	StringBuffer patientImages = new StringBuffer();
    	StringBuffer endBuf = new StringBuffer();
    	
    	//<img src="imgs/cover1.jpg" alt="The Beatles - Abbey Road"/>
    	
    	String[] values = new String[]{this.id,"", "", " ", "",
				"", "", " ","","", "" };

    	String[] images = new String[]{"assessment.png","bloodstat.jpg","cardioReport.gif" +
    									"chest-xray.jpg", "chest-xray2.jpg","mri.jpg"};
    	String[] imageTitles = new String[]{"Assessment","Blood Stats","Cardio Report", "Chest XRay", "Chest XRay","MRI" };
    	int i=0;
    	
    	String dir = "patient1";
    		
    	for (String image: images)
    	{
    	
    		patientImages.append("<img src=\"../" + dir +"/" + image + "\" alt=\"" + imageTitles[i] + "\"/>" );	
    		i++;
    	}
    	return new StringRepresentation( startBuf.toString() + patientImages.toString() 
                 + endBuf.toString()); 
             
    }

    @Get("json")
    public JsonRepresentation toJson(){
        try
        {
        	System.out.println("ListWidgetResource JSON start");
            
        	String[] widgetName = new String[]{"Charts","Images", "Date Picker","Slider"};
        	
        	String[] images = new String[]{"chart.png","coverflow.png", "date.png", "slider-small.png"};
        	String[] clickUrl = new String[]{"http://127.0.0.1:8080/medcafe/chart.jsp","http://127.0.0.1:8080/medcafe/coverflow-flash/index.jsp","", "http://127.0.0.1:8080/medcafe/slider.jsp"};
        	
        	int i=0;
        	
        	String tempDir = "images/";
            JSONObject obj = new JSONObject();
            System.out.println("ListWidgetResource JSON start 1");
            for(String widget: widgetName)
            {
            	
                JSONObject inner_obj = new JSONObject ();
                inner_obj.put("name", widget);
                inner_obj.put("image", tempDir + images[i]);
                inner_obj.put("clickURL", clickUrl[i]);
                obj.append("widgets", inner_obj);  //append creates an array for you
                i++;
            }
            log.finer( obj.toString());
            System.out.println("ListWidgetResource JSON " +  obj.toString());
            return new JsonRepresentation(obj);
        }
        catch(Exception e)
        {
            log.throwing(KEY, "toJson()", e);
            return null;
        }
    }
}