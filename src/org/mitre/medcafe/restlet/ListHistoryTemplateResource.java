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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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


public class ListHistoryTemplateResource extends ServerResource {

	 /** The underlying Item object. */
    //Patient item;

    /** The sequence of characters that identifies the resource. */
	private String patientId;
    String repository;
    public final static String KEY = ListHistoryTemplateResource.class.getName();
    public final static Logger log = Logger.getLogger( KEY );
    // static{log.setLevel(Level.FINER);}

     protected void doInit() throws ResourceException {
        // Get the "type" attribute value taken from the URI template
        Form form = getRequest().getResourceRef().getQueryAsForm();
        patientId = (String) getRequest().getAttributes().get("id");
    }

    @Get("html")
    public Representation toHtml(){

    	log.finer("Found ListHistoryTemplateResource html ");

    	StringBuffer startBuf = new StringBuffer();

    	return new StringRepresentation( startBuf.toString());


    }

    @Get("json")
    public JsonRepresentation toJson(){
        try
        {
        	log.finer("Found ListHistoryTemplateResource json ");

       	  	JSONObject obj = History.getSymptomHistory(patientId);

            log.finer( "ListHistoryTemplateResource: " + obj.toString());

            return new JsonRepresentation(obj);

        }
        catch(Exception e)
        {
        	log.throwing(KEY, "toJson()", e);
        	return new JsonRepresentation(WebUtils.buildErrorJson( "Problem on creation of JSON for component: Error " + e.getMessage() ));

        }
    }



}
