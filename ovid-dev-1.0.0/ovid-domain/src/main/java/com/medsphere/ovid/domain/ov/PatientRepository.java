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
package com.medsphere.ovid.domain.ov;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.medsphere.fileman.FMField;
import com.medsphere.fileman.FMFile;
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
import com.medsphere.fmdomain.FMMaritalStatus;
import com.medsphere.fmdomain.FMPatient;
import com.medsphere.fmdomain.FMPatientContact;
import com.medsphere.fmdomain.FMRaceInformation;
import com.medsphere.fmdomain.FMPatientMovement;
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

    private Logger logger = Logger.getLogger(PatientRepository.class);

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
        FMPatient patient = null;
        RPCConnection connection = null;

        try {
            connection = getServerConnection();

            ResAdapter adapter = obtainServerRPCAdapter();

            FMQueryList query = new FMQueryList(adapter, FMPatient.getFileInfoForClass());

            FMScreen byIEN = new FMScreenEquals(new FMScreenIEN(), new FMScreenValue(ien));
            query.setScreen(byIEN);
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
        Collection<FMPatient> list = new ArrayList<FMPatient>();
        RPCConnection connection = null;
        try {
            connection = getServerConnection();

            ResAdapter adapter = obtainServerRPCAdapter();

            FMQueryList query = new FMQueryList(adapter, FMPatient.getFileInfoForClass());

            FMScreen byIENScreen = null;
            for (String patientIEN : iens) {
                if (byIENScreen == null) {
                    byIENScreen = new FMScreenEquals(new FMScreenIEN(), new FMScreenValue(patientIEN));
                } else {
                    byIENScreen = new FMScreenOr(byIENScreen, new FMScreenEquals(new FMScreenIEN(), new FMScreenValue(patientIEN)));
                }
            }

            query.setScreen(byIENScreen);
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
        ResAdapter adapter =  obtainServerRPCAdapter();

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
        }
        catch (OvidDomainException ovidE)
        {
            throw ovidE;
        }
        catch (ResException resE)
        {
            logger.error("Error unable to insert because "+ resE.getMessage());
        }

    }

        public void updatePatient(FMPatientContact patient, FMNameComponents nameComp) throws OvidDomainException {

        try {
        ResAdapter adapter =  obtainServerRPCAdapter();

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
        }
        catch (OvidDomainException ovidE)
        {
            throw ovidE;
        }
        catch (ResException resE)
        {
            logger.error("Error unable to update because "+ resE.getMessage());
        }

    }
/*  not implemented yet   
     private void admitPatient(FMPatientContact patient, FMPatientMovement admit) throws OvidDomainException {

        try {
        ResAdapter adapter =  obtainServerRPCAdapter();

        FMInsert insert = new FMInsert(adapter);
        admit.setPatient(patient);
        insert.setEntry(admit);

        FMResultSet results = insert.execute();
        if (results == null || results.getError() != null) {
            logger.error("Error, unable to insert because " + results.getError());
        } else {
            System.out.println("added as IEN " + patient.getIEN());
        }
        }
        catch (OvidDomainException ovidE)
        {
            throw ovidE;
        }
        catch (ResException resE)
        {
            logger.error("Error unable to insert because "+ resE.getMessage());
        }

    }
*/

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
                query = new FMQueryList( adapter, nameComponent );
                FMScreen nameComponentScreen = null;
                for (FMPatient pat : patients) {
                    FMScreen forOnePatient = new FMScreenAnd(
                                                   new FMScreenAnd(new FMScreenEquals(new FMScreenField("FILE"), new FMScreenValue("2")),
                                                                 new FMScreenEquals(new FMScreenField("FIELD"), new FMScreenValue(".01"))),
                                             new FMScreenEquals(new FMScreenField(".03"), new FMScreenValue(pat.getIEN()+",")));

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
                        if ((patient.getIEN()+",").equals(entry.getValue(".03"))) {
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

            FMQueryList query = new FMQueryList(adapter, FMDemographicPatient.getFileInfoForClass());

            FMScreen byIEN = null;
            for (String ien : iens) {
                if (byIEN == null) {
                    byIEN = new FMScreenEquals(new FMScreenIEN(), new FMScreenValue(ien));
                } else {
                    byIEN = new FMScreenOr(byIEN, new FMScreenEquals(new FMScreenIEN(), new FMScreenValue(ien)));
                }
            }

            query.setScreen(byIEN);
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

        fetchNameComponents((Collection)patients);

        return patients;

    }

    public Collection<FMRaceInformation> getRaceInformation(FMDemographicPatient patient) throws OvidDomainException {
        Collection<FMRaceInformation> raceInformationList = new ArrayList<FMRaceInformation>();
        try {
            ResAdapter adapter = obtainServerRPCAdapter();
            FMRaceInformation raceInformation = patient.getRaceInformation();

            FMQueryList query = new FMQueryList( adapter,  raceInformation.getFile());
            FMResultSet results = query.execute();
            while (results.next()) {
                FMRaceInformation qRace = new FMRaceInformation(results);
                raceInformationList.add(qRace);
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
            while (results.next()) {
                FMEthnicity qEthnicity = new FMEthnicity(results);
                ethnicityList.add(qEthnicity);
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
            FMQueryList query = new FMQueryList(adapter, FMMaritalStatus.getFileInfoForClass());

            FMScreen byIEN =  new FMScreenEquals(new FMScreenIEN(), new FMScreenValue(patient.getMaritalStatus().toString()));

            query.setScreen(byIEN);
            FMResultSet results = query.execute();
            if (results != null) {
                if (results.getError() != null) {
                    throw new OvidDomainException(results.getError());
                }
                if (results.next()) {
                    maritalStatus = new FMMaritalStatus(results);
                }
            } else {
                throw new OvidDomainException("marital status not found for ien(s) " + patient.getMaritalStatus());
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

            FMQueryList query = new FMQueryList(adapter, FMPatientContact.getFileInfoForClass());

            FMScreen byIEN = null;
            for (String ien : iens) {
                if (byIEN == null) {
                    byIEN = new FMScreenEquals(new FMScreenIEN(), new FMScreenValue(ien));
                } else {
                    byIEN = new FMScreenOr(byIEN, new FMScreenEquals(new FMScreenIEN(), new FMScreenValue(ien)));
                }
            }

            query.setScreen(byIEN);
            query.setPacked(false);
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
            } else {
                throw new OvidDomainException("patient not found for ien(s) " + iens);
            }
        } catch (ResException e) {
            throw new OvidDomainException(e);
        }
        fetchNameComponents((Collection)patients);
        return patients;

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
                return patients;
            }
            Collection<String> iens = new ArrayList<String>();
            for (String item : items) {
                String parts[] = item.split("\\^",-1);
                if (parts.length > 0) {
                    String id = parts[0];
                    iens.add(id);
                }

            }
            patients = getPatientsForIENS(iens);
        } catch (RPCException ex) {
            logger.error(ex);
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

    public static void main(String[] args) throws OvidDomainException {
        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.ERROR);
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

            for (FMPatient lpatient : new PatientRepository(userConn, serverConn).getPatientsByName("SMITH,BETTY")) {
                System.out.println("byName patient: " + lpatient.getName()
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
            for (FMPatient lpatient : new PatientRepository(userConn, serverConn).lookupPatientsByName("SM,B")) {
                System.out.println("lookup patient: " + lpatient.getName()
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

            for (FMPatient lpatient : new PatientRepository(userConn, serverConn).getAllPatients()) {
                System.out.println("patient: " + lpatient.getName()
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
            System.out.println("patient: " + patient.getName() + ", " + patient.getDisplayAge());

            Collection<String> ids = new ArrayList<String>();
            ids.add("3");
            ids.add("1");
            for (FMDemographicPatient demo : new PatientRepository(userConn, serverConn).getDemographics(ids)) {
                System.out.println("Demograpics: " + demo.toString());
                System.out.println("===> " + new PatientRepository(userConn, serverConn).getRaceInformation(demo));
                System.out.println("===> " + new PatientRepository(userConn, serverConn).getMaritalStatus(demo));
                System.out.println("===> " + new PatientRepository(userConn, serverConn).getEthicity(demo));
            }

            for (FMPatientContact contact : new PatientRepository(userConn, serverConn).getContacts(ids)) {
                System.out.println("Contacts: " + contact.toString());
                for (FMPatientContact.ContactInfo contactInfo : contact.getContacts()) {
                    System.out.println("\t" + contactInfo);
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
