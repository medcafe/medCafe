// <editor-fold defaultstate="collapsed">
/*
 * Copyright (C) 2010  Medsphere Systems Corporation
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
package org.medsphere.datasource.pool;

import com.medsphere.vistarpc.InvalidConnection;
import com.medsphere.vistarpc.RPCConnection;
import com.medsphere.vistarpc.RPCException;
import com.medsphere.vistarpc.RPCResponse;
import com.medsphere.vistarpc.VistaRPC;
import java.util.Date;
import org.medsphere.connection.ProxyConnection;

final class PooledConnectionProxy extends ProxyConnection {

    private long lastUseTime; // includes pings
    private long lastExecuteTime; // last time the user made a call
    boolean expired = false;
    String lastContext;
    VistaConnectionPool pool;

    public PooledConnectionProxy(RPCConnection child, VistaConnectionPool pool) {
        super(child);
        this.pool = pool;
        markExecuted();
    }

    PooledConnectionProxy( PooledConnectionProxy other ) {
        super(other.getDelegate());
        this.pool = other.pool;
        this.lastExecuteTime = other.lastExecuteTime;
        this.lastUseTime = other.lastUseTime;
    }

    @Override
    public void close() throws RPCException {
        if (!expired) {
            pool.returnConnection(new PooledConnectionProxy(this));
            setDelegate( new InvalidConnection("Connection was closed") );
        }
    }

    @Override
    public synchronized void setContext(String context) throws RPCException {
        lastContext = context;
        super.setContext(context);
    }

    @Override
    public RPCResponse execute(VistaRPC rpc) throws RPCException {
        markExecuted();
        return super.execute(rpc);
    }

    @Override
    public synchronized RPCResponse execute(VistaRPC rpc, String context) throws RPCException {
        lastContext = context;
        markExecuted();
        return executeInternal(rpc, context);
    }

    synchronized RPCResponse ping(VistaRPC rpc, String context) throws RPCException {
        markUsed();
        try {
            return executeInternal(rpc, context);
        } finally {
            setContext(lastContext);
        }
    }

    private RPCResponse executeInternal(VistaRPC rpc, String context) throws RPCException {
        return super.execute(rpc, context);
    }

    private void markExecuted() {
        lastExecuteTime = new Date().getTime();
        markUsed();
    }

    private void markUsed() {
        lastUseTime = new Date().getTime();
    }

    void shutdown() throws RPCException {
        if (!expired) {
            super.close();
        }
    }

    public void expire(String reason) {
        expired = true;
        setDelegate( new InvalidConnection(reason) );
    }

    public long getLastExecuteTime() {
        return lastExecuteTime;
    }

    public long getLastUseTime() {
        return lastUseTime;
    }

}
