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

package com.medsphere.ovid.acceptance;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.medsphere.vistalink.VistaLinkRPCConnection;
import com.medsphere.vistarpc.RPCBrokerConnection;
import com.medsphere.vistarpc.RPCConnection;
import com.medsphere.vistarpc.RPCException;
import com.medsphere.vistarpc.RPCResponse;
import com.medsphere.vistarpc.VistaRPC;
import com.medsphere.vistarpc.RPCResponse.ResponseType;

public class AuthByToken {

    public void testRPCBroker() throws RPCException {
        RPCConnection conn = new RPCBrokerConnection("openvista.medsphere.org", 9201, "DB1234", "DB1234!!");

        VistaRPC rpc = new VistaRPC("XUS GET TOKEN", ResponseType.SINGLE_VALUE);

        RPCResponse response = conn.execute(rpc);
        if (response == null || response.getError() != null) {
            System.err.println("ERROR: " + response.getError());
            return;
        }

        System.out.println("logging in with token " + response.getString());

        RPCConnection tokenConn = new RPCBrokerConnection("openvista.medsphere.org", 9201, response.getString());
        VistaRPC imHere = new VistaRPC("XWB IM HERE");
        response = tokenConn.execute(imHere);
        if (response == null || response.getError() != null) {
            System.err.println("ERROR: " + response.getError());
            return;
        }

        System.out.println("Success: " + response.getString());

    }

    public void testVistaLink() throws RPCException {
        RPCConnection conn = new VistaLinkRPCConnection("openvista.medsphere.org", "8002", "DB1234", "DB1234!!");

        VistaRPC rpc = new VistaRPC("XUS GET TOKEN", ResponseType.SINGLE_VALUE);

        RPCResponse response = conn.execute(rpc);
        if (response == null || response.getError() != null) {
            System.err.println("ERROR: " + response.getError());
            return;
        }

        System.out.println("logging in with token " + response.getString());

        RPCConnection tokenConn = new VistaLinkRPCConnection("openvista.medsphere.org", "8002", response.getString());
        VistaRPC imHere = new VistaRPC("XWB IM HERE");
        response = tokenConn.execute(imHere);
        if (response == null || response.getError() != null) {
            System.err.println("ERROR: " + response.getError());
            return;
        }

        System.out.println("Success: " + response.getString());

    }

    public static void main(String[] args) throws RPCException {
        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.INFO);
        AuthByToken auth = new AuthByToken();
        auth.testRPCBroker();
        auth.testVistaLink();
    }

}
