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

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.*;
import javax.xml.transform.stream.StreamSource;

import org.hl7.greencda.c32.Allergy;
import org.hl7.greencda.c32.Code;
import org.hl7.greencda.c32.Condition;
import org.hl7.greencda.c32.Encounter;
import org.hl7.greencda.c32.Immunization;
import org.hl7.greencda.c32.Medication;
import org.hl7.greencda.c32.Person;
import org.hl7.greencda.c32.Procedure;
import org.hl7.greencda.c32.Result;
import org.hl7.greencda.c32.SocialHistory;
import org.hl7.greencda.c32.Support;
import org.json.JSONException;
import org.json.JSONObject;
import org.mitre.medcafe.model.Patient;
import org.mitre.medcafe.util.Constants;
import org.mitre.medcafe.util.NotImplementedException;
import org.mitre.medcafe.util.Repository;
import org.mitre.medcafe.util.WebUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.medsphere.fileman.FMRecord;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import java.util.HashMap;

/**
 *  This class implements an interface to a back-end Vista repository.  The Medsphere (http://www.medsphere.com/) version of VistA, named OpenVista, is
 *  used via the medsphere ovid library
 */
public class GreenCDARepository extends Repository {

   
	public final static String KEY = GreenCDARepository.class.getName();
    public final static Logger log = Logger.getLogger(KEY);
    private GreenCDARepository cda;
    private TreeMap<Date, Result> vitalTree = null;
    public final static String CCR_DIR = "ccrFiles";


    private String userName ="guest";
    
    
    public GreenCDARepository() {
	}
    
    public GreenCDARepository(HashMap<String, String> credMap) {
		super(credMap);
		// TODO Auto-generated constructor stub
	}
    
	@Override
	public List<Result> getAllVitals(String id)
			throws NotImplementedException {
		// TODO Auto-generated method stub
		List<Result> vitals = new ArrayList<Result>();
		
		return vitals;
	}
	@Override
	public List<Allergy> getAllergies(String patientId) {
		// TODO Auto-generated method stub
		List<Allergy> allergies = new ArrayList<Allergy>();	
		return allergies;
	}
	@Override
	public List<Immunization> getImmunizations(String id)
			throws NotImplementedException {
		List<Result> vitals = new ArrayList<Result>();
		   
		return null;
	}
	@Override
	public List<Result> getLatestVitals(String id)
			throws NotImplementedException {
		// TODO Auto-generated method stub
	   	
		//Sort Vitals by date and return most recent
		List<Result> vitals = null;
		
        return vitals;
	}
	
	@Override
	public List<Result> getResults(String patientId) {
		// TODO Auto-generated method stub
		 
		List<Result> results = null;
		
		return results;
	}
	
	@Override
	public List<Medication> getMedications(String patientId) {
		
		//{"_id":"50d1e69dbd8009e351000244","codes":{"RxNorm":["314076"]},"mood_code":"EVN","version":1,"_type":"Medication","time":null,"start_time":1277438400,"end_time":null,"description":"ACE inhibitors","free_text":"ACE inhibitors","route":null,"dose":null,"site":null,"productForm":null,"deliveryMethod":null,"typeOfMedication":null,"indication":null,"vehicle":null}
		// public static String callServer(String server, String action, String format, String... params) throws IOException
		String server = "http://1.1.22.110:3000/records/4/medications/50d1e69dbd8009e351000244";
		//String results ="{\"id\":\"50d1e69dbd8009e351000244\",\"codes\":{\"RxNorm\":[\"314076\"]},\"mood_code\":\"EVN\",\"version\":1,\"_type\":\"Medication\",\"time\":null,\"start_time\":1277438400,\"end_time\":null,\"description\":\"ACE inhibitors\",\"free_text\":\"ACE inhibitors\",\"route\":null,\"dose\":null,\"site\":null,\"productForm\":null,\"deliveryMethod\":null,\"typeOfMedication\":null,\"indication\":null,\"vehicle\":null}";
		String results ="{\"id\":\"50d1e69dbd8009e351000244\",\"freeText\":\"ACE inhibitors\",\"route\":{\"code\":\"Oral\"},\"dose\":null,\"site\":null,\"productForm\":null,\"deliveryMethod\":null,\"typeOfMedication\":null,\"indication\":null,\"vehicle\":null}";
		
		List<Medication> meds = new ArrayList<Medication>();
		
		try {
			String tempResults = WebUtils.callServer(server, "GET", "application/json", new String[]{});
			System.out.println("GreenCDARepository getMedications " + results);
			Gson gson = new Gson();
			JsonParser parser = new JsonParser();
			JsonObject o = parser.parse(results).getAsJsonObject();
			Medication med = gson.fromJson(o,  Medication.class);
			
            meds.add(med);
		
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
            meds.add(returnMed);
            
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return meds;

	}
	
	@Override
	public Person getPatient(String userName, String patientId) {
		// TODO Auto-generated method stub
		this.userName = userName;
		
		Person patient = null;
		return patient;
	}
	
	@Override
	public Map<String, String> getPatientByName(String family, String given,
			String middle) {
		// TODO Auto-generated method stub
		
		Map<String, String> ret = new HashMap<String, String>();
		this.userName = userName;
		//Lookup patient id in DB
		
		return ret;
	}
	
	@Override
	public List<Encounter> getPatientEncounters(String id)
			throws NotImplementedException {
	
		List<Encounter> encounters = null;
		return encounters;
	}
	@Override
	public Map<String, String> getPatients() {
		
		Map<String, String> ret = new HashMap<String, String>();
		
		return ret;
	}
	@Override
	public List<Condition> getProblems(String patientId)
			throws NotImplementedException 
	{
		// TODO Auto-generated method stub
		 List<Condition> problems= null;
		return problems;
	}
	@Override
	public List<Support> getSupportInfo(String patientId)
			throws NotImplementedException {
		// TODO Auto-generated method stub
		
		List<Support> support = null;
		return support;
	}
        
	@Override
	public List<Procedure> getProcedures(String patientId)
			throws NotImplementedException {
		// TODO Auto-generated method stub
		List<Procedure> procedures = null;
		return procedures;
	}
	
	    
     public   List<SocialHistory> getSocialHistory(String patientId) throws NotImplementedException
     {
    	 List<SocialHistory> socialHistory = null;
         return socialHistory;
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

}

    
