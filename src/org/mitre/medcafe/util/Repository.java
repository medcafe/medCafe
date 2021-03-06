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


import org.json.JSONObject;
import org.mitre.medcafe.model.*;
import java.util.*;

import org.projecthdata.hdata.schemas._2009._06.patient_information.*;
import org.projecthdata.hdata.schemas._2009._06.allergy.*;
import org.projecthdata.hdata.schemas._2009._06.medication.*;
import org.projecthdata.hdata.schemas._2009._06.immunization.*;
import org.projecthdata.hdata.schemas._2009._06.condition.*;
import org.projecthdata.hdata.schemas._2009._06.support.*;
import org.projecthdata.hdata.schemas._2009._06.result.*;
import org.mitre.medcafe.hdatabased.encounter.*;
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


    protected TreeSet<Product> prodSet;
    protected TreeSet<Reaction> reactionSet;
    private ReentrantReadWriteLock allergyReactantLock = new ReentrantReadWriteLock(true);
    private ReentrantReadWriteLock allergyReactionLock = new ReentrantReadWriteLock(true);
    private Timer updateTableTimer;

    public Repository(HashMap<String, String> credMap) {
        setCredentials(credMap);
        createAllergyReactionObject();
        createAllergyReactantObject();
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
    public abstract org.projecthdata.hdata.schemas._2009._06.patient_information.Patient getPatient(String patientId);

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
    public abstract List<org.projecthdata.hdata.schemas._2009._06.condition.Condition> getProblems(String patientId) throws NotImplementedException;

    /**
     *  Get a support (contact) list for a patient.
     */
    public abstract List<Support> getSupportInfo(String patientId) throws NotImplementedException;

    /**
     * Get a list of immunizations for a patient.
     */
    public abstract List<Immunization> getImmunizations(String id) throws NotImplementedException;
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

    public abstract Collection<FMRecord> getTimeLineInfo(String ien) throws NotImplementedException;

    public abstract Collection<EncounterDetail> getPatientEncounters(String id) throws NotImplementedException;

    public abstract List<Result> getLatestVitals(String id) throws NotImplementedException;

    public abstract List<Result> getAllVitals(String id) throws NotImplementedException;

    /* don't currently have history in OpenVista or hData; this refers back to data on the local database system
    on a live system, this would need to be rewritten for each repository class in order to access that repository   */
    public JSONObject getHistory(String patientId, String category, Date startDate, Date endDate) {
        int id = org.mitre.medcafe.model.Patient.getLocalId(getName(), patientId);
        String local_id = String.valueOf(id);
        return History.getHistory(local_id, category, startDate, endDate);
    }

    public JSONObject getAllergyReactantObject(String search) {
        try {
            allergyReactantLock.readLock().lock();
            Product first = new Product();
            Product last = new Product();
            first.setDisplayName(search);
            first.setValue(search);
            
            last.setDisplayName(search.substring(0,search.length()-1) + String.valueOf((char)(search.charAt(search.length()-1) + 1)));
            last.setValue(last.getDisplayName());
             ArrayList<Product> products = new ArrayList<Product>();
             products.addAll(prodSet.subSet(first, true, last, false));
            JSONObject retObj = WebUtils.bundleJsonResponseObject("allergenList", products);
            return retObj;
        } finally {
            allergyReactantLock.readLock().unlock();
        }
    }
 public JSONObject getAllergyReactionObject(String search) {
        try {
        		ArrayList<Reaction> reactions = new ArrayList<Reaction>();
            allergyReactionLock.readLock().lock();
            if (search == null ||search.equals(""))
            {
            	reactions.addAll(reactionSet);
            }
            else
            {
           		Reaction first = new Reaction();
            	Reaction last = new Reaction();
            first.setDisplayName(search);
            first.setValue(search);
            
            last.setDisplayName(search.substring(0,search.length()-1) + String.valueOf((char)(search.charAt(search.length()-1) + 1)));
            last.setValue(last.getDisplayName());
             reactions.addAll(reactionSet.subSet(first, true, last, false));
             }
            JSONObject retObj = WebUtils.bundleJsonResponseObject("reactionList", reactions);
            return retObj;
        } finally {
            allergyReactionLock.readLock().unlock();
        }
    }


    public synchronized void createAllergyReactionObject() {
        TreeSet<Reaction> reactList = generateAllergyReactionList();
        try {
            allergyReactionLock.writeLock().lock();
            reactionSet = reactList;
        } finally {
            allergyReactionLock.writeLock().unlock();
        }
    }

    public synchronized void createAllergyReactantObject() {
        TreeSet<Product> prodList = generateAllergyReactantList();
        try {
            allergyReactantLock.writeLock().lock();
				prodSet = prodList;
        } finally {
            allergyReactantLock.writeLock().unlock();
        }

    }

    public abstract TreeSet<Reaction> generateAllergyReactionList();

    public abstract TreeSet<Product> generateAllergyReactantList();

    public void updateLookupTables()
    {
        createAllergyReactantObject();
        createAllergyReactionObject();
    }
            public class ProductComparator implements Comparator<Product>
    {
    	public ProductComparator()
    	{
    		super();
    	}
    	public int compare(Product a, Product b)
    	{
    		return (a.getDisplayName().compareToIgnoreCase(b.getDisplayName()));
    	}
    }
    public class ReactionComparator implements Comparator<Reaction>
    {
    	public ReactionComparator()
    	{
    		super();
    	}
    	public int compare(Reaction a, Reaction b)
    	{
    		return (a.getDisplayName().compareToIgnoreCase(b.getDisplayName()));
    	}
    }

}
