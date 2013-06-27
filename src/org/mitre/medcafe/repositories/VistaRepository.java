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
import com.medsphere.fileman.*;
import com.medsphere.fmdomain.*;
import com.medsphere.ovid.domain.ov.*;
import com.medsphere.ovid.model.domain.*;
import com.medsphere.ovid.model.domain.patient.*;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;


import org.mitre.medcafe.model.Patient;
import org.mitre.medcafe.model.Values;
import org.mitre.medcafe.util.NotImplementedException;
import org.mitre.medcafe.util.Repository;

//import com.medsphere.vistarpc.RPCBrokerConnection;
//import com.medsphere.vistarpc.factory.RPCPooledConnection;
//import com.medsphere.vistarpc.factory.RPCBrokerPooledConnection;
import com.medsphere.vistarpc.RPCException;
//import com.medsphere.vistarpc.factory.RPCBrokerPooledConnectionFactory;
import com.medsphere.vistarpc.RPCConnection;
import org.medsphere.connection.VistaConnectionProperties;
import org.medsphere.connection.VistaConnectionException;
import org.medsphere.datasource.ServiceLocator;
import org.medsphere.lifecycle.LifecycleManager;
import org.medsphere.lifecycle.LifecycleListener;
import org.medsphere.auth.SubjectCache;
import org.hl7.greencda.c32.Address;
import org.hl7.greencda.c32.Allergy;
import org.hl7.greencda.c32.Code;
import org.hl7.greencda.c32.Codes;
import org.hl7.greencda.c32.Condition;
import org.hl7.greencda.c32.Encounter;
import org.hl7.greencda.c32.Immunization;
import org.hl7.greencda.c32.Interval;
import org.hl7.greencda.c32.Medication;
import org.hl7.greencda.c32.Medication.DoseRestriction;
import org.hl7.greencda.c32.Medication.OrderInformation;
import org.hl7.greencda.c32.ObjectFactory;
import org.hl7.greencda.c32.Person;
import org.hl7.greencda.c32.PersonalName;
import org.hl7.greencda.c32.Procedure;
import org.hl7.greencda.c32.Quantity;
import org.hl7.greencda.c32.Result;
import org.hl7.greencda.c32.SimpleCode;
import org.hl7.greencda.c32.SocialHistory;
import org.hl7.greencda.c32.Support;
import org.hl7.greencda.c32.Telecom;

import org.json.JSONObject;
import org.json.JSONString;
import org.json.JSONArray;
import org.json.JSONException;


import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

import java.util.HashMap;

/**
 *  This class implements an interface to a back-end Vista repository.  The Medsphere (http://www.medsphere.com/) version of VistA, named OpenVista, is
 *  used via the medsphere ovid library
 */
public class VistaRepository extends Repository {

    public final static String KEY = VistaRepository.class.getName();
    public final static Logger log = Logger.getLogger(KEY);
    public final static String PHYS_QUANTITY = "PhysicalQuantityResultValue";
    public final static String LAB_RESULT = "LabResult";
    public final static String EVENT_MOOD_CODE = "EVN";
    public final static SimpleDateFormat longDf = new SimpleDateFormat("dd-MMM-yyyy");
    public final static SimpleDateFormat shortDf = new SimpleDateFormat("MM/dd/yyyy");
    private static TreeMap<String, AllergyAgent> vistaAgents;
    private static TreeMap<String, FMSignSymptom> vistaReactions;
    private static ReentrantReadWriteLock vistaAgentLock = new ReentrantReadWriteLock();
    private static ReentrantReadWriteLock vistaReactionLock = new ReentrantReadWriteLock();
    private static DisplayGroupType[] resultTypes = {DisplayGroupType.ANATOMIC_PATHOLOGY, DisplayGroupType.AUTOPSY, DisplayGroupType.CARDIOLOGY_STUDIES_NUC_MED, DisplayGroupType.CHEMISTRY,
    	DisplayGroupType.CT_SCAN, DisplayGroupType.CYTOLOGY, DisplayGroupType.GENERAL_RADIOLOGY, DisplayGroupType.IMAGING,
    	DisplayGroupType.LABORATORY, DisplayGroupType.MAGNETIC_RESONANCE_IMAGING, DisplayGroupType.MAMMOGRAPHY,
    	DisplayGroupType.SURGICAL_PATHOLOGY, DisplayGroupType.ULTRASOUND};
    private static OrderStatusType[] resultStatuses = {OrderStatusType.COMPLETE};
    static{log.setLevel(Level.FINER);
}
    //protected static VistaLinkPooledConnectionFactory factory = null;
    // protected RPCBrokerConnection conn = null;
    // protected RPCBrokerPooledConnectionFactory rpcConnFactory = null;
    protected VistaConnectionProperties connectionProps = null;

    public VistaRepository(HashMap<String, String> credMap) {
        super(credMap);

        type = "VistA";
        getAllergens();
        getReactions();
    }

    /**
     *  Given a patient id, get the patient info
     */
    public Patient getPatient(String id) {
        RPCConnection conn = null;
        try {
            FMPatientContact filemanPat = null;
            FMPatientContact[] patList = null;
            PatientRepository patientRepository = null;

            conn = setConnection();

            if (conn != null) {
                log.finer("Connection successful");
                patientRepository = new PatientRepository(conn);
                Collection<String> iens = new ArrayList<String>();
                iens.add(id);
                Collection<FMPatientContact> patColl = patientRepository.getContacts(iens);
                if (patColl.size() == 0) {
                    log.severe("no results returned");
                    return null;
                }
                patList = patColl.toArray(new FMPatientContact[0]);
                filemanPat = patList[0];
                patientRepository.fetchNameComponents(filemanPat);
                log.finer("\t" + patList[0].getIEN());
                /*            for (FMField field : filemanPat.getFields()) {
                if (filemanPat.getValue(field.getName()) != null) {
                log.finer("\t" +field.getName() + ": " + filemanPat.getValue(field.getName()));
                }
                }   */
            } else {
                log.severe("Connection unsuccessful");
            }


            if (filemanPat == null) {
                return null;
            }
            log.finer(filemanPat.getName());
            Patient ret = new Patient();
            String suffix = "";
            String title = "";
            StringBuffer strBuff = new StringBuffer();
            //set  name
            List<String> given = new ArrayList<String>();
            if (stringExists(filemanPat.getGivenName())) {
            	strBuff.append(capitalizeString(filemanPat.getGivenName()) + " ");
            }
            if (stringExists(filemanPat.getMiddleName())) {
            	strBuff.append(capitalizeString(filemanPat.getMiddleName()));
            }
            if (stringExists(filemanPat.getSuffix())) {
            	suffix = filemanPat.getSuffix();
            }
            if (stringExists(filemanPat.getFamilyName())) {
                ret.setLastName(capitalizeString(filemanPat.getFamilyName()));
            }
            if (stringExists(filemanPat.getPrefix())) {
                title = capitalizeString(filemanPat.getPrefix());
            }
            ret.setDemographics(filemanPat.getSex() + ", " + longDf.format(filemanPat.getDob()) + " ("  + filemanPat.getDisplayAge()+ " years old)");

            ret.setFirstName(title + " " + suffix + " " + strBuff.toString());
            
            //phone numbers
           /* List<Telecom> teleList = ret.getTelecom();

            setPhoneNumber(teleList, filemanPat.getPhoneNumberResidence(), "phone-landline", "home");
            setPhoneNumber(teleList, filemanPat.getPhoneNumberWork(), "phone-landline", "work");
            setPhoneNumber(teleList, filemanPat.getPhoneNumberCellular(), "phone-cell", "other");
            setPhoneNumber(teleList, filemanPat.getPagerNumber(), "phone-pager", "other");
            setPhoneNumber(teleList, filemanPat.getEmailAddress(), "email", "other");
*/
            //setId(filemanPat.getIEN());
            return ret;
        
        } catch (OvidDomainException e) {
            log.log(Level.SEVERE, "Error retrieving patient " + id, e);
            return null;
        }  finally {
            closeConnection(conn);
        }

    }
	public static void main(String[] args)
	{
		HashMap<String, String> creds = new HashMap<String,String>();
		creds.put(HOST_URL, "medcafe.mitre.org");
		creds.put(ACCESS_CODE, "OV1234");
		creds.put(VERIFY_CODE, "OV1234!!");
		creds.put(PORT,"9201");
		VistaRepository repo = new VistaRepository(creds);
		Set<String> lookupAll = repo.lookup("allergens", "pen");
		for (String lookupString: lookupAll)
		{
			System.out.println(lookupString);
		}
		Set<String> lookupReact = repo.lookup("reactions", "ras");
		for (String lookupString: lookupReact)
		{
			System.out.println(lookupString);
		}
		
	}
	@Override
	public boolean canInsertAllergies()
	{
		return true;
	}
    @Override
	public String getPatientID(String family, String given)
			throws NotImplementedException {
		// TODO Auto-generated method stub
		return null;
	}
    /**
     *  Get a list of patient identifiers
     */
    public Map<String, String> getPatients() {
        Map<String, String> ret = new HashMap<String, String>();
        RPCConnection conn = null;
        try {
            conn = setConnection();
            if (conn != null) {
                for (FMPatient lpatient : new PatientRepository(conn).getAllPatients()) {
                    // ret.add( lpatient.getEnterprisePatientIdentifier() );
                    // log.finer(lpatient.getIEN());
                    //ret.put( new String[]{"id",lpatient.getIEN()}, new String[]{"name", lpatient.getName()} );
                    ret.put(lpatient.getIEN(), lpatient.getName());
                }
            } else {
                return null;
            }
            return ret;
        } catch (OvidDomainException e) {
            log.log(Level.SEVERE, "Error retrieving patient list", e);
            return null;
        } finally {
            closeConnection(conn);
        }
    }

    /**
     *  Get a list of patient identifiers
     */
    public Map<String, String> getPatientByName(String family, String given, String middle) {
        Map<String, String> ret = new HashMap<String, String>();
        RPCConnection conn = null;
        try {
            conn = setConnection();
            if (conn != null) {
                for (FMPatient lpatient : new PatientRepository(conn).searchByNameComponentsForPatients(family, given, middle)) {
                    // ret.add( lpatient.getEnterprisePatientIdentifier() );
                    // log.finer(lpatient.getIEN());
                    //ret.put( new String[]{"id",lpatient.getIEN()}, new String[]{"name", lpatient.getName()} );
                    ret.put(lpatient.getIEN(), lpatient.getName());
                }
            } else {
                return null;
            }
            return ret;
        } catch (OvidDomainException ode) {
            log.log(Level.SEVERE, "Error retrieving patient list", ode);
            return null;
        } finally {
            closeConnection(conn);
        }
    }

    /*    public void factorySetUp() {
    try {
    rpcConnFactory = new RPCBrokerPooledConnectionFactory(credentials.get(Repository.HOST_URL), credentials.get(Repository.PORT), credentials.get(Repository.ACCESS_CODE), credentials.get(Repository.VERIFY_CODE));
    } catch (Exception e) {
    log.severe("Connection to repository failed.  Credentials were " + credentials.toString());
    }
    }*/

    /*    }
     */
    /**
     *  sets the passed VistaLinkPooledConnection
     *  @return true if connection worked.  False otherwise.
     */
    public void propertySetUp() {
    	connectionProps = new VistaConnectionProperties();
        connectionProps.put("brokerType", "RPC Broker");
        connectionProps.put("server", credentials.get(Repository.HOST_URL));
        connectionProps.put("port", credentials.get(Repository.PORT));
        connectionProps.put("accessCode", credentials.get(Repository.ACCESS_CODE));
        connectionProps.put("verifyCode", credentials.get(Repository.VERIFY_CODE));
        LifecycleManager.getInstance().addListener(new LifecycleListener() {

            public void shutdown() {
                // On application shutdown, close any subjects in the cache
                SubjectCache.getInstance().dispose();
            }
        });

    }

    protected RPCConnection setConnection() throws OvidDomainException {
        RPCConnection conn = null;
        synchronized (this) {
            if (connectionProps == null) {



                propertySetUp();
            }
            try {

                conn = ServiceLocator.getInstance().getDataSource(connectionProps).getConnection();
                conn.setContext(FMUtil.FM_RPC_CONTEXT);
            } catch (VistaConnectionException rpcE) {
                log.severe("Connection to repository failed. Credentials were " + credentials.toString());
                return null;
            } catch (RPCException rpcE) {
                log.severe("Error setting context on connection: " + rpcE.getMessage());
                closeConnection(conn);
                return null;
            }
        }


        return conn;
    }

    /**
     *  closes the passed RPCBrokerConnection
     */
    protected void closeConnection(RPCConnection conn) {
        if (conn != null) {

            log.finer("closing connection");
            // conn.returnToPool();
            try {
                conn.close();
            } catch (RPCException rpcE) {
                log.severe("Error closing RPC connection: " + rpcE.getMessage());
            }
            conn = null;

        }
    }

    /**
     * Set credentials property.
     *
     *@param credentials New credentials property.
     */
    @Override
    public void setCredentials(HashMap<String, String> credMap) {

        if (credMap.get(Repository.HOST_URL) == null || credMap.get(Repository.HOST_URL).equals("")) {
            throw new RuntimeException("Must include hostURL for OpenVista database");
        }
        if (credMap.get(Repository.PORT) == null || credMap.get(Repository.PORT).equals("")) {
            throw new RuntimeException("Must include port for OpenVista database");
        }
        if (credMap.get(Repository.ACCESS_CODE) == null || credMap.get(Repository.ACCESS_CODE).equals("")) {
            throw new RuntimeException("Must include access code for OpenVista database");
        }
        if (credMap.get(Repository.VERIFY_CODE) == null || credMap.get(Repository.VERIFY_CODE).equals("")) {
            throw new RuntimeException("Must include verify code for OpenVista database");
        }

        credentials = credMap;
    }
    private  TreeMap<String, AllergyAgent> getAllergens()
    {
    	RPCConnection conn = null;
    	if (vistaAgents!= null)
    	{
    		return vistaAgents;
    	}
    	WriteLock lock = vistaAgentLock.writeLock();
    	if (vistaAgents == null)
    	{

    		try {
                conn = setConnection();

                if (conn != null) {

                    AllergyAgentRepository r = new AllergyAgentRepository(conn);
                    vistaAgents = new TreeMap<String, AllergyAgent>(new CaseInsensitiveComparator());
                    TreeSet<AllergyAgent> allergyAgents = r.getAllAllergyAgents();
                    for (AllergyAgent agent: allergyAgents)
                    {
                    	vistaAgents.put(agent.getDisplayName(), agent);
                    }
                   
        	}
    	}
    		catch (OvidDomainException ovidE) {
                log.severe("Error making connection");
            }  finally {
                closeConnection(conn);
                

            }
    	}
    	if (lock.isHeldByCurrentThread())
    	{
    		lock.unlock();
    	}
    	return vistaAgents;
    }
    private  TreeMap<String, FMSignSymptom> getReactions()
    {
    	RPCConnection conn = null;
    	if (vistaReactions!= null)
    	{
    		return vistaReactions;
    	}
    	WriteLock lock = vistaReactionLock.writeLock();
    	if (vistaReactions == null)
    	{

    		try {
                conn = setConnection();

                if (conn != null) {

                    SignSymptomRepository r = new SignSymptomRepository(conn);
                    TreeSet<FMSignSymptom> reactions = r.getAllSignsSymptoms(true);
                    vistaReactions = new TreeMap<String, FMSignSymptom>(new CaseInsensitiveComparator());
                  
                    for (FMSignSymptom reaction : reactions)
                    {
                    	vistaReactions.put(reaction.getName(), reaction);
                    	if (reaction.getSynonyms()!= null && reaction.getSynonyms().size()>0)
                    	{
                    		for (String syn : reaction.getSynonyms())
                    		{
                    			vistaReactions.put(syn, reaction);
                    		}
                    	}
                    }

        	}
    	}
    		catch (OvidDomainException ovidE) {
                log.severe("Error making connection");
            }  finally {
                closeConnection(conn);
                

            }
    	}
    	if (lock.isHeldByCurrentThread())
    	{
    		lock.unlock();
    	}
    	return vistaReactions;
    }
    public Set<String> lookup(String lookupType, String lookupChars)
    {
    	lookupChars = lookupChars.toLowerCase();
    	String endLookup = lookupChars;
    	int lLength = lookupChars.length();
    	if (lLength>0)
    	{
    		char nextChar = (char) (lookupChars.charAt(lLength-1)+1);
    		endLookup = lookupChars.substring(0,lLength-1) + nextChar;
    	}
    	if (lookupType.equals("reactions"))
    	{
    		getReactions();

    		if (lLength == 0)
    		{
    			return vistaReactions.keySet();
    		}
    		return vistaReactions.subMap(lookupChars, true, endLookup, false).keySet();
    	}else if (lookupType.equals("allergens"))
    	{
    		getAllergens();
    		if (lLength == 0)
    		{
    			return vistaAgents.keySet();
    		}
    		return vistaAgents.subMap(lookupChars, true, endLookup, false).keySet();
    	}
    	return null;
    }
    @Override
    public List<Allergy> getAllergies(String id) {
        List<Allergy> list = new ArrayList<Allergy>();
        RPCConnection conn = null;
        try {
            conn = setConnection();

            if (conn != null) {

                PatientAllergyRepository r = new PatientAllergyRepository(conn);

                Collection<FMPatient_Allergies> vista_list = r.getAllergiesForPatientIEN(id, false);
    
                // an ArrayList of PatientAllergies objects is returned -  converty to hData Allergy type
                for (FMPatient_Allergies a : vista_list) {
                    Allergy allergy = new Allergy();  //hData type

                    //populate

                    //set time for adverse reaction
                    if (a.getOriginationDateTime() != null) {
                        GregorianCalendar cal = new GregorianCalendar();
                        cal.setTime(a.getOriginationDateTime());
                        DatatypeFactory factory = DatatypeFactory.newInstance();
                        Interval inter = new Interval();
                        inter.setValue(factory.newXMLGregorianCalendar(cal));
                        allergy.setEffectiveTime(inter);
                        allergy.setTime(inter.getValue().toString());
                     
                    }
                    allergy.setDescription(capitalizeString(a.getReactant()));
                    
                    String symptoms = "";
                    for (FMPatientAllergyReaction react : a.getReactions().values()) {
                        if (!symptoms.equals("")) {
                            symptoms = symptoms + "; ";
                        }
                        symptoms = symptoms + react.getReactionValue();
              
                  
                    }
                    Code reaction = new Code();
                    reaction.setDisplayName(capitalizeString(symptoms));
                    reaction.setOriginalText(capitalizeString(symptoms));
                    allergy.setReaction(reaction);

                    String code = "";
                    String val = "";
                    log.severe(a.getAllergyType());
                    if (a.getAllergyType().equals(FMPatient_Allergies.FOOD_ALLERGY)) {
                        code = "414285001";
                        val = "food allergy";
                    } else if (a.getAllergyType().equals(FMPatient_Allergies.DRUG_ALLERGY)) {
                        code = "416098002";
                        val = "drug allergy";
                    } else {
                        code = "419199007";
                        val = "allergy to substance";
                    }
                    Code advEventType = new Code();

                    advEventType.setCode(code);
                    advEventType.setDisplayName(val);
                    advEventType.setOriginalText(val);
                    advEventType.setCodeSystem("2.16.840.1.113883.6.96");
                    advEventType.setCodeSystemName("SNOMED CT");
                    allergy.setType(advEventType);
                    String narr = "";
                    if (a.getComments() != null) {
                        for (FMPatientAllergyComment comment : a.getComments().values()) {
                            if (!narr.equals("")) {
                                narr = narr + "; ";
                            }
                            narr = narr + comment.getComments();
                        }
                    }
                    
                    //add to the list
                    list.add(allergy);
                }
            } else {
                log.warning("BAD CONNECTION");

            }

        } catch (OvidDomainException ovidE) {
            log.severe("Error making connection");
        } catch (DatatypeConfigurationException dataE) {
            log.severe("Error with data type configuration");
        }  finally {
            closeConnection(conn);

        }
        return list;
    }

   
   
    public List<Support> getSupportInfo(String id) {
        List<Support> list = new ArrayList<Support>();
        RPCConnection conn = null;
        try {
            conn = setConnection();
            if (conn != null) {
                PatientRepository patRepository = new PatientRepository(conn);
                Collection<String> ids = new ArrayList<String>();
                ids.add(id);
                Collection<FMPatientContact> fmPatientList = patRepository.getContacts(ids);
                for (FMPatientContact fmPatient : fmPatientList) {
                    Collection<FMPatientContact.ContactInfo> contactList = fmPatient.getContacts();
                    for (FMPatientContact.ContactInfo contact : contactList) {
                       
                    	
                    	FMPatientContact.ContactType cType = contact.getType();
                        switch (cType) {
                            case NEXT_OF_KIN:
                            case EMERGENCY:
                            case GUARDIAN:
                            case DESIGNEE:
                                fillInContactInfo(list, contact);
                                break;
                            default:
                        }


                    }
                }
                log.finer("Number of contacts for patient " + id + " is " + list.size());
            } else {
                log.warning("BAD CONNECTION");
            }

        } catch (Throwable e) {
            log.throwing(KEY, "Error retreiving PatientItems", e);
        } finally {
            closeConnection(conn);

        }
        return list;
    }

    public List<Medication> getMedications(String id) {
        List<Medication> list = new ArrayList<Medication>();
        RPCConnection conn = null;
        try {
            conn = setConnection();
            if (conn != null) {
                PatientItemRepository r = new PatientItemRepository(conn, conn, "MSC PATIENT DASHBOARD");
                Collection<IsAPatientItem> vista_list = r.getMedications(id);
                // an ArrayList of PatientAllergies objects is returned -  converty to hData Medication type
                for (IsAPatientItem a : vista_list) {
                	ObjectFactory oFact = new ObjectFactory();
                    Medication medication = new Medication();  //GreenCDA type
                    PatientMedication pa = (PatientMedication) a;  //vista (ovid) type
                    //populate
                    //message -> narrative
                    String patientInstructions = capitalizeString(pa.getMessage(), true);

                    String medName = capitalizeString(pa.getMedName(), true);

                    //set time for adverse reaction
                    if (pa.getDateTime() != null) {
                        GregorianCalendar cal = new GregorianCalendar();
                       
                        cal.setTime(pa.getDateTime());
                        DatatypeFactory factory = DatatypeFactory.newInstance();
                        Interval inter = oFact.createInterval();
                        inter.setValue(factory.newXMLGregorianCalendar(cal));
                    
                        String displayDate = parseDate(cal.getTime().getTime(), true);
                        medication.setTime(displayDate);
                        medication.setStart_time(displayDate);
                        
                        medication.setEffectiveTime(inter);
                    }

                    
                    OrderInformation orderInfo = new OrderInformation();
                  
                    String doseStr = capitalizeString(pa.getDose(), true);
                    Code dose = oFact.createCode();
                    dose.setDisplayName(doseStr);
                    
                    Code delivery = oFact.createCode();
                    delivery.setDisplayName(capitalizeString(pa.getDelivery(), true));
                    medication.setDeliveryMethod(delivery);
  
                    medication.setPatientInstructions(patientInstructions + " " + pa.getFrequency());
                    //add to the list
                    String fullText = medName + ":" + patientInstructions + ": Delivery : " + delivery.getDisplayName() ;
                    medication.setDescription(fullText);
                    Code typeCode = oFact.createCode();
                    typeCode.setDisplayName(medName);
                    typeCode.setCodeSystem("None");
                    medication.setType(typeCode);
                    list.add(medication);
                }
                log.finer("Number of medications for patient " + id + " is " + list.size());
            } else {
                log.warning("BAD CONNECTION");
            }

        } catch (Throwable e) {
            log.throwing(KEY, "Error retreiving PatientItems", e);
        } finally {
            closeConnection(conn);

        }
        return list;
    }

    public List<Immunization> getImmunizations(String id) {
        List<Immunization> list = new ArrayList<Immunization>();
        RPCConnection conn = null;
        try {
            conn = setConnection();
            if (conn != null) {
                PatientItemRepository r = new PatientItemRepository(conn, conn, "MSC PATIENT DASHBOARD");
                Collection<PatientImmunization> vista_list = r.getImmunizations(id);

                for (PatientImmunization imm : vista_list) {
                    Immunization immunization = fillInImmunizationInfo(imm);
                    list.add(immunization);
                }
                log.finer("Number of immunizations for patient " + id + " is " + list.size());
            } else {
                log.warning("BAD CONNECTION");
            }

        } catch (Throwable e) {
            log.throwing(KEY, "Error retreiving PatientItems", e);
        } finally {
            closeConnection(conn);

        }
        return list;
    }

    private List<Result> getVitals(String id, boolean latest) {
        RPCConnection conn = null;
        List<Result> results = new ArrayList<Result>();
        // RPCBrokerConnection conn = null;
        try {
            conn = setConnection();
            //conn = new RPCBrokerConnection("medcafe.mitre.org", 9201, "OV1234", "OV1234!!");
            if (conn != null) {
                log.finer("Connection made . . .");
                conn.setContext("MSC PATIENT DASHBOARD");
                PatientItemRepository r = new PatientItemRepository(conn, conn, "MSC PATIENT DASHBOARD");
  
                GregorianCalendar calendar = new GregorianCalendar();

                calendar.roll(Calendar.YEAR, -100);
                Date earlyDate = calendar.getTime();
                Collection<IsAPatientItem> vitalItems = r.getVitals(id, earlyDate, new Date());
                Collection<PatientVitalEvent> vitals = new ArrayList<PatientVitalEvent>();
                for (IsAPatientItem vitalItem : vitalItems) {
                    PatientVitalEvent item = (PatientVitalEvent) vitalItem;
                    vitals.add(item);
                }
                if (vitals.size() > 0) {
                    if (latest) {
                        PriorityQueue<PatientVitalEvent> vitalQueue = new PriorityQueue<PatientVitalEvent>(vitals.size(), new MostRecentVitalComparator());
                        vitalQueue.addAll(vitals);
                        vitals = new ArrayList<PatientVitalEvent>();
                        vitals.add(vitalQueue.peek());
                    } else {
                        PriorityQueue<PatientVitalEvent> vitalQueue = new PriorityQueue<PatientVitalEvent>(vitals.size(), new EarliestVitalComparator());
                        vitalQueue.addAll(vitals);
                        vitals = new ArrayList<PatientVitalEvent>();
                        while (!vitalQueue.isEmpty()) {
                            vitals.add(vitalQueue.poll());
                        }
                    }
                }
                ObjectFactory oFact = new ObjectFactory();
                for (PatientVitalEvent vital : vitals) {

                    if (vital.getDateTime() != null) {
                        GregorianCalendar cal = new GregorianCalendar();
                        cal.setTime(vital.getDateTime());
                        
                        DatatypeFactory factory = DatatypeFactory.newInstance();
                       
                        Collection<VitalSignDetail> details = vital.getDetails();
                        for (VitalSignDetail detail : details) {
                        	if (detail.getName().equals("B/P"))
                        	{
                        		results.add(createVital("systolic blood pressure", detail.getValue().split("\\/")[0], detail.getUnits(), detail, cal, factory, oFact));
                        		results.add(createVital("diastolic blood pressure", detail.getValue().split("\\/")[1], detail.getUnits(), detail, cal, factory, oFact));
                        	}
                        	else
                        	{
                        		results.add(createVital(detail.getName(), detail.getValue(), detail.getUnits(), detail, cal, factory, oFact));
                        		if (detail.getBmi() != null && !detail.getBmi().equals(""))
                        		{
                        			results.add(createVital("BMI", detail.getBmi(), "", detail, cal, factory, oFact));
                        		}
                        	}
                        	
                            
                        }
                    }
                }
            }
            //conn.close();
        } catch (Throwable e) {
            log.severe("VistaRepository: Error retrieving patient vitals. " + e.getMessage());

        } finally {
            closeConnection(conn);
        }
        return results;
    }

    public List<Result> getLatestVitals(String id) {
        return getVitals(id, true);
    }

    public List<Result> getAllVitals(String id) {
        return getVitals(id, false);
    }

    public List<Condition> getProblems(String id) {
        List<Condition> list = new ArrayList<Condition>();
        RPCConnection conn = null;
        try {
            conn = setConnection();
            if (conn != null) {
                log.finer("Connection made...");
                PatientItemRepository r = new PatientItemRepository(conn, conn, "MSC PATIENT DASHBOARD");
              
                Collection<PatientProblem> vista_list = r.getProblems(id);
                // an ArrayList of PatientProblem objects is returned -  converty to hData Condition type
                for (PatientProblem p : vista_list) {
                    Condition problem = new Condition();  //hData type

                    //populate


                    if (p.getDateTime() != null) {
                        GregorianCalendar cal = new GregorianCalendar();
                        cal.setTime(p.getDateTime());
                        DatatypeFactory factory = DatatypeFactory.newInstance();
                       
                        Interval inter = new Interval();
                        inter.setValue(factory.newXMLGregorianCalendar(cal));
             
                        problem.setEffectiveTime(inter);
                        problem.setTime(cal.toString());
                        problem.setStart_time(shortDf.format(cal.getTime()));
                    }
                    Code pcode = new Code();
                    pcode.setCode(p.getIcd());
                    problem.setCode(pcode);
                    
                    problem.setDescription(p.getMessage());
                    problem.setFreeText(p.getStatus());
                  

                    //add to the list
                    list.add(problem);
                }
            } else {
                log.warning("BAD CONNECTION");
            }

        } catch (Throwable e) {
            log.throwing(KEY, "Error retreiving PatientItems", e);
        } finally {
            closeConnection(conn);

        }
        return list;
    }

    private boolean stringExists(String testString) {
        return ((testString != null) && (!testString.equals("")));
    }

    private void setPhoneNumber(List<Telecom> teleList, String telephone, String type, String use) {
        if (stringExists(telephone)) {
            Telecom telecom = new Telecom();
            telecom.setValue(telephone);
            telecom.setUse(use);
            teleList.add(telecom);
        }
    }

    private void fillInContactInfo(List<Support> list, FMPatientContact.ContactInfo contact) {
    	Support support = new Support();
    	
    	PersonalName personName = new PersonalName();
    	String[] nameParts = contact.getName().split(",");
    	personName.setFamilyName(nameParts[0]);
    	String given = personName.getGivenName();



    	support.setName(personName);
    	support.setMothersMaidenName("");
    	support.setId("");
    	ObjectFactory factory = new ObjectFactory();

    	Address address = factory.createAddress();
    	String temp = contact.getStreet1();
    	if (temp != null && !temp.equals(""))
    	{
    		address.getContent().add(factory.createAddressStreet(temp));
    	}
    	temp = contact.getStreet2();
    	if (temp != null && !temp.equals(""))
    	{
    		address.getContent().add(factory.createAddressStreet(temp));
    	}
    	temp = contact.getCity();
    	if (temp != null && !temp.equals(""))
    	{
    		address.getContent().add(factory.createAddressCity(temp));
    	}
    	temp = contact.getState();
    	if (temp != null && !temp.equals(""))
    	{
    		address.getContent().add(factory.createAddressState(temp));
    	}
    	temp = contact.getZip();
    	if (temp != null && !temp.equals(""))
    	{
    		address.getContent().add(factory.createAddressPostalCode(temp));
    	}
    	if (address.getContent().size()>0)
    	{
    		support.getAddress().add(address);
    	}
    	List<Telecom> teleList =support.getTelecom();
    	setPhoneNumber(teleList, contact.getPhoneNumber(), "phone-landline", "home");
    	setPhoneNumber(teleList, contact.getAltPhoneNumber(), "phone-landline", "work");

    	String relationship = contact.getRelationshipToPatient();


    	if (relationship != null && !relationship.equals(""))
    	{
    		support.setRelationship(relationship);
    	}
    	else
    	{
    		support.setRelationship("");
    	}
    	switch (contact.getType()) {
    		case NEXT_OF_KIN:
    			support.setType("Next of Kin");
    			break;
    		case GUARDIAN:
    			support.setType("Guardian");
    		case DESIGNEE:
    			support.setType("Emergency Contact");
    			break;
    		case EMERGENCY:
    			support.setType("Emergency Contact");
    			break;

    	}
        list.add(support);
    }

   
    private PersonalName setPersonsName(String fullname) {
    	PersonalName personsName = new PersonalName();
        String[] nameParts = fullname.split(",");
        personsName.setFamilyName(nameParts[0]);
        String given = personsName.getGivenName();
       
        return personsName;
    }

    public List<Encounter> getPatientEncounters(String id) {
        Collection<PatientVisit> ret;

        RPCConnection conn = null;
        try {
            DatatypeFactory factory = DatatypeFactory.newInstance();
            conn = setConnection();
            if (conn != null) {
                ret = new PatientVisitRepository(conn, "MSC PATIENT DASHBOARD").getVisitsByPatientDFN(id);


            } else {
                return null;
            }
            List<Encounter> visits = new ArrayList<Encounter>();


            for (PatientVisit visit : ret) {
            	
                Encounter encounter = new Encounter();
                Codes codes = new Codes();
                encounter.setCodes(codes);
                ArrayList<String> icd9List = new ArrayList<String>();
                codes.setICD_9_CM(icd9List);
                encounter.set_type("Encounter");
                visit.getPOVs();
                String description;
                if (visit.getVisit().getPatientInOut().equalsIgnoreCase("in"))
                {
                	description = "Hospitalization: ";
                }
                else
                {
                	description = "Outpatient: ";
                }
                for (FMV_POV pov : visit.getPOVs())
                {
                	FMICD_Diagnosis diag = pov.getDiagnosis();
                	
                	icd9List.add(diag.getIcd9());
                	description += diag.getDescription().toLowerCase() + "; ";
                }
                encounter.setMood_code("EVN");
                encounter.setDescription(description);
                encounter.setTime(shortDf.format(visit.getVisit().getVisitDate()));
                Interval time = new Interval();
                GregorianCalendar cal = new GregorianCalendar();
                cal.setTime(visit.getVisit().getVisitDate());
                time.setValue(factory.newXMLGregorianCalendar(cal));
                encounter.setEffectiveTime(time);
                visits.add(encounter);

            }

            return visits;
        } catch (OvidDomainException e) {
            log.log(Level.SEVERE, "Error retrieving patient visits", e);
            return null;
        } catch (Throwable e) {
            log.log(Level.SEVERE, "Error retreiving PatientItems", e);
            return null;
        } finally {
            closeConnection(conn);

        }
    }

    private String capitalizeString(String toBeFixed) {
        return capitalizeString(toBeFixed, false);
    }
    
    private String capitalizeString(String toBeFixed, boolean medString) {
        if (toBeFixed != null && !toBeFixed.equals("")) {
            String returnString = "";
            toBeFixed = toBeFixed.toLowerCase();
            String delimiters = ",/.;:-_()&!? []{}0123456789";
            StringTokenizer parts = new StringTokenizer(toBeFixed, delimiters, true);
            while (parts.hasMoreTokens()) {
                String word = parts.nextToken();
                if (delimiters.contains(word)) {
                    returnString = returnString + word;
                } else {
                    returnString = returnString + word.substring(0, 1).toUpperCase() + word.substring(1);
                }
            }
            if (!medString) {
                return returnString;
            } else {
                returnString = returnString.replace("Mg", "mg");
                return returnString.replace("Ml", "mL");
            }
        }
        return toBeFixed;
    }
    private Immunization fillInImmunizationInfo(PatientImmunization imm)
    {
    	Immunization gcImm = new Immunization();
    	ObjectFactory oFact = new ObjectFactory();

    	gcImm.set_type("Immunization");
    	gcImm.setDescription(imm.getImmunizationName());
    	gcImm.setRefused(false);
    	if (imm.getSeries()!= null && !imm.getSeries().equals(""))
    	try {
    		gcImm.setSeriesNumber(BigInteger.valueOf(Long.parseLong(imm.getSeries())));

    	}
    	catch(Exception e)
    	{
    		// not an integer doesn't translate, so no entry
    	}
    	gcImm.setStatus(imm.getStatus());
    	try {
        if (imm.getImmunizationDate() != null) {
            GregorianCalendar cal = new GregorianCalendar();
           
            cal.setTime(imm.getDateTime());
            DatatypeFactory factory = DatatypeFactory.newInstance();
           
            Interval interval = new Interval();
            interval.setValue(factory.newXMLGregorianCalendar(cal));
            gcImm.setEffectiveTime(interval);
            
            String displayDate = parseDate(cal.getTime().getTime(), true);
           gcImm.setTime(displayDate);
         
        }
    	}
    	catch(DatatypeConfigurationException dE)
    	{
    		log.log(Level.SEVERE, dE.getMessage());
    	}
        gcImm.setFreeText(imm.getImmunizationName() + " given by " + imm.getEncounterProvider());
        gcImm.set_type("Immunization");
        gcImm.setMood_code("EVN");
        try {
        if (imm.getSeries() != null)
        {
        gcImm.setSeriesNumber(BigInteger.valueOf(Long.parseLong(imm.getSeries())));
        }
        }
        catch(NumberFormatException e)
        {
        	// don't do anything doesn't convert to a number
        }
        if (imm.getContraindicated()!= null && !imm.getContraindicated().equals(""))
        {
        	Code refusal = new Code();
        	refusal.setOriginalText(imm.getContraindicated());
        	refusal.setDisplayName(imm.getContraindicated());
        	gcImm.setRefusalReason(refusal);
        	gcImm.setRefused(true);
        }
        return gcImm;

    }

	@Override
	public boolean insertAllergies(String patientId,
			Collection<Allergy> allergies) throws NotImplementedException {

		if (allergies==null ||allergies.size()==0)
		{
			return false;
		}
		boolean success =true;
		RPCConnection conn = null;
		try {
			conn = setConnection();

			if (conn != null) {

				PatientAllergyRepository r = new PatientAllergyRepository(conn);
				for (Allergy allergy : allergies)
				{
					FMPatient_Allergies fmAllergy = new FMPatient_Allergies();

					fmAllergy.setPatient(Integer.parseInt(patientId)); 
					vistaAgentLock.readLock().lock(); 

					AllergyAgent returnAgent = vistaAgents.floorEntry(allergy.getDescription()).getValue(); 
					// if the specified allergy exists in the table, proceed to build allergy record;
					if (returnAgent != null && returnAgent.getDisplayName().compareToIgnoreCase(allergy.getDescription())==0) { 
						fmAllergy.setAllergy(returnAgent.getLookupFile().getFilenum(), returnAgent.getAllergenRecord().getIEN()); 
						fmAllergy.setReactant(returnAgent.getDisplayName().toUpperCase()); 
						// Originator has to be in the new person file, and since we aren't currently using that value 
						// the originator is hard coded in
						fmAllergy.setOriginator(42); 
						FMPatientAllergyComment comment = new FMPatientAllergyComment(); 
						comment.setComments(allergy.getFreeText());
						FMPatientAllergyComment severity = new FMPatientAllergyComment();
						severity.setComments(allergy.getSeverity().getDisplayName());

						fmAllergy.addComments(comment);
						fmAllergy.addComments(severity);
						String reactionList = allergy.getReaction().getDisplayName(); 
						String[] reacts = reactionList.split(";");
						for (String react : reacts) { 
							FMPatientAllergyReaction fmReact = new FMPatientAllergyReaction(); 
							fmReact.setDateEntered(allergy.getEffectiveTime().getValue().toGregorianCalendar().getTime());   
							vistaReactionLock.readLock().lock(); 
							try {
								FMSignSymptom returnSign = vistaReactions.get(react);
								if (returnSign != null ) { 
									fmReact.setReactionIEN(returnSign);
									fmAllergy.addReactions(fmReact);
								} 
							}
							finally 
							{ 
								vistaReactionLock.readLock().unlock(); 
							}
						} 
						// Origination date must be set, if not returned default to current day 
						if (allergy.getEffectiveTime()!= null && allergy.getEffectiveTime().getValue() != null)
						{ 
							fmAllergy.setOriginationDateTime(allergy.getEffectiveTime().getValue().toGregorianCalendar().getTime());
						} 
						else 
						{
							GregorianCalendar cal = new GregorianCalendar();
							fmAllergy.setOriginationDateTime(cal.getTime());
						} 
						//Observed or historical field must be set; hData doesn't pass that information so default to historical 
						fmAllergy.setObservedOrHist("h");
						if (allergy.getType().getCode().equals("414285001")) //food allergy 
						{
							fmAllergy.setAllergyType(FMPatient_Allergies.FOOD_ALLERGY); 
						} else if (allergy.getType().getCode().equals("416098002")) { 
							fmAllergy.setAllergyType(FMPatient_Allergies.DRUG_ALLERGY);
						} else if (allergy.getType().getCode().equals("419199007")) {
							fmAllergy.setAllergyType(FMPatient_Allergies.OTHER_ALLERGY);
						} 
						try {
							r.addAllergy(fmAllergy); 
						}
						catch (InsertFileManRecordException insertE) { 
							log.severe(insertE.getMessage() + " for patient id: " + patientId + ", allergen: " + allergy.getDescription()); 
							success = false; 
						}
						catch(Exception e) 
						{ 
							log.severe(e.getMessage());
							success = false;
							e.printStackTrace();
							if (fmAllergy!= null)
								log.severe(fmAllergy.toString()); 
						}
						finally{ 
							vistaAgentLock.readLock().unlock(); 
						} 
					} else { 
						log.severe("Can't insert allergy to " + allergy.getDescription()
								+ " no match found.");
						success = false;
					} 
				}
			}
		} 
		catch (OvidDomainException e) {
			log.log(Level.SEVERE, "Error inserting patient allergies", e);
			return false;
		} catch (Throwable e) {
			log.log(Level.SEVERE, "Error inserting patient allergies", e);
			return false;
		} finally {
			closeConnection(conn);

		}
		return true;
}

	@Override
	public List<SocialHistory> getSocialHistory(String patientId)
			throws NotImplementedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FMRecord> getTimeLineInfo(String ien)
			throws NotImplementedException {
		// TODO Auto-generated method stub
		return null;
	}
@Override
public List<Result> getResults(String patientId){
	  Collection<IsAPatientItem> ret;
	    RPCConnection conn = null;
	    try {
	    DatatypeFactory factory = DatatypeFactory.newInstance();
	    List<DisplayGroupType> groupTypes = Arrays.asList(resultTypes);
	    List<OrderStatusType> statusTypes = Arrays.asList(resultStatuses);
	    GregorianCalendar calendar = new GregorianCalendar();
	    calendar.set(1876, 0,1);
	    Date afterDate = calendar.getTime();
	    try {
	        conn = setConnection();
	        if (conn != null) {
	            ret = new PatientItemRepository(conn, conn, "MSC PATIENT DASHBOARD").getResults(patientId, conn.getDUZ(), afterDate, new Date(), true);


	        } else {
	            return null;
	        }
	        List<Result> results = new ArrayList<Result>();


	        for (IsAPatientItem order : ret) {
	        	log.finer(order.toString());
	        	PatientResult res = (PatientResult) order;
	        	GregorianCalendar cal = new GregorianCalendar();
                
                cal.setTime(res.getDateTime());
                Interval interval = new Interval();
                interval.setValue(factory.newXMLGregorianCalendar(cal));
                
        
                 
              
	        	for (ResultDetail det : res.getDetails())
	        	{
		        	Result result = new Result();
	        		result.setDescription(det.getTestName());
	        		result.setEffectiveTime(interval);
	        		result.setMood_code("EVN");
	        		result.setTime(shortDf.format(res.getDateTime()));
	        		result.setReferenceRange(det.getReferenceRange());
	        		try{
	        		Quantity quantity = new Quantity();
	        		quantity.setAmount(Float.parseFloat(det.getValue()));
	        		quantity.setUnit(det.getUnits());
	        		result.setValue(quantity);
	        		}
	        		catch (Exception e)
	        		{
	        			//can't evaluate the value as a float
	        		}
	        		Values values = new Values();
	        		values.set_type("PhysicalQuantityResultValue");
	        		values.setScalar(det.getValue());
	        		values.setUnits(det.getUnits());
	        		ArrayList<Values>valueList = new ArrayList<Values>();
	        		valueList.add(values);
	        		result.setValues(valueList);
	        		Codes codes = new Codes();
	        		ArrayList<String> loincList = new ArrayList<String>();
	        		loincList.add(det.getLoincCode());
	        		codes.setLOINC(loincList);
	        		result.setCodes(codes);
	        		SimpleCode interpretationCode = new SimpleCode();
	        		interpretationCode.setDisplayName(det.getIndicator());
	        		result.setInterpretation(interpretationCode);
	        		results.add(result);

	        	}
	        	
	        }
	        return results;
	        
	    } catch (OvidDomainException e) {
	        log.log(Level.SEVERE, "Error retrieving patient visits", e);
	        return null;
	    } catch (Throwable e) {
	        log.log(Level.SEVERE, "Error retreiving PatientItems", e);
	        return null;
	    } finally {
	        closeConnection(conn);

	    }
	    }
catch(DatatypeConfigurationException dE)
{
	log.log(Level.SEVERE, dE.getMessage());
	return null;
}
}
	@Override
	public List<Procedure> getProcedures(String patientId) {
	  Collection<PatientVisit> ret;
    RPCConnection conn = null;
    try {
        conn = setConnection();
        if (conn != null) {
            ret = new PatientVisitRepository(conn, "MSC PATIENT DASHBOARD").getVisitsByPatientDFN(patientId);


        } else {
            return null;
        }
        List<Procedure> procedures = new ArrayList<Procedure>();


        for (PatientVisit visit : ret) {
        	log.finer(visit.toString());
        	Interval visitInterval = new Interval();
        	DatatypeFactory dtFactory = DatatypeFactory.newInstance();
        	GregorianCalendar cal = new GregorianCalendar();
        	cal.setTime(visit.getVisit().getVisitDate());
        	visitInterval.setValue(dtFactory.newXMLGregorianCalendar(cal));
      
        	
            for (FMV_PatientEd pEd : visit.getPatientEd())
            {
                Procedure procedure = new Procedure();
                procedure.set_type("_Procedure");
                procedure.setMood_code("EVN");
                procedure.setDescription(capitalizeString(pEd.getTopicValue()));
                if (pEd.getEventDate()!= null)
                {
                	Interval interval = new Interval();
                	GregorianCalendar tempCal = new GregorianCalendar();
                	tempCal.setTime(pEd.getEventDate());
                	interval.setValue(dtFactory.newXMLGregorianCalendar(tempCal));
                	procedure.setEffectiveTime(interval);
                	procedure.setTime(shortDf.format(pEd.getEventDate()));

                }
                else
                {
                	procedure.setTime(shortDf.format(visit.getVisit().getVisitDate()));
                	procedure.setEffectiveTime(visitInterval);
                }
                procedures.add(procedure);
                
            }
            for (FMV_SkinTest test :visit.getSkinTests())
            {
            	Procedure procedure = new Procedure();
            	 procedure.set_type("_Procedure");
                 procedure.setMood_code("EVN");
                 procedure.setDescription(capitalizeString(test.getSkinTestValue() + " " + test.getResults() + " " + test.getComments()));
                 procedure.setTime(shortDf.format(test.getEventDate()));
                 Interval interval = new Interval();
             	GregorianCalendar tempCal = new GregorianCalendar();
             	tempCal.setTime(test.getEventDate());
             	interval.setValue(dtFactory.newXMLGregorianCalendar(tempCal));
             	procedure.setEffectiveTime(interval);
                 procedures.add(procedure);
            	
            }
            for (FMV_Treatment trmt: visit.getTreatments())
            {
            	Procedure procedure = new Procedure();
           	 	procedure.set_type("_Procedure");
                procedure.setMood_code("EVN");
                procedure.setDescription(capitalizeString(trmt.getTreatmentValue()));
                procedure.setTime(shortDf.format(trmt.getEventDate()));
                Interval interval = new Interval();
            	GregorianCalendar tempCal = new GregorianCalendar();
            	tempCal.setTime(trmt.getEventDate());
            	interval.setValue(dtFactory.newXMLGregorianCalendar(tempCal));
            	procedure.setEffectiveTime(interval);
                procedures.add(procedure);
            }
            
           

        }

        return procedures;
    } catch (OvidDomainException e) {
        log.log(Level.SEVERE, "Error retrieving patient visits", e);
        return null;
    } catch (Throwable e) {
        log.log(Level.SEVERE, "Error retreiving PatientItems", e);
        return null;
    } finally {
        closeConnection(conn);

    }
	}
	private Result createVital(String vitalName, String vitalQuantity, String vitalUnit, VitalSignDetail vital, GregorianCalendar cal, DatatypeFactory factory, ObjectFactory oFact)
	{
		  Result result = oFact.createResult();
          Values values = new Values();
          
          values.set_type(PHYS_QUANTITY);
         
          values.setScalar(vitalQuantity);
          values.setUnits(vitalUnit);
          ArrayList<Values> valList = new ArrayList<Values>();
          valList.add(values);
          result.setValues(valList);
          
          Interval inter = new Interval();

          inter.setValue(factory.newXMLGregorianCalendar(cal));
          result.setTime(shortDf.format(cal.getTime()));
          result.setEffectiveTime(inter);
          result.set_type(LAB_RESULT);
          result.setDescription(vitalName);
          result.setMood_code(EVENT_MOOD_CODE);

          Code resultType = oFact.createCode();
          Quantity quantity = oFact.createQuantity();
          quantity.setUnit(vitalUnit);
          quantity.setAmount(Float.parseFloat(vitalQuantity));
          result.setValue(quantity);
       
          resultType.setDisplayName(vitalName);
          
         
          result.setCode(resultType);

          String indicator = vital.getIndicator();
          if (stringExists(vital.getSo2())) {
              indicator = indicator + " " + vital.getSo2();
          }
          if (indicator != null && ! indicator.equals(""))
          {
        	  resultType.setDisplayName(vitalName + " - " + indicator);
          }
          return result;
	}

	 private class MostRecentVitalComparator implements Comparator<PatientVitalEvent> {

	        public MostRecentVitalComparator() {
	            super();
	        }

	        public int compare(PatientVitalEvent one, PatientVitalEvent two) {
	            return two.getDateTime().compareTo(one.getDateTime());
	        }
	    }

	    private class EarliestVitalComparator implements Comparator<PatientVitalEvent> {

	        public EarliestVitalComparator() {
	            super();
	        }

	        public int compare(PatientVitalEvent one, PatientVitalEvent two) {
	            return -(two.getDateTime().compareTo(one.getDateTime()));
	        }
	    }

		@Override
		public Person getPerson(String userName, String patientId) {
			    Person person = new Person();
				ObjectFactory factory = new ObjectFactory();
		        RPCConnection conn = null;
		        try {
		            conn = setConnection();
		            if (conn != null) {
		                PatientRepository patRepository = new PatientRepository(conn);
		                Collection<String> ids = new ArrayList<String>();
		                ids.add(patientId);
		                Collection<FMPatientContact> fmPatientList = patRepository.getContacts(ids);
		          
		                for (FMPatientContact fmPatient : fmPatientList) {
		                    PersonalName name = new PersonalName();
		                    name.setGivenName(fmPatient.getGivenName());
		                    name.setFamilyName(fmPatient.getFamilyName());
		                    name.setTitle(fmPatient.getPrefix());
		                    person.setName(name);
		                    List<Address> addresses = person.getAddress();
		                    Address address = new Address();
		                    List<Serializable>  content = address.getContent();
		                    content.add(factory.createAddressStreet(fmPatient.getStreetAddressLine1()));
		                    content.add(factory.createAddressStreet(fmPatient.getStreetAddressLine2()));
		                    content.add(factory.createAddressCity(fmPatient.getCity()));
		                    content.add(factory.createAddressState(fmPatient.getStateValue()));
		                    content.add(factory.createAddressPostalCode(fmPatient.getZipCode()));
		                    addresses.add(address);
		                    List<Telecom> telecoms = person.getTelecom();
		                    Telecom telecom = new Telecom();
		                    telecom.setPreferred(false);
		                    telecom.setUse("Home");
		                    telecom.setValue(fmPatient.getPhoneNumberResidence());
		                    telecoms.add(telecom);
		                    telecom = new Telecom();
		                    telecom.setPreferred(false);
		                    telecom.setUse("Cell");
		                    telecom.setValue(fmPatient.getPhoneNumberCellular());
		                    telecoms.add(telecom);
		                    telecom = new Telecom();
		                    telecom.setPreferred(false);
		                    telecom.setUse("Work");
		                    telecom.setValue(fmPatient.getPhoneNumberWork());
		                    telecoms.add(telecom);
		                    telecom = new Telecom();
		                    telecom.setPreferred(false);
		                    telecom.setUse("Pager");
		                    telecom.setValue(fmPatient.getPagerNumber());
		                    telecoms.add(telecom);
		                    
		                    
		                    
		                }
		               
		            } else {
		                log.warning("BAD CONNECTION");
		            }

		        } catch (Throwable e) {
		            log.throwing(KEY, "Error retreiving PatientItems", e);
		        } finally {
		            closeConnection(conn);

		        }
		        return person;
		}

	    private class CaseInsensitiveComparator implements Comparator<String>
	    {

			@Override
			public int compare(String arg0, String arg1) {
				return arg0.compareToIgnoreCase(arg1);
			}
	    	
	    }
}
