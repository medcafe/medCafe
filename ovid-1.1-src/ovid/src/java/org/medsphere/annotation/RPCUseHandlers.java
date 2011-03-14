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
 * This annotation will apply all annotations in its value array to the
 * class. This makes it possible for one class to define multiple handlers.
 *  For example:
 * <pre>
 *&#064;RPCUseHandlers({
 *   &#064;RPCUseHandler(handler = "TheDefault"),
 *   &#064;RPCUseHandler(rpc = "RPC1", handler = "Handler1"),
 *   &#064;RPCUseHandler(rpc = "RPC2", handler = "Handler2")
 *})
 *public class SomeClass{...}</pre>
 * When a method of class SomeClass calls an RPC, Handler1 will be invoked for
 * the rpc named RPC1, Handler2 for RPC2, and the rest will invoke the handler
 * named TheDefault.
 *
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE})
public @interface RPCUseHandlers {

    /**
     * An array of RPCUseHandler annotations.
     * @return An array of RPCUseHandler annotations.
     */
    public RPCUseHandler[] value();
}
