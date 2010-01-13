package org.mitre.medcafe.restlet;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

public class MedcafeApplication extends Application {

    /**
     * Creates a root Restlet that will receive all incoming calls.
     */
    @Override
    public synchronized Restlet createInboundRoot() {
        // Create a router Restlet that routes each call to a
        // new instance of HelloWorldResource.
        Router router = new Router(getContext());

        // register the data Restlets
        router.attach("/repositories", org.mitre.medcafe.restlet.RepositoryListResource.class);
        router.attach("/repositories/{repository}/patients", org.mitre.medcafe.restlet.PatientListResource.class);
        router.attach("/repositories/{repository}/patients/{id}", org.mitre.medcafe.restlet.PatientResource.class);
        router.attach("/repositories/{repository}/patients/{id}/images", org.mitre.medcafe.restlet.PatientImagesResource.class);
        router.attach("/repositories/{repository}/patients/{id}/charts/{chartType}", org.mitre.medcafe.restlet.PatientChartResource.class);
        
        //List the available Widgets
        router.attach("/widgets",org.mitre.medcafe.restlet.ListWidgetResource.class);
        //List the available Widgets
        router.attach("/dates",org.mitre.medcafe.restlet.ListDatesResource.class);
        
        // register the view restlets
        router.attach("/treenode", org.mitre.medcafe.restlet.TreeNodeResource.class);

        return router;
    }

}
