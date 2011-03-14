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

public class FMQueryIndexes implements FMStatement {
    protected final ResAdapter adapter;
    protected final String fileNum;
    private final static int NAME_POS=0;
    private final static int STYLE_POS=1;
    private final static int SHORTDESC_POS=2;
    private final static int FILE_POS=3;
    private final static int FIELD_POS=4;
    private final static int ORDER_POS=5;
    private final static int LONGDESC_POS=6;
    private static String[] colNames = null;

    static {
        colNames = new String[7];
        colNames[NAME_POS] = "NAME";
        colNames[STYLE_POS] = "STYLE";
        colNames[SHORTDESC_POS] = "SHORTDESC";
        colNames[FILE_POS] = "FILE";
        colNames[FIELD_POS] = "FIELD";
        colNames[ORDER_POS] = "ORDER";
        colNames[LONGDESC_POS] = "LONGDESC";
    }

    public FMQueryIndexes(ResAdapter sourceAdapter, String fileNum) {
        adapter = sourceAdapter;
        this.fileNum = fileNum;
    }

    public FMResultSet execute() throws ResException {
        FMResultSet retVal = new FMResultSet();
        retVal.setColumns( colNames );
        Resource res = new Resource("FILEMAN");
        res.addCompound("INDEX");
        res.addProperty("FILE", fileNum);
        res.endCompound(); //INDEX;
        adapter.writeResource( res );
        Resource resultRes = adapter.readResource();
        if (resultRes!=null) {
            ResWalker walker = new ResWalker(resultRes);
            while (walker.nextProperty()) {
                String prop = walker.getProperty();
                if (prop.equals("INDEX")) {
                    processRow(walker, retVal);
                } else {
                    walker.skip();
                }
            }
        }
        return retVal;
    }

    private void processRow(ResWalker walker, FMResultSet retVal) {
        String name = null;
        String shortDesc = null;
        String longDesc = null;
        String style = "Traditional";
        while (walker.nextProperty()) {
            String prop = walker.getProperty();
            if (prop.equals("NAME")) {
                name = walker.getValue();
            } else if (prop.equals("SHORTDESC")) {
                shortDesc = walker.getValue();
            } else if (prop.equals("STYLE")) {
                style = walker.getValue();
            } else if (prop.equals("LONGDESC")) {
                if (longDesc==null) {
                    longDesc = walker.getValue();
                } else {
                    longDesc = longDesc + "\n" + walker.getValue();
                }
            } else if (prop.equals("FIELD")) {
                // we should get the fields last for each index
                String[] newRow = new String[colNames.length];
                newRow[NAME_POS] = name;
                newRow[SHORTDESC_POS] = shortDesc;
                newRow[LONGDESC_POS] = longDesc;
                newRow[STYLE_POS] = style;
                while (walker.nextProperty()) {
                    String fieldProp = walker.getProperty();
                    if (fieldProp.equals("FIELD")) {
                        newRow[FIELD_POS] = walker.getValue();
                    } else if (fieldProp.equals("FILE")) {
                        newRow[FILE_POS] = walker.getValue();
                    } else if (fieldProp.equals("ORDER")) {
                        newRow[ORDER_POS] = walker.getValue();
                    } else {
                        walker.skip();
                    }
                }
                retVal.addRow(newRow);
            } else {
                walker.skip();
            }
        }
    }
}
