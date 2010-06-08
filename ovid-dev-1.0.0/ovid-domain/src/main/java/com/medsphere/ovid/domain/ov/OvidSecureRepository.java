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

import org.apache.log4j.Logger;

import com.medsphere.fileman.FMUtil;
import com.medsphere.ovid.config.VistalinkProperties;
import com.medsphere.resource.ResAdapter;
import com.medsphere.rpcresadapter.RPCResAdapter;
import com.medsphere.vistalink.VistaLinkPooledConnectionFactory;
import com.medsphere.vistarpc.RPCConnection;
import com.medsphere.vistarpc.RPCException;

public abstract class OvidSecureRepository {
    private static Logger logger = Logger.getLogger(OvidSecureRepository.class);
    private RPCConnection rpcConnection;
    private RPCConnection rpcServerConnection;
    private String context = null;

    public OvidSecureRepository(RPCConnection connection) {
        this(connection, null);
    }

    public OvidSecureRepository(RPCConnection connection, RPCConnection serverConnection) {
        this.rpcConnection = connection;
        this.rpcServerConnection = serverConnection;
    }
    public RPCConnection getConnection() {
        return rpcConnection;
    }

    public RPCConnection getServerConnection() {
        return rpcServerConnection;
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

    protected ResAdapter obtainServerRPCAdapter() throws OvidDomainException {

        try {
            rpcServerConnection.setContext(FMUtil.FM_RPC_CONTEXT);
        } catch (RPCException e) {
            logger.error(e);
            throw new OvidDomainException(e);
        }
        ResAdapter adapter = new RPCResAdapter(rpcServerConnection, FMUtil.FM_RPC_NAME);
        return adapter;
    }

    public static RPCConnection getDirectVistaLinkConnection(String accessCode, String verifyCode) {
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

    public String getDUZForLastConnection() {
        return rpcConnection.getDUZ();
    }

    public static RPCConnection getDirectVistaLinkConnection(String host, String port, String accessCode, String verifyCode) {
        VistaLinkPooledConnectionFactory factory;
        try {
            factory = new VistaLinkPooledConnectionFactory(host, port, accessCode, verifyCode);

        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(OvidSecureRepository.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            System.out.println("==== Could not obtain a connection =====");
            return null;
        }
        RPCConnection connection = factory.getConnection();

        return connection;
    }

    protected boolean isEmptyResult(String[] items) {
        return (items.length==0)||((items.length==1)&&items[0].isEmpty());
    }

}
