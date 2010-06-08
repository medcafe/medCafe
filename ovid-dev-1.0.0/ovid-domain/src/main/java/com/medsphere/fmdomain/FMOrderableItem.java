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

import java.lang.reflect.AnnotatedElement;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import com.medsphere.fileman.FMAnnotateFieldInfo;
import com.medsphere.fileman.FMField;
import com.medsphere.fileman.FMFile;
import com.medsphere.fileman.FMRecord;
import com.medsphere.fileman.FMResultSet;

public class FMOrderableItem extends FMRecord  {

    /*-------------------------------------------------------------
     * begin static initialization
     *-------------------------------------------------------------*/
    private static Set<FMField> domainFields;
    private static FMFile fileInfo;
    private static Map<String, AnnotatedElement> domainJavaFields;
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
    protected Map<String, AnnotatedElement> getDomainJavaFields() {
        return domainJavaFields;
    }

    @Override
    protected Map<String, String> getNumericMapping() {
        return domainNumbers;
    }

    /*-------------------------------------------------------------
     * end static initialization
     *-------------------------------------------------------------*/

    @FMAnnotateFieldInfo( name="NAME", number=".01", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    protected String name;
    @FMAnnotateFieldInfo( name="INACTIVATED", number=".1", fieldType=FMField.FIELDTYPE.DATE)
    protected Date inactivatedDate;
    @FMAnnotateFieldInfo( name="PACKAGE NAME", number="1.1", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    protected String packageName;

    @FMAnnotateFieldInfo( name="ID", number="2", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    protected String id;


    @FMAnnotateFieldInfo( name="DISPLAY GROUP", number="5", fieldType=FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer displayGroup;
    @FMAnnotateFieldInfo( name="LAB SECTION", number="60.6", fieldType=FMField.FIELDTYPE.SET_OF_CODES)
    protected String labSection;

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
