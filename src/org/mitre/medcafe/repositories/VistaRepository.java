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
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.projecthdata.hdata.schemas._2009._06.allergy.*;
import org.projecthdata.hdata.schemas._2009._06.core.*;
import org.projecthdata.hdata.schemas._2009._06.patient_information.*;
import org.projecthdata.hdata.schemas._2009._06.medication.*;
import org.projecthdata.hdata.schemas._2009._06.condition.*;
import org.projecthdata.hdata.schemas._2009._06.support.Support;
import org.projecthdata.hdata.schemas._2009._06.support.ContactRelationship;
import org.projecthdata.hdata.schemas._2009._06.immunization.Immunization;
import org.projecthdata.hdata.schemas._2009._06.provider.*;
import org.projecthdata.hdata.schemas._2009._06.comment.*;
import org.projecthdata.hdata.schemas._2009._06.encounter.EncounterType;
import org.projecthdata.hdata.schemas._2009._06.result.*;
import org.projecthdata.hdata.schemas._2009._06.core.Informant;


import org.mitre.medcafe.hdatabased.encounter.EncounterDetail;
import org.mitre.medcafe.hdatabased.exam.Exam;
import org.mitre.medcafe.hdatabased.exam.ExamType;
import org.mitre.medcafe.hdatabased.exam.ExamResult;
import org.mitre.medcafe.hdatabased.healthfactor.Factor;
import org.mitre.medcafe.hdatabased.healthfactor.HealthFactor;
import org.mitre.medcafe.hdatabased.patienteducation.PatientEducation;
import org.mitre.medcafe.hdatabased.patienteducation.Topic;
import org.mitre.medcafe.hdatabased.patienteducation.PatientUnderstanding;
import org.mitre.medcafe.hdatabased.procedure.Procedure;
import org.mitre.medcafe.hdatabased.procedure.ProcedureCode;
import org.mitre.medcafe.hdatabased.treatment.Treatment;
import org.mitre.medcafe.hdatabased.treatment.TreatmentType;
import org.mitre.medcafe.util.Repository;
import org.mitre.medcafe.util.Repository.ProductComparator;
import org.mitre.medcafe.util.Repository.ReactionComparator;

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
import org.json.JSONObject;
import org.json.JSONString;
import org.json.JSONArray;
import org.json.JSONException;


import java.util.Collection;
import java.util.ArrayList;

import java.util.HashMap;

/**
 *  This class implements an interface to a back-end Vista repository.  The Medsphere (http://www.medsphere.com/) version of VistA, named OpenVista, is
 *  used via the medsphere ovid library
 */
public class VistaRepository extends Repository {

    public final static String KEY = VistaRepository.class.getName();
    public final static Logger log = Logger.getLogger(KEY);
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

            //set  name
            Name personName = new Name();
            List<String> given = personName.getGiven();
            if (stringExists(filemanPat.getGivenName())) {
                given.add(capitalizeString(filemanPat.getGivenName()));
            }
            if (stringExists(filemanPat.getMiddleName())) {
                given.add(capitalizeString(filemanPat.getMiddleName()));
            }
            if (stringExists(filemanPat.getSuffix())) {
                personName.setSuffix(filemanPat.getSuffix());
            }
            if (stringExists(filemanPat.getFamilyName())) {
                personName.setLastname(capitalizeString(filemanPat.getFamilyName()));
            }
            if (stringExists(filemanPat.getPrefix())) {
                personName.setTitle(capitalizeString(filemanPat.getPrefix()));
            }
            ret.setName(personName);

            // set address
            List<Address> addressList = ret.getAddress();
            Address address = new Address();
            address.setCity(filemanPat.getCity());
            address.setStateOrProvince(capitalizeString(capitalizeString(filemanPat.getStateValue())));
            address.setZip(filemanPat.getZip4());
            List<String> streetAddresses = address.getStreetAddress();
            streetAddresses.add(filemanPat.getStreetAddressLine1());
            String streetAddress = filemanPat.getStreetAddressLine2();
            if (stringExists(streetAddress)) {
                streetAddresses.add(streetAddress);
                streetAddress = filemanPat.getStreetAddressLine3();
                if (stringExists(streetAddress)) {
                    streetAddresses.add(streetAddress);
                }
            }

            addressList.add(address);


            //phone numbers
            List<Telecom> teleList = ret.getTelecom();

            setPhoneNumber(teleList, filemanPat.getPhoneNumberResidence(), "phone-landline", "home");
            setPhoneNumber(teleList, filemanPat.getPhoneNumberWork(), "phone-landline", "work");
            setPhoneNumber(teleList, filemanPat.getPhoneNumberCellular(), "phone-cell", "other");
            setPhoneNumber(teleList, filemanPat.getPagerNumber(), "phone-pager", "other");
            setPhoneNumber(teleList, filemanPat.getEmailAddress(), "email", "other");

            ret.setId(filemanPat.getIEN());


            //birthplace
            if (stringExists(filemanPat.getBirthCity())) {
                Address birthPlace = new Address();
                birthPlace.setCity(filemanPat.getBirthCity());
                birthPlace.setStateOrProvince(capitalizeString(filemanPat.getBirthStateValue().toString()));
                ret.setBirthPlace(birthPlace);
            }

            // marital status

            FMMaritalStatus fmMarStatus = patientRepository.getMaritalStatus(filemanPat);
            if (fmMarStatus != null) {
                boolean unknown = false;
                MaritalStatus marStatus = new MaritalStatus();
                marStatus.setCodeSystem("2.16.840.1.113883.5.2");
                marStatus.setCodeSystemName("HL7 MaritalStatusCode");
                if (fmMarStatus.getName() != null) {
                    char letter = fmMarStatus.getName().charAt(0);
                    switch (letter) {
                        case 'D':
                            marStatus.setCode("D");
                            marStatus.setDisplayName("Divorced");
                            break;
                        case 'M':
                            marStatus.setCode("M");
                            marStatus.setDisplayName("Married");
                            break;
                        case 'W':
                            marStatus.setCode("W");
                            marStatus.setDisplayName("Widowed");
                            break;
                        case 'N':
                            marStatus.setCode("S");
                            marStatus.setDisplayName("Never Married");
                            break;
                        case 'S':
                            marStatus.setCode("L");
                            marStatus.setDisplayName("Legally Separated");
                            break;
                        case 'U':
                            unknown = true;

                            break;
                    }
                }
                if (!unknown) {
                    ret.setMaritialStatus(marStatus);
                }


            }

            //Race
            Collection<FMRaceInformation> patRaceList = patientRepository.getRaceInformation(filemanPat);
            if (patRaceList.size() > 0) {
                List<Race> raceList = ret.getRace();
                for (FMRaceInformation raceInfo : patRaceList) {
                    Race race = new Race();
                    race.setCodeSystem("2.16.840.1.113883.6.238");
                    race.setCodeSystemName("CDC Race and Ethnicity");
                    String raceDesc = raceInfo.getRaceInformationValue();
                    race.setDisplayName(capitalizeString(raceDesc));
                    String twoChars = raceDesc.substring(0, 2);
                    if (twoChars.equalsIgnoreCase("AM")) {
                        race.setCode("1004-1");
                    } else if (twoChars.equalsIgnoreCase("AS")) {
                        race.setCode("2028-9)");
                    } else if (twoChars.equalsIgnoreCase("BL")) {
                        race.setCode("2058-6");
                    } else if (twoChars.equalsIgnoreCase("NA")) {
                        race.setCode("2076-8");
                    } else if (twoChars.equalsIgnoreCase("WH")) {
                        race.setCode("2106-3");
                    }
                    raceList.add(race);
                }
            }
            //Guardian info
            if (filemanPat.getAge() < 18 || filemanPat.getCivilGuardianDateRuledIncompetent() != null
                    || filemanPat.getIncompVADate() != null) {

                Person guardian = new Person();

                Name guardName = new Name();
                String civilGuardName = filemanPat.getGuardianCivil();
                String vaGuardName = filemanPat.getVaGuardian();
                if (stringExists(civilGuardName)) {
                    String[] nameParts = civilGuardName.split(",");
                    guardName.setLastname(nameParts[0]);
                    given = guardName.getGiven();
                    for (int i = 1; i < nameParts.length; i++) {
                        given.add(nameParts[i]);
                    }
                    guardian.setName(guardName);
                    addressList = guardian.getAddress();
                    address = new Address();
                    address.setCity(filemanPat.getCivilGuardianCity());
                    address.setStateOrProvince(capitalizeString(filemanPat.getCivilGuardianStateValue()));
                    address.setZip(filemanPat.getCivilGuardianZip4());
                    streetAddresses = address.getStreetAddress();
                    streetAddresses.add(filemanPat.getCivilGuardianStreetAddress1());
                    streetAddress = filemanPat.getCivilGuardianStreetAddress2();
                    if (stringExists(streetAddress)) {
                        streetAddresses.add(streetAddress);
                    }
                    addressList.add(address);
                    teleList = guardian.getTelecom();
                    setPhoneNumber(teleList, filemanPat.getCivilGuardianPhone(), "other", "other");

                } // if no civil guardian listed assume next of kin is guardian for
                // patient who is underage
                else if (filemanPat.getAge() < 18) {
                    if (stringExists(filemanPat.getNokNameOfPrimary())) {
                        String[] nameParts = filemanPat.getNokNameOfPrimary().split(",");
                        guardName.setLastname(nameParts[0]);
                        given = guardName.getGiven();
                        for (int i = 1; i < nameParts.length; i++) {
                            given.add(nameParts[i]);
                        }
                        guardian.setName(guardName);
                        addressList = guardian.getAddress();
                        address = new Address();
                        address.setCity(filemanPat.getNokCity());
                        address.setStateOrProvince(capitalizeString(filemanPat.getNokStateValue()));
                        address.setZip(filemanPat.getNokZip4());
                        streetAddresses = address.getStreetAddress();
                        streetAddresses.add(filemanPat.getNokStreetAddressLine1());
                        streetAddress = filemanPat.getNokStreetAddressLine2();
                        if (stringExists(streetAddress)) {
                            streetAddresses.add(streetAddress);
                        }
                        addressList.add(address);
                        teleList = guardian.getTelecom();
                        setPhoneNumber(teleList, filemanPat.getNokPhoneNumber(), "phone-landline", "home");
                        setPhoneNumber(teleList, filemanPat.getNokWorkPhoneNumber(), "phone-landline", "work");

                    }
                } else if (stringExists(vaGuardName)) {

                    String[] nameParts = vaGuardName.split(",");
                    guardName.setLastname(capitalizeString(nameParts[0]));
                    given = guardName.getGiven();
                    for (int i = 1; i < nameParts.length; i++) {
                        given.add(capitalizeString(nameParts[i]));
                    }
                    guardian.setName(guardName);
                    addressList = guardian.getAddress();
                    address = new Address();
                    address.setCity(filemanPat.getVaGuardianCity());
                    address.setStateOrProvince(capitalizeString(filemanPat.getVaGuardianStateValue()));
                    address.setZip(filemanPat.getVaGuardianZip4());
                    streetAddresses = address.getStreetAddress();
                    streetAddresses.add(filemanPat.getVaGuardianStreetAddress1());
                    streetAddress = filemanPat.getVaGuardianStreetAddress2();
                    if (stringExists(streetAddress)) {
                        streetAddresses.add(streetAddress);
                    }
                    addressList.add(address);
                    teleList = guardian.getTelecom();
                    setPhoneNumber(teleList, filemanPat.getVaGuardianPhone(), "other", "other");
                }
                ret.setGuardian(guardian);
            }

            //set gender
            Gender g = new Gender();
            g.setDisplayName(filemanPat.getSex().toLowerCase());
            ret.setGender(g);
            log.finer(String.valueOf(filemanPat.getDob()));

            //set DOB
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(filemanPat.getDob());
            DatatypeFactory factory = DatatypeFactory.newInstance();
            ret.setBirthtime(factory.newXMLGregorianCalendar(cal));
            return ret;
        } catch (OvidDomainException e) {
            log.log(Level.SEVERE, "Error retrieving patient " + id, e);
            return null;
        } catch (DatatypeConfigurationException e) {
            log.throwing(KEY, "Error assembling return object", e);
            return null;
        } finally {
            closeConnection(conn);
        }

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

                    Product c = new Product();
                    c.setValue(capitalizeString(a.getReactant()));
                    allergy.setProduct(c);
                    //set time for adverse reaction
                    if (a.getOriginationDateTime() != null) {
                        GregorianCalendar cal = new GregorianCalendar();
                        cal.setTime(a.getOriginationDateTime());
                        DatatypeFactory factory = DatatypeFactory.newInstance();
                        DateRange d = new DateRange();
                        d.setLow(factory.newXMLGregorianCalendar(cal));
                        allergy.setAdverseEventDate(d);
                    }

                    Reaction re = new Reaction();

                    String symptoms = "";
                    for (FMPatientAllergyReaction react : a.getReactions().values()) {
                        if (!symptoms.equals("")) {
                            symptoms = symptoms + "; ";
                        }
                        symptoms = symptoms + react.getReactionValue();
                    }
                    re.setValue(capitalizeString(symptoms));
                    allergy.setReaction(re);
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
                    AdverseEventType advEventType = new AdverseEventType();

                    advEventType.setCode(code);
                    advEventType.setDisplayName(val);
                    advEventType.setValue(val);
                    advEventType.setCodeSystem("2.16.840.1.113883.6.96");
                    advEventType.setCodeSystemName("SNOMED CT");
                    allergy.setAdverseEventType(advEventType);
                    String narr = "";
                    if (a.getComments() != null) {
                        for (FMPatientAllergyComment comment : a.getComments().values()) {
                            if (!narr.equals("")) {
                                narr = narr + "; ";
                            }
                            narr = narr + comment.getComments();
                        }
                    }
                    allergy.setNarrative(narr);
                    List<Informant> infoSource = allergy.getInformationSource();
                    Informant source = new Informant();

                    List<Person> contact = source.getContact();
                    Person person = new Person();
                    Name personName = setPersonsName(capitalizeString(a.getOriginatorName()));
                    person.setName(personName);
                    contact.add(person);
                    infoSource.add(source);

     
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

    public boolean updateAllergies(String patientId, Collection<Allergy> allergies) {
        boolean success = true;
        RPCConnection conn = null;
        try {
            conn = setConnection();
            Collection<Allergy> insertList = new ArrayList<Allergy>();
            PatientAllergyRepository patAllergyRepo = new PatientAllergyRepository(conn);
            Collection<FMPatient_Allergies> fmAllergyList = patAllergyRepo.getAllergiesForPatientIEN(patientId, true);
            HashMap<String, FMPatient_Allergies> fmAllergyMap = new HashMap<String, FMPatient_Allergies>();
            for (FMPatient_Allergies fmAll : fmAllergyList) {
                fmAllergyMap.put(fmAll.getReactant().toUpperCase(), fmAll);
            }
            for (Allergy allergy : allergies) {

                FMPatient_Allergies fmAllergy = fmAllergyMap.get(allergy.getProduct().getValue().toUpperCase());
                if (fmAllergy != null) {



                    String reactionList = allergy.getReaction().getValue();

                    String[] reacts = reactionList.split(";");
                    for (int i = 0; i< reacts.length; i++) {
                      
         
                        HashMap reactionMap = fmAllergy.getReactionLookup();

                        FMPatientAllergyReaction fmReact = new FMPatientAllergyReaction();
                        fmReact.setDateEntered(allergy.getAdverseEventDate().getLow().toGregorianCalendar().getTime());
                        FMSignSymptom fmSigns = new FMSignSymptom(reacts[i].trim());

                        vistaReactionLock.readLock().lock();
                        try{
                        FMSignSymptom returnSign = vistaReactions.floor(fmSigns);

                        if (returnSign != null && (returnSign.getName().compareToIgnoreCase(reacts[i].trim()) == 0)
                                && reactionMap.get(returnSign.getIEN()) == null) {
                            fmReact.setReactionIEN(returnSign);
                            fmAllergy.addReactions(fmReact);

                        }
    
                      }
                        finally
                        {
                            vistaReactionLock.readLock().unlock();
                        }

                    }
                    String[] comments = allergy.getNarrative().split(";");
                    for (int i = 0; i<comments.length; i++) {
                        HashMap commentMap = fmAllergy.getCommentLookup();
                        if (commentMap.get(comments[i].trim().toLowerCase()) == null) {
                            FMPatientAllergyComment fmComment = new FMPatientAllergyComment();
                            GregorianCalendar cal = new GregorianCalendar();
                            fmComment.setCommentDate(cal.getTime());
                            fmComment.setComments(comments[i].trim());
                            fmAllergy.addComments(fmComment);
                        }
                    }
                    if (allergy.getAdverseEventType().getCode().equals("414285001")
                            && !fmAllergy.getAllergyType().equals(FMPatient_Allergies.FOOD_ALLERGY)) //food allergy
                    {
                        fmAllergy.setAllergyType(FMPatient_Allergies.FOOD_ALLERGY);
                    } else if (allergy.getAdverseEventType().getCode().equals("416098002")
                            && !fmAllergy.getAllergyType().equals(FMPatient_Allergies.DRUG_ALLERGY)) {

                        fmAllergy.setAllergyType(FMPatient_Allergies.DRUG_ALLERGY);
                    } else if (allergy.getAdverseEventType().getCode().equals("419199007")
                            && (fmAllergy.getAllergyType().equals(FMPatient_Allergies.DRUG_ALLERGY)
                            || fmAllergy.getAllergyType().equals(FMPatient_Allergies.FOOD_ALLERGY))) {
                        fmAllergy.setAllergyType(FMPatient_Allergies.OTHER_ALLERGY);
                    }
                    patAllergyRepo.updateAllergy(fmAllergy);


                } else {
                    log.severe("Can't modify " + allergy.getProduct().getDisplayName()
                            + " no match found.");
                    insertList.add(allergy);
                }



            }
            if (insertList.size() == 0) {
                success = true;
            } else {
                success = insertAllergies(patientId, insertList);
            }
        } catch (OvidDomainException ovidE) {
            log.severe("Error modifiying patient allergies.");
            success = false;
        } finally {
            closeConnection(conn);
            return success;
        }

    }

    public boolean insertAllergies(String patientId, Collection<Allergy> allergies) {
        boolean success = true;
        RPCConnection conn = null;
        FMPatient_Allergies fmAllergy = null;
        try {
            conn = setConnection();
            PatientAllergyRepository patAllergyRepo = new PatientAllergyRepository(conn);
            for (Allergy allergy : allergies) {
                try {
                    fmAllergy = new FMPatient_Allergies();
                    fmAllergy.setPatient(Integer.parseInt(patientId));


                    AllergyAgent lookupAgent = new AllergyAgent();
                    lookupAgent.setDisplayName(allergy.getProduct().getValue());
                    vistaAgentLock.readLock().lock();
                    AllergyAgent returnAgent = vistaAgents.floor(lookupAgent);
                    // if the specified allergy exists in the table, proceed to build allergy record;
                    if (returnAgent != null && returnAgent.getDisplayName().compareToIgnoreCase(lookupAgent.getDisplayName())==0) {
        
                        fmAllergy.setAllergy(returnAgent.getLookupFile().getFilenum(), returnAgent.getAllergenRecord().getIEN());
                        fmAllergy.setReactant(returnAgent.getDisplayName().toUpperCase());

                        // Originator has to be in the new person file, and since we aren't currently using that value
                        // the originator is hard coded in
                        fmAllergy.setOriginator(42);
                        FMPatientAllergyComment comment = new FMPatientAllergyComment();
                        comment.setComments(allergy.getNarrative());
                        fmAllergy.addComments(comment);
                        String reactionList = allergy.getReaction().getValue();
                        String[] reacts = reactionList.split(";");
                        for (String react : reacts) {
                            FMPatientAllergyReaction fmReact = new FMPatientAllergyReaction();
                            fmReact.setDateEntered(allergy.getAdverseEventDate().getLow().toGregorianCalendar().getTime());
                            FMSignSymptom fmSigns = new FMSignSymptom(react.trim());
                            vistaReactionLock.readLock().lock();
                            try {
                            FMSignSymptom returnSign = vistaReactions.floor(fmSigns);
                            if (returnSign != null && returnSign.getName().compareToIgnoreCase(react.trim())==0) {
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
                        if (allergy.getAdverseEventDate()!= null && allergy.getAdverseEventDate().getLow() != null)
                        {
                            fmAllergy.setOriginationDateTime(allergy.getAdverseEventDate().getLow().toGregorianCalendar().getTime());
                        }
                        else
                        {
                            GregorianCalendar cal = new GregorianCalendar();
                            fmAllergy.setOriginationDateTime(cal.getTime());
                        }
                        //Observed or historical field must be set; hData doesn't pass that information so default to historical
                        fmAllergy.setObservedOrHist("h");
                        if (allergy.getAdverseEventType().getCode().equals("414285001")) //food allergy
                        {
                            fmAllergy.setAllergyType(FMPatient_Allergies.FOOD_ALLERGY);
                        } else if (allergy.getAdverseEventType().getCode().equals("416098002")) {

                            fmAllergy.setAllergyType(FMPatient_Allergies.DRUG_ALLERGY);
                        } else if (allergy.getAdverseEventType().getCode().equals("419199007")) {
                            fmAllergy.setAllergyType(FMPatient_Allergies.OTHER_ALLERGY);
                        }

                        patAllergyRepo.addAllergy(fmAllergy);
      

                    } else {
                        log.severe("Can't insert allergy to " + allergy.getProduct().getValue()
                                + " no match found.");
                        success = false;
                    }
                } catch (ModifiedKeyFileManFieldException modE) {
                    log.severe("Can't change id or reactant in existing field");
                    success = false;
                } catch (InvalidFileManFieldValueException invalE) {
                    log.severe(invalE.getMessage());
                    success = false;
                } catch (InsertFileManRecordException insertE) {
                    log.severe(insertE.getMessage() + " for patient id: " + patientId + ", allergen: " + allergy.getProduct().getDisplayName());
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
            }
        } catch (OvidDomainException ovidE) {
            log.severe("Error inserting patient allergies.");
            success = false;
        } finally {
            closeConnection(conn);
            return success;
        }

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
                    Medication medication = new Medication();  //hData type
                    PatientMedication pa = (PatientMedication) a;  //vista (ovid) type
                    //populate
                    //message -> narrative
                    medication.setNarrative(capitalizeString(pa.getMessage(), true));
                    //set time for adverse reaction
                    if (pa.getDateTime() != null) {
                        GregorianCalendar cal = new GregorianCalendar();
                        cal.setTime(pa.getDateTime());
                        DatatypeFactory factory = DatatypeFactory.newInstance();
                        medication.getEffectiveTime().add(factory.newXMLGregorianCalendar(cal));
                    }

                    String medname = capitalizeString(pa.getMedName(), true);
                    MedicationInformation m = new MedicationInformation();
                    MedicationInformation.ManufacturedMaterial mm = new MedicationInformation.ManufacturedMaterial();
                    m.setManufacturedMaterial(mm);
                    mm.setFreeTextBrandName(medname);
                    medication.setMedicationInformation(m);

                    Dose d = new Dose();
                    d.setValue(capitalizeString(pa.getDose(), true));
                    medication.setDose(d);

                    CodedValue c = new CodedValue();
                    c.setValue(capitalizeString(pa.getDelivery(), true));
                    medication.setDeliveryMethod(c);

                    medication.setPatientInstructions(pa.getFrequency());
                    //add to the list
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
                // an ArrayList of PatientAllergies objects is returned -  converty to hData Medication type
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
                for (PatientVitalEvent vital : vitals) {

                    if (vital.getDateTime() != null) {
                        GregorianCalendar cal = new GregorianCalendar();
                        cal.setTime(vital.getDateTime());
                        DatatypeFactory factory = DatatypeFactory.newInstance();
                        DateRange d = new DateRange();
                        d.setLow(factory.newXMLGregorianCalendar(cal));

                        Collection<VitalSignDetail> details = vital.getDetails();
                        for (VitalSignDetail detail : details) {
                            Result result = new Result();
                            result.setResultDateTime(d);
                            ResultType resultType = new ResultType();
                            resultType.setValue(detail.getName());
                            result.setResultType(resultType);
                            String value = detail.getValue();
                            if (stringExists(detail.getUnits())) {
                                value = value + " " + detail.getUnits();
                            }
                            result.setResultValue(value);
                            String indicator = detail.getIndicator();
                            if (stringExists(detail.getSo2())) {
                                indicator = indicator + " " + detail.getSo2();
                            }
                            result.setNarrative(indicator);
                            results.add(result);
                            if (stringExists(detail.getBmi())) {
                                result = new Result();
                                result.setResultValue(detail.getBmi());
                                resultType = new ResultType();
                                resultType.setValue("BMI");
                                result.setResultType(resultType);
                                result.setResultDateTime(d);
                                result.setNarrative(indicator);
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

    public List<org.projecthdata.hdata.schemas._2009._06.condition.Condition> getProblems(String id) {
        List<org.projecthdata.hdata.schemas._2009._06.condition.Condition> list = new ArrayList<org.projecthdata.hdata.schemas._2009._06.condition.Condition>();
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
                        DateRange d = new DateRange();
                        d.setLow(factory.newXMLGregorianCalendar(cal));
                        problem.setProblemDate(d);
                    }
                    ProblemCode pcode = new ProblemCode();
                    pcode.setCode(p.getIcd());
                    problem.setProblemCode(pcode);
                    problem.setProblemName(p.getMessage());
                    problem.setNarrative(p.getStatus());

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
            telecom.setType(type);
            telecom.setUse(use);
            teleList.add(telecom);
        }
    }

    private void fillInContactInfo(List<Support> list, FMPatientContact.ContactInfo contact) {
        Support support = new Support();
        Person person = new Person();
        Name personName = new Name();
        String[] nameParts = contact.getName().split(",");
        personName.setLastname(nameParts[0]);
        List<String> given = personName.getGiven();
        for (int i = 1; i < nameParts.length; i++) {
            given.add(nameParts[i]);
        }

        person.setName(personName);
        List<Address> addressList = person.getAddress();
        Address address = new Address();
        address.setCity(contact.getCity());
        address.setStateOrProvince(contact.getState());
        String zip = contact.getZip();
        address.setZip(contact.getZip());
        List<String> streetAddresses = address.getStreetAddress();
        streetAddresses.add(contact.getStreet1());
        String streetAddress = contact.getStreet2();
        if (stringExists(streetAddress)) {
            streetAddresses.add(streetAddress);
        }
        addressList.add(address);

        List<Telecom> teleList = person.getTelecom();
        setPhoneNumber(teleList, contact.getPhoneNumber(), "phone-landline", "home");
        setPhoneNumber(teleList, contact.getAltPhoneNumber(), "phone-landline", "work");
        support.setContact(person);
        ContactRelationship relationship = new ContactRelationship();
        if (stringExists(contact.getRelationshipToPatient())) {
            relationship.setDisplayName(contact.getRelationshipToPatient());
            support.setContactRelationship(relationship);
        }
        switch (contact.getType()) {
            case NEXT_OF_KIN:
                support.setContactType("NOK");
                break;
            case GUARDIAN:
            case DESIGNEE:
                support.setContactType("AGNT");
                break;
            case EMERGENCY:
                support.setContactType("ECON");
                break;

        }
        list.add(support);
    }

    public Collection<FMRecord> getTimeLineInfo(String ien) {
        Collection<FMRecord> list = new ArrayList<FMRecord>();
        RPCConnection conn = null;

        try {
            conn = setConnection();
            if (conn != null) {
                for (FMPatientMovement patMove : new PatientMovementRepository(conn).getPatientMovementByPatientDFN(ien)) {

                    list.add(patMove);
                }
                for (FMOutpatientEncounter patEncoun : new OutpatientEncounterRepository(conn).getOutpatientEncounterByPatientDFN(ien)) {
                    list.add(patEncoun);
                }
            } else {
                return null;
            }
            return list;
        } catch (OvidDomainException e) {
            log.log(Level.SEVERE, "Error retrieving patient list", e);
            return null;
        } finally {
            closeConnection(conn);
        }
    }

    private Name setPersonsName(String fullname) {
        Name personsName = new Name();
        String[] nameParts = fullname.split(",");
        personsName.setLastname(nameParts[0]);
        List<String> given = personsName.getGiven();
        for (int i = 1; i < nameParts.length; i++) {
            given.add(nameParts[i]);
        }
        return personsName;
    }

    public Collection<EncounterDetail> getPatientEncounters(String id) {
        Collection<PatientVisit> ret;
        RPCConnection conn = null;
        try {
            conn = setConnection();
            if (conn != null) {
                ret = new PatientVisitRepository(conn, "MSC PATIENT DASHBOARD").getVisitsByPatientDFN(id);


            } else {
                return null;
            }
            Collection<EncounterDetail> visits = new ArrayList<EncounterDetail>();


            for (PatientVisit visit : ret) {
                EncounterDetail encounter = new EncounterDetail();
                populateEncounterDetail(encounter, visit);

                List<Actor> providers = encounter.getEncounterProvider();
                populateProviderList(providers, visit);


                List<Procedure> procedures = encounter.getProcedures();
                populateProcedureList(procedures, visit);

                List<Condition> conditions = encounter.getConditions();
                populateConditionList(conditions, visit);

                List<PatientEducation> patientEd = encounter.getEducation();
                populatePatientEducationList(patientEd, visit);

                List<Exam> exams = encounter.getExams();
                populateExamList(exams, visit);

                List<Immunization> immunizations = encounter.getImmunizations();
                populateImmunizationList(immunizations, visit);

                List<Result> results = encounter.getResults();
                populateResultList(results, visit);

                List<Treatment> treatments = encounter.getTreatments();
                populateTreatmentList(treatments, visit);

                List<HealthFactor> healthFactors = encounter.getHealthFactors();
                populateHealthFactorList(healthFactors, visit);
    
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

    private Immunization fillInImmunizationInfo(PatientImmunization imm) {

        Immunization immunization = new Immunization();  //hData type
        immunization.setNarrative("Series: " + capitalizeString(imm.getSeries()) + "^Reaction: " + capitalizeString(imm.getReaction())
                + "^Contraindicated: " + capitalizeString(imm.getContraindicated()));
        Actor provider = new Actor();
        Person person = new Person();
        person.setName(setPersonsName(capitalizeString(imm.getEncounterProvider())));
        provider.setPerson(person);
        immunization.setPerformer(provider);
        //populate

        if (imm.getDateTime() != null) {
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(imm.getDateTime());
            try {
                DatatypeFactory factory = DatatypeFactory.newInstance();
                immunization.setAdministeredDate(factory.newXMLGregorianCalendar(cal));
            } catch (Exception e) {
                log.log(Level.FINER, e.getMessage());
            }

        }


        MedicationInformation m = new MedicationInformation();

        MedicationInformation.ManufacturedMaterial mm = new MedicationInformation.ManufacturedMaterial();
        mm.setFreeTextBrandName(capitalizeString(imm.getImmunizationName()));
        m.setManufacturedMaterial(mm);

        immunization.setMedicationInformation(m);
        return immunization;


    }

    private void populateTreatmentList(List<Treatment> treatments, PatientVisit visit) {
        for (FMV_Treatment fmTreatment : visit.getTreatments()) {

            Treatment treatment = new Treatment();
            if (stringExists(fmTreatment.getComments())) {
                Comment comment = new Comment();
                comment.setText(fmTreatment.getComments());
                treatment.setComment(comment);
            }

            TreatmentType treatmentType = new TreatmentType();
            treatmentType.setValue(fmTreatment.getTreatmentValue());
            treatment.setTreatmentType(treatmentType);

            List<Provider> trProviders = treatment.getProviders();
            if (stringExists(fmTreatment.getEncounterProviderValue())) {

                Provider trProvider = new Provider();
                Actor performer = new Actor();
                Person person = new Person();
                person.setName(setPersonsName(fmTreatment.getEncounterProviderValue()));
                performer.setPerson(person);
                trProvider.setProviderEntity(performer);
                trProvider.setProviderRoleFreeText("Encounter Provider");
                trProviders.add(trProvider);
            }

            if (stringExists(fmTreatment.getOrderingProviderValue())) {

                Provider trProvider = new Provider();
                Actor performer = new Actor();
                Person person = new Person();
                person.setName(setPersonsName(fmTreatment.getOrderingProviderValue()));
                performer.setPerson(person);
                trProvider.setProviderEntity(performer);
                trProvider.setProviderRoleFreeText("Ordering Provider");
                trProviders.add(trProvider);
            }

            if (fmTreatment.getEventDate() != null) {
                GregorianCalendar cal = new GregorianCalendar();
                cal.setTime(fmTreatment.getEventDate());
                try {
                    DatatypeFactory factory = DatatypeFactory.newInstance();
                    DateRange dateRange = new DateRange();
                    dateRange.setLow(factory.newXMLGregorianCalendar(cal));
                    treatment.setEncounterDate(dateRange);
                } catch (Exception e) {
                    log.log(Level.FINER, "Error setting date in Treatment record: e.getMessage()");
                }

            }

            treatments.add(treatment);
        }
    }

    private void populateHealthFactorList(List<HealthFactor> healthFactors, PatientVisit visit) {
        for (FMV_HealthFactors fmFactor : visit.getHealthFactors()) {

            HealthFactor healthFactor = new HealthFactor();
            if (stringExists(fmFactor.getComments())) {
                Comment comment = new Comment();
                comment.setText(fmFactor.getComments());
                healthFactor.setComment(comment);
            }

            Factor factor = new Factor();
            factor.setValue(fmFactor.getHealthFactorValue());
            healthFactor.setHealthFactor(factor);

            Severity severity = new Severity();
            severity.setValue(fmFactor.getLevelSeverity());
            healthFactor.setSeverity(severity);

            List<Provider> hfProviders = healthFactor.getProviders();
            if (stringExists(fmFactor.getEncounterProviderValue())) {

                Provider hfProvider = new Provider();
                Actor performer = new Actor();
                Person person = new Person();
                person.setName(setPersonsName(fmFactor.getEncounterProviderValue()));
                performer.setPerson(person);
                hfProvider.setProviderEntity(performer);
                hfProvider.setProviderRoleFreeText("Encounter Provider");
                hfProviders.add(hfProvider);
            }

            if (stringExists(fmFactor.getOrderingProviderValue())) {

                Provider hfProvider = new Provider();
                Actor performer = new Actor();
                Person person = new Person();
                person.setName(setPersonsName(fmFactor.getOrderingProviderValue()));
                performer.setPerson(person);
                hfProvider.setProviderEntity(performer);
                hfProvider.setProviderRoleFreeText("Ordering Provider");
                hfProviders.add(hfProvider);
            }

            if (fmFactor.getEventDate() != null) {
                GregorianCalendar cal = new GregorianCalendar();
                cal.setTime(fmFactor.getEventDate());
                try {
                    DatatypeFactory factory = DatatypeFactory.newInstance();
                    DateRange dateRange = new DateRange();
                    dateRange.setLow(factory.newXMLGregorianCalendar(cal));
                    healthFactor.setEncounterDate(dateRange);
                } catch (Exception e) {
                    log.log(Level.FINER, "Error setting date in HealthFactor record: e.getMessage()");
                }
            }

            healthFactors.add(healthFactor);
        }
    }

    private void populateResultList(List<Result> results, PatientVisit visit) {
        for (FMV_SkinTest fmSkinTest : visit.getSkinTests()) {

            Result result = new Result();
            ResultType resultType = new ResultType();
            resultType.setValue(fmSkinTest.getSkinTestValue());
            result.setResultType(resultType);
            if (fmSkinTest.getReading() != null) {
                result.setResultValue(fmSkinTest.getReading());
            }
            if (stringExists(fmSkinTest.getResults())) {
                CodedValue interpretation = new CodedValue();
                interpretation.setValue(fmSkinTest.getResults());
                result.setResultInterpretation(interpretation);
            }
            if (stringExists(fmSkinTest.getComments())) {
                result.setNarrative(fmSkinTest.getComments());
            }
            DateRange dateRange = new DateRange();
            try {
                DatatypeFactory factory = DatatypeFactory.newInstance();
                if (fmSkinTest.getEventDate() != null) {
                    GregorianCalendar cal = new GregorianCalendar();
                    cal.setTime(fmSkinTest.getEventDate());


                    dateRange.setLow(factory.newXMLGregorianCalendar(cal));
                    result.setResultDateTime(dateRange);


                }
                if (fmSkinTest.getDateRead() != null) {
                    GregorianCalendar cal = new GregorianCalendar();
                    cal.setTime(fmSkinTest.getDateRead());


                    dateRange.setHigh(factory.newXMLGregorianCalendar(cal));
                    result.setResultDateTime(dateRange);


                }
            } catch (Exception e) {
                log.log(Level.FINER, "Error setting date in Result record: e.getMessage()");
            }

            results.add(result);
        }
    }

    private void populateImmunizationList(List<Immunization> immunizations, PatientVisit visit) {
        for (FMV_Immunization fmImmune : visit.getImmunizations()) {

            Immunization immunization = new Immunization();  //hData type
            immunization.setNarrative("Series: " + fmImmune.getSeries() + " Reaction: " + fmImmune.getReaction()
                    + " Contraindicated: " + fmImmune.getContraindicated());
            Actor provider = new Actor();
            Person person = new Person();
            person.setName(setPersonsName(fmImmune.getEncounterProviderValue()));
            provider.setPerson(person);
            immunization.setPerformer(provider);
            //populate

            if (fmImmune.getEventDate() != null) {
                GregorianCalendar cal = new GregorianCalendar();
                cal.setTime(fmImmune.getEventDate());
                try {
                    DatatypeFactory factory = DatatypeFactory.newInstance();
                    immunization.setAdministeredDate(factory.newXMLGregorianCalendar(cal));
                } catch (Exception e) {
                    log.log(Level.FINER, e.getMessage());
                }

            }


            MedicationInformation m = new MedicationInformation();

            MedicationInformation.ManufacturedMaterial mm = new MedicationInformation.ManufacturedMaterial();
            mm.setFreeTextBrandName(fmImmune.getImmunizationValue());
            m.setManufacturedMaterial(mm);

            immunization.setMedicationInformation(m);

            immunizations.add(immunization);
        }
    }

    private void populateExamList(List<Exam> exams, PatientVisit visit) {
        for (FMV_Exam fmExam : visit.getExams()) {

            Exam exam = new Exam();
            ExamType examType = new ExamType();
            examType.setValue(fmExam.getExamValue());
            exam.setExamType(examType);
            if (stringExists(fmExam.getComments())) {
                Comment comment = new Comment();
                comment.setText(fmExam.getComments());
                exam.setComment(comment);
            }
            ExamResult exResult = new ExamResult();
            exResult.setValue(fmExam.getResult());
            exam.setExamResult(exResult);

            List<Provider> exProviders = exam.getProviders();
            if (stringExists(fmExam.getEncounterProviderValue())) {

                Provider exProvider = new Provider();
                Actor performer = new Actor();
                Person person = new Person();
                person.setName(setPersonsName(fmExam.getEncounterProviderValue()));
                performer.setPerson(person);
                exProvider.setProviderEntity(performer);
                exProvider.setProviderRoleFreeText("Encounter Provider");
                exProviders.add(exProvider);
            }

            if (stringExists(fmExam.getOrderingProviderValue())) {

                Provider exProvider = new Provider();
                Actor performer = new Actor();
                Person person = new Person();
                person.setName(setPersonsName(fmExam.getOrderingProviderValue()));
                performer.setPerson(person);
                exProvider.setProviderEntity(performer);
                exProvider.setProviderRoleFreeText("Ordering Provider");
                exProviders.add(exProvider);
            }

            if (fmExam.getEventDate() != null) {
                GregorianCalendar cal = new GregorianCalendar();
                cal.setTime(fmExam.getEventDate());
                try {
                    DatatypeFactory factory = DatatypeFactory.newInstance();
                    DateRange dateRange = new DateRange();
                    dateRange.setLow(factory.newXMLGregorianCalendar(cal));
                    exam.setEncounterDate(dateRange);
                } catch (Exception e) {
                    log.log(Level.FINER, "Error setting date in Exam record: e.getMessage()");
                }
            }

            exams.add(exam);
        }
    }

    private void populatePatientEducationList(List<PatientEducation> patientEd, PatientVisit visit) {
        for (FMV_PatientEd fmEd : visit.getPatientEd()) {

            PatientEducation pEducation = new PatientEducation();
            Topic topic = new Topic();
            topic.setValue(fmEd.getTopicValue());
            pEducation.setTopic(topic);
            PatientUnderstanding understand = new PatientUnderstanding();
            understand.setValue(fmEd.getLevelOfUnderstanding());
            pEducation.setPatientUnderstanding(understand);
            if (stringExists(fmEd.getComments())) {
                Comment comment = new Comment();
                comment.setText(fmEd.getComments());
                pEducation.setComment(comment);
            }
            List<Provider> edProviders = pEducation.getProviders();
            if (stringExists(fmEd.getEncounterProviderValue())) {

                Provider edProvider = new Provider();
                Actor performer = new Actor();
                Person person = new Person();
                person.setName(setPersonsName(fmEd.getEncounterProviderValue()));
                performer.setPerson(person);
                edProvider.setProviderEntity(performer);
                edProvider.setProviderRoleFreeText("Encounter Provider");
                edProviders.add(edProvider);
            }

            if (stringExists(fmEd.getOrderingProviderValue())) {

                Provider edProvider = new Provider();
                Actor performer = new Actor();
                Person person = new Person();
                person.setName(setPersonsName(fmEd.getOrderingProviderValue()));
                performer.setPerson(person);
                edProvider.setProviderEntity(performer);
                edProvider.setProviderRoleFreeText("Ordering Provider");
                edProviders.add(edProvider);
            }

            if (fmEd.getEventDate() != null) {
                GregorianCalendar cal = new GregorianCalendar();
                cal.setTime(fmEd.getEventDate());
                try {
                    DatatypeFactory factory = DatatypeFactory.newInstance();
                    DateRange dateRange = new DateRange();
                    dateRange.setLow(factory.newXMLGregorianCalendar(cal));
                    pEducation.setEncounterDate(dateRange);
                } catch (Exception e) {
                    log.log(Level.FINER, "Error setting date in PatientEducation record: e.getMessage()");
                }
            }

            patientEd.add(pEducation);
        }
    }

    private void populateConditionList(List<Condition> conditions, PatientVisit visit) {
        for (FMV_POV pov : visit.getPOVs()) {

            Condition condition = new Condition();
            ProblemCode code = new ProblemCode();
            code.setCode(pov.getPovValue());
            code.setValue(pov.getICDNarrative());
            code.setCodeSystem("2.16.840.1.113883.6.104");
            code.setCodeSystemName("ICD9");
            condition.setProblemCode(code);
            condition.setProblemName(pov.getICDNarrative());
            if (pov.getEventDate() != null) {
                GregorianCalendar cal = new GregorianCalendar();
                cal.setTime(pov.getEventDate());
                try {
                    DatatypeFactory factory = DatatypeFactory.newInstance();
                    DateRange dateRange = new DateRange();
                    dateRange.setLow(factory.newXMLGregorianCalendar(cal));
                    condition.setProblemDate(dateRange);
                } catch (Exception e) {
                    log.log(Level.FINER, "Error setting date in Condition record: e.getMessage()");
                }
            }
            condition.setNarrative(pov.getProviderNarrativeValue());
            List<Condition.TreatingProvider> treatingProviders = condition.getTreatingProvider();
            if (stringExists(pov.getEncounterProviderValue())) {
                Condition.TreatingProvider provider = new Condition.TreatingProvider();
                Actor performer = new Actor();
                Person person = new Person();
                person.setName(setPersonsName(pov.getEncounterProviderValue()));
                performer.setPerson(person);
                provider.setActor(performer);
                treatingProviders.add(provider);
            }
            conditions.add(condition);
        }
    }

    private void populateProcedureList(List<Procedure> procedures, PatientVisit visit) {
        for (FMV_CPT procedureCode : visit.getCurrentProcedureCodes()) {

            Procedure procedure = new Procedure();
            ProcedureCode code = new ProcedureCode();
            code.setCode(procedureCode.getCPTRecord().getCptCode());
            code.setValue(procedureCode.getCPTRecord().getShortName());
            code.setCodeSystem("2.16.840.1.113883.6.12");
            code.setCodeSystemName("CPT-4");
            procedure.setProcedureType(code);
            procedure.setProcedureFreeTextType(procedureCode.getCPTRecord().getShortName());
            if (stringExists(procedureCode.getEncounterProviderValue())) {
                Actor performer = new Actor();
                Person person = new Person();
                person.setName(setPersonsName(procedureCode.getEncounterProviderValue()));
                performer.setPerson(person);
                procedure.setPerformer(performer);
            }
            procedure.setNarrative(procedureCode.getComments());
            if (procedureCode.getEventDate() != null) {
                GregorianCalendar cal = new GregorianCalendar();
                cal.setTime(procedureCode.getEventDate());
                try {
                    DatatypeFactory factory = DatatypeFactory.newInstance();

                    procedure.setProcedureDate(factory.newXMLGregorianCalendar(cal));
                } catch (Exception e) {
                    log.log(Level.FINER, "Error setting date in Procedure record: e.getMessage()");
                }
            }

            procedures.add(procedure);
        }
    }

    private void populateProviderList(List<Actor> providers, PatientVisit visit) {

        for (FMV_Provider fmProvider : visit.getProviders()) {

            Actor provider = new Actor();
            Person person = new Person();
            if (stringExists(fmProvider.getProviderValue())) {
                Name providerName = setPersonsName(fmProvider.getProviderValue());
                person.setName(providerName);
                provider.setPerson(person);
                providers.add(provider);
            }
        }
    }

    private void populateEncounterDetail(EncounterDetail encounter, PatientVisit visit) {
        InstanceIdentifier encounterId = new InstanceIdentifier();
        encounterId.setRoot(visit.getVisit().getPatientName());
        encounterId.setExtension(visit.getVisit().getVisitID());
        encounter.setEncounterId(encounterId);
        EncounterType encounterType = new EncounterType();
        encounterType.setValue(visit.getVisit().getPatientInOut() + "PATIENT");
        encounter.setEncounterType(encounterType);
        if (stringExists(visit.getVisit().getComments())) {
            encounter.setNarrative(visit.getVisit().getComments());
        }
        if (visit.getVisit().getVisitDate() != null) {
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(visit.getVisit().getVisitDate());
            try {
                DatatypeFactory factory = DatatypeFactory.newInstance();
                DateRange dateRange = new DateRange();
                dateRange.setLow(factory.newXMLGregorianCalendar(cal));
                encounter.setEncounterDate(dateRange);
            } catch (Exception e) {
                log.log(Level.FINER, "Error setting date in Encounter record: e.getMessage()");
            }

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

    public TreeSet<Reaction> generateAllergyReactionList() {
        if (vistaReactionLock == null)
        {
        		vistaReactionLock = new ReentrantReadWriteLock();
        }
        RPCConnection conn = null;
        try {
            conn = setConnection();
            if (conn != null) {
                SignSymptomRepository symptomRepository = new SignSymptomRepository(conn);
                TreeSet<FMSignSymptom> tempTree = symptomRepository.getAllSignsSymptoms(true);
                 vistaReactionLock.writeLock().lock();
                try {
                vistaReactions = tempTree;
                }
                finally
                {
                vistaReactionLock.writeLock().unlock();
                }
                vistaReactionLock.readLock().lock();
                TreeSet<Reaction> reactColl;
                try{
                String message = "Number of reactions is " + vistaReactions.size();
                log.finer(message);
                reactColl = new TreeSet<Reaction>(new ReactionComparator());

                for (FMSignSymptom symptom : vistaReactions) {
                    Reaction react = new Reaction();
                    react.setDisplayName(capitalizeString(symptom.getName()));
                    reactColl.add(react);

                }
                }
                finally{
                	vistaReactionLock.readLock().unlock();
                }
                return reactColl;
            } else {
                log.warning("BAD CONNECTION");
            }

        } catch (Throwable e) {
            log.throwing(KEY, "Error retreiving Allergy Reaction List", e);
        } finally {
            closeConnection(conn);

        }

        return null;
    }

    public TreeSet<Product> generateAllergyReactantList() {
        RPCConnection conn = null;
        if (vistaAgentLock == null)
        {
        		vistaAgentLock = new ReentrantReadWriteLock();
        }
        try {
            conn = setConnection();
            if (conn != null) {
                AllergyAgentRepository agentRepository = new AllergyAgentRepository(conn);
                TreeSet<AllergyAgent> tempTree = agentRepository.getAllAllergyAgents();
                vistaAgentLock.writeLock().lock();
                try {
                vistaAgents = tempTree;
                }
                finally
                {
                vistaAgentLock.writeLock().unlock();
                }
                vistaAgentLock.readLock().lock();
                TreeSet<Product> prodColl;
                try{
                String message = "Number of allergy agents is " + vistaAgents.size();
                log.finer(message);
                prodColl = new TreeSet<Product>(new ProductComparator());

                for (AllergyAgent agent : vistaAgents) {
                    Product prod = new Product();
                    prod.setDisplayName(capitalizeString(agent.getDisplayName()));
                    prod.setValue(capitalizeString(agent.getAllergen().getName()));
                    prodColl.add(prod);

                }
                }
                finally
                {
                	vistaAgentLock.readLock().unlock();
                }
                return prodColl;
            } else {
                log.warning("BAD CONNECTION");
            }

        } catch (Throwable e) {
            log.throwing(KEY, "Error retreiving Allergy Agent List", e);
        } finally {
            closeConnection(conn);
                 }

        return null;
    }

}
