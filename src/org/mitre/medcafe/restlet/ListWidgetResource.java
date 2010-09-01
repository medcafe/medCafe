package org.mitre.medcafe.restlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONObject;
import org.mitre.medcafe.model.MedCafeComponent;
import org.mitre.medcafe.util.Config;
import org.mitre.medcafe.util.Constants;
import org.mitre.medcafe.util.Repository;
import org.mitre.medcafe.util.WebUtils;
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
    // static{log.setLevel(Level.FINER);}


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

    //@Get("json")
    public JsonRepresentation toJsonOld(){
        try
        {

        	/*	var link = $(this).find('img').attr("custom:url");
				var type = $(this).find('img').attr("custom:type");
				var html = $(this).find('img').attr("custom:html");
				var method = $(this).find('img').attr("custom:method");
				var patientId = $(this).find('img').attr("custom:Id");
			*/
        	System.out.println("ListWidgetResource JSON start");
        	String server = "http://" + Config.getServerUrl() + "/";
        	String[] widgetName = new String[]{"OpenVISTA", "hData", "Charts", "Slider",  "Dates","Editor","Categories"};
        	String[] type = new String[]{"Repository","Repository", "Chart", "Slider", "Date","Editor","Category"};

        	String[] images = new String[]{"openvista.gif","hData.png", "chart.png", "slider-small.png",  "date.png","pages-icon.png","filter.png"};
        	String[] clickUrl = new String[]{server,server, server + "chart.jsp",server +"slider.jsp", "","editor.jsp", "listCategory.jsp" };

        	String[] method = new String[]{"medcafe.add('"+ server+ "','"+ Constants.OUR_VISTA+ "')","medcafe.add('"+ server+ "','"+ Constants.HDATA + "')", "","","", "",""};
        	String[] params = new String[]{"repository:OurVista","repository:OurHdata","", "","","", ""};
        	String[] repository = new String[]{"OurVista","OurHdata", "", "","","", ""};

        	int i=0;

        	String tempDir = "images/";
            JSONObject obj = new JSONObject();
            //System.out.println("ListWidgetResource JSON start 1");
            for(String widget: widgetName)
            {

            	 JSONObject inner_obj = new JSONObject ();
            	 inner_obj.put("id", 1);
            	 inner_obj.put("name", widget);
            	 inner_obj.put("image", tempDir + images[i]);
            	 inner_obj.put("clickURL", clickUrl[i]);
            	 inner_obj.put("method", method[i]);
            	 inner_obj.put("type", type[i]);
            	 inner_obj.put("params", params[i]);
            	 inner_obj.put("repository", repository[i]);

            	 obj.append("widgets", inner_obj);  //append creates an array for you
                i++;
            }
            log.finer( obj.toString());
            //System.out.println("ListWidgetResource JSON " +  obj.toString());
            return new JsonRepresentation(obj);
        }
        catch(Exception e)
        {
        	log.throwing(KEY, "toJson()", e);
        	return new JsonRepresentation(WebUtils.buildErrorJson( "Problem oncreation of JSON for component: Error " + e.getMessage() ));		
         
        }
    }
    
    @Get("json")
    public JsonRepresentation toJson(){
        try
        {

        	/*	var link = $(this).find('img').attr("custom:url");
				var type = $(this).find('img').attr("custom:type");
				var html = $(this).find('img').attr("custom:html");
				var method = $(this).find('img').attr("custom:method");
				var patientId = $(this).find('img').attr("custom:Id");
			*/
        	System.out.println("ListWidgetResource JSON start");
      
            
            String tempDir = "images/";
        	
        	ArrayList<MedCafeComponent> compList = MedCafeComponent.retrieveComponents(MedCafeComponent.GENERAL, tempDir);
        	int i=0;
   	
            JSONObject obj = new JSONObject();
            System.out.println("ListWidgetResource JSON general widgets number of components " + compList.size());
            for(MedCafeComponent component: compList)
            {
            	 
            	 JSONObject inner_obj = component.toJSON();
            	 
            	 //inner_obj.append("widget", inner_inner_obj);
            	 obj.append("widgets", inner_obj);  //append creates an array for you
                 i++;
            }
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
