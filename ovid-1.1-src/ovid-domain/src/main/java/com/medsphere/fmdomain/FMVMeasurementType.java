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

public class FMVMeasurementType extends FMRecord {
    /*-------------------------------------------------------------
     * begin static initialization
     *-------------------------------------------------------------*/
    private static Set<FMField> domainFields;
    private static FMFile fileInfo;
    private static Map<String, AnnotatedElement> domainJavaFields;
    private static Map<String, String> domainNumbers;

    static {
        domainJavaFields = getDomainJavaFields(FMVMeasurementType.class);
        domainFields = getFieldsInDomain(domainJavaFields);
        domainNumbers = getNumericMapping(FMVMeasurementType.class);
        fileInfo = new FMFile("MEASUREMENT TYPE") {

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

    @FMAnnotateFieldInfo(name = "TYPE", number = ".01", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String type;

    @FMAnnotateFieldInfo(name = "DESCRIPTION", number = ".02", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String description;

    @FMAnnotateFieldInfo(name = "CODE", number = ".03", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String code;

    @FMAnnotateFieldInfo(name = "INACTIVE", number = ".04", fieldType = FMField.FIELDTYPE.SET_OF_CODES)
    protected String inactive;

    @FMAnnotateFieldInfo(name = "CPT CODE", number = ".11", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE /*(81)*/)
    protected Integer cptCode;


    public FMVMeasurementType() {
        super("MEASUREMENT TYPE");
    }

    public FMVMeasurementType(FMResultSet results) {
        super("MEASUREMENT TYPE");
        processResults(results);
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public String getCode() {
        return code;
    }

    public String getInactive() {
        return inactive;
    }

    public String getCptCode() {
        return getValue(".11");
    }

    @Override
    public String toString() {
	return ""
    	+ ((getIEN() != null) ? " ien=["+ getIEN() +"]" : "")
	    + ((getType() != null) ? " type=["+ getType() +"]" : "")
	    + ((getDescription() != null) ? " description=["+ getDescription() +"]" : "")
	    + ((getCode() != null) ? " code=["+ getCode() +"]" : "")
	    + ((getInactive() != null) ? " inactive=["+ getInactive() +"]" : "")
	    + ((getCptCode() != null) ? " cptCode=["+ getCptCode() +"]" : "")
	    ;
    }

}
