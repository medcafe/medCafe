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
package com.medsphere.fileman;

import java.util.TreeSet;
import java.util.Iterator;

public class FMFieldSet extends TreeSet<FMField> {
    private static final long serialVersionUID = 2210251659437377341L;

    public void remove(String fieldID) {
        Iterator<FMField> iter = iterator();
        FMField field;
        while (iter.hasNext()) {
            field = iter.next();
            if (field.matches(fieldID)) {
                iter.remove();
                break;
            }
        }
    }
    public FMField get(String fieldID) {
        Iterator<FMField> iter = iterator();
        FMField field;
        while (iter.hasNext()) {
            field = iter.next();
            if (field.matches(fieldID)) {
                return field;
            }
        }
        return null;
    }
}
