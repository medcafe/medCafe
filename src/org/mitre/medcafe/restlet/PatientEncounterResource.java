package org.mitre.medcafe.restlet;

import com.google.gson.*;
import java.io.IOException;
import java.util.*;
import org.json.JSONObject;
import org.mitre.medcafe.util.*;



import org.mitre.medcafe.hdatabased.encounter.EncounterDetail;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.*;

public class PatientEncounterResource extends ServerResource {

    /** The sequence of characters that identifies the resource. */
    String id;
    String repository;

    @Override
    protected void doInit() throws ResourceException {
        this.id = (String) getRequest().getAttributes().get("id");
        this.repository = (String) getRequest().getAttributes().get("repository");
    }

    @Get("json")
    public JsonRepresentation toJson() {
        Repository r = Repositories.getRepository(repository);

        if (r == null) {
            return new JsonRepresentation(WebUtils.buildErrorJson("A repository named " + repository + " does not exist."));
        }

        try {
            Collection<EncounterDetail> encounters = r.getPatientEncounters(id);

            if (encounters == null) {
                return new JsonRepresentation(WebUtils.buildErrorJson("Could not establish a connection to the repository " + repository + " at this time."));
            }

            if (encounters.size() == 0) {
                return new JsonRepresentation(WebUtils.buildErrorJson("There are no encounters listed for patient " + id + " in repository " + repository));
            }
            //convert to JSON
     /*  try{
            System.out.println(WebUtils.bundleJsonResponse("encounters", encounters,repository,id).getText());
            }
            catch (IOException IOe)
            {
            System.out.println("Couldn't print");
            }   */
            return WebUtils.bundleJsonResponse("encounters", encounters, repository, id);
        } catch (NotImplementedException notImplE) {
            return new JsonRepresentation(WebUtils.buildErrorJson(notImplE.getMessage()));
        }
        //output
        //
    }
}

