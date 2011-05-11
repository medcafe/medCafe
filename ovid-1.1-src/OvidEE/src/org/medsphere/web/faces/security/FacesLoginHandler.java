// <editor-fold defaultstate="collapsed">
/*
 * Copyright (C) 2011  Medsphere Systems Corporation
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
package org.medsphere.web.faces.security;

import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.medsphere.web.security.LoginHandler;

/**
 * Implements basic login functionality for a JSF web application. A single
 * <code>ServiceLocator</code> entry should be specified in each web
 * application to provide the implementation for that application. This is used
 * to retrieve static data from the concrete FacesLoginHandler, specifically,
 * {@link #isSecureView isSecureView} and
 * {@link #getWelcomeScreen getWelcomeScreen}.
 */
public abstract class FacesLoginHandler extends LoginHandler {

    /**
     * Returns the JSF welcome page for the application. This is the page that
     * the user will go to before authenticating. It is usually the login
     * screen.
     * @return The name of the welcome screen.
     */
    protected abstract String welcomeScreen();

    /**
     * Returns the JSF start page for the application. This is the page that
     * the user will go to after authenticating.
     * @return The name of the start page.
     */
    public abstract String getStartScreen();

    /**
     * Provide a list of unsecured web pages. By default, pages are unavailable
     * unless the session has been authenticated. The login screen is unsecured.
     * @return An array of unsecured login screens.
     */
    protected abstract String[] getUnsecuredPaths();
    private static FacesLoginHandler delegate = null;

    /**
     * When invoked, it will invalidate the subject's session and move them to
     * the login view.
     *
     * @return The name of the JSF login outcome.
     */
    public String logout() {
        logoutUser();
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return getWelcomeScreen();
    }

    @Override
    protected void postError(String summary, String detail) {
        FacesContext context = FacesContext.getCurrentInstance();
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, detail);
        context.addMessage(null, message);
    }

    /**
     * Given a path, return whether the page is secured by the current
     * implementation of FacesLoginHandler. By default, all are unless they are
     * in {@link #getUnsecuredPaths getUnsecuredPaths}
     *
     * @param path the path to check.
     *
     * @return Returns true if the path is secure.
     */
    public static boolean isSecureView(String path) {
        for (String unsecured : getDelegate().getUnsecuredPaths()) {
            if (unsecured.equals(path)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Validates the subject. This may be called directly from the login screen.
     *
     * @return
     */
    public String validateUser() {
        return authenticateUser() ? getStartScreen() : null;
    }

    @Override
    protected String getSessionID() {
        FacesContext fCtx = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) fCtx.getExternalContext().getSession(false);
        return session.getId();
    }

    /**
     * Provides a concrete implementation of a FacesLoginHandler.
     * @return Instance of a FacesLoginHandler.
     */
    protected static FacesLoginHandler getDelegate() {
        if (delegate == null) {
            ServiceLoader<FacesLoginHandler> loader = ServiceLoader.load(FacesLoginHandler.class);
            Iterator<FacesLoginHandler> iter = loader.iterator();
            if (iter.hasNext()) {
                delegate = iter.next();
            } else {
                throw new IllegalStateException("No 'org.medsphere.web.faces.security.FacesLoginHandler' defined for ServiceLoader.");
            }
        }
        return delegate;
    }

    @Override
    protected Map<String, Object> getSessionMap() {
        FacesContext context = FacesContext.getCurrentInstance();
        return context.getExternalContext().getSessionMap();
    }

    /**
     * Provides the welcome (aka login) screen for the current web application.
     * @return Name of the welcome screen.
     */
    public static String getWelcomeScreen() {
        return getDelegate().welcomeScreen();
    }
}
