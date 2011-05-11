/*
 * Copyright (C) 2007-2009  Medsphere Systems Corporation
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
package com.medsphere.ovid.security;

import com.medsphere.vistarpc.RPCConnection;
import com.medsphere.vistarpc.RPCException;
import java.util.Iterator;
import javax.security.auth.Subject;
import org.apache.log4j.Logger;


public abstract class UserAuthenticator {

    public static boolean isAuthenticated(Subject subject) {
        if (subject != null) {
            return subject.getPrincipals(VistaConnectionPrincipal.class).size()>0;
        }
        return false;
    }

    public abstract boolean authenticate(Subject subject, RPCConnectionProperties properties);

    public static VistaConnectionPrincipal getVistaConnectionPrincipal(Subject subject) {
        Iterator<VistaConnectionPrincipal> iter = subject.getPrincipals(VistaConnectionPrincipal.class).iterator();
        if (!iter.hasNext()) {
            return null;
        }
        return iter.next();
    }

    public static void dispose(Subject subject) {
        VistaConnectionPrincipal principal = UserAuthenticator.getVistaConnectionPrincipal(subject);
        if (principal != null ){
            try {
                RPCConnection connection = principal.getAuthenticatedConnection();
                if (connection!=null) {
                    principal.getAuthenticatedConnection().close();
                }
            } catch (RPCException ex) {
                Logger.getLogger(UserAuthenticator.class).warn("Could not cleanly dispose subject", ex);
            }
        }
    }
}
