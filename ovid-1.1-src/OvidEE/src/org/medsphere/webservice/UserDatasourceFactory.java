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
package org.medsphere.webservice;

import org.medsphere.connection.VistaConnectionProperties;
import org.medsphere.datasource.DefaultDataSourceFactory;
import org.medsphere.datasource.VistaDataSource;

/**
 * Creates data sources that have no authentication or pooling properties. This
 * can be specified as the data source factory to foolproof the configuration
 * of the user data source against having inappropriate properties.
 */
public class UserDatasourceFactory extends DefaultDataSourceFactory {

    /**
     * Create the data source. Pooling and authentication properties are
     * ignored.
     * @param properties Properties used to create VistaDataSource.
     * @return VistaDataSource without authentication or pooling.
     */
    @Override
    public VistaDataSource createDatasource(VistaConnectionProperties properties) {
        VistaConnectionProperties userProperties = new VistaConnectionProperties(properties);
        userProperties.remove("accessCode");
        userProperties.remove("verifyCode");
        userProperties.remove("token");
        userProperties.remove("poolName");
        return super.createDatasource(userProperties);
    }
}
