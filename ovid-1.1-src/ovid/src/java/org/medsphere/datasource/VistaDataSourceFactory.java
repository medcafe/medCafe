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

import org.medsphere.connection.VistaConnectionProperties;

/**
 * Factory interface for creating VistaDataSource objects from properties
 */
public interface VistaDataSourceFactory {

    /**
     * Create a VistaDataSource from the given properties
     * @param properties Properties used to create the VistaDataSource
     * @return VistaDataSource object
     */
    public VistaDataSource createDatasource(VistaConnectionProperties properties);
}
