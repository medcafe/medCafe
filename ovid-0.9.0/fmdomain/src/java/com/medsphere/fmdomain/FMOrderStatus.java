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

/*
 * Data from fileman ORDER_STATUS file
 */
package com.medsphere.fmdomain;

import com.medsphere.fileman.FMAnnoteFieldInfo;
import com.medsphere.fileman.FMField;
import com.medsphere.fileman.FMFile;
import com.medsphere.fileman.FMRecord;
import com.medsphere.fileman.FMResultSet;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class FMOrderStatus extends FMRecord {

    /*-------------------------------------------------------------
     * begin static initialization 
     *-------------------------------------------------------------*/
    private static Set<FMField> domainFields;
    private static FMFile fileInfo;
    private static Map<String, Field> domainJavaFields;
    private static Map<String, String> domainNumbers;
    
    static {
        domainJavaFields = getDomainJavaFields(FMOrderStatus.class);
        domainFields = getFieldsInDomain(domainJavaFields);
        domainNumbers = getNumericMapping(FMOrderStatus.class);
        fileInfo = new FMFile("100.01") {

            @Override
            public Collection<FMField> getFields() {
                return domainFields;
            }
        };
        fileInfo.setPack(true);
        
    }

    public static FMFile getFileInfoForClass() {
        return fileInfo;
    }

    @Override
    protected Set<FMField> getDomainFields() {
        return domainFields;
    }

    @Override
    protected Map<String, Field> getDomainJavaFields() {
        return domainJavaFields;
    }

    @Override
    protected Map<String, String> getNumericMapping() {
        return domainNumbers;
    }
    
    /*-------------------------------------------------------------
     * end static initialization 
     *-------------------------------------------------------------*/
 
    @FMAnnoteFieldInfo(name = "NAME", number = ".01", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    private String name;
    @FMAnnoteFieldInfo(name = "SHORT NAME", number = ".02", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    private String shortName;
    @FMAnnoteFieldInfo(name = "ABBREVIATION", number = ".1", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    private String abbreviation;

    
    public FMOrderStatus() {
        super(fileInfo.getFileName());
    }

    public FMOrderStatus(FMResultSet results) {
        super(results);
    }    

    public String getName() {
        return name;
    }
    
    public String getShortName() {
        return shortName;
    }
    
    public String getAbbreviation() {
        return abbreviation;
    }
}
