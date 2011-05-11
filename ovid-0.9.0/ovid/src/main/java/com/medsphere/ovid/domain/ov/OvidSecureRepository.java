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

package com.medsphere.ovid.domain.ov;

import com.medsphere.fileman.FMUtil;
import com.medsphere.ovid.config.VistalinkProperties;
import com.medsphere.ovid.connection.OvidVistaLinkConnectionException;
import com.medsphere.resource.ResAdapter;
import com.medsphere.rpcresadapter.RPCResAdapter;
import com.medsphere.vistalink.VistaLinkPooledConnection;
import com.medsphere.vistalink.VistaLinkPooledConnectionFactory;
import com.medsphere.vistalink.VistaLinkRPCConnection;
import com.medsphere.vistarpc.RPCConnection;
import com.medsphere.vistarpc.RPCException;
import gov.va.med.exception.FoundationsException;
import gov.va.med.vistalink.adapter.cci.VistaLinkConnection;
import org.apache.log4j.Logger;

public abstract class OvidSecureRepository {
    private static Logger logger = Logger.getLogger(OvidSecureRepository.class);
    private VistaLinkConnection vlConnection;
    private String context = null;

    public OvidSecureRepository(VistaLinkConnection connection) {
        vlConnection = connection;
    }

    public VistaLinkConnection getConnection() throws OvidVistaLinkConnectionException {
        return vlConnection;
    }

    public String getContext() {
        if (context==null) {
            context = FMUtil.FM_RPC_CONTEXT;
        }
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    protected ResAdapter obtainVistaLinkAdapter() throws OvidVistaLinkConnectionException {
        RPCConnection connection = new VistaLinkRPCConnection(getConnection());
        try {
            connection.setContext(FMUtil.FM_RPC_CONTEXT);
        } catch (RPCException e) {
            logger.error(e);
            throw new OvidVistaLinkConnectionException(e);
        }
        ResAdapter adapter = new RPCResAdapter(connection, FMUtil.FM_RPC_NAME);
        return adapter;
    }

    public static VistaLinkPooledConnection getDirectConnection(String accessCode, String verifyCode) {
        String port = VistalinkProperties.getProperties().getProperty("vistalink.port", "");
        String host = VistalinkProperties.getProperties().getProperty("vistalink.address", "");

        VistaLinkPooledConnectionFactory factory;
        try {
            factory = new VistaLinkPooledConnectionFactory(host, port, accessCode, verifyCode);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(OvidSecureRepository.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            System.out.println("==== Could not obtain a connection =====");
            return null;
        }
        return factory.getConnection();
    }

    private static String duz = null;
    public static String getDUZForLastConnection() {
        return duz;
    }
    
    public static VistaLinkPooledConnection getDirectConnection(String host, String port, String accessCode, String verifyCode) {
        VistaLinkPooledConnectionFactory factory;
        try {
            factory = new VistaLinkPooledConnectionFactory(host, port, accessCode, verifyCode);

        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(OvidSecureRepository.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            System.out.println("==== Could not obtain a connection =====");
            return null;
        }
        VistaLinkPooledConnection connection = factory.getConnection();
        try {
            duz = factory.getDUZForConnection(connection);
        } catch (FoundationsException ex) {
            logger.error(ex);
            connection = null;
        }
        return connection;

    }
}
