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
package org.medsphere.auth;

import com.medsphere.vistarpc.RPCConnection;
import com.medsphere.vistarpc.RPCException;
import java.security.Principal;

class VistaConnectionPrincipal implements Principal {

    private PrincipalConnection connection = null;

    public VistaConnectionPrincipal(RPCConnection connection) {
        this.connection = new PrincipalConnection( connection );
    }

    public RPCConnection getAuthenticatedConnection() {
        return connection;
    }

    @Override
    public String getName() {
        return connection.getDUZ();
    }

    public void dispose() throws RPCException {
        connection.dispose();
    }
}
