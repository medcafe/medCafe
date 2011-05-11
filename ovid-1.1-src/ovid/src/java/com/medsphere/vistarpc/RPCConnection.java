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

package com.medsphere.vistarpc;

/**
 *  This interface defines a connection to Vista that supports RPC calls.
 */
public interface RPCConnection {

    /**
     * Closes the underlying connection, including all associated cleanup.
     * @throws RPCException
     */
    public void close() throws RPCException;

    /**
     * Sets the context of the call for subsequent RPC calls.<p>
     * <b>Warning:</b> in a multithreaded environment, other threads may call
     * this method before
     * {@link #execute(com.medsphere.vistarpc.VistaRPC) execute} is called. In a
     * multithreaded environment, set the context with
     * {@link #execute(com.medsphere.vistarpc.VistaRPC, java.lang.String) execute(rpc,context)}
     * instead.
     * @param context The context (VA FileMan file #19, field .01) to use for
     * subsequent RPC calls.
     * @throws RPCException
     */
    public void setContext(String context) throws RPCException;

    /**
     * Executes a Vista RPC.
     *
     * @param rpc The RPC to call.
     * @return Returns a response object.
     * @throws RPCException
     */
    public RPCResponse execute(VistaRPC rpc) throws RPCException;

    /**
     * Executes a Vista RPC with the given context.
     * @param rpc The RPC to call.
     * @param context The context (VA FileMan file #19, field .01) to execute
     * the RPC with.
     * @return Returns a response object.
     * @throws RPCException
     */
    public RPCResponse execute(VistaRPC rpc, String context) throws RPCException;

    /**
     * This method is used by VistaLink to build a string representation of an
     * index for a list-type RPC parameter. See the VistaLink documentation
     * for <code>buildMultipleMSubscriptKey</code>.
     * @param string The string to build an index from.
     * @return Returns a string that can be parsed by the underlying broker.
     */
    public String buildSubscript(String string);

    /**
     * Get the DUZ of the user that has authenticated this connection.
     * @return The DUZ (IEN of FileMan file #200) of the current user.
     */
    public String getDUZ();
}
