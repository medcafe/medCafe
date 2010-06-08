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

import com.medsphere.vistarpc.RPCConnection;
import com.medsphere.vistarpc.RPCException;
import com.medsphere.vistarpc.VistaRPC;
import com.medsphere.vistarpc.factory.RPCPooledConnectionFactory;
import com.medsphere.vistarpc.factory.RPCPooledConnection;
import org.apache.log4j.Logger;

public class CIABrokerPooledConnectionFactory extends RPCPooledConnectionFactory {

    private Logger logger = Logger.getLogger(CIABrokerPooledConnectionFactory.class);
    private final String server;
    private final int port;
    private final String accessCode;
    private final String verifyCode;
    private final String token;
    private final String uci;

    public  CIABrokerPooledConnectionFactory(String server, int port, String access, String verify, String token, String uci) {
        this.server = server;
        this.port = port;
        this.accessCode = access;
        this.verifyCode = verify;
        this.token = token;
        this.uci = uci;
    }

    @Override
    public RPCPooledConnection getConnection() {
        logger.debug("getConnection()");
        RPCPooledConnection retVal = null;
        synchronized (available) {
            try {
                boolean connectionIsUsable = false;
                while (!connectionIsUsable) {
                    if (available.peek() == null) {
                        logger.debug("no connections in pool... making new connection");
                        RPCPooledConnection conn;
                        conn = new CIABrokerPooledConnection(server, port, accessCode, verifyCode, token, uci, this);
                        available.offer(conn);
                    }
                    retVal = available.remove();
                    if (validateConnection(retVal)) {
                        logger.debug("found good connection");
                        inUse.add(retVal);
                        connectionIsUsable = true;
                    }
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (RPCException e) {
                e.printStackTrace();
            } finally {

            }
        }
        return retVal;
    }

    private boolean validateConnection(RPCConnection connection) {
        if (connection == null) {
            return false;
        }
        try {
            RPCConnection rpcConn = connection;
            VistaRPC rpc = new VistaRPC("XWB IM HERE");
            rpcConn.execute(rpc);
        } catch (RPCException e) {
            // Since we may not be authorized to execute XWB IM HERE, a
            // non-networked failure is OK.
            if (e.getMessage().startsWith("IO error")) {
                try {
                    connection.close();
                } catch (RPCException e1) {
                    e1.printStackTrace();
                }
                return false;
            }
        }
        return true;
    }
}
