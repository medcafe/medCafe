/*
 *  Copyright 2010 Jeffrey Hoyt.  All rights reserved.
 */
package org.mitre.medcafe.servlets;

import java.sql.*;
import java.util.logging.Logger;
import javax.servlet.http.*;
import javax.servlet.*;

import org.mitre.medcafe.util.Constants;
import org.mitre.medcafe.util.DatabaseUtility;
import org.mitre.medcafe.util.DbConnection;

import java.io.*;

/**
 * Filter to put the theme value that the user has saved
 *  
 */
public class ThemeFilter implements Filter
{

    public final static String KEY = ThemeFilter.class.getName();
    public final static Logger log = Logger.getLogger( KEY );
    // static{log.setLevel(Level.FINER);}

    //{{{ Members
    private FilterConfig filterConfig = null;
   
    /**
     * Checks to see if the character is dead or not.  If they are, redirect any attempted action to dead.jsp.
     *
     */
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
        throws IOException, ServletException
    {
        log.finer("filter running");
        System.out.println("ThemeFilter : do Filter start");
        String endpoint = ((HttpServletRequest)request).getRequestURI();
        log.finer("processing " + endpoint );
        HttpSession session = ((HttpServletRequest)request).getSession(false);
        if (session != null) 
        {
        	DbConnection conn;
			try {
				conn = new DbConnection();
			
	        	PreparedStatement prep=null;
	        	String userName = ((HttpServletRequest)request).getRemoteUser();	
	        	     
	        	String query = "select value from preferences where key=? and username=?";
	        	//Default to a custom CSS that this is deployed with
	        	String themeValue = Constants.DEFAULT_CSS_THEME;
	        	prep = conn.prepareStatement(query);
	        	prep.setString(1, "theme");
	        	prep.setString(2, userName);
	        		  
	        	System.out.println("ThemeFilter : doFilter prep statement " + prep.toString());
	            
	        	ResultSet rs = prep.executeQuery();
	        			
	        	if (rs.next())
	        	{
	        		themeValue = rs.getString("value");
	        	}
	        	String webApp =(String) filterConfig.getServletContext().getAttribute("base");
	        			  
	        	session.setAttribute(Constants.CSS_THEME,  webApp + "/" + themeValue);
	        	DatabaseUtility.close(rs);
	        	
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				throw new ServletException("Problem with setting CSS theme");
			}
        }
        else
        {
            log.finer("session is null");
            filterConfig.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
            return;
        }
        
        chain.doFilter(request, response);
    }


    public void init(FilterConfig filterConfig) throws ServletException
    {
        this.filterConfig = filterConfig;
    }

    public void destroy() {}


}

// :wrap=none:noTabs=true:collapseFolds=1:folding=explicit:

