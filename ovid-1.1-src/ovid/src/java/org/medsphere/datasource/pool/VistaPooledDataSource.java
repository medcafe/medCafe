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

import com.medsphere.vistarpc.RPCConnection;
import org.medsphere.auth.VistaSubject;
import org.medsphere.connection.VistaConnectionException;
import org.medsphere.connection.VistaConnectionProperties;
import org.medsphere.datasource.AbstractDataSource;
import org.medsphere.datasource.VistaDataSource;

/**
 * Creates a pool pooled source of connections using a delegate source to do
 * actual pool creation.
 */
public final class VistaPooledDataSource extends AbstractDataSource {

    private VistaConnectionPool pool = null;

    /**
     * Construct a pooled data source.
     * @param delegate A data source to be used to create new connections.
     * @param properties Additional connection properties.
     * @throws VistaConnectionException
     */
    public VistaPooledDataSource(VistaDataSource delegate, VistaConnectionProperties properties)
            throws VistaConnectionException {
        this.pool = VistaConnectionPoolManager.getInstance().createPool(delegate, properties);
        if (this.pool==null) {
            throw new VistaConnectionException("Pool was not created");
        }
    }

    @Override
    public RPCConnection getConnection(VistaSubject subject, VistaConnectionProperties properties)
            throws VistaConnectionException {
        RPCConnection connection = null;
        connection = pool.getConnection(subject, properties);
        if (connection==null) {
            throw new VistaConnectionException("Could not retrieve pooled connection.");
        }
        return connection;
    }
}
