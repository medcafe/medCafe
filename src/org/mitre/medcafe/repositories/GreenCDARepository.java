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
package org.mitre.medcafe.repositories;

// import org.mitre.hdata.hrf.core.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import org.hl7.greencda.c32.Allergy;
import org.hl7.greencda.c32.Code;
import org.hl7.greencda.c32.Condition;
import org.hl7.greencda.c32.Encounter;
import org.hl7.greencda.c32.HealthObject;
import org.hl7.greencda.c32.Immunization;
import org.hl7.greencda.c32.Medication;
import org.hl7.greencda.c32.Person;
import org.hl7.greencda.c32.PersonalName;
import org.hl7.greencda.c32.Procedure;
import org.hl7.greencda.c32.Result;
import org.hl7.greencda.c32.SocialHistory;
import org.hl7.greencda.c32.Support;
import org.mitre.medcafe.model.Patient;
import org.mitre.medcafe.util.GreenCDAFeedParser;
import org.mitre.medcafe.util.NotImplementedException;
import org.mitre.medcafe.util.Repository;
import org.mitre.medcafe.util.WebUtils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.medsphere.fileman.FMRecord;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.HashMap;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndLinkImpl;
import com.sun.syndication.io.SyndFeedInput;
/**
 *  This class implements an interface to a back-end Vista repository.  The Medsphere (http://www.medsphere.com/) version of VistA, named OpenVista, is
 *  used via the medsphere ovid library
 */
public class GreenCDARepository extends Repository {

   
	public final static String KEY = GreenCDARepository.class.getName();
    public final static Logger log = Logger.getLogger(KEY);
    private GreenCDARepository cda;
    private TreeMap<Date, Result> vitalTree = null;
    

    private String userName ="guest";
    private GreenCDAFeedParser gcda = new GreenCDAFeedParser();
    public static String greenCDADataUrl= "";
    
    //For test purposes
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
			List<String> results = gcda.findHealthDetail( patientId, "vital_signs");
			for (String url: results)
			{
				server = greenCDADataUrl + url;
				
				String vitalSignsResults = WebUtils.callServer(server, "GET", "application/json", new String[]{});
				
				JsonObject o = parser.parse(vitalSignsResults).getAsJsonObject();
				Result record = gson.fromJson(o,  Result.class);
				HealthObject ho = gson.fromJson(o, HealthObject.class);
				String testJsonHo = gson.toJson(ho);

				vitals.add(record);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return vitals;
	}
	
	// this 'lil nested class is a cheat to cheapen the complexity of the code for
	// getting the "latest" vitals. Simply sort, then split the list. 
	public class NewVitalsComparable implements Comparator<Result>{
	    @Override
	    public int compare(Result o1, Result o2) {
	    	return o1.getEffectiveTime().getValue().compare(o2.getEffectiveTime().getValue());
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
			List<String> allergyResults = gcda.findHealthDetail( patientId, "allergies");
			for (String allergyUrl: allergyResults)
			{
				String server = greenCDADataUrl + allergyUrl;
				String tempResults = WebUtils.callServer(server, "GET", "application/json", new String[]{});
				System.out.println("GreenCDARepository getAllergies results from json " + tempResults);

				JsonObject o = parser.parse(tempResults).getAsJsonObject();
				Allergy allergy = gson.fromJson(o,  Allergy.class);
				
				allergies.add(allergy);
			}
			
            
            
		/*} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			*/
		}
		catch (Exception e) {
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
			List<String> results = gcda.findHealthDetail( id, "immunizations");
			for (String url: results)
			{
				server = greenCDADataUrl + url;
				
				String jsonResults = WebUtils.callServer(server, "GET", "application/json", new String[]{});
				
				JsonObject o = parser.parse(jsonResults).getAsJsonObject();
				Immunization immunization = gson.fromJson(o,  Immunization.class);
				
				vax.add(immunization);
			}
		}
		catch (Exception e) {
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
			List<String> results = gcda.findHealthDetail( patientId, "results");
			for (String url: results)
			{
				server = greenCDADataUrl + url;
				
				String jsonResults = WebUtils.callServer(server, "GET", "application/json", new String[]{});
				
				JsonObject o = parser.parse(jsonResults).getAsJsonObject();
				Result result = gson.fromJson(o,  Result.class);
				
				resultList.add(result);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}
	
	@Override
	public List<Medication> getMedications(String patientId) {
		
		//{"_id":"50d1e69dbd8009e351000244","codes":{"RxNorm":["314076"]},"mood_code":"EVN","version":1,"_type":"Medication","time":null,"start_time":1277438400,"end_time":null,"description":"ACE inhibitors","free_text":"ACE inhibitors","route":null,"dose":null,"site":null,"productForm":null,"deliveryMethod":null,"typeOfMedication":null,"indication":null,"vehicle":null}
		// public static String callServer(String server, String action, String format, String... params) throws IOException
		String server = "http://1.1.22.110:3000/records/4/medications/50d1e69dbd8009e351000244";
		//String results ="{\"id\":\"50d1e69dbd8009e351000244\",\"codes\":{\"RxNorm\":[\"314076\"]},\"mood_code\":\"EVN\",\"version\":1,\"_type\":\"Medication\",\"time\":null,\"start_time\":1277438400,\"end_time\":null,\"description\":\"ACE inhibitors\",\"free_text\":\"ACE inhibitors\",\"route\":null,\"dose\":null,\"site\":null,\"productForm\":null,\"deliveryMethod\":null,\"typeOfMedication\":null,\"indication\":null,\"vehicle\":null}";
		String results ="{\"id\":\"50d1e69dbd8009e351000244\",\"codes\": [{\"RxNorm\":\"314076\"}],\"mood_code\":\"EVN\",\"version\":1,\"_type\":\"Medication\",\"time\":null,\"start_time\":1277438400,\"end_time\":null,\"description\":\"ACE inhibitors\",\"freeText\":\"ACE inhibitors\",\"route\":{\"code\":\"Oral\"},\"dose\":null,\"site\":null,\"productForm\":null,\"deliveryMethod\":null,\"typeOfMedication\":null,\"indication\":null,\"vehicle\":null}";

		List<Medication> meds = new ArrayList<Medication>();
		Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		try {
			List<String> medResults = gcda.findHealthDetail( patientId, "medications");
			for (String medUrl: medResults)
			{
				server = greenCDADataUrl + medUrl;
				String tempResults = WebUtils.callServer(server, "GET", "application/json", new String[]{});
				System.out.println("GreenCDARepository getMedications results from json " + tempResults);

				JsonObject o = parser.parse(tempResults).getAsJsonObject();
				Medication med = gson.fromJson(o,  Medication.class);
				HealthObject ho = gson.fromJson(o, HealthObject.class);
				String testJsonHo = gson.toJson(ho);

				System.out.println("GreenCDARepository Health Object " + testJsonHo );
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
            //meds.add(returnMed);
            
		/*} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			*/
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return meds;

	}
	
	@Override
	public Person getPatient(String userName, String patientId) {
		// TODO Auto-generated method stub
		this.userName = userName;
		Person patient = new Person();
		try {
			//List<String> patientResults = gcda.findPatient(firstName, lastName, url);
		
			Gson gson = new Gson();
			JsonParser parser = new JsonParser();
			String url = greenCDADataUrl + "/records/" + patientId + "/c32";
			List<String> patientUrls = null;
			
			List<SyndEntry> patientEntries = gcda.parseAtom(url);
				
			for (SyndEntry patientEntry: patientEntries)
			{
				/*<entry>
				    <id>3</id>
				    <title>Smith59, Tom</title>
				    <link href="http://localhost:3000/records/3/c32/3"/>
				  </entry>*/
				
				name = patientEntry.getTitle();
				PersonalName personName= new PersonalName();
				String[] nameParts = name.split(",");
				personName.setGivenName(nameParts[0]);
				personName.setFamilyName(nameParts[1]);
				patient.setName(personName);
			}
			//If need to get demographics
			/*String patientUrl ="";
			if (patientUrls.size() > 0)
			{
				patientUrl = patientUrls.get(0);
				String server = greenCDADataUrl + patientUrl;
				String patientResults = WebUtils.callServer(server, "GET", "application/json", new String[]{});
				JsonObject o = parser.parse(patientResults).getAsJsonObject();
				patient = gson.fromJson(o,  Person.class);
			}*/
			return patient;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return patient;
	}
	
	@Override
	public Map<String, String> getPatientByName(String family, String given,
			String middle) {
		// TODO Auto-generated method stub
		
		Map<String, String> ret = new HashMap<String, String>();
		this.userName = userName;
		//Lookup patient id in DB
		try {
			String url = greenCDADataUrl + "/records";
			List<SyndEntry> patientEntries = gcda.findPatientEntries(given, family, url);

			System.out.println("GreenCDARepository : getPatientByName size " + patientEntries.size());
	    				
			SyndFeedInput input = new SyndFeedInput();
		    SyndFeed feed = null;     	 
		    for (SyndEntry patientEntry: patientEntries)
		    {
		    	String name = patientEntry.getTitle();
		    	
		    	List<SyndLinkImpl> patientLinks = gcda.findPatientLinks(patientEntry);
		    	for (SyndLinkImpl patientLink: patientLinks)
			    {
			    	if (patientLink.getType().equalsIgnoreCase(gcda.ATOM_LINK_TYPE))
			        {
			        	 String patientLinkUrl = patientLink.getHref();
			        	 //href="http://localhost:3000/records/5"
			        	 String id = patientLinkUrl.substring(patientLinkUrl.lastIndexOf("/"));
			        	 ret.put(id, name);
			        }
			    }
		    }
	    	
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return ret;
	}
	
	@Override
	public List<Encounter> getPatientEncounters(String id)
			throws NotImplementedException {
	
		String type = "encounters";
		List<Encounter> encounters =  new ArrayList<Encounter>();
		   
		String server = greenCDADataUrl + "/records/" + id;

		Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		try {
			List<String> results = gcda.findHealthDetail( id, type);
			for (String url: results)
			{
				server = greenCDADataUrl + url;
				
				String jsonResults = WebUtils.callServer(server, "GET", "application/json", new String[]{});
				
				JsonObject o = parser.parse(jsonResults).getAsJsonObject();
				Encounter encounter = gson.fromJson(o,  Encounter.class);
				
				encounters.add(encounter);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return encounters;
	}
	@Override
	public Map<String, String> getPatients() {
		
		Map<String, String> ret = new HashMap<String, String>();
		this.userName = userName;
		//Lookup patient id in DB
		try {
			String url =greenCDADataUrl + "/records";
			System.out.println("GreenCDARepository : url " + url);
	    	
			List<SyndEntry> patientEntries = gcda.findAllPatientEntries(url);

			System.out.println("GreenCDARepository : getPatientByName size " + patientEntries.size());
	    				
			SyndFeedInput input = new SyndFeedInput();
		    SyndFeed feed = null;     	 
		    for (SyndEntry patientEntry: patientEntries)
		    {
		    	String name = patientEntry.getTitle();
		    	
		    	List<SyndLinkImpl> patientLinks = gcda.findPatientLinks(patientEntry);
		    	for (SyndLinkImpl patientLink: patientLinks)
			    {
			    	if (patientLink.getType().equalsIgnoreCase(gcda.ATOM_LINK_TYPE))
			        {
			        	 String patientLinkUrl = patientLink.getHref();
			        	 //href="http://localhost:3000/records/5"
			        	 String id = patientLinkUrl.substring(patientLinkUrl.lastIndexOf("/") + 1);
			        	 ret.put(id, name);
			        }
			    }
		    }
	    	
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return ret;
	}
	@Override
	public List<Condition> getProblems(String patientId)
			throws NotImplementedException 
	{
		// TODO Auto-generated method stub
		 List<Condition> problems= new ArrayList<Condition>();

		 Gson gson = new Gson();
			JsonParser parser = new JsonParser();
			try {
				List<String> results = gcda.findHealthDetail( patientId, "problems");
				for (String url: results)
				{
					String server = greenCDADataUrl + url;
					String jsonResults = WebUtils.callServer(server, "GET", "application/json", new String[]{});
					
					JsonObject o = parser.parse(jsonResults).getAsJsonObject();
					Condition problem = gson.fromJson(o,  Condition.class);
					
					problems.add(problem);
				}
				
	            
	            
			/*} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				*/
			}
			catch (Exception e) {
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
			List<String> results = gcda.findHealthDetail( patientId, "support");
			for (String url: results)
			{
				server = greenCDADataUrl + url;
				
				String jsonResults = WebUtils.callServer(server, "GET", "application/json", new String[]{});
				
				JsonObject o = parser.parse(jsonResults).getAsJsonObject();
				Support support = gson.fromJson(o,  Support.class);
				
				supportList.add(support);
			}
		}
		catch (Exception e) {
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
			List<String> results = gcda.findHealthDetail( patientId, "procedures");
			for (String url: results)
			{
				server = greenCDADataUrl + url;
				
				String jsonResults = WebUtils.callServer(server, "GET", "application/json", new String[]{});
				
				JsonObject o = parser.parse(jsonResults).getAsJsonObject();
				Procedure procedure = gson.fromJson(o,  Procedure.class);
				
				procedures.add(procedure);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return procedures;
	}
	
	@Override
     public   List<SocialHistory> getSocialHistory(String patientId) throws NotImplementedException
     {
    	 List<SocialHistory> socialHistoryList  = new ArrayList<SocialHistory>();
 		
 		String server = greenCDADataUrl + "/records/" + patientId;

 		Gson gson = new Gson();
 		JsonParser parser = new JsonParser();
 		try {
 			List<String> results = gcda.findHealthDetail( patientId, "social_history");
 			for (String url: results)
 			{
 				server = greenCDADataUrl + url;
 				
 				String jsonResults = WebUtils.callServer(server, "GET", "application/json", new String[]{});
 				
 				JsonObject o = parser.parse(jsonResults).getAsJsonObject();
 				SocialHistory socialHistory = gson.fromJson(o,  SocialHistory.class);
 				
 				socialHistoryList.add(socialHistory);
 			}
 		}
 		catch (Exception e) {
 			e.printStackTrace();
 		}
        return socialHistoryList;
     }
                
	
	private String buildName(String firstName, String lastName)
	{
		if (firstName.length() == 0)
		{
			if (lastName.length() > 0)
				return lastName;
			else
				return "";
		}
		else if (lastName.length() == 0)
			return firstName;
		else
			return firstName + " " + lastName;
	}
	
	@Override
	public String getPatientID(String family, String given) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Patient getPatient(String patientId) {
		// TODO Auto-generated method stub
		return null;
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
	
	public void setCredentials(HashMap<String, String> credMap)
    {
    	    if (credMap.get(Repository.HOST_URL)== null || credMap.get(Repository.HOST_URL).equals(""))
    		{
    			throw new RuntimeException("Must include hostURL for hData");
    		}
    		if (credMap.get(Repository.PORT)== null || credMap.get(Repository.PORT).equals(""))
    		{
    			throw new RuntimeException("Must include port for hData database");
    		}
		greenCDADataUrl = credMap.get(Repository.HOST_URL) + ":" + credMap.get(Repository.PORT);
    	credentials = credMap;
    }

}

    
