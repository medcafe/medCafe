/*
 *  Copyright 2009 The MITRE Corporation. All Rights Reserved.
 */
package org.mitre.medcafe.util;

import java.sql.*;
import java.util.*;
import javax.sql.*;
import javax.naming.*;

/**
 *  The DbConnection class coordinates all SQL calls to the database. It should the only way the
 *  database is accessed.
 *
 * @author     Jeffrey Hoyt
 */
public class DbConnection
{
    private Connection conn = null;
    private Statement stmt = null;


    public DbConnection()
        throws SQLException
    {
        try {
            InitialContext cxt = new InitialContext();
            if ( cxt == null ) {
               throw new SQLException("Uh oh -- no context!");
            }

            DataSource ds = (DataSource) cxt.lookup( "java:/comp/env/jdbc/medcafe" );

            if ( ds == null ) {
               throw new SQLException("Data source not found!");
            }
            conn = ds.getConnection();
        }
        catch (NamingException e)
        {
            throw new SQLException( e );
        }
    }

    public PreparedStatement prepareStatement(String sql)
    throws SQLException
    {
        return conn.prepareStatement(sql);
    }

    /**
     *  This method sets whether the connection automatically commits SQL stmtements individually or as grouped
     *  transactions that are terminated by either a call to Rollback() or Commit(). By default, new connections
     *  automatically commit SQL stmtements individually.
     *
     * @param  bAutoCommit                The new autoCommit value
     * @exception  SQLException  Description of the Exception
     */
    public void setAutoCommit(boolean bAutoCommit)
        throws SQLException
    {
        conn.setAutoCommit(bAutoCommit);
    }

    /**
     *  This method returns the existing connection to the database.
     *
     * @return    The connection value
     */
    public Connection getConnection()
    {
        return conn;
    }


    /**
     *  This method will either return the connection to the pool or close the open connection and stmtement of the
     *  database. This method is used to free up resources.
     *
     * @exception  SQLException  Description of the Exception
     */
    public void close()
    {
        DatabaseUtility.close(stmt);
        DatabaseUtility.close(conn);
    }


    /**
     *  This method executes the Query SQL stmtement (usually a SELECT) that is passed as a parameter and returns a
     *  java.sql.ResultSet. Repeated calls to this method delete previous ResultSets. The close method of the Resultset
     *  should be invoked when the user has finished using the Resultset.
     *
     * @param  sSQL                       Description of the Parameter
     * @return                            Description of the Return Value
     * @exception  SQLException  Description of the Exception
     */
    public ResultSet executeQuery( String sSQL )
        throws SQLException
    {
        if( stmt==null || stmt.isClosed() )
        {
            stmt = conn.createStatement();
        }
        return (stmt.executeQuery( sSQL ) );
    }

    /**
     *  This method executes an updating SQL stmtement (usually an UPDATE, INSERT, or DELETE) that is passed as a
     *  parameter and returns an int that represents either the row count or 0 for SQL stmtements that don't have return
     *  values.
     *
     * @param  sSQL                       Description of the Parameter
     * @return                            Description of the Return Value
     * @exception  SQLException  Description of the Exception
     */
    public int executeUpdate(String sSQL)
        throws SQLException
    {
        int iRowCount = stmt.executeUpdate(sSQL);
        return (iRowCount);
    }

    /**
     *  This method executes an updating SQL stmtement (usually an UPDATE, INSERT, or DELETE) that is passed as a
     *  parameter signals the driver with the given flag about whether the auto-generated keys produced by this
     *  stmtement should be made available for retrieval.
     *
     * @param  sSQL                       Description of the Parameter
     * @param returnGenerated             one of the following constants: Statement.RETURN_GENERATED_KEYS  Statement.NO_GENERATED_KEYS
     * @return                            Description of the Return Value
     * @exception  SQLException  Description of the Exception
     */
    public ResultSet executeUpdate(String sSQL, int returnGenerated)
        throws SQLException
    {
        stmt.executeUpdate(sSQL, returnGenerated);
        return stmt.getGeneratedKeys();
    }


}

