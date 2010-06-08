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

package com.medsphere.rpcresadapter;

import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;

import com.medsphere.resource.ResAdapter;
import com.medsphere.resource.ResException;
import com.medsphere.resource.Resource;
import com.medsphere.vistarpc.RPCArray;
import com.medsphere.vistarpc.RPCConnection;
import com.medsphere.vistarpc.RPCException;
import com.medsphere.vistarpc.RPCResponse;
import com.medsphere.vistarpc.VistaRPC;
import com.medsphere.vistarpc.RPCResponse.ResponseType;

public class RPCResAdapter implements ResAdapter {

    protected RPCConnection connection;
    protected String rpcName;
    protected RPCResponse response;
    private Logger logger = Logger.getLogger(RPCResAdapter.class);

    //  CHUNK_SIZE must match those being used on the MUMPS side.
    //  See MUMPS routine MSCRES
    private final int CHUNK_SIZE = 201;

    public RPCResAdapter(RPCConnection connection, String rpcName) {
        this.connection = connection;
        this.rpcName = rpcName;
    }

    private byte[][] toByteArray(Resource res) {
        byte[] resBytes = res.toBytes();
        int arraySize = resBytes.length / CHUNK_SIZE + 1;
        if (resBytes.length%CHUNK_SIZE==0) {
            --arraySize;
        }
        byte[][] bytes = new byte[arraySize][];
        int remainingSize = resBytes.length;
        int idx = 0;
        while (remainingSize>0) {
            int thisSize = remainingSize>CHUNK_SIZE ? CHUNK_SIZE : remainingSize;
            bytes[idx] = new byte[thisSize];
            System.arraycopy(resBytes, idx*CHUNK_SIZE, bytes[idx], 0, thisSize);
            ++idx;
            remainingSize -= thisSize;
        }
        return bytes;
    }


    public Resource readResource() {
        if (response==null) {
            return null;
        }

        if (response.getError() != null) {
            logger.error("=================== RPCResAdapter::readResource has error: " + response.getError());
            return null;
        }

        StringBuffer sb = new StringBuffer();
        for (String s : response.getArray()) {
            sb.append(s);
        }
        byte[] rawBytes = null;
        try {
            rawBytes = sb.toString().getBytes( "UTF-8" );
        } catch (UnsupportedEncodingException e) {
            // should never happen with UTF-8
            e.printStackTrace();
            return null;
        }
        return Resource.fromBytes( rawBytes );
    }

    public void writeResource(Resource res) throws ResException {
        response = null;
        RPCArray arg1 = new RPCArray();
        byte[][] bytes = toByteArray(res);
        int arraySize = bytes.length;
        for (int i=0; i<arraySize; ++i) {
            arg1.put(Integer.toString(i+1), new String( bytes[i] ));
        }
        VistaRPC rpc = new VistaRPC(rpcName, ResponseType.ARRAY);
        rpc.setParam(1, arg1);
        try {
            response = connection.execute(rpc);
        } catch (RPCException e) {
            throw new ResException("Failed to execute RPC", e);
        }
    }

}
