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
import org.mitre.medj.jaxb.ContinuityOfCareRecord.Body.Medications;
import org.mitre.medj.util.Patients;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.mitre.medj.WebUtils;

import com.google.gson.Gson;


public class PatientMedicationResource extends MedJResource {

    public final static String KEY = PatientMedicationResource.class.getName();
    public final static Logger log = Logger.getLogger( KEY );
    // static{log.setLevel(Level.FINER);}

    /** The sequence of characters that identifies the resource. */

   /**
     *  Grab the information from the url
     */
    @Override
    protected void doInit() throws ResourceException {

    	super.doInit();

    }

    @Get("json")
    public JsonRepresentation toJson(){
        
    	JsonRepresentation rtn = super.toJson();
    	//If anything here then there is an issue
    	if (rtn != null)
    		return rtn;
    	
    	try {
    		ContinuityOfCareRecord ccr =  getCCR();
	        
    		Medications meds = ccr.getBody().getMedications();
	        
    		Gson gson = new Gson();
	        if ( ccr!= null)
            {
            	String jsonString = gson.toJson(meds);
                JSONObject obj = new JSONObject(jsonString);
                return new JsonRepresentation(obj);
            }
            else
            {
            	return new JsonRepresentation(WebUtils.buildErrorJson( "Problem oncreation of JSON for CCR:" ) );
  
            }
    	} 
    	catch (JSONException e) {
			// TODO Auto-generated catch block
			return new JsonRepresentation(WebUtils.buildErrorJson( "Problem oncreation of JSON for CCR: Error " + e.getMessage() ));

		}
    }


   
}
