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
 * OvidDatabaseVersionRepository
 * Retrieve and persist information regarding the ovid databasse version.
 */
package com.medsphere.ovid.domain.database;

import com.medsphere.common.type.SoftwareVersion;
import com.medsphere.common.type.SoftwareVersionException;
import com.medsphere.common.util.VersionLocator;
import com.medsphere.ovid.database.connection.OvidDatabaseConnectionFactory;
import com.medsphere.ovid.database.connection.OvidDatabaseException;
import com.medsphere.ovid.tools.install.OvidDatabaseInstaller;
import com.medsphere.ovid.tools.install.OvidDatabaseVersion;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;

public class OvidDatabaseVersionRepository {

    private Logger logger = Logger.getLogger(OvidDatabaseVersionRepository.class);
    private String application = null;
    
    private OvidDatabaseVersionRepository() {}
    
    public OvidDatabaseVersionRepository(String application) {
        this.application = application;
    }

    public OvidDatabaseVersion getLatestOvidDatabaseVersion() throws SoftwareVersionException {
        return getLatestOvidDatabaseVersion(application);
    }
    public OvidDatabaseVersion getLatestOvidDatabaseVersion(String application) throws SoftwareVersionException {
        Connection conn = null;
        try {
            createOvidDatabaseVersionTable_IfNeeded();
            conn = OvidDatabaseConnectionFactory.getInstance().getConnection();
            Statement statement = conn.createStatement();
            String sql = "select Version, Major, Minor, Revision, Build, AppliedDate from OvidDatabaseVersion where Application = '" + application + "' order by Major desc, Minor desc, Revision desc, Build desc";
            logger.debug(sql);            
            ResultSet rs = statement.executeQuery(sql);            
            if (rs.next()) {
                logger.debug("found OvidDatabaseVersion");
                OvidDatabaseVersion ovidDatabaseVersion = new OvidDatabaseVersion(rs.getString("Version"),
                        rs.getInt("Major"),
                        rs.getInt("Minor"),
                        rs.getInt("Revision"),
                        rs.getInt("Build"),
                        rs.getDate("AppliedDate"));
                statement.close();
                rs.close();
                return ovidDatabaseVersion;
            } else {
                statement.close();
                rs.close();
                return new OvidDatabaseVersion("0000.00", 0000, 00, 00, null, new java.sql.Date(System.currentTimeMillis()));
            }

        } catch (SQLException e) {
            return new OvidDatabaseVersion("0000.00", 0000, 00, 00, null, new java.sql.Date(System.currentTimeMillis()));
        } catch (OvidDatabaseException e) {
            return new OvidDatabaseVersion("0000.00", 0000, 00, 00, null, new java.sql.Date(System.currentTimeMillis()));
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    private void createOvidDatabaseVersionTable_IfNeeded() throws OvidDatabaseException {
        String tableName = "OvidDatabaseVersion";
        Connection connection = null;
        try {
            boolean exists = doesTableExist(tableName);
            if (!exists) {
                String sql = "create table " + tableName + " (" +
                        "Version varchar(50) not null," +
                        "Application varchar(50) not null default 'ovid'," +
                        "Major int not null," +
                        "Minor int not null," +
                        "Revision int not null," +
                        "Build int null," +
                        "AppliedDate date not null," +
                        "constraint PK_" + tableName + " primary key (Application, Version)" +
                        ")";
                connection = OvidDatabaseConnectionFactory.getInstance().getConnection();
                connection.createStatement().executeUpdate(sql);
                logger.info("created table " + tableName);
                updateOvidDatabaseVersionToCurrent(application);
            }
        } catch (SQLException e) {
            throw new OvidDatabaseException(e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error(e);
                }
            }
        }

    }

    private boolean doesTableExist(String tableName) throws OvidDatabaseException {
        boolean exists = false;
        ResultSet rs = null;
        Connection connection = null;
        try {
            String tableExistsQuery = OvidDatabaseConnectionFactory.getInstance().getTableExistsQuery();
            connection = OvidDatabaseConnectionFactory.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement(tableExistsQuery);
            ps.setString(1, tableName);
            logger.debug("table exists query: " + tableExistsQuery + " with " + tableName.toUpperCase() + " as table");
            rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getInt(1) > 0) {
                    exists = true;
                }
            }
            ps.close();

        } catch (SQLException e) {
            throw new OvidDatabaseException(e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error(e);
                }
            }
        }
        return exists;

    }

    
    protected void updateOvidDatabaseVersionToCurrent(String application) throws OvidDatabaseException {
        OvidDatabaseVersion currentVersion = null;
        try {
            currentVersion = new OvidDatabaseVersionRepository(application).getLatestOvidDatabaseVersion();

        } catch (SoftwareVersionException e) {
        }
        VersionLocator versionLocator = new VersionLocator();
        String version = versionLocator.getVersion("/com/medsphere/" + application);
        try {
            if (version != null) {
                version.replace("([a-zA-Z])|(:.+)$", "");
                if (new OvidDatabaseVersion(version).isGreaterThan(currentVersion)) {
                    new OvidDatabaseInstaller("/dev/null", new SoftwareVersion(version), application).updateCurrentVersion();
                }
            }
        } catch (IOException e) {
            throw new OvidDatabaseException(e);
        } catch (SoftwareVersionException e) {
            throw new OvidDatabaseException(e);
        }
    }
}
