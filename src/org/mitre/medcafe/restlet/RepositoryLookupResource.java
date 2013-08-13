/*
 *  Copyright 2011 The MITRE Corporation (http://www.mitre.org/). All Rights Reserved.
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

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import  org.mitre.medcafe.util.*;

import java.io.IOException;
import org.json.*;
import org.restlet.ext.json.*;
import org.restlet.representation.*;
import org.restlet.resource.*;

public class RepositoryLookupResource extends ServerResource {

    public final static String KEY = RepositoryLookupResource.class.getName();
    public final static Logger log = Logger.getLogger( KEY );
    // static{log.setLevel(Level.FINER);}

    /** The underlying Item object. */
    //Patient item;

    /** The sequence of characters that identifies the resource. */
 	 String type;
    String repository;
    String lookupString;

    @Override
    protected void doInit() throws ResourceException {
        this.repository = (String) getRequest().getAttributes().get("repository");
        this.type = (String) getRequest().getAttributes().get("type");
        this.lookupString = (String) getRequest().getAttributes().get("lookupString");
        if (lookupString==null)
        {
        	   lookupString = "";
        }
    }

    @Get("json")
    public JsonRepresentation toJson(){
        Repository r = Repositories.getRepository( repository );
        if( r == null )
        {
            return new JsonRepresentation(WebUtils.buildErrorJson( "A repository named " + repository + " does not exist."));
        }
        String typeName = type+"List";
		Collection<String> objects =  r.lookup(type, lookupString); 
		if (r==null)
		{
			return new JsonRepresentation(WebUtils.bundleJsonResponseObject(typeName, "null"));
		}
		return  new JsonRepresentation(WebUtils.bundleJsonResponseObject( typeName, objects ));

 
       

    }

}
