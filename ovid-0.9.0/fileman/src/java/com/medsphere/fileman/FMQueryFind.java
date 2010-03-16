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

import com.medsphere.resource.ResAdapter;
import com.medsphere.resource.ResException;
import com.medsphere.resource.Resource;

public class FMQueryFind extends FMQuery {

    private String indexName = null;
    private String indexValue = null;

    public FMQueryFind( ResAdapter sourceAdapter, FMFile fileInfo ) {
        super(sourceAdapter, fileInfo);
    }

    public void setIndex(String indexName, String indexValue) {
        this.indexName = indexName;
        this.indexValue = indexValue;
    }

    public FMResultSet execute() throws ResException {
        Resource queryRes = getQueryResource();
        queryRes.addCompound("FIND");
        addSearchProperties( queryRes );
        if (indexName!=null && indexValue!=null) {
            queryRes.addCompound("INDEX");
            queryRes.addProperty("NAME", indexName);
            queryRes.addProperty("VALUE", indexValue);
            queryRes.endCompound(); // INDEX
        }
        queryRes.endCompound(); // FIND
        return getResult( queryRes );
    }

}
