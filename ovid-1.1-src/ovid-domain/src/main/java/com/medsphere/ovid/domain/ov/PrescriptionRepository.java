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
import com.medsphere.fileman.FMQueryList;
import com.medsphere.fileman.FMResultSet;
import com.medsphere.fmdomain.FMPrescription;
import com.medsphere.fmdomain.FMPrescriptionRefill;
import com.medsphere.resource.ResAdapter;
import com.medsphere.resource.ResException;
import com.medsphere.vistarpc.RPCBrokerConnection;
import com.medsphere.vistarpc.RPCConnection;
import com.medsphere.vistarpc.RPCException;

public class PrescriptionRepository extends OvidSecureRepository {

    private static Logger logger = LoggerFactory.getLogger(PrescriptionRepository.class);

    /**
     * right now, we only use a server connection
     * @param serverConnection
     */
    public PrescriptionRepository(RPCConnection serverConnection) {
        super(null, serverConnection);
    }

    public Collection<FMPrescription> getPrescriptionsByDFN(String dfn) throws OvidDomainException {
        return getPrescriptionsByDFN(dfn, false);
    }

    public Collection<FMPrescription> getPrescriptionsByDFN(String dfn, boolean internal) throws OvidDomainException {
        Collection<FMPrescription> list = new ArrayList<FMPrescription>();

        try {

            ResAdapter adapter = obtainServerRPCAdapter();

            FMQueryFind query = new FMQueryFind(adapter, FMPrescription.getFileInfoForClass());
            query.setIndex("C", dfn);

            query.getField("PATIENT").setInternal(internal);
            query.getField("DRUG").setInternal(internal);
            query.getField("PROVIDER").setInternal(internal);
            query.getField("PATIENT STATUS").setInternal(internal);
            query.getField("PHARMACIST").setInternal(internal);
            FMResultSet results = query.execute();
            if (results != null) {
                if (results.getError() != null) {
                    throw new OvidDomainException(results.getError());
                }
                while (results.next()) {
                    list.add(new FMPrescription(results));
                }
            }
        } catch (ResException e) {
            throw new OvidDomainException(e);
        }

        return list;

    }

    public Collection<FMPrescription> getPrescriptionsByIEN(Collection<String> iens) throws OvidDomainException {
        return getPrescriptionsByIEN(iens, false);
    }

    public Collection<FMPrescription> getPrescriptionsByIEN(Collection<String> iens, boolean internal) throws OvidDomainException {
        Collection<FMPrescription> list = new ArrayList<FMPrescription>();

        try {

            ResAdapter adapter = obtainServerRPCAdapter();

            FMQueryByIENS query = new FMQueryByIENS(adapter, FMPrescription.getFileInfoForClass());
            query.setIENS(iens);

            query.getField("PATIENT").setInternal(internal);
            query.getField("DRUG").setInternal(internal);
            query.getField("PROVIDER").setInternal(internal);
            query.getField("PATIENT STATUS").setInternal(internal);
            query.getField("PHARMACIST").setInternal(internal);
            FMResultSet results = query.execute();
            if (results != null) {
                if (results.getError() != null) {
                    throw new OvidDomainException(results.getError());
                }
                while (results.next()) {
                    list.add(new FMPrescription(results));
                }
            }
        } catch (ResException e) {
            throw new OvidDomainException(e);
        }

        return list;

    }
    public Collection<FMPrescriptionRefill> getRefills(FMPrescription prescription) throws OvidDomainException {
        Collection<FMPrescriptionRefill> refillList = new ArrayList<FMPrescriptionRefill>();
        try {
            ResAdapter adapter = obtainServerRPCAdapter();
            FMPrescriptionRefill refill = prescription.getRefill();

            //refill.setParent(prescription);
            FMQueryList query = new FMQueryList( adapter,  refill.getFile());
            query.getField("PHARMACIST NAME").setInternal(false);
            FMResultSet results = query.execute();
            if (results.getError() != null) {
                throw new OvidDomainException(results.getError());
            }
            while (results.next()) {
                FMPrescriptionRefill qRefill = new FMPrescriptionRefill(results);
                refillList.add(qRefill);
            }
        } catch (ResException e) {
            throw new OvidDomainException(e);
        } finally {}
        return refillList;
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

            PrescriptionRepository repo = new PrescriptionRepository(serverConn);
            Collection<String> iens = new ArrayList<String>();
            iens.add("1");
            iens.add("2");
            iens.add("3");
            Collection<FMPrescription> prescriptions = repo.getPrescriptionsByIEN(iens);
            for (FMPrescription script : prescriptions) {
                logger.debug(script.getIEN() + ":" + script);
                Collection<FMPrescriptionRefill> refills = repo.getRefills(script);
                for (FMPrescriptionRefill refill : refills) {
                    logger.debug("\t"+refill);
                }
            }
            for (Integer i = 1; i < 1000; i++) {
                if ((i % 100) == 0) {
                    logger.debug(i +"...");
                }
                for (FMPrescription prescription : repo.getPrescriptionsByDFN(i.toString())) {
                    logger.debug("dfn: " + i);
                    logger.debug(prescription.toString());
                    logger.debug("*** refill info:");
                    for (FMPrescriptionRefill refill : repo.getRefills(prescription)) {
                        logger.debug("\t =>"+refill);
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
