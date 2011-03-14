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
package org.medsphere.datasource.pool;

import com.medsphere.fileman.FMUtil;
import com.medsphere.vistarpc.RPCException;
import com.medsphere.vistarpc.VistaRPC;
import java.util.Date;
import org.medsphere.connection.VistaConnectionProperties;
import org.medsphere.pool.PoolEventCallback;

class PingCallback implements PoolEventCallback<PooledConnectionProxy> {

    private String pingRPC;
    private String pingContext;
    private final long interval;

    public PingCallback(VistaConnectionProperties properties, long interval) {
        this.interval = interval;
        pingRPC = properties.get("pingRPC");
        if (pingRPC == null) {
            pingRPC = "XWB IM HERE";
        }
        pingContext = properties.get("pingContext");
        if (pingContext == null) {
            //We really don't have a good default
            pingContext = FMUtil.FM_RPC_CONTEXT;
        }
    }

    public boolean event(PooledConnectionProxy connection) {
        if (new Date().getTime()-connection.getLastUseTime() > interval) {
            try {
                VistaRPC imHere = new VistaRPC(pingRPC);
                connection.ping(imHere, pingContext);
            } catch (RPCException ex) {
                connection.expire("Could not ping server: " + ex.getMessage());
                return false;
            }
        }
        return true;
    }
}
