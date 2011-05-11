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
package org.medsphere.webservice;

import javax.security.auth.Subject;
import javax.xml.ws.WebServiceContext;
import org.medsphere.auth.VistaSubject;
import org.medsphere.connection.VistaConnectionException;
import org.medsphere.connection.VistaConnectionProperties;
import org.medsphere.datasource.VistaDataSource;

/**
 * The base class for Vista-based services. This handles some of the details of
 * authentication. The {@link getStore() getStore} method obtains the
 * authenticated user for the session. Child classes are responsible for
 * creating a {@link Store} (see {@link getStaticStore() getStaticStore}) which
 * will handle its own server (pooled) connection.
 *
 * @param <T> The type of {@link Store} that the service will delegate to.
 */
public abstract class VistaService<T extends Store> {

    /**
     * Returns the user data source. If a configuration file is being used, it
     * must be called during a web service call so that the configuration file
     * name can be derived from the web service context path.
     * @return A data source that produces user connections.
     * @throws VistaConnectionException
     */
    abstract protected VistaDataSource getUserDataSource() throws VistaConnectionException;

    /**
     * The child class must return a {@link Store} defined <code>static</code>.
     * Child classes need not call this, only define it. Use {@link getStore()
     * getStore} when handling web service requests.
     * @return A static {@link Store}
     * @throws VistaConnectionException
     */
    abstract protected T getStaticStore() throws VistaConnectionException;

    /**
     * Child classes should inject a {@link WebServiceContext} object, which
     * they should return in this call.
     * @return An injected context
     */
    abstract protected WebServiceContext getWebServiceContext();

    /**
     * No-argument constructor
     */
    public VistaService() {
    }

    /**
     * Returns a backing store with the {@link Subject} set to the user that
     * authenticated the session. Both user and server connections with correct
     * credentials can be retrieved from the store with no other work.
     * @return a fully-authenticated store
     * @throws AuthenticationServiceException Thrown if the session is not
     * authenticated or connection problems occur.
     */
    protected T getStore() throws AuthenticationServiceException {
        VistaSubject subj = ServiceAuthenticator.getAuthenticatedSubject(getWebServiceContext().getMessageContext());
        if (subj == null) {
            throw new AuthenticationServiceException("Connection is not authenticated");
        }
        T store;
        try {
            store = getStaticStore();
        } catch (VistaConnectionException ex) {
            throw new AuthenticationServiceException("Could not initialize store", ex);
        }
        store.setSubject(subj);
        return store;
    }

    /**
     * Returns an authenticated <code>VistaSubject</code>, or throws
     * <code>AuthenticationServiceException</code> if a connection can not be
     * made. Generally, either an access and verify pair or a token must be
     * supplied, but it is up to the underlying connection types to determine
     * what is needed.
     * @param accessCode Vista access code
     * @param verifyCode Vista verify code
     * @param token Login token (from single sign on)
     * @return An authenticated subject
     * @throws AuthenticationServiceException Thrown when a connection can not be made.
     */
    protected VistaSubject authenticate(String accessCode, String verifyCode, String token) throws AuthenticationServiceException {
        VistaConnectionProperties tmpProps = new VistaConnectionProperties();
        if (accessCode != null && !accessCode.isEmpty()) {
            tmpProps.put("accessCode", accessCode);
        }
        if (verifyCode != null && !verifyCode.isEmpty()) {
            tmpProps.put("verifyCode", verifyCode);
        }
        if (token != null && !token.isEmpty()) {
            tmpProps.put("token", token);
        }
        try {
            return ServiceAuthenticator.login(getWebServiceContext(), getUserDataSource(), tmpProps);
        } catch (VistaConnectionException ex) {
            throw new AuthenticationServiceException(ex);
        }
    }

    /**
     * Logs out the current user.
     */
    public void logout() {
        ServiceAuthenticator.logout(getWebServiceContext());
    }
}
