// <editor-fold defaultstate="collapsed">
/*
 * Copyright (C) 2011  Medsphere Systems Corporation
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
 * Profiled connection type.
 * </P>
 * Required property:
 * <ul>
 * <li><code>profiledDelegateBrokerType</code> - The broker type used to create
 * the delegate connection.</li>
 * </ul>
 */
public class ProfiledConnectionType implements ConnectionType {

    private final static String name = "Profiled";
    private static final ArrayList<String> properties = new ArrayList<String>();

    static {
        properties.add(ProfiledConnection.DELEGATE_TYPE);
    }

    @Override
    public RPCConnection createConnection(VistaSubject subject, VistaConnectionProperties vcp) throws VistaConnectionException {
        return new ProfiledConnection(subject, vcp);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Collection<String> getPropertyNames() {
        return properties;
    }
}
