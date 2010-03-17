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
    static{log.setLevel(Level.FINER);}

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
