// <editor-fold defaultstate="collapsed">
/*
 * Copyright (C) 2009  Medsphere Systems Corporation
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

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import org.medsphere.web.security.LoginHandler;

/**
 * This <code>PhaseListener</code> will be take action before the
 * <code>Restore View</code> phase is invoked. This allows us to check to see
 * if the user is logged in before allowing them to request a secure resource.
 * If the user isn't logged in, then the listener will move the user to the
 * login page.
 */
public class AuthenticationPhaseListener implements PhaseListener {

    /**
     * Determines if the user is authenticated. If not, direct the user to the
     * login view, otherwise allow the user to continue to the requested view.
     * <p/>
     * Implementation Note: We do this in the <code>afterPhase</code>to make use
     * of the <code>NavigationHandler</code>.
     *
     * @param event Ignored.
     */
    @Override
    public void afterPhase(PhaseEvent event) {
        FacesContext context = event.getFacesContext();

        if (userExists(context)) {
            // allow processing of the requested view
            return;
        } else {
            // send the user to the login view
            if (requestingSecureView(context)) {
                context.responseComplete();
                context.getApplication().
                        getNavigationHandler().handleNavigation(context,
                        null,
                        FacesLoginHandler.getWelcomeScreen());
            }
        }
    }

    /**
     * This is a no-op.
     * <p/>
     *
     * @param event Ignored.
     */
    @Override
    public void beforePhase(PhaseEvent event) {
    }

    /**
     * Gets the phase ID.
     * @return <code>PhaseId.RESTORE_VIEW</code>.
     */
    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }

    // --------------------------------------------------------- Private Methods
    /**
     * Determine if the user has been authenticated by checking the session for
     * an existing <code>VistaUser</code> object.
     *
     * @param context The <code>FacesContext</code> for the current request.
     * @return <code>true</code> if the user has been authenticated, otherwise
     *  <code>false</code>.
     */
    private boolean userExists(FacesContext context) {
        ExternalContext extContext = context.getExternalContext();
        return (extContext.getSessionMap().containsKey(LoginHandler.VISTA_USER));
    }

    /**
     * Determines if the requested view is one of the login pages which will
     * allow the user to access them without being authenticated.
     * <p/>
     * Note, this implementation most likely will not work if the
     * <code>FacesServlet</code> is suffix mapped.
     *
     * @param context The <code>FacesContext</code> for the current request.
     * @return <code>true</code> if the requested view is allowed to be accessed
     *  without being authenticated, otherwise <code>false</code>.
     */
    private boolean requestingSecureView(FacesContext context) {
        ExternalContext extContext = context.getExternalContext();
        String path = extContext.getRequestPathInfo();
        return FacesLoginHandler.isSecureView(path);
    }
}
