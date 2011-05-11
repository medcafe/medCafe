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

import gov.va.med.exception.FoundationsException;
import gov.va.med.vistalink.security.CallbackHandlerUnitTest;
import gov.va.med.vistalink.security.VistaKernelPrincipalImpl;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

public class UserAuthenticator {

   private final static Map<Subject, LoginContext> contexts
            = Collections.synchronizedMap( new HashMap<Subject, LoginContext>() );

   public static boolean authenticate(String jaasName, Subject subject, String accessCode, String verifyCode) {
       System.out.println("authenticating " + jaasName + " subject " + subject);
        try {
            LoginContext lc = new LoginContext(jaasName, subject, new CallbackHandlerUnitTest(accessCode, verifyCode, ""));
            lc.login();
            contexts.put(subject, lc);
        } catch (LoginException ex) {
            System.out.println("authentication error: " + ex.getMessage());
            Logger.getLogger(UserAuthenticator.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (Exception e) {
            System.out.println("error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    public static boolean isAuthenticated(Subject subject) {
        if (subject!=null) {
            try {
                return VistaKernelPrincipalImpl.getKernelPrincipal(subject) != null;
            } catch (FoundationsException ex) {
                Logger.getLogger(UserAuthenticator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }

    public static void dispose(Subject subject) {
        LoginContext lc = contexts.get(subject);
        // We should always get the same subject back from the context.
        if (lc!=null) {
            if (lc.getSubject()==subject) {
               try {
                    lc.logout();
                } catch (LoginException ex) {
                    Logger.getLogger(UserAuthenticator.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                Logger.getLogger(UserAuthenticator.class.getName()).log(Level.WARNING,
                        "The login context that was cached for the subject does not match the subject. The user will not be properly logged out.");
            }
            contexts.remove(lc);
        }
    }
}
