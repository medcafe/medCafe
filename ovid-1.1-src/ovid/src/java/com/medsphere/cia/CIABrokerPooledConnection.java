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

package com.medsphere.cia;

import com.medsphere.vistarpc.RPCException;
import com.medsphere.vistarpc.factory.RPCPooledConnectionFactory;
import com.medsphere.vistarpc.factory.RPCPooledConnection;

@Deprecated
public class CIABrokerPooledConnection extends CIABrokerConnection implements RPCPooledConnection {
    private final RPCPooledConnectionFactory source;

    public CIABrokerPooledConnection(String server, int port, String access, String verify, String token, String uci, RPCPooledConnectionFactory source) throws RPCException {
        super( server, port, access, verify, token, uci );
        this.source = source;
    }

    @Override
    public void returnToPool() {
        source.acquireConnection( this );
    }

    @Override
    public void close() throws RPCException {
        source.removeFromPool(this);
        super.close();
    }

}
