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
 * Annotates a method that can be used as a VistaRPC execution handler. The name
 * can be any identifier that names this handler. This allows an
 * {@link RPCUseHandler} to refer to this handler explicitly regardless of the
 * RPCs being called. Setting the RPC value declares the method as the default
 * handler for all matching RPC calls.
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD})
public @interface RPCHandler {

    /**
     * Defines the RPC that this method will handle. All calls to this RPC will
     * be directed to this handler if not otherwise directed.
     * @return The RPC that this method will handle.
     */
    public String rpc() default "";

    /**
     * Defines the name of the handler.
     * @return The name of the handler.
     */
    public String name() default "";
}
