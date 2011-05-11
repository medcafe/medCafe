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

package com.medsphere.ovid.tutorial.fm;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.medsphere.fileman.FMField;
import com.medsphere.fileman.FMFile;
import com.medsphere.fileman.FMQueryList;
import com.medsphere.fileman.FMResultSet;
import com.medsphere.fileman.FMRetrieve;
import com.medsphere.fileman.FMScreen;
import com.medsphere.fileman.FMScreenAnd;
import com.medsphere.fileman.FMScreenEquals;
import com.medsphere.fileman.FMScreenField;
import com.medsphere.fileman.FMScreenGreaterThan;
import com.medsphere.fileman.FMScreenIEN;
import com.medsphere.fileman.FMScreenOr;
import com.medsphere.fileman.FMScreenValue;
import com.medsphere.fileman.FMUtil;
import com.medsphere.ovid.domain.ov.OvidDomainException;
import com.medsphere.resource.ResAdapter;
import com.medsphere.resource.ResException;
import com.medsphere.rpcresadapter.RPCResAdapter;
import com.medsphere.vistarpc.RPCBrokerConnection;
import com.medsphere.vistarpc.RPCConnection;
import com.medsphere.vistarpc.RPCException;

public class InstallRepositoryTutorial {

    private RPCConnection connection = null;

    public InstallRepositoryTutorial(RPCConnection connection) {
        this.connection = connection;
    }

    // this is for tutorial purposes and demonstrates an ad hoc approach for
    // making a call to fileman.
    public void printReport() throws OvidDomainException {

        try {
            // This is a specialized context that only a FM level user should be able to execute
            connection.setContext(FMUtil.FM_RPC_CONTEXT);
            // Create a resource adapater for the connection and RPC.  FM_RPC_NAME resolves to a
            // specialized RPC (installed with the OVID KIDs build) that knows how to execute FileMan
            // calls.  A "resource" is basically a message that defines the inputs/outputs of the RPC
            // we are calling.  The adapter is the "go between" that facilitates the message.
            ResAdapter adapter = new RPCResAdapter(connection, FMUtil.FM_RPC_NAME);

            FMResultSet results;
            // We could have given the numeric "9.7" as the FMFile target.
            // FMFile is in the fileman.jar
            FMFile install = new FMFile("INSTALL");
            // setup to run a fileman list query.
            FMQueryList query = new FMQueryList(adapter, install);

            // we need to include the fields we are interested in
            query.addField("NAME", FMField.FIELDTYPE.FREE_TEXT);
            query.addField("STATUS", FMField.FIELDTYPE.SET_OF_CODES);
            query.addField("PACKAGE FILE LINK", FMField.FIELDTYPE.POINTER_TO_FILE);
            query.addField("FILE COMMENT", FMField.FIELDTYPE.FREE_TEXT);
            query.addField("INSTALLED BY", FMField.FIELDTYPE.POINTER_TO_FILE);
            query.addField("INSTALL COMPLETE TIME", FMField.FIELDTYPE.DATE);
            // comment the following field to get a user name instead of number
            //query.getField("INSTALLED BY").setInternal(false);

            // this actually transmits our resource to RPC/VistaLink Broker and gleans the results.
            results = query.execute();
            while (results.next()) {
                System.out.println("name=["+results.getValue("NAME") + "]"
                                   + " status=[" + results.getValue("STATUS") + "]"
                                   + " package file link=[" + results.getValue("PACKAGE FILE LINK") + "]"
                                   + " file comment=[" + results.getValue("FILE COMMENT") + "]"
                                   + " installed by=[" + results.getValue("INSTALLED BY") + "]"
                                   + " install complete time=[" + results.getValue("INSTALL COMPLETE TIME") + "]"
                                   + " install complete time=[" + FMUtil.fmDateToDate(results.getValue("INSTALL COMPLETE TIME")) + "]"
                                   );
            }
        } catch (RPCException e) {
            throw new OvidDomainException(e);
        } catch (ResException e) {
            throw new OvidDomainException(e);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // this is for tutorial purposes and demonstrates the use of an FM domain class to collect
    // INSTALL records
    public Collection<FMInstall> getAllInstallRecords() throws OvidDomainException {
        Collection<FMInstall> list = new ArrayList<FMInstall>();

        try {
            // This is a specialized context that only a FM level user should be able to execute
            connection.setContext(FMUtil.FM_RPC_CONTEXT);
            // Create a resource adapater for the connection and RPC.  FM_RPC_NAME resolves to a
            // specialized RPC (installed with the OVID KIDs build) that knows how to execute FileMan
            // calls.  A "resource" is basically a message that defines the inputs/outputs of the RPC
            // we are calling.  The adapter is the "go between" that facilitates the message.
            ResAdapter adapter = new RPCResAdapter(connection, FMUtil.FM_RPC_NAME);

            FMResultSet results;

            // setup to run a fileman list query.
            FMQueryList query = new FMQueryList(adapter, FMInstall.getFileInfoForClass());
            query.getField("INSTALLED BY").setInternal(false);

            // this actually transmits our resource to RPC/VistaLink Broker and gleans the results.
            results = query.execute();
            while (results.next()) {
                list.add(new FMInstall(results));
            }
        } catch (RPCException e) {
            throw new OvidDomainException(e);
        } catch (ResException e) {
            throw new OvidDomainException(e);
        }

        return list;
    }

    // this method will return a list of all FMInstall records between the start and end date specified.
    public Collection<FMInstall> getInstallRecordsBetweenDates(Date startDate, Date endDate) throws OvidDomainException {
        Collection<FMInstall> list = new ArrayList<FMInstall>();

        try {
            // This is a specialized context that only a FM level user should be able to execute
            connection.setContext(FMUtil.FM_RPC_CONTEXT);
            // Create a resource adapater for the connection and RPC.  FM_RPC_NAME resolves to a
            // specialized RPC (installed with the OVID KIDs build) that knows how to execute FileMan
            // calls.  A "resource" is basically a message that defines the inputs/outputs of the RPC
            // we are calling.  The adapter is the "go between" that facilitates the message.
            ResAdapter adapter = new RPCResAdapter(connection, FMUtil.FM_RPC_NAME);

            FMResultSet results;

            // setup to run a fileman list query
            // use screens to simulate greater than or equal to the start date
            // and less than or equal to the end date.  There currently isn't an FMScreenLessThan,
            // so we'll just swap the parameters to simulate (e.g. 1 < 2 is the same as 2 > 1 for this purpose)
            FMQueryList query = new FMQueryList(adapter, FMInstall.getFileInfoForClass());
            FMScreen afterDateScreen = new FMScreenGreaterThan(new FMScreenField("INSTALL COMPLETE TIME"),
                                                               new FMScreenValue(FMUtil.dateToFMDate(startDate)));
            FMScreen beforeDateScreen = new FMScreenGreaterThan(new FMScreenValue(FMUtil.dateToFMDate(endDate)), new FMScreenField("INSTALL COMPLETE TIME"));

            FMScreen afterOrEqualDateScreen = new FMScreenOr(new FMScreenEquals(new FMScreenField("INSTALL COMPLETE TIME"),
                                                                                new FMScreenValue(FMUtil.dateToFMDate(startDate))),
                                                             afterDateScreen);
            FMScreen beforeOrEqualDateScreen = new FMScreenOr(new FMScreenEquals(new FMScreenField("INSTALL COMPLETE TIME"),
                                                                                 new FMScreenValue(FMUtil.dateToFMDate(endDate))),
                                                             beforeDateScreen);

            query.setScreen(new FMScreenAnd(beforeOrEqualDateScreen, afterOrEqualDateScreen));

            query.getField("INSTALLED BY").setInternal(false);

            // this actually transmits our resource to RPC/VistaLink Broker and gleans the results.
            results = query.execute();
            while (results.next()) {
                list.add(new FMInstall(results));
            }

        } catch (RPCException e) {
            throw new OvidDomainException(e);
        } catch (ResException e) {
            throw new OvidDomainException(e);
        }

        return list;
    }

    // the goal of this method is to take a list of FMInstalls and create a distinct list of
    // package file link IENs.  This list is fed to the getPackages() method which will
    // FMScreenOr them all together and collect them all in one call.  Then, we can go through
    // the original list and set the proper FMPackage to its FMInstall parent.
    public void findPackageFilesForInstall(Collection<FMInstall> list) throws OvidDomainException {
        Collection<Integer> ienList = new ArrayList<Integer>();

        // first, build a list of distinct IENs and get packages for each
        for (FMInstall item : list) {
            if (item.getPackageFileLink() != null) {
                if (!ienList.contains(item.getPackageFileLink())) {
                    ienList.add(item.getPackageFileLink());
                }
            }
        }

        Collection<FMPackage> packages = getPackages(ienList);

        for (FMInstall item : list) {
            if (item.getPackageFileLink() != null) {
               for (FMPackage packageFile : packages) {
                    if (packageFile.getIEN().equals(item.getPackageFileLink().toString())) {
                        item.setPackageFile(packageFile);
                        break;
                    }
                }
            }
        }
    }

    // Uses FMScreenOr to build a list of package file link IENs and sends that as a fileman
    // query.  Note that screens can't be Or'd together one after the other.  This is how
    // to chain screens together for form complex queries.
    public Collection<FMPackage> getPackages(Collection<Integer> iens) throws OvidDomainException {
        Collection<FMPackage> packages = new ArrayList<FMPackage>();
        try {
            // This is a specialized context that only a FM level user should be able to execute
            connection.setContext(FMUtil.FM_RPC_CONTEXT);
            // Create a resource adapater for the connection and RPC.  FM_RPC_NAME resolves to a
            // specialized RPC (installed with the OVID KIDs build) that knows how to execute FileMan
            // calls.  A "resource" is basically a message that defines the inputs/outputs of the RPC
            // we are calling.  The adapter is the "go between" that facilitates the message.
            ResAdapter adapter = new RPCResAdapter(connection, FMUtil.FM_RPC_NAME);

            FMResultSet results;

            // setup to run a fileman list query.
            FMQueryList query = new FMQueryList(adapter, FMPackage.getFileInfoForClass());
            FMScreen byIENs = null;
            for (Integer ien : iens) {
                if (byIENs == null) {
                    byIENs = new FMScreenEquals(new FMScreenIEN(), new FMScreenValue(ien.toString()));
                } else {
                    byIENs = new FMScreenOr(byIENs, new FMScreenEquals(new FMScreenIEN(), new FMScreenValue(ien.toString())));
                }
            }
            query.setScreen(byIENs);

            // this actually transmits our resource to RPC/VistaLink Broker and gleans the results.
            results = query.execute();
            while (results.next()) {
                packages.add(new FMPackage(results));
            }

        } catch (RPCException e) {
            throw new OvidDomainException(e);
        } catch (ResException e) {
            throw new OvidDomainException(e);
        }

        return packages;
    }

    // lookup an FMPackage object by ien
    public FMPackage getPackageByIEN(Integer ien) throws OvidDomainException {

        FMPackage packageFile = new FMPackage();
        if (ien == null) {
            return null;
        }

        try {
            // This is a specialized context that only a FM level user should be able to execute
            connection.setContext(FMUtil.FM_RPC_CONTEXT);
            // Create a resource adapater for the connection and RPC.  FM_RPC_NAME resolves to a
            // specialized RPC (installed with the OVID KIDs build) that knows how to execute FileMan
            // calls.  A "resource" is basically a message that defines the inputs/outputs of the RPC
            // we are calling.  The adapter is the "go between" that facilitates the message.
            ResAdapter adapter = new RPCResAdapter(connection, FMUtil.FM_RPC_NAME);

            packageFile.setIEN(ien.toString());
            FMRetrieve query = new FMRetrieve(adapter);
            query.setRecord(packageFile);
            query.execute();

        } catch (RPCException e) {
            throw new OvidDomainException(e);
        } catch (ResException e) {
            throw new OvidDomainException(e);
        }
        return packageFile;
    }

    public static void main(String[] args) throws OvidDomainException {
        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.ERROR);
        // rpcbroker: 9201
        // vistalink: 8002
        args = new String[] { "localhost", "9201", "OV1234", "OV1234!!" };
        if (args.length < 4) {
            System.err.println("usage: InstallRepository <host> <port> <access-code> <verify-code>");
            return;
        }

        RPCConnection connection = null;

        try {
            // choose which transport you want to use.... we'll go with vistalink for now.
            //connection = new VistaLinkRPCConnection(args[0], args[1], args[2], args[3]);
            connection = new RPCBrokerConnection(args[0], Integer.parseInt(args[1]), args[2], args[3]);

            InstallRepositoryTutorial repo = new InstallRepositoryTutorial(connection);

            // the follow are obsolete implementations left around for documentation purposes.
            //repo.printReport();
            //Collection<FMInstall> list = repo.getAllInstallRecords();

            // let's go back to 1/1/2007 as the start.
            Calendar startDate = GregorianCalendar.getInstance();
            startDate.set(2007, Calendar.JANUARY, 1, 0, 0, 0);

            // get the list of FMInstall records from 1/1/2007 to the present
            Collection<FMInstall> list = repo.getInstallRecordsBetweenDates(startDate.getTime(), new Date());

            // let's resolve all the related package files now.
            repo.findPackageFilesForInstall(list);

            // we want to have the list sorted by date.  Use the comparator found in FMInstall
            Collections.sort((ArrayList<FMInstall>)list, new FMInstall().new InstallDateComparator());

            for (FMInstall install : list) {
                System.out.println("INSTALL: "+ install);
                if (install.getPackageFile() != null) {
                    System.out.println("\tPACKAGE: " + install.getPackageFile());
                }
            }
            System.out.println("Got " + list.size() + " install records");

        } catch (NumberFormatException e) {
            throw new OvidDomainException(e);
        } catch (RPCException e) {
            throw new OvidDomainException(e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (RPCException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
