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

public class FMPackage extends FMRecord {

    /*-------------------------------------------------------------
     * begin static initialization
     *-------------------------------------------------------------*/
    private static Set<FMField> domainFields;
    private static FMFile fileInfo;
    private static Map<String, AnnotatedElement> domainJavaFields;
    private static Map<String, String> domainNumbers;

    static {
        domainJavaFields = getDomainJavaFields(FMPackage.class);
        domainFields = getFieldsInDomain(domainJavaFields);
        domainNumbers = getNumericMapping(FMPackage.class);
        fileInfo = new FMFile("9.4") {

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

    // required constructors... 9.4 is the File number of the PACKAGE file
    public FMPackage() {
        super("9.4");
    }

    public FMPackage(FMResultSet results) {
        super(results);
    }

    // define annotated members
    @FMAnnotateFieldInfo( name="NAME", number=".01", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    protected String name;
    @FMAnnotateFieldInfo( name="PREFIX", number="1", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    protected String prefix;
    @FMAnnotateFieldInfo( name="SHORT DESCRIPTION", number="2", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    protected String shortDescription;
    @FMAnnotateFieldInfo( name="DEVELOPER (PERSON/SITE)", number="10", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    protected String developer;
    @FMAnnotateFieldInfo( name="DEVELOPMENT ISC", number="11.01", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    protected String developmentISC;
    @FMAnnotateFieldInfo( name="CURRENT VERSION", number="13", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    protected String currentVersion;
    @FMAnnotateFieldInfo( name="MOST RECENT PATCH", number="13.214", fieldType=FMField.FIELDTYPE.COMPUTED)
    protected String mostRecentPatch;
    @FMAnnotateFieldInfo( name="LATEST VERSION DATE", number="13.2141", fieldType=FMField.FIELDTYPE.DATE)
    protected Date latestVersionDate;

    public static FMFile getFileInfo() {
        return fileInfo;
    }

    public String getName() {
        return name;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getDeveloper() {
        return developer;
    }

    public String getDevelopmentISC() {
        return developmentISC;
    }

    public String getCurrentVersion() {
        return currentVersion;
    }

    public String getMostRecentPatch() {
        return mostRecentPatch;
    }

    public Date getLatestVersionDate() {
        return latestVersionDate;
    }

    @Override
    public String toString() {
        return
            "IEN=[" + getIEN() + "]"
            + " name=["+ name + "]"
            + " prefix=[" + prefix + "]"
            + " short description=[" + shortDescription + "]"
            + " developer=[" + developer + "]"
            + " development isc=[" + developmentISC + "]"
            + " current version=[" + currentVersion + "]"
            + " most recent patch=[" + mostRecentPatch + "]"
            + " latest version date=[" + latestVersionDate + "]"
        ;
    }

}
