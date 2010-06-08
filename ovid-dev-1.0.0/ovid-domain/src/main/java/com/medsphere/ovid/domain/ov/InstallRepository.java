package com.medsphere.ovid.domain.ov;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

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
import com.medsphere.fmdomain.FMInstall;
import com.medsphere.fmdomain.FMPackage;
import com.medsphere.resource.ResAdapter;
import com.medsphere.resource.ResException;
import com.medsphere.rpcresadapter.RPCResAdapter;
import com.medsphere.vistarpc.RPCConnection;
import com.medsphere.vistarpc.RPCException;
import com.medsphere.vistarpc.factory.RPCBrokerPooledConnectionFactory;
import com.medsphere.vistarpc.factory.RPCPooledConnectionFactory;

public class InstallRepository {

    private RPCConnection connection = null;

    public InstallRepository(RPCConnection connection) {
        this.connection = connection;
    }

    // this method will return a list of all FMInstall records between the start and end date specified.
    public Collection<FMInstall> getInstallRecordsBetweenDates(Date startDate, Date endDate) throws OvidDomainException {
        Collection<FMInstall> list = new ArrayList<FMInstall>();

        try {
            connection.setContext(FMUtil.FM_RPC_CONTEXT);
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
    // query.  Note that screens can't be Or'd together one after the other.
    public Collection<FMPackage> getPackages(Collection<Integer> iens) throws OvidDomainException {
        Collection<FMPackage> packages = new ArrayList<FMPackage>();
        try {
            connection.setContext(FMUtil.FM_RPC_CONTEXT);
             ResAdapter adapter = new RPCResAdapter(connection, FMUtil.FM_RPC_NAME);

            FMResultSet results;

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
             connection.setContext(FMUtil.FM_RPC_CONTEXT);
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

    private static void testUsingPooledRPCBroker() throws OvidDomainException {
        RPCPooledConnectionFactory pooledConnectionFactory = null;
        RPCConnection connection = null;

        try {
            pooledConnectionFactory = new RPCBrokerPooledConnectionFactory("openvista.medsphere.org", "9201", "OV1234", "OV1234!!");
            // choose which transport you want to use.... we'll go with vistalink for now.
            //connection = new VistaLinkRPCConnection(args[0], args[1], args[2], args[3]);
            connection = pooledConnectionFactory.getConnection();  // new RPCBrokerConnection(args[0], Integer.parseInt(args[1]), args[2], args[3]);

            InstallRepository repo = new InstallRepository(connection);

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
    public static void main(String[] args) throws OvidDomainException {
        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.ERROR);
        // rpcbroker: 9201
        // vistalink: 8002
        args = new String[] { "openvista.medsphere.org", "9201", "OV1234", "OV1234!!" };
        if (args.length < 4) {
            System.err.println("usage: InstallRepository <host> <port> <access-code> <verify-code>");
            return;
        }

        InstallRepository.testUsingPooledRPCBroker();
//        RPCConnection connection = null;
//
//        try {
//            // choose which transport you want to use.... we'll go with vistalink for now.
//            //connection = new VistaLinkRPCConnection(args[0], args[1], args[2], args[3]);
//            connection = new RPCBrokerConnection(args[0], Integer.parseInt(args[1]), args[2], args[3]);
//
//            InstallRepository repo = new InstallRepository(connection);
//
//             // let's go back to 1/1/2007 as the start.
//            Calendar startDate = GregorianCalendar.getInstance();
//            startDate.set(2007, Calendar.JANUARY, 1, 0, 0, 0);
//
//            // get the list of FMInstall records from 1/1/2007 to the present
//            Collection<FMInstall> list = repo.getInstallRecordsBetweenDates(startDate.getTime(), new Date());
//
//            // let's resolve all the related package files now.
//            repo.findPackageFilesForInstall(list);
//
//            // we want to have the list sorted by date.  Use the comparator found in FMInstall
//            Collections.sort((ArrayList<FMInstall>)list, new FMInstall().new InstallDateComparator());
//
//            for (FMInstall install : list) {
//                System.out.println("INSTALL: "+ install);
//                if (install.getPackageFile() != null) {
//                    System.out.println("\tPACKAGE: " + install.getPackageFile());
//                }
//            }
//            System.out.println("Got " + list.size() + " install records");
//
//            repo.testUsingPooledRPCBroker();
//
//        } catch (NumberFormatException e) {
//            throw new OvidDomainException(e);
//        } catch (RPCException e) {
//            throw new OvidDomainException(e);
//        } finally {
//            if (connection != null) {
//                try {
//                    connection.close();
//                } catch (RPCException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
    }

}
