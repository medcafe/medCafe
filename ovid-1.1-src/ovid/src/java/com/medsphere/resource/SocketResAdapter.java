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


package com.medsphere.resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SocketResAdapter implements ResAdapter {

    private OutputStream outStream;
    private InputStream inStream;

    public SocketResAdapter( Socket socket ) throws ResException {
        try {
            outStream = socket.getOutputStream();
            inStream = socket.getInputStream();
        } catch (IOException e) {
            throw new ResException("Could not get socket stream: "+e.getLocalizedMessage(), e);
        }
    }

    public void writeResource(Resource res) throws ResException {
        byte bytes[] = res.toBytes();
        byte destBytes[] = new byte[bytes.length + 4];
        System.arraycopy(bytes, 0, destBytes, 4, bytes.length);
        Resource.encodeInt(destBytes, bytes.length, 4);
        try {
            outStream.write( destBytes );
        } catch (IOException e) {
            throw new ResException("Write to output failed: "+e.getLocalizedMessage(), e);
        }
    }

    public Resource readResource() throws ResException {
        byte[] header = new byte[4];
        try {
            if (inStream.read( header )!=4) { // discard length of header
                throw new ResException( "Could not read header" );
            }
            return Resource.fromStream(inStream);
        } catch (IOException e) {
            throw new ResException("Read from input failed: "+e.getLocalizedMessage(), e);
        }
    }

}
