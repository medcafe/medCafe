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

import com.medsphere.fileman.FMField;
import com.medsphere.fileman.FMQueryList;
import com.medsphere.fileman.FMResultSet;
import com.medsphere.fileman.FMScreen;
import com.medsphere.fileman.FMScreenEquals;
import com.medsphere.fileman.FMScreenIEN;
import com.medsphere.fileman.FMScreenOr;
import com.medsphere.fileman.FMScreenValue;
import com.medsphere.fmdomain.FMPatient;
import com.medsphere.ovid.connection.OvidVistaLinkConnectionException;
import com.medsphere.resource.ResAdapter;
import com.medsphere.resource.ResException;
import com.medsphere.vistalink.VistaLinkPooledConnection;
import gov.va.med.vistalink.adapter.cci.VistaLinkConnection;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class PatientRepository extends OvidSecureRepository {

    public PatientRepository(VistaLinkConnection connection) {
        super(connection);
    }

    /**
     * Get a patient by internal id
     * @param ien
     * @return FMPatient
     */
    public FMPatient getPatientByIEN(String ien) throws OvidDomainException {
        FMPatient patient = null;
        VistaLinkConnection connection = null;

        try {
            connection = getConnection();

            ResAdapter adapter = obtainVistaLinkAdapter();

            FMQueryList query = new FMQueryList(adapter, FMPatient.getFileInfoForClass());

            FMScreen byIEN = new FMScreenEquals(new FMScreenIEN(), new FMScreenValue(ien));
            // byIEN.toStream(System.out);
            query.setScreen(byIEN);
            query.getField("ATTENDING PHYSICIAN").setInternal(false);
            query.getField("CURRENT ROOM").setInternal(false);
            query.getField("CURRENT MOVEMENT").setInternal(false);
            query.getField("PROVIDER").setInternal(false);
            FMResultSet results = query.execute();
            // System.out.println(String.valueOf(results));
            if (results != null) {
                if (results.getError() != null) {
                    throw new OvidVistaLinkConnectionException(results.getError());
                }
                if (results.next()) {
                    System.out.println("got one!");
                    patient = new FMPatient(results);
                }
                else
                    System.out.println("Didn't find " + ien);
            }
        } catch (OvidVistaLinkConnectionException e) {
            throw new OvidDomainException(e);
        } catch (ResException e) {
            throw new OvidDomainException(e);
        }
        return patient;
    }

    /**
     * get patient by IEN, filling in passed FMPatient object
     * @param ien
     * @param patient
     * @throws com.medsphere.ovid.connection.OvidVistaLinkConnectionException
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
     * @throws com.medsphere.ovid.connection.OvidVistaLinkConnectionException
     */
    public Collection<FMPatient> getPatientsForIENS(Collection<String> iens) throws OvidDomainException {
        Collection<FMPatient> list = new ArrayList<FMPatient>();
        VistaLinkConnection connection = null;
        try {
            connection = getConnection();

            ResAdapter adapter = obtainVistaLinkAdapter();

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
            FMResultSet results = query.execute();
            if (results != null) {
                if (results.getError() != null) {
                    throw new OvidDomainException(results.getError());
                }
                while (results.next()) {
                    list.add(new FMPatient(results));
                }
            }
        } catch (OvidVistaLinkConnectionException e) {
            throw new OvidDomainException(e);
        } catch (ResException e) {
            throw new OvidDomainException(e);
        }

        return list;

    }

    public Collection<FMPatient> getAllPatients() throws OvidDomainException {
        Collection<FMPatient> list = new ArrayList<FMPatient>();
        VistaLinkConnection connection = null;
        try {
            connection = getConnection();

            ResAdapter adapter = obtainVistaLinkAdapter();

            FMQueryList query = new FMQueryList(adapter, FMPatient.getFileInfoForClass());

            query.getField("ATTENDING PHYSICIAN").setInternal(false);
            query.getField("CURRENT ROOM").setInternal(false);
            query.getField("CURRENT MOVEMENT").setInternal(false);
            query.getField("PROVIDER").setInternal(false);
            FMResultSet results = query.execute();
            if (results != null) {
                if (results.getError() != null) {
                    throw new OvidDomainException(results.getError());
                }
                while (results.next()) {
                    list.add(new FMPatient(results));
                }
            }
        } catch (OvidVistaLinkConnectionException e) {
            throw new OvidDomainException(e);
        } catch (ResException e) {
            throw new OvidDomainException(e);
        }

        return list;

    }
    public static void main(String[] args) throws OvidDomainException {
        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.ERROR);
        VistaLinkPooledConnection conn = null;
        try {

            //args = new String[] { "localhost", "8002", "OV1234", "OV1234!!"};

            if (args.length < 4) {
                System.err.println("usage: PatientRepository <host> <port> <ovid-access-code> <ovid-verify-code>");
                return;
            }

            conn = getDirectConnection(args[0], args[1], args[2], args[3]);

            if (conn==null) {
                return;
            }

            for (FMPatient lpatient : new PatientRepository(conn).getAllPatients()) {
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
                        );

            }
        } finally {
            conn.close();
        }
    }
}
