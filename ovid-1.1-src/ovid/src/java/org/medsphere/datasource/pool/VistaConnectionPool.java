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

import org.medsphere.pool.PoolEventCallback;
import org.medsphere.pool.GenericPool;
import com.medsphere.vistarpc.RPCConnection;
import com.medsphere.vistarpc.RPCException;
import org.medsphere.auth.VistaSubject;
import org.medsphere.connection.VistaConnectionException;
import org.medsphere.connection.VistaConnectionProperties;
import org.medsphere.datasource.VistaDataSource;

class VistaConnectionPool {

    private boolean isShutdown = false;
    private final VistaDataSource dataSource;
    private final GenericPool<PooledConnectionProxy> pool;
    private long GRANULARITY = 4;

    public VistaConnectionPool(VistaDataSource dataSource, VistaConnectionProperties properties)
            throws VistaConnectionException {
        this.dataSource = dataSource;
        pool = new GenericPool<PooledConnectionProxy>();
        setShutdownCallback();
        setPing(properties);
        setTimeout(properties);
    }

    RPCConnection getConnection(VistaSubject subject, VistaConnectionProperties properties)
            throws VistaConnectionException {
        PooledConnectionProxy connection = null;
        if (!isShutdown) {
            connection = pool.getObject();
            if (connection == null) {
                // We can't pass the subject in since the subject should not be changed
                // as a result of grabbing a common object.
                RPCConnection newConnection = dataSource.getConnection(properties);
                if (newConnection != null) {
                    connection = new PooledConnectionProxy(newConnection, this);
                    pool.addObject(connection);
                }
            }
        } else {
            throw new VistaConnectionException("Pool has been shut down");
        }
        return connection;
    }

    void returnConnection(PooledConnectionProxy connection) {
        if (!isShutdown) {
            pool.returnObject(connection);
        }
    }

    void shutdown() {
        isShutdown = true;
        pool.shutdown();
    }

    private void setPing(VistaConnectionProperties properties) throws VistaConnectionException {
        long interval = getLongProperty(properties, "pingInterval");
        if (interval <= 0) {
            return;
        }
        long timeslice = interval / GRANULARITY;
        PoolEventCallback<PooledConnectionProxy> callback = new PingCallback(properties, interval-timeslice);
        pool.addTimerCallback(callback, timeslice, false);
    }

    private void setTimeout(VistaConnectionProperties properties) throws VistaConnectionException {
        long interval = getLongProperty(properties, "timeoutInterval");
        if (interval <= 0) {
            return;
        }
        long timeslice = interval / GRANULARITY;
        PoolEventCallback<PooledConnectionProxy> callback = new TimeoutCallback(interval-timeslice);
        pool.addTimerCallback(callback, timeslice, true);
    }

    private long getLongProperty(VistaConnectionProperties properties, String propname)
            throws VistaConnectionException {
        String intString = properties.get(propname);
        if (intString == null) {
            return -1;
        }
        try {
            return Long.parseLong(intString);
        } catch (NumberFormatException ex) {
            throw new VistaConnectionException(propname + " is not a numeric value", ex);
        }
    }

    private void setShutdownCallback() {
        PoolEventCallback<PooledConnectionProxy> callback =
                new PoolEventCallback<PooledConnectionProxy>() {

                    public boolean event(PooledConnectionProxy connection) {
                        try {
                            connection.shutdown();
                        } catch (RPCException ex) {
                            // Do nothing, we shutting down anyway
                        }
                        return true;
                    }
                };
        pool.addShutdownCallback(callback);
    }
}
