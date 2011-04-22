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
package org.mitre.medj.restlet;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.*;

import javax.xml.bind.JAXBException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mitre.medj.jaxb.ContinuityOfCareRecord;
import org.mitre.medj.util.Patients;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.mitre.medj.WebUtils;

import com.google.gson.Gson;


public class PatientsListResource extends ServerResource {

    public final static String KEY = PatientsListResource.class.getName();
    public final static Logger log = Logger.getLogger( KEY );
    // static{log.setLevel(Level.FINER);}

    private final static String ID = "id";
    private final static String FIRST_NAME = "first_name";
    private final static String LAST_NAME = "last_name";
    private final static String MIDDLE_INITIAL = "middle_initial";

    /** The sequence of characters that identifies the resource. */
    private String repository;

    /* If any info given on name of patient - use these attributes for searching for patient Ids*/

    private String id;
   /**
     *  Grab the information from the url
     */
    @Override
    protected void doInit() throws ResourceException {
    	this.id = (String) getRequest().getAttributes().get("id");
    }

    @Get("json")
    public JsonRepresentation toJson(){
      
    	try {
	    	JSONObject patientList = new JSONObject();
	    	File dir = new File(WebUtils.BASE_DIR + "/" + WebUtils.CCR_DIR);
	    	if (!dir.exists())
	    		return new JsonRepresentation(WebUtils.buildErrorJson( "Problem on creation of JSON for Patients: Could not find file " ));

	    		
	    	ArrayList<String> patientFileList = WebUtils.listPatients(dir);
	    	JSONArray patientIdList = new JSONArray();
	    	for (String patient: patientFileList)
	    	{
	    		JSONObject patientId = new JSONObject();
	    		patientId.put("id", patient);
	    		patientIdList.put(patientId);
	    		patientList.put("patients", patientIdList);
	    	}
	    	return new JsonRepresentation(patientList);
    	} 
    	 catch (JSONException e) {
			// TODO Auto-generated catch block
			return new JsonRepresentation(WebUtils.buildErrorJson( "Problem oncreation of JSON for CCR: Error " + e.getMessage() ));

		}
    }


   
}
