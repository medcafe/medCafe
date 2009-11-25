package org.mitre.medcafe.restlet;

import java.io.IOException;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.ext.xml.DomRepresentation;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class RepositoryResource extends ServerResource {

    /** The underlying Item object. */
    //Patient item;

    /** The sequence of characters that identifies the resource. */
    String id;
    String repository;

    @Override
    protected void doInit() throws ResourceException {
        // Get the "id" attribute value taken from the URI template
        // /items/{id}.
        this.id = (String) getRequest().getAttributes().get("id");
        this.repository = (String) getRequest().getAttributes().get("repository");
        // Get the item directly from the "persistence layer".
        //this.item = getItems().get(id);
        System.out.println("Found RepositoryResource");
        for(Variant v : getVariants())
        {
            System.out.println(String.valueOf(v));
        }

        //setExisting(this.item != null);
    }

    @Get("html")
    public Representation toHtml(){
        return new StringRepresentation( "<li style=\"position: static; clear: none; z-index: auto; opacity: 1; left: auto; top: auto;\" class=\"widget color-yellow\">" +
            "<div style=\"cursor: move;\" class=\"widget-head\"><a style=\"\" href=\"#\" class=\"collapse\">COLLAPSE</a>" +
                "<h3>Patient Data</h3>" +
                "<a href=\"#\" class=\"remove\">CLOSE</a><a href=\"#\" class=\"edit\">EDIT</a>" +
            "</div>" +
            "<div class=\"edit-box\" style=\"display: none;\">" +
                "<ul><li class=\"item\"><label>Change the title?</label><input value=\"Widget title\"></li></ul><li class=\"item\"><label>Available colors:</label><ul class=\"colors\"><li class=\"color-yellow\"></li><li class=\"color-red\"></li><li class=\"color-blue\"></li><li class=\"color-white\"></li><li class=\"color-orange\"></li><li class=\"color-green\"></li></ul></li>" +
            "</div>" +
            "<div style=\"display: block;\" class=\"widget-content\">" +
                "<p>" +
                    "Patient ID: "+this.id+"<br/>" +
                    "Name: <br/>" +
                    "Address: <br/>" +
                    "Phone numbers: <br/>" +
                    "Gender: <br/>" +
                    "Languages: <br/>" +
                    "Birthdate: <br/>" +
                    "Maritial Status: <br/>" +
                    "Race: <br/>" +
                    "Guardian: <br/>" +
                    "Birth place: <br/>" +
                "</p>" +
            "</div>" +
        "</li>" );
    }


}
