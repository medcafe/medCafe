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
package org.mitre.medcafe.servlets;

import java.io.*;
import java.sql.*;
import java.text.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.mitre.medcafe.restlet.*;
import org.mitre.medcafe.util.*;

/**
 *  Class to clean up any Repository connections on shutdown
 */
public class RepositoryListener implements ServletContextListener
{

    public final static String KEY = RepositoryListener.class.getName();
    public final static Logger log = Logger.getLogger( KEY );
    // static{log.setLevel(Level.FINER);}
	ServletContext context;

	public void contextInitialized(ServletContextEvent contextEvent)
	{
		context = contextEvent.getServletContext();
		// set variable to servlet context
		// context.setAttribute("TEST", "TEST_VALUE");
	}

	public void contextDestroyed(ServletContextEvent contextEvent)
	{
		context = contextEvent.getServletContext();
		Repositories.onShutdown();
	}
}

