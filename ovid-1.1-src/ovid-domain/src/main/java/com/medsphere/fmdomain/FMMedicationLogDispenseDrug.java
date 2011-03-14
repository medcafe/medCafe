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

/**
 *
 * @author Pete Johanson <pete.johanson@medsphere.com>
 */
public class FMMedicationLogDispenseDrug extends FMRecord {


    /*-------------------------------------------------------------
     * begin static initialization
     *-------------------------------------------------------------*/
    private static Set<FMField> domainFields;
    private static FMFile fileInfo;
    private static Map<String, AnnotatedElement> domainJavaFields;
    private static Map<String, String> domainNumbers;

    static {
        domainJavaFields = getDomainJavaFields(FMMedicationLogDispenseDrug.class);
        domainFields = getFieldsInDomain(domainJavaFields);
        domainNumbers = getNumericMapping(FMMedicationLogDispenseDrug.class);
        fileInfo = new FMFile("DISPENSE DRUG") {

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

    @FMAnnotateFieldInfo(name = "DISPENSE DRUG", number = ".01", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer dispenseDrug;
    @FMAnnotateFieldInfo(name = "DOSES ORDERED", number = ".02", fieldType = FMField.FIELDTYPE.NUMERIC)
    protected Double dosesOrdered;
    @FMAnnotateFieldInfo(name = "DOSES GIVEN", number = ".03", fieldType = FMField.FIELDTYPE.NUMERIC)
    protected Double dosesGiven;
    @FMAnnotateFieldInfo(name = "UNIT OF ADMINISTRATION", number = ".04", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String unitOfAdministration;

    public FMMedicationLogDispenseDrug() {
        super("DISPENSE DRUG");
    }

    public FMMedicationLogDispenseDrug(FMResultSet results) {
        super("DISPENSE DRUG");
        processResults(results);
    }
    
    public Integer getDispenseDrug() {
        return dispenseDrug;
    }

    public Double getDosesOrdered() {
        return dosesOrdered;
    }

    public Double getDosesGiven() {
        return dosesGiven;
    }

    public String getUnitOfAdministration() {
        return unitOfAdministration;
    }

}
