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

package com.medsphere.common.util;

import java.io.IOException;
import java.io.InputStream;

public class VersionLocator {

    public String getVersion( String resourcePath ) {
        InputStream istream = this.getClass().getResourceAsStream( resourcePath + "/version.txt" );
        if (istream == null) {
            return null;
        }
        byte[] buffer;
        String retVal = null;
        try {
            buffer = new byte[ istream.available() ];
            istream.read( buffer );
            retVal = new String( buffer );
        } catch (IOException e) {
            // do nothing
        } finally {
            try {
                istream.close();
            } catch (IOException e) {
                // do nothing
            }
        }
        return retVal;
    }

    public static void main(String[] args) {
        if (args.length > 0) {
            for (String arg : args) {
                System.out.println("Version for " + arg + ": " + new VersionLocator().getVersion(arg));
            }
        }
    }
}
