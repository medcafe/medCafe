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

import java.util.Map;
import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import org.medsphere.auth.SubjectAuthenticator;
import org.medsphere.auth.SubjectCache;
import org.medsphere.auth.VistaSubject;
import org.medsphere.connection.VistaConnectionException;
import org.medsphere.connection.VistaConnectionProperties;
import org.medsphere.datasource.VistaDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class defines static methods that use web services contexts to
 * perform authentication-related tasks.
 */
public class ServiceAuthenticator {

    private static Logger logger = LoggerFactory.getLogger(ServiceAuthenticator.class);

    private static String getSessionID(MessageContext context) throws AuthenticationServiceException {
        HttpServletRequest httpServletRequest = (javax.servlet.http.HttpServletRequest) context.get(MessageContext.SERVLET_REQUEST);
        if (httpServletRequest != null) {
            // Look for HttpSession cookie
            HttpSession httpSession = httpServletRequest.getSession();
            if (httpSession != null) {
                logger.debug("httpsession : isNew == " + httpSession.isNew() + " getID == " + httpSession.getId());
                return httpSession.getId();
            }
        }
        // Look for session in HTTP header
        String token = getToken(context);
        if (token != null) {
            return token;
        } else {
            throw new AuthenticationServiceException("Unable to find a session for this service.  Are cookies enabled on the client?");
        }
    }

    private static String getToken(MessageContext context) {
        String token = null;
        Map<?, ?> requestHeaders = (Map<?, ?>) context.get(MessageContext.HTTP_REQUEST_HEADERS);
        if (requestHeaders != null) {
            String cookie = requestHeaders.get("Cookie").toString();
            if (cookie != null) {
                String[] parts = cookie.split("[=;]");
                for (int i = 0; i < parts.length; i++) {
                    if ("medsphere-token".equalsIgnoreCase(parts[i])) {
                        token = parts[i + 1];
                        break;
                    }
                }
            }
            if (token == null) {
                token = requestHeaders.get("Token").toString();
            }
        }

        return token;
    }

    /**
     * Authenticates given then context, data source, and connection properties.
     * On success, the subject is added to the subject cache.
     * @param wsContext The current web context
     * @param dataSource The data source to request a connection from
     * @param properties Connection properties
     * @return Returns an authenticated subject, or throws an exception.
     * @throws AuthenticationServiceException The subject credentials were not
     * valid
     * @throws VistaConnectionException No connection could be established.
     */
    public static VistaSubject login(WebServiceContext wsContext, VistaDataSource dataSource, VistaConnectionProperties properties) throws AuthenticationServiceException, VistaConnectionException {
        VistaSubject vistaSubject = getAuthenticatedSubject(wsContext.getMessageContext());
        if (vistaSubject != null) {
            return vistaSubject;
        }
        Subject subject = getSubject(wsContext);

        if (subject == null) {
            subject = new Subject();
        }
        vistaSubject = new VistaSubject(subject);
        if (!SubjectAuthenticator.getInstance().authenticate(vistaSubject, dataSource, properties)) {
            return null; // Should throw instead of reaching here.
        }
        String sessionID = getSessionID(wsContext.getMessageContext());
        SubjectCache.getInstance().addToCache(sessionID, vistaSubject);
        return vistaSubject;
    }

    /**
     * Logs out the current user and removes the subject from the subject cache.
     * @param wsContext
     */
    public static void logout(WebServiceContext wsContext) {
        try {
            MessageContext msgContext = wsContext.getMessageContext();
            VistaSubject subject = getAuthenticatedSubject(msgContext);
            if (subject != null) {
                subject.dispose();
                SubjectCache.getInstance().removeFromCache(getSessionID(wsContext.getMessageContext()));
            }
        } catch (Exception ex) {
            // do nothing
            logger.error("Exception while logging out ", ex);
        }
    }

    /**
     * Attempts to retrieve the subject associated with the given context.
     * @param messageContext The context being used.
     * @return An authenticated subject, or null if no subject has been
     * associated with the context.
     */
    public static VistaSubject getAuthenticatedSubject(MessageContext messageContext) {
        Subject subject;
        VistaSubject vistaSubject;
        // First see if the subject has been authenticated, such as by WSIT
        subject = getSubject(messageContext);
        if (subject != null) {
            vistaSubject = new VistaSubject(subject);
            if (SubjectAuthenticator.getInstance().isAuthenticated(vistaSubject)) {
                return vistaSubject;
            }
        }
        String sessionID;
        try {
            sessionID = getSessionID(messageContext);
        } catch (AuthenticationServiceException ex) {
            logger.error("Error getting session ID", ex);
            return null;
        }
        logger.debug("sessionid: " + sessionID);
        vistaSubject = SubjectCache.getInstance().getByKey(sessionID);
        return vistaSubject;
    }

    private static Subject getSubject(MessageContext messageContext) {
        return (Subject) messageContext.get("javax.security.auth.Subject");
    }

    private static Subject getSubject(WebServiceContext wsContext) {
        MessageContext msgCtx = wsContext.getMessageContext();
        return getSubject(msgCtx);
    }
}
