/*
 *  Copyright 2010 The MITRE Corporation (http://www.mitre.org/). All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mitre.medcafe.restlet;


import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;
import java.util.logging.Level;

import org.hl7.greencda.c32.Immunization;
import org.hl7.greencda.c32.Result;
import org.json.JSONObject ;
import org.mitre.medcafe.util.*;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.*;



public class PatientResultsResource extends ServerResource {
    public final static String KEY = PatientResultsResource.class.getName();
    public final static Logger log = Logger.getLogger( KEY );
    // static{log.setLevel(Level.FINER);}
    /** The sequence of characters that identifies the resource. */
    String id;
    String repository;

    @Override
    protected void doInit() throws ResourceException {
        this.id = (String) getRequest().getAttributes().get("id");
        this.repository = (String) getRequest().getAttributes().get("repository");
    }


    @Get("json")
    public JsonRepresentation toJson()
    {
        Repository r = Repositories.getRepository( repository );
        if( r == null )
        {
            return new JsonRepresentation(WebUtils.buildErrorJson( "A repository named " + repository + " does not exist."));
        }
 	       List<Result> results = r.getResults( id );

 	       if( results == null )
 	       {
 	           return new JsonRepresentation(WebUtils.buildErrorJson( "Could not establish a connection to the repository " + repository + " at this time."));
 	       }

 	       if( results.size() == 0)
 	       {
 	           return new JsonRepresentation(WebUtils.buildErrorJson( "There are no lab results currently listed for patient " + id + " in repository " + repository ));
 	       }
 	       //convert to JSON
 	      try{
 	       log.finer("PatientResultsResource:  " + WebUtils.bundleJsonResponse("results",results,repository,id).getText());
 	       }
 	       catch (IOException IOe)
 	       {
 	       	
 	       }
 	       return WebUtils.bundleJsonResponse( "results", results, repository, id );

    	
	}
}
