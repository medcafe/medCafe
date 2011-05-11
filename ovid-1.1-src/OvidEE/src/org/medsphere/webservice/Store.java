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
package org.medsphere.webservice;

import com.medsphere.vistarpc.RPCConnection;
import com.medsphere.vistarpc.RPCException;
import org.medsphere.auth.VistaSubject;
import org.medsphere.connection.VistaConnectionException;
import org.medsphere.datasource.VistaDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a Vista instance, and is responsible for providing connections to
 * it. User connections are retrieved from a thread-local VistaSubject that has
 * been set by the calling class before connections are requested. The server
 * connections, though, are entirely maintained by this class.
 */
public class Store {

    private ThreadLocal<VistaSubject> subject = new ThreadLocal<VistaSubject>();
    private final VistaDataSource serverDataSource;
    private Logger logger = LoggerFactory.getLogger(Store.class);

    /**
     * Constructor
     * @param serverDataSource The data source used to retrieve server
     * connections.
     */
    public Store(VistaDataSource serverDataSource) {
        this.serverDataSource = serverDataSource;
    }

    /**
     * Convenience method for use by child classes to close multiple
     * connections at once. When the calling class is done with any connections
     * retrieved from this store, it should close them all. User connections
     * will remain open since they are associated with an authenticated
     * subject. Server connections are most-likely pooled and will return to the
     * pool.
     * @param connections Variable argument list of connections that will be
     * closed.
     */
    protected void closeConnections(RPCConnection... connections) {
        for (RPCConnection connection : connections) {
            if (connection != null) {
                try {
                    connection.close();
                } catch (RPCException ex) {
                    logger.error("Exception closing connection", ex);
                }
            }
        }
    }

    /**
     * Returns a connection authenticated to the current subject. It is assumed
     * that the subject has already been authenticated.
     * @return A connection authenticated to the current subject.
     */
    protected RPCConnection getUserConnection() {
        return subject.get().getVistaConnection();
    }

    /**
     * Returns a connection authenticated to the server data source.
     * @return A connection authenticated to the server data source.
     * @throws VistaConnectionException If a connection can not be retrieved
     * from the data source, this exception is throw.
     */
    protected RPCConnection getServerConnection() throws VistaConnectionException {
        return serverDataSource.getConnection();
    }

    /**
     * Set the thread-local variable containing a vista subject. It is assumed
     * that the subject has already been authenticated.
     * @param subject A vista subject.
     */
    public void setSubject(VistaSubject subject) {
        this.subject.set(subject);
    }
}
