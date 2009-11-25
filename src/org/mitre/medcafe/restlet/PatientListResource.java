package org.mitre.medcafe.restlet;

import java.util.List;

import org.mitre.medcafe.util.Repository;
import org.restlet.resource.ResourceException;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;


public class PatientListResource extends ServerResource {

    /** The sequence of characters that identifies the resource. */
    String repository;

    /**
     *  Grab the information from the url
     */
    @Override
    protected void doInit() throws ResourceException {
        this.repository = (String) getRequest().getAttributes().get("repository");
        System.out.println("Found Repository: " + this.repository);
    }

    /**
     *  Html representation - this will likely have to changes once full integration is done
     */
    @Get("html")
    public Representation toHtml(){
        StringBuilder ret = new StringBuilder( "Available Patients:<br/>\n<ul>" );
        Repository repo = Repositories.getRepository(repository);
        if( repo == null )
        {
            ret.append("<li>That repository does not exist.</li></ul>");
            return new StringRepresentation( ret.toString() );
        }
        List<String> patids = repo.getPatients();
        if( patids == null )
        {
            ret.append("<li>No patients returned.  Contact with the server was likely interrupted.</li></ul>");
            return new StringRepresentation( ret.toString() );
        }
        for( String patid : patids)
        {
            ret.append( "<li>" );
            ret.append( "<a href=\"browsePatient.jsp?id=" + patid + "\">" + patid + "</a>" );
            ret.append( "</li>\n" );
        }
        ret.append( "</ul>" );
        return new StringRepresentation( ret.toString() );
    }
}
