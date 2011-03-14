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
package com.medsphere.ovid.domain.ov;

import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.medsphere.fileman.FMFile;
import com.medsphere.fileman.FMQueryList;
import com.medsphere.fileman.FMRecord;
import com.medsphere.fileman.FMResultSet;
import com.medsphere.fmdomain.FMNewPerson;
import com.medsphere.resource.ResException;
import com.medsphere.vistarpc.RPCBrokerConnection;
import com.medsphere.vistarpc.RPCConnection;
import com.medsphere.vistarpc.RPCException;
import com.medsphere.vistarpc.RPCResponse;
import com.medsphere.vistarpc.VistaRPC;
import com.medsphere.vistarpc.RPCResponse.ResponseType;

public class ProviderRepository extends OvidSecureRepository {
    private Logger logger = LoggerFactory.getLogger(ProviderRepository.class);
    public ProviderRepository(RPCConnection connection,	RPCConnection serverConnection, String context) {
        super(connection, serverConnection);
        try {
            connection.setContext(context);
        } catch (RPCException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public Collection<FMNewPerson> findProvidersByName(String lastName, String firstName) throws OvidDomainException {
        RPCConnection connection = null;
        VistaRPC rpc = new VistaRPC("ORQQPL PROVIDER LIST", ResponseType.ARRAY);
        try {
            connection = getConnection();
            rpc.setParam(1, ""); // flag
            rpc.setParam(2, ""); // nbr to find
            rpc.setParam(3, ""); // from
            if (lastName == null) {
                throw new OvidDomainException("must supply last name of provider");
            }
            if (firstName != null  && firstName.length() > 0) {
                rpc.setParam(4, lastName + "," + firstName);
            } else {
                rpc.setParam(4, lastName);
            }
            RPCResponse response = connection.execute(rpc);
            if (response.getError() != null) {
                throw new OvidDomainException(response.getError());
            }
            String items[] = response.getArray();

            if (isEmptyResult(items)) {
                return new ArrayList<FMNewPerson>();
            }

            Collection<String> iens = new ArrayList<String>();
            for (String item : items) {
                String parts[] = item.split("\\^",-1);
                String id = parts[0];
                String name = parts[1];
                logger.debug( "id: " + id + ", name: " + name);
                iens.add(id);
            }
            NewPersonRepository newPersonRepo = new NewPersonRepository(getServerConnection());
            Collection<FMNewPerson> list = newPersonRepo.getNewPersonsByIEN(iens);
            for (FMNewPerson provider : list) {
                provider.setPersonClassList(newPersonRepo.getPersonClass(provider));
            }
            new NameComponentRepository(getServerConnection()).fetchNameComponents((Collection)list);
            return list;
        } catch (RPCException ex) {
            logger.error("RPC exception", ex);
            throw new OvidDomainException(ex);
        }
    }

    public class DivisionInfo {
        private String ien;
        private String name;
        private String defaultCode;

        public String getDefaultCode() {
            return defaultCode;
        }

        public void setDefaultCode(String defaultCode) {
            this.defaultCode = defaultCode;
        }

        public String getIen() {
            return ien;
        }

        public void setIen(String ien) {
            this.ien = ien;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final DivisionInfo other = (DivisionInfo) obj;
            if ((this.ien == null) ? (other.ien != null) : !this.ien.equals(other.ien)) {
                return false;
            }
            if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 37 * hash + (this.ien != null ? this.ien.hashCode() : 0);
            hash = 37 * hash + (this.name != null ? this.name.hashCode() : 0);
            hash = 37 * hash + (this.defaultCode != null ? this.defaultCode.hashCode() : 0);
            return hash;
        }     

    }
    
    public Collection<DivisionInfo> getProviderDivisions(FMNewPerson provider) throws OvidDomainException {
        Collection<DivisionInfo> divs = new ArrayList<DivisionInfo>();

        try {
            FMFile secondarySubfile = new FMFile("16"); // 16 is the field number for division in new person
            secondarySubfile.addField(".01").setInternal(false);
            secondarySubfile.addField("1"); // default code            
            secondarySubfile.setParentRecord(provider); // So the query knows which set of subfile records

            FMQueryList divisionQuery = new FMQueryList( obtainServerRPCAdapter(), secondarySubfile );
            FMResultSet results = divisionQuery.execute();

            while (results.next()) {
                FMRecord entry = new FMRecord(results);
                DivisionInfo div = new DivisionInfo();
                div.setName(entry.getValue(".01"));
                div.setIen(entry.getIEN());
                div.setDefaultCode(entry.getValue("1"));
                divs.add(div);
            }
        } catch (ResException e) {
            e.printStackTrace();
            new OvidDomainException(e);
        }

        return divs;
    }

    public Collection<FMNewPerson> getProviders() throws OvidDomainException {
        RPCConnection connection = null;
        VistaRPC rpc = new VistaRPC("ORQPT PROVIDERS", ResponseType.ARRAY);
        try {
            connection = getConnection();
            RPCResponse response = connection.execute(rpc);
            if (response.getError() != null) {
                throw new OvidDomainException(response.getError());
            }
            String items[] = response.getArray();

            if (isEmptyResult(items)) {
                return new ArrayList<FMNewPerson>();
            }

            Collection<String> iens = new ArrayList<String>();
            for (String item : items) {
                String parts[] = item.split("\\^",-1);
                String id = parts[0];
                String name = parts[1];
                logger.debug( "id: " + id + ", name: " + name);
                iens.add(id);
            }
            NewPersonRepository newPersonRepo = new NewPersonRepository(getServerConnection());
            Collection<FMNewPerson> list = newPersonRepo.getNewPersonsByIEN(iens);
            for (FMNewPerson provider : list) {
                provider.setPersonClassList(newPersonRepo.getPersonClass(provider));
            }
            new NameComponentRepository(getServerConnection()).fetchNameComponents((Collection)list);
            return list;
        } catch (RPCException ex) {
            logger.error("RPC exception", ex);
            throw new OvidDomainException(ex);
        }

    }

    public static void main(String[] args) {
        //BasicConfigurator.configure();
        //Logger.getRootLogger().setLevel(Level.ERROR);
        RPCConnection userConn = null;
        RPCConnection serverConn = null;

        try {

            if (args == null || args.length == 0) {
                args = new String[] { "localhost", "9201", "PU1234", "PU1234!!", "OV1234", "OV1234!!"};
            }

            if (args.length < 5) {
                System.err.println("usage: ProviderRepository <host> <port> <user-access-code> <user-verify-code> <ovid-access-code> <ovid-verify-code>");
                return;
            }

            String dfn = args[4];
            userConn = new RPCBrokerConnection(args[0], new Integer(args[1]), args[2], args[3]);
            serverConn = new RPCBrokerConnection(args[0], new Integer(args[1]), args[4], args[5]);
            if (serverConn==null || userConn==null) {
                return;
            }

            ProviderRepository repo = new ProviderRepository(userConn, serverConn, "OR CPRS GUI CHART");
            System.out.println("*** all providers");
            for (FMNewPerson provider : repo.getProviders()) {
                System.out.println(provider);
                System.out.println("\tdivs: " + repo.getProviderDivisions(provider));
             }

            String lastName = "MANAGER";
            String firstName = "SYSTEM";
            System.out.println("*** provider search for " + lastName + " : " + firstName);
            for (FMNewPerson provider : repo.findProvidersByName(lastName, firstName)) {
                System.out.println(provider);                
             }

        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (RPCException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (OvidDomainException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (userConn != null) {
                try {
                    userConn.close();
                } catch (RPCException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            if (serverConn != null) {
                try {
                    serverConn.close();
                } catch (RPCException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    }

}
