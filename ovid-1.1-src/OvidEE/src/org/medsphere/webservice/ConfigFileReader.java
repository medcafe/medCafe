// <editor-fold defaultstate="collapsed">
/*
 * Copyright (C) 2010  Medsphere Systems Corporation
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
package org.medsphere.webservice;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.Name;
import javax.servlet.ServletContext;
import org.medsphere.connection.VistaConnectionProperties;
import org.medsphere.datasource.JNDIDataSourceFactory;
import org.medsphere.datasource.ServiceLocator;

/**
 * Create a VistaDataSource by using a configuration file, if present. Otherwise
 * fall back to JNDI configuration.
 */
public class ConfigFileReader extends JNDIDataSourceFactory {

    @Override
    public Object getObjectInstance(Object obj, Name name, Context nameCtx, Hashtable<?, ?> environment) throws Exception {
        String configFileName = null;
        VistaConnectionProperties properties = getProperties(obj, environment);
        configFileName = properties.get("configFileName");
        if (configFileName != null) {
            properties.putAll(getProperties(configFileName));
            return ServiceLocator.getInstance().getDataSource(properties);
        }
        return super.getObjectInstance(obj, name, nameCtx, environment);
    }

    /**
     * Retrieves connections properties from a text file. The file should be
     * in {@link Properties} format.
     * @param configFileName The name of the file to read from.
     * @return Properties contained in the file, or an empty set if the file
     * does not exist.
     */
    public static VistaConnectionProperties getProperties(String configFileName) {
        FileInputStream fis = null;
        VistaConnectionProperties retVal = new VistaConnectionProperties();
        try {
            try {
                Properties p = new Properties();
                fis = new FileInputStream(configFileName);
                p.load(fis);
                retVal = new VistaConnectionProperties(p);
            } finally {
                if (fis != null) {
                    fis.close();
                }
            }
        } catch (Exception ex) {
            // Do nothing
        }
        return retVal;
    }

    /**
     * Retrieves connections properties from a text file. The file should be
     * in {@link Properties} format. The context is used to derive the file
     * name. See {@link #getConfigFileName getConfigFileName}.
     * @param sc A context used to define a file name.
     * @return Properties contained in the file, or an empty set if the file
     * does not exist.
     */
    public static VistaConnectionProperties getProperties(ServletContext sc) {
        return getProperties(getConfigFileName(sc));
    }

    /**
     * Returns a file name based on the current directory and the context path.
     * @param sc A context used to define a file name.
     * @return A file name based on the current directory and the context path.
     */
    public static String getConfigFileName(ServletContext sc) {
        String appPath = sc.getContextPath().replaceAll("[\\/]", "");
        return sc.getRealPath("../../store." + appPath + ".properties");
    }

    /**
     * Writes a file in {@link Properties} format containing the supplied
     * properties to the specified file.
     * @param configFileName The name of the file to write to.
     * @param vcp The properties to write.
     * @throws IOException On filesystem error.
     */
    public static void writeProperties(String configFileName, VistaConnectionProperties vcp) throws IOException {
        Properties p = new Properties();
        FileOutputStream fos = null;
        for (Entry<String, String> entry : vcp.entrySet()) {
            p.put(entry.getKey(), entry.getValue());
        }
        try {
            fos = new FileOutputStream(configFileName);
            p.store(fos, null);
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
    }
}
