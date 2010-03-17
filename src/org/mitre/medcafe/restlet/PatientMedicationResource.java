package org.mitre.medcafe.restlet;

import com.google.gson.*;
import java.io.IOException;
import java.util.*;
import org.json.JSONObject ;
import org.mitre.medcafe.util.*;
import org.projecthdata.hdata.schemas._2009._06.medication.*;
import org.projecthdata.hdata.schemas._2009._06.patient_information.*;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.*;



public class PatientMedicationResource extends ServerResource {

    /** The sequence of characters that identifies the resource. */
    String id;
    String repository;

    @Override
    protected void doInit() throws ResourceException {
        this.id = (String) getRequest().getAttributes().get("id");
        this.repository = (String) getRequest().getAttributes().get("repository");
    }

    //Output example : {"effectiveTime":[{}],"dose":{"value":"40MG"},"deliveryMethod":{"value":"ORAL"},"medicationInformation":{"manufacturedMaterial":{"freeTextBrandName":"QUINAPRIL TAB"}},"patientInstructions":"BID","narrative":"QUINAPRIL TAB ..."}

    @Get("json")
    public JsonRepresentation toJson(){
        Repository r = Repositories.getRepository( repository );
        
        if( r == null )
        {
            return new JsonRepresentation(WebUtils.buildErrorJson( "A repository named " + repository + " does not exist."));
        }
        
        List<Medication> medications = r.getMedications( id );
        
        if( medications == null )
        {
            return new JsonRepresentation(WebUtils.buildErrorJson( "Could not establish a connection to the repository " + repository + " at this time."));
        }
        
        if( medications.size() == 0)
        {
            return new JsonRepresentation(WebUtils.buildErrorJson( "There are no medications currently listed for patient " + id + " in repository " + repository ));
        }
        //convert to JSON
        return WebUtils.bundleJsonResponse( "medications", medications, repository, id );
    }
    //output
    //
}
