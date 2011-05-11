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
import java.util.Map;
import java.util.Set;

import com.medsphere.fileman.FMAnnotateFieldInfo;
import com.medsphere.fileman.FMField;
import com.medsphere.fileman.FMFile;
import com.medsphere.fileman.FMRecord;
import com.medsphere.fileman.FMResultSet;

public class FMDrugSynonym extends FMRecord {

    /*-------------------------------------------------------------
     * begin static initialization
     *-------------------------------------------------------------*/
    private static Set<FMField> domainFields;
    private static FMFile fileInfo;
    private static Map<String, AnnotatedElement> domainJavaFields;
    private static Map<String, String> domainNumbers;

    static {
        domainJavaFields = getDomainJavaFields(FMDrugSynonym.class);
        domainFields = getFieldsInDomain(domainJavaFields);
        domainNumbers = getNumericMapping(FMDrugSynonym.class);
        fileInfo = new FMFile("SYNONYM") {

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

    @FMAnnotateFieldInfo( name="SYNONYM", number=".01", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    protected String synonym;
    @FMAnnotateFieldInfo( name="NDC CODE", number="2", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    protected String ndcCode;
    @FMAnnotateFieldInfo( name="INTENDED USE", number="1", fieldType=FMField.FIELDTYPE.SET_OF_CODES)
    protected String intendedUse;

    public FMDrugSynonym() {
        super("SYNONYM");
    }

    public FMDrugSynonym(FMResultSet results) {
        super( results );
    }

    public String getSynonym() {
        return synonym;
    }

    public void setSynonym(String synonym) {
        setDomainValue("synonym", synonym);
    }

    public String getNDCCode() {
        return ndcCode;
    }

    public void setNDCCode(String ndcCode) {
        setDomainValue("ndcCode", ndcCode);
    }

    public String getIntendedUse() {
        return intendedUse;
    }

    public void setIntendedUse(String intendedUse) {
        this.intendedUse = intendedUse;
    }

}
