// <editor-fold defaultstate="collapsed">
/*
 * Copyright (C) 2011  Medsphere Systems Corporation
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
package com.medsphere.fmdomain;

import java.lang.reflect.AnnotatedElement;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.medsphere.fileman.FMAnnotateFieldInfo;
import com.medsphere.fileman.FMField;
import com.medsphere.fileman.FMFile;
import com.medsphere.fileman.FMRecord;
import com.medsphere.fileman.FMResultSet;

public class FMInterventionRecommendation extends FMRecord {

    /*-------------------------------------------------------------
     * begin static initialization
     *-------------------------------------------------------------*/
    private static Set<FMField> domainFields;
    private static FMFile fileInfo;
    private static Map<String, AnnotatedElement> domainJavaFields;
    private static Map<String, String> domainNumbers;

    static {
        domainJavaFields = getDomainJavaFields(FMInterventionRecommendation.class);
        domainFields = getFieldsInDomain(domainJavaFields);
        domainNumbers = getNumericMapping(FMInterventionRecommendation.class);
        fileInfo = new FMFile("APSP INTERVENTION RECOMMENDATION") {

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
    public FMInterventionRecommendation() {
        super("APSP INTERVENTION RECOMMENDATION");
    }

    public FMInterventionRecommendation(FMResultSet results) {
        super(results);
    }
    // define annotated members
    @FMAnnotateFieldInfo(name = "NAME", number = ".01", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    private String name;

    public static FMFile getFileInfo() {
        return fileInfo;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "name=[" + name + "]  IEN=["+ getIEN() + "]";
    }
}
