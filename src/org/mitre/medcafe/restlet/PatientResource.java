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

import org.json.JSONObject ;
import org.json.JSONException;
import org.mitre.medcafe.model.Patient;
import org.mitre.medcafe.util.Repository;
import org.mitre.medcafe.util.WebUtils;
import java.util.logging.Logger;
import java.util.logging.Level;
import com.google.gson.*;
import java.io.IOException;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.ext.xml.DomRepresentation;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class PatientResource extends ServerResource {

    /** The underlying Item object. */
    //Patient item;
 public final static String KEY = PatientResource.class.getName();
    public final static Logger log = Logger.getLogger( KEY );
    // static{log.setLevel(Level.FINER);}

    /** The sequence of characters that identifies the resource. */
    private String id;
    private String repository;
    private boolean isSingle;

    @Override
    protected void doInit() throws ResourceException {
        // Get the "id" attribute value taken from the URI template
        // /items/{id}.
        this.id = (String) getRequest().getAttributes().get("id");
        this.repository = (String) getRequest().getAttributes().get("repository");
        String single = (String) getRequest().getAttributes().get("isSingle");
        if (single != null && single.equals("true"))
        	isSingle = true;
        else
        	isSingle = false;
        	log.finer("Is single repository request: " + isSingle);


    }

    /*
    @Get("html")
    public Representation toHtml(){

    	StringBuffer buf = new StringBuffer();
    	StringBuffer endBuf = new StringBuffer();

    	buf.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"example" + this.id +"\">");
    	buf.append("");

    	String[] titles = new String[]{"Patient ID","Name", "Address", "Phone numbers", "Gender",
    									"Languages", "Birthdate", "Maritial Status","Race","Guardian", "Birth place" };

    	String[] values = new String[]{this.id,"", "", " ", "",
				"", "", " ","","", "" };

    	StringBuffer patientData = new StringBuffer();
    	patientData.append("<thead><tr><th></th><th></th></tr></thead>");
    	patientData.append("<tbody>");
    	int i=0;
    	for (String title:titles)
    	{
    		patientData.append("<tr class=\"gradeX\"><td>" + title + "</td><td>" + values[i]+ "</td></tr>" );
    		i++;
    	}

    	endBuf.append("</tbody></table>");
    	return new StringRepresentation( buf.toString() + patientData.toString()
                 + endBuf.toString());

    }
    */

    @Get("json")
    public JsonRepresentation toJson(){
        Repository r = Repositories.getRepository( repository );
        JsonRepresentation patJsonRep = null;
        if (r == null)
        {
        	 log.finer("PatientResource : toJSON: Cannot find the repository " + repository);
        	 return new JsonRepresentation(WebUtils.buildErrorJson( "Could not find the repository " + repository ));
        }
        Patient pat = r.getPatient( id );
 
        //convert to JSON
        if (pat != null)
        {
        	  patJsonRep = WebUtils.bundleJsonResponse("patient_data", pat, repository, id);
        }
        else
        {
            return new JsonRepresentation(WebUtils.buildErrorJson( "There is no patient info for patient " + id + " in repository " + repository ));
        }
		  if (!isSingle)
        		return patJsonRep;
        else
        {
        		try {
        			JSONObject patientData = WebUtils.bundleJsonResponseObject("patient_data", pat, repository, id);
        	  		JSONObject ret = new JSONObject();
 					ret.append("repositoryList", patientData);
 					log.finer("PatientResource: " + ret.toString());
 					return new JsonRepresentation(ret);
 				}
 				catch (JSONException jsonE)
 				{
 					log.severe("Error with single repository "+ jsonE.getMessage());
 					patJsonRep = new JsonRepresentation(WebUtils.buildErrorJson("Error creating JSON for the patient data"));
 				}
 				return patJsonRep;
 			}
    }
}
