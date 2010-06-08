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

package com.medsphere.ovid.tutorial.fm.insert;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.medsphere.fileman.FMInsert;
import com.medsphere.fileman.FMQueryList;
import com.medsphere.fileman.FMResultSet;
import com.medsphere.fileman.FMUtil;
import com.medsphere.resource.ResAdapter;
import com.medsphere.resource.ResException;
import com.medsphere.rpcresadapter.RPCResAdapter;
import com.medsphere.vistarpc.RPCBrokerConnection;
import com.medsphere.vistarpc.RPCConnection;
import com.medsphere.vistarpc.RPCException;


public class PatientInsertTutorial {
    private Logger logger = Logger.getLogger(PatientInsertTutorial.class);
    private RPCConnection connection = null;

    public PatientInsertTutorial(RPCConnection connection) {
        this.connection = connection;
    }

    public Collection<FMPersistPatient> getAllPatients() throws RPCException, ResException  {
        Collection<FMPersistPatient> list = new ArrayList<FMPersistPatient>();

        try {

            try {
                connection.setContext(FMUtil.FM_RPC_CONTEXT);
            } catch (RPCException e) {
                logger.error(e);
                throw e;
            }
            ResAdapter adapter = new RPCResAdapter(connection, FMUtil.FM_RPC_NAME);


            FMQueryList query = new FMQueryList(adapter, FMPersistPatient.getFileInfoForClass());
            query.getField("SEX").setInternal(false);
            query.getField("MARITAL STATUS").setInternal(false);
            FMResultSet results = query.execute();
            if (results != null) {
                if (results.getError() != null) {
                    throw new RPCException(results.getError());
                }
                while (results.next()) {
                    list.add(new FMPersistPatient(results));
                }
            }
        } finally {}

        return list;

    }

    private void printAllPatients() throws RPCException, ResException {
        for (FMPersistPatient patient : this.getAllPatients()) {
            System.out.println("patient: " + patient.getName() + "\tIEN = " + patient.getIEN()
                    + "\n\tdob = " + new SimpleDateFormat("yyyy-dd-MM").format(patient.getDob())
                    + "\n\tsex = " + patient.getSex()
                    + "\n\tmarital status = " + patient.getMaritalStatus()
                    );

        }

    }


    private void addPatient(FMPersistPatient patient) throws ResException {
        FMResultSet resultz;
        ResAdapter adapter = new RPCResAdapter(connection, FMUtil.FM_RPC_NAME);

        FMInsert insert = new FMInsert(adapter);
        insert.setEntry(patient);
        resultz = insert.execute();

        if (resultz == null || resultz.getError() != null) {

            logger.error("Error, unable to insert because " + resultz.getError());
        } else {
            System.out.println("added as IEN " + patient.getIEN());
        }

    }

    public static void main(String[] args) throws NumberFormatException, RPCException, ResException {

        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.ERROR);
        RPCConnection conn = null;
        try {

            args = new String[] { "openvista.medsphere.org", "9201", "OV1234", "OV1234!!"};

            if (args.length < 4) {
                System.err.println("usage: PatientInsertTutorial <host> <port> <ovid-access-code> <ovid-verify-code>");
                return;
            }

            conn = new RPCBrokerConnection(args[0], Integer.parseInt(args[1]), args[2], args[3]);

            if (conn==null) {
                throw new RPCException("unable to connect");
            }

            PatientInsertTutorial repo = new PatientInsertTutorial(conn);

            // first, let's list the current patients before we add anyone....
            repo.printAllPatients();

            // now, add a patient
            // note: executing this multiple times will result in adding duplicate patient
            //       information, each with a different IEN
            System.out.println("======== Adding new patient =======");
            FMPersistPatient patient = new FMPersistPatient();
            patient.setName("ZZZADDINGPATIENT,TEST");
            Calendar cal = GregorianCalendar.getInstance();
            cal.set(2007, Calendar.AUGUST, 20, 0, 0, 0); // can't use time on DOB so set time to 0's
            patient.setDob(cal.getTime());
            patient.setSex("F");
            patient.setMaritalStatus(1); // 1 == the IEN of a valid record in the MARITAL STATUS file (11)
            repo.addPatient(patient);

            // now, print the list again to see our new ZZZADDINGPATIENT,TEST
            repo.printAllPatients();


        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

}