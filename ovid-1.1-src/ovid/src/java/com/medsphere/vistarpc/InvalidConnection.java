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

package com.medsphere.vistarpc;

public class InvalidConnection implements RPCConnection {

    private final String message;
    public InvalidConnection(String message) {
        this.message = message;
    }

    public void close() throws RPCException {
        throw getException();
    }

    public void setContext(String context) throws RPCException {
        throw getException();
    }

    public RPCResponse execute(VistaRPC rpc) throws RPCException {
        throw getException();
    }

    public RPCResponse execute(VistaRPC rpc, String context) throws RPCException {
        throw getException();
    }

    public String buildSubscript(String string) {
        return "";
    }

    public String getDUZ() {
        return "";
    }

    private RPCException getException() {
        return new RPCException(message);
    }
}
