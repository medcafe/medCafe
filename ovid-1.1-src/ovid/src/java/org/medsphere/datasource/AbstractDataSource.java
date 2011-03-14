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
 * Base class for implementations of {@link VistaDataSource}. This implements
 * some convenience methods and handles default properties.
 *
 */
abstract public class AbstractDataSource implements VistaDataSource {

    private VistaConnectionProperties defaultProperties = new VistaConnectionProperties();

    public RPCConnection getConnection() throws VistaConnectionException {
        return getConnection(null, null);
    }

    public RPCConnection getConnection(VistaSubject subject) throws VistaConnectionException {
        return getConnection(subject, null);
    }

    public RPCConnection getConnection(VistaConnectionProperties properties) throws VistaConnectionException {
        return getConnection(null, properties);
    }

    /**
     * Sets the default properties
     * @param properties The properties to use as defaults. The values are
     * copied into the object, so later changes to the collection have no effect
     * on the defaults once they are set.
     */
    final protected void setProperties(VistaConnectionProperties properties) {
        if (properties != null) {
            defaultProperties.putAll(properties);
        }
    }

    /**
     * Merges the properties provided with the default properties. The default
     * properties have precedence.
     * @param properties The properties to merge with the default.
     * @return A new VistaConnectionProperties containing the merge results.
     */
    protected VistaConnectionProperties getMergedProperties(VistaConnectionProperties properties) {
        VistaConnectionProperties mergedProperties = new VistaConnectionProperties(properties);
        mergedProperties.putAll(defaultProperties);
        return mergedProperties;
    }
}
