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

import com.medsphere.vistarpc.RPCConnection;
import java.util.ArrayList;
import java.util.Collection;
import org.medsphere.auth.VistaSubject;

/**
 * A mock connection type. There are no required properties.
 */
public class MockConnectionType implements ConnectionType {
    private final static String name = "Mock";
    private static final ArrayList<String> properties = new ArrayList<String>();

    public RPCConnection createConnection(VistaSubject vs, VistaConnectionProperties vcp) throws VistaConnectionException {
        return new MockConnection(vcp);
    }

    public String getName() {
        return name;
    }

    public Collection<String> getPropertyNames() {
        return properties;
    }

}
