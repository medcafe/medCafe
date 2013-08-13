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

import java.sql.Time;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;


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
    public final static String EVENT_MOOD_CODE = "EVN"
    private TreeSet<AllergyAgent> vistaAgents;
    private TreeSet<FMSignSymptom> vistaReactions;
    private ReentrantReadWriteLock vistaAgentLock;
    private ReentrantReadWriteLock vistaReactionLock;
    // static{log.setLevel(Level.FINER);}
    //protected static VistaLinkPooledConnectionFactory factory = null;
    // protected RPCBrokerConnection conn = null;
    // protected RPCBrokerPooledConnectionFactory rpcConnFactory = null;
    protected VistaConnectionProperties connectionProps = null;

    public VistaRepository(HashMap<String, String> credMap) {
        super(credMap);

        type = "VistA";
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

                    
                    String symptoms = "";
                    for (FMPatientAllergyReaction react : a.getReactions().values()) {
                        if (!symptoms.equals("")) {
                            symptoms = symptoms + "; ";
                        }
                        symptoms = symptoms + react.getReactionValue();
                    }
                    Code reaction = new Code();
                    reaction.setDisplayName(capitalizeString(symptoms));
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
                            Result result = oFact.createResult();
                            Values values = new Values();
                            
                            values.set_type(PHYS_QUAN);
                            values.setScalar(detail.getValue());
                            values.setUnits(detail.getUnits());
                            ArrayList<Values> valList = new ArrayList<Values>();
                            valList.add(values);
                            result.setValues(valList);
                            
                            Interval inter = new Interval();
   
                            inter.setValue(factory.newXMLGregorianCalendar(cal));
                            result.setTime(inter.getValue().toString());
                            result.setEffectiveTime(inter);
                            result.setType(LAB_RESULT);
                            result.setDescription(detail.getName());
                            result.setMoodCode(EVENT_MOOD_CODE);
                            Code resultType = oFact.createCode();
                            Quantity quantity = oFact.createQuantity();
                            quantity.setUnit(detail.getUnits());
                            quantity.setAmount(Float.parseFloat(detail.getValue()));
                            result.setValue(quantity);
                            result.setReferenceRange(detail.))
                            resultType.setDisplayName(detail.getName());
                            
                            String value = detail.getValue();
                            Quantity gcValue = oFact.createQuantity();
                            gcValue.setAmount(Float.parseFloat(detail.getValue());
                            result.setValue
                            result.setDescription(value);
                            
                          
                            String indicator = detail.getIndicator();
                            if (stringExists(detail.getSo2())) {
                                indicator = indicator + " " + detail.getSo2();
                            }
                            
                          
                            result.setDescription(indicator);
                            results.add(result);
                            if (stringExists(detail.getBmi())) {
                                Result bmiResult = oFact.createResult();
                                bmiResult.setResultValue(deatil.getBmi());
                                /*result.setResultValue(detail.getBmi());
                                resultType = new ResultType();
                                resultType.setValue("BMI");
                                result.setResultType(resultType);
                                result.setResultDateTime(d);
                                result.setNarrative(indicator);*/
                                results.add(result);
                            }
                        }
                    }
                }
            }
            //conn.close();
        } catch (Throwable e) {
            log.severe("VistaRepository: Error retrieving patient vitals.");

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
    	List<Telecom> teleList = person.getTelecom();
    	setPhoneNumber(teleList, contact.getPhoneNumber(), "phone-landline", "home");
    	setPhoneNumber(teleList, contact.getAltPhoneNumber(), "phone-landline", "work");

    	String relationship = contact.getRelationshipToPatient();


    	if (relationship() != null && !relationship().equals(""))
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
            conn = setConnection();
            if (conn != null) {
                ret = new PatientVisitRepository(conn, "MSC PATIENT DASHBOARD").getVisitsByPatientDFN(id);


            } else {
                return null;
            }
            List<Encounter> visits = new ArrayList<Encounter>();


            for (PatientVisit visit : ret) {
                Encounter encounter = new Encounter();
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
    private Immunization fillInImmunization(PatientImmunization imm)
    {
    	Immunization gcImm = new Immunization();
    	ObjectFactory oFact = new ObjectFactory();
    	String contraIndicated = imm.getContraindicated();
    	String provider = imm.getEncounterProvider();
    	String name = imm.getImmunizationName();
    	String series = imm.getSeries();
    	String sa = imm.getRemarks();
    	String sad = imm.getMessage();
    	String saa = imm.getDateTimeString();
    	String sab = imm.getMessage();
    	String sak = imm.getReaction();
    	String saaw = imm.getStatus();
    	String saak = imm.getDateTime();
    	String sasas = imm.getDiagnoses();
    	String saassa = imm.getImmunizationDate();
    	String sssss = imm.getIsTimeRangeDependent();
    	
    	gcImm.set_type("Immunization");
    	gcImm.setDescription(imm.getImmunizationName());
    	gcImm.setRefused(false);
    	try {
    	gcImm.setSeriesNumber(new BigInteger(Integer.parseInt(series)));
    	}
    	catch(Exception e)
    	{
    		// not an integer doesn't translate, so no entry
    	}
    	gcImm.setStatus(imm.getStatus());
        if (imm.getImmunizationDate() != null) {
            GregorianCalendar cal = new GregorianCalendar();
           
            cal.setTime(imm.getDateTime());
            DatatypeFactory factory = DatatypeFactory.newInstance();
           
        
            
            String displayDate = parseDate(cal.getTime().getTime(), true);
           gcImm.setTime(displayDate);
         
        }
        gcImm.setFreeText(imm.getImmunizationName() + " given by " + imm.getEncounterProvider());
        return gcImm;

    }

	@Override
	public boolean insertAllergies(String patientId,
			Collection<Allergy> allergies) throws NotImplementedException {
		// TODO Auto-generated method stub
		return false;
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
	public List<Procedure> getProcedures(String patientId)
			throws NotImplementedException {
		// TODO Auto-generated method stub
		return null;
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
}
