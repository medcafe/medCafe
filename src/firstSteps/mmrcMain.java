package firstSteps;

import org.restlet.*;
import org.restlet.data.*;

public class mmrcMain {
    public static void main(String [] args) {
        try {
            // Create a new Component.
            Component component = new Component();

            // Add a new HTTP server listening on port 8182.
            component.getServers().add(Protocol.HTTP, 8182);
            component.getClients().add(Protocol.FILE);

            // Attach the sample application.
            component.getDefaultHost().attach("/first", new FirstStepsApplication());

            // Create an application
            Application application = new Application() {
                @Override
                public Restlet createRoot() {
                    return new Directory(getContext(), "file:///home/jchoyt/devel/mmrc/html/");
                }
            };

            // Attach the application to the component and start it
            component.getDefaultHost().attach("", application);

            // Start the component.
            component.start();
        } catch (Exception e) {
            // Something is wrong.
            e.printStackTrace();
        }
    }
}
