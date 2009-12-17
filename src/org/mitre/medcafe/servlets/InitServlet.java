/*
 *  Copyright 2008 The MITRE Corporation (http://www.mitre.org/). All Rights Reserved.
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
package org.mitre.medcafe.servlets;

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
import org.mitre.medcafe.restlet.*;
import org.mitre.medcafe.util.*;

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
        /*
         *  required for all Servlets
         */
        super.init( config );
        /*
         *  load up Config.java
         */
        String base_path = config.getServletContext().getRealPath( "/" );
        String config_dir = base_path + "WEB-INF/";

        Config.init( config_dir );
        // Config.setProperty( "BasePath", base_path );
        /*
         *  Grab the name for the currently deployed webapp.  It's possible this could be in error if the webapp is deployed
         *  as a subdirectory (i.e., The docbase is http://localhost:8080/first/sub).  If this ever gets deployed that way it will
         *  have to be fixed.
         */
        String tempdir = String.valueOf(getServletContext() .getAttribute("javax.servlet.context.tempdir"));
        String webapp = new File(tempdir).getName();
        if( webapp.equals("ROOT") )
        {
            webapp = "";
        }
        else webapp = "/" + webapp;
        Config.setWebapp( webapp );
        getServletContext().setAttribute("base",  webapp );
        getServletContext().setAttribute("images",  webapp + "/images");
        getServletContext().setAttribute("js",  webapp + "/js");
        getServletContext().setAttribute("css",  webapp + "/css");
        System.out.println("Attributes set in InitServlet");

        /* set up repositories */
        Repositories.setDefaultRepositories();
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

