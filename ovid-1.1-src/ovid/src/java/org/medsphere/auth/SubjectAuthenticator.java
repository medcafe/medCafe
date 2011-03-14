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
package org.medsphere.auth;

import org.medsphere.connection.VistaConnectionProperties;
import org.medsphere.connection.VistaConnectionException;
import org.medsphere.datasource.ServiceLocator;
import org.medsphere.datasource.VistaDataSource;

/**
 * Manages user authentication. This class attempt to make a connection to
 * Vista, and if successful it associates the specific subject with the
 * connection.
 */
public class SubjectAuthenticator {

    private static SubjectAuthenticator instance = null;

    private SubjectAuthenticator() {
    }

    /**
     * Gets singleton instance of this class.
     * @return Instance of <code>SubjectAuthenticator</code>
     */
    public static synchronized SubjectAuthenticator getInstance() {
        if (instance == null) {
            instance = new SubjectAuthenticator();
        }
        return instance;
    }

    /**
     * Checks if a subject has been authenticated.
     * @param vistaSubject The subject to authenticate.
     * @return <code>true</code> is the subject is authenticated.
     */
    public boolean isAuthenticated(VistaSubject vistaSubject) {
        return vistaSubject.getVistaConnection() != null;
    }

    /**
     * Attempts to authenticated a subject. See {@link ServiceLocator} for a
     * list of common properties. Also note that if property
     * <code>dataSourceName</code> is supplied, indicating that JNDI should be
     * used to supply properties as well, the properties are merged. The
     * JNDI-specified properties take precedence if both sources declare the
     * same property.
     * <p/>
     * If the subject is already authenticated, this acts as a no-op and returns
     * <code>true</code>.
     * @param vistaSubject The subject to authenticate.
     * @param properties The properties to use for this connection attempt.
     * @return <code>true</code> if authentication succeeded or if the subject
     * was already authenticated, otherwise <code>false</code>.
     * @throws VistaConnectionException
     */
    public boolean authenticate(VistaSubject vistaSubject, VistaConnectionProperties properties)
            throws VistaConnectionException {
        if (isAuthenticated(vistaSubject)) {
            return true;
        }
        return authenticate(vistaSubject, ServiceLocator.getInstance().getDataSource(properties), properties);
    }

      /**
     * Attempts to authenticated a subject. See {@link ServiceLocator} for a
     * list of common properties. Also note that if property
     * <code>dataSourceName</code> is supplied, indicating that JNDI should be
     * used to supply properties as well, the properties are merged. The
     * JNDI-specified properties take precedence if both sources declare the
     * same property.
     * <p/>
     * If the subject is already authenticated, this acts as a no-op and returns
     * <code>true</code>.
     * @param vistaSubject The subject to authenticate.
     * @param dataSource The datasource to use for validation
     * @param properties The properties to use for this connection attempt.
     * @return <code>true</code> if authentication succeeded or if the subject
     * was already authenticated.
     * @throws NullPointerException If dataSource is null
     * @throws VistaConnectionException If connection can not be established
     */
    public boolean authenticate(VistaSubject vistaSubject, VistaDataSource dataSource, VistaConnectionProperties properties)
            throws VistaConnectionException {
        if (isAuthenticated(vistaSubject)) {
            return true;
        }

        // If we can make a connection, then we must be authenticated.
        // We are surely going to need the connection, so save it in the
        // principal, and put the principal in the subject.

        vistaSubject.setConnection(dataSource.getConnection(vistaSubject, properties));
        return true;
    }
}
