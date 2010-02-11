package org.mitre.medcafe.util;

import org.json.JSONObject;
import java.util.*;


/**
 *  This class represents a data Repository for MedCafe.  This allows for common functionality no matter if the underlying repository is VistA or hData or C32
 *  or other
 */
public abstract class Repository
{

    /**
     *  Given a patient id, get the patient info
     */
    // public abstract Patient getPatient( String id );
    public abstract JSONObject getPatient( String id );

    /**
     *  Get a list of patient identifiers
     */
    public abstract List<String> getPatients();

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
}
