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

import org.restlet.data.*;

import java.io.IOException;
import java.util.*;
import org.json.JSONObject ;
import org.mitre.medcafe.util.*;
import org.projecthdata.hdata.schemas._2009._06.allergy.*;
import org.projecthdata.hdata.schemas._2009._06.patient_information.*;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
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
    public JsonRepresentation toJson()
    {
        Repository r = Repositories.getRepository( repository );
        if( r == null )
        {
            return new JsonRepresentation(WebUtils.buildErrorJson( "A repository named " + repository + " does not exist."));
        }

        List<Allergy> allergies = r.getAllergies( id );

        if( allergies == null )
        {
            return new JsonRepresentation(WebUtils.buildErrorJson( "Could not establish a connection to the repository " + repository + " at this time."));
        }

        if( allergies.size() == 0)
        {
            return new JsonRepresentation(WebUtils.buildErrorJson( "There are no allergies currently listed for patient " + id + " in repository " + repository ));
        }
        //convert to JSON
        return WebUtils.bundleJsonResponse( "allergies", allergies, repository, id );
    }

    @Put
    public Representation acceptItem(Representation entity)
    {
        Representation ret = null;
        if( entity == null )
        {
            // PUT request with no entity.
            getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            return new StringRepresentation("Error", MediaType.TEXT_PLAIN);
        }

        // Parse the given representation and retrieve pairs of
        // "name=value" tokens.
        Form form = new Form(entity);
        String allergyName = form.getFirstValue("name");
        this.id = (String) getRequest().getAttributes().get("id");
        this.repository = (String) getRequest().getAttributes().get("repository");

        //DO INSERT HERE.  Really,we should go through the Repository Object like we do for the @Get.  And that means making an hData object first and THEN putting it
        //into VistA.  You can bypass all that, though, if you want...just leave a note there to say that'd be the "proper" way to do it.

        // See http://stackoverflow.com/questions/996819/restlet-how-to-process-multipart-form-data-requests for more info

        ret = new StringRepresentation( "Allergy " + allergyName + " inserted for patient " + id + " in repository " + repository + ".\\n\\n or would be if Jeff hadn't slapped this together purely as a demo." );

        return ret;
    }
}
