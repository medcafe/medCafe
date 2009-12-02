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


public class PatientResource extends ServerResource {

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
        System.out.println("Found PatientResource");
        for(Variant v : getVariants())
        {
            System.out.println(String.valueOf(v));
        }

        //setExisting(this.item != null);
    }

    /**
     * Handle DELETE requests.
     */
    @Delete
    public void removeItem() {
        // if (item != null) {
        //     // Remove the item from the list.
        //     getItems().remove(item.getName());
        // }

        // // Tells the client that the request has been successfully fulfilled.
        // setStatus(Status.SUCCESS_NO_CONTENT);
    }

    /**
     * Handle PUT requests.
     *
     * @throws IOException
     */
    @Put
    public void storeItem(Representation entity) throws IOException {
        // // The PUT request updates or creates the resource.
        // if (item == null) {
        //     item = new Item(id);
        // }

        // // Update the description.
        // Form form = new Form(entity);
        // item.setDescription(form.getFirstValue("description"));

        // if (getItems().putIfAbsent(item.getName(), item) == null) {
        //     setStatus(Status.SUCCESS_CREATED);
        // } else {
        //     setStatus(Status.SUCCESS_OK);
        // }
    }

    @Get("html")
    public Representation toHtml(){
    	
    	StringBuffer buf = new StringBuffer();
    	StringBuffer endBuf = new StringBuffer();
    	//endBuf.append("</div></li>");
    	
    	/*buf.append("<li style=\"position: static; clear: none; z-index: auto; opacity: 1; left: auto; top: auto;\" class=\"widget color-yellow\">" +
            "<div style=\"cursor: move;\" class=\"widget-head\"><a style=\"\" href=\"#\" class=\"collapse\">COLLAPSE</a>" +
                "<h3>Patient Data</h3>" +
                "<a href=\"#\" class=\"remove\">CLOSE</a><a href=\"#\" class=\"edit\">EDIT</a>" +
            "</div>" +
            "<div class=\"edit-box\" style=\"display: none;\">" +
                "<ul><li class=\"item\"><label>Change the title?</label><input value=\"Widget title\"></li></ul><li class=\"item\"><label>Available colors:</label><ul class=\"colors\"><li class=\"color-yellow\"></li><li class=\"color-red\"></li><li class=\"color-blue\"></li><li class=\"color-white\"></li><li class=\"color-orange\"></li><li class=\"color-green\"></li></ul></li>" +
            "</div>");
    	buf.append("<div style=\"display: block;\" class=\"widget-content\">");*/
        
    	buf.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"example" + this.id +"\">");
    	buf.append("");
    	
    	String[] titles = new String[]{"Patient ID","Name", "Address", "Phone numbers", "Gender",
    									"Languages", "Birthdate", "Maritial Status","Race","Guardian", "Birth place" };
    	
    	String[] values = new String[]{this.id,"", "", " ", "",
				"", "", " ","","", "" };

    	StringBuffer patientData = new StringBuffer();
    	patientData.append("<thead><tr><th></th><th></th></tr></thead>");
    	patientData.append("<tbody>");
    	int i=0;
    	for (String title:titles)
    	{
    		patientData.append("<tr class=\"gradeX\"><td>" + title + "</td><td>" + values[i]+ "</td></tr>" );
    		i++;
    	}
    	
    	endBuf.append("</tbody></table>");
    	
    	/*patientData.append("<p>" +
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
            "</p>" );*/
    	
    	return new StringRepresentation( buf.toString() + patientData.toString() 
                 + endBuf.toString()); 
             
    }

    @Get("xml")
    public Representation toXml() {
        // try {
        //     DomRepresentation representation = new DomRepresentation(
        //             MediaType.TEXT_XML);
        //     // Generate a DOM document representing the item.
        //     Document d = representation.getDocument();

        //     Element eltItem = d.createElement("item");
        //     d.appendChild(eltItem);
        //     Element eltName = d.createElement("name");
        //     eltName.appendChild(d.createTextNode(item.getName()));
        //     eltItem.appendChild(eltName);

        //     Element eltDescription = d.createElement("description");
        //     eltDescription.appendChild(d.createTextNode(item.getDescription()));
        //     eltItem.appendChild(eltDescription);

        //     d.normalizeDocument();

        //     // Returns the XML representation of this document.
        //     return representation;
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }

        return null;

    }

/*     @Get("json")
    public JsonRepresentation representAsJson(){
        return null;
        //return XML.toJSONObject("…");
    } */


}
