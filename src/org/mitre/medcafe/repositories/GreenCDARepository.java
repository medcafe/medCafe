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
package org.mitre.medcafe.repositories;

// import org.mitre.hdata.hrf.core.*;

import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import javax.xml.datatype.DatatypeFactory;

import org.hl7.greencda.c32.Allergy;
import org.hl7.greencda.c32.Code;
import org.hl7.greencda.c32.Condition;
import org.hl7.greencda.c32.Encounter;
import org.hl7.greencda.c32.Immunization;
import org.hl7.greencda.c32.Interval;
import org.hl7.greencda.c32.Medication;
import org.hl7.greencda.c32.Person;
import org.hl7.greencda.c32.PersonalName;
import org.hl7.greencda.c32.Procedure;
import org.hl7.greencda.c32.Result;
import org.hl7.greencda.c32.SocialHistory;
import org.hl7.greencda.c32.Support;
import org.mitre.medcafe.model.Patient;
import org.mitre.medcafe.util.Constants;
import org.mitre.medcafe.util.GreenCDAFeedParser;
import org.mitre.medcafe.util.NotImplementedException;
import org.mitre.medcafe.util.Repository;
import org.mitre.medcafe.util.WebUtils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.medsphere.fileman.FMRecord;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndLinkImpl;
import com.sun.syndication.io.SyndFeedInput;

/**
 * This class implements an interface to a back-end Vista repository. The
 * Medsphere (http://www.medsphere.com/) version of VistA, named OpenVista, is
 * used via the medsphere ovid library
 */
public class GreenCDARepository extends Repository {

	public final static String		KEY				= GreenCDARepository.class
															.getName();
	public final static Logger		log				= Logger.getLogger(KEY);
	private GreenCDARepository		cda;
	private TreeMap<Date, Result>	vitalTree		= null;

	private String					userName		= "guest";
	private GreenCDAFeedParser		gcda			= new GreenCDAFeedParser();
	public static String			greenCDADataUrl	= "";

	// Time is given in seconds not milliseconds
	public static final boolean		isMillis		= false;

	// For test purposes
	public GreenCDARepository(String baseUrl) {
		greenCDADataUrl = baseUrl;
	}

	public GreenCDARepository(HashMap<String, String> credMap) {
		super(credMap);
		// TODO Auto-generated constructor stub
		type = "greenCDA";
	}

	@Override
	public List<Result> getAllVitals(String patientId) {

		String server = greenCDADataUrl + "/records/" + patientId;

		List<Result> vitals = new ArrayList<Result>();
		Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		try {
			List<String> results = gcda.findHealthDetail(patientId,
					"vital_signs");
			DatatypeFactory factory = DatatypeFactory.newInstance();
            
			for (String url : results) {
				server = greenCDADataUrl + url;

				String vitalSignsResults = WebUtils.callServer(server, "GET",
						"application/json", new String[] {});

				JsonObject o = parser.parse(vitalSignsResults)
						.getAsJsonObject();
				Result record = gson.fromJson(o, Result.class);
				String time = record.getTime();
				record.setTime(parseDate(time, isMillis));
				
				//Set up the interval time
				Interval inter = getInterval(factory, time);
				record.setEffectiveTime(inter);
				vitals.add(record);
			}
			Collections.sort(vitals, new NewVitalsComparable());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vitals;
	}

	private Interval getInterval(DatatypeFactory factory, String time)
	{
		Interval inter = new Interval();
		Date setTime = getDateObj(time, isMillis);
		GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(setTime);
        inter.setValue(factory.newXMLGregorianCalendar(cal));
        return inter;
	}
	// this 'lil nested class is a cheat to cheapen the complexity of the code
	// for
	// getting the "latest" vitals. Simply sort, then split the list.
	public class NewVitalsComparable implements Comparator<Result> {
		@Override
		public int compare(Result o1, Result o2) {
			return o2.getEffectiveTime().getValue()
					.compare(o1.getEffectiveTime().getValue());
		}
	}

	@Override
	public List<Result> getLatestVitals(String id) {
		List<Result> vitals = this.getAllVitals(id);
		Collections.sort(vitals, new NewVitalsComparable());
		vitals.subList(0, 4);
		return vitals;
	}

	@Override
	public List<Allergy> getAllergies(String patientId) {
		// TODO Auto-generated method stub
		List<Allergy> allergies = new ArrayList<Allergy>();

		Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		try {
			List<String> allergyResults = gcda.findHealthDetail(patientId,
					"allergies");
			for (String allergyUrl : allergyResults) {
				String server = greenCDADataUrl + allergyUrl;
				String tempResults = WebUtils.callServer(server, "GET",
						"application/json", new String[] {});
				System.out
						.println("GreenCDARepository getAllergies results from json "
								+ tempResults);

				JsonObject o = parser.parse(tempResults).getAsJsonObject();
				Allergy allergy = gson.fromJson(o, Allergy.class);
				String time = allergy.getTime();
				allergy.setTime(parseDate(time, isMillis));

				allergies.add(allergy);
			}

			/*
			 * } catch (IOException e) {
			 * // TODO Auto-generated catch block
			 * e.printStackTrace();
			 */
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return allergies;
	}

	@Override
	public List<Immunization> getImmunizations(String id)
			throws NotImplementedException {
		List<Immunization> vax = new ArrayList<Immunization>();

		String server = greenCDADataUrl + "/records/" + id;

		Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		try {
			List<String> results = gcda.findHealthDetail(id, "immunizations");
			for (String url : results) {
				server = greenCDADataUrl + url;

				String jsonResults = WebUtils.callServer(server, "GET",
						"application/json", new String[] {});

				JsonObject o = parser.parse(jsonResults).getAsJsonObject();
				Immunization immunization = gson
						.fromJson(o, Immunization.class);
				String time = immunization.getTime();
				immunization.setTime(parseDate(time, isMillis));

				vax.add(immunization);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vax;
	}

	@Override
	public List<Result> getResults(String patientId) {
		// TODO Auto-generated method stub

		List<Result> resultList = new ArrayList<Result>();

		String server = greenCDADataUrl + "/records/" + patientId;

		Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		try {
			DatatypeFactory factory = DatatypeFactory.newInstance();
	          
			List<String> results = gcda.findHealthDetail(patientId, "results");
			for (String url : results) {
				server = greenCDADataUrl + url;

				String jsonResults = WebUtils.callServer(server, "GET",
						"application/json", new String[] {});

				JsonObject o = parser.parse(jsonResults).getAsJsonObject();
				Result result = gson.fromJson(o, Result.class);
				String time = result.getTime();
				result.setTime(parseDate(time, isMillis));

				Interval inter = getInterval(factory, time);
				result.setEffectiveTime(inter);
				
				resultList.add(result);
			}
			Collections.sort(resultList, new NewVitalsComparable());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}

	@Override
	public List<Medication> getMedications(String patientId) {

		String server = "";

		List<Medication> meds = new ArrayList<Medication>();
		Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		try {
			List<String> medResults = gcda.findHealthDetail(patientId,
					"medications");
			for (String medUrl : medResults) {
				server = greenCDADataUrl + medUrl;
				String tempResults = WebUtils.callServer(server, "GET",
						"application/json", new String[] {});
				System.out
						.println("GreenCDARepository getMedications results from json "
								+ tempResults);

				JsonObject o = parser.parse(tempResults).getAsJsonObject();
				Medication med = gson.fromJson(o, Medication.class);
				String time = med.getTime();
				String startTime = med.getStart_time();

				med.setTime(parseDate(time, isMillis));
				med.setStart_time(parseDate(startTime, isMillis));
				meds.add(med);
			}

			Medication testMed = new Medication();
			testMed.setId("TestMed");
			testMed.setStatus("Active");
			Code delivMeth = new Code();
			delivMeth.setCode("By Mouth");
			testMed.setDeliveryMethod(delivMeth);

			Code code = new Code();
			code.setCode("Oral");
			testMed.setRoute(code);
			testMed.setFreeText("Free Text Value");

			String testJson = gson.toJson(testMed);
			Medication returnMed = gson.fromJson(testJson, Medication.class);
			// meds.add(returnMed);

			/*
			 * } catch (IOException e) {
			 * // TODO Auto-generated catch block
			 * e.printStackTrace();
			 */
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return meds;

	}

	@Override
	public String downloadPhoto(String patientId) throws Exception{
		String repo_url = greenCDADataUrl + "/assets/thumbnails/" + patientId
				+ ".jpg";			
		
		String photo_path_url = "pds_img/" + patientId + ".jpg";
		String real_photo_path =  Constants.BASE_PATH + "images/patients/" + photo_path_url;
		log.info("Writing photo to " + real_photo_path);
		URL website = new URL(repo_url);
		ReadableByteChannel rbc = Channels.newChannel(website.openStream());
		FileOutputStream fos = new FileOutputStream(real_photo_path);
		fos.getChannel().transferFrom(rbc, 0, 1 << 24);  //reads at-most 16MB
		return photo_path_url;
	}

	@Override
	public Person getPatient(String userName, String patientId) {
		this.userName = userName;
		Person patient = new Person();
		try {
			// List<String> patientResults = gcda.findPatient(firstName,
			// lastName, url);

			Gson gson = new Gson();
			JsonParser parser = new JsonParser();
			String url = greenCDADataUrl + "/records/" + patientId + "/c32";
			List<String> patientUrls = null;

			List<SyndEntry> patientEntries = gcda.parseAtom(url);

			for (SyndEntry patientEntry : patientEntries) {
				/*
				 * <entry>
				 * <id>3</id>
				 * <title>Smith59, Tom</title>
				 * <link href="http://localhost:3000/records/3/c32/3"/>
				 * </entry>
				 */

				name = patientEntry.getTitle();
				PersonalName personName = new PersonalName();
				String[] nameParts = name.split(",");
				personName.setGivenName(nameParts[0]);
				personName.setFamilyName(nameParts[1]);
				patient.setName(personName);
			}
			// If need to get demographics
			/*
			 * String patientUrl ="";
			 * if (patientUrls.size() > 0)
			 * {
			 * patientUrl = patientUrls.get(0);
			 * String server = greenCDADataUrl + patientUrl;
			 * String patientResults = WebUtils.callServer(server, "GET",
			 * "application/json", new String[]{});
			 * JsonObject o = parser.parse(patientResults).getAsJsonObject();
			 * patient = gson.fromJson(o, Person.class);
			 * }
			 */
			return patient;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return patient;
	}

	@Override
	public Map<String, String> getPatientByName(String family, String given,
			String middle) {
	
		Map<String, String> ret = new HashMap<String, String>();
		this.userName = userName;
		// Lookup patient id in DB
		try {
			String url = greenCDADataUrl + "/records";
			List<SyndEntry> patientEntries = gcda.findPatientEntries(given,
					family, url);

			System.out.println("GreenCDARepository : getPatientByName size "
					+ patientEntries.size());

			SyndFeedInput input = new SyndFeedInput();
			SyndFeed feed = null;
			for (SyndEntry patientEntry : patientEntries) {
				String name = patientEntry.getTitle();

				List<SyndLinkImpl> patientLinks = gcda
						.findPatientLinks(patientEntry);
				for (SyndLinkImpl patientLink : patientLinks) {
					if (patientLink.getType().equalsIgnoreCase(
							gcda.ATOM_LINK_TYPE)) {
						String patientLinkUrl = patientLink.getHref();
						// href="http://localhost:3000/records/5"
						String id = patientLinkUrl.substring(patientLinkUrl
								.lastIndexOf("/"));
						ret.put(id, name);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	@Override
	public List<Encounter> getPatientEncounters(String id)
			throws NotImplementedException {

		String type = "encounters";
		List<Encounter> encounters = new ArrayList<Encounter>();

		String server = greenCDADataUrl + "/records/" + id;

		Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		try {
			List<String> results = gcda.findHealthDetail(id, type);
			for (String url : results) {
				server = greenCDADataUrl + url;

				String jsonResults = WebUtils.callServer(server, "GET",
						"application/json", new String[] {});

				JsonObject o = parser.parse(jsonResults).getAsJsonObject();
				Encounter encounter = gson.fromJson(o, Encounter.class);
				String time = encounter.getTime();
				encounter.setTime(parseDate(time, isMillis));

				encounters.add(encounter);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return encounters;
	}

	@Override
	public Map<String, String> getPatients() {

		Map<String, String> ret = new HashMap<String, String>();
		this.userName = userName;
		// Lookup patient id in DB
		try {
			String url = greenCDADataUrl + "/records";
			System.out.println("GreenCDARepository : url " + url);

			List<SyndEntry> patientEntries = gcda.findAllPatientEntries(url);

			System.out.println("GreenCDARepository : getPatientByName size "
					+ patientEntries.size());

			SyndFeedInput input = new SyndFeedInput();
			SyndFeed feed = null;
			for (SyndEntry patientEntry : patientEntries) {
				String name = patientEntry.getTitle();

				List<SyndLinkImpl> patientLinks = gcda
						.findPatientLinks(patientEntry);
				for (SyndLinkImpl patientLink : patientLinks) {
					if (patientLink.getType().equalsIgnoreCase(
							gcda.ATOM_LINK_TYPE)) {
						String patientLinkUrl = patientLink.getHref();
						// href="http://localhost:3000/records/5"
						String id = patientLinkUrl.substring(patientLinkUrl
								.lastIndexOf("/") + 1);
						ret.put(id, name);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	@Override
	public List<Condition> getProblems(String patientId)
			throws NotImplementedException {
		// TODO Auto-generated method stub
		List<Condition> problems = new ArrayList<Condition>();

		Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		try {
			List<String> results = gcda.findHealthDetail(patientId, "problems");
			for (String url : results) {
				String server = greenCDADataUrl + url;
				String jsonResults = WebUtils.callServer(server, "GET",
						"application/json", new String[] {});

				JsonObject o = parser.parse(jsonResults).getAsJsonObject();
				Condition problem = gson.fromJson(o, Condition.class);
				String time = problem.getTime();
				problem.setTime(parseDate(time, isMillis));

				problems.add(problem);
			}

			/*
			 * } catch (IOException e) {
			 * // TODO Auto-generated catch block
			 * e.printStackTrace();
			 */
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return problems;
	}

	@Override
	public List<Support> getSupportInfo(String patientId)
			throws NotImplementedException {
		// TODO Auto-generated method stub

		List<Support> supportList = new ArrayList<Support>();

		String server = greenCDADataUrl + "/records/" + patientId;

		Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		try {
			List<String> results = gcda.findHealthDetail(patientId, "support");
			for (String url : results) {
				server = greenCDADataUrl + url;

				String jsonResults = WebUtils.callServer(server, "GET",
						"application/json", new String[] {});

				JsonObject o = parser.parse(jsonResults).getAsJsonObject();
				Support support = gson.fromJson(o, Support.class);

				supportList.add(support);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return supportList;
	}

	@Override
	public List<Procedure> getProcedures(String patientId)
			throws NotImplementedException {
		// TODO Auto-generated method stub
		List<Procedure> procedures = new ArrayList<Procedure>();

		String server = greenCDADataUrl + "/records/" + patientId;

		Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		try {
			List<String> results = gcda.findHealthDetail(patientId,
					"procedures");
			for (String url : results) {
				server = greenCDADataUrl + url;

				String jsonResults = WebUtils.callServer(server, "GET",
						"application/json", new String[] {});

				JsonObject o = parser.parse(jsonResults).getAsJsonObject();
				Procedure procedure = gson.fromJson(o, Procedure.class);
				String time = procedure.getTime();
				procedure.setTime(parseDate(time, isMillis));

				procedures.add(procedure);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return procedures;
	}

	@Override
	public List<SocialHistory> getSocialHistory(String patientId)
			throws NotImplementedException {
		List<SocialHistory> socialHistoryList = new ArrayList<SocialHistory>();

		String server = greenCDADataUrl + "/records/" + patientId;

		Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		try {
			List<String> results = gcda.findHealthDetail(patientId,
					"social_history");
			for (String url : results) {
				server = greenCDADataUrl + url;

				String jsonResults = WebUtils.callServer(server, "GET",
						"application/json", new String[] {});

				JsonObject o = parser.parse(jsonResults).getAsJsonObject();
				SocialHistory socialHistory = gson.fromJson(o,
						SocialHistory.class);
				String time = socialHistory.getTime();
				socialHistory.setTime(parseDate(time, isMillis));
				socialHistoryList.add(socialHistory);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return socialHistoryList;
	}

	private String buildName(String firstName, String lastName) {
		if (firstName.length() == 0) {
			if (lastName.length() > 0) return lastName;
			else return "";
		}
		else if (lastName.length() == 0) return firstName;
		else return firstName + " " + lastName;
	}

	@Override
	public String getPatientID(String family, String given) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Patient getPatient(String patientId) {
		Patient ret = new Patient();
		
		try {
			String url = greenCDADataUrl + "/records/" + patientId;
			List<SyndEntry> patientEntries = gcda.parseAtom(url);

			System.out.println("GreenCDARepository : getPatientByName size "
					+ patientEntries.size());

			SyndFeedInput input = new SyndFeedInput();
			SyndFeed feed = null;
			for (SyndEntry patientEntry : patientEntries) {
				String name = patientEntry.getTitle();
				ret.setFirstName(name.split(" ")[0]);
				ret.setLastName(name.split(" ")[1]);
				String demographics = "";
				for(Object o : patientEntry.getContents()){
					SyndContentImpl  content = (SyndContentImpl ) o;
					demographics.concat(content.getValue());
				}
				ret.setDemographics(demographics);
				break; //it's the first one.. for some reason I can't get the entry id...
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	@Override
	public boolean insertAllergies(String patientId,
			Collection<Allergy> allergies) throws NotImplementedException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<FMRecord> getTimeLineInfo(String ien)
			throws NotImplementedException {
		// TODO Auto-generated method stub
		return null;
	}

	public void setCredentials(HashMap<String, String> credMap) {
		if (credMap.get(Repository.HOST_URL) == null
				|| credMap.get(Repository.HOST_URL).equals("")) { throw new RuntimeException(
				"Must include hostURL for hData"); }
		if (credMap.get(Repository.PORT) == null
				|| credMap.get(Repository.PORT).equals("")) { throw new RuntimeException(
				"Must include port for hData database"); }
		greenCDADataUrl = credMap.get(Repository.HOST_URL) + ":"
				+ credMap.get(Repository.PORT);
		credentials = credMap;
	}

}
