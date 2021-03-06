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
package org.medsphere.vistalink;

//See VistaLinkPooledConnectionFactory for the actual implementation via proxy
import gov.va.med.vistalink.adapter.cci.VistaLinkConnection;


import com.medsphere.vistarpc.factory.RPCPooledConnection;
import javax.security.auth.login.LoginContext;

@Deprecated
public class VistaLinkPooledConnection extends VistaLinkRPCConnection implements RPCPooledConnection {

    private final VistaLinkPooledConnectionFactory factory;

    VistaLinkPooledConnection(VistaLinkConnection vlConnection, LoginContext loginContext, VistaLinkPooledConnectionFactory factory) {
        super(vlConnection, loginContext);
        this.factory = factory;
    }

    @Override
    public void returnToPool() {
        factory.acquireConnection(this);
    }

    @Override
    public void close() {
        factory.removeFromPool(this);
        super.close();
    }
}
