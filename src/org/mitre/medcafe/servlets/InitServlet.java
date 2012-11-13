/*
 * Copyright 2010 The MITRE Corporation (http://www.mitre.org/). All Rights
 * Reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.mitre.medcafe.servlets;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mitre.medcafe.restlet.Repositories;
import org.mitre.medcafe.util.Config;
import org.mitre.medcafe.util.Constants;
import org.mitre.medcafe.util.VelocityUtil;

/**
 * This allows for initial MedCafe setup, including
 * <ul>
 * <li>Setting application-wide variables for css, js, and image files</li>
 * </ul>
 * 
 * @see javax.servlet.http.HttpServlet
 */

public class InitServlet extends HttpServlet {

  public final static String KEY = InitServlet.class.getName();
  public final static Logger log = Logger.getLogger(KEY);
  // static{log.setLevel(Level.FINER);}
  public static final long serialVersionUID = 1L;

  /**
   * Constructor for the PqmServlet object
   */
  public InitServlet() {
    super();
  }

  public void init(ServletConfig config) throws ServletException {
    log.entering(KEY, "init()");
    /*
     * required for all Servlets
     */
    super.init(config);
    /*
     * load up Config.java
     */
    String base_path = config.getServletContext().getRealPath("/");
    Constants.BASE_PATH = base_path;

    Constants.CONFIG_DIR = base_path + "WEB-INF/";
    Config.init(Constants.CONFIG_DIR);
    // Config.setProperty( "BasePath", base_path );

    /* Configure velocity */
    Properties p = new Properties();
    p.setProperty("file.resource.loader.path", Constants.CONFIG_DIR
        + "templates");
    p.setProperty("runtime.log", base_path + "../../logs/velocity_example.log");
    VelocityUtil.init(p);
    /*
     * Grab the name for the currently deployed webapp. It's possible this could
     * be in error if the webapp is deployed as a subdirectory (i.e., The
     * docbase is http://localhost:8080/first/sub). If this ever gets deployed
     * that way it will have to be fixed.
     */
    String tempdir = String.valueOf(getServletContext().getAttribute(
        "javax.servlet.context.tempdir"));
    String serverName = String.valueOf(getServletContext().getInitParameter(
        "server.host"));
    String webapp = new File(tempdir).getName();  // could this be replaced by
                                                 // ServletContext.getServletContextName()
                                                 // ???
    if (webapp.equals("ROOT")) {
      webapp = "";
    }
    else
      webapp = "/" + webapp;
    Config.setWebapp(webapp);
    Config.setWebserver(serverName);
    log.finer("Server Name set in InitServlet " + serverName);
    getServletContext().setAttribute("base", webapp);
    getServletContext().setAttribute("images", webapp + "/images");
    getServletContext().setAttribute("js", webapp + "/js");
    getServletContext().setAttribute("css", webapp + "/css");

    getServletContext().setAttribute("server", Config.getServerUrl());
    log.finer("Attributes set in InitServlet");

    log.finer("Server Name in Config " + Config.getServerUrl());

    /* set up repositories */
    try {
      Repositories.setDefaultRepositories();
    } catch (Exception e) {
      log.severe("Error reading Repositories.xml file: " + e.getMessage());
    }
    log.exiting(KEY, "init()");
  }

  public void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws IOException, ServletException {
    doPost(req, resp);
  }


  public void doPost(HttpServletRequest req, HttpServletResponse resp) {
    //does nothing!
  }
}
