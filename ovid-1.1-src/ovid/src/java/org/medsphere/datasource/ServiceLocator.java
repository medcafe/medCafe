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

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.medsphere.connection.VistaConnectionException;
import org.medsphere.connection.VistaConnectionProperties;

/**
 * This class is used to provide {@link VistaDataSource} objects based on
 * supplied properties. The properties commonly used by
 * <code>ServiceLocator</code> are:
 * <ul>
 * <li><code>dataSource</code> - The fully qualified name for a class
 * implementing {@link VistaDataSource}. The default is the full name for
 * {@link VistaServiceLoaderDataSource}.
 * </li>
 * <li><code>brokerType</code> - When the <code>dataSource</code> is
 * <code>VistaServiceLoaderDataSource</code>, this property is used to determine
 * what the kind of connection to make. Some valid values are "RPC Broker",
 * "VistaLink", and "CIA Broker". See {@link VistaServiceLoaderDataSource} for
 * information on how to add your own types.</li>
 * <li><code>dataSourceName</code> - Optional. The name of the JNDI that
 * contains additional properties. The properties configured in the JNDI
 * configuration are merged with the ones provided to
 * <code>getDataSource</code>. In case of conflicting declarations for the same
 * property, the JNDI configuration takes precedence.</li>
 * <li><code>vistaDataSourceFactory</code> - Optional. The fully-qualified name
 * of the VistaDataSourceFactory implementation that will be used to produce the
 * VistaDataSource. Specify this when you want to use a custom factory with
 * custom behavior, such as ignoring certain properties or providing defaults
 * for others.</li>
 * <li><code>poolName</code> - If specified, this property will make the
 * returned by <code>getDataSource</code> a pooled data source. The first data
 * source to use a pool of a given name will be the data source for all further
 * connections from that pool. This means that for a pooled data source, the
 * supplied properties for subsequent data source creation using that pool name
 * may not be applied if the pool's original data source already had them
 * defined. It is best practice to define pooled data sources in JNDI with all
 * important properties defined, so that all connections in the pool will be
 * homogenous.</li>
 * </ul>
 * Some common connection specific properties are:
 * <ul>
 * <li><code>server</code> - The TCP/IP address or name of the Vista server.</li>
 * <li><code>port</code> - The port on which the Vista broker is listening.</li>
 * <li><code>accessCode</code> - The Vista access code.</li>
 * <li><code>verifyCode</code> - The Vista verify code.</li>
 * <li><code>token</code> - Mutually exclusive with access/verify, used for SSO.</li>
 * <li><code>uci</code> - The UCI for CIA Broker connections.</li>
 * <li><code>jaasVistaLinkName</code> - The JAAS name used by VistaLink.</li>
 * </ul>
 */
public class ServiceLocator {

    private static ServiceLocator instance;
    public final static String ADDITIONAL_PROPERTIES = "VistaConnection";

    private ServiceLocator() {
    }

    /**
     * Gets singleton instance of this class.
     * @return Instance of <code>ServiceLocator</code>
     */
    public static synchronized ServiceLocator getInstance() {
        if (instance == null) {
            instance = new ServiceLocator();
        }
        return instance;
    }

    /**
     * Returns a data source as defined by the give properties.
     * @param properties Properties defining the data source. These properties
     * do not need to be completely specified. Additional properties may be
     * provided when a <code>getConnection</code> call is made against the data
     * source.
     * <p/>
     * For example, one might specify a <code>dataSourceName</code> (i.e., JNDI)
     * that is configured to supply the server, port, and connection type of a
     * Vista broker. When a connection attempt is made, the additional access
     * and verify codes can be provided.
     * @return A VistaDataSource
     * @throws VistaConnectionException
     * <P/>
     * Note: There is no validation of properties in the return value. It is
     * only when a connection attempt is made against the returned value will
     * its validity be known.
     */
    public VistaDataSource getDataSource(VistaConnectionProperties properties) throws VistaConnectionException {
        VistaDataSource dataSource = null;

        String datasourceName = properties.get("dataSourceName");

        if (datasourceName != null) {
            try {
                // The context lookup will probably recursively call this method
                // again, so remove "dataSourceName" so we don't go down this
                // path again.
                VistaConnectionProperties newProps = new VistaConnectionProperties(properties);
                newProps.remove("dataSourceName");
                InitialContext context = new InitialContext();
                Context envCtx = (Context) context.lookup("java:comp/env");
                envCtx.removeFromEnvironment(ADDITIONAL_PROPERTIES);
                if (!newProps.isEmpty()) {
                    envCtx.addToEnvironment(ADDITIONAL_PROPERTIES, newProps);
                }
                // First try to look up resource refs
                try {
                    dataSource = (VistaDataSource) envCtx.lookup(datasourceName);
                } catch (NamingException e) {
                    dataSource = null;
                }
                // If no resource refs, then look up by straight JNDI name
                if (dataSource==null) {
                    context.removeFromEnvironment(ADDITIONAL_PROPERTIES);
                    if (!newProps.isEmpty()) {
                        context.addToEnvironment(ADDITIONAL_PROPERTIES, newProps);
                    }
                    dataSource = (VistaDataSource) context.lookup(datasourceName);
                }
            } catch (NamingException e) {
                dataSource = null;
                throw new VistaConnectionException("JNDI lookup failed: "+e.getMessage(), e);
            }
        } else {
            try {
                VistaDataSourceFactory factory = null;
                String factoryName = properties.get("vistaDataSourceFactory");
                if (factoryName != null) {
                    Class<VistaDataSourceFactory> clazz = (Class<VistaDataSourceFactory>) Class.forName(factoryName);
                    factory = clazz.newInstance();
                } else {
                    factory = new DefaultDataSourceFactory();
                }
                dataSource = factory.createDatasource(properties);
            } catch (Exception ex) {
                throw new VistaConnectionException(ex.getMessage(), ex);
            }
        }

        if (dataSource==null) {
            throw new VistaConnectionException("Could not create a data source.");
        }
        return dataSource;
    }
}
