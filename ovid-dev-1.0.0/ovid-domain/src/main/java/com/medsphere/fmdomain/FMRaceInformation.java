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
import com.medsphere.fileman.FMField.FIELDTYPE;

public class FMRaceInformation extends FMRecord {
    /*-------------------------------------------------------------
     * begin static initialization
     *-------------------------------------------------------------*/
    private static Set<FMField> domainFields;
    private static FMFile fileInfo;
    private static Map<String, AnnotatedElement> domainJavaFields;
    private static Map<String, String> domainNumbers;

    static {
        domainJavaFields = getDomainJavaFields(FMRaceInformation.class);
        domainFields = getFieldsInDomain(domainJavaFields);
        domainNumbers = getNumericMapping(FMRaceInformation.class);
        fileInfo = new FMFile("RACE INFORMATION") {

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

    @FMAnnotateFieldInfo(name="RACE INFORMATION", number=".01", fieldType=FIELDTYPE.POINTER_TO_FILE)
    protected Integer raceInformation;
    @FMAnnotateFieldInfo(name="METHOD OF COLLECTION", number=".02", fieldType=FIELDTYPE.POINTER_TO_FILE)
    protected Integer methodOfCollection;

    public FMRaceInformation() {
        super("RACE INFORMATION");
    }

    public FMRaceInformation(FMResultSet results) {
        super( results );
    }

    public Integer getRaceInformation() {
        return raceInformation;
    }

    public String getRaceInformationValue() {
        return getValue(".01");
    }

    public Integer getMethodOfCollection() {
        return methodOfCollection;
    }

    public String getMethodOfCollectionValue() {
        return getValue(".02");
    }

    @Override
    public String toString() {
        return "raceInformation=[" + getRaceInformationValue() + "] methodOfCollection=[" + getMethodOfCollectionValue() + "]";
    }
}
