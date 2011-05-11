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

package com.medsphere.vistarpc.factory;

import com.medsphere.vistarpc.RPCBrokerConnection;
import org.apache.log4j.Logger;

import com.medsphere.vistarpc.RPCException;

public class RPCBrokerPooledConnection extends RPCBrokerConnection implements RPCPooledConnection {
   private final RPCBrokerPooledConnectionFactory source;

    private Logger logger = Logger.getLogger(RPCBrokerPooledConnection.class);
    RPCBrokerPooledConnection(String server, int port, String access, String verify, RPCBrokerPooledConnectionFactory source) throws RPCException {
        super( server, port, access, verify );
        this.source = source;
        logger.debug("instantiated " + this);
    }

    @Override
    public void close() {
        source.removeFromPool(this);
        try {
            super.close();
        } catch (RPCException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void returnToPool() {
        source.acquireConnection(this);
    }

}
