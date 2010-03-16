/*
 * Copyright (C) 2007-2009  Medsphere Systems Corporation
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
/*
 * OvidDatabaseConnectionFactory
 */
package com.medsphere.ovid.database.connection;

import com.medsphere.common.database.AbstractJDBCPooledConnection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.log4j.Logger;

/**
 *
 */
public class OvidDatabaseConnectionFactory extends AbstractJDBCPooledConnection {

    private static String tableExistsQuery = null;

    private static boolean dataSourceSupplied = false;
    
    private Logger logger = Logger.getLogger(OvidDatabaseConnectionFactory.class);
    private static OvidDatabaseConnectionFactory _instance = null;

    private OvidDatabaseConnectionFactory() throws SQLException {
        super("config", "ovid-db.properties");
    }

    private OvidDatabaseConnectionFactory(DataSource ds) {
        super(ds);
        dataSourceSupplied = true;
    }
    
    public static OvidDatabaseConnectionFactory getInstance() throws OvidDatabaseException {
        if (_instance == null) {
            try {
                _instance = new OvidDatabaseConnectionFactory();
                tableExistsQuery = _instance.getLoadedProperties().getProperty("jdbc.table.exists.query");
            } catch (SQLException e) {
                throw new OvidDatabaseException(e);
            }
        }
        return _instance;
    }
    
    public static OvidDatabaseConnectionFactory getInstance(DataSource ds) throws OvidDatabaseException {
        if (_instance == null) {
            _instance = new OvidDatabaseConnectionFactory(ds);            
        } else if (!isDataSourceSupplied()) {
            try {
                _instance = new OvidDatabaseConnectionFactory();
            } catch (SQLException ex) {
                throw new OvidDatabaseException(ex);
            }
        }
        return _instance;
    }
    
    private  static boolean isDataSourceSupplied() {
        return dataSourceSupplied;
    }
        
    public String getTableExistsQuery() {
        return tableExistsQuery;
    }
    
}
