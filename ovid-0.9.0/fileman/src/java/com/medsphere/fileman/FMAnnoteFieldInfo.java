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

import java.lang.annotation.Retention;

import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import com.medsphere.fileman.FMField.FIELDTYPE;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FMAnnoteFieldInfo {
    String number();
    String name();
    FIELDTYPE fieldType();
}
