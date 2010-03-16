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

public class FMDisplayGroup extends FMRecord {

    /*-------------------------------------------------------------
     * begin static initialization 
     *-------------------------------------------------------------*/
    private static Set<FMField> domainFields;
    private static FMFile fileInfo;
    private static Map<String, Field> domainJavaFields;
    private static Map<String, String> domainNumbers;
    
    static {
        domainJavaFields = getDomainJavaFields(FMDisplayGroup.class);
        domainFields = getFieldsInDomain(domainJavaFields);
        domainNumbers = getNumericMapping(FMDisplayGroup.class);
        fileInfo = new FMFile("100.98") {

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
    
    public FMDisplayGroup() {
        super("100.98");
    }

    public FMDisplayGroup(FMResultSet results) {
        super( results );
    }
    
    @FMAnnoteFieldInfo( name="NAME", number=".01", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    private String name;
    @FMAnnoteFieldInfo( name="MIXED NAME", number="2", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    private String mixedName;
    @FMAnnoteFieldInfo( name="SHORT NAME", number="3", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    private String shortName;

    public String getMixedName() {
        return mixedName;
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }

    
}
