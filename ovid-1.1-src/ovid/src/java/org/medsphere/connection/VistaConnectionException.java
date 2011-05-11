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
package org.medsphere.connection;

/**
 * Exception thrown when something is drastically wrong with a connection
 * attempt. This probably is due to a misconfiguration.
 */
public class VistaConnectionException extends Exception {

    /**
     * Create an exception using a string.
     * @param msg Message
     */
    public VistaConnectionException(String msg) {
        super(msg);
    }

    /**
     * Create an exception using a string and a source exception.
     * @param msg Message
     * @param cause Exception that caused this exception to be created.
     */
    public VistaConnectionException(String msg, Exception cause) {
        super(msg, cause);
    }
}
