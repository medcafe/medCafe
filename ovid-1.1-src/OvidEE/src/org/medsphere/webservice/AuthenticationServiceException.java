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

/**
 * Exception thrown as a result of a communications failure or an inability to
 * authenticate.
 */
public class AuthenticationServiceException extends Exception {

    /**
     * Constructs a <code>AuthenticationServiceException</code> with the
     * specified detail message.
     *
     * @param message The detail message.
     */
    public AuthenticationServiceException(String message) {
        super(message);
    }

    /**
     * Constructs a <code>AuthenticationServiceException</code> with the
     * specified cause.
     *
     * @param message The detail message.
     * @param cause The cause.
     */
    public AuthenticationServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a <code>AuthenticationServiceException</code> with the
     * specified detail  message and cause.
     *
     * @param cause The cause.
     */
    public AuthenticationServiceException(Throwable cause) {
        super(cause);
    }
}
