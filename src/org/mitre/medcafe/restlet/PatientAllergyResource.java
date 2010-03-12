package org.mitre.medcafe.restlet;

import com.google.gson.*;
import java.io.IOException;
import java.util.*;
import org.json.JSONObject ;
import org.mitre.medcafe.util.*;
import org.projecthdata.hdata.schemas._2009._06.allergy.*;
import org.projecthdata.hdata.schemas._2009._06.patient_information.*;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.*;



public class PatientAllergyResource extends ServerResource {

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
        List<Allergy> allergies = r.getAllergies( id );
        //convert to JSON
        Gson gson = new Gson();
        // JSONObject obj = r.getPatient( id );
        return new JsonRepresentation(  gson.toJson(allergies) );
    }
}
