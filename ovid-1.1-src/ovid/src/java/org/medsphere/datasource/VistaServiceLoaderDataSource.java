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
import java.util.HashMap;
import java.util.ServiceLoader;
import org.medsphere.auth.VistaSubject;
import org.medsphere.connection.ConnectionType;
import org.medsphere.connection.VistaConnectionException;
import org.medsphere.connection.VistaConnectionProperties;

/**
 * This class uses <code>java.util.ServiceLoader</code> to locate other objects
 * which in turn can create a connection. See {@link #getConnection(org.medsphere.auth.VistaSubject, org.medsphere.connection.VistaConnectionProperties) getConnection}
 * for more details.
 * <P/>
 * To add your own connection type, perhaps a mock connection or a proxy type to
 * assist in profiling or debugging, add a file called
 * <code>META-INF/services/org.medsphere.connection.ConnectionType</code>
 * to your project. Within it, list the fully-qualified class names of
 * your implementations of {@link ConnectionType}. Your new connection type will
 * now be available just by changing the <code>brokerType</code> in the
 * application configuration.
 */
public class VistaServiceLoaderDataSource extends AbstractDataSource {

    private static final HashMap<String, ConnectionType> cache;

    static {
        cache = new HashMap<String, ConnectionType>();
        ServiceLoader<ConnectionType> connectionLoader = ServiceLoader.load(ConnectionType.class);
        for (ConnectionType connectionType : connectionLoader) {
            cache.put(connectionType.getName(), connectionType);
        }
    }

    /**
     * Constructor taking the default properties.
     * @param properties Default properties for the object. May be
     * <code>null</code>.
     */
    public VistaServiceLoaderDataSource(VistaConnectionProperties properties) {
        setProperties(properties);
    }

    /**
     * Retrieves a connection based on the merging of default properties for
     * this object with the properties supplied in the argument.
     * When both default and argument define the same property, the default
     * takes precedence.
     * <P/>
     * A connections-creation delegate is obtained by using
     * <code>java.util.ServiceLoader</code> to iterate over {@link ConnectionType}
     * instances, searching for one with a name that matches the
     * <code>brokerType</code> property. The match is then used to create the
     * connection.
     * @param subject The subject to use for authentication. May be
     * <code>null</code>.
     * @param properties Properties used for the connection attempt. May be
     * <code>null</code>. Either the default or the argument must define
     * <code>brokerType</code> property.
     * @return An RPCConnection object on success, or throws
     * VistaConnectionException.
     * @throws VistaConnectionException
     */
    @Override
    public RPCConnection getConnection(VistaSubject subject, VistaConnectionProperties properties)
            throws VistaConnectionException {
        VistaConnectionProperties mergedProperties = getMergedProperties(properties);

        String type = mergedProperties.get("brokerType");
        if (type==null) {
            throw new VistaConnectionException("brokerType is null in connection properties");
        }

        ConnectionType connectionType = cache.get(type);
        if (connectionType == null) {
            throw new VistaConnectionException("Unknown broker type: "+ type);
        }
        return connectionType.createConnection(subject, mergedProperties);
    }
}
