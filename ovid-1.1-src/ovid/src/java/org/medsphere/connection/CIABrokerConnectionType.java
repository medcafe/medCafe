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

import com.medsphere.cia.CIABrokerConnection;
import com.medsphere.vistarpc.RPCConnection;
import com.medsphere.vistarpc.RPCException;
import java.util.ArrayList;
import java.util.Collection;
import org.medsphere.auth.VistaSubject;

/**
 * CIA Broker connection type.
 * </P>
 * Requires properties:
 * <ul>
 * <li><code>server</code> - The TCP/IP address or name of the Vista server.</li>
 * <li><code>port</code> - The port on which the Vista broker is listening.</li>
 * <li><code>uci</code> - The UCI.</li>
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
public class CIABrokerConnectionType implements ConnectionType {

    static private final ArrayList<String> properties;

    static {
        properties = new ArrayList<String>();
        properties.add("server");
        properties.add("port");
        properties.add("accessCode");
        properties.add("verifyCode");
        properties.add("uci");
    }

    public RPCConnection createConnection(VistaSubject vistaSubject, VistaConnectionProperties properties)
            throws VistaConnectionException {
        String server = properties.get("server");
        String port = properties.get("port");
        String accessCode = properties.get("accessCode");
        String verifyCode = properties.get("verifyCode");
        String token = properties.get("token");
        String uci = properties.get("uci");
        if ((token == null && (accessCode == null || verifyCode == null)) || server == null || port == null || uci == null) {
            throw new VistaConnectionException("CIA Broker connection parameters were not fully specified.");
        }
        int portNum;
        try {
            portNum = Integer.parseInt(port);
        } catch (NumberFormatException ex) {
            return null;
        }
        RPCConnection conn;
        try {
            conn = new CIABrokerConnection(server, portNum, accessCode, verifyCode, token, uci);
        } catch (RPCException ex) {
            return null;
        }
        return conn;
    }

    public String getName() {
        return "CIA Broker";
    }

    public Collection<String> getPropertyNames() {
        return properties;
    }
}
