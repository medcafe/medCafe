// <editor-fold defaultstate="collapsed">
/*
 * Copyright (C) 2009  Medsphere Systems Corporation
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

import com.medsphere.fileman.FMQueryRefBy;
import com.medsphere.fileman.FMResultSet;
import com.medsphere.fileman.FMUtil;
import com.medsphere.resource.ResAdapter;
import com.medsphere.resource.ResException;
import com.medsphere.rpcresadapter.RPCResAdapter;
import com.medsphere.vistarpc.RPCBrokerConnection;
import com.medsphere.vistarpc.RPCConnection;
import com.medsphere.vistarpc.RPCException;
import org.testng.annotations.Test;

@Test(groups = {"acceptance"})
public class TestGlobalArrayRPC {

    @Test
    public void doRefBy() throws RPCException, ResException {
        //BasicConfigurator.resetConfiguration();
        //BasicConfigurator.configure();
        //RPCConnection connection = new RPCBrokerConnection("sql.medsphere.com", 9260, "OV1234", "OV1234!!");
        RPCConnection connection = new RPCBrokerConnection("192.168.1.102", 9201, "FS12345", "FS12345!!");
        connection.setContext(FMUtil.FM_RPC_CONTEXT);
        ResAdapter adapter = new RPCResAdapter(connection, FMUtil.FM_RPC_NAME);
        FMQueryRefBy refBy = new FMQueryRefBy(adapter, "2");
        FMResultSet results = refBy.execute();
        while (results.next()) {
            System.out.println("FILE: " + results.getValue("FILE") + "  FIELD: " + results.getValue("FIELD"));
        }
        connection.close();
    }
}
