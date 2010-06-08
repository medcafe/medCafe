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

/**
 *
 */
public class FMTIUDocumentHeader extends FMRecord {
    /*-------------------------------------------------------------
     * begin static initialization
     *-------------------------------------------------------------*/
    private static Set<FMField> domainFields;
    private static FMFile fileInfo;
    private static Map<String, AnnotatedElement> domainJavaFields;
    private static Map<String, String> domainNumbers;

    static {
        domainJavaFields = getDomainJavaFields(FMTIUDocumentHeader.class);
        domainFields = getFieldsInDomain(domainJavaFields);
        domainNumbers = getNumericMapping(FMTIUDocumentHeader.class);
        fileInfo = new FMFile("8925") {

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

    public FMTIUDocumentHeader() {
        super(fileInfo.getFileName());
    }

    public FMTIUDocumentHeader(FMResultSet results) {
        super(results);
    }

    @FMAnnotateFieldInfo( name="DOCUMENT TYPE", number=".01", fieldType=FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer documentTypeID;
    @FMAnnotateFieldInfo(name="AUTHOR/DICTATOR", number="1202", fieldType=FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer authorID;
    @FMAnnotateFieldInfo( name="REFERENCE DATE", number="1301", fieldType=FMField.FIELDTYPE.DATE)
    protected Date referenceDate;

    public Integer getDocumentTypeID() {
        return documentTypeID;
    }

//    public void setDocumentTypeID(Integer documentTypeID) {
//        setDomainValue("documentTypeID", documentTypeID);
//    }

    public String getDocumentType() {
        return getValue(".01");
    }

    public Integer getAuthorID() {
        return authorID;
    }

    public String getAuthorName() {
        return getValue("1202");
    }
    public Date getReferenceDate() {
        return referenceDate;
    }


}
