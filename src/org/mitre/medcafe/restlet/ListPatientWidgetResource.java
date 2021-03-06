/*
 * Copyright 2010 The MITRE Corporation (http://www.mitre.org/). All Rights
 * Reserved.
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

import java.util.ArrayList;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;
import org.mitre.medcafe.model.MedCafeComponent;
import org.mitre.medcafe.util.WebUtils;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class ListPatientWidgetResource extends ServerResource {

	/** The underlying Item object. */
	// Patient item;

	/** The sequence of characters that identifies the resource. */
	String						id;
	String						repository;
	public final static String	KEY	= ListPatientWidgetResource.class.getName();
	public final static Logger	log	= Logger.getLogger(KEY);

	// static{log.setLevel(Level.FINER);}

	// @Get("html")
	public Representation toHtml() {

		StringBuffer startBuf = new StringBuffer();
		StringBuffer patientImages = new StringBuffer();
		StringBuffer endBuf = new StringBuffer();

		// <img src="imgs/cover1.jpg" alt="The Beatles - Abbey Road"/>

		String[] images = new String[] { "assessment.png", "bloodstat.jpg",
				"cardioReport.gif" + "chest-xray.jpg", "chest-xray2.jpg",
				"mri.jpg" };
		String[] imageTitles = new String[] { "Assessment", "Blood Stats",
				"Cardio Report", "Chest XRay", "Chest XRay", "MRI" };
		int i = 0;

		String dir = "patient1";

		for (String image : images) {

			patientImages.append("<img src=\"../" + dir + "/" + image
					+ "\" alt=\"" + imageTitles[i] + "\"/>");
			i++;
		}
		return new StringRepresentation(startBuf.toString()
				+ patientImages.toString() + endBuf.toString());

	}

	// @Get("json")
	public JsonRepresentation toJsonOld() {
		try {

			String server = "";
			String[] widgetName = new String[] { "Details", "Images", "Slider",
					"Editor", "Timeline", "Bookmarks", "Medications",
					"Allergies", "History", "Problem", "Charts", "Dates",
					"SupportInfo", "Immunizations" };
			String[] type = new String[] { "Detail", "Image", "Slider",
					"Editor", "Timeline", "Bookmarks", "Medications",
					"Allergies", "History", "Problem", "Chart", "Date",
					"Support", "Immunizations" };

			String[] images = new String[] { "patient.png", "coverflow.png",
					"slider-small.png", "pages-icon.png", "timeline.png",
					"bookmark.png", "prescription.png", "allergy.jpg",
					"history.png", "problem.png", "chart.png", "date.png",
					"Address_Book.png", "hypo.png" };
			String[] clickUrl = new String[] {
					server + "repository-listJSON.jsp",
					server + "coverflow-flash/index.jsp",
					server + "slider.jsp", "editor.jsp", "timelineJSON.jsp",
					server + "bookmarksJSON.jsp",
					server + "prescriptionJSON.jsp",
					server + "allergyJSON.jsp", server + "historyJSON.jsp",
					server + "problemListJSON.jsp", server + "chart.jsp", "",
					server + "supportListJSON.jsp",
					server + "immunizationJSON.jsp" };

			String[] method = new String[] { "", "", "", "", "", "", "", "",
					"", "", "", "", "", "" };

			String[] repository = new String[] { "OurVista", "", "",
					"OurVista", "OurVista", "OurVista", "OurVista", "OurVista",
					"OurVista", "OurVista", "", "", "OurVista", "OurVista" };

			int i = 0;

			String tempDir = "images/";
			JSONObject obj = new JSONObject();

			for (String widget : widgetName) {

				JSONObject inner_obj = new JSONObject();
				inner_obj.put("id", 1);
				inner_obj.put("name", widget);
				inner_obj.put("image", tempDir + images[i]);
				inner_obj.put("clickURL", clickUrl[i]);
				inner_obj.put("method", method[i]);
				inner_obj.put("type", type[i]);
				inner_obj.put("repository", repository[i]);
				// inner_obj.append("widget", inner_inner_obj);
				obj.append("widgets", inner_obj);  // append creates an array for
													// you
				i++;
			}
			log.finer("ListPatientWidgetResource: " + obj.toString());

			return new JsonRepresentation(obj);
		} catch (Exception e) {
			log.throwing(KEY, "toJson()", e);
			return null;
		}
	}

	@Get("json")
	public JsonRepresentation toJson() {
		try {

			String tempDir = "images/";

			ArrayList<MedCafeComponent> compList = MedCafeComponent
					.retrieveComponents(MedCafeComponent.PATIENT, tempDir);
		
			log.finer("ListPatientWidgetResource JSON general widgets number of components "
					+ compList.size());

			JSONObject obj = new JSONObject();

			for (MedCafeComponent component : compList) {

				JSONObject inner_obj = component.toJSON();

				// inner_obj.append("widget", inner_inner_obj);
				obj.append("widgets", inner_obj);  // append creates an array for
													// you
			}
			log.finer("ListPatientWidgetResource: " + obj.toString());

			return new JsonRepresentation(obj);
		} catch (JSONException je) {
			log.throwing(KEY, "toJson()", je);
			return new JsonRepresentation(
					WebUtils.buildErrorJson("Problem on creation of JSON for component: Error "
							+ je.getMessage()));

		} catch (Exception e) {
			log.throwing(KEY, "toJson()", e);
			return null;
		}
	}
}
