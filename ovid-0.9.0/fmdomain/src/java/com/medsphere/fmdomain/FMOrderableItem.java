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
import java.util.Date;
import java.util.Map;
import java.util.Set;

public class FMOrderableItem extends FMRecord  {
    
    /*-------------------------------------------------------------
     * begin static initialization 
     *-------------------------------------------------------------*/
    private static Set<FMField> domainFields;
    private static FMFile fileInfo;
    private static Map<String, Field> domainJavaFields;
    private static Map<String, String> domainNumbers;
    
    static {
        domainJavaFields = getDomainJavaFields(FMOrderableItem.class);
        domainFields = getFieldsInDomain(domainJavaFields);
        domainNumbers = getNumericMapping(FMOrderableItem.class);
        fileInfo = new FMFile("101.43") {

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
    
    @FMAnnoteFieldInfo( name="NAME", number=".01", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    private String name;
    @FMAnnoteFieldInfo( name="INACTIVATED", number=".1", fieldType=FMField.FIELDTYPE.DATE)
    private Date inactivatedDate;
    @FMAnnoteFieldInfo( name="PACKAGE NAME", number="1.1", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    private String packageName;

    @FMAnnoteFieldInfo( name="ID", number="2", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    private String id;

    
    @FMAnnoteFieldInfo( name="DISPLAY GROUP", number="5", fieldType=FMField.FIELDTYPE.POINTER_TO_FILE)
    private Integer displayGroup;
    @FMAnnoteFieldInfo( name="LAB SECTION", number="60.6", fieldType=FMField.FIELDTYPE.SET_OF_CODES)
    private String labSection;

    public FMOrderableItem() {
        super("101.43");
    }

    public FMOrderableItem(FMResultSet results) {
        super( results );
    }

    public Integer getDisplayGroup() {
        return displayGroup;
    }

    public String getId() {
        return id;
    }

    public Date getInactivatedDate() {
        return inactivatedDate;
    }

    public String getLabSection() {
        return labSection;
    }

    public String getName() {
        return name;
    }

    public String getPackageName() {
        return packageName;
    }

}
