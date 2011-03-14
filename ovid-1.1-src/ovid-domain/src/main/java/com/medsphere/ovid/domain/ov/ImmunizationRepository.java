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

import com.medsphere.fileman.FMQueryByIENS;
import com.medsphere.fileman.FMQueryFind;
import com.medsphere.fileman.FMResultSet;
import com.medsphere.fileman.FMScreen;
import com.medsphere.fileman.FMScreenEquals;
import com.medsphere.fileman.FMScreenField;
import com.medsphere.fileman.FMScreenValue;
import com.medsphere.fmdomain.FMImmunization;
import com.medsphere.fmdomain.FMPatientRefusalsForService;
import com.medsphere.fmdomain.FMVImmunization;
import com.medsphere.resource.ResAdapter;
import com.medsphere.resource.ResException;
import com.medsphere.vistarpc.RPCBrokerConnection;
import com.medsphere.vistarpc.RPCConnection;
import com.medsphere.vistarpc.RPCException;
import com.medsphere.vistarpc.RPCResponse;
import com.medsphere.vistarpc.VistaRPC;
import com.medsphere.vistarpc.RPCResponse.ResponseType;
import java.util.logging.Level;

/**
 * repository for Immunization data
 *
 */
public class ImmunizationRepository extends OvidSecureRepository {
    private Logger logger = LoggerFactory.getLogger(ImmunizationRepository.class);

    public ImmunizationRepository(RPCConnection userConnection, RPCConnection serverConnection) {
        super(userConnection, serverConnection);
        try {
            userConnection.setContext("OR CPRS GUI CHART");
        } catch (RPCException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Get immunizations for a DFN via RPC
     * @param dfn
     * @return
     * @throws OvidDomainException
     */
    public Collection<String> getImmunizationsList(String dfn) throws OvidDomainException {
        Collection<String> itemList = new ArrayList<String>();
        RPCConnection connection = null;
        VistaRPC rpc = new VistaRPC("ORQQPX IMMUN LIST", ResponseType.ARRAY);
        try {
            connection = getConnection();
            rpc.setParam(1, dfn);
            RPCResponse response = connection.execute(rpc);
            if (response.getError() != null) {
                throw new OvidDomainException(response.getError());
            }
            String items[] = response.getArray();
            if (isEmptyResult(items)) {
                return itemList;
            }

            for (String item : items) {
                itemList.add(item);
            }

            return itemList;
         } catch (RPCException ex) {
            logger.error("RPC Exception", ex);
            throw new OvidDomainException(ex);
        }

    }

    /**
     * getImmunizations for a DFN from V IMMUNIZATION
     * @param dfn
     * @return
     * @throws OvidDomainException
     */
    public Collection<FMVImmunization> getImmunizations(String dfn) throws OvidDomainException {
        Collection<FMVImmunization> list = new ArrayList<FMVImmunization>();
        try {
            ResAdapter adapter = obtainServerRPCAdapter();

            FMQueryFind query = new FMQueryFind(adapter, FMVImmunization.getFileInfoForClass());
            query.getField("IMMUNIZATION").setInternal(true);
            query.getField("PATIENT NAME").setInternal(false);
            query.getField("VISIT").setInternal(false);
            query.getField("LOT").setInternal(false);
            query.getField("REACTION").setInternal(false);
            query.getField("ORDERING PROVIDER").setInternal(false);
            query.getField("CLINIC").setInternal(false);
            query.getField("ENCOUNTER PROVIDER").setInternal(false);
            query.getField("PARENT").setInternal(false);
            query.getField("ANCILLARY POV").setInternal(false);
            query.getField("USER LAST UPDATE").setInternal(false);
            query.getField("ORDERING LOCATION").setInternal(false);
            query.getField("PACKAGE").setInternal(false);
            query.getField("PACKAGE").setInternal(false);
            query.setIndex("AC", dfn);
            FMResultSet results = query.execute();
            if (results != null) {
                if (results.getError() != null) {
                    throw new OvidDomainException(results.getError());
                }
                while (results.next()) {
                    list.add(new FMVImmunization(results));
                }
            }
        } catch (ResException e) {
            throw new OvidDomainException(e);
        } finally {
        }
        getImmunizationDetails(list);
        return list;
    }

    private void getImmunizationDetails(Collection<FMVImmunization> vImmunizations) throws OvidDomainException {
        if (vImmunizations == null || vImmunizations.size() < 1) {
            return;
        }
        try {
            ResAdapter adapter = obtainServerRPCAdapter();

            FMQueryByIENS query = new FMQueryByIENS(adapter, FMImmunization.getFileInfoForClass());
            query.getField("DEFAULT LOT#").setInternal(false);
            query.getField("VACCINE GROUP (SERIES TYPE)").setInternal(false);
            query.getField("CPT CODE").setInternal(false);
            query.getField("COMPONENT #1").setInternal(false);
            query.getField("COMPONENT #2").setInternal(false);
            query.getField("COMPONENT #3").setInternal(false);
            query.getField("COMPONENT #4").setInternal(false);
            query.getField("COMPONENT #5").setInternal(false);
            query.getField("COMPONENT #6").setInternal(false);
            query.getField("CPT CODE 2ND").setInternal(false);

            for (FMVImmunization v : vImmunizations) {
                query.addIEN(v.getImmunization());
            }
            FMResultSet results = query.execute();
            if (results != null) {
                if (results.getError() != null) {
                    throw new OvidDomainException(results.getError());
                }
                while (results.next()) {
                    FMImmunization immunization = new FMImmunization(results);
                    for (FMVImmunization v : vImmunizations) {
                        if (v.getImmunization().equals(immunization.getIEN())) {
                            v.setImmunizationDetails(immunization);
                        }
                    }
                }
            }
        } catch (ResException e) {
            throw new OvidDomainException(e);
        } finally {
        }


    }

    /**
     * Refusals for immunizations
     * @param dfn
     * @return
     * @throws OvidDomainException
     */
    public Collection<FMPatientRefusalsForService> getRefusals(String dfn) throws OvidDomainException {
        Collection<FMPatientRefusalsForService> list = new ArrayList<FMPatientRefusalsForService>();
        try {
            PlatformType platformType = new KernelSystemParametersRepository(getServerConnection()).getPlatformType();
            if (platformType == PlatformType.RPMS) {
                ResAdapter adapter = obtainServerRPCAdapter();

                FMQueryFind query = new FMQueryFind(adapter, FMPatientRefusalsForService.getFileInfoForClass());
                query.getField("REFUSAL TYPE").setInternal(true);
                query.getField("PATIENT NAME").setInternal(false);
                query.getField("POINTER FILE").setInternal(false);
                query.getField("PROVIDER WHO DOCUMENTED").setInternal(false);
                query.setIndex("AC", dfn);
                FMScreen screen = new FMScreenEquals(new FMScreenField("REFUSAL TYPE"), new FMScreenValue("3")); // 3 == IMMUNIZATION
                query.setScreen(screen);
                FMResultSet results = query.execute();
                if (results != null) {
                    if (results.getError() != null) {
                        throw new OvidDomainException(results.getError());
                    }
                    while (results.next()) {
                        list.add(new FMPatientRefusalsForService(results));
                    }
                }

            }
            return list;
        } catch (ResException e) {
            throw new OvidDomainException(e);
        } finally {
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
                System.err.println("usage: ImmunizationRepository <host> <port> <user-access-code> <user-verify-code> <ovid-access-code> <ovid-verify-code>");
                return;
            }

            userConn = new RPCBrokerConnection(args[0], new Integer(args[1]), args[2], args[3]);
            serverConn = new RPCBrokerConnection(args[0], new Integer(args[1]), args[4], args[5]);
            if (serverConn==null || userConn==null) {
                return;
            }

            ImmunizationRepository repo = new ImmunizationRepository(userConn, serverConn);
            for (Integer i = 14400; i <= 14484; i++) {
                //System.out.println("*** dfn: " + i);
                Collection<FMVImmunization> immunizations = repo.getImmunizations(i.toString());
                if (immunizations.size() > 0) {
                    System.out.println(new PatientRepository(userConn, serverConn).getPatientByIEN(i.toString()));
                    System.out.println("\t# immunizations: " + immunizations.size());
                    for (FMVImmunization imm : immunizations) {
                        System.out.println("\t"+imm);
                    }
                    for (FMPatientRefusalsForService refusal : repo.getRefusals(i.toString())) {
                        //System.out.println("Refusal: " + refusal);
                    }
                }
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
        System.exit(0);

    }

}
