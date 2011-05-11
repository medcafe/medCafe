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
package org.medsphere.web.security;

import com.medsphere.vistarpc.RPCConnection;
import com.medsphere.vistarpc.RPCException;
import java.util.Date;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import org.medsphere.auth.VistaSubject;

/**
 * This object represents a web user. It is placed into the session, and later
 * used to retrieve connections to the Vista server. Authenticated users will
 * contain an authenticated <code>VistaSubject</code>.
 */
public class VistaWebUser implements HttpSessionBindingListener {

    private String status = null;
    private final VistaSubject subject;
    private final Date date = new Date();

    /**
     * Constructor taking a <code>VistaSubject</code> which should be
     * authenticated, though no check is performed to verify that.
     * @param subject The <code>VistaSubject</code> this user represents.
     */
    public VistaWebUser(VistaSubject subject) {
        this.subject = subject;
    }

    /**
     * Display a status string for the user. This string is composed of the
     * subject's name and the time <code>this</code> was created.
     * @return
     */
    public String getStatus() {
        if (status == null) {
            if (subject != null) {
                status = "Logged in as: " + subject.getUserName() + " at " + date;
            } else {
                status = "Not logged in";
            }
        }
        return status;
    }

    /**
     * Returns the connection that the subject used for authentication.
     * @return The subject's connection, or <code>null</code> if not
     * authenticated.
     */
    public RPCConnection getVistaConnection() {
        RPCConnection connection = null;
        if (subject != null) {
            connection = subject.getVistaConnection();
        }
        return connection;
    }

    /**
     * Clean up all resources associated with this object.
     * @throws RPCException
     */
    public void dispose() throws RPCException {
        if (subject != null) {
            subject.dispose();
        }
    }

    /**
     * This is a no-op.
     * @param event Ignored.
     */
    @Override
    public void valueBound(HttpSessionBindingEvent event) {
    }

    /**
     * Called by the framework, this implements the method called as specified
     * by <code>HttpSessionBindingListener</code> when the object is removed
     * from the session, including manual removal and session expiration.
     * @param event Ignored.
     */
    @Override
    public void valueUnbound(HttpSessionBindingEvent event) {
        try {
            dispose();
        } catch (RPCException ex) {
            // Do nothing since we're shutting down
        }
    }
}
