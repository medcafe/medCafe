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
package org.medsphere.connection;

import com.medsphere.vistarpc.RPCConnection;
import java.util.Collection;
import org.medsphere.auth.VistaSubject;
import org.medsphere.datasource.VistaServiceLoaderDataSource;

/**
 * Interface used to specify a broker type. This is used by
 * {@link VistaServiceLoaderDataSource}.
 */
public interface ConnectionType {

    /**
     * Create a connection based on the supplied properties.
     * @param vistaSubject The subject to use when creating the connection. This
     * should be null unless a subject has been provided that <b>must</b> be
     * used.
     * @param properties Connection properties
     * @return A connection if successful, otherwise <code>throws VistaConnectionException</code>.
     * @throws VistaConnectionException 
     */
    RPCConnection createConnection(VistaSubject vistaSubject, VistaConnectionProperties properties)
            throws VistaConnectionException;

    /**
     * A human-readable name for this type of connection.
     * @return Connection type name.
     */
    public String getName();

    /**
     * Retrieve a set of commonly-used properties that can be used to
     * authenticate a connection of this type.
     * @return Names of connection properties.
     */
    public Collection<String> getPropertyNames();
}
