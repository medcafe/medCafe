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

/**
 * OvidDatabaseInstaller
 * This is the main that installs/updates the ovid database from sql
 * scripts.
 */
package com.medsphere.ovid.tools.install;

import com.medsphere.common.type.IsASoftwareVersion;
import com.medsphere.common.type.SoftwareVersionException;
import com.medsphere.ovid.database.connection.OvidDatabaseConnectionFactory;
import com.medsphere.ovid.database.connection.OvidDatabaseException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class OvidDatabaseInstaller {

    Logger logger = Logger.getLogger(OvidDatabaseInstaller.class);
    private BufferedReader in = null;
    private IsASoftwareVersion version = null;
    private String application = "ovid";

    @SuppressWarnings("unused")
    private OvidDatabaseInstaller() {
    }

    public OvidDatabaseInstaller(String file, IsASoftwareVersion version) throws IOException {
        this(file, version, "ovid");
    }
    
    public OvidDatabaseInstaller(String file, IsASoftwareVersion version, String application) throws IOException {
        this.application = application;
        if (file.equalsIgnoreCase("/dev/null") || file.equalsIgnoreCase("nul:")) {
        } else {
            in = new BufferedReader(new FileReader(file));
        }
        this.version = version;

    }

    /**
     * using the file passed to the constructor, install the sql.
     * @throws java.io.IOException
     * @throws java.sql.SQLException
     * @throws com.medsphere.ovid.database.connection.OvidDatabaseException
     */
    private final String generalSQLEndOfStatement = ".*;$";
    private final String spEndOfStatement = "^\\s*[eE][nN][dD];\\s*$";
    private final String createProcedure = "^\\s*[cC][rR][eE][aA][tT][eE]\\s[pP][rR][oO][cC][eE][dD][uU][rR][eE].+"; // ^\\s*[cC][rR][eE][aA][tT][eE] [pP][rR][oO][cE][dD][uU][rR][eE]\\s*.+$";
    public void installSQL() throws IOException, SQLException, OvidDatabaseException {
        String str;
        StringBuffer sql = new StringBuffer();
        
        String endOfStatementPattern = generalSQLEndOfStatement;
        while ((str = in.readLine()) != null) {
            if (str.matches(createProcedure)) {
                endOfStatementPattern = spEndOfStatement;
                logger.info("=========================== found a stored procedure...");
            }
            sql.append(str + '\n');            
            if (str.trim().matches(endOfStatementPattern)) {
                endOfStatementPattern = generalSQLEndOfStatement;
                Connection conn = null;
                try {
                    conn = OvidDatabaseConnectionFactory.getInstance().getConnection();
                    Statement statement = conn.createStatement();
                    logger.info("Executing SQL statement: " + sql.toString());
                    statement.executeUpdate(sql.toString());
                    sql.delete(0, sql.length());
                    statement.close();
                    conn.close();
                } finally {
                    if (conn != null) {
                        try {
                            conn.close();
                        } catch (SQLException e) {
                        }

                    }
                }

            }

        }
        updateCurrentVersion();
    }

    public boolean versionExists(IsASoftwareVersion v) throws OvidDatabaseException {
        return versionExists(v, application);
    }
    
    public boolean versionExists(IsASoftwareVersion v, String application) throws OvidDatabaseException {
        boolean versionDoesExist = false;

        Connection conn = null;

        try {
            conn = OvidDatabaseConnectionFactory.getInstance().getConnection();
            String sql = "select 1 from OvidDatabaseVersion where version = ? and application = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, v.toString());
            ps.setString(2, application);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                versionDoesExist = true;
            }
            ps.close();
            rs.close();
        } catch (SQLException e) {
            throw new OvidDatabaseException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
        return versionDoesExist;

    }

    public void updateCurrentVersion() throws OvidDatabaseException {
        updateCurrentVersion(application);        
    }
    
    public void updateCurrentVersion(String application) throws OvidDatabaseException {
        if (versionExists(version)) {
            return;
        }
        Connection conn = null;
        try {
            conn = OvidDatabaseConnectionFactory.getInstance().getConnection();
            String sql = "insert into OvidDatabaseVersion values (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, version.toString());
            ps.setString(2, application);
            ps.setInt(3, version.getMajor());
            ps.setInt(4, version.getMinor());
            ps.setInt(5, version.getRevision());
            if (version.getBuild() != null) {
                ps.setInt(6, version.getBuild());
            } else {
                ps.setNull(6, Types.INTEGER);
            }
            ps.setDate(7, new Date(System.currentTimeMillis()));
            logger.info("adding version " + version.toString() + " to OvidDatabaseVersion table for application "+ application);
            logger.debug(ps);
            @SuppressWarnings("unused")
            int rs = ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new OvidDatabaseException(e);
        } catch (OvidDatabaseException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }

    }

    public static void main(String[] args) throws OvidDatabaseException {
        try {
            BasicConfigurator.configure();
            if (args.length > 0 && args[0].matches("^\\-[dD].*")) {
                Logger.getRootLogger().setLevel(Level.DEBUG);
            } else {
                Logger.getRootLogger().setLevel(Level.INFO);
            }

            final String installRoot = "scripts/OvidDatabase";
            
            System.out.print("checking for Ovid Database Install/Upgrade files");
            try {
                OvidDatabaseVersionDirectoryLocator ovidDatabaseVersionDirectoryLocator = new OvidDatabaseVersionDirectoryLocator(installRoot, "ovid");
                for (IsASoftwareVersion v : ovidDatabaseVersionDirectoryLocator.getListOfVersionsToBeApplied()) {
                    for (String p : ovidDatabaseVersionDirectoryLocator.getFilePathsForVersion(v, ".+\\.[sS][qQ][lL]$")) {
                        if (p != null && p.length() > 0) {
                            System.out.println("Applying SQL Statements in file " + p + " at version " + v.toString());
                            new OvidDatabaseInstaller(p, v).installSQL();
                        }
                    }
                }
            } catch (IOException e) {
                throw new OvidDatabaseException(e);
            } catch (NumberFormatException e) {
                throw new OvidDatabaseException(e);
            } catch (SQLException e) {
                throw new OvidDatabaseException(e);
            } catch (SoftwareVersionException e) {
                throw new OvidDatabaseException(e);
            }



            System.out.println("Done.");
        } finally {
            OvidDatabaseConnectionFactory.getInstance().closeAllConnections();
        }

    }
}
