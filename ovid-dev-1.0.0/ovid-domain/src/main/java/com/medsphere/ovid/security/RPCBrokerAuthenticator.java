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

import javax.security.auth.Subject;

import org.apache.log4j.Logger;

import com.medsphere.vistarpc.RPCBrokerConnection;
import com.medsphere.vistarpc.RPCConnection;
import com.medsphere.vistarpc.RPCException;

/**
 *
 *
 */
public class RPCBrokerAuthenticator extends UserAuthenticator {
    private Logger logger = Logger.getLogger(RPCBrokerAuthenticator.class);
    private static RPCBrokerAuthenticator _instance = null;
    private RPCBrokerAuthenticator() {
    }

    public static UserAuthenticator getInstance() {
        if (_instance == null) {
            _instance = new RPCBrokerAuthenticator();
        }
        return _instance;
    }

    @Override
    public boolean authenticate(Subject subject, RPCConnectionProperties properties) {
        if (subject == null) {
            return false;
        }
        String accessCode = properties.get("vistaAccessCode");
        String verifyCode = properties.get("vistaVerifyCode");
        String token = properties.get("token");
        String server = properties.get("server");
        String port = properties.get("port");
        int portNum;
        if (server==null || server==null) {
            logger.error("Server and port not both specified");
            return false;
        }
        try {
            portNum = Integer.parseInt(port);
        } catch (NumberFormatException ex) {
            logger.error("Port " + port + " not an integer", ex);
            return false;
        }
        try {
            RPCConnection conn;
            if (token!=null) {
                conn = new RPCBrokerConnection(server, portNum, token);
            } else if (accessCode!=null && verifyCode!=null) {
                conn = new RPCBrokerConnection(server, portNum, accessCode, verifyCode);
            } else {
                logger.error("Neither complete access and verify, nor token, was supplied");
                return false;
            }
            VistaConnectionPrincipal principal = new VistaConnectionPrincipal(conn);
            subject.getPrincipals().add(principal);
        } catch (RPCException e) {
            return false;
        }
        return true;
    }

}
