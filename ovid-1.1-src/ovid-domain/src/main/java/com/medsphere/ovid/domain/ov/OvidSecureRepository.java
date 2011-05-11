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


package com.medsphere.ovid.domain.ov;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.medsphere.fileman.FMUtil;
import com.medsphere.resource.ResAdapter;
import com.medsphere.rpcresadapter.RPCResAdapter;
import com.medsphere.vistarpc.RPCConnection;
import com.medsphere.vistarpc.RPCException;

public abstract class OvidSecureRepository {
    private static Logger logger = LoggerFactory.getLogger(OvidSecureRepository.class);
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
            logger.error("Resource exception", e);
            throw new OvidDomainException(e);
        }
        ResAdapter adapter = new RPCResAdapter(rpcServerConnection, FMUtil.FM_RPC_NAME);
        return adapter;
    }

    public String getDUZForLastConnection() {
        return rpcConnection.getDUZ();
    }

    protected boolean isEmptyResult(String[] items) {
        return (items.length==0)||((items.length==1)&&items[0].isEmpty());
    }

}
