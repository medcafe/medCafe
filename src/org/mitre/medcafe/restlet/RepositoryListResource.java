package org.mitre.medcafe.restlet;

import org.mitre.medcafe.util.*;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.*;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;


public class RepositoryListResource extends ServerResource {

    public final static String KEY = RepositoryListResource.class.getName();
    public final static Logger log = Logger.getLogger( KEY );
    static{log.setLevel(Level.FINER);}

    @Get("html")
    public Representation toHtml(){
        StringBuilder ret = new StringBuilder( "Available Repositories:<br/>\n<ul>" );
        for( String name : Repositories.getRepositoryNames())
        {
            ret.append( "<li>" );
            ret.append( "<a href=\"browseRepository.jsp?repo=" + name + "\">" + name + "</a>" );
            ret.append( "</li>\n" );
        }
        ret.append( "</ul>" );
        return new StringRepresentation( ret.toString() );
    }

    @Get("json")
    public JsonRepresentation toJson(){
        try
        {
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
        catch(Exception e)
        {
            log.throwing(KEY, "toJson()", e);
            return null;
        }
    }
}
