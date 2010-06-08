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

package com.medsphere.vistalink;

import gov.va.med.exception.FoundationsException;
import gov.va.med.vistalink.adapter.cci.VistaLinkConnection;
import gov.va.med.vistalink.rpc.RpcRequest;
import gov.va.med.vistalink.rpc.RpcRequestFactory;
import gov.va.med.vistalink.rpc.RpcRequestParams;
import gov.va.med.vistalink.rpc.RpcResponse;
import gov.va.med.vistalink.security.CallbackHandlerUnitTest;
import gov.va.med.vistalink.security.VistaKernelPrincipalImpl;
import gov.va.med.vistalink.security.VistaLinkTokenBasedCallbackHandler;

import java.util.Map.Entry;

import javax.resource.ResourceException;
import javax.security.auth.login.Configuration;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import com.medsphere.vistarpc.RPCArray;
import com.medsphere.vistarpc.RPCConnection;
import com.medsphere.vistarpc.RPCException;
import com.medsphere.vistarpc.RPCResponse;
import com.medsphere.vistarpc.VistaRPC;

public class VistaLinkRPCConnection implements RPCConnection {
    VistaLinkConnection connection;
    String context;
    String duz = "0";
    protected final LoginContext loginContext;

    public VistaLinkRPCConnection(String server, String port, String access, String verify) throws RPCException {
        Configuration.setConfiguration( new AuthConfig(server, port) );
        try {
            loginContext = new LoginContext("", new CallbackHandlerUnitTest(access, verify, ""));
            loginContext.login();
            VistaKernelPrincipalImpl principal = VistaKernelPrincipalImpl.getKernelPrincipal(loginContext.getSubject());
            connection = principal.getAuthenticatedConnection();
            connection.setTimeOut(0);
            duz = principal.getUserDemographicValue(VistaKernelPrincipalImpl.KEY_DUZ);
        } catch (LoginException e) {
            throw new RPCException( "Can not log in: "+e.getLocalizedMessage(), e);
        } catch (FoundationsException e) {
            throw new RPCException( "Can not log in: "+e.getLocalizedMessage(), e);
        }
        setContext( "" );
    }

    public VistaLinkRPCConnection(String server, String port, String token) throws RPCException {
        Configuration.setConfiguration( new AuthConfig(server, port) );
        try {
            loginContext = new LoginContext("", new VistaLinkTokenBasedCallbackHandler(token, ""));
            loginContext.login();
            VistaKernelPrincipalImpl principal = VistaKernelPrincipalImpl.getKernelPrincipal(loginContext.getSubject());
            connection = principal.getAuthenticatedConnection();
            connection.setTimeOut(0);
            duz = principal.getUserDemographicValue(VistaKernelPrincipalImpl.KEY_DUZ);
        } catch (LoginException e) {
            throw new RPCException( "Can not log in: "+e.getLocalizedMessage(), e);
        } catch (FoundationsException e) {
            throw new RPCException( "Can not log in: "+e.getLocalizedMessage(), e);
        }
        setContext( "" );
    }

    public VistaLinkRPCConnection(VistaLinkConnection connection, LoginContext loginContext) {
        this.loginContext = loginContext;
        this.connection = connection;
    }

    public void close() {
        try {
            loginContext.logout();
        } catch (LoginException ex) {
            // do nothing
        }
        try {
            connection.close();
        } catch (ResourceException ex) {
            // do nothing
        }
    }

    public void setContext(String context) {
        this.context = context;
    }

    public RPCResponse execute(VistaRPC rpc) throws RPCException {
        RpcRequest request;
        RpcResponse response = null;
        try {
            request = RpcRequestFactory.getRpcRequest(context, rpc.getName());
            RpcRequestParams params = request.getParams();
            for( Entry<Integer,Object> entry : rpc.getParams().entrySet()) {
                int paramNumber = entry.getKey();
                Object paramValue = entry.getValue();
                if (paramValue instanceof String) {
                    params.setParam(paramNumber, "string", paramValue);
                } else if (paramValue instanceof RPCArray) {
                    params.setParam(paramNumber, "array", paramValue);
                } else {
                    // Reference paramters not supported yet
                    throw new RPCException( "Unsupported parameter type: " + paramValue.getClass().getName());
                }
            }
            response = connection.executeRPC(request);
        } catch (FoundationsException e) {
            throw new RPCException("VistaLink foundation exception", e);
        }
        switch (rpc.getType()) {
        case SINGLE_VALUE:
            return new RPCResponse(response.getResults());
        case ARRAY:
            return new RPCResponse(response.getResults().split("\n"));
        }
        throw new RPCException("Unsupported return type");
    }

    public String buildSubscript(String string) {
        return RpcRequest.buildMultipleMSubscriptKey( string );
    }

    @Override
    public String getDUZ() {
        return duz;
    }

}
