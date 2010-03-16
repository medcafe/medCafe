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

/*
 * Repository class for obtaining drug information.
 */
package com.medsphere.ovid.domain.ov;

import com.medsphere.common.util.TimeKeeperDelegate;
import com.medsphere.fileman.FMQueryList;
import com.medsphere.fileman.FMResultSet;
import com.medsphere.fileman.FMScreenIsNull;
import com.medsphere.fileman.FMUtil;
import com.medsphere.fmdomain.FMDrug;
import com.medsphere.ovid.connection.OvidVistaLinkConnectionException;
import com.medsphere.resource.ResAdapter;
import com.medsphere.resource.ResException;
import com.medsphere.rpcresadapter.RPCResAdapter;
import com.medsphere.vistalink.VistaLinkPooledConnection;
import com.medsphere.vistalink.VistaLinkRPCConnection;
import com.medsphere.vistarpc.RPCConnection;
import com.medsphere.vistarpc.RPCException;
import gov.va.med.vistalink.adapter.cci.VistaLinkConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.log4j.BasicConfigurator;

public class DrugRepository extends OvidSecureRepository {

    public DrugRepository(VistaLinkConnection connection) {
        super(connection);
    }

    public Collection<FMDrug> getActiveDrugList() throws OvidDomainException {
        Collection<FMDrug> list = new ArrayList<FMDrug>();

        try {
            TimeKeeperDelegate.start("Getting Connection");
            RPCConnection connection = new VistaLinkRPCConnection(getConnection());
            try {
                connection.setContext(FMUtil.FM_RPC_CONTEXT);
            } catch (RPCException ex) {
                throw new OvidDomainException(ex);
            }
            TimeKeeperDelegate.stopAndLog("Getting Connection");
            
            TimeKeeperDelegate.start("getting ActiveDrugList");
            ResAdapter adapter =
                    new RPCResAdapter(connection,
                    FMUtil.FM_RPC_NAME);
            FMQueryList query = new FMQueryList(adapter, FMDrug.getFileInfoForClass());
            // modify query
            query.setScreen(new FMScreenIsNull("100", true));
            query.removeField("100"); // remove inactive date from query results, since we're screening it

            FMResultSet results = query.execute();
            if (results != null) {
                if (results.getError() != null) {
                    throw new OvidVistaLinkConnectionException(results.getError());
                }
                while (results.next()) {
                    list.add(new FMDrug(results));
                }
            }
        } catch (OvidVistaLinkConnectionException e) {
            throw new OvidDomainException(e);
        } catch (ResException e) {
            throw new OvidDomainException(e);
        } finally {
            TimeKeeperDelegate.stopAndLog("getting ActiveDrugList");
        }

        return list;
    }

    public static void main(String[] main) throws OvidDomainException {
        BasicConfigurator.configure();
        VistaLinkPooledConnection conn = null;
        try {
            conn = getDirectConnection("VL1234", "VL1234!!!");
            if (conn==null) {
                return;
            }
            Collection<FMDrug> list = new DrugRepository(conn).getActiveDrugList();
            for (FMDrug drug : list) {
                System.out.println("Drug: " + drug.getIEN() + ", " + drug.getGenericName() + ", " + drug.getNdc()); // + "," + drug.getSynonym().getSynonym());
            }
            System.out.println("got " + list.size() + " drugs.");
        } finally {
            conn.close();
        }
    }
}
