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

import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.medsphere.ovid.domain.ov.OvidDomainException;
import com.medsphere.vistarpc.RPCBrokerConnection;
import com.medsphere.vistarpc.RPCConnection;
import com.medsphere.vistarpc.RPCException;
import com.medsphere.vistarpc.RPCResponse;
import com.medsphere.vistarpc.VistaRPC;
import com.medsphere.vistarpc.RPCResponse.ResponseType;

public class WardPatientRepository {
    private RPCConnection connection = null;
    private String rpcContext = null;

    public WardPatientRepository(RPCConnection connection, String context) {
        this.connection = connection;
        this.rpcContext = context;
    }

    // call ORQPT WARDS to get a list of active wards
    public Collection<Ward> getWards() throws OvidDomainException {
        // create an empty list to add to... this way, we can return an empty list
        // if no wards exist.
        Collection<Ward> wards = new ArrayList<Ward>();
        RPCConnection rpcConnection;

        try {
            rpcConnection = connection;
            rpcConnection.setContext(rpcContext);
        } catch (RPCException ex) {
            throw new OvidDomainException(ex);
        }

        VistaRPC rpc = new VistaRPC("ORQPT WARDS", ResponseType.ARRAY);
        try {
            RPCResponse response = rpcConnection.execute(rpc);
            String items[] = response.getArray();
            if (items.length == 0) {
                return wards;
            }
            for (String item : items) {
                String parts[] = item.split("\\^", -1);
                if (parts.length > 1) {
                    String ien = parts[0];
                    String name = parts[1];
                    wards.add(new Ward(ien, name));
                }
            }
        } catch (RPCException ex) {
            throw new OvidDomainException(ex);
        }

        return wards;
    }

    // call ORQPT WARD PATIENTS to get a list of active wards
    public Collection<WardPatient> getWardPatients(String wardIEN) throws OvidDomainException {
        // create an empty list to add to... this way, we can return an empty list
        // if no patients exist.
        Collection<WardPatient> wardPatients = new ArrayList<WardPatient>();
        RPCConnection rpcConnection;

        try {
            rpcConnection = connection;
            rpcConnection.setContext(rpcContext);
        } catch (RPCException ex) {
            throw new OvidDomainException(ex);
        }

        VistaRPC rpc = new VistaRPC("ORQPT WARD PATIENTS", ResponseType.ARRAY);
        rpc.setParam(1, wardIEN);
        try {
            RPCResponse response = rpcConnection.execute(rpc);
            String items[] = response.getArray();
            if (items.length == 0) {
                return wardPatients;
            }
            for (String item : items) {
                String parts[] = item.split("\\^", -1);
                if (parts.length > 1) {
                    String ien = parts[0];
                    String name = parts[1];
                    wardPatients.add(new WardPatient(ien, name));
                }
            }
        } catch (RPCException ex) {
            throw new OvidDomainException(ex);
        }

        return wardPatients;
    }

    public static void main(String[] args) throws OvidDomainException {
        // these two lines suppress some log4j noise.
        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.ERROR);

        // hard code the args here. comment the following line out if you want args
        // passed by the user
        args = new String[] { "openvista.medsphere.org", "9201", "PU1234", "PU1234!!", "OR CPRS GUI CHART" };
        if (args.length < 5) {
            System.err.println("usage: WardPatientRepository <host> <port> <access-code> <verify-code> <context>");
            return;
        }

        RPCConnection rconn = null;

        try {
            // get a connection to the RPCBroker on openvista.medsphere.org for the PU1234 user.
            rconn = new RPCBrokerConnection(args[0], Integer.parseInt(args[1]), args[2], args[3]);

            // instantiate the repository with the connection.  args[4] contains the "OR CPRS GUI CHART" context
            WardPatientRepository repo = new WardPatientRepository(rconn, args[4]);

            // get the list of wards
            Collection<Ward> wards = repo.getWards();

            // iterate through each ward.  we'll print the name and use the ien
            // as input to getWardPatients
            for (Ward ward : wards) {
                System.out.println("\nWard: " + ward);
                // print out each patient in the ward
                for (WardPatient patient : repo.getWardPatients(ward.getIEN())) {
                    System.out.println("\tPatient: " + patient);
                }
            }
        } catch (NumberFormatException e) {
            throw new OvidDomainException(e);
        } catch (RPCException e) {
            throw new OvidDomainException(e);
        } finally {
            if (rconn != null) {
                try {
                    rconn.close();
                } catch (RPCException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    // a simple bean to hold and print the values
    public class Ward {

        private String IEN;
        private String name;

        public Ward(String ien, String name) {

            this.IEN = ien;
            this.name = name;
        }

        public String getIEN() {
            return IEN;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "IEN=[" + IEN + "] name=[" + name + "]";
        }

    }

    // a simple bean to hold and print the values
    public class WardPatient {

        private String DFN;
        private String name;

        public WardPatient(String dfn, String name) {

            this.DFN = dfn;
            this.name = name;
        }

        public String getDFN() {
            return DFN;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "DFN=[" + DFN + "] name=[" + name + "]";
        }

    }
}
