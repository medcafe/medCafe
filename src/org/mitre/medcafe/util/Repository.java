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

import com.medsphere.fileman.FMRecord;


/**
 *  This class represents a data Repository for MedCafe.  This allows for common functionality no matter if the underlying repository is VistA or hData or C32
 *  or other
 */
public abstract class Repository
{
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

    /**
     *  Given a patient id, get the patient info
     */
    public abstract org.projecthdata.hdata.schemas._2009._06.patient_information.Patient getPatient( String patientId );

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
    public abstract List<Allergy> getAllergies( String patientId );

    /**
     *  Get a set of medications specific to a patient
     */
    public abstract List<Medication> getMedications( String patientId );

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
    protected HashMap<String,String> credentials = null;

    /**
     * Get credentials property.
     *
     *@return Credentials property.
     */
    public HashMap<String,String> getCredentials() {
    	return this.credentials;
    }

    /**
     * Set credentials property.
     *
     *@param credentials New credentials property.
     */
    public void setCredentials(HashMap<String, String> credMap)
    {
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
    public void onShutdown()
    {

    }
    public abstract Collection<FMRecord> getTimeLineInfo(String ien) throws NotImplementedException;
    
    public abstract Collection<EncounterDetail> getPatientEncounters(String id) throws NotImplementedException;
    
    public abstract List<Result> getLatestVitals(String id) throws NotImplementedException;
    public abstract List<Result> getAllVitals(String id) throws NotImplementedException;
    
    /* don't currently have history in OpenVista or hData; this refers back to data on the local database system
       on a live system, this would need to be rewritten for each repository class in order to access that repository   */
       
    public JSONObject getHistory(String patientId, String category,  Date startDate, Date endDate)
    {
    	int id = org.mitre.medcafe.model.Patient.getLocalId(getName(),patientId);
    	String local_id = String.valueOf(id);
    	return History.getHistory(local_id, category, startDate, endDate);
    } 
    

}
