package org.mitre.medcafe.restlet;


import java.io.IOException;
import java.util.*;
import org.json.JSONObject ;
import org.mitre.medcafe.util.*;

import org.projecthdata.hdata.schemas._2009._06.result.*;
import org.projecthdata.hdata.schemas._2009._06.patient_information.*;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.*;



public class PatientVitalsResource extends ServerResource {



    /** The sequence of characters that identifies the resource. */
    String id;
    String repository;
    String choice;

    @Override
    protected void doInit() throws ResourceException {
        this.id = (String) getRequest().getAttributes().get("id");
       // System.out.println("id = " + id);
        this.repository = (String) getRequest().getAttributes().get("repository");
        this.choice = (String) getRequest().getAttributes().get("choice");
    }


    @Get("json")
    public JsonRepresentation toJson()
    {
        Repository r = Repositories.getRepository( repository );
        if( r == null )
        {
            return new JsonRepresentation(WebUtils.buildErrorJson( "A repository named " + repository + " does not exist."));
        }
        List<Result> vitals = null;
        try{
        		if (choice.equals("all"))
        			vitals = r.getAllVitals( id );
        		else
        			vitals = r.getLatestVitals(id);
        		if( vitals == null )
        		{
        		    return new JsonRepresentation(WebUtils.buildErrorJson( "Could not establish a connection to the repository " + repository + " at this time."));
        		}
        
        		if( vitals.size() == 0)
        		{
            	return new JsonRepresentation(WebUtils.buildErrorJson( "There are no vitals currently listed for patient " + id + " in repository " + repository ));
        		}
        //convert to JSON
        		return WebUtils.bundleJsonResponse( "vitals", vitals, repository, id );
 
 		}
  		catch(NotImplementedException notImplE)
      {
        		return new JsonRepresentation(WebUtils.buildErrorJson(notImplE.getMessage()));
      }
    }
}
