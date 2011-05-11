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
package org.medsphere.datasource;

import java.util.Enumeration;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.spi.ObjectFactory;
import org.medsphere.connection.VistaConnectionProperties;

/**
 * Called by JNDI to create a {@link VistaDataSource}.
 */
public class JNDIDataSourceFactory implements ObjectFactory {

    public Object getObjectInstance(Object obj, Name name, Context nameCtx, Hashtable<?, ?> environment) throws Exception {
        return ServiceLocator.getInstance().getDataSource(getProperties(obj, environment));
    }

    protected VistaConnectionProperties getProperties(Object obj, Hashtable<?, ?> environment) {
        VistaConnectionProperties properties = new VistaConnectionProperties();

        // Properties specified in the JNDI configuration come in as references.
        if (obj != null && obj instanceof Reference) {
            Reference ref = (Reference) obj;
            for (Enumeration<RefAddr> e = ref.getAll(); e.hasMoreElements();) {
                RefAddr refAddr = e.nextElement();
                properties.put(refAddr.getType(), refAddr.getContent().toString());
            }
        }
        // Look for more properties put into the environment by ServiceLocator.
        Object otherPropsObj = environment.get(ServiceLocator.ADDITIONAL_PROPERTIES);
        if (otherPropsObj instanceof VistaConnectionProperties) {
            properties.putAll((VistaConnectionProperties) otherPropsObj);
        }
        return properties;
    }
}
