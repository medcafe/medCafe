package org.mitre.medcafe.restlet;

import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;


public class RepositoryListResource extends ServerResource {

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
        StringBuilder ret = new StringBuilder( "{ \"names\" : [ " );
        boolean first = true;
        for( String name : Repositories.getRepositoryNames())
        {
            if( first )
            {
                ret.append( "\"" + name + "\"");
                first = false;
            }
            else
            ret.append( ", \"" + name + "\"");
        }
        ret.append( " ] }" );
        // String object = "{ \"names\" : [  \"One\",  \"Two\", \"Three\" ] }";

        return new JsonRepresentation( ret.toString() );
    }
}
