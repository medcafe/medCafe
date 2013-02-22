/*
 * Copyright 2010 The MITRE Corporation (http://www.mitre.org/). All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
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
	 * receives all incoming calls.
	 */
	@Override
	public synchronized Restlet createInboundRoot() {
		Router router = new Router(getContext());
	
		// register the data Restlets
		router.attach("/repositories", RepositoryListResource.class);
		router.attach("/repositories/{repository}", RepositoryResource.class);

		// Repository lookups
		router.attach("/repositories/{repository}/lookup/{type}", RepositoryLookupResource.class);
		router.attach("/repositories/{repository}/lookup/{type}/{lookupString}", RepositoryLookupResource.class);
		
		//individual patients
		router.attach("/repositories/{repository}/patients", PatientListResource.class);
		router.attach("/repositories/{repository}/patients/{id}", PatientResource.class);
		router.attach("/repositories/{repository}/patients/{id}/singleRep/{isSingle}", PatientResource.class);
		router.attach("/repositories/{repository}/patients/{id}/images", PatientImagesResource.class);
		router.attach("/repositories/{repository}/patients/{id}/charts/{chartType}", PatientChartResource.class);
		router.attach("/repositories/{repository}/patients/{id}/events", PatientListEventResource.class);
		router.attach("/repositories/{repository}/patients/{id}/allergies", PatientAllergyResource.class);
		router.attach("/repositories/{repository}/patients/{id}/medications", PatientMedicationResource.class);
		router.attach("/repositories/{repository}/patients/{id}/problems", PatientProblemResource.class);
		router.attach("/repositories/{repository}/patients/{id}/bookmarks", PatientBookmarkResource.class);
		router.attach("/repositories/{repository}/patients/{id}/immunizations", PatientImmunizationResource.class);
		router.attach("/repositories/{repository}/patients/{id}/procedures", PatientProcedureResource.class);
		router.attach("/repositories/{repository}/patients/{id}/socialhistory", PatientSocialHistoryResource.class);
		router.attach("/repositories/{repository}/patients/{id}/results", PatientResultsResource.class);
		router.attach("/repositories/{repository}/patients/{id}/encounters", PatientEncounterResource.class);
		router.attach("/repositories/{repository}/patients/{id}/supportList", PatientSupportResource.class);
		router.attach("/repositories/{repository}/patients/{id}/vitals/{choice}", PatientVitalsResource.class);
		router.attach("/repositories/{repository}/patients/{id}/history/{category}", PatientHistoryResource.class);

		// List the available Widgets
		router.attach("/widgets", ListWidgetResource.class);
		router.attach("/widgets/patients", ListPatientWidgetResource.class);
		
		//history info
		router.attach("/history/templates", ListHistoryTemplateResource.class);
		router.attach("/history/templates/patients/{id}", ListHistoryTemplateResource.class);
		
		router.attach("/dates", ListDatesResource.class);

		//this patient resource is outside the individual repository information
		router.attach("/patients/{id}/address", ListAddressResource.class);


		// some unused items
		/* 
		 router.attach("/treenode", TreeNodeResource.class);
		 router.attach("/repositories/{repository}/patients/{id}/history/{category}", PatientHistoryResource.class);
		*/
		
		return router;
	}

}
