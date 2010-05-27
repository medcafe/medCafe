package org.mitre.medcafe.restlet;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;
import org.mitre.medcafe.util.DbConnection;
import org.mitre.medcafe.util.WebUtils;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;


public class ListHistoryTemplateResource extends ServerResource {

	 /** The underlying Item object. */
    //Patient item;

    /** The sequence of characters that identifies the resource. */
    String id;
    String repository;
    public final static String KEY = ListHistoryTemplateResource.class.getName();
    public final static Logger log = Logger.getLogger( KEY );
    // static{log.setLevel(Level.FINER);}

    private final static String SELECT_TEMPLATE_HISTORY = "select category, symptom from physical_category, symptom_list " + 
    														" where physical_category.id = symptom_list.physical_category order by category";

    private final static String SELECT_PATIENT_HISTORY = "select symptom_id, category, symptom " +
    													" from physical_category, symptom_list,patient_symptom_list " +
    													" where physical_category.id = symptom_list.physical_category and patient_symptom_list.symptom_id = symptom_list.id and patient_id =?  order by category";
    
    @Get("html")
    public Representation toHtml(){

    	System.out.println("Found ListHistoryTemplateResource html ");

    	StringBuffer startBuf = new StringBuffer();
    	
    	return new StringRepresentation( startBuf.toString());
    	

    }
    
    @Get("json")
    public JsonRepresentation toJson(){
        try
        {
        	System.out.println("Found ListHistoryTemplateResource json ");

       	  	JSONObject obj = getHistory();
           
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
    
    public static JSONObject getHistory()
	 {
		 JSONObject ret = new JSONObject();
		 PreparedStatement prep;
		 DbConnection dbConn = null;
		 try 
		 {
			 if (dbConn == null)
				 dbConn= new DbConnection();

			 if (dbConn == null)
				 dbConn= new DbConnection();
			 String sql = SELECT_TEMPLATE_HISTORY;
			
			 prep = dbConn.prepareStatement(sql);
			 
			 ResultSet rs = prep.executeQuery();
			 boolean rtnResults = false;
			
			 System.out.println("Patient: getPatientHistory : query " + prep.toString());
			 
			 JSONObject catObj =null;
			 
			 String prevCategory = "";
			 String category = "";
			 while (rs.next())
			 {
				  rtnResults = true;
		            
				  category = rs.getString("category");
				  //If this is a new category
				  if (!category.equals(prevCategory))
				  {
					  if (catObj != null)
					  {
						  catObj.put("category", category);
						  ret.append("categories", catObj);
					  }
					  catObj = new JSONObject();
					  
				  }
				  String symptom = rs.getString("symptom");
				  JSONObject symptomObj = new JSONObject();
				  symptomObj.put("name", symptom);
				  
				  catObj.append("symptoms", symptomObj);
			     
				  prevCategory = category;
			      
		     }    
			 
			 catObj.put("category", category);
			 ret.append("categories", catObj);
			  
			 if (!rtnResults)
		      {
		        	return WebUtils.buildErrorJson( "There is no medical history listed " );
		      	  
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