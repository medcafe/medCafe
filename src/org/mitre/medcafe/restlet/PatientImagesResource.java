package org.mitre.medcafe.restlet;

import java.io.IOException;
import java.util.List;

import org.mitre.medcafe.util.Repository;
import org.restlet.resource.ResourceException;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;


public class PatientImagesResource extends ServerResource {

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
    	
    	StringBuffer startBuf = new StringBuffer();
    	StringBuffer patientImages = new StringBuffer();
    	StringBuffer endBuf = new StringBuffer();
    	
    	//<img src="imgs/cover1.jpg" alt="The Beatles - Abbey Road"/>
    	
    	String[] values = new String[]{this.id,"", "", " ", "",
				"", "", " ","","", "" };

    	String[] images = new String[]{"assessment.png","bloodstat.jpg","cardioReport.gif" +
    									"chest-xray.jpg", "chest-xray2.jpg","mri.jpg"};
    	String[] imageTitles = new String[]{"Assessment","Blood Stats","Cardio Report", "Chest XRay", "Chest XRay","MRI" };
    	int i=0;
    	String dir = "patient1";
    		
    	for (String image: images)
    	{
    	
    		patientImages.append("<img src=\"../" + dir +"/" + image + "\" alt=\"" + imageTitles[i] + "\"/>" );	
    		i++;
    	}
    	return new StringRepresentation( startBuf.toString() + patientImages.toString() 
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