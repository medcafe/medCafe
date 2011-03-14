// <editor-fold defaultstate="collapsed">
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
// </editor-fold>


package com.medsphere.vistarpc.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.medsphere.vistarpc.RPCConnection;
import com.medsphere.vistarpc.RPCException;
import com.medsphere.vistarpc.RPCResponse;
import com.medsphere.vistarpc.VistaRPC;

@Deprecated
public class RPCBrokerPooledConnectionFactory extends RPCPooledConnectionFactory {

    private Logger logger = LoggerFactory.getLogger(RPCBrokerPooledConnectionFactory.class);
    private String server;
    private String port;
    private String accessCode;
    private String verifyCode;

    public RPCBrokerPooledConnectionFactory(String server, String port, String accessCode, String verifyCode) {
        logger.debug("instantiating");
        this.server = server;
        this.port = port;
        this.accessCode = accessCode;
        this.verifyCode = verifyCode;
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
                        RPCPooledConnection conn = new RPCBrokerPooledConnection(server, new Integer(port), accessCode, verifyCode, this);
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
            RPCResponse response = rpcConn.execute(rpc);
            if (response != null) {
                logger.debug("RESPONSE: [error] " + response.getError() + " : [type] " + response.getResponseType() + " : [string] " +  response.getString());
            } else {
                logger.debug("RESPONSE IS NULL -- closing");
            }
            if (response == null || response.getError() != null || !"1".equals(response.getString())) {
                connection.close();
                return false;
            }
            return true;
        } catch (RPCException e) {
            try {
                connection.close();
            } catch (RPCException e1) {
                e1.printStackTrace();
            }
            return false;
        }

    }

}
