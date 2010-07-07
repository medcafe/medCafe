package org.mitre.medcafe.util;

import org.json.JSONObject;

import java.util.*;
import org.projecthdata.hdata.schemas._2009._06.patient_information.*;
import org.projecthdata.hdata.schemas._2009._06.allergy.*;
import org.projecthdata.hdata.schemas._2009._06.medication.*;
import org.projecthdata.hdata.schemas._2009._06.immunization.*;
import org.projecthdata.hdata.schemas._2009._06.condition.*;
import org.projecthdata.hdata.schemas._2009._06.support.*;
import com.medsphere.fileman.FMRecord;

/**
 *  This class represents a data Repository for MedCafe.  This allows for common functionality no matter if the underlying repository is VistA or hData or C32
 *  or other
 */
public abstract class Repository
{

    /**
     *  Given a patient id, get the patient info
     */
    public abstract Patient getPatient( String patientId );

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
    protected String[] credentials = null;

    /**
     * Get credentials property.
     *
     *@return Credentials property.
     */
    public String[] getCredentials() {
    	return this.credentials;
    }

    /**
     * Set credentials property.
     *
     *@param credentials New credentials property.
     */
    public void setCredentials(String... credentials) {
    	this.credentials = credentials;
    }

    /**
     * Set credentials property.
     *
     *@param credentials New credentials property.
     */
     public void setCredentialsArray(String[] credentials) {
    	this.credentials = credentials;
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
}
