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
package com.medsphere.fmdomain.test;

import gov.va.med.exception.FoundationsException;

import java.util.Date;

import javax.security.auth.login.LoginException;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.medsphere.fileman.FMDelete;
import com.medsphere.fileman.FMDictionary;
import com.medsphere.fileman.FMField;
import com.medsphere.fileman.FMFile;
import com.medsphere.fileman.FMFileInfo;
import com.medsphere.fileman.FMInsert;
import com.medsphere.fileman.FMQueryFiles;
import com.medsphere.fileman.FMQueryIndexes;
import com.medsphere.fileman.FMQueryList;
import com.medsphere.fileman.FMRecord;
import com.medsphere.fileman.FMResultSet;
import com.medsphere.fileman.FMRetrieve;
import com.medsphere.fileman.FMScreenEquals;
import com.medsphere.fileman.FMScreenField;
import com.medsphere.fileman.FMScreenIsNull;
import com.medsphere.fileman.FMScreenValue;
import com.medsphere.fileman.FMUpdate;
import com.medsphere.fileman.GlobalNode;
import com.medsphere.fmdomain.FMDrug;
import com.medsphere.fmdomain.FMDrugSynonym;
import com.medsphere.resource.ResException;
import com.medsphere.rpcresadapter.RPCResAdapter;
import com.medsphere.vistalink.VistaLinkPooledConnectionFactory;
import com.medsphere.vistarpc.RPCException;
import com.medsphere.vistarpc.factory.RPCPooledConnectionFactory;
import com.medsphere.vistarpc.factory.RPCPooledConnection;


/**
 * The following represent the acceptance-level tests for the fmdomain framework.  They are
 * currently implemented with testNG, but could be converted to junit fairly easily as some point
 * in the future.
 *
 * There are several TODO tags scattered throughout that indicate areas that still need
 * attention.
 *
 * To run, inspect the "private final" configuration variables to make sure that they match
 * the target you wish to run against.  Then, either execute the run-acceptance-tests target
 * in the build.xml or run within your IDE (currently eclipse supports testNG) by right-clicking
 * the FMDomainAcceptanceTests.xml file in the base.dir and choosing "Run as testNG TestSuite".
 *
 * Most tests simply pull objects and make sure that values are present.  No checks for actual data
 * are done because the actual data could change from OpenVista server to OpenVista server.  Also,
 * guard against running this against a QA or live datasource.
 *
 * All tests use a log4j logger for output, but logging is effectively turned off in the @BeforeClass
 * so that the tests are not noisy.  If you want to see the actual, printed output of a particular test,
 * then change the logger.debug in that test to logger.info.  If you want to see all output from
 * all tests, change the @BeforeClass [warning: you will get the multitudinous vistalink debug output also].
 *
 * Lastly, if you simply want to keep a test from running, just comment out the @Test annotation that
 * precedes it and it will no longer run.
 */
@SuppressWarnings("unused")
public class FMDomainTestSuite {

    public FMDomainTestSuite() {	}

    private VistaLinkPooledConnectionFactory factory = null;
    private Logger logger = Logger.getLogger(FMDomainTestSuite.class);

    private final String host = "sql.medsphere.com";
    private final String port = "8002";
    private final String accessCode = "VL1234";
    private final String verifyCode = "VL1234!!!";
    private final String fmResourceUser = "MSC FM RESOURCE USER";
    private final String fmFilemanResource = "MSC FILEMAN RESOURCE";

//    private final String host = "localhost";
//    private final String port = "8002";
//    private final String accessCode = "SQ1234";
//    private final String verifyCode = "SQ1234!!";
//    private final String fmResourceUser = "MSC FM RESOURCE USER";
//    private final String fmFilemanResource = "MSC FILEMAN RESOURCE";

    @BeforeClass
    public void setUp() {
        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.WARN);
        logger.setLevel(Level.INFO);

        try {
            factory = new VistaLinkPooledConnectionFactory(host, port, accessCode, verifyCode);
        } catch (LoginException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FoundationsException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


   @AfterClass
    public void tearDown() {
        if (factory != null) {
            factory.emptyPool();
        }
    }

     @Test
    private void testIndexList() throws ResException, RPCException {
        RPCPooledConnection connection = factory.getConnection();
        connection.setContext(fmResourceUser);
        RPCResAdapter adapter = new RPCResAdapter( connection, fmFilemanResource );

        FMQueryIndexes idxQuery = new FMQueryIndexes( adapter, "63.05" );
        FMResultSet results;
        results = idxQuery.execute();
        while (results.next()) {
            FMRecord entry = new FMRecord(results);
            Assert.assertNotNull(entry);
            Assert.assertTrue(entry.getValue("NAME") != null && entry.getValue("NAME").length() > 0);
            logger.debug( entry.getValue("NAME") + " " + entry.getValue("FILE") + " " + entry.getValue("FIELD") );
        }

        connection.returnToPool();
    }


    @Test
    private void testDictionarySubfile() throws ResException, RPCException {
        RPCPooledConnection connection = factory.getConnection();
        connection.setContext(fmResourceUser);
        RPCResAdapter adapter = new RPCResAdapter( connection, fmFilemanResource );
        FMDictionary dictQuery = new FMDictionary( adapter, "63.05" );
        try {
            GlobalNode gn = dictQuery.execute();
            Assert.assertNotNull(gn);
            Assert.assertTrue(gn.getChildCount() > 0);
        } catch (ResException e) {
            e.printStackTrace();
            throw e;
        }
        connection.returnToPool();
    }

    @Test
    private void testDictionaryToplevel() throws ResException, RPCException {
        RPCPooledConnection connection = factory.getConnection();
        connection.setContext(fmResourceUser);
        RPCResAdapter adapter = new RPCResAdapter( connection, fmFilemanResource );
        FMDictionary dictQuery = new FMDictionary( adapter, "63" );
        try {
            GlobalNode gn = dictQuery.execute();
            Assert.assertNotNull(gn);
            Assert.assertTrue(gn.getChildCount() > 0);
        } catch (ResException e) {
            e.printStackTrace();
            throw e;
        }
        connection.returnToPool();
    }

    @Test
    private void testFileList() throws ResException, RPCException {
        RPCPooledConnection connection = factory.getConnection();
        connection.setContext(fmResourceUser);
        RPCResAdapter adapter = new RPCResAdapter( connection, fmFilemanResource );

        FMQueryFiles query = new FMQueryFiles( adapter );

        try {
            FMResultSet results;
            results = query.execute();
            Assert.assertNotNull(results);
            while (results.next()) {
                FMRecord entry = new FMRecord(results);
                Assert.assertNotNull(entry);
                Assert.assertNotNull(entry.getValue("NAME"));
                Assert.assertTrue(entry.getValue("NAME").length() > 0);
                logger.debug( entry.getValue("NAME") + "1" + entry.getValue("NUMBER") + "2" + entry.getValue("PARENT") );
            }
        } catch (ResException e) {
            e.printStackTrace();
            throw e;
        }

        connection.returnToPool();
    }

    @Test
    private void testFileInfo() throws ResException, RPCException {
        RPCPooledConnection connection = factory.getConnection();
        connection.setContext(fmResourceUser);
        RPCResAdapter adapter = new RPCResAdapter( connection, fmFilemanResource );
        FMFileInfo qi = new FMFileInfo( adapter, "1", true );
        try {
            FMFile info = qi.execute();
            Assert.assertNotNull(info);
            Assert.assertNotNull(info.getDescription());
            Assert.assertTrue(info.getDescription().length > 0);
            Assert.assertNotNull(info.getFileName());
            Assert.assertNotNull(info.getFields());
            Assert.assertTrue(info.getFields().size() > 0);
            Assert.assertNotNull(info.getNumber());
        } catch (ResException e) {
            e.printStackTrace();
            throw e;
        }
        connection.returnToPool();
    }

    @Test
    private void testFileInfoForSubfiles() throws ResException, RPCException {
        RPCPooledConnection connection = factory.getConnection();
        connection.setContext(fmResourceUser);
        RPCResAdapter adapter = new RPCResAdapter( connection, fmFilemanResource );
        FMFileInfo qi = new FMFileInfo( adapter, "63.04" );
        try {
            FMFile info = qi.execute();
            Assert.assertNotNull(info);
            Assert.assertNotNull(info.getDescription());
            Assert.assertTrue(info.getDescription().length > 0);
            Assert.assertNotNull(info.getFileName());
            Assert.assertNotNull(info.getFields());
            Assert.assertTrue(info.getFields().size() > 0);
            Assert.assertNotNull(info.getNumber());
        } catch (ResException e) {
            e.printStackTrace();
            throw e;
        }
        connection.returnToPool();
    }

    @Test
    private void testRetrieveSubfileCount() throws ResException, RPCException {
        RPCPooledConnection connection = factory.getConnection();
        connection.setContext(fmResourceUser);
        RPCResAdapter adapter = new RPCResAdapter( connection, fmFilemanResource );

        FMRecord labData = new FMRecord( "63" );
        labData.setIEN( "3" );
        FMRetrieve query = new FMRetrieve( adapter );
        query.setRecord( labData );
        query.addField("5", FMField.FIELDTYPE.SUBFILE);
        try {
            FMResultSet results = query.execute();
            Assert.assertNotNull(results);
        } catch (ResException e) {
            e.printStackTrace();
            throw e;
        }
        // TODO: Figure out a good assertion to make here.
        logger.debug( labData.getIEN() + " has this many subrecords: " + labData.getValue("5") );

        connection.returnToPool();
    }

    @Test
    private void testRetrieveUserInfo() throws ResException, RPCException {
        RPCPooledConnection connection = factory.getConnection();
        connection.setContext(fmResourceUser);
        RPCResAdapter adapter = new RPCResAdapter( connection, fmFilemanResource );

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
            logger.debug( "Primary option: " + newPerson.getValue("201"));
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
                logger.debug( " " + entry.getValue(".01"));
            }
        } catch (ResException e) {
            e.printStackTrace();
            throw e;
        }

        connection.returnToPool();
    }

    @Test
    private void testGetWPField() throws ResException, RPCException {
        RPCPooledConnection connection = factory.getConnection();
        connection.setContext(fmResourceUser);
        RPCResAdapter adapter = new RPCResAdapter( connection, fmFilemanResource );

        FMRecord deltaChecks = new FMRecord( "DELTA CHECKS" );
        deltaChecks.setIEN( "1" );
        FMRetrieve query = new FMRetrieve( adapter );
        query.setRecord( deltaChecks );
        query.addField("DESCRIPTION", FMField.FIELDTYPE.WORD_PROCESSING);

        try {
            FMResultSet results = query.execute();
            Assert.assertNotNull(results);
        } catch (ResException e) {
            e.printStackTrace();
            throw e;
        }
        Object wpfield = deltaChecks.getObject("DESCRIPTION");
        Assert.assertTrue(deltaChecks.getIEN().length() > 0);
        logger.debug( "testGetWPField: " + deltaChecks.getIEN() + " " + deltaChecks.getValue("DESCRIPTION") );

        connection.returnToPool();
    }

    @Test
    private void testRetrieveVP() throws ResException, RPCException {
        RPCPooledConnection connection = factory.getConnection();
        connection.setContext(fmResourceUser);
        RPCResAdapter adapter = new RPCResAdapter( connection, fmFilemanResource );

        FMRecord order = new FMRecord( "ORDER" );
        order.setIEN( "1" );
        FMRetrieve query = new FMRetrieve( adapter );
        query.setRecord( order );
        query.addField("DIALOG", FMField.FIELDTYPE.VARIABLE_POINTER);

        try {
            FMResultSet results = query.execute();
        } catch (ResException e) {
            e.printStackTrace();
            throw e;
        }
        Object vpfield = order.getObject("DIALOG");
        //TODO: Seems that vpfield is often null....
        Assert.assertNotNull(order.getIEN());
        logger.debug( "testRetrieveVP: " +  order.getIEN() + " " + order.getValue("DIALOG") );

        connection.returnToPool();
    }

    // TODO: Jeff, can you look at this one?  It errors out because of problems with DrugSynonymField...
    //@Test
    private void testRetrieve() throws ResException, RPCException {
        RPCPooledConnection connection = factory.getConnection();
        connection.setContext(fmResourceUser);
        RPCResAdapter adapter = new RPCResAdapter( connection, fmFilemanResource );

        FMResultSet results;
        FMDrug drug = new FMDrug();
        drug.setIEN( "2" );
        FMRetrieve query = new FMRetrieve( adapter );
        query.setRecord( drug );
        query.addField("MESSAGE", FMField.FIELDTYPE.FREE_TEXT);
        try {
            results = query.execute();
        } catch (ResException e) {
            e.printStackTrace();
            throw e;
        }
        logger.debug( "testRetrieve: " + drug.getIEN() + " " + drug.getValue("MESSAGE") + " " + drug.getValue(".01") );

        connection.returnToPool();
    }

    // TODO: Synonym problems here too
    //@Test
    private void testRetrieveOneField() throws ResException, RPCException {
        RPCPooledConnection connection = factory.getConnection();
        connection.setContext(fmResourceUser);
        RPCResAdapter adapter = new RPCResAdapter( connection, fmFilemanResource );

        FMResultSet results;
        FMDrug drug = new FMDrug();
        drug.setIEN( "2" );
        FMRetrieve query = new FMRetrieve( adapter );
        query.setRecord( drug );
        try {
            results = query.execute();
            query = new FMRetrieve( adapter );
            query.setRecord( drug );
            query.removeAllFields();
            query.clearOldValues( false );
            query.addField("MESSAGE", FMField.FIELDTYPE.FREE_TEXT);
            results = query.execute();
            logger.debug( "testRetrieveOneField: " + drug.getIEN() + " " + drug.getValue("MESSAGE") + " " + drug.getValue(".01") );
        } catch (ResException e) {
            e.printStackTrace();
            throw e;
        }
        connection.returnToPool();
    }

    @Test
    private void testRetrieveNondomain() throws ResException, RPCException {
        RPCPooledConnection connection = factory.getConnection();
        connection.setContext(fmResourceUser);
        RPCResAdapter adapter = new RPCResAdapter( connection, fmFilemanResource );

        FMResultSet results;
        FMRecord drug = new FMRecord("DRUG");
        drug.setIEN( "2" );
        FMRetrieve query = new FMRetrieve( adapter );
        query.setRecord( drug );
        query.addField("VA CLASSIFICATION", FMField.FIELDTYPE.FREE_TEXT);
        try {
            results = query.execute();
        } catch (ResException e) {
            e.printStackTrace();
            throw e;
        }
        Assert.assertNotNull(drug.getIEN());
        Assert.assertTrue(drug.getValue("VA CLASSIFICATION").length() > 0);
        logger.debug( "testRetrieveNondomain: " + drug.getIEN() + " " + drug.getValue("VA CLASSIFICATION") );

        connection.returnToPool();
    }

    //TODO: Getting some error here.
    //@Test
    private void testListSubfiles() throws ResException, RPCException {
        RPCPooledConnection connection = factory.getConnection();
        connection.setContext(fmResourceUser);
        RPCResAdapter adapter = new RPCResAdapter( connection, fmFilemanResource );

        FMDrug drug = new FMDrug();
        drug.setIEN( "1" );
        FMDrugSynonym synonymField = drug.getSynonym();
        try {
            FMQueryList query = new FMQueryList( adapter, synonymField.getFile() );
            FMResultSet results = query.execute();
            int i = 0;
            while (results.next()) {
                FMRecord entry = new FMRecord(results);
                //System.out.print( ++i );
                Assert.assertNotNull(entry);
                //TODO: Synonym doesn't seem to be picked out...
                logger.debug( " " + entry.getValue("SYNONYM") + " " +  entry.getValue("INTENDED USE") );
                //System.out.println( " " + entry.getValue(".01") + " " +  entry.getValue("1") );
            }
        } catch (ResException e) {
            e.printStackTrace();
            throw e;
        }
        connection.returnToPool();
    }

    @Test
    private void testListSubfilesNondomain() throws ResException, RPCException {
        RPCPooledConnection connection = factory.getConnection();
        connection.setContext(fmResourceUser);
        RPCResAdapter adapter = new RPCResAdapter( connection, fmFilemanResource );

        FMRecord parentEntry = new FMRecord( "OPTION" );
        parentEntry.setIEN( "2" );

        FMFile menuFile = new FMFile("MENU");
        menuFile.addField( "SYNONYM" );
        menuFile.addField( "ITEM", FMField.FIELDTYPE.POINTER_TO_FILE );
        menuFile.setParentRecord( parentEntry );
        try {
            FMQueryList query = new FMQueryList( adapter, menuFile );
            FMResultSet results = query.execute();
            while (results.next()) {
                FMRecord entry = new FMRecord(results);
                Assert.assertTrue(entry.getValue("ITEM").length() > 0);
                logger.debug( entry.getValue("ITEM") + "  " + entry.getValue("SYNONYM") );
            }

        } catch (ResException e) {
            e.printStackTrace();
            throw e;
        }
        connection.returnToPool();
    }

    @Test
    private void testInsertSubfiles() throws ResException, RPCException {
        RPCPooledConnection connection = factory.getConnection();
        connection.setContext(fmResourceUser);
        RPCResAdapter adapter = new RPCResAdapter( connection, fmFilemanResource );

        FMDrug drug = new FMDrug();
        drug.setIEN( "2" );
        FMDrugSynonym synonym = new FMDrugSynonym();
        synonym.setParent( drug );
        synonym.setNDCCode( "NDC 1" );
        synonym.setSynonym( "Syn 1" );
        FMInsert insert = new FMInsert( adapter );
        insert.setEntry( synonym );
        try {
            FMResultSet results = insert.execute();
            Assert.assertNotNull(results);
        } catch (ResException e) {
            e.printStackTrace();
            throw e;
        }
        connection.returnToPool();
    }

    @Test
    private void testInsertSubfilesNondomain() throws ResException, RPCException {
        RPCPooledConnection connection = factory.getConnection();
        connection.setContext(fmResourceUser);
        RPCResAdapter adapter = new RPCResAdapter( connection, fmFilemanResource );

        FMRecord drug = new FMRecord("DRUG");
        drug.setIEN( "2" );
        FMRecord synonym = new FMRecord("SYNONYM");
        synonym.setParent( drug );
        synonym.setValue("SYNONYM", "NONDOMAIN INSERT");
        FMInsert insert = new FMInsert( adapter );
        insert.setEntry( synonym );
        try {
            FMResultSet results = insert.execute();
            Assert.assertNotNull(results);
        } catch (ResException e) {
            e.printStackTrace();
            throw e;
        }
        connection.returnToPool();
    }

    @Test
    private void testUpdateDrugInvalid() throws ResException, RPCException {
        RPCPooledConnection connection = factory.getConnection();
        connection.setContext(fmResourceUser);
        RPCResAdapter adapter = new RPCResAdapter( connection, fmFilemanResource );

        FMUpdate update = new FMUpdate( adapter );
        FMDrug drug = new FMDrug();
        drug.setIEN( "17" );
        drug.setOrderUnit( 3 );
        update.setEntry( drug );
        try {
            FMResultSet results = update.execute();
            Assert.assertNotNull(results);
        } catch (ResException e) {
            e.printStackTrace();
            throw e;
        }
        connection.returnToPool();
    }

    @Test
    private void testUpdateDrug() throws ResException, RPCException {
        RPCPooledConnection connection = factory.getConnection();
        connection.setContext(fmResourceUser);
        RPCResAdapter adapter = new RPCResAdapter( connection, fmFilemanResource );

        FMUpdate update = new FMUpdate( adapter );
        FMDrug drug = new FMDrug();
        drug.setIEN( "4804" );
        drug.setValue("MESSAGE", "This is message #4 for 4804");
        drug.setInactiveDate( new Date() );
        update.setEntry( drug );
        try {
            FMResultSet results = update.execute();
            Assert.assertNotNull(results);
        } catch (ResException e) {
            e.printStackTrace();
            throw e;
        }
        connection.returnToPool();
    }

    @Test
    private void testUpdateNondomain() throws ResException, RPCException {
        RPCPooledConnection connection = factory.getConnection();
        connection.setContext(fmResourceUser);
        RPCResAdapter adapter = new RPCResAdapter( connection, fmFilemanResource );

        FMUpdate update = new FMUpdate( adapter );
        FMRecord drug = new FMRecord("50");
        drug.setIEN( "5504" );
        drug.setValue("GENERIC NAME", "CHANGED NAME 5");
        update.setEntry( drug );
        try {
            FMResultSet results = update.execute();
            Assert.assertNotNull(results);
        } catch (ResException e) {
            e.printStackTrace();
            throw e;
        }
        connection.returnToPool();
    }

    // TODO: I see no pharmacy orderable item in the Drug class...
    //@Test
    private void testUpdatePointer() throws ResException, RPCException {
        RPCPooledConnection connection = factory.getConnection();
        connection.setContext(fmResourceUser);
        RPCResAdapter adapter = new RPCResAdapter( connection, fmFilemanResource );

        FMDrug drug = new FMDrug();
        drug.setIEN( "2" );
        FMRetrieve retrieveQuery = new FMRetrieve( adapter );
        retrieveQuery.setRecord( drug );
        retrieveQuery.addField("PHARMACY ORDERABLE ITEM", FMField.FIELDTYPE.FREE_TEXT);
        try {
            retrieveQuery.execute();
            if (drug.getValue("PHARMACY ORDERABLE ITEM").equals("2")) {
                drug.setValue("PHARMACY ORDERABLE ITEM", "20");
            } else {
                drug.setValue("PHARMACY ORDERABLE ITEM", "2");
            }

            FMUpdate update = new FMUpdate( adapter );
            update.setEntry( drug );
            FMResultSet results = update.execute();
        } catch (ResException e) {
            e.printStackTrace();
            throw e;
        }
        connection.returnToPool();
    }

    @Test
    private void testInsertThenDeleteDrug() throws ResException, RPCException {
        RPCPooledConnection connection = factory.getConnection();
        connection.setContext(fmResourceUser);
        RPCResAdapter adapter = new RPCResAdapter( connection, fmFilemanResource );

        FMDrug drug = new FMDrug();
        drug.setGenericName( "JDA DRUG4" );
        drug.setValue("MESSAGE", "This is a message");
        //drug.setValue("PHARMACY ORDERABLE ITEM", "20");

        FMInsert insert = new FMInsert( adapter );
        insert.setEntry( drug );
        try {
            FMResultSet results = insert.execute();
            Assert.assertNotNull(results);
        } catch (ResException e) {
            e.printStackTrace();
            throw e;
        }

        // now delete it.
        FMDelete delete = new FMDelete( adapter );
        if (drug==null) {
            drug = new FMDrug();
            drug.setIEN("5503");
        }

        delete.setEntry( drug );
        try {
            FMResultSet results = delete.execute();
            Assert.assertNotNull(results);
        } catch (ResException e) {

            e.printStackTrace();
        }

        connection.returnToPool();

    }

    // TODO: not working...
    //@Test
    private void testUpdateSubfile( ) throws ResException, RPCException {
        RPCPooledConnection connection = factory.getConnection();
        connection.setContext(fmResourceUser);
        RPCResAdapter adapter = new RPCResAdapter( connection, fmFilemanResource );

        FMUpdate update = new FMUpdate( adapter );
        FMDrug drug = new FMDrug();
        drug.setIEN( "2" );
        FMDrugSynonym synonym = new FMDrugSynonym();
        synonym.setParent( drug );
        synonym.setIEN("2");
        synonym.setSynonym( "Update 1A" );
        update.setEntry( synonym );
        try {
            FMResultSet results = update.execute();
        } catch (ResException e) {
            e.printStackTrace();
            throw e;
        }
        connection.returnToPool();
    }


    @Test
    private void testUpdateSubfileNondomain() throws ResException, RPCException {
        RPCPooledConnection connection = factory.getConnection();
        connection.setContext(fmResourceUser);
        RPCResAdapter adapter = new RPCResAdapter( connection, fmFilemanResource );

        FMUpdate update = new FMUpdate( adapter );
        FMRecord drug = new FMRecord("DRUG");
        drug.setIEN( "2" );
        FMRecord synonym = new FMRecord("SYNONYM");
        synonym.setParent( drug );
        synonym.setIEN("2");
        synonym.setValue( "SYNONYM", "Update 2A" );
        update.setEntry( synonym );
        try {
            FMResultSet results = update.execute();
            logger.debug("testUpdateSubfileNondomain: " + results.getValue("SYNONYM"));
        } catch (ResException e) {
            e.printStackTrace();
            throw e;
        }
        connection.returnToPool();
    }

    @Test
    private void testListNondomain() throws ResException, RPCException {
        FMFile optionsInfo = new FMFile("OPTION"); // options, called either OPTION or OPTION_MSC
        optionsInfo.addField( "UPPERCASE MENU TEXT" ); // uppercase menu text
        FMQueryList query;
        FMResultSet results;
        RPCPooledConnection connection = factory.getConnection();
        connection.setContext(fmResourceUser);
        RPCResAdapter adapter = new RPCResAdapter( connection, fmFilemanResource );

        query = new FMQueryList( adapter, optionsInfo );
        try {
            results = query.execute();
            while (results.next()) {
                FMRecord entry = new FMRecord(results);
                Assert.assertTrue(entry.getValue("UPPERCASE MENU TEXT").length() > 0);
                logger.debug( entry.getValue("UPPERCASE MENU TEXT") );
            }
        } catch (ResException e) {
            e.printStackTrace();
            throw e;
        }
        connection.returnToPool();
    }

    // TODO: This one is broken and I think it should work.
    //@Test
    private void testListNondomainWithScreen() throws ResException, RPCException {
        FMFile userInfo = new FMFile("200");
        userInfo.addField( ".01" );
        FMQueryList query;
        FMResultSet results;
        RPCPooledConnection connection = factory.getConnection();
        connection.setContext(fmResourceUser);
        RPCResAdapter adapter = new RPCResAdapter( connection, fmFilemanResource );

        query = new FMQueryList( adapter, userInfo );
        query.setScreen(new FMScreenEquals(new FMScreenField("INITIAL"), new FMScreenValue("PB")));
        try {
            results = query.execute();
            while (results.next()) {
                FMRecord entry = new FMRecord(results);
                Assert.assertTrue(entry.getValue(".01").length() > 0);
                logger.debug("testListNondomainWithScreen: " +  entry.getValue(".01") );
            }
        } catch (ResException e) {
            e.printStackTrace();
            throw e;
        }
        connection.returnToPool();
    }

    @Test
    private void testListMultiple() throws ResException, RPCException {
        FMFile optionsInfo = new FMFile("200");
        optionsInfo.addField( ".01" );
        optionsInfo.addField( "747.27" );
        optionsInfo.addField( "9" );
        FMQueryList query;
        FMResultSet results;
        RPCPooledConnection connection = factory.getConnection();
        connection.setContext(fmResourceUser);
        RPCResAdapter adapter = new RPCResAdapter( connection, fmFilemanResource );

        query = new FMQueryList( adapter, optionsInfo );
        try {
            results = query.execute();
            while (results.next()) {
                FMRecord entry = new FMRecord(results);
                Assert.assertNotNull(entry);
                Assert.assertNotNull(entry.getValue(".01"));
                logger.debug( "testListMultiple:" + entry.getValue(".01")+ entry.getValue("747.27") + entry.getValue("9") );
            }
        } catch (ResException e) {
            e.printStackTrace();
            throw e;
        }
        connection.returnToPool();
    }

    @Test
    private void testListNondomainWithWP() throws ResException, RPCException {
        FMFile optionsInfo = new FMFile("DELTA CHECKS");
        optionsInfo.addField( "NAME" );
        optionsInfo.addField( "DESCRIPTION", FMField.FIELDTYPE.WORD_PROCESSING );
        FMQueryList query;
        FMResultSet results;
        RPCPooledConnection connection = factory.getConnection();
        connection.setContext(fmResourceUser);
        RPCResAdapter adapter = new RPCResAdapter( connection, fmFilemanResource );

        query = new FMQueryList( adapter, optionsInfo );
        try {
            results = query.execute();
            while (results.next()) {
                FMRecord entry = new FMRecord(results);
                Assert.assertNotNull(entry);
                Assert.assertNotNull(entry.getValue("NAME"));
                logger.debug( entry.getValue("NAME") + " " + entry.getValue("DESCRIPTION") );
            }
        } catch (ResException e) {
            e.printStackTrace();
            throw e;
        }
        connection.returnToPool();
    }

    @Test
    private void testListNondomainWithVP() throws ResException, RPCException {
        RPCPooledConnection connection = factory.getConnection();
        connection.setContext(fmResourceUser);
        RPCResAdapter adapter = new RPCResAdapter( connection, fmFilemanResource );

        FMFile orderInfo = new FMFile("ORDER");
        orderInfo.addField( "DIALOG", FMField.FIELDTYPE.VARIABLE_POINTER );
        orderInfo.addField( "PACKAGE REFERENCE" );

        FMQueryList query = new FMQueryList( adapter, orderInfo );
        FMResultSet results;
        try {
            results = query.execute();
            if (results!=null) {
                while (results.next()) {
                    FMRecord entry = new FMRecord(results);
                    Assert.assertNotNull(entry);
                    Assert.assertTrue(entry.getValue("PACKAGE REFERENCE") != null || entry.getValue("DIALOG") != null);
                    logger.debug( entry.getValue("PACKAGE REFERENCE") + " " + entry.getValue("DIALOG") );
                }
            }
        } catch (ResException e) {
            e.printStackTrace();
            throw e;
        }

        connection.returnToPool();
    }

    @Test
    private void testListDomain() throws ResException, RPCException {
        RPCPooledConnection connection = factory.getConnection();
        connection.setContext(fmResourceUser);
        RPCResAdapter adapter = new RPCResAdapter( connection, fmFilemanResource );

        FMQueryList query;
        FMResultSet results;
        query = new FMQueryList( adapter, FMDrug.getFileInfoForClass() );

        // modify query
        query.setScreen( new FMScreenIsNull("100", false) );
        //query.removeField( "100" ); // remove inactive date from query results, since we're screening it

        query.addField("MESSAGE", FMField.FIELDTYPE.FREE_TEXT);
        try {
            results = query.execute();
            while (results.next()) {
                FMDrug drug = new FMDrug( results );
                Assert.assertNotNull(drug);
                Assert.assertNotNull(drug.getIEN());
                Assert.assertNotNull(drug.getValue("100"));
                logger.debug( "testListDomain: " + drug.getIEN() + " " + drug.getValue("MESSAGE") + " " + drug.getValue("100") );
            }
        } catch (ResException e) {
            e.printStackTrace();
            throw e;
        }
        connection.returnToPool();
    }

    @Test
    private void testShowDrugMessages() throws ResException, RPCException {
        RPCPooledConnection connection = factory.getConnection();
        connection.setContext(fmResourceUser);
        RPCResAdapter adapter = new RPCResAdapter( connection, fmFilemanResource );

        FMQueryList query;
        FMResultSet results;
        query = new FMQueryList( adapter, FMDrug.getFileInfoForClass() );

        // modify query
        query.setScreen( new FMScreenIsNull("MESSAGE", false) );

        try {
            results = query.execute();
            int total;
            while (results.next()) {
                FMDrug drug = new FMDrug( results );
                Assert.assertNotNull(drug);
                Assert.assertNotNull(drug.getGenericName());
                logger.debug( "testShowDrugMessages: " + drug.getGenericName() );
            }
        } catch (ResException e) {
            e.printStackTrace();
            throw e;
        }
        connection.returnToPool();
    }


    // TODO: Reimplement these as tests that can safely delete data without horking the database.
    private void testDeleteSubfile(RPCPooledConnectionFactory factory ) throws RPCException {
        RPCPooledConnection connection = factory.getConnection();
        connection.setContext(fmResourceUser);
        RPCResAdapter adapter = new RPCResAdapter( connection, "MSC FILEMAN RESOURCE" );
        FMDelete delete = new FMDelete( adapter );
        FMDrug drug = new FMDrug();
        drug.setIEN( "2" );
        FMDrugSynonym synonym = new FMDrugSynonym();
        synonym.setParent( drug );
        synonym.setIEN("10");
        delete.setEntry( synonym );
        try {
            FMResultSet results = delete.execute();
        } catch (ResException e) {

            e.printStackTrace();
        }
        connection.returnToPool();
    }

    private void testDeleteSubfileNondomain(RPCPooledConnectionFactory factory ) throws RPCException {
        RPCPooledConnection connection = factory.getConnection();
        connection.setContext(fmResourceUser);
        RPCResAdapter adapter = new RPCResAdapter( connection, "MSC FILEMAN RESOURCE" );
        FMDelete delete = new FMDelete( adapter );
        FMRecord parentEntry = new FMRecord("OPTION");
        parentEntry.setIEN( "2" );
        FMRecord synonym = new FMRecord("MENU");
        synonym.setParent( parentEntry );
        synonym.setIEN("9");
        delete.setEntry( synonym );
        try {
            FMResultSet results = delete.execute();
        } catch (ResException e) {

            e.printStackTrace();
        }
        connection.returnToPool();
    }

}
