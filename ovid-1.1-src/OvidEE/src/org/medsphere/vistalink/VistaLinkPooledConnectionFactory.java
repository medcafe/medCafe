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
package org.medsphere.vistalink;

import gov.va.med.exception.FoundationsException;
import gov.va.med.vistalink.adapter.cci.VistaLinkConnection;
import gov.va.med.vistalink.security.CallbackHandlerUnitTest;
import gov.va.med.vistalink.security.VistaKernelPrincipalImpl;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.security.auth.login.Configuration;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import com.medsphere.vistarpc.factory.RPCPooledConnectionFactory;
import com.medsphere.vistarpc.factory.RPCPooledConnection;

@Deprecated
public class VistaLinkPooledConnectionFactory extends RPCPooledConnectionFactory {

    private final Configuration config;
    private final String jaasName;
    private final String accessCode;
    private final String verifyCode;

    public VistaLinkPooledConnectionFactory(String server, String port, String accessCode, String verifyCode) throws LoginException, FoundationsException {
        jaasName = "";
        config = new AuthConfig(server, port);
        this.accessCode = accessCode;
        this.verifyCode = verifyCode;
    }

    public VistaLinkPooledConnectionFactory(String jaasName, String accessCode, String verifyCode) throws LoginException, FoundationsException {
        this.accessCode = accessCode;
        this.verifyCode = verifyCode;
        config = Configuration.getConfiguration();
        this.jaasName = jaasName;
    }

    public RPCPooledConnection getConnection() {
        RPCPooledConnection retVal = null;
        synchronized (available) {
            Configuration oldConfig = null;
            try {
                oldConfig = Configuration.getConfiguration();
            } catch (Exception e) {
            }
            try {
                if (available.peek() == null) {
                    Configuration.setConfiguration(config);
                    LoginContext newCtx = new LoginContext(jaasName, new CallbackHandlerUnitTest(accessCode, verifyCode, ""));
                    newCtx.login();
                    VistaLinkConnection vlConn = getConnection(newCtx);
                    vlConn.setTimeOut(0);
                    retVal = new VistaLinkPooledConnection(getConnection(newCtx), newCtx, this);
                    available.offer(retVal);
                }
                retVal = available.remove();
                inUse.add(retVal);
                return retVal;
            } catch (LoginException ex) {
                Logger.getLogger(VistaLinkPooledConnectionFactory.class.getName()).log(Level.SEVERE, null, ex);
            } catch (FoundationsException ex) {
                Logger.getLogger(VistaLinkPooledConnectionFactory.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                if (oldConfig != null) {
                    Configuration.setConfiguration(oldConfig);
                }
            }
        }
        return retVal;
    }

    private VistaLinkConnection getConnection(LoginContext ctx) throws FoundationsException {
        VistaKernelPrincipalImpl principal = VistaKernelPrincipalImpl.getKernelPrincipal(ctx.getSubject());
        return principal.getAuthenticatedConnection();
    }
}
