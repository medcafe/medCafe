// <editor-fold defaultstate="collapsed">
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
// </editor-fold>


/*
 * General Ovid Domain level Exception
 */

package com.medsphere.ovid.domain.ov;

public class OvidDomainException extends Exception {

    public OvidDomainException(String message) {
        super(message);
    }
    
    public OvidDomainException(Throwable reason) {
        super(reason);
    }
}
