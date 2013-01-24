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
package org.mitre.medcafe.util;


import org.hl7.greencda.c32.Allergy;
import org.hl7.greencda.c32.Condition;
import org.hl7.greencda.c32.Encounter;
import org.hl7.greencda.c32.Immunization;
import org.hl7.greencda.c32.Medication;
import org.hl7.greencda.c32.Person;
import org.hl7.greencda.c32.Procedure;
import org.hl7.greencda.c32.Result;
import org.hl7.greencda.c32.Support;
import org.json.JSONObject;
import org.mitre.medcafe.model.*;

import java.util.*;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.Timer;
import com.medsphere.fileman.FMRecord;

/**
 *  This class represents a data Repository for MedCafe.  This allows for common functionality no matter if the underlying repository is VistA or hData or C32
 *  or other
 */
public abstract class Repository {

    public static final String HOST_URL = "hostURL";
    public static final String PORT = "port";
    public static final String ACCESS_CODE = "accessCode";
    public static final String VERIFY_CODE = "verifyCode";
    public static final String CREDENTIALS = "credentials";
    public static final String HOST = "host";
    public static final String REPOSITORY_TYPE = "type";
    public static final String REPOSITORY_NAME = "name";
    public static final String REPOSITORY_ITEM = "repository";
    public static final String REPOSITORIES = "repositories";


   // protected TreeSet<Product> prodSet;
   // protected TreeSet<Reaction> reactionSet;
   // private ReentrantReadWriteLock allergyReactantLock = new ReentrantReadWriteLock(true);
   // private ReentrantReadWriteLock allergyReactionLock = new ReentrantReadWriteLock(true);
    private Timer updateTableTimer;

    public Repository(HashMap<String, String> credMap) {
        setCredentials(credMap);
        //createAllergyReactionObject();
        //createAllergyReactantObject();
        updateTableTimer = new Timer();
        GregorianCalendar cal = new GregorianCalendar();
        cal.add(Calendar.DATE, 1);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 1);
        cal.set(Calendar.SECOND, 0);

        updateTableTimer.scheduleAtFixedRate(new RepositoryTimerTask(this), cal.getTime(), (long)(1000*60*60*24) );
    }

    /**
     *  Given a patient id, get the patient info
     */
    public abstract Patient getPatient(String patientId);

    /**
     *  Given a patient name, get the patient id
     */
    public abstract Map<String, String> getPatientByName(String family, String given, String middle);

    /**
     *  Get a list of patient identifiers
     */
    public abstract Map<String, String> getPatients();

    /**
     *  Get a set of allergies specific to a patient
     */
    public abstract List<Allergy> getAllergies(String patientId);

    public abstract boolean insertAllergies(String patientId, Collection<Allergy> allergies) throws NotImplementedException;
    /**
     *  Get a set of medications specific to a patient
     */
    public abstract List<Medication> getMedications(String patientId);

    /**
     *  Get a problem list for a patient.
     */
    public abstract List<Condition> getProblems(String patientId) throws NotImplementedException;

    /**
     *  Get a support (contact) list for a patient.
     */
    public abstract List<Support> getSupportInfo(String patientId) throws NotImplementedException;

    /**
     * Get a list of immunizations for a patient.
     */
    public abstract List<Immunization> getImmunizations(String id) throws NotImplementedException;
    
    
    public abstract List<FMRecord> getTimeLineInfo(String ien) throws NotImplementedException;

    public abstract List<Encounter> getPatientEncounters(String id) throws NotImplementedException;

    public abstract List<Result> getLatestVitals(String id) throws NotImplementedException;

    public abstract List<Result> getAllVitals(String id) throws NotImplementedException;

    public  abstract List<Procedure> getProcedures(String patientId)  throws NotImplementedException;
		  
    public  abstract String getPatientID(String family, String given) throws NotImplementedException;;
    	
    /**
     * Type property.
     */
    protected String type = null;

    /**
     * Get type property.
     *
     *@return Type property.
     */
    public String getType() {
        return this.type;
    }

    /**
     * Set type property.
     *
     *@param type New type property.
     */
    public void setType(String type) {
        this.type = type;
    }
    /**
     * Credentials property.
     */
    protected HashMap<String, String> credentials = null;

    /**
     * Get credentials property.
     *
     *@return Credentials property.
     */
    public HashMap<String, String> getCredentials() {
        return this.credentials;
    }

    /**
     * Set credentials property.
     *
     *@param credentials New credentials property.
     */
    public void setCredentials(HashMap<String, String> credMap) {
        credentials = credMap;
    }
    /**
     * Name property.
     */
    protected String name = null;

    /**
     * Get name property.
     *
     *@return Name property.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Set name property.
     *
     *@param name New name property.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *  Called at web server shutdown to cleanup any hanging resources
     */
    public void onShutdown() {
                if (updateTableTimer != null)
        {
            updateTableTimer.cancel();
        }
    }

   
    /* don't currently have history in OpenVista or hData; this refers back to data on the local database system
    on a live system, this would need to be rewritten for each repository class in order to access that repository   */
    public JSONObject getHistory(String patientId, String category, Date startDate, Date endDate) {
        int id = org.mitre.medcafe.model.Patient.getLocalId(getName(), patientId);
        String local_id = String.valueOf(id);
        return History.getHistory(local_id, category, startDate, endDate);
    }


    //public abstract TreeSet<Reaction> generateAllergyReactionList();

    //public abstract TreeSet<Product> generateAllergyReactantList();

    public void updateLookupTables()
    {
        //createAllergyReactantObject();
        //createAllergyReactionObject();
    }

	public Person getPatient(String userName, String patientId) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Result> getResults(String patientId) {
		// TODO Auto-generated method stub
		return null;
	}

	
	

	 
   
}
