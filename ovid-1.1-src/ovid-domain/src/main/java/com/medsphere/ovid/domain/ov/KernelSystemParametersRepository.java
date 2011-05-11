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
package com.medsphere.ovid.domain.ov;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.medsphere.fileman.FMRecord;
import com.medsphere.fileman.FMRetrieve;
import com.medsphere.fileman.FMField.FIELDTYPE;
import com.medsphere.resource.ResAdapter;
import com.medsphere.resource.ResException;
import com.medsphere.vistarpc.RPCBrokerConnection;
import com.medsphere.vistarpc.RPCConnection;
import com.medsphere.vistarpc.RPCException;
import java.util.logging.Level;

public class KernelSystemParametersRepository extends OvidSecureRepository {
    private Logger logger = LoggerFactory.getLogger(KernelSystemParametersRepository.class);
    private static PlatformType platformType = null;

    public KernelSystemParametersRepository(RPCConnection serverConnection) {
        super(null, serverConnection);

    }

    /**
     * Map the implementation to a type
     * @return
     */
    public PlatformType getPlatformType() {
        if (platformType == null) {
            PlatformType returnType = PlatformType.UNKNOWN;
            String platform = getPlatformText();
            if (platform.startsWith("V")) {
                returnType = PlatformType.VISTA;
            } else if (platform.startsWith("O")) {
                returnType = PlatformType.OPENVISTA;
            } else if (platform.startsWith("E")) {
                returnType = PlatformType.WORLDVISTA;
            } else if (platform.startsWith("I")) {
                returnType = PlatformType.RPMS;
            }
            platformType = returnType;
        }
        return platformType;
    }

    /**
     * Determine the vista implementation:
     * V is Vista
     * O is OpenVista
     * E is WorldVista
     * I is RPMS
     * @return
     */    
    public String getPlatformText() {
        String platform = "";
        try {
            ResAdapter adapter = obtainServerRPCAdapter();
            FMRecord kernelSystemParameters = new FMRecord("8989.3");
            kernelSystemParameters.setIEN("1");
            FMRetrieve query = new FMRetrieve(adapter);
            query.setRecord(kernelSystemParameters);
            query.addField("9", FIELDTYPE.SET_OF_CODES); // agency code
            query.execute();
            return kernelSystemParameters.getValue("9");
        } catch (ResException e) {
            logger.error("Resource exception", e);
        } catch (OvidDomainException e) {
            logger.error("Domain exception", e);
        } finally {

        }
        return platform;
    }


    public static void main(String[] args) {
        //BasicConfigurator.configure();
        //Logger.getRootLogger().setLevel(Level.ERROR);
        RPCConnection userConn = null;
        RPCConnection serverConn = null;

        try {

            if (args == null || args.length == 0) {
                args = new String[] { "localhost", "9201", "PU1234", "PU1234!!", "OV1234", "OV1234!!"};
            }

            if (args.length < 5) {
                System.err.println("usage: ImmunizationRepository <host> <port> <user-access-code> <user-verify-code> <ovid-access-code> <ovid-verify-code>");
                return;
            }

            userConn = new RPCBrokerConnection(args[0], new Integer(args[1]), args[2], args[3]);
            serverConn = new RPCBrokerConnection(args[0], new Integer(args[1]), args[4], args[5]);
            if (serverConn==null || userConn==null) {
                return;
            }

            KernelSystemParametersRepository repo = new KernelSystemParametersRepository(serverConn);
            System.out.println(repo.getPlatformType());
            KernelSystemParametersRepository repo2 = new KernelSystemParametersRepository(serverConn);
            System.out.println(repo2.getPlatformType());
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (RPCException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (userConn != null) {
                try {
                    userConn.close();
                } catch (RPCException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            if (serverConn != null) {
                try {
                    serverConn.close();
                } catch (RPCException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        System.exit(0);


    }
}
