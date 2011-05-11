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
package com.medsphere.ovid.domain.ov;

import com.medsphere.fileman.FMQueryList;
import com.medsphere.fileman.FMResultSet;
import com.medsphere.fileman.FMScreen;
import com.medsphere.fileman.FMScreenEquals;
import com.medsphere.fileman.FMScreenIEN;
import com.medsphere.fileman.FMScreenValue;
import com.medsphere.fileman.FMUtil;
import com.medsphere.fmdomain.FMTIUDocumentHeader;
import com.medsphere.ovid.connection.OvidVistaLinkConnectionException;
import com.medsphere.resource.ResAdapter;
import com.medsphere.resource.ResException;
import com.medsphere.vistalink.VistaLinkPooledConnection;
import com.medsphere.vistalink.VistaLinkRPCConnection;
import com.medsphere.vistarpc.RPCException;
import com.medsphere.vistarpc.RPCResponse;
import com.medsphere.vistarpc.RPCResponse.ResponseType;
import com.medsphere.vistarpc.VistaRPC;
import gov.va.med.vistalink.adapter.cci.VistaLinkConnection;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class TIUNoteRepository extends OvidSecureRepository {
    public TIUNoteRepository(VistaLinkConnection conn) {
        super( conn );
    }

    public String[] getText(String[] ienList, boolean detailed) throws OvidDomainException {
        return detailed ? getDetailText(ienList) : getShortText(ienList);
    }

    public String[] getDetailText(String[] ienList) throws OvidDomainException {
        return callRPC("TIU DETAILED DISPLAY", ienList);
    }

    public String[] getShortText(String[] ienList) throws OvidDomainException {
        return callRPC("TIU GET RECORD TEXT", ienList);
     }
 
    private String[] callRPC(String rpcName, String[] ienList) throws OvidDomainException {
        String[] retVal = new String[ienList.length];
        try {
            VistaLinkRPCConnection rpcConnection = new VistaLinkRPCConnection(getConnection());
            rpcConnection.setContext(getContext());
            VistaRPC tiuRpc = new VistaRPC(rpcName, ResponseType.ARRAY);
            for (int idx=0; idx<ienList.length; ++idx) {
                tiuRpc.setParam(1, ienList[idx]);
                RPCResponse response = rpcConnection.execute(tiuRpc);
                String items[] = response.getArray();
                StringBuilder sb = new StringBuilder();
                for (int idx2=0; idx2<items.length; ++idx2) {
                    sb.append(items[idx2]).append("\n");
                }
                retVal[idx] = sb.toString();
            }
        } catch (RPCException ex) {
            throw new OvidDomainException(ex);
        } catch (OvidVistaLinkConnectionException e) {
            throw new OvidDomainException(e);
        }
        return retVal;
    }

    public FMTIUDocumentHeader getTIUDocumentHeader(String ien) throws OvidDomainException {
       FMTIUDocumentHeader tiuDocumentHeader = null;

        try {
            ResAdapter adapter = obtainVistaLinkAdapter();
            
            FMQueryList query = new FMQueryList(adapter, FMTIUDocumentHeader.getFileInfoForClass());
            FMScreen byIEN = new FMScreenEquals(new FMScreenIEN(), new FMScreenValue(ien));
            query.getField("DOCUMENT TYPE").setInternal(false);
            query.getField("AUTHOR/DICTATOR").setInternal(false);
            query.setScreen(byIEN);
            FMResultSet results = query.execute();
            if (results != null) {
                if (results.getError() != null) {
                    throw new OvidDomainException(results.getError());
                }
                while (results.next()) {
                    tiuDocumentHeader = new FMTIUDocumentHeader(results);
                }
            }
        } catch (OvidVistaLinkConnectionException e) {
            throw new OvidDomainException(e);
        } catch (ResException e) {
            throw new OvidDomainException(e);
        }
        return tiuDocumentHeader;
    }
    public static void main(String[] args) throws OvidDomainException {
        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.INFO);
        // may need to set to another A/V
        VistaLinkPooledConnection conn = getDirectConnection("VL1234", "VL1234!!!");
        if (conn==null) {
            return;
        }
        TIUNoteRepository repo = new TIUNoteRepository(conn);
        // for use with flowsheets. OR CPRS GUI CHART should work
        repo.setContext("MSC FLOWSHEETS");
        String[] iens = {"3", "4"};
        String[] texts = repo.getText(iens, true);
        for (String text : texts) {
            System.out.println(text);
        }
        texts = repo.getText(iens, false);
        for (String text : texts) {
            System.out.println(text);
        }
        
        repo.setContext(FMUtil.FM_RPC_CONTEXT);
        for (Integer i = 1; i < 10; i++) {
            FMTIUDocumentHeader doc = repo.getTIUDocumentHeader(i.toString());
            if (doc != null) {
                System.out.println(doc.getDocumentType() + " : " + doc.getAuthorName() + " : " + doc.getReferenceDate());
            }
        }
        
        
        conn.close();
    }
}
