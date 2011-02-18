/*
 *  Copyright 2010 The MITRE Corporation (http://www.mitre.org/). All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
        router.attach("/repositories/{repository}", org.mitre.medcafe.restlet.RepositoryResource.class);
        router.attach("/repositories/{repository}/patients", org.mitre.medcafe.restlet.PatientListResource.class);
        router.attach("/repositories/{repository}/patients/{id}", org.mitre.medcafe.restlet.PatientResource.class);
         router.attach("/repositories/{repository}/patients/{id}/singleRep/{isSingle}", org.mitre.medcafe.restlet.PatientResource.class);
        router.attach("/repositories/{repository}/patients/{id}/images", org.mitre.medcafe.restlet.PatientImagesResource.class);
        router.attach("/repositories/{repository}/patients/{id}/charts/{chartType}", org.mitre.medcafe.restlet.PatientChartResource.class);
        router.attach("/repositories/{repository}/patients/{id}/events", org.mitre.medcafe.restlet.PatientListEventResource.class);
        router.attach("/repositories/{repository}/patients/{id}/allergies", org.mitre.medcafe.restlet.PatientAllergyResource.class);
        router.attach("/repositories/{repository}/patients/{id}/medications", org.mitre.medcafe.restlet.PatientMedicationResource.class);
        router.attach("/repositories/{repository}/patients/{id}/problems", org.mitre.medcafe.restlet.PatientProblemResource.class);
        router.attach("/repositories/{repository}/patients/{id}/bookmarks", org.mitre.medcafe.restlet.PatientBookmarkResource.class);
        router.attach("/repositories/{repository}/patients/{id}/immunizations", org.mitre.medcafe.restlet.PatientImmunizationResource.class);
        router.attach("/repositories/{repository}/patients/{id}/encounters", org.mitre.medcafe.restlet.PatientEncounterResource.class);
		  router.attach("/repositories/{repository}/patients/{id}/supportList", org.mitre.medcafe.restlet.PatientSupportResource.class);
		  router.attach("/repositories/{repository}/patients/{id}/vitals/{choice}", org.mitre.medcafe.restlet.PatientVitalsResource.class);
		  router.attach("/repositories/{repository}/patients/{id}/history/{category}", org.mitre.medcafe.restlet.PatientHistoryResource.class);
        //router.attach("/repositories/{repository}/patients/{id}/history/{category}", org.mitre.medcafe.restlet.PatientHistoryResource.class);

        //List the available Widgets
        router.attach("/widgets",org.mitre.medcafe.restlet.ListWidgetResource.class);
        router.attach("/widgets/patients",org.mitre.medcafe.restlet.ListPatientWidgetResource.class);
        router.attach("`",org.mitre.medcafe.restlet.ListPatientWidgetResource.class);
        router.attach("/history/templates",org.mitre.medcafe.restlet.ListHistoryTemplateResource.class);
        router.attach("/history/templates/patients/{id}",org.mitre.medcafe.restlet.ListHistoryTemplateResource.class);
        //List the available Widgets
        router.attach("/dates",org.mitre.medcafe.restlet.ListDatesResource.class);

        router.attach("/patients/{id}/address", org.mitre.medcafe.restlet.ListAddressResource.class);
        // register the view restlets
        //router.attach("/treenode", org.mitre.medcafe.restlet.TreeNodeResource.class);

        return router;
    }

}
