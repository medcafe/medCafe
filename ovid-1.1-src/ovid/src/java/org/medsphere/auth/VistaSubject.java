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
package org.medsphere.auth;

import com.medsphere.vistarpc.RPCConnection;
import com.medsphere.vistarpc.RPCException;
import com.medsphere.vistarpc.RPCResponse;
import com.medsphere.vistarpc.RPCResponse.ResponseType;
import com.medsphere.vistarpc.VistaRPC;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;
import javax.security.auth.Subject;

/**
 * Encapsulates the concept of a vista user. This class acts like a simplified
 * version of <code>javax.security.auth.Subject</code>. Once authenticated,
 * the subject can be queried to retrieve the connection it used authenticate.
 * <P/>
 * The connection returned by the <code>getConnection</code> methods can not
 * be closed. Calling <code>close</code> on the connection is a no-op. This
 * ensures that authenticated subjects always have a valid, open connection. To
 * close the connection, <code>dispose</close> its subject.
 */
public class VistaSubject implements Serializable {

    private final Subject subject;
    private String userName;

    /**
     * No-argument constructor.
     */
    public VistaSubject() {
        this(null);
    }

    /**
     * Creates a new object using an existing subject.
     *
     * @param subject The existing subject. May be null.
     */
    public VistaSubject(Subject subject) {
        if (subject == null) {
            this.subject = new Subject();
        } else {
            this.subject = subject;
        }
    }

    private static VistaConnectionPrincipal getVistaConnectionPrincipal(Subject subj) {
        if (subj == null) {
            return null;
        }
        Iterator<VistaConnectionPrincipal> iter = subj.getPrincipals(VistaConnectionPrincipal.class).iterator();
        if (!iter.hasNext()) {
            return null;
        }
        return iter.next();
    }

    /**
     * Acquire the connection used to authenticate this subject.
     * @return The authentication connection, or null if it could not be
     * authenticated.
     */
    public RPCConnection getVistaConnection() {
        return getVistaConnection(this.subject);
    }

    static private RPCConnection getVistaConnection(Subject subject) {
        RPCConnection connection = null;
        VistaConnectionPrincipal principal = getVistaConnectionPrincipal(subject);
        if (principal != null) {
            connection = principal.getAuthenticatedConnection();
        }
        return connection;
    }

    /**
     * Retrieve the Vista user's name from the server, using an RPC call. Note
     * that this is not the access or verify code, as those are not public
     * credentials.
     * @return The user name from Vista's <code>NEW PERSON</code> file.
     */
    public String getUserName() {
        if (userName == null) {
            userName = "Could not retrieve user information";
            RPCConnection connection = getVistaConnection();
            if (connection != null) {
                try {
                    VistaRPC userInfo = new VistaRPC("XUS GET USER INFO", ResponseType.ARRAY);
                    RPCResponse response = connection.execute(userInfo);
                    if (response != null && response.getError() == null) {
                        String[] responseArray = response.getArray();
                        if (responseArray.length >= 3) {
                            userName = responseArray[2];
                        }
                    }
                } catch (RPCException ex) {
                    // Do nothing, we already set the default
                }
            }
        }
        return userName;
    }

    /**
     * Get the backing <code>subject</code> for this object.
     * @return The <code>javax.security.auth.Subject</code> used by this object.
     */
    public Subject getSubject() {
        return subject;
    }

    /**
     * Release resources used by the subject. For authenticated subjects, this
     * is the only way to close the connection to the server.
     * @throws RPCException
     */
    public void dispose() throws RPCException {
        VistaConnectionPrincipal principal = getVistaConnectionPrincipal(getSubject());
        if (principal != null) {
            principal.dispose();
        }
    }

    void setConnection(RPCConnection conn) {
        Subject existingSubject = getSubject();
        if (existingSubject != null) {
            VistaConnectionPrincipal existingPrincipal = getVistaConnectionPrincipal(getSubject());
            if (existingPrincipal != null) {
                try {
                    existingPrincipal.dispose();
                } catch (RPCException ex) {
                    // Do nothing
                }
            }
            Set<VistaConnectionPrincipal> principals = existingSubject.getPrincipals(VistaConnectionPrincipal.class);
            if (principals != null) {
                principals.clear();
            }
        }

        VistaConnectionPrincipal principal = new VistaConnectionPrincipal(conn);
        getSubject().getPrincipals().add(principal);
    }
}
