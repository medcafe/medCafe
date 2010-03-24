/*
 *  Copyright 2009 The MITRE Corporation. All Rights Reserved.
 */
package org.mitre.medcafe.util;

import com.sun.rowset.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import javax.naming.*;
import javax.sql.*;
import javax.sql.rowset.*;

/**
 *  The DbConnection class coordinates all SQL calls to the database. It should the only way the
 *  database is accessed.
 *
 * @author     Jeffrey Hoyt
 */
public class DbConnection
{

    public final static String KEY = DbConnection.class.getName();
    public final static Logger log = Logger.getLogger( KEY );
    static{log.setLevel(Level.FINER);}
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
    /**
     *  This method executes the Query SQL stmtement (usually a SELECT) that is passed as a parameter and returns a
     *  CachedResultSet  Do NOT use this when retrieving large amounts of data, as this dumps lots of data into memory.
     *  Also, for large data pulls, you should be setting the retreival cache size to speed retreival.
     *
     * @param  sSQL        Query string suitable for creating a PreparedStatment
     * @param Object...    Comma delimited list of parameters, in order!
     * @return             ResultSet of the query
     * @exception  SQLException  Description of the Exception
     */
    public ResultSet psExecuteQuery(String query, String errorMsg, Object... params)
    {
        log.entering(KEY, "psExecuteQuery", query);
        log.entering(KEY, "psExecuteQuery", params);
        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            CachedRowSet crs = new CachedRowSetImpl();
            ps = conn.prepareStatement(query);
            //set params
            int i = 1;
            for(Object param: params)
            {
                if( param instanceof String )
                {
                    ps.setString(i, (String)param);
                }
                else if( param instanceof Integer )
                {
                    ps.setInt(i, ((Integer)param).intValue());
                }
                else if( param instanceof Float )
                {
                    ps.setFloat(i, ((Float)param).floatValue());
                }
                else if( param instanceof Double )
                {
                    ps.setDouble(i, ((Double)param).doubleValue());
                }
                else if( param == null )
                {
                    throw new NullPointerException("Parameters passed to psExecuteQuery() cannot be null");
                }
                else throw new RuntimeException( "This method does not handle " + param.getClass() + " yet." );
                i++;
            }
            rs = ps.executeQuery();
            crs.populate(rs);
            DatabaseUtility.close(rs);
            DatabaseUtility.close(ps);
            return crs;
        }
        catch(SQLException e)
        {
            log.throwing( KEY, errorMsg, e);
            return null;
        }
        finally
        {
            DatabaseUtility.close(rs);
            DatabaseUtility.close(ps);
        }
    }


    /**
     *  This method executes the Query SQL stmtement (usually a SELECT) that is passed as a parameter and returns a
     *  CachedResultSet  Do NOT use this when retrieving large amounts of data, as this dumps lots of data into memory.
     *  Also, for large data pulls, you should be setting the retreival cache size to speed retreival.
     *
     * @param  sSQL        Query string suitable for creating a PreparedStatment
     * @param Object...    Comma delimited list of parameters, in order!
     * @return             ResultSet of the query
     * @exception  SQLException  Description of the Exception
     */
    public int psExecuteUpdate(String query, String errorMsg, Object... params)
    {
        log.entering(KEY, "psExecuteUpdate", query);
        log.entering(KEY, "psExecuteUpdate", params);

        PreparedStatement ps = null;
        try
        {
            ps = conn.prepareStatement(query);
            //set params
            int i = 1;
            for(Object param: params)
            {
                if( param instanceof String )
                {
                    ps.setString(i, (String)param);
                }
                else if( param instanceof Integer )
                {
                    ps.setInt(i, ((Integer)param).intValue());
                }
                else if( param instanceof Float )
                {
                    ps.setFloat(i, ((Float)param).floatValue());
                }
                else if( param instanceof Double )
                {
                    ps.setDouble(i, ((Double)param).doubleValue());
                }
                else if( param == null )
                {
                    throw new NullPointerException("Parameters passed to psExecuteQuery() cannot be null");
                }
                else throw new RuntimeException( "This method does not handle " + param.getClass() + " yet." );
                i++;
            }
            return ps.executeUpdate();
        }
        catch(SQLException e)
        {
            log.throwing( KEY, errorMsg, e);
            return -1;
        }
        finally
        {
            DatabaseUtility.close(ps);
        }
    }
}

