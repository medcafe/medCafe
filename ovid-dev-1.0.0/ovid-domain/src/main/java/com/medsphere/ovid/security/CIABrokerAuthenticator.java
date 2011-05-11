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

package com.medsphere.ovid.security;

import com.medsphere.cia.CIABrokerConnection;
import com.medsphere.vistarpc.RPCConnection;
import com.medsphere.vistarpc.RPCException;

import javax.security.auth.Subject;
import org.apache.log4j.Logger;

public class CIABrokerAuthenticator extends UserAuthenticator {
    private Logger logger = Logger.getLogger(RPCBrokerAuthenticator.class);
    private static CIABrokerAuthenticator _instance = null;

    public static UserAuthenticator getInstance() {
        if (_instance == null) {
            _instance = new CIABrokerAuthenticator();
        }
        return _instance;
    }

    @Override
    public boolean authenticate(Subject subject, RPCConnectionProperties properties) {
        String server = properties.get("server");
        String port = properties.get("port");
        String accessCode = properties.get("vistaAccessCode");
        String verifyCode = properties.get("vistaVerifyCode");
        String token = properties.get("token");
        String uci = properties.get("uci");
        if ((token==null && (accessCode==null || verifyCode==null)) || server==null || port==null || uci==null) {
            logger.error("Did not get all connection properties. CIA Broker requires server, port, uci, vistaAccessCode and vistaVerifyCode");
            return false;
        }
        int portNum;
        try {
            portNum = Integer.parseInt(port);
        } catch (NumberFormatException ex) {
            logger.error("Port number was not an integer");
            return false;
        }
        RPCConnection conn;
        try {
            conn = new CIABrokerConnection(server, portNum, accessCode, verifyCode, token, uci);
        } catch (RPCException ex) {
            logger.debug("Failed to create the connection: " + ex.toString());
            return false;
        }
        VistaConnectionPrincipal principal = new VistaConnectionPrincipal(conn);
        subject.getPrincipals().add(principal);
        return true;
    }

}
