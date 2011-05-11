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
import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import com.medsphere.fileman.FMAnnotateFieldInfo;
import com.medsphere.fileman.FMField;
import com.medsphere.fileman.FMFile;
import com.medsphere.fileman.FMRecord;
import com.medsphere.fileman.FMResultSet;

public class FMInstall extends FMRecord {

    /*-------------------------------------------------------------
     * begin static initialization
     *-------------------------------------------------------------*/
    private static Set<FMField> domainFields;
    private static FMFile fileInfo;
    private static Map<String, AnnotatedElement> domainJavaFields;
    private static Map<String, String> domainNumbers;

    static {
        domainJavaFields = getDomainJavaFields(FMInstall.class);
        domainFields = getFieldsInDomain(domainJavaFields);
        domainNumbers = getNumericMapping(FMInstall.class);
        fileInfo = new FMFile("9.7") {

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

    // required constructors... 9.7 is the File number of the INSTALL file
    public FMInstall() {
        super("9.7");
    }

    public FMInstall(FMResultSet results) {
        super(results);
    }

    // define annotated members
    @FMAnnotateFieldInfo( name="NAME", number=".01", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    protected String name;
    @FMAnnotateFieldInfo( name="STATUS", number=".02", fieldType=FMField.FIELDTYPE.SET_OF_CODES)
    protected String status;
    @FMAnnotateFieldInfo( name="PACKAGE FILE LINK", number="1", fieldType=FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer packageFileLink;
    @FMAnnotateFieldInfo( name="FILE COMMENT", number="6", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    protected String fileComment;
    @FMAnnotateFieldInfo( name="INSTALLED BY", number="9", fieldType=FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer installedBy;
    @FMAnnotateFieldInfo( name="INSTALL COMPLETE TIME", number="17", fieldType=FMField.FIELDTYPE.DATE)
    protected Date installCompleteTime;

    private FMPackage packageFile;

    // we aren't going to change any data, so we'll just define getters.
    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public Integer getPackageFileLink() {
        return packageFileLink;
    }

    public void setPackageFile(FMPackage packageFile) {
        this.packageFile = packageFile;
    }

    public FMPackage getPackageFile() {
        return packageFile;
    }

    public String getFileComment() {
        return fileComment;
    }

    public Integer getInstalledBy() {
        return installedBy;
    }

    // we want to get the string representation if the external value is requested.
    public String getInstalledByName() {
        return getValue("9");
    }

    public Date getInstallCompleteTime() {
        return installCompleteTime;
    }

    @Override
    public String toString() {

        return
                "IEN=[" + getIEN() + "]"
                + " name=["+ name + "]"
                + " status=[" + status + "]"
                + " package file link=[" + packageFileLink + "]"
                + " file comment=[" + fileComment + "]"
                + " installed by=[" + getInstalledByName() + "]"
                + " install complete time=[" + installCompleteTime + "]"
                ;
    }

    // implement a couple of comparators for sorting... these would be used by consumers of this class
    // when they want to sort.  See the java Collections class for more information.
    public class InstallDateComparator implements Comparator<FMInstall> {

        @Override
        public int compare(FMInstall arg0, FMInstall arg1) {
            if (arg0 != null && arg1 != null && arg0.getInstallCompleteTime() != null && arg1.getInstallCompleteTime() != null) {
                try {
                return (arg0.getInstallCompleteTime().compareTo(arg1.getInstallCompleteTime()));
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    return -1;
                }
            } else {
                return -1;
            }

        }

    }

    public class IENComparator implements Comparator<FMInstall> {

        @Override
        public int compare(FMInstall arg0, FMInstall arg1) {
            if (arg0 != null && arg1 != null) {
                return (arg0.getIEN().compareTo(arg1.getIEN()));
            } else {
                return -1;
            }

        }

    }

}
