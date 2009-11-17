package firstSteps;

import org.restlet.resource.*;
import org.restlet.representation.*;


/**
 * Resource which has only one representation.
 *
 */
public class HelloWorldResource extends ServerResource {

    @Override
    protected void doInit() throws ResourceException {
        System.out.println("found HelloWorldResource");
        for(Variant v : getVariants())
        {
            System.out.println(String.valueOf(v));
        }

        //setExisting(this.item != null);
    }

    @Get("html")
    public Representation represent() {
        return new StringRepresentation("hello, world");
    }

}
