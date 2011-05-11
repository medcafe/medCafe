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
package org.medsphere.web.faces.error;

import com.medsphere.vistarpc.RPCException;
import java.util.Iterator;
import java.util.Map;
import javax.faces.FacesException;
import javax.faces.application.NavigationHandler;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;

class RPCExceptionExceptionHandler extends ExceptionHandlerWrapper {

    private ExceptionHandler wrapped;

    public RPCExceptionExceptionHandler(ExceptionHandler wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public ExceptionHandler getWrapped() {
        return this.wrapped;
    }

    @Override
    public void handle() throws FacesException {
        for (Iterator<ExceptionQueuedEvent> i = getUnhandledExceptionQueuedEvents().iterator(); i.hasNext();) {
            ExceptionQueuedEvent event = i.next();
            ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();
            Throwable t = context.getException();

            while (t != null && !(t instanceof RPCException)) {
                t = t.getCause();
            }
            if (t != null) {
                RPCException rpcException = (RPCException) t;
                FacesContext fc = FacesContext.getCurrentInstance();
                Map<String, Object> requestMap = fc.getExternalContext().getRequestMap();
                NavigationHandler nav = fc.getApplication().getNavigationHandler();

                try {
                    // Push stuff to the request scope for use in the page
                    requestMap.put("rpcexception", new RPCExceptionFormatter(rpcException));
                    nav.handleNavigation(fc, null, "error");
                    fc.renderResponse();
                } finally {
                    i.remove();
                }
            }
        }
        // At this point, the queue will not contain any RPCExceptions, so let
        // the parent handle them.
        getWrapped().handle();
    }
}
