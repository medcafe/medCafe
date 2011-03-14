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

package com.medsphere.fileman;

import com.medsphere.resource.ResAdapter;
import com.medsphere.resource.ResException;
import com.medsphere.resource.ResWalker;
import com.medsphere.resource.Resource;

public class FMDictionary {

    private ResAdapter adapter;
    private String number;

    public FMDictionary( ResAdapter adapter, String number ) {
        this.adapter = adapter;
        this.number = number;
    }

    public GlobalNode execute() throws ResException {
        Resource infoRes = new Resource("FILEMAN");
        infoRes.addCompound("DIC");
        infoRes.addProperty("FILE", number);
        infoRes.endCompound(); // DICT
        adapter.writeResource( infoRes );
        GlobalNode retVal = new GlobalNode();;
        Resource resultRes = adapter.readResource();
        if (resultRes!=null) {
            ResWalker walker = new ResWalker(resultRes);
            while( walker.nextProperty() ) {
                String prop = walker.getProperty();
                if (prop.equals("N")) {
                    retVal.addChild( new GlobalNode( walker ) );
                } else {
                    walker.skip();
                }
            }
        }
        return retVal;
    }
}
