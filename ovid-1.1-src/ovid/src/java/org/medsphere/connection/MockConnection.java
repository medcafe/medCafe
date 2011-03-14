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

package org.medsphere.connection;

import com.medsphere.vistarpc.AbstractRPCConnection;
import com.medsphere.vistarpc.RPCException;
import com.medsphere.vistarpc.RPCResponse;
import com.medsphere.vistarpc.RPCResponse.ResponseType;
import com.medsphere.vistarpc.VistaRPC;

/**
 * A mock, do-nothing connection.
 */
public class MockConnection extends AbstractRPCConnection {
    final VistaConnectionProperties vcp;
    private String lastRPCExecuted = null;
    private long lastExecuteTime = 0L;
    private static int globalID = 0;
    private int id = ++globalID;
    public final static String STATUS_RPC_NAME = "Fake rpc just to get status";

    public MockConnection() {
         this.vcp = new VistaConnectionProperties();
    }

    public MockConnection(VistaConnectionProperties vcp) {
        this.vcp = new VistaConnectionProperties(vcp);
    }

    public void close() throws RPCException {
    }

    public void setContext(String string) throws RPCException {
    }

    public RPCResponse execute(VistaRPC vrpc) throws RPCException {
        String rpcName = vrpc.getName();
        if (rpcName.equals(STATUS_RPC_NAME)) {
            return new RPCResponse(new String[] {lastRPCExecuted, Long.toString(lastExecuteTime), Integer.toString(id)});
        }
        lastRPCExecuted = vrpc.getName();
        lastExecuteTime = System.currentTimeMillis();
        if (vrpc.getType()==ResponseType.SINGLE_VALUE) {
            return new RPCResponse("OK");
        }
        return new RPCResponse(new String[]{"OK"});
    }

    public String buildSubscript(String string) {
        return string;
    }

    public String getDUZ() {
        return "";
    }

}
