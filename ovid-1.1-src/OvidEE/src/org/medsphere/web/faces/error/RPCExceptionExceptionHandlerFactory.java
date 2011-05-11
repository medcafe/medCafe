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

import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;

/**
 * Creates exception handlers that are specifically designed for RPCExceptions.
 * Since RCPExceptions are likely to be thrown while in the middle of a request,
 * we handle them specifically and present a customized stack trace that can be
 * provided to support staff.
 * <p/>
 * These objects are instantiated by the framework. This class is configured to
 * be used by the <code>exception-handler-factory</code> property in the JSF
 * configuration file.
 */
public class RPCExceptionExceptionHandlerFactory extends ExceptionHandlerFactory {

    private ExceptionHandlerFactory parent;

    /**
     * Create a new factory. This is called by the framework.
     *
     * @param parent The existing factory.
     */
    public RPCExceptionExceptionHandlerFactory(ExceptionHandlerFactory parent) {
        this.parent = parent;
    }

    /**
     * Returns a new instance of <code>RPCExceptionExceptionHandler</code>.
     *
     * @return Custom exception handler.
     */
    @Override
    public ExceptionHandler getExceptionHandler() {
        return new RPCExceptionExceptionHandler(parent.getExceptionHandler());
    }
}
