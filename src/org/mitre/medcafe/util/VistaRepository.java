package org.mitre.medcafe.util;

// import org.mitre.hdata.hrf.core.*;
import com.medsphere.fileman.*;
import com.medsphere.fmdomain.*;
import com.medsphere.ovid.domain.ov.*;
import com.medsphere.ovid.model.domain.patient.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.*;
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

import com.medsphere.vistarpc.RPCBrokerConnection;
import com.medsphere.vistarpc.factory.RPCPooledConnection;
import com.medsphere.vistarpc.factory.RPCBrokerPooledConnection;
import com.medsphere.vistarpc.RPCException;
import com.medsphere.vistarpc.factory.RPCBrokerPooledConnectionFactory;

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
    // static{log.setLevel(Level.FINER);}
    //protected static VistaLinkPooledConnectionFactory factory = null;
    // protected RPCBrokerConnection conn = null;
    protected static RPCBrokerPooledConnectionFactory rpcConnFactory = null;

    public VistaRepository() {
        type = "VistA";
    }

    /**
     *  Given a patient id, get the patient info
     */
    public Patient getPatient(String id) {
        RPCBrokerPooledConnection conn = null;
        try {
            FMPatientContact filemanPat = null;
            FMPatientContact[] patList = null;
            PatientRepository patientRepository = null;

            conn = setConnection();
            if (conn != null) {
                patientRepository = new PatientRepository(conn);
                Collection<String> iens = new ArrayList<String>();
                iens.add(id);
                Collection<FMPatientContact> patColl = patientRepository.getContacts(iens);
                if (patColl.size() == 0) {
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
                birthPlace.setStateOrProvince(filemanPat.getBirthStateValue().toString());
                ret.setBirthPlace(birthPlace);
            }

            // marital status

            FMMaritalStatus fmMarStatus = patientRepository.getMaritalStatus(filemanPat);
            if (fmMarStatus != null) {
                boolean unknown = false;
                MaritalStatus marStatus = new MaritalStatus();
                marStatus.setCodeSystem("2.16.840.1.113883.5.2");
                marStatus.setCodeSystemName("HL7 MaritalStatusCode");

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
                    race.setDisplayName(raceDesc);
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
        RPCBrokerPooledConnection conn = null;
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
        RPCBrokerPooledConnection conn = null;
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

    public static void factorySetUp(String[] creds) {
        try {
            rpcConnFactory = new RPCBrokerPooledConnectionFactory(creds[0], creds[1], creds[2], creds[3]);
        } catch (Exception e) {
            log.severe("Connection to repository failed.  Credentials were " + Arrays.toString(creds));
        }
    }

    @Override
    public void onShutdown() {
        if (rpcConnFactory != null) {
            rpcConnFactory.emptyPool();
        }
        rpcConnFactory = null;
        /*  if (conn != null) {
        closeConnection();
        }  */
    }

    /**
     *  sets the passed VistaLinkPooledConnection
     *  @return true if connection worked.  False otherwise.
     */
    protected RPCBrokerPooledConnection setConnection() throws OvidDomainException {
        RPCBrokerPooledConnection conn = null;

        if (rpcConnFactory == null) {
            factorySetUp(credentials);
        }


        conn = (RPCBrokerPooledConnection) rpcConnFactory.getConnection();

        //       conn = new RPCBrokerConnection(credentials[0], Integer.parseInt(credentials[1]), credentials[2], credentials[3]);

        //conn = factory.getConnection();
        if (conn == null) {
            log.severe("Connection to repository failed.  Credentials were " + Arrays.toString(credentials));
            return null;
        }
        return conn;
    }

    /**
     *  closes the passed RPCBrokerConnection
     */
    protected void closeConnection(RPCBrokerPooledConnection conn) {
        if (conn != null) {

            log.finer("closing connection");
            // conn.returnToPool();

            conn.returnToPool();

        }
    }

    /**
     * Set credentials property.
     *
     *@param credentials New credentials property.
     */
    @Override
    public void setCredentials(String... credentials) {
        if (credentials.length < 4) {
            throw new RuntimeException("Invalid number of credentials.  You must proivide <host> <port> <ovid-access-code> <ovid-verify-code>");
        }
        this.credentials = credentials;
    }

    public List<Allergy> getAllergies(String id) {
        List<Allergy> list = new ArrayList<Allergy>();
        RPCBrokerPooledConnection conn = null;
        try {
            conn = setConnection();
            if (conn != null) {
                log.finer("Connection made...");
                PatientItemRepository r = new PatientItemRepository(conn, conn, "MSC PATIENT DASHBOARD");
                log.finer("HERE!! " + r.toString());
                Collection<IsAPatientItem> vista_list = r.getAllergies(id, false);
                // an ArrayList of PatientAllergies objects is returned -  converty to hData Allergy type
                for (IsAPatientItem a : vista_list) {
                    Allergy allergy = new Allergy();  //hData type
                    PatientAllergy pa = (PatientAllergy) a;  //vista (ovid) type
                    //populate

                    Product c = new Product();
                    c.setValue(capitalizeString(pa.getMessage()));
                    allergy.setProduct(c);
                    //set time for adverse reaction
                    if (pa.getDateTime() != null) {
                        GregorianCalendar cal = new GregorianCalendar();
                        cal.setTime(pa.getDateTime());
                        DatatypeFactory factory = DatatypeFactory.newInstance();
                        DateRange d = new DateRange();
                        d.setLow(factory.newXMLGregorianCalendar(cal));
                        allergy.setAdverseEventDate(d);
                    }

                    Reaction re = new Reaction();
                    re.setValue(capitalizeString(pa.getSigns()));
                    allergy.setReaction(re);


                    // System.out.println(pa.toString());
                    //add to the list
                    list.add(allergy);
                }
            } else {
                log.warning("BAD CONNECTION");
            }

        } catch (Throwable e) {
            log.throwing(KEY, "Error retreiving PatientItems", e);
        } finally {
            closeConnection(conn);
            return list;
        }
    }

    public List<Support> getSupportInfo(String id) {
        List<Support> list = new ArrayList<Support>();
        RPCBrokerPooledConnection conn = null;
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
            return list;
        }
    }

    public List<Medication> getMedications(String id) {
        List<Medication> list = new ArrayList<Medication>();
        RPCBrokerPooledConnection conn = null;
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
            return list;
        }
    }

    public List<Immunization> getImmunizations(String id) {
        List<Immunization> list = new ArrayList<Immunization>();
        RPCBrokerPooledConnection conn = null;
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
            return list;
        }
    }

    private List<Result> getVitals(String id, boolean latest) {
        RPCBrokerPooledConnection conn = null;
        List<Result> results = new ArrayList<Result>();
       // RPCBrokerConnection conn = null;
        try {
            conn = setConnection();
            //conn = new RPCBrokerConnection("medcafe.mitre.org", 9201, "OV1234", "OV1234!!");
            if (conn != null) {
                log.finer("Connection made . . .");
                conn.setContext("MSC PATIENT DASHBOARD");
                PatientItemRepository r = new PatientItemRepository(conn, conn, "MSC PATIENT DASHBOARD");
                log.finer("HERE!! " + r.toString());
                GregorianCalendar calendar = new GregorianCalendar();

                calendar.roll(Calendar.YEAR, -100);
                Date earlyDate = calendar.getTime();
                Collection<IsAPatientItem> vitalItems = r.getVitals(id, earlyDate, new Date());
                Collection<PatientVitalEvent> vitals = new ArrayList<PatientVitalEvent>();
                for (IsAPatientItem vitalItem : vitalItems) {
                		PatientVitalEvent item = (PatientVitalEvent) vitalItem;
                    vitals.add(item);
                }
                if (vitals.size()>0) {
                	if (latest) {
                    PriorityQueue<PatientVitalEvent> vitalQueue = new PriorityQueue<PatientVitalEvent>(vitals.size(), new PatientVitalComparator());
                    vitalQueue.addAll(vitals);
                    vitals = new ArrayList<PatientVitalEvent>();
                    vitals.add(vitalQueue.peek());
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
                            if (stringExists(detail.getBmi())){
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
            log.severe("Error retrieving patient vitals.");
            e.printStackTrace(System.err);
        } finally {
             conn.close();
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
        RPCBrokerPooledConnection conn = null;
        try {
            conn = setConnection();
            if (conn != null) {
                log.finer("Connection made...");
                PatientItemRepository r = new PatientItemRepository(conn, conn, "MSC PATIENT DASHBOARD");
                log.finer("HERE!! " + r.toString());
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

                    //System.out.println(problem.toString());
                    // System.out.println(pa.toString());
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
            return list;
        }
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
        RPCBrokerPooledConnection conn = null;

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
        RPCBrokerPooledConnection conn = null;
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
                //System.out.println(visit);
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
   private String capitalizeString(String toBeFixed)
   {
   	return capitalizeString(toBeFixed, false);
   }
	private String capitalizeString(String toBeFixed, boolean medString)
	{	
		if (toBeFixed != null && !toBeFixed.equals("")){
			String returnString = "";
			toBeFixed = toBeFixed.toLowerCase();
			String delimiters = ",/.;:-_()&!? []{}0123456789";
			StringTokenizer parts = new StringTokenizer(toBeFixed, delimiters, true);
			while (parts.hasMoreTokens())
			{
				String word = parts.nextToken();
				if (delimiters.contains(word))
				{
					returnString = returnString + word;
				}
				else
				{
					returnString = returnString + word.substring(0,1).toUpperCase() + word.substring(1);
				}
			}
			if (!medString)
				return returnString;
			else {
				returnString = returnString.replace("Mg", "mg");
				return returnString.replace("Ml", "mL");
			}
		}
		return toBeFixed;
	}
    private class PatientVitalComparator implements Comparator<PatientVitalEvent> {

        public PatientVitalComparator() {
            super();
        }

        public int compare(PatientVitalEvent one, PatientVitalEvent two) {
            return two.getDateTime().compareTo(one.getDateTime());
        }
    }
}

