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

import java.util.logging.Level;
import java.util.logging.Logger;
import  org.mitre.medcafe.util.*;
import java.io.IOException;
import org.json.*;
import org.restlet.ext.json.*;
import org.restlet.representation.*;
import org.restlet.resource.*;

public class RepositoryResource extends ServerResource {

    public final static String KEY = RepositoryResource.class.getName();
    public final static Logger log = Logger.getLogger( KEY );
    // static{log.setLevel(Level.FINER);}

    /** The underlying Item object. */
    //Patient item;

    /** The sequence of characters that identifies the resource. */
    String id;
    String repository;

    @Override
    protected void doInit() throws ResourceException {
        this.repository = (String) getRequest().getAttributes().get("repository");
    }

    @Get("json")
    public Representation toJson(){
        Repository r = Repositories.getRepository( repository );
        if( r == null )
        {
            return new JsonRepresentation(WebUtils.buildErrorJson( "A repository named " + repository + " does not exist."));
        }
        try
        {
            JSONObject ret = new JSONObject();
            ret.put("repository", r.getName() );
            ret.put("type", r.getType() );
            return new JsonRepresentation( ret );
        }
        catch(JSONException e)
        {

            log.throwing(KEY, "toJson", e);
            return new JsonRepresentation(WebUtils.buildErrorJson( "The repostiory was found, however there was an error constructing the return data."));
        }
    }

}
