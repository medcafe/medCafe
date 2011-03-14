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
package org.medsphere.datasource;

import com.medsphere.vistarpc.RPCConnection;
import org.medsphere.auth.VistaSubject;
import org.medsphere.connection.VistaConnectionException;
import org.medsphere.connection.VistaConnectionProperties;

/**
 * Interface for objects that can act as a source for <code>RPCConnection</code>
 * objects.
 */
public interface VistaDataSource {

    /**
     * Retrieves a connection based on the default properties for this object.
     * Most likely, the defaults have been supplied in the implementing
     * class's constructor.
     * @return An RPCConnection object on success, throws
     * VistaConnectionException otherwise.
     * @throws VistaConnectionException
     */
    public RPCConnection getConnection() throws VistaConnectionException;

    /**
     * Retrieves a connection based on the merging of default properties for
     * this object with the properties supplied in the argument. Most likely,
     * the defaults have been supplied in the implementing class's constructor.
     * When both default and argument define the same property, the default
     * takes precedence.
     * @param properties Properties used for the connection attempt. May be
     * <code>null</code>.
     * @return An RPCConnection object on success, <null> otherwise.
     * @throws VistaConnectionException
     */
    public RPCConnection getConnection(VistaConnectionProperties properties) throws VistaConnectionException;

    /**
     * Retrieves a connection based on the default properties for this object.
     * Most likely, the defaults have been supplied in the implementing
     * class's constructor.
     *
     * @param subject The subject to use for authentication. May be
     * <code>null</code>.
     * @return An RPCConnection object on success, <code>null</code> otherwise.
     * @throws VistaConnectionException
     */
    public RPCConnection getConnection(VistaSubject subject) throws VistaConnectionException;

    /**
     * Retrieves a connection based on the merging of default properties for
     * this object with the properties supplied in the argument. Most likely,
     * the defaults have been supplied in the implementing class's constructor.
     * When both default and argument define the same property, the default
     * takes precedence.
     * @param subject The subject to use for authentication. May be
     * <code>null</code>.
     * @param properties Properties used for the connection attempt. May be
     * <code>null</code>.
     * @return An RPCConnection object on success, or throws
     * VistaConnectionException otherwise.
     * @throws VistaConnectionException
     */
    public RPCConnection getConnection(VistaSubject subject, VistaConnectionProperties properties) throws VistaConnectionException;
}
