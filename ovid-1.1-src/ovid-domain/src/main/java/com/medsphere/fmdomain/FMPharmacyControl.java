// <editor-fold defaultstate="collapsed">
/*
 * Copyright (C) 2007-2010  Medsphere Systems Corporation
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
public class FMPharmacyControl extends FMRecord {

    /*-------------------------------------------------------------
     * begin static initialization
     *-------------------------------------------------------------*/
    private static Set<FMField> domainFields;
    private static FMFile fileInfo;
    private static Map<String, AnnotatedElement> domainJavaFields;
    private static Map<String, String> domainNumbers;

    static {
        domainJavaFields = getDomainJavaFields(FMPharmacyControl.class);
        domainFields = getFieldsInDomain(domainJavaFields);
        domainNumbers = getNumericMapping(FMPharmacyControl.class);
        fileInfo = new FMFile("APSP CONTROL") {

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
    @FMAnnotateFieldInfo( name="SHOW CHRONIC MED PROMPT", number="313", fieldType=FMField.FIELDTYPE.SET_OF_CODES)
    protected String showChronic;
    @FMAnnotateFieldInfo( name="MAIL/WINDOW OPTION", number="7", fieldType=FMField.FIELDTYPE.SET_OF_CODES)
    protected String showMailWindow;
    @FMAnnotateFieldInfo( name="SHOW CASH DUE PROMPT", number="319", fieldType=FMField.FIELDTYPE.SET_OF_CODES)
    protected String showCashDue;
    @FMAnnotateFieldInfo( name="DEFAULT PRINTER DEVICE", number="401", fieldType=FMField.FIELDTYPE.SET_OF_CODES)
    protected String defaultPrinterDevice;

    public FMPharmacyControl() {
        super("APSP CONTROL");
    }

    public FMPharmacyControl(FMResultSet results) {
        super("APSP CONTROL");
        processResults(results);
    }

    public String getShowChronic() {
        return showChronic;
    }

    public String getShowMailWindow() {
        return showMailWindow;
    }

    public String getShowCashDue() {
        return showCashDue;
    }

    public String getDefaultPrinterDevice() {
        return defaultPrinterDevice;
    }

    @Override
    public String toString() {
        return
        "chronic=["+showChronic+"]"
        +"   mail/window=["+showMailWindow+"]"
        +"  cash due=["+showCashDue+"]"
        ;
    }
}

