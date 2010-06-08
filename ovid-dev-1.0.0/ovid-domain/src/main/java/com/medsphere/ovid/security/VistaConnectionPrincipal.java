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
package com.medsphere.ovid.security;

import java.security.Principal;

import com.medsphere.vistarpc.RPCConnection;

public class VistaConnectionPrincipal implements Principal {

    private RPCConnection connection = null;

    public VistaConnectionPrincipal() {}

    public VistaConnectionPrincipal(RPCConnection connection) {
        this.connection = connection;
    }

    public RPCConnection getAuthenticatedConnection() {
        return connection;
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

}
