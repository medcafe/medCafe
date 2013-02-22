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

import java.util.Map;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;
import org.mitre.medcafe.util.Repository;
import org.mitre.medcafe.util.WebUtils;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;


public class RepositoryListResource extends ServerResource {

    public final static String KEY = RepositoryListResource.class.getName();
    public final static Logger log = Logger.getLogger( KEY );
    // static{log.setLevel(Level.FINER);}

    @Get("html")
    public Representation toHtml(){
        StringBuilder ret = new StringBuilder( "Available Repositories:<br/>\n<ul>" );
        for( String name : Repositories.getRepositoryNames())
        {
            ret.append( "<li>" );
            ret.append( "<a href=\"browseRepository.jsp?repo=" + name + "\">" + name + "</a>" );
            ret.append( "</li>\n" );
        }
        ret.append( "</ul> <br/><br/>Offline repositories: <br/><ul>" );
        
        for( String name : Repositories.getOfflineRepositoryNames()){
            ret.append( "<li>" );
            ret.append( name );
            ret.append( "</li>\n" );    
        }
        ret.append( "</ul>" );

        return new StringRepresentation( ret.toString() );
    }

    @Get("json")
    public JsonRepresentation toJson(){
        try
        {

            Map<String, Repository> reps = Repositories.getRepositories();
            if( reps == null )
            {
                return new JsonRepresentation(WebUtils.buildErrorJson( "No repositories currently exist."));
            }

            JSONObject obj = new JSONObject();
            for(Repository r: Repositories.getRepositories().values() )
            {
                JSONObject inner_obj = new JSONObject ();
                inner_obj.put("name", r.getName());
                inner_obj.put("type", r.getType());
                obj.append("repositories", inner_obj);  //append creates an array for you
            }
            return new JsonRepresentation(obj);

        }
        catch(JSONException e)
        {

            log.throwing(KEY, "toJson", e);
            return new JsonRepresentation(WebUtils.buildErrorJson( "The repositories were found, however there was an error constructing the return data."));
        }
        catch(Exception e)
        {
            log.throwing(KEY, "toJson()", e);
            return null;
        }
    }

    //public Representation handle()
    /*{
        log.finer(getClientInfo().getAgent());
        log.finer(getClientInfo().getAgentName());
        List<Preference<MediaType>> mediaTypes = getClientInfo().getAcceptedMediaTypes();
        for(Preference<MediaType> pref : mediaTypes)
            log.finer( String.valueOf(pref) );
        log.finer(getPreferredVariant(getVariants()));
        return super.handle();
    }*/
}
