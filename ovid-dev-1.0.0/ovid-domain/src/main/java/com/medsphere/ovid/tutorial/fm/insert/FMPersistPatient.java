package com.medsphere.ovid.tutorial.fm.insert;

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

public class FMPersistPatient extends FMRecord {

    /*-------------------------------------------------------------
     * begin static initialization
     *-------------------------------------------------------------*/
    private static Set<FMField> domainFields;
    private static FMFile fileInfo;
    private static Map<String, AnnotatedElement> domainJavaFields;
    private static Map<String, String> domainNumbers;

    static {
        domainJavaFields = getDomainJavaFields(FMPersistPatient.class);
        domainFields = getFieldsInDomain(domainJavaFields);
        domainNumbers = getNumericMapping(FMPersistPatient.class);
        fileInfo = new FMFile("PATIENT") {

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

    @FMAnnotateFieldInfo(name = "NAME", number = ".01", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    private String name;
    @FMAnnotateFieldInfo(name = "SEX", number = ".02", fieldType = FMField.FIELDTYPE.SET_OF_CODES)
    private String sex;
    @FMAnnotateFieldInfo(name = "DATE OF BIRTH", number = ".03", fieldType = FMField.FIELDTYPE.DATE)
    private Date dob;
    @FMAnnotateFieldInfo(name = "MARITAL STATUS", number = ".05", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    private Integer maritalStatus;

    public FMPersistPatient() {
        super(fileInfo.getFileName());
    }

    public FMPersistPatient(FMResultSet results) {
        super(results);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        setDomainValue("name", name);
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        setDomainValue("dob", dob);
    }

    public String getSex() {
        return getValue(".02");
    }

    public void setSex(String sex) {
        setDomainValue("sex", sex);
    }

    public String getMaritalStatus() {
        return getValue(".05");
    }

    public void setMaritalStatus(Integer maritalStatus) {
        setDomainValue("maritalStatus", maritalStatus);
    }

}
