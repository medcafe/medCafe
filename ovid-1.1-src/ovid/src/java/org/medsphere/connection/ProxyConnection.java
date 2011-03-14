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

package org.medsphere.connection;

import com.medsphere.vistarpc.AbstractRPCConnection;
import com.medsphere.vistarpc.RPCConnection;
import com.medsphere.vistarpc.RPCException;
import com.medsphere.vistarpc.RPCResponse;
import com.medsphere.vistarpc.VistaRPC;
import org.medsphere.auth.VistaSubject;
import org.medsphere.datasource.ServiceLocator;

/**
 * Supplies a basic connection proxy. By default, the methods just invoke the
 * delegate methods. If the delegate is null, RPCExceptions will be thrown.
 */
public class ProxyConnection extends AbstractRPCConnection {

    private RPCConnection delegate;

    /**
     * Create a new proxy using the supplied delegate.
     * @param delegate The delegate, which may be null.
     */
    public ProxyConnection(RPCConnection delegate) {
        this.delegate = delegate;
    }

    /**
     * Create a new proxy, creating the delegate at the same time.
     * @param subject Subject used by some connection types
     * @param vcp Connection properties used to create the delegate
     * @param delegatePropertyName The broker type of the delegate
     * @throws VistaConnectionException On delegate creation error
     */
    public ProxyConnection(VistaSubject subject,
            VistaConnectionProperties vcp,
            String delegatePropertyName) throws VistaConnectionException {
        String delegateType = vcp.get(delegatePropertyName);
        if (delegateType != null) {
            // We create a new VistaConnectionProperties since we're going to
            // change the brokerType
            VistaConnectionProperties myVCP = new VistaConnectionProperties(vcp);
            myVCP.put("brokerType", delegateType);
            /*
             * Remove the pool name, otherwise we go into an infinite loop where
             * 1) The pool creates a proxy connection.
             * 2) The proxy connection creates a delegate using the connection
             *    properties.
             * 3) Those properties instruct the pooled datasource to create a
             *    connection, which will be a proxy.
             * 4) Go to step 1
             */
            myVCP.remove("poolName");
            delegate = ServiceLocator.getInstance().getDataSource(myVCP).getConnection(subject);
        }
    }

    public void close() throws RPCException {
        if (delegate!=null) {
            delegate.close();
            return;
        }
        throw new RPCException("Delegate not set");
    }

    public void setContext(String context) throws RPCException {
        if (delegate!=null) {
            delegate.setContext(context);
            return;
        }
        throw new RPCException("Delegate not set");
    }

    public RPCResponse execute(VistaRPC rpc) throws RPCException {
        if (delegate!=null) {
            return delegate.execute(rpc);
        }
        throw new RPCException("Delegate not set");
    }

    public String buildSubscript(String string) {
        if (delegate!=null) {
            return delegate.buildSubscript(string);
        }
        return string;
    }

    public String getDUZ() {
        if (delegate!=null) {
            return delegate.getDUZ();
        }
        return "";
    }

    /**
     * Getter for delegate
     * @return Delegate connection
     */
    public RPCConnection getDelegate() {
        return delegate;
    }

    /**
     * Setter for delegate
     * @param delegate Delegate connection
     */
    public void setDelegate(RPCConnection delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof ProxyConnection)) {
            return false;
        }
        ProxyConnection otherProxy = (ProxyConnection) other;
        return otherProxy.getDelegate().equals(getDelegate());
    }
}
