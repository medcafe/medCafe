/*
 *  Copyright 2010 The MITRE Corporation (http://www.mitre.org/). All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mitre.medj.servlets;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.*;
import java.util.*;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.mitre.medj.WebUtils;

/**
 *  This allows for initial MedCafe setup, inlcuding
 *  <ul>
 *    <li> Setting application-wide variables for css, js, and image files</li>
 *  </ul>
 *
 *
 *@author     Gail Hamilton
 *@version    1.0
 *@see        javax.servlet.http
 */

public class InitServlet extends HttpServlet
{

    public final static String KEY = InitServlet.class.getName();
    public final static Logger log = Logger.getLogger( KEY );
    //static{log.setLevel(Level.FINER);}
    public static final long serialVersionUID = 1L;

    /**
     *  Constructor for the PqmServlet object
     *
     *@since
     */
    public InitServlet()
    {
        super();
    }


    /**
     *  Description of the Method
     *
     *@param  config                Description of the Parameter
     *@exception  ServletException  Description of the Exception
     */
    public void init( ServletConfig config )
        throws ServletException
    {
        log.entering(KEY, "init()");
        /*
         *  required for all Servlets
         */
        super.init( config );
        /*
         *  load up Config.java
         */
        String base_path = config.getServletContext().getRealPath( "/" );
        WebUtils.BASE_DIR = base_path;

      /*
         *  Grab the name for the currently deployed webapp.  It's possible this could be in error if the webapp is deployed
         *  as a subdirectory (i.e., The docbase is http://localhost:8080/first/sub).  If this ever gets deployed that way it will
         *  have to be fixed.
         */
        String tempdir = String.valueOf(getServletContext() .getAttribute("javax.servlet.context.tempdir"));
       
        log.finer("Attributes set in InitServlet");

        
        /* Any other set up */
        try{
       
        }
        catch (Exception e)
        {
        		log.severe("Error reading Repositories.xml file: " + e.getMessage());
        }
        log.exiting(KEY, "init()");
    }

    /**
     *  Description of the Method
     *
     *@param  req                   Description of the Parameter
     *@param  resp                  Description of the Parameter
     *@exception  ServletException  Description of the Exception
     *@exception  IOException       Description of the Exception
     */
    public void doGet( HttpServletRequest req, HttpServletResponse resp )
        throws IOException, ServletException
    {
        doPost( req, resp );
    }


    /**
     *  Description of the Method
     *
     *@param  req                   Description of the Parameter
     *@param  resp                  Description of the Parameter
     *@exception  ServletException  Description of the Exception
     *@exception  IOException       Description of the Exception
     */
    public void doPost( HttpServletRequest req, HttpServletResponse resp )
    {

    }
}

