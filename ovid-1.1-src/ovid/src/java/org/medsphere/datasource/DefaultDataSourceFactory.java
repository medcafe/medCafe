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

import java.lang.reflect.Constructor;
import org.medsphere.connection.VistaConnectionException;
import org.medsphere.connection.VistaConnectionProperties;
import org.medsphere.datasource.pool.VistaPooledDataSource;

/**
 * The default VistaDataSourceFactory object, which is also capable of creating
 * pooled data sources.
 */
public class DefaultDataSourceFactory implements VistaDataSourceFactory {

    public VistaDataSource createDatasource(VistaConnectionProperties properties) {
        VistaDataSource retVal = null;
        if (properties.get("dataSource") != null) {
            try {
                Class clazz = Class.forName(properties.get("dataSource").toString());
                Constructor<VistaDataSource> constructor = clazz.getConstructor(VistaConnectionProperties.class);
                Object obj = constructor.newInstance(properties);
                if (obj instanceof VistaDataSource) {
                    retVal = (VistaDataSource) obj;
                }
            } catch (Exception ex) {
                // do nothing
            }
        } else {
            retVal = new VistaServiceLoaderDataSource(properties);
        }
        // Now add pooling
        if (retVal != null && properties.get("poolName") != null) {
            try {
                retVal = new VistaPooledDataSource(retVal, properties);
            } catch (VistaConnectionException ex) {
                // Do nothing. This should never happen.
            }
        }
        return retVal;
    }
}
