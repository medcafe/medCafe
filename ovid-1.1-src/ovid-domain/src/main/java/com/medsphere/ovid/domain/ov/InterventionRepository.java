// <editor-fold defaultstate="collapsed">
/*
 * Copyright (C) 2007-2010  Medsphere Systems Corporation
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

import com.medsphere.fileman.FMQueryByIENS;
import com.medsphere.fileman.FMResultSet;
import com.medsphere.fmdomain.FMIntervention;
import com.medsphere.fmdomain.FMPatient;
import com.medsphere.resource.ResAdapter;
import com.medsphere.resource.ResException;
import com.medsphere.vistarpc.RPCBrokerConnection;
import com.medsphere.vistarpc.RPCConnection;
import com.medsphere.vistarpc.RPCException;
import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mark.taylor
 */
public class InterventionRepository extends OvidSecureRepository {

    private static Logger logger = LoggerFactory.getLogger(InterventionRepository.class);

    /**
     * right now, we only use a server connection
     * @param serverConnection
     */
     public InterventionRepository(RPCConnection serverConnection) {
        super(null, serverConnection);
    }

    public Collection<FMIntervention> getInterventionsByPatient(String patientIEN) throws OvidDomainException {
        return getInterventionsByPatient(patientIEN, false);
    }

    public Collection<FMIntervention> getInterventionsByPatient(String patientIEN, boolean internal) throws OvidDomainException {
        Collection<FMIntervention> retList = new ArrayList<FMIntervention>();

        try {
            RPCConnection rpcConnection = getConnection();
            ResAdapter adapter = obtainServerRPCAdapter();

            FMQueryByIENS query = new FMQueryByIENS(adapter, FMPatient.getFileInfoForClass());
            query.addIEN(patientIEN);
            query.getField("PATIENT").setInternal(internal);
            query.getField("PROVIDER").setInternal(internal);
            query.getField("PHARMACIST").setInternal(internal);
            query.getField("DRUG").setInternal(internal);
            query.getField("INSTITUTED BY").setInternal(internal);
            query.getField("DIVISION").setInternal(internal);
            FMResultSet results = query.execute();
            if (results != null) {
                if (results.getError() != null) {
                    throw new OvidDomainException(results.getError());
                }
                while (results.next()) {
                    retList.add(new FMIntervention(results));
                }
            }
        } catch (ResException e) {
            throw new OvidDomainException(e);
        }
        return retList;
    }

    public static void main(String[] args) {
        //BasicConfigurator.configure();
        //Logger.getRootLogger().setLevel(Level.ERROR);
        RPCConnection serverConn = null;

        try {

            if (args == null || args.length == 0) {
                args = new String[] { "openvista.medsphere.org", "9201", "OV1234", "OV1234!!", "7"};
            }

            if (args.length < 5) {
                System.err.println("usage: PrescriptionRepository <host> <port> <ovid-access-code> <ovid-verify-code> <dfn>");
                return;
            }

            String dfn = args[4];
            serverConn = new RPCBrokerConnection(args[0], new Integer(args[1]), args[2], args[3]);
            if (serverConn==null) {
                return;
            }

            InterventionRepository repo = new InterventionRepository(serverConn);
            Collection<String> iens = new ArrayList<String>();
            iens.add("1");
            iens.add("2");
            iens.add("3");

            Collection<FMIntervention> interventions = repo.getInterventionsByPatient("7");
            System.out.println(interventions.toString());

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
