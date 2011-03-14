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

/**
 * Helper class to format RPCExceptions. Used to format display on web pages.
 */
class RPCExceptionFormatter {

    private final RPCException rpcException;

    /**
     * Create new formatter for a exception.
     * @param rpcException Exception to format.
     */
    RPCExceptionFormatter(RPCException rpcException) {
        this.rpcException = rpcException;
    }

    /**
     * Format and return multi-line string. We show the cause and the stack
     * trace up until we leave medsphere code.
     *
     * @return Formatted exception text.
     */
    public String getrpcText() {
        StringBuilder sb = new StringBuilder();
        String line;
        sb.append("Cause: ").append(rpcException.getMessage()).append("\n\n");
        // print the stack trace, but only go 1 frame into non-medsphere code
        for (StackTraceElement element : rpcException.getStackTrace()) {
            line = element.toString();
            sb.append(line).append("\n");
            if (!line.contains("medsphere")) {
                sb.append("...").append("\n");
                break;
            }
        }
        return sb.toString();
    }
}
