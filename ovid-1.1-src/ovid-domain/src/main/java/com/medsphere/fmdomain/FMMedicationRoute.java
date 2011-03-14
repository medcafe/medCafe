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

import com.medsphere.fileman.FMAnnotateFieldInfo;
import com.medsphere.fileman.FMField;
import com.medsphere.fileman.FMField.FIELDTYPE;
import com.medsphere.fileman.FMFile;
import com.medsphere.fileman.FMRecord;
import com.medsphere.fileman.FMResultSet;
import java.lang.reflect.AnnotatedElement;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author mark.taylor
 */
public class FMMedicationRoute extends FMRecord {
    /*-------------------------------------------------------------
     * begin static initialization
     *-------------------------------------------------------------*/
    private static Set<FMField> domainFields;
    private static FMFile fileInfo;
    private static Map<String, AnnotatedElement> domainJavaFields;
    private static Map<String, String> domainNumbers;

    static {
        domainJavaFields = getDomainJavaFields(FMMedicationRoute.class);
        domainFields = getFieldsInDomain(domainJavaFields);
        domainNumbers = getNumericMapping(FMMedicationRoute.class);
        fileInfo = new FMFile("MEDICATION ROUTES") {

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

    @FMAnnotateFieldInfo(name="NAME", number=".01", fieldType=FIELDTYPE.FREE_TEXT)
    protected String name;
    @FMAnnotateFieldInfo(name="ABBREVIATION", number="1", fieldType=FIELDTYPE.POINTER_TO_FILE)
    protected String abbreviation;
    @FMAnnotateFieldInfo(name="IV FLAG", number="6", fieldType=FIELDTYPE.WORD_PROCESSING)
    protected Boolean IVFlag;

    public FMMedicationRoute() {
        super("MEDICATION ROUTES");
    }

    public FMMedicationRoute(FMResultSet results) {
        super( results );
    }

    public String getName() {
        return name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public Boolean getIVFlag() {
        return IVFlag;
    }
    @Override
    public String toString() {
        return "\nname=[" + name + "]\n   abbreviation=[" + getAbbreviation() + "]\n   IV Flag=[" + getIVFlag() + "]";
    }

}
