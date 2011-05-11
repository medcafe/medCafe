// <editor-fold defaultstate="collapsed">
/*
 * Copyright (C) 2010  Medsphere Systems Corporation
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
// </editor-fold>
package org.medsphere.web.lifecycle;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.medsphere.lifecycle.LifecycleManager;

/**
 * Listens for shutdown of the servlet context and fires a lifecycle event
 * when it occurs. This will be fired when the server is shut down, the service
 * is stopped, the WAR file is redeployed, etc.
 *
 */
@WebListener
public class ServletLifecycleListener implements ServletContextListener {

    /**
     * No-arg constructor invoked due to the <code>@WebListener</code> annotation.
     */
    public ServletLifecycleListener() {
    }

    /**
     * This is a no-op.
     * @param sce Ignored.
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
    }

    /**
     * Used to call the <code>LifecycleManager</code> manager's shutdown.
     *
     * @param sce Ignored.
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LifecycleManager.getInstance().fireShutdown();
    }
}
