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
package org.medsphere.connection;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple mapping of key strings to value strings.
 */
public final class VistaConnectionProperties extends HashMap<String, String> {

    /**
     * Constructor will use the <code>toString</code> on each value in the set
     * of pairs in the argument.
     * @param mapArg Existing object mapping. May be null.
     */
    public VistaConnectionProperties(Map<Object, Object> mapArg) {
        putAllAsStrings(mapArg);
    }

    /**
     * Constructor will all key/value pairs from the argument.
     * @param other Existing object. May be null.
     */
    public VistaConnectionProperties(VistaConnectionProperties other) {
        if (other != null) {
            putAll(other);
        }
    }

    /**
     * No-arg constructor.
     */
    public VistaConnectionProperties() {
    }

    /**
     * Copy the <code>toString</code> on each value in the set of pairs in the
     * argument. Values provided in the argument  overwrite existing values.
     * @param mapArg The object to copy values from.
     */
    public void putAllAsStrings(Map<Object, Object> mapArg) {
        if (mapArg != null) {
            for (Map.Entry<Object, Object> entry : mapArg.entrySet()) {
                put(entry.getKey().toString(), entry.getValue().toString());
            }
        }
    }
}
