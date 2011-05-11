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

package com.medsphere.fileman.validate;

import com.medsphere.vistarpc.RPCBrokerConnection;
import com.medsphere.vistarpc.RPCConnection;
import com.medsphere.vistarpc.RPCException;
import com.medsphere.vistarpc.RPCResponse;
import com.medsphere.vistarpc.VistaRPC;


public class OvidInstallTest {

    public static void main(String[] args) {
        //args = new String[] { "localhost", "9201", "OV1234", "OV1234!!" };
        if (args.length < 4) {
            System.err.println("usage: OvidInstallTest <host> <port> <access-code> <verify-code>");
            return;
        }

        RPCConnection connection = null;

        try {
            // choose which transport you want to use.... we'll go with vistalink for now.
            //connection = new VistaLinkRPCConnection(args[0], args[1], args[2], args[3]);
            connection = new RPCBrokerConnection(args[0], Integer.parseInt(args[1]), args[2], args[3]);
            VistaRPC rpc = new VistaRPC("XWB IM HERE");
            RPCResponse response = connection.execute(rpc);
            if (response != null && "1".equals(response.getString())) {
                System.out.println("Congratulations!  The OVID ping test to " + args[0] + " on port "
                                    + args[1] + " as user " + args[2] + " succeeded.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Ovid ping validation failed because: ");
            e.printStackTrace();
        } catch (RPCException e) {
            System.out.println("Ovid ping validation failed because: ");
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (RPCException e) {
                }
            }
        }
    }
}
