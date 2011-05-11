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

import com.medsphere.resource.Resource;

public class FMScreenEquals implements FMScreen {

    protected final FMScreen left;
    protected final FMScreen right;

    public FMScreenEquals(FMScreen left, FMScreen right) {
        this.left = left;
        this.right = right;
    }

    public void writeResource(Resource res) {
        res.addCompound("EQUALS");
        {
            res.addCompound("LEFT");
            left.writeResource(res);
            res.endCompound(); // LEFT
            res.addCompound("RIGHT");
            right.writeResource(res);
            res.endCompound(); // RIGHT
        }
        res.endCompound(); // EQUALS
        System.out.println(String.valueOf(res));
    }

}
