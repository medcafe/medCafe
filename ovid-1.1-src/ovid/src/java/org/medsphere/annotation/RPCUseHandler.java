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
package org.medsphere.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates a class that which may call Vista RPCs in order to determine which
 * methods will handle the calls. There are two forms.<p>
 * &#064;RPCUseHandler(handler="a") will use the handler "a" in all cases.<p>
 * &#064;RPCUseHandler(handler="a",rpc="b") will use the handler "a" only if the rpc
 * being called is named "b".
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE})
public @interface RPCUseHandler {

    /**
     * The name of the handler to invoke.
     * @return The name of the handler to invoke.
     */
    public String handler();

    /**
     * If defined, only RPC calls that match this value will invoke the handler.
     * @return The RPC name restricts this handler.
     */
    public String rpc() default "";
}
