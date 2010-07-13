package org.mitre.medcafe.restlet;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.*;

import org.json.JSONObject;
import org.mitre.medcafe.util.Repository;
import org.mitre.medcafe.util.WebUtils;
import org.restlet.data.Form;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

public class PatientListResource extends ServerResource {

    public final static String KEY = PatientListResource.class.getName();
    public final static Logger log = Logger.getLogger( KEY );
    // static{log.setLevel(Level.FINER);}

    private final static String FIRST_NAME = "first_name";
    private final static String LAST_NAME = "last_name";
    private final static String MIDDLE_INITIAL = "middle_initial";
    
    /** The sequence of characters that identifies the resource. */
    private String repository;

    /* If any info given on name of patient - use these attributes for searching for patient Ids*/
    
    private String firstName;
    private String lastName;
    private String middleInitial;
    /**
     *  Grab the information from the url
     */
    @Override
    protected void doInit() throws ResourceException {
        this.repository = (String) getRequest().getAttributes().get("repository");
        
        Form form = getRequest().getResourceRef().getQueryAsForm();
        firstName = form.getFirstValue(FIRST_NAME);
        lastName = form.getFirstValue(LAST_NAME);
        middleInitial = form.getFirstValue(MIDDLE_INITIAL);
         
        System.out.println("Found Repository: " + this.repository);
    }

    @Get("json")
    public JsonRepresentation toJson(){
        Repository r = Repositories.getRepository( repository );
        if( r == null )
        {
            return new JsonRepresentation(WebUtils.buildErrorJson( "A repository named " + repository + " does not exist."));
        }

        Map<String, String> patients = new HashMap<String, String>();
        boolean nameCriteria = false;
        if ( (firstName != null) || (lastName != null) || (middleInitial != null) )
        {
        	nameCriteria = true;
        	patients = r.getPatientByName(lastName, firstName, middleInitial );
        }
        else
        {
        	patients = r.getPatients( );
        }
        
        if( patients == null )
        {
            return new JsonRepresentation(WebUtils.buildErrorJson( "Could not establish a connection to the repository " + repository + " at this time."));
        }

        if( patients.size() == 0)
        {
        	String err_mess =  "There are no patients currently listed for  " + repository;
        	if (nameCriteria)
        		err_mess =  "There are no patients currently listed for  " + repository + " with family name " + 
        					lastName + " given name " + firstName + " and middle initial " + middleInitial;
        	
            return new JsonRepresentation(WebUtils.buildErrorJson(err_mess ));
        }
        //convert to JSON
        JSONObject ret = new JSONObject();
        try
        {
            ret.put("repository", repository );
            for( String key : patients.keySet() )
            {
                JSONObject o = new JSONObject();
                o.put("id", key);
                o.put("name", patients.get(key));
                ret.append("patients", o);
            }
            return new JsonRepresentation( ret );
        }
        catch(org.json.JSONException e)
        {
            log.throwing(KEY, "toJson()", e);
            return new JsonRepresentation("{\"error\": \""+e.getMessage()+"\"}");
        }
    }


    /**
     *  Html representation - this will likely have to changes once full integration is done
     */
    @Get("html")
    public Representation toHtml()
    {


    	StringBuffer buf = new StringBuffer();
    	StringBuffer endBuf = new StringBuffer();
    	endBuf.append("</tbody></table>");

    	StringBuilder ret = new StringBuilder( "Available Patients:<br/>\n<ul>" );
        Repository repo = Repositories.getRepository(repository);

        if( repo == null )
        {
            ret.append("<li>That repository does not exist.</li></ul>");
            return new StringRepresentation( ret.toString() );
        }
        buf.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"example" + repo.getName()+"\">");
    	buf.append("");

        StringBuilder patients = new StringBuilder();

        patients.append("<thead><tr><th></th></tr></thead>");
        patients.append("<tbody>");

        Map<String,String> patids = repo.getPatients();

        if( patids == null )
        {
            ret.append("<li>No patients returned.  Contact with the server was likely interrupted.</li></ul>");
            return new StringRepresentation( ret.toString() );
        }
        for( String patid : patids.keySet())
        {
        	System.out.println("PatientListResource: toHtml : paient id " + patid);

        	patients.append("<tr class=\"gradeX\"><td><span class=\"summary\"><a href=\"#\" class=\"details\">"+ patid+ "</a></span></td><td>"+patids.get(patid) + "</td></tr>" );
        	//patients.append("<tr class=\"gradeX\"><td>"+ patid+ "</td></tr>" );

        }

        return new StringRepresentation( buf.toString() + patients.toString()
                + endBuf.toString());
    }
}
