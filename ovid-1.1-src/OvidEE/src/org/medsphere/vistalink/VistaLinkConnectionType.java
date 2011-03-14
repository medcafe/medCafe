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
package org.medsphere.vistalink;

import com.medsphere.vistarpc.RPCConnection;
import com.medsphere.vistarpc.RPCException;
import java.util.ArrayList;
import java.util.Collection;
import javax.security.auth.Subject;
import org.medsphere.auth.VistaSubject;
import org.medsphere.connection.ConnectionType;
import org.medsphere.connection.VistaConnectionException;
import org.medsphere.connection.VistaConnectionProperties;

/**
 * VistaLink connection type.
 * </P>
 * Requires properties:
 * <ul>
 * <li><code>server</code> - The TCP/IP address or name of the Vista server.</li>
 * <li><code>port</code> - The port on which the Vista broker is listening.</li>
 * <li><code>jaasVistaLinkName</code> - The JAAS name.</li>
 * </ul>
 * And either
 * <ul>
 * <li><code>accessCode</code> - The Vista access code.</li>
 * <li><code>verifyCode</code> - The Vista verify code.</li>
 * </ul>
 * or
 * <ul>
 * <li><code>token</code> - Mutually exclusive with access/verify, used for SSO.</li>
 * </ul>
 */
public class VistaLinkConnectionType implements ConnectionType {

    static private final ArrayList<String> properties;

    static {
        properties = new ArrayList<String>();
        properties.add("jaasVistaLinkName");
        properties.add("accessCode");
        properties.add("verifyCode");
    }

    public RPCConnection createConnection(VistaSubject vistaSubject, VistaConnectionProperties properties)
            throws VistaConnectionException {
        try {
            String jaasName = properties.get("jaasVistaLinkName");
            String accessCode = properties.get("accessCode");
            String verifyCode = properties.get("verifyCode");
            String token = properties.get("token");
            if (jaasName == null || (token == null && (accessCode == null || verifyCode == null))) {
                throw new VistaConnectionException("Connection parameters were not been fully specified.");
            }
            Subject subject;
            if (vistaSubject == null) {
                subject = new Subject();
            } else {
                subject = vistaSubject.getSubject();
            }
            return new VistaLinkRPCConnection(jaasName, accessCode, verifyCode, token, subject);
        } catch (RPCException ex) {
            throw new VistaConnectionException(ex.getMessage(), ex);
        }
    }

    public String getName() {
        return "VistaLink";
    }

    public Collection<String> getPropertyNames() {
        return properties;
    }
}
