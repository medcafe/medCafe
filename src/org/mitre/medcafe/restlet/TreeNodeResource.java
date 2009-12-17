package org.mitre.medcafe.restlet;

import java.util.*;
import java.util.logging.*;
import org.json.*;
import org.mitre.medcafe.util.*;
import org.restlet.data.*;
import org.restlet.ext.json.*;
import org.restlet.representation.*;
import org.restlet.resource.*;


public class TreeNodeResource extends ServerResource {

    public final static String KEY = TreeNodeResource.class.getName();
    public final static Logger log = Logger.getLogger( KEY );
    static{log.setLevel(Level.FINER);}

    protected String view = "plain";
    protected List<String> supportedViews = Arrays.asList( "plain", "link", "array" );

    protected String relurl = Config.EMPTY_STR;

    @Override
    protected void doInit() throws ResourceException {
        // Get the "type" attribute value taken from the URI template
        Form form = getRequest().getResourceRef().getQueryAsForm();
        String whatToShow = form.getFirstValue("type");
        if( whatToShow != null && supportedViews.contains(whatToShow) )
        {
            view = whatToShow;
        }
        log.finer( "Formatting as " + view );
        relurl = form.getFirstValue("relurl");
        log.finer( "relurl retrieved is " + relurl );
    }

    @Get("html")
    public Representation toHtml(){
        try
        {
            if( relurl == null )
            {
                return new StringRepresentation( "<li id=\"treeme\"><span class=\"folder\">No resource idenitifed</span></li>\n" );
            }
            ClientResource resource = new ClientResource( "http://127.0.0.1:8080/medcafe/c" + relurl );

            Representation answer = resource.get(MediaType.APPLICATION_JSON);
            if (resource.getStatus().isSuccess() && resource.getResponseEntity().isAvailable()) {
                JsonRepresentation response = new JsonRepresentation(answer);
                // TODO - add calls to formatting class and determine type of data
                String format = Config.getFormat(relurl);
                /* check for custom formatting */
                if( format == null )
                {
                    return defaultFormat( response );
                }
                /* must have custom formatting configured.  Use it. */
                return customFormat( response );
            }
            else
            {
                log.warning( "Resource was not reachable" );
                log.warning( "\tFound resource?  " + resource.getStatus().isSuccess() );
                return new StringRepresentation( "<li id=\"treeme\"><span class=\"folder\">Resource was not reachable</li>" );
            }
            // return new StringRepresentation( ret.toString() );
        }
        catch (Exception e)
        {
            log.throwing( KEY, "toHtml()", e );
            return  new StringRepresentation(" <li id=\"treeme\"><span class=\"folder\">An error occurred and access to the requestion information failed.</li> ");
        }
    }

    /**
     *  uses the custom formatting to craft the response
     */
    protected Representation customFormat( JsonRepresentation representation )
        throws JSONException
    {
        StringBuilder ret = new StringBuilder();
        JSONArray a = representation.getJsonObject().getJSONArray( "names" );
        for(int i = 0; i < a.length(); i++)
        {
            ret.append( "<li id=\"treeme\"><span class=\"folder\">" + String.valueOf(a.get(i))  + "</span></li>\n" );
        }
        return new StringRepresentation( ret.toString() );
    }

    protected Representation defaultFormat( JsonRepresentation representation )
    {
        return new StringRepresentation("<li id=\"treeme\"><span class=\"folder\">Default formatting not implemented yet.</li>");
    }
}
