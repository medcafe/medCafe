package org.mitre.medcafe.restlet;

import com.google.gson.*;
import java.io.IOException;
import java.util.*;
import org.json.JSONObject ;
import org.mitre.medcafe.util.*;
import org.projecthdata.hdata.schemas._2009._06.support.*;

import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.*;



public class PatientSupportResource extends ServerResource {

    /** The sequence of characters that identifies the resource. */
    String id;
    String repository;

    @Override
    protected void doInit() throws ResourceException {
        this.id = (String) getRequest().getAttributes().get("id");
        this.repository = (String) getRequest().getAttributes().get("repository");
    }

    
    @Get("json")
    public JsonRepresentation toJson(){
        Repository r = Repositories.getRepository( repository );

        if( r == null )
        {
            return new JsonRepresentation(WebUtils.buildErrorJson( "A repository named " + repository + " does not exist."));
        }
		  try{
        List<Support> supportInfo = r.getSupportInfo( id );

        if( supportInfo == null)
        {
            return new JsonRepresentation(WebUtils.buildErrorJson( "Could not establish a connection to the repository " + repository + " at this time."));
        }

        if( supportInfo.size() == 0)
        {
            return new JsonRepresentation(WebUtils.buildErrorJson( "There are no support contacts currently listed for patient " + id + " in repository " + repository ));
        }
        //convert to JSON
    /*  try{
        System.out.println(WebUtils.bundleJsonResponse("supportInfo",supportInfo,repository,id).getText());
        }
        catch (IOException IOe)
        {
        	System.out.println("Couldn't print");
        }  */ 
        return WebUtils.bundleJsonResponse( "supportInfo", supportInfo, repository, id );
        }
        catch (NotImplementedException notImplE)
        {
        		return new JsonRepresentation(WebUtils.buildErrorJson(notImplE.getMessage()));
        }
    }
    //output
    //
}
