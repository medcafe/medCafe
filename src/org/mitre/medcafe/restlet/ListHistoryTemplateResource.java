package org.mitre.medcafe.restlet;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;
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


public class ListHistoryTemplateResource extends ServerResource {

	 /** The underlying Item object. */
    //Patient item;

    /** The sequence of characters that identifies the resource. */
	private String patientId;
    String repository;
    public final static String KEY = ListHistoryTemplateResource.class.getName();
    public final static Logger log = Logger.getLogger( KEY );
    // static{log.setLevel(Level.FINER);}

    private final static String SELECT_TEMPLATE_HISTORY = "select symptom_list.id, category, symptom from physical_category, symptom_list " + 
    														" where physical_category.id = symptom_list.physical_category order by category";

    private final static String SELECT_PATIENT_HISTORY = "select symptom_id, category, symptom " +
    													" from physical_category, symptom_list,patient_symptom_list " +
    													" where physical_category.id = symptom_list.physical_category and patient_symptom_list.symptom_id = symptom_list.id and patient_id =?  order by category";
    
    private final static String SELECT_PATIENT_HISTORY_COUNT = "select count(*) " +
														" from physical_category, symptom_list,patient_symptom_list " +
														" where physical_category.id = symptom_list.physical_category and patient_symptom_list.symptom_id = symptom_list.id and patient_id =? ";


    protected void doInit() throws ResourceException {
        // Get the "type" attribute value taken from the URI template
        Form form = getRequest().getResourceRef().getQueryAsForm();
        patientId = (String) getRequest().getAttributes().get("id");
    }

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
    
    public JSONObject getHistory()
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
			 ResultSet rs = null;
			 //First check if patient records already exist
			 String sql = SELECT_PATIENT_HISTORY_COUNT;
			 ArrayList<Integer> symptomIds = new ArrayList<Integer>();
			 
			 if (patientId == null)
			 {
				 System.out.println("ListHistoryTemplateResource: getHistory : patient not found  ");
				 
				 sql = SELECT_TEMPLATE_HISTORY;
				 prep = dbConn.prepareStatement(sql);
				 
			 }
			 else
			 {
				 
				 System.out.println("ListHistoryTemplateResource: getHistory : patient found  " + patientId);
					
				 prep = dbConn.prepareStatement(sql);
				 int patient_id = Integer.parseInt(patientId);

				 prep.setInt(1,patient_id);
				 
				 int count = 0;
				 System.out.println("ListHistoryTemplateResource: getHistory : get the number of records " + count);
					
				 rs = prep.executeQuery();
				 if (rs.next())
				 {
					 count = rs.getInt(1);  
					 System.out.println("ListHistoryTemplateResource: getHistory : count is " + count);
						
				 }
				 if (count > 0)
				 {
					 sql = SELECT_PATIENT_HISTORY;
					 prep = dbConn.prepareStatement(sql);
					 prep.setInt(1, patient_id);
					 rs = prep.executeQuery();
					 while (rs.next())
					 {
						 int symptomId = rs.getInt("symptom_id");
						 symptomIds.add(symptomId);
					 }
				 }
				 
				 sql = SELECT_TEMPLATE_HISTORY;
				 prep = dbConn.prepareStatement(sql);
				 
			 }
			 System.out.println("ListHistoryTemplateResource: getHistory : about to execute query " + prep.toString());
				
			 rs = prep.executeQuery();
			 boolean rtnResults = false;
			
			 System.out.println("ListHistoryTemplateResource: getHistory : query " + prep.toString());
			 
			 JSONObject catObj =null;
			 
			 String prevCategory = "";
			 String category = "";
			 while (rs.next())
			 {
				  rtnResults = true;
		            
				  int symptomId = rs.getInt(1);

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
				  if (symptomIds.contains(symptomId))
				  {
					  symptomObj.put("hasSymptom", "true");
				  }
				  else
				  {
					  symptomObj.put("hasSymptom", "false");
				  }
				  
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