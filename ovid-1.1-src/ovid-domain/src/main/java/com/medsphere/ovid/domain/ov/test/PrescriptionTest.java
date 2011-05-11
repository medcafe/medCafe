// <editor-fold defaultstate="collapsed">
/*
 * Copyright (C) 2011  Medsphere Systems Corporation
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
package com.medsphere.ovid.domain.ov.test;

import org.testng.Assert;

import com.medsphere.cia.CIABrokerConnection;
import com.medsphere.common.util.TimeKeeperDelegate;
import com.medsphere.fileman.FMField;
import com.medsphere.fileman.FMFile;
import com.medsphere.fileman.FMQueryList;
import com.medsphere.fileman.FMRecord;
import com.medsphere.fileman.FMResultSet;
import com.medsphere.fileman.FMRetrieve;
import com.medsphere.ovid.domain.ov.OvidDomainException;
import com.medsphere.ovid.domain.ov.OvidSecureRepository;
import com.medsphere.resource.ResAdapter;
import com.medsphere.resource.ResException;
import com.medsphere.vistarpc.RPCConnection;
import com.medsphere.vistarpc.RPCException;

public class PrescriptionTest extends OvidSecureRepository {

    public PrescriptionTest(RPCConnection serverConnection) {
        super(null, serverConnection);
    }

    public void getRefillInfo() throws OvidDomainException, ResException {
        ResAdapter adapter = obtainServerRPCAdapter();

        // First, retrieve a single record with a known IEN
        FMRecord prescription = new FMRecord( "52" );
        prescription.setIEN( "145" );
        FMRetrieve query = new FMRetrieve( adapter );
        query.setRecord( prescription );
        query.addField("2", FMField.FIELDTYPE.POINTER_TO_FILE);
        query.setInternal( false ); // we want the text of .01 field it points to, not the IEN
        try {
            query.execute();
            // Our new person record is now populated
            Assert.assertNotNull(query);
            Assert.assertNotNull(prescription.getValue("2"));
            Assert.assertTrue(prescription.getValue("2").length() > 0);
            System.out.println( "Name: " + prescription.getValue("2"));
        } catch (ResException e) {
            e.printStackTrace();
            throw e;
        }

        // Now, list the subfiles for secondary options
        FMFile secondarySubfile = new FMFile("52"); // 52 is the field number for refills
        secondarySubfile.addField(".01"); // Resolve internal, get the name
        secondarySubfile.setParentRecord(prescription); // So the query knows which set of subfile records
        try {
            FMQueryList secondaryQuery = new FMQueryList( adapter, secondarySubfile );
            FMResultSet results = secondaryQuery.execute();
            Assert.assertNotNull(results);

            while (results.next()) {
                FMRecord entry = new FMRecord(results);
                Assert.assertNotNull(entry);
                Assert.assertNotNull(entry.getValue(".01"));
                System.out.println( " " + entry.getValue(".01"));
            }
        } catch (ResException e) {
            e.printStackTrace();
            throw e;
        }

    }

    public void getOptionInfo() throws OvidDomainException, ResException {
        ResAdapter adapter = obtainServerRPCAdapter();

        // First, retrieve a single record with a known IEN
        FMRecord newPerson = new FMRecord( "200" );
        newPerson.setIEN( "11" );
        FMRetrieve query = new FMRetrieve( adapter );
        query.setRecord( newPerson );
        query.addField("201", FMField.FIELDTYPE.POINTER_TO_FILE);
        query.setInternal( false ); // we want the text of .01 field it points to, not the IEN
        try {
            query.execute();
            // Our new person record is now populated
            Assert.assertNotNull(query);
            Assert.assertNotNull(newPerson.getValue("201"));
            Assert.assertTrue(newPerson.getValue("201").length() > 0);
            System.out.println( "Primary option: " + newPerson.getValue("201"));
        } catch (ResException e) {
            e.printStackTrace();
            throw e;
        }

        // Now, list the subfiles for secondary options
        FMFile secondarySubfile = new FMFile("203"); // 203 is the field number for 2ndary menu
        secondarySubfile.addField(".01").setInternal(false); // Resolve internal, get the name
        secondarySubfile.setParentRecord(newPerson); // So the query knows which set of subfile records
        try {
            FMQueryList secondaryQuery = new FMQueryList( adapter, secondarySubfile );
            FMResultSet results = secondaryQuery.execute();
            Assert.assertNotNull(results);
            // TODO: Figure out why there are no secondary entries...
            while (results.next()) {
                FMRecord entry = new FMRecord(results);
                Assert.assertNotNull(entry);
                Assert.assertNotNull(entry.getValue(".01"));
                System.out.println( " " + entry.getValue(".01"));
            }
        } catch (ResException e) {
            e.printStackTrace();
            throw e;
        }

    }
    public static void main(String[] args) throws OvidDomainException, NumberFormatException, RPCException, ResException {
        RPCConnection conn = null;
        RPCConnection serverConn = null;
        //BasicConfigurator.configure();
        //org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.WARN);
        //TimeKeeperDelegate.setLogLevel(Level.DEBUG);

        try {
            TimeKeeperDelegate.start("PrescriptionTest");
            args = new String[] { "localhost", "9200", "AD1234", "AD1234!!"};
            if (args.length < 4) {
                System.err.println("usage: PatientList <host> <port> <access-code> <verify-code>");
                return;
            }


            conn = new CIABrokerConnection(args[0], new Integer(args[1]), args[2], args[3], null, "KMREHR");
            serverConn = new CIABrokerConnection(args[0], new Integer(args[1]), "OV1234", "OV1234!!", null, "KMREHR");

            PrescriptionTest repo = new PrescriptionTest(serverConn);
            repo.getRefillInfo();

        } finally {
            TimeKeeperDelegate.stop("PrescriptionTest");
            System.out.println("Timer: " + TimeKeeperDelegate.getDisplayString("PrescriptionTest"));
            try {
                if (conn != null) {
                    conn.close();
                }
                if (serverConn != null) {
                    serverConn.close();
                }
            } catch (RPCException e) {}
            System.exit(0);
        }
    }
}
