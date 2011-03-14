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
import java.util.Date;
import java.util.Map;
import java.util.Set;

import com.medsphere.fileman.FMAnnotateFieldInfo;
import com.medsphere.fileman.FMField;
import com.medsphere.fileman.FMFile;
import com.medsphere.fileman.FMRecord;
import com.medsphere.fileman.FMResultSet;

public class FMPatientRefusalsForService extends FMRecord {
    /*-------------------------------------------------------------
     * begin static initialization
     *-------------------------------------------------------------*/
    private static Set<FMField> domainFields;
    private static FMFile fileInfo;
    private static Map<String, AnnotatedElement> domainJavaFields;
    private static Map<String, String> domainNumbers;

    static {
        domainJavaFields = getDomainJavaFields(FMPatientRefusalsForService.class);
        domainFields = getFieldsInDomain(domainJavaFields);
        domainNumbers = getNumericMapping(FMPatientRefusalsForService.class);
        fileInfo = new FMFile("9000022") {

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

    @FMAnnotateFieldInfo(name = "REFUSAL TYPE", number = ".01", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer refusalType;

    @FMAnnotateFieldInfo(name = "PATIENT NAME", number = ".02", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer patientName;

    @FMAnnotateFieldInfo(name = "DATE REFUSED/NOT INDICATED", number = ".03", fieldType = FMField.FIELDTYPE.DATE)
    protected Date dateRefusedOrNotIndicated;

    @FMAnnotateFieldInfo(name = "REFUSAL ITEM", number = ".04", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String refusalItem;

    @FMAnnotateFieldInfo(name = "POINTER FILE", number = ".05", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer pointerFile;

    @FMAnnotateFieldInfo(name = "POINTER VALUE", number = ".06", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String pointerValue;

    @FMAnnotateFieldInfo(name = "REASON FOR REFUSAL", number = ".07", fieldType = FMField.FIELDTYPE.SET_OF_CODES)
    protected String reasonForRefusal;

    @FMAnnotateFieldInfo(name = "COMMENT", number = "1101", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String comment;

    @FMAnnotateFieldInfo(name = "PROVIDER WHO DOCUMENTED", number = "1204", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer providerWhoDocumented;

    public FMPatientRefusalsForService() {
        super(fileInfo.getFileName());
    }

    public FMPatientRefusalsForService(FMResultSet results) {
        super(results);
    }

    public String getRefusalType() {
        return getValue(".01");
    }

    public String getPatientName() {
        return getValue(".02");
    }

    public Date getDateRefusedOrNotIndicated() {
        return dateRefusedOrNotIndicated;
    }

    public String getRefusalItem() {
        return refusalItem;
    }

    public String getPointerFile() {
        return getValue(".05");
    }

    public String getPointerValue() {
        return pointerValue;
    }

    public String getReasonForRefusal() {
        return reasonForRefusal;
    }

    public String getComment() {
        return comment;
    }

    public String getProviderWhoDocumented() {
        return getValue("1204");
    }

    @Override
    public String toString() {

	return ""
	    + ((getRefusalType() != null) ? " refusalType=["+ getRefusalType() +"]" : "")
	    + ((getPatientName() != null) ? " patientName=["+ getPatientName() +"]" : "")
	    + ((getDateRefusedOrNotIndicated() != null) ? " dateRefusedOrNotIndicated=["+ getDateRefusedOrNotIndicated() +"]" : "")
	    + ((getRefusalItem() != null) ? " refusalItem=["+ getRefusalItem() +"]" : "")
	    + ((getPointerFile() != null) ? " pointerFile=["+ getPointerFile() +"]" : "")
	    + ((getPointerValue() != null) ? " pointerValue=["+ getPointerValue() +"]" : "")
	    + ((getReasonForRefusal() != null) ? " reasonForRefusal=["+ getReasonForRefusal() +"]" : "")
	    + ((getComment() != null) ? " comment=["+ getComment() +"]" : "")
	    + ((getProviderWhoDocumented() != null) ? " providerWhoDocumented=["+ getProviderWhoDocumented() +"]" : "")
	    ;
    }
}
