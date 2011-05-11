// <editor-fold defaultstate="collapsed">
/*
 * Copyright (C) 2010  Medsphere Systems Corporation
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
package com.medsphere.ovid.test.acceptance;

import com.medsphere.fileman.FMField;
import com.medsphere.fileman.FMFile;
import com.medsphere.fileman.FMQueryList;
import com.medsphere.fileman.FMRecord;
import com.medsphere.fileman.FMResultSet;
import com.medsphere.fileman.FMRetrieve;
import com.medsphere.fileman.FMUtil;
import com.medsphere.resource.ResException;
import com.medsphere.rpcresadapter.RPCResAdapter;
import com.medsphere.vistarpc.RPCBrokerConnection;
import com.medsphere.vistarpc.RPCConnection;
import com.medsphere.vistarpc.RPCException;
import org.testng.annotations.Test;

@Test(groups = {"acceptance"})
public class TestWPRetrieval {

    @Test
    void doRetrieve() throws RPCException, ResException {
        //BasicConfigurator.resetConfiguration();
        //BasicConfigurator.configure();

        FMFile fileInfo = new FMFile("DELTA CHECKS");
        RPCConnection connection = new RPCBrokerConnection("192.168.1.102", 9201, "FS12345", "FS12345!!");
        connection.setContext(FMUtil.FM_RPC_CONTEXT);
        RPCResAdapter adapter = new RPCResAdapter(connection, FMUtil.FM_RPC_NAME);

        FMQueryList queryList = new FMQueryList(adapter, fileInfo);
        try {
            FMRecord deltaChecks = new FMRecord(fileInfo);
            FMResultSet listResults = queryList.execute();
            FMRetrieve retrieve = new FMRetrieve(adapter);
            retrieve.setRecord(deltaChecks);
            retrieve.addField("DESCRIPTION", FMField.FIELDTYPE.WORD_PROCESSING);
            while (listResults.next()) {
                deltaChecks.setIEN(listResults.getValue("IEN"));
                retrieve.execute();
                System.out.println("## " + deltaChecks.getValue("DESCRIPTION"));
            }
        } catch (ResException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
