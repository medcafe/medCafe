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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Resource {
    protected ResNode rootNode;
    protected ResNode writeNode;
    final static private String char64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

    public Resource( String topProp ) {
        writeNode = rootNode = new ResNode( null, topProp );
    }

    private Resource( ResNode rootNode ) {
        this.rootNode = writeNode = rootNode;
    }

    public ResNode addCompound( String propertyName ) {
        // Resource building
        ResNode newNode = new ResNode( writeNode, propertyName, null );
        writeNode.addChild( newNode );
        writeNode = newNode;
        return newNode;
    }

    public void endCompound() {
        // Resource building
        if (writeNode.getParent()!=null) {
            writeNode = writeNode.getParent();
        }
    }

    public void addProperty( String propertyName, String value ) {
        // Resource building
        if (value==null) {
            value = "";
        }
        writeNode.addChild( new ResNode( writeNode, propertyName, value ) );
    }

    protected ResNode getRootNode() {
        return rootNode;
    }

    private void putProperty( OutputStream outputStream, String name, String value ) throws IOException {
        encodeString( outputStream, name, 1 );
        if (value==null) {
            encodeInt( outputStream, 0, 2 ); // signal that this is a compound
        } else {
            encodeString( outputStream, value, 2 );
        }
    }

    private void writeCompound( OutputStream outputStream, ResWalker walker ) throws IOException {
        putProperty( outputStream, walker.getProperty(), null );
        while (walker.nextProperty()) {
            if (walker.isCompound()) {
                writeCompound( outputStream, walker );
            } else {
                putProperty( outputStream, walker.getProperty(), walker.getValue() );
            }
        }
        encodeInt( outputStream, 0, 1 ); // signal end of compound
    }

    static private void encodeString(OutputStream outputStream, String string, int headerSize ) throws IOException {
        encodeInt( outputStream, string.length(), headerSize );
        outputStream.write( string.getBytes() );
    }

    static public void encodeInt( byte[] dest, int number, int numbytes ) {
       for (int x=0; x<numbytes; ++x ) {
            dest[x] = (byte) char64.charAt(number%64);
            number >>>= 6;
        }
    }

    static private void encodeInt(OutputStream outputStream, int number, int numBytes) throws IOException {
        byte[] dest = new byte[numBytes];
        encodeInt( dest, number, numBytes);
        outputStream.write(dest);
    }

    public void toStream( OutputStream stream ) throws IOException {
        stream.write(toBytes());
    }

    public byte[] toBytes() {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream(100);
        ResWalker walker = new ResWalker( this );
        try {
            writeCompound( byteStream, walker );
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return byteStream.toByteArray();
    }

    static private int decodeInt( InputStream stream, int numBytes ) throws IOException {
        int retVal = 0;
        int multiplier = 1;
        int tmp;
        while (numBytes>0) {
            tmp = char64.indexOf( stream.read() );
            if (tmp<0) {
                throw new IOException("Encoded integer has invalid value");
            }
            retVal += multiplier*tmp;
            multiplier <<= 6;
            --numBytes;
        }
        return retVal;
    }

    static private String decodeString( InputStream stream, int numBytes ) throws IOException {
        int len = decodeInt( stream, numBytes );
        if (len==0) {
            return null;
        }
        byte[] bytes = new byte[len];
        stream.read(bytes, 0, len);
        return new String( bytes );
    }

    static private void readChildren( ResNode parent, InputStream stream ) throws IOException {
        ResNode child;
        while ((child=readProperty(parent, stream))!=null) {
            parent.addChild( child );
        }
    }

    static private ResNode readProperty( ResNode parent, InputStream stream ) throws IOException {
        String propName = decodeString(stream,1);
        if (propName==null) {
            return null;
        }
        String value = decodeString(stream,2);
        ResNode retVal = new ResNode( parent, propName, value );
        if (value==null) {
            readChildren( retVal, stream );
        }
        return retVal;
    }

    static public Resource fromStream( InputStream stream ) throws IOException {
        return new Resource( readProperty( null, stream ) );
    }

    static public Resource fromBytes( byte[] bytes ) {
        ByteArrayInputStream stream = new ByteArrayInputStream( bytes );
        try {
            return fromStream( stream );
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }
}
