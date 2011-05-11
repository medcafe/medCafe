// <editor-fold defaultstate="collapsed">
/*
 * Copyright (C) 2007-2009  Medsphere Systems Corporation
 * All rights reserved.
 *
 * This source code contains the intellectual property
 * of its copyright holder(s), and is made available
 * under a license. If you do not know the terms of
 * the license, please stop and do not read further.
 *
 * Please read LICENSES for detailed information about
 * the license this source code file is available under.
 * Questions should be directed to legal@medsphere.com
 *
 */
// </editor-fold>

package com.medsphere.ovid.domain.ov;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.medsphere.fileman.FMField;
import com.medsphere.fileman.FMFile;
import com.medsphere.fileman.FMQueryByIENS;
import com.medsphere.fileman.FMQueryFind;
import com.medsphere.fileman.FMQueryList;
import com.medsphere.fileman.FMRecord;
import com.medsphere.fileman.FMResultSet;
import com.medsphere.fileman.FMScreen;
import com.medsphere.fileman.FMScreenAnd;
import com.medsphere.fileman.FMScreenEquals;
import com.medsphere.fileman.FMScreenField;
import com.medsphere.fileman.FMScreenIEN;
import com.medsphere.fileman.FMScreenOr;
import com.medsphere.fileman.FMScreenValue;
import com.medsphere.fileman.FMInsert;
import com.medsphere.fileman.FMUpdate;
import com.medsphere.fmdomain.FMNameComponents;
import com.medsphere.fmdomain.FMDemographicPatient;
import com.medsphere.fmdomain.FMEthnicity;
import com.medsphere.fmdomain.FMHealthRecordNumber;
import com.medsphere.fmdomain.FMMaritalStatus;
import com.medsphere.fmdomain.FMPatient;
import com.medsphere.fmdomain.FMPatientContact;
import com.medsphere.fmdomain.FMPatientRPMS;
import com.medsphere.fmdomain.FMRaceInformation;
import com.medsphere.resource.ResAdapter;
import com.medsphere.resource.ResException;
import com.medsphere.vistarpc.RPCArray;
import com.medsphere.vistarpc.RPCBrokerConnection;
import com.medsphere.vistarpc.RPCConnection;
import com.medsphere.vistarpc.RPCException;
import com.medsphere.vistarpc.RPCResponse;
import com.medsphere.vistarpc.VistaRPC;
import com.medsphere.vistarpc.RPCResponse.ResponseType;

public class PatientRepository extends OvidSecureRepository {

     static private Logger logger = LoggerFactory.getLogger(PatientRepository.class);

    public PatientRepository(RPCConnection serverConnection) {
        super(null, serverConnection);
    }
    public PatientRepository(RPCConnection connection, RPCConnection serverCconnection) {
        super(connection, serverCconnection);
    }

    /**
     * Get a patient by internal id
     * @param ien
     * @return FMPatient
     */
    public FMPatient getPatientByIEN(String ien) throws OvidDomainException {
        return getPatientByIEN(ien, false);
    }

    /**
     * Get a patient by internal id - returns internal values for certain fields if getInternal = true
     * @param ien
     * @param getInternal
     * @return FMPatient
     */
    public FMPatient getPatientByIEN(String ien, Boolean getInternal) throws OvidDomainException {
        FMPatient patient = null;
        RPCConnection connection = null;

        try {
            connection = getServerConnection();

            ResAdapter adapter = obtainServerRPCAdapter();

            FMQueryByIENS query = new FMQueryByIENS(adapter, FMPatient.getFileInfoForClass());
            query.addIEN(ien);
            query.getField("ATTENDING PHYSICIAN").setInternal(getInternal);
            query.getField("CURRENT ROOM").setInternal(getInternal);
            query.getField("CURRENT MOVEMENT").setInternal(getInternal);
            query.getField("PROVIDER").setInternal(getInternal);
            query.getField("PLACE OF BIRTH (STATE)").setInternal(false);
            FMResultSet results = query.execute();
            if (results != null) {
                if (results.getError() != null) {
                    throw new OvidDomainException(results.getError());
                }
                if (results.next()) {
                    patient = new FMPatient(results);
                }
            } else {
                throw new OvidDomainException("patient not found for ien " + ien);
            }
        } catch (ResException e) {
            throw new OvidDomainException(e);
        }

        return patient;
    }

    /**
     * get patient by IEN, filling in passed FMPatient object
     * @param ien
     * @param patient
     * @throws com.medsphere.ovid.connection.OvidDomainException
     */
    public void getPatientByIEN(String ien, FMPatient patient) throws OvidDomainException {

        FMPatient tmp = getPatientByIEN(ien);
        patient.clearModifiedFields();
        patient.setIENS(tmp.getIENS());
        for (FMField field : tmp.getFields()) {
            if (tmp.getValue(field.getName()) != null) {
                patient.setValue(field.getName(), tmp.getValue(field.getName()));
            }
        }
    }

    /**
     * Get a list of patients for a list of iens
     * @param iens
     * @return
     * @throws com.medsphere.ovid.connection.OvidDomainException
     */
    public Collection<FMPatient> getPatientsForIENS(Collection<String> iens) throws OvidDomainException {
        return getPatientsForIENS(iens, false);
    }

    /**
     * Get a list of patients for a list of iens
     * @param iens
     * @return
     * @throws com.medsphere.ovid.connection.OvidDomainException
     */
    public Collection<FMPatient> getPatientsForIENS(Collection<String> iens, Boolean getInternal) throws OvidDomainException {
        Collection<FMPatient> list = new ArrayList<FMPatient>();
        RPCConnection connection = null;
        try {
            connection = getServerConnection();

            ResAdapter adapter = obtainServerRPCAdapter();

            FMQueryByIENS query = new FMQueryByIENS(adapter, FMPatient.getFileInfoForClass());
            query.setIENS(iens);
            query.getField("ATTENDING PHYSICIAN").setInternal(getInternal);
            query.getField("CURRENT ROOM").setInternal(getInternal);
            query.getField("CURRENT MOVEMENT").setInternal(getInternal);
            query.getField("PROVIDER").setInternal(getInternal);
            query.getField("PLACE OF BIRTH (STATE)").setInternal(false);
            FMResultSet results = query.execute();
            if (results != null) {
                if (results.getError() != null) {
                    throw new OvidDomainException(results.getError());
                }
                while (results.next()) {
                    list.add(new FMPatient(results));
                }
            }
        } catch (ResException e) {
            throw new OvidDomainException(e);
        }

        return list;

    }

    /**
     * Get patients by name (LAST,FIRST)
     * @param name
     * @return
     * @throws OvidDomainException
     */
    public Collection<FMPatient> getPatientsByName(String name) throws OvidDomainException {
        Collection<FMPatient> list = new ArrayList<FMPatient>();

        try {
            ResAdapter adapter = obtainServerRPCAdapter();

            FMQueryFind query = new FMQueryFind(adapter, FMPatient.getFileInfoForClass());
            query.setIndex("B", name);
            query.setScreen(new FMScreenEquals(new FMScreenField("NAME"), new FMScreenValue(name)));
            query.getField("ATTENDING PHYSICIAN").setInternal(false);
            query.getField("CURRENT ROOM").setInternal(false);
            query.getField("CURRENT MOVEMENT").setInternal(false);
            query.getField("PROVIDER").setInternal(false);
            query.getField("PLACE OF BIRTH (STATE)").setInternal(false);
            FMResultSet results = query.execute();
            if (results != null) {
                if (results.getError() != null) {
                    throw new OvidDomainException(results.getError());
                }
                while (results.next()) {
                    list.add(new FMPatient(results));
                }
            }
        } catch (ResException e) {
            throw new OvidDomainException(e);
        }

        return list;

    }
// added

    public void addPatient(FMPatientContact patient, FMNameComponents nameComp) throws OvidDomainException {

        try {
            ResAdapter adapter = obtainServerRPCAdapter();

            FMInsert insert = new FMInsert(adapter);

            patient.setName(nameComp);
            insert.setEntry(patient);

            FMResultSet results = insert.execute();
            if (results == null || results.getError() != null) {
                logger.error("Error, unable to insert because " + results.getError());
            } else {
                System.out.println("added as IEN " + patient.getIEN());
                nameComp.setIENS(patient.getIEN());
                insert = new FMInsert(adapter);
                insert.setEntry(nameComp);
                results = insert.execute();
                if (results == null || results.getError() != null) {
                    logger.error("Error, unable to insert because " + results.getError());
                } else {
                    System.out.println("added as IEN " + nameComp.getIEN());

                }
            }
        } catch (OvidDomainException ovidE) {
            throw ovidE;
        } catch (ResException resE) {
            logger.error("Error unable to insert because " + resE.getMessage());
        }

    }

    public void updatePatient(FMPatientContact patient, FMNameComponents nameComp) throws OvidDomainException {

        try {
            ResAdapter adapter = obtainServerRPCAdapter();

            FMUpdate update = new FMUpdate(adapter);
            patient.setName(nameComp);

            update.setEntry(patient);

            FMResultSet results = update.execute();
            if (results == null || results.getError() != null) {
                logger.error("Error, unable to update because " + results.getError());
            } else {
                System.out.println("updated IEN " + patient.getIEN());
                nameComp.setIENS(patient.getIEN());
            }

            update = new FMUpdate(adapter);


            update.setEntry(nameComp);

            results = update.execute();
            if (results == null || results.getError() != null) {
                logger.error("Error, unable to update because " + results.getError());
            } else {
                System.out.println("updated name components for " + patient.getIEN());
            }
        } catch (OvidDomainException ovidE) {
            throw ovidE;
        } catch (ResException resE) {
            logger.error("Error unable to update because " + resE.getMessage());
        }

    }
    /**
     * get RPMS patient (file 9000001) by IEN
     * @param ien
     * @return
     * @throws com.medsphere.ovid.connection.OvidDomainException
     */
    public FMPatientRPMS getPatientRPMSByIEN(String ien) throws OvidDomainException {
        FMPatientRPMS patient = null;
        RPCConnection connection = null;

        try {
            connection = getServerConnection();

            ResAdapter adapter = obtainServerRPCAdapter();

            FMQueryByIENS query = new FMQueryByIENS(adapter, FMPatientRPMS.getFileInfoForClass());
            query.addIEN(ien);
            FMResultSet results = query.execute();
            if (results != null) {
                if (results.getError() != null) {
                    throw new OvidDomainException(results.getError());
                }
                if (results.next()) {
                    patient = new FMPatientRPMS(results);
                }
            } else {
                throw new OvidDomainException("patient not found for ien " + ien);
            }
        } catch (ResException e) {
            throw new OvidDomainException(e);
        }

        return patient;
    }
    public void fetchNameComponents(FMPatient patient) throws OvidDomainException {
        Collection<FMPatient> collection = new ArrayList<FMPatient>();
        collection.add(patient);
        fetchNameComponents(collection);
    }

    public void fetchNameComponents(Collection<FMPatient> patients) throws OvidDomainException {
        if (patients.size() > 0) {
            FMFile nameComponent = new FMFile("20");
            nameComponent.addField("1"); // family name
            nameComponent.addField("2"); // given name
            nameComponent.addField("3"); // middle name
            nameComponent.addField("4"); // prefix
            nameComponent.addField("5"); // suffix
            nameComponent.addField("6"); // degree
            nameComponent.addField(".03"); // iens

            try {
                FMQueryList query;
                ResAdapter adapter = obtainServerRPCAdapter();
                query = new FMQueryList(adapter, nameComponent);
                FMScreen nameComponentScreen = null;
                for (FMPatient pat : patients) {
                    FMScreen forOnePatient = new FMScreenAnd(
                            new FMScreenAnd(new FMScreenEquals(new FMScreenField(".01"), new FMScreenValue("2")),
                            new FMScreenEquals(new FMScreenField(".02"), new FMScreenValue(".01"))),
                            new FMScreenEquals(new FMScreenField(".03"), new FMScreenValue(pat.getIEN() + ",")));

                    if (nameComponentScreen == null) {
                        nameComponentScreen = forOnePatient;
                    } else {
                        nameComponentScreen = new FMScreenOr(nameComponentScreen, forOnePatient);
                    }
                }

                query.setScreen(nameComponentScreen);
                FMResultSet results = query.execute();
                while (results.next()) {
                    FMRecord entry = new FMRecord(results);
                    for (FMPatient patient : patients) {
                        if ((patient.getIEN() + ",").equals(entry.getValue(".03"))) {
                            patient.setFamilyName(entry.getValue("1"));
                            patient.setGivenName(entry.getValue("2"));
                            patient.setMiddleName(entry.getValue("3"));
                            patient.setPrefix(entry.getValue("4"));
                            patient.setSuffix(entry.getValue("5"));
                            patient.setDegree(entry.getValue("6"));
                        }
                    }
                }

            } catch (OvidDomainException e) {
                throw e;
            } catch (ResException e) {
                throw new OvidDomainException(e);
            }

        }

    }


    public Collection<FMDemographicPatient> getDemographics(Collection<String> iens) throws OvidDomainException {
        Collection<FMDemographicPatient> patients = new ArrayList<FMDemographicPatient>();

        try {

            ResAdapter adapter = obtainServerRPCAdapter();

            FMQueryByIENS query = new FMQueryByIENS(adapter, FMDemographicPatient.getFileInfoForClass());
            query.setIENS(iens);

            query.getField("ATTENDING PHYSICIAN").setInternal(false);
            query.getField("CURRENT ROOM").setInternal(false);
            query.getField("CURRENT MOVEMENT").setInternal(false);
            query.getField("PROVIDER").setInternal(false);
            query.getField("STATE").setInternal(false);
            query.getField("PLACE OF BIRTH (STATE)").setInternal(false);
            FMResultSet results = query.execute();
            if (results != null) {
                if (results.getError() != null) {
                    throw new OvidDomainException(results.getError());
                }
                while (results.next()) {
                    FMDemographicPatient patient = new FMDemographicPatient(results);
                    patients.add(patient);
                }
            } else {
                throw new OvidDomainException("patient not found for ien(s) " + iens);
            }
        } catch (ResException e) {
            throw new OvidDomainException(e);
        }

        new NameComponentRepository(getServerConnection()).fetchNameComponents((Collection)patients);

        return patients;

    }

    public Collection<FMRaceInformation> getRaceInformation(FMDemographicPatient patient) throws OvidDomainException {
        Collection<FMRaceInformation> raceInformationList = new ArrayList<FMRaceInformation>();
        try {
            ResAdapter adapter = obtainServerRPCAdapter();
            FMRaceInformation raceInformation = patient.getRaceInformation();

            FMQueryList query = new FMQueryList( adapter,  raceInformation.getFile());
            query.getField("RACE INFORMATION").setInternal(false);
            query.getField("METHOD OF COLLECTION").setInternal(false);
            FMResultSet results = query.execute();
            if (results != null) {
                if (results.getError() != null) {
                    throw new OvidDomainException(results.getError());
                }
                while (results.next()) {
                    FMRaceInformation qRace = new FMRaceInformation(results);
                    raceInformationList.add(qRace);
                }
            }
        } catch (ResException e) {
            throw new OvidDomainException(e);
        } finally {}
        return raceInformationList;
    }

    public Collection<FMEthnicity> getEthicity(FMDemographicPatient patient) throws OvidDomainException {
        Collection<FMEthnicity> ethnicityList = new ArrayList<FMEthnicity>();
        try {
            ResAdapter adapter = obtainServerRPCAdapter();
            FMEthnicity ethnicity = patient.getEthnicity();

            FMQueryList query = new FMQueryList( adapter,  ethnicity.getFile());
            query.getField("ETHNICITY INFORMATION").setInternal(false);
            query.getField("METHOD OF COLLECTION").setInternal(false);
            FMResultSet results = query.execute();
            if (results != null) {
                if (results.getError() != null) {
                    throw new OvidDomainException(results.getError());
                }
               while (results.next()) {
                    FMEthnicity qEthnicity = new FMEthnicity(results);
                    ethnicityList.add(qEthnicity);
                }
            }
        } catch (ResException e) {
            throw new OvidDomainException(e);
        } finally {}
        return ethnicityList;
    }

    public FMMaritalStatus getMaritalStatus(FMDemographicPatient patient) throws OvidDomainException {
        FMMaritalStatus maritalStatus = null;

        if (patient == null || patient.getMaritalStatus() == null) {
            return null;
        }
        try {
            ResAdapter adapter = obtainServerRPCAdapter();
            FMQueryByIENS query = new FMQueryByIENS(adapter, FMMaritalStatus.getFileInfoForClass());
            query.addIEN(String.valueOf(patient.getMaritalStatus()));

            FMResultSet results = query.execute();
            if (results != null) {
                if (results.getError() != null) {
                    throw new OvidDomainException(results.getError());
                }
                if (results.next()) {
                    maritalStatus = new FMMaritalStatus(results);
                }
            }
        } catch (ResException e) {
            throw new OvidDomainException(e);
        }
        return maritalStatus;
    }

    public Collection<FMPatientContact> getContacts(Collection<String> iens) throws OvidDomainException {
        Collection<FMPatientContact> patients = new ArrayList<FMPatientContact>();

        try {

            ResAdapter adapter = obtainServerRPCAdapter();

            FMQueryByIENS query = new FMQueryByIENS(adapter, FMPatientContact.getFileInfoForClass());
            query.setIENS(iens);

            query.getField("ATTENDING PHYSICIAN").setInternal(false);
            query.getField("CURRENT ROOM").setInternal(false);
            query.getField("CURRENT MOVEMENT").setInternal(false);
            query.getField("PROVIDER").setInternal(false);
            query.getField("STATE").setInternal(false);
            query.getField("K-STATE").setInternal(false);
            query.getField("K2-STATE").setInternal(false);
            query.getField("SPOUSE'S EMPLOYER'S STATE").setInternal(false);
            query.getField("STATE (VA)").setInternal(false);
            query.getField("STATE (CIVIL)").setInternal(false);
            query.getField("EMPLOYER STATE").setInternal(false);
            query.getField("E-STATE").setInternal(false);
            query.getField("E2-STATE").setInternal(false);
            query.getField("D-STATE").setInternal(false);
            query.getField("PLACE OF BIRTH (STATE)").setInternal(false);
            FMResultSet results = query.execute();
            if (results != null) {
                if (results.getError() != null) {
                    throw new OvidDomainException(results.getError());
                }
                while (results.next()) {
                    FMPatientContact patient = new FMPatientContact(results);
                    patients.add(patient);
                }
            }
            else
            {
            	throw new OvidDomainException("Results of query were null, context was");
            }
        } catch (ResException e) {
            throw new OvidDomainException(e);
        }
        new NameComponentRepository(getServerConnection()).fetchNameComponents((Collection)patients);
        return patients;

    }

    /**
     * get RPMS patient's Health Record Numbers (for all facilities)
     * @param <FMPatientRPMS> patientRPMS
     * @return
     * @throws com.medsphere.ovid.connection.OvidDomainException
     */
    public Collection<FMHealthRecordNumber> getHealthRecordNumber(FMPatientRPMS patientRPMS) throws OvidDomainException {
        Collection<FMHealthRecordNumber> hrnList = new ArrayList<FMHealthRecordNumber>();
        try {
            ResAdapter adapter = obtainServerRPCAdapter();
            FMHealthRecordNumber hrn = patientRPMS.getHealthRecordNumber();

            FMQueryList query = new FMQueryList( adapter,  hrn.getFile());
            FMResultSet results = query.execute();
            if (results != null) {
                if (results.getError() != null) {
                    throw new OvidDomainException(results.getError());
                }
                while (results.next()) {
                    FMHealthRecordNumber qHrn = new FMHealthRecordNumber(results);
                    hrnList.add(qHrn);
                }
            }
        } catch (ResException e) {
            throw new OvidDomainException(e);
        } finally {}
        return hrnList;
    }

    /**
     * get RPMS patient's Health Record Numbers (for all facilities)
     * @param <FMPatientRPMS> patientRPMS
     * @return
     * @throws com.medsphere.ovid.connection.OvidDomainException
     */
    public Collection<FMHealthRecordNumber> getHealthRecordNumber(FMPatientRPMS patientRPMS, String facilityId) throws OvidDomainException {
        Collection<FMHealthRecordNumber> hrnList = new ArrayList<FMHealthRecordNumber>();
        try {
            ResAdapter adapter = obtainServerRPCAdapter();
            FMHealthRecordNumber hrn = patientRPMS.getHealthRecordNumber();

            FMQueryList query = new FMQueryList( adapter,  hrn.getFile());
            FMScreen byFacility = new FMScreenEquals(new FMScreenField("FACILITY"), new FMScreenValue(facilityId));
            FMResultSet results = query.execute();
            if (results != null) {
                if (results.getError() != null) {
                    throw new OvidDomainException(results.getError());
                }
                while (results.next()) {
                    FMHealthRecordNumber qHrn = new FMHealthRecordNumber(results);
                    hrnList.add(qHrn);
                }
            }
        } catch (ResException e) {
            throw new OvidDomainException(e);
        } finally {}
        return hrnList;
    }

    /**
     * lookup by identifier
     * @param id
     * @return
     * @throws OvidDomainException
     */
    public Collection<FMPatient> lookupPatientsByID(String id) throws OvidDomainException {
        Collection<String> ids = new ArrayList<String>();
        ids.add(id);
        return patientLookup(ids);
    }

    /**
     * lookup by list of identifiers
     * @param ids
     * @return
     * @throws OvidDomainException
     */
    public Collection<FMPatient> lookupPatientsByIDs(Collection<String> ids) throws OvidDomainException {
        return patientLookup(ids);
    }

    /**
     * lookup by name (partial names are ok (e.g. LASTNAME).  Uses SC PATIENT LOOKUP RPC
     * @param name
     * @return
     * @throws OvidDomainException
     */
    public Collection<FMPatient> lookupPatientsByName(String name) throws OvidDomainException {
        Collection<String> nameList = new ArrayList<String>();
        nameList.add(name);
        return patientLookup(nameList);
    }

    private final String RPC_CONTEXT = "OR CPRS GUI CHART";
    private Collection<FMPatient> patientLookup(Collection<String> values) throws OvidDomainException {
        Collection<FMPatient> patients = new ArrayList<FMPatient>();

        logger.debug("looking up patients by name: " + values);
        RPCConnection rpcConnection;

        try {
            rpcConnection = getConnection();
            rpcConnection.setContext(RPC_CONTEXT);
        } catch (RPCException ex) {
            throw new OvidDomainException(ex);
        }

        VistaRPC rpc = new VistaRPC("SC PATIENT LOOKUP", ResponseType.ARRAY);
        RPCArray valArray = new RPCArray();
        for (String value : values) {
            valArray.put("\"VALUE\"", value);
        }

        rpc.setParam(1, valArray);
        try {
            RPCResponse response = rpcConnection.execute(rpc);
            String items[] = response.getArray();
            if (isEmptyResult(items)) {
                logger.debug("empty list");
                return patients;
            }
            Collection<String> iens = new ArrayList<String>();
            for (String item : items) {
                String parts[] = item.split("\\^",-1);
                if (parts.length > 0) {
                    String id = parts[0];
                    if (id.matches("\\d+")) {
                        iens.add(id);
                    }
                }

            }
            patients = getPatientsForIENS(iens);
        } catch (RPCException ex) {
            logger.error("RPC exception", ex);
        }

        return patients;
    }

    public Collection<FMPatient> getAllPatients() throws OvidDomainException {
        Collection<FMPatient> list = new ArrayList<FMPatient>();

        try {

            ResAdapter adapter = obtainServerRPCAdapter();

            FMQueryList query = new FMQueryList(adapter, FMPatient.getFileInfoForClass());

            query.getField("ATTENDING PHYSICIAN").setInternal(false);
            query.getField("CURRENT ROOM").setInternal(false);
            query.getField("CURRENT MOVEMENT").setInternal(false);
            query.getField("PROVIDER").setInternal(false);
            query.getField("PLACE OF BIRTH (STATE)").setInternal(false);
            FMResultSet results = query.execute();
            if (results != null) {
                if (results.getError() != null) {
                    throw new OvidDomainException(results.getError());
                }
                while (results.next()) {
                    list.add(new FMPatient(results));
                }
            }
        } catch (ResException e) {
            throw new OvidDomainException(e);
        }

        return list;

    }
    public Collection<FMPatient> searchByNameComponentsForPatients(String family, String given, String middle) throws OvidDomainException {
        Collection<FMPatient> list = new ArrayList<FMPatient>();
        HashMap<String, FMNameComponents> nameMap = new HashMap<String, FMNameComponents>();
        Collection<String> iens = new ArrayList<String>();

        try {
            ResAdapter adapter = obtainServerRPCAdapter();

            FMQueryFind query = new FMQueryFind(adapter, FMNameComponents.getFileInfoForClass());
            query.setIndex("B", "2");
            FMScreen screen = new FMScreenEquals(new FMScreenField("FILE"), new FMScreenValue("2"));
            screen = new FMScreenAnd(screen, new FMScreenEquals(new FMScreenField("FIELD"), new FMScreenValue(".01")));
            query.setScreen(screen);
            FMResultSet results = query.execute();
            if (results != null) {
                if (results.getError() != null) {
                    throw new OvidDomainException(results.getError());
                }
                while (results.next()) {
                    FMNameComponents nameComp = new FMNameComponents(results);


                    boolean match = true;
                    if (family != null && !family.equals("")) {
                        int searchLength = family.length();

                        if (nameComp.getFamilyName() == null || searchLength > nameComp.getFamilyName().length()
                                || !family.equalsIgnoreCase(nameComp.getFamilyName().substring(0, searchLength))) {
                            match = false;
                        }

                    }
                    if (match && given != null && !given.equals("")) {
                        int searchLength = given.length();
                        if (nameComp.getGiven() == null || searchLength > nameComp.getGiven().length()
                                || !given.equalsIgnoreCase(nameComp.getGiven().substring(0, searchLength))) {
                            match = false;
                        }

                    }
                    if (match && middle != null && !middle.equals("")) {
                        int searchLength = middle.length();
                        if (nameComp.getMiddleName() == null || searchLength > nameComp.getMiddleName().length()
                                || !middle.equalsIgnoreCase(nameComp.getMiddleName().substring(0, searchLength))) {
                            match = false;
                        }

                    }
                    if (match) {

                        String pointer = nameComp.getOriginatingFileIEN().substring(0, nameComp.getOriginatingFileIEN().length() - 1);

                        nameMap.put(pointer, nameComp);
                        iens.add(pointer);
                    }

                }

            }
            if (iens.size() > 0) {

                list = getPatientsForIENS(iens);

                for (FMPatient patient : list) {

                    FMNameComponents nameComp = nameMap.get(patient.getIEN());

                    patient.setFamilyName(nameComp.getFamilyName());
                    patient.setMiddleName(nameComp.getMiddleName());
                    patient.setGivenName(nameComp.getGiven());
                    patient.setPrefix(nameComp.getPrefix());
                    patient.setSuffix(nameComp.getSuffix());
                }
            }
        } catch (ResException e) {
            throw new OvidDomainException(e);
        } 

        return list;

    }
    public static void main(String[] args) throws OvidDomainException {
        //BasicConfigurator.configure();
        //Logger.getRootLogger().setLevel(Level.ERROR);
        RPCConnection serverConn = null;
        RPCConnection userConn = null;
        try {

            if (args == null || args.length == 0) {
                args = new String[] { "localhost", "9201", "OV1234", "OV1234!!", "PU1234", "PU1234!!"};
            }
            if (args.length < 6) {
                System.err.println("usage: PatientRepository <host> <port> <ovid-access-code> <ovid-verify-code> <user-access-code> <user-verify-code>");
                return;
            }

            serverConn = new RPCBrokerConnection(args[0], new Integer(args[1]), args[2], args[3]);
            if (serverConn==null) {
                return;
            }

            userConn = new RPCBrokerConnection(args[0], new Integer(args[1]), args[4], args[5]);

            for (FMPatient pat : new PatientRepository(userConn, serverConn).getAllPatients()) {
            	System.out.println("patient: " + pat);
            }
            
            for (FMPatient lpatient : new PatientRepository(userConn, serverConn).getPatientsByName("SMITH,BETTY")) {
                logger.debug("byName patient: " + lpatient.getName()
                        + "\n\tsex = " + lpatient.getSex()
                        + "\n\tdob = " + lpatient.getDob().toString()
                        + "\n\teid = " + lpatient.getEnterprisePatientIdentifier()
                        + "\n\tid = " + lpatient.getId()
                        + "\n\tage = " + lpatient.getAge()
                        + "\n\tdisplay age = " + lpatient.getDisplayAge()
                        + "\n\troom-bed = " + lpatient.getRoomBed()
                        + "\n\tcurrent room = " + lpatient.getCurrentRoom()
                        + "\n\tattending = " + lpatient.getAttendingPhysician()
                        + "\n\tward = " + lpatient.getWardLocation()
                        + "\n\tcurrent movement = " + lpatient.getCurrentMovement()
                        + "\n\tcurrent admission = " + lpatient.getCurrentAdmission()
                        + "\n\tadmitting physician = " + lpatient.getAdmittingPhysician()
                        + "\n\tadmitting diagnosis = " + lpatient.getAdmittingDiagnosis()
                        + "\n\tDFN = " + lpatient.getIEN()
                        );

            }

            // lookup by last name
            for (FMPatient lpatient : new PatientRepository(userConn, serverConn).lookupPatientsByName("PATIENT,CLINICAL F")) {
                logger.debug("lookup patient: " + lpatient.getName()
                        + "\n\tsex = " + lpatient.getSex()
                        + "\n\tdob = " + lpatient.getDob().toString()
                        + "\n\teid = " + lpatient.getEnterprisePatientIdentifier()
                        + "\n\tid = " + lpatient.getId()
                        + "\n\tage = " + lpatient.getAge()
                        + "\n\tdisplay age = " + lpatient.getDisplayAge()
                        + "\n\troom-bed = " + lpatient.getRoomBed()
                        + "\n\tcurrent room = " + lpatient.getCurrentRoom()
                        + "\n\tattending = " + lpatient.getAttendingPhysician()
                        + "\n\tward = " + lpatient.getWardLocation()
                        + "\n\tcurrent movement = " + lpatient.getCurrentMovement()
                        + "\n\tcurrent admission = " + lpatient.getCurrentAdmission()
                        + "\n\tadmitting physician = " + lpatient.getAdmittingPhysician()
                        + "\n\tadmitting diagnosis = " + lpatient.getAdmittingDiagnosis()
                        + "\n\tDFN = " + lpatient.getIEN()
                        );

            }

            FMPatient patient = new PatientRepository(userConn, serverConn).getPatientByIEN("5");
            logger.debug("patient: " + patient.getName() + ", " + patient.getDisplayAge() + ", " + patient.getDob());

            Collection<String> ids = new ArrayList<String>();
            ids.add("3");
            ids.add("1");
            for (FMDemographicPatient demo : new PatientRepository(userConn, serverConn).getDemographics(ids)) {
                logger.debug("Demograpics: " + demo.toString());
                logger.debug("===> " + new PatientRepository(userConn, serverConn).getRaceInformation(demo));
                logger.debug("===> " + new PatientRepository(userConn, serverConn).getMaritalStatus(demo));
                logger.debug("===> " + new PatientRepository(userConn, serverConn).getEthicity(demo));
            }

            for (FMPatientContact contact : new PatientRepository(userConn, serverConn).getContacts(ids)) {
                logger.debug("Contacts: " + contact.toString());
                for (FMPatientContact.ContactInfo contactInfo : contact.getContacts()) {
                    logger.debug("\t" + contactInfo);
                }
            }

        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (RPCException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (serverConn != null) {
                    serverConn.close();
                }
                if (userConn != null) {
                    userConn.close();
                }
            } catch (RPCException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }
}
