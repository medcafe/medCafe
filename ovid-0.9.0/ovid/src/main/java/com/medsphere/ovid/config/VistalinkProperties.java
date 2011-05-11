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
package com.medsphere.ovid.config;

import java.io.IOException;
import java.util.Properties;

import com.medsphere.common.util.PropertyFileLocator;

public class VistalinkProperties {
    private static Properties properties = null;
    public static Properties getProperties() {
        if (properties==null) {
            try {
                properties = new PropertyFileLocator("config", "vistalink.properties").getProperties();
            } catch (IOException e) {
                // Currently, never gets thrown
                e.printStackTrace();
            }
        }
        return properties;
    }
}
