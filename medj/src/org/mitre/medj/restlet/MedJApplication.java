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
package org.mitre.medj.restlet;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

public class MedJApplication extends Application {

    /**
     * Creates a root Restlet that will receive all incoming calls.
     */
    @Override
    public synchronized Restlet createInboundRoot() {
        // Create a router Restlet that routes each call to a
        // new instance of HelloWorldResource.
        Router router = new Router(getContext());

        // register the data Restlets
        router.attach("/patients", org.mitre.medj.restlet.PatientsListResource.class);
        router.attach("/patients/{id}",org.mitre.medj.restlet.PatientListResource.class);
        router.attach("/patients/{id}/medications",org.mitre.medj.restlet.PatientMedicationResource.class);
        router.attach("/patients/{id}/alerts",org.mitre.medj.restlet.PatientAllergyResource.class);
        router.attach("/patients/{id}/immunizations",org.mitre.medj.restlet.PatientImmunizationResource.class);
        router.attach("/patients/{id}/family",org.mitre.medj.restlet.PatientFamilyHistoryResource.class);
        router.attach("/patients/{id}/procedure",org.mitre.medj.restlet.PatientProcedureResource.class);
        router.attach("/patients/{id}/vitalSigns",org.mitre.medj.restlet.PatientVitalSignsResource.class);
        router.attach("/patients/{id}/problems",org.mitre.medj.restlet.PatientProblemsResource.class);
        router.attach("/patients/{id}/socialHistory",org.mitre.medj.restlet.PatientSocialHistoryResource.class);
        router.attach("/patients/{id}/results", org.mitre.medj.restlet.PatientResultsResource.class);
         
        
        return router;
    }

}
