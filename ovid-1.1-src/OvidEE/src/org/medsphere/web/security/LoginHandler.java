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
package org.medsphere.web.security;

import com.medsphere.vistarpc.RPCException;
import java.util.Map;
import org.medsphere.auth.SubjectAuthenticator;
import org.medsphere.auth.SubjectCache;
import org.medsphere.auth.VistaSubject;
import org.medsphere.connection.VistaConnectionException;
import org.medsphere.connection.VistaConnectionProperties;

/**
 * This class handles the basics for authenticating web applications. The
 * abstract methods should be implemented for each application based on the
 * application workflow and web framework.
 */
public abstract class LoginHandler {

    /**
     * The name of the session variable containing the
     * <code>VistaWebUser</code>.
     */
    static public final String VISTA_USER = "vista_user";

    /**
     * Provides the connection properties for user authentication.
     * @return Properties for the user's connection attempt.
     */
    protected abstract VistaConnectionProperties getConnectionProperties();

    /**
     * Provides the session map for the implementing web framework.
     * @return Session map.
     */
    protected abstract Map<String, Object> getSessionMap();

    /**
     * Posts an error message to the web framework.
     * @param summary A short message describing the error.
     * @param detail A detailed message containing the error.
     */
    protected abstract void postError(String summary, String detail);

    /**
     * Provides the session ID from the web framework.
     * @return Session ID.
     */
    protected abstract String getSessionID();

    /**
     * Authenticates the user, using the results of the
     * {@link #getConnectionProperties getConnectionProperties} call. Errors
     * messages are sent to {@link #postError postError}. Authenticated users
     * are added both to the framework's session cache and OVID's session cache.
     * @return True if authentication was successful.
     */
    public boolean authenticateUser() {
        // See if user is already logged in
        if (getUser() == null) {
            try {
                VistaConnectionProperties connectionProps = getConnectionProperties();
                VistaSubject vistaSubject = new VistaSubject();
                SubjectAuthenticator.getInstance().authenticate(vistaSubject, connectionProps);
                VistaWebUser VistaUser = new VistaWebUser(vistaSubject);
                getSessionMap().put(VISTA_USER, VistaUser);
                // Add to own own session cache so that we can expire it, ping it, whatever
                SubjectCache.getInstance().addToCache(getSessionID(), vistaSubject);

            } catch (VistaConnectionException ex) {
                postError("Login Failed!", ex.getMessage());
            }
        }
        return getUser() != null;
    }

    /**
     * Retrieve the user for the current session.
     *
     * @return Returns the user, or <code>null</code> if no user is
     * authenticated.
     */
    public VistaWebUser getUser() {
        VistaWebUser user = null;
        Object obj = getSessionMap().get(VISTA_USER);
        if (obj instanceof VistaWebUser) {
            user = (VistaWebUser) obj;
        }
        return user;
    }

    /**
     * Logs out the user corresponding to the current session.
     */
    public void logoutUser() {
        String sessionID = getSessionID();
        VistaSubject cachedSubject = null;
        // remove from our subject cache
        if (sessionID != null) {
            cachedSubject = SubjectCache.getInstance().getByKey(sessionID);
            SubjectCache.getInstance().removeFromCache(sessionID);
        }
        // remove from session map
        if (getSessionMap().remove(VISTA_USER) == null) {
            // We should not need to manually dispose of the subject. Removing
            // it from the session will call the valueUnbound() handler in the
            // VistaWebUser object since it implements HttpSessionBindingListener.
            // However, we check here just to make sure, and if it's not in the
            // session map, we go ahead and dispose it.
            if (cachedSubject != null) {
                try {
                    cachedSubject.dispose();
                } catch (RPCException ex) {
                    postError("Logout Failed!", ex.getMessage());
                }
            }
        }
    }

    /**
     * Returns the current status of the user in a display string.
     *
     * @return <code>Not logged in</code> if the user has not authenticated,
     * otherwise the user's name is queried from the server and combined
     * with the log in time.
     */
    public String getStatus() {
        VistaWebUser user = getUser();
        if (user == null) {
            return "Not logged in";
        }
        return user.getStatus();
    }
}
