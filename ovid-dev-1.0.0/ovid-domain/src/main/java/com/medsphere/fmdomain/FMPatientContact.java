package com.medsphere.fmdomain;

import java.lang.reflect.AnnotatedElement;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import com.medsphere.fileman.FMAnnotateFieldInfo;
import com.medsphere.fileman.FMField;
import com.medsphere.fileman.FMFile;
import com.medsphere.fileman.FMResultSet;

public class FMPatientContact extends FMDemographicPatient {

    private static Set<FMField> domainFields;
    private static FMFile fileInfo;
    private static Map<String, AnnotatedElement> domainJavaFields;
    private static Map<String, String> domainNumbers;

    static {
        domainJavaFields = getDomainJavaFields(FMPatientContact.class);
        domainFields = getFieldsInDomain(domainJavaFields);
        domainNumbers = getNumericMapping(FMPatientContact.class);
        fileInfo = new FMFile("2") { // need to use number because RPMS names file 2 'VA PATIENT'

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
    public FMPatientContact() {
        super();
    }

    public FMPatientContact(FMResultSet results) {
        processResults(results);
    }

    public static enum ContactType {

        EMERGENCY, GUARDIAN, NEXT_OF_KIN, EMPLOYER, SPOUSE_EMPLOYER, DESIGNEE
    }

    public class ContactInfo {

        private String name;
        private ContactType type;
        private String relationshipToPatient;
        private String street1;
        private String street2;
        private String city;
        private String state;
        private String zip;
        private String phoneNumber;
        private String altPhoneNumber;
        private boolean primary;
        private boolean vaGuard;

        public ContactInfo(String name, ContactType type,
                String relationshipToPatient, String street1, String street2,
                String city, String state, String zip, String phoneNumber,
                String altPhoneNumber) {
            super();
            this.name = name;
            this.type = type;
            this.relationshipToPatient = relationshipToPatient;
            this.street1 = street1;
            this.street2 = street2;
            this.city = city;
            this.state = state;
            this.zip = zip;
            this.phoneNumber = phoneNumber;
            this.altPhoneNumber = altPhoneNumber;
            primary = true;
            vaGuard = false;
        }

        public String getName() {
            return name;
        }

        public ContactType getType() {
            return type;
        }

        public String getRelationshipToPatient() {
            return relationshipToPatient;
        }

        public String getStreet1() {
            return street1;
        }

        public String getStreet2() {
            return street2;
        }

        public String getCity() {
            return city;
        }

        public String getState() {
            return state;
        }

        public String getZip() {
            return zip;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public String getAltPhoneNumber() {
            return altPhoneNumber;
        }

        public boolean hasMinimumData() {
            return (name != null && name.length() > 0);
        }

        @Override
        public String toString() {
            return " name=[" + name + "]"
                    + ", type=[" + type + "]"
                    + ", relationshipToPatient=[" + relationshipToPatient + "]"
                    + ", street1=[" + street1 + "]"
                    + ", street2=[" + street2 + "]"
                    + ", city=[" + city + "]"
                    + ", state=[" + state + "]"
                    + ", zip=[" + zip + "]"
                    + ", phoneNumber=[" + phoneNumber + "]"
                    + ", altPhoneNumber=[" + altPhoneNumber + "]";
        }

        public boolean isPrimary() {
            return primary;
        }

        public void setPrimary(boolean prime) {
            primary = prime;
        }

        public boolean isVAGuardian() {
            return vaGuard;
        }

        public void setVaGuardian(boolean vaGuard) {
            this.vaGuard = vaGuard;
        }
    }

    public Collection<ContactInfo> getContacts() {
        Collection<ContactInfo> contacts = new ArrayList<ContactInfo>();

        ContactInfo nok = new ContactInfo(getNokNameOfPrimary(), ContactType.NEXT_OF_KIN, getNokRelationshipToPatient(),
                getNokStreetAddressLine1(), getNokStreetAddressLine2(), getNokCity(), getNokStateValue(), getNokZipCode(),
                getNokPhoneNumber(), getNokWorkPhoneNumber());

        ContactInfo nok2 = new ContactInfo(getNok2NameOfSecondary(), ContactType.NEXT_OF_KIN, getNok2RelationshipToPatient(),
                getNok2StreetAddressLine1(), getNok2StreetAddressLine2(), getNok2City(), getNok2StateValue(), getNok2ZipCode(),
                getNok2PhoneNumber(), getNok2WorkPhoneNumber());
        nok2.setPrimary(false);

        ContactInfo emergency = new ContactInfo(getEmergencyName(), ContactType.EMERGENCY, getEmergencyRelationshipToPatient(),
                getEmergencyStreetAddressLine1(), getEmergencyStreetAddressLine2(), getEmergencyCity(), getEmergencyStateValue(), getEmergencyZipCode(),
                getEmergencyPhoneNumber(), getEmergencyWorkPhoneNumber());

        ContactInfo emergency2 = new ContactInfo(getEmergency2NameOfSecondaryContact(), ContactType.EMERGENCY, getEmergency2RelationshipToPatient(),
                getEmergency2StreetAddressLine1(), getEmergency2StreetAddressLine2(), getEmergency2City(), getEmergency2StateValue(), getEmergency2ZipCode(),
                getEmergency2PhoneNumber(), getEmergency2WorkPhoneNumber());
        emergency2.setPrimary(false);

        ContactInfo civilGuardian = new ContactInfo(getGuardianCivil(), ContactType.GUARDIAN, getCivilGuardianRelationship(),
                getCivilGuardianStreetAddress1(), getCivilGuardianStreetAddress2(), getCivilGuardianCity(), getCivilGuardianStateValue(), getCivilGuardianZip(),
                getCivilGuardianPhone(), null);

        ContactInfo vaGuardContact = new ContactInfo(getVaGuardian(), ContactType.GUARDIAN, getVaGuardianRelationship(),
                getVaGuardianStreetAddress1(), getVaGuardianStreetAddress2(), getVaGuardianCity(), getVaGuardianStateValue(), getVaGuardianZip(),
                getVaGuardianPhone(), null);
        vaGuardContact.setVaGuardian(true);

        ContactInfo employer = new ContactInfo(getEmployerName(), ContactType.EMPLOYER, "EMPLOYER",
                getEmployerStreetLine1(), getEmployerStreetLine2(), getEmployerCity(), getEmployerStateValue(), getEmployerZipCode(),
                getEmployerPhoneNumber(), null);

        ContactInfo spouseEmployer = new ContactInfo(getSpouseEmployerName(), ContactType.SPOUSE_EMPLOYER, "SPOUSE EMPLOYER",
                getSpouseEmployerStreetLine1(), getSpouseEmployerStreetLine2(), getSpouseEmployerCity(), getSpouseEmployerStateValue(), getSpouseEmployerZipCode(),
                getSpouseEmployerPhoneNumber(), null);

        ContactInfo designee = new ContactInfo(getDesigneeName(), ContactType.DESIGNEE, getDesigneeRelationshipToPatient(),
                getDesigneeStreetAddressLine1(), getDesigneeStreetAddressLine2(), getDesigneeCity(), getDesigneeStateValue(), getDesigneeZipCode(),
                getDesigneePhoneNumber(), getDesigneeWorkPhoneNumber());

        if (nok.hasMinimumData()) {
            contacts.add(nok);
        }
        if (nok2.hasMinimumData()) {
            contacts.add(nok2);
        }
        if (emergency.hasMinimumData()) {
            contacts.add(emergency);
        }
        if (emergency2.hasMinimumData()) {
            contacts.add(emergency2);
        }
        if (civilGuardian.hasMinimumData()) {
            contacts.add(civilGuardian);
        }
        if (vaGuardContact.hasMinimumData()) {
            contacts.add(vaGuardContact);
        }
        if (employer.hasMinimumData()) {
            contacts.add(employer);
        }
        if (spouseEmployer.hasMinimumData()) {
            contacts.add(spouseEmployer);
        }
        if (designee.hasMinimumData()) {
            contacts.add(designee);
        }

        return contacts;
    }
    // NOK info
    @FMAnnotateFieldInfo(name = "K-WORK PHONE NUMBER", number = ".21011", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String nokWorkPhoneNumber;
    @FMAnnotateFieldInfo(name = "PRIMARY NOK CHANGE DATE/TIME", number = ".21012", fieldType = FMField.FIELDTYPE.DATE)
    protected Date nokPrimaryChangeDateTime;
    @FMAnnotateFieldInfo(name = "K-NAME OF PRIMARY NOK", number = ".211", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String nokNameOfPrimary;
    @FMAnnotateFieldInfo(name = "SECONDARY NOK CHANGE DATE/TIME", number = ".211012", fieldType = FMField.FIELDTYPE.DATE)
    protected Date nokSecondaryChangeDateTime;
    @FMAnnotateFieldInfo(name = "K-RELATIONSHIP TO PATIENT", number = ".212", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String nokRelationshipToPatient;
    @FMAnnotateFieldInfo(name = "K-ADDRESS SAME AS PATIENT'S?", number = ".2125", fieldType = FMField.FIELDTYPE.SET_OF_CODES)
    protected String nokAddressSameAsPatients;
    @FMAnnotateFieldInfo(name = "K-STREET ADDRESS [LINE 1]", number = ".213", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String nokStreetAddressLine1;
    @FMAnnotateFieldInfo(name = "K-STREET ADDRESS [LINE 2]", number = ".214", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String nokStreetAddressLine2;
    @FMAnnotateFieldInfo(name = "K-STREET ADDRESS [LINE 3]", number = ".215", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String nokStreetAddressLine3;
    @FMAnnotateFieldInfo(name = "K-CITY", number = ".216", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String nokCity;
    @FMAnnotateFieldInfo(name = "K-STATE", number = ".217", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer nokState;
    @FMAnnotateFieldInfo(name = "K-ZIP CODE", number = ".218", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String nokZipCode;
    @FMAnnotateFieldInfo(name = "K-ZIP+4", number = ".2207", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String nokZip4;
    @FMAnnotateFieldInfo(name = "K-PHONE NUMBER", number = ".219", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String nokPhoneNumber;
    // NOK 2 Info
    @FMAnnotateFieldInfo(name = "K2-NAME OF SECONDARY NOK", number = ".2191", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String nok2NameOfSecondary;
    @FMAnnotateFieldInfo(name = "K2-RELATIONSHIP TO PATIENT", number = ".2192", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String nok2RelationshipToPatient;
    @FMAnnotateFieldInfo(name = "K2-ADDRESS SAME AS PATIENT'S?", number = ".21925", fieldType = FMField.FIELDTYPE.SET_OF_CODES)
    protected String nok2AddressSameAsPatients;
    @FMAnnotateFieldInfo(name = "K2-STREET ADDRESS [LINE 1]", number = ".2193", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String nok2StreetAddressLine1;
    @FMAnnotateFieldInfo(name = "K2-STREET ADDRESS [LINE 2]", number = ".2194", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String nok2StreetAddressLine2;
    @FMAnnotateFieldInfo(name = "K2-STREET ADDRESS [LINE 3]", number = ".2195", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String nok2StreetAddressLine3;
    @FMAnnotateFieldInfo(name = "K2-CITY", number = ".2196", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String nok2City;
    @FMAnnotateFieldInfo(name = "K2-STATE", number = ".2197", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer nok2State;
    @FMAnnotateFieldInfo(name = "K2-ZIP CODE", number = ".2198", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String nok2ZipCode;
    @FMAnnotateFieldInfo(name = "K2-PHONE NUMBER", number = ".2199", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String nok2PhoneNumber;
    @FMAnnotateFieldInfo(name = "K2-WORK PHONE NUMBER", number = ".211011", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String nok2WorkPhoneNumber;
    @FMAnnotateFieldInfo(name = "K2-ZIP+4", number = ".2203", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String nok2Zip4;
    // Spouse Employer Info
    @FMAnnotateFieldInfo(name = "SPOUSE'S EMPLOYER NAME", number = ".251", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String spouseEmployerName;
    @FMAnnotateFieldInfo(name = "SPOUSE'S OCCUPATION", number = ".2514", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String spouseOccupation;
    @FMAnnotateFieldInfo(name = "SPOUSE'S EMPLOYMENT STATUS", number = ".2515", fieldType = FMField.FIELDTYPE.SET_OF_CODES)
    protected String spouseEmploymentStatus;
    @FMAnnotateFieldInfo(name = "SPOUSE'S RETIREMENT DATE", number = ".2516", fieldType = FMField.FIELDTYPE.DATE)
    protected Date spouseRetirementDate;
    @FMAnnotateFieldInfo(name = "SPOUSE'S EMP STREET [LINE 1]", number = ".252", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String spouseEmployerStreetLine1;
    @FMAnnotateFieldInfo(name = "SPOUSE'S EMP STREET [LINE 2]", number = ".253", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String spouseEmployerStreetLine2;
    @FMAnnotateFieldInfo(name = "SPOUSE'S EMP STREET [LINE 3]", number = ".254", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String spouseEmployerStreetLine3;
    @FMAnnotateFieldInfo(name = "SPOUSE'S EMPLOYER'S CITY", number = ".255", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String spouseEmployerCity;
    @FMAnnotateFieldInfo(name = "SPOUSE'S EMPLOYER'S STATE", number = ".256", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer spouseEmployerState;
    @FMAnnotateFieldInfo(name = "SPOUSE'S EMP ZIP CODE", number = ".257", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String spouseEmployerZipCode;
    @FMAnnotateFieldInfo(name = "SPOUSE'S EMP ZIP+4", number = ".2206", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String spouseEmployerZip4;
    @FMAnnotateFieldInfo(name = "SPOUSE'S EMP PHONE NUMBER", number = ".258", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String spouseEmployerPhoneNumber;
    // Guardian VA
    @FMAnnotateFieldInfo(name = "GUARDIAN (VA)", number = ".2912", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String vaGuardian;
    @FMAnnotateFieldInfo(name = "RELATIONSHIP (VA)", number = ".2913", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String vaGuardianrelationship;
    @FMAnnotateFieldInfo(name = "STREET ADDRESS 1 (VA)", number = ".2914", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String vaGuardianStreetAddress1;
    @FMAnnotateFieldInfo(name = "STREET ADDRESS 2 (VA)", number = ".2915", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String vaGuardianStreetAddress2;
    @FMAnnotateFieldInfo(name = "CITY (VA)", number = ".2916", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String vaGuardianCity;
    @FMAnnotateFieldInfo(name = "STATE (VA)", number = ".2917", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer vaGuardianState;
    @FMAnnotateFieldInfo(name = "ZIP (VA)", number = ".2918", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String vaGuardianZip;
    @FMAnnotateFieldInfo(name = "ZIP+4 (VA)", number = ".29013", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String vaGuardianZip4;
    @FMAnnotateFieldInfo(name = "PHONE (VA)", number = ".2919", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String vaGuardianPhone;
    // Guardian Civil
    @FMAnnotateFieldInfo(name = "GUARDIAN (CIVIL)", number = ".2922", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String guardianCivil;
    @FMAnnotateFieldInfo(name = "INSTITUTION (CIVIL)", number = ".2921", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String civilGuardianInstitution;
    @FMAnnotateFieldInfo(name = "DATE RULED INCOMPETENT (CIVIL)", number = ".292", fieldType = FMField.FIELDTYPE.DATE)
    protected Date civilGuardianDateRuledIncompetent;
    @FMAnnotateFieldInfo(name = "RELATIONSHIP (CIVIL)", number = ".2923", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String civilGuardianRelationship;
    @FMAnnotateFieldInfo(name = "STREET ADDRESS 1 (CIVIL)", number = ".2924", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String civilGuardianStreetAddress1;
    @FMAnnotateFieldInfo(name = "STREET ADDRESS 2 (CIVIL)", number = ".2925", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String civilGuardianStreetAddress2;
    @FMAnnotateFieldInfo(name = "CITY (CIVIL)", number = ".2926", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String civilGuardianCity;
    @FMAnnotateFieldInfo(name = "STATE (CIVIL)", number = ".2927", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer civilGuardianState;
    @FMAnnotateFieldInfo(name = "ZIP (CIVIL)", number = ".2928", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String civilGuardianZip;
    @FMAnnotateFieldInfo(name = "ZIP+4 (CIVIL)", number = ".290012", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String civilGuardianZip4;
    @FMAnnotateFieldInfo(name = "PHONE (CIVIL)", number = ".2929", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String civilGuardianPhone;
    // Employer
    @FMAnnotateFieldInfo(name = "EMPLOYER NAME", number = ".3111", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String employerName;
    @FMAnnotateFieldInfo(name = "EMPLOYMENT STATUS", number = ".31115", fieldType = FMField.FIELDTYPE.SET_OF_CODES)
    protected String employmentStatus;
    @FMAnnotateFieldInfo(name = "DATE OF RETIREMENT", number = ".31116", fieldType = FMField.FIELDTYPE.DATE)
    protected Date dateOfRetirement;
    @FMAnnotateFieldInfo(name = "GOVERNMENT AGENCY", number = ".3112", fieldType = FMField.FIELDTYPE.SET_OF_CODES)
    protected String governmentAgency;
    @FMAnnotateFieldInfo(name = "EMPLOYER STREET [LINE 1]", number = ".3113", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String employerStreetLine1;
    @FMAnnotateFieldInfo(name = "EMPLOYER STREET [LINE 2]", number = ".3114", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String employerStreetLine2;
    @FMAnnotateFieldInfo(name = "EMPLOYER STREET [LINE 3]", number = ".3115", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String employerStreetLine3;
    @FMAnnotateFieldInfo(name = "EMPLOYER CITY", number = ".3116", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String employerCity;
    @FMAnnotateFieldInfo(name = "EMPLOYER STATE", number = ".3117", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer employerState;
    @FMAnnotateFieldInfo(name = "EMPLOYER ZIP CODE", number = ".3118", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String employerZipCode;
    @FMAnnotateFieldInfo(name = "EMPLOYER ZIP+4", number = ".2205", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String employerZip4;
    @FMAnnotateFieldInfo(name = "EMPLOYER PHONE NUMBER", number = ".3119", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String employerPhoneNumber;
    // Emergency contact
    @FMAnnotateFieldInfo(name = "E-NAME", number = ".331", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String emergencyName;
    @FMAnnotateFieldInfo(name = "E-CONTACT CHANGE DATE/TIME", number = ".33012", fieldType = FMField.FIELDTYPE.DATE)
    protected Date emergencyContactChangeDateTime;
    @FMAnnotateFieldInfo(name = "E-EMER. CONTACT SAME AS NOK?", number = ".3305", fieldType = FMField.FIELDTYPE.SET_OF_CODES)
    protected String emergencyContactSameAsNok;
    @FMAnnotateFieldInfo(name = "E-RELATIONSHIP TO PATIENT", number = ".332", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String emergencyRelationshipToPatient;
    @FMAnnotateFieldInfo(name = "E-STREET ADDRESS [LINE 1]", number = ".333", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String emergencyStreetAddressLine1;
    @FMAnnotateFieldInfo(name = "E-STREET ADDRESS [LINE 2]", number = ".334", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String emergencyStreetAddressLine2;
    @FMAnnotateFieldInfo(name = "E-STREET ADDRESS [LINE 3]", number = ".335", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String emergencyStreetAddressLine3;
    @FMAnnotateFieldInfo(name = "E-CITY", number = ".336", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String emergencyCity;
    @FMAnnotateFieldInfo(name = "E-STATE", number = ".337", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer emergencyState;
    @FMAnnotateFieldInfo(name = "E-ZIP CODE", number = ".338", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String emergencyZipCode;
    @FMAnnotateFieldInfo(name = "E-ZIP+4", number = ".2201", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String emergencyZip4;
    @FMAnnotateFieldInfo(name = "E-PHONE NUMBER", number = ".339", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String emergencyPhoneNumber;
    @FMAnnotateFieldInfo(name = "E-WORK PHONE NUMBER", number = ".33011", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String emergencyWorkPhoneNumber;
    // Emergency 2 Contact
    @FMAnnotateFieldInfo(name = "E2-NAME OF SECONDARY CONTACT", number = ".3311", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String emergency2NameOfSecondaryContact;
    @FMAnnotateFieldInfo(name = "E2-CONTACT CHANGE DATE/TIME", number = ".33112", fieldType = FMField.FIELDTYPE.DATE)
    protected Date emergency2ContactChangeDateTime;
    @FMAnnotateFieldInfo(name = "E2-RELATIONSHIP TO PATIENT", number = ".3312", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String emergency2RelationshipToPatient;
    @FMAnnotateFieldInfo(name = "E2-STREET ADDRESS [LINE 1]", number = ".3313", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String emergency2StreetAddressLine1;
    @FMAnnotateFieldInfo(name = "E2-STREET ADDRESS [LINE 2]", number = ".3314", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String emergency2StreetAddressLine2;
    @FMAnnotateFieldInfo(name = "E2-STREET ADDRESS [LINE 3]", number = ".3315", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String emergency2StreetAddressLine3;
    @FMAnnotateFieldInfo(name = "E2-CITY", number = ".3316", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String emergency2City;
    @FMAnnotateFieldInfo(name = "E2-STATE", number = ".3317", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer emergency2State;
    @FMAnnotateFieldInfo(name = "E2-ZIP CODE", number = ".3318", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String emergency2ZipCode;
    @FMAnnotateFieldInfo(name = "E2-ZIP+4", number = ".2204", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String emergency2Zip4;
    @FMAnnotateFieldInfo(name = "E2-PHONE NUMBER", number = ".3319", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String emergency2PhoneNumber;
    @FMAnnotateFieldInfo(name = "E2-WORK PHONE NUMBER", number = ".331011", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String emergency2WorkPhoneNumber;
    // Designee
    @FMAnnotateFieldInfo(name = "D-NAME OF DESIGNEE", number = ".341", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String designeeName;
    @FMAnnotateFieldInfo(name = "D-DESIGNEE SAME AS NOK?", number = ".3405", fieldType = FMField.FIELDTYPE.SET_OF_CODES)
    protected String designeeSameAsNok;
    @FMAnnotateFieldInfo(name = "DESIGNEE CHANGE DATE/TIME", number = ".3412", fieldType = FMField.FIELDTYPE.DATE)
    protected Date designeeChangeDateTime;
    @FMAnnotateFieldInfo(name = "D-RELATIONSHIP TO PATIENT", number = ".342", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String designeeRelationshipToPatient;
    @FMAnnotateFieldInfo(name = "D-STREET ADDRESS [LINE 1]", number = ".343", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String designeeStreetAddressLine1;
    @FMAnnotateFieldInfo(name = "D-STREET ADDRESS [LINE 2]", number = ".344", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String designeeStreetAddressLine2;
    @FMAnnotateFieldInfo(name = "D-STREET ADDRESS [LINE 3]", number = ".345", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String designeeStreetAddressLine3;
    @FMAnnotateFieldInfo(name = "D-CITY", number = ".346", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String designeeCity;
    @FMAnnotateFieldInfo(name = "D-STATE", number = ".347", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer designeeState;
    @FMAnnotateFieldInfo(name = "D-ZIP CODE", number = ".348", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String designeeZipCode;
    @FMAnnotateFieldInfo(name = "D-ZIP+4", number = ".2202", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String designeeZip4;
    @FMAnnotateFieldInfo(name = "D-PHONE NUMBER", number = ".349", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String designeePhoneNumber;
    @FMAnnotateFieldInfo(name = "D-WORK PHONE NUMBER", number = ".34011", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String designeeWorkPhoneNumber;
    @FMAnnotateFieldInfo(name = "DATE RULED INCOMPETENT (VA)", number = ".291", fieldType = FMField.FIELDTYPE.DATE)
    protected Date incompVADate;
    @FMAnnotateFieldInfo(name = "DATE RULED INCOMPETENT (CIVIL)", number = ".292", fieldType = FMField.FIELDTYPE.DATE)
    protected Date incompCivilDate;
    @FMAnnotateFieldInfo(name = "RATED INCOMPETENT?", number = ".293", fieldType = FMField.FIELDTYPE.SET_OF_CODES)
    protected String ratedIncompetent;

    public String getNokNameOfPrimary() {
        return nokNameOfPrimary;
    }

    public String getNokWorkPhoneNumber() {
        return nokWorkPhoneNumber;
    }

    public Date getNokPrimaryChangeDateTime() {
        return nokPrimaryChangeDateTime;
    }

    public Date getNokSecondaryChangeDateTime() {
        return nokSecondaryChangeDateTime;
    }

    public String getNokRelationshipToPatient() {
        return nokRelationshipToPatient;
    }

    public String getNokAddressSameAsPatients() {
        return nokAddressSameAsPatients;
    }

    public String getNokStreetAddressLine1() {
        return nokStreetAddressLine1;
    }

    public String getNokStreetAddressLine2() {
        return nokStreetAddressLine2;
    }

    public String getNokStreetAddressLine3() {
        return nokStreetAddressLine3;
    }

    public String getNokCity() {
        return nokCity;
    }

    public Integer getNokState() {
        return nokState;
    }

    public String getNokStateValue() {
        return getValue(".217");
    }

    public String getNokZipCode() {
        return nokZipCode;
    }

    public String getNokZip4() {
        return nokZip4;
    }

    public String getNokPhoneNumber() {
        return nokPhoneNumber;
    }

    public String getNok2NameOfSecondary() {
        return nok2NameOfSecondary;
    }

    public String getNok2RelationshipToPatient() {
        return nok2RelationshipToPatient;
    }

    public String getNok2AddressSameAsPatients() {
        return nok2AddressSameAsPatients;
    }

    public String getNok2StreetAddressLine1() {
        return nok2StreetAddressLine1;
    }

    public String getNok2StreetAddressLine2() {
        return nok2StreetAddressLine2;
    }

    public String getNok2StreetAddressLine3() {
        return nok2StreetAddressLine3;
    }

    public String getNok2City() {
        return nok2City;
    }

    public Integer getNok2State() {
        return nok2State;
    }

    public String getNok2StateValue() {
        return getValue(".2197");
    }

    public String getNok2ZipCode() {
        return nok2ZipCode;
    }

    public String getNok2PhoneNumber() {
        return nok2PhoneNumber;
    }

    public String getNok2WorkPhoneNumber() {
        return nok2WorkPhoneNumber;
    }

    public String getNok2Zip4() {
        return nok2Zip4;
    }

    public String getSpouseEmployerName() {
        return spouseEmployerName;
    }

    public String getSpouseOccupation() {
        return spouseOccupation;
    }

    public String getSpouseEmploymentStatus() {
        return spouseEmploymentStatus;
    }

    public Date getSpouseRetirementDate() {
        return spouseRetirementDate;
    }

    public String getSpouseEmployerStreetLine1() {
        return spouseEmployerStreetLine1;
    }

    public String getSpouseEmployerStreetLine2() {
        return spouseEmployerStreetLine2;
    }

    public String getSpouseEmployerStreetLine3() {
        return spouseEmployerStreetLine3;
    }

    public String getSpouseEmployerCity() {
        return spouseEmployerCity;
    }

    public Integer getSpouseEmployerState() {
        return spouseEmployerState;
    }

    public String getSpouseEmployerStateValue() {
        return getValue(".256");
    }

    public String getSpouseEmployerZipCode() {
        return spouseEmployerZipCode;
    }

    public String getSpouseEmployerZip4() {
        return spouseEmployerZip4;
    }

    public String getSpouseEmployerPhoneNumber() {
        return spouseEmployerPhoneNumber;
    }

    public String getVaGuardian() {
        return vaGuardian;
    }

    public String getVaGuardianRelationship() {
        return vaGuardianrelationship;
    }

    public String getVaGuardianStreetAddress1() {
        return vaGuardianStreetAddress1;
    }

    public String getVaGuardianStreetAddress2() {
        return vaGuardianStreetAddress2;
    }

    public String getVaGuardianCity() {
        return vaGuardianCity;
    }

    public Integer getVaGuardianState() {
        return vaGuardianState;
    }

    public String getVaGuardianStateValue() {
        return getValue(".2917");
    }

    public String getVaGuardianZip() {
        return vaGuardianZip;
    }

    public String getVaGuardianZip4() {
        return vaGuardianZip4;
    }

    public String getVaGuardianPhone() {
        return vaGuardianPhone;
    }

    public String getGuardianCivil() {
        return guardianCivil;
    }

    public String getCivilGuardianInstitution() {
        return civilGuardianInstitution;
    }

    public Date getCivilGuardianDateRuledIncompetent() {
        return civilGuardianDateRuledIncompetent;
    }

    public String getCivilGuardianRelationship() {
        return civilGuardianRelationship;
    }

    public String getCivilGuardianStreetAddress1() {
        return civilGuardianStreetAddress1;
    }

    public String getCivilGuardianStreetAddress2() {
        return civilGuardianStreetAddress2;
    }

    public String getCivilGuardianCity() {
        return civilGuardianCity;
    }

    public Integer getCivilGuardianState() {
        return civilGuardianState;
    }

    public String getCivilGuardianStateValue() {
        return getValue(".2927");
    }

    public String getCivilGuardianZip() {
        return civilGuardianZip;
    }

    public String getCivilGuardianZip4() {
        return civilGuardianZip4;
    }

    public String getCivilGuardianPhone() {
        return civilGuardianPhone;
    }

    public String getEmployerName() {
        return employerName;
    }

    public String getEmploymentStatus() {
        return employmentStatus;
    }

    public Date getDateOfRetirement() {
        return dateOfRetirement;
    }

    public String getGovernmentAgency() {
        return governmentAgency;
    }

    public String getEmployerStreetLine1() {
        return employerStreetLine1;
    }

    public String getEmployerStreetLine2() {
        return employerStreetLine2;
    }

    public String getEmployerStreetLine3() {
        return employerStreetLine3;
    }

    public String getEmployerCity() {
        return employerCity;
    }

    public Integer getEmployerState() {
        return employerState;
    }

    public String getEmployerStateValue() {
        return getValue(".3117");
    }

    public String getEmployerZipCode() {
        return employerZipCode;
    }

    public String getEmployerZip4() {
        return employerZip4;
    }

    public String getEmployerPhoneNumber() {
        return employerPhoneNumber;
    }

    public String getEmergencyName() {
        return emergencyName;
    }

    public Date getEmergencyContactChangeDateTime() {
        return emergencyContactChangeDateTime;
    }

    public String getEmergencyContactSameAsNok() {
        return emergencyContactSameAsNok;
    }

    public String getEmergencyRelationshipToPatient() {
        return emergencyRelationshipToPatient;
    }

    public String getEmergencyStreetAddressLine1() {
        return emergencyStreetAddressLine1;
    }

    public String getEmergencyStreetAddressLine2() {
        return emergencyStreetAddressLine2;
    }

    public String getEmergencyStreetAddressLine3() {
        return emergencyStreetAddressLine3;
    }

    public String getEmergencyCity() {
        return emergencyCity;
    }

    public Integer getEmergencyState() {
        return emergencyState;
    }

    public String getEmergencyStateValue() {
        return getValue(".337");
    }

    public String getEmergencyZipCode() {
        return emergencyZipCode;
    }

    public String getEmergencyZip4() {
        return emergencyZip4;
    }

    public String getEmergencyPhoneNumber() {
        return emergencyPhoneNumber;
    }

    public String getEmergencyWorkPhoneNumber() {
        return emergencyWorkPhoneNumber;
    }

    public String getEmergency2NameOfSecondaryContact() {
        return emergency2NameOfSecondaryContact;
    }

    public Date getEmergency2ContactChangeDateTime() {
        return emergency2ContactChangeDateTime;
    }

    public String getEmergency2RelationshipToPatient() {
        return emergency2RelationshipToPatient;
    }

    public String getEmergency2StreetAddressLine1() {
        return emergency2StreetAddressLine1;
    }

    public String getEmergency2StreetAddressLine2() {
        return emergency2StreetAddressLine2;
    }

    public String getEmergency2StreetAddressLine3() {
        return emergency2StreetAddressLine3;
    }

    public String getEmergency2City() {
        return emergency2City;
    }

    public Integer getEmergency2State() {
        return emergency2State;
    }

    public String getEmergency2StateValue() {
        return getValue(".3317");
    }

    public String getEmergency2ZipCode() {
        return emergency2ZipCode;
    }

    public String getEmergency2Zip4() {
        return emergency2Zip4;
    }

    public String getEmergency2PhoneNumber() {
        return emergency2PhoneNumber;
    }

    public String getEmergency2WorkPhoneNumber() {
        return emergency2WorkPhoneNumber;
    }

    public String getDesigneeName() {
        return designeeName;
    }

    public String getDesigneeSameAsNok() {
        return designeeSameAsNok;
    }

    public Date getDesigneeChangeDateTime() {
        return designeeChangeDateTime;
    }

    public String getDesigneeRelationshipToPatient() {
        return designeeRelationshipToPatient;
    }

    public String getDesigneeStreetAddressLine1() {
        return designeeStreetAddressLine1;
    }

    public String getDesigneeStreetAddressLine2() {
        return designeeStreetAddressLine2;
    }

    public String getDesigneeStreetAddressLine3() {
        return designeeStreetAddressLine3;
    }

    public String getDesigneeCity() {
        return designeeCity;
    }

    public Integer getDesigneeState() {
        return designeeState;
    }

    public String getDesigneeStateValue() {
        return getValue(".347");
    }

    public String getDesigneeZipCode() {
        return designeeZipCode;
    }

    public String getDesigneeZip4() {
        return designeeZip4;
    }

    public String getDesigneePhoneNumber() {
        return designeePhoneNumber;
    }

    public String getDesigneeWorkPhoneNumber() {
        return designeeWorkPhoneNumber;
    }

// added
    public boolean hasVAGuardian() {
        return (incompVADate.after(new Date(0, 1, 1)));
    }

    public boolean hasCivilGuardian() {
        return (incompVADate.after(new Date(0, 1, 1)) || age < 18);

    }

    public boolean ratedIncompetent() {
        if (ratedIncompetent != null){
        return (ratedIncompetent.equals("YES"));
        }
        else
            return false;
    }

    public void setNokNameOfPrimary(String name) {
         setDomainValue("nokNameOfPrimary", name);
    }

    public void setNokWorkPhoneNumber(String phoneNo) {
         setDomainValue("nokWorkPhoneNumber", phoneNo);
    }

    public void setNokPrimaryChangeDateTime(Date date) {
         setDomainValue("nokPrimaryChangeDateTime", date);
    }

    public void setNokSecondaryChangeDateTime(Date date) {
         setDomainValue("nokSecondaryChangeDateTime", date);
    }

    public void setNokRelationshipToPatient(String relation) {
         setDomainValue("nokRelationshipToPatient", relation);
    }

    public boolean isNokAddressSameAsPatients() {
        return (nokAddressSameAsPatients.equals("Y"));
    }

    public void setNokAddressSameAsPatients(boolean sameAddress) {
        if (sameAddress) {
             setDomainValue("nokAddressSameAsPatients", "Y");
        } else {
             setDomainValue("nokAddressSameAsPatients", "N");
        }
    }

    public void setNokStreetAddressLine1(String addressLine) {
         setDomainValue("nokStreetAddressLine1", addressLine);
    }

    public void setNokStreetAddressLine2(String addressLine) {
         setDomainValue("nokStreetAddressLine2", addressLine);
    }

    public void setNokStreetAddressLine3(String addressLine) {
         setDomainValue("nokStreetAddressLine3", addressLine);
    }

    public void setNokCity(String city) {
        setDomainValue("nokCity",  city);
    }

    public void setNokState(FMState state) {
        setDomainValue("nokState", Integer.parseInt(state.getIEN()));
    }

    public void setNokZipCode(String zip) {
        setDomainValue("nokZipCode", ((zip.length() > 5) ? zip.substring(0, 5) : zip));
        setDomainValue("nokZip4", zip);

    }

    public void setNokPhoneNumber(String phone) {
         setDomainValue("nokPhoneNumber", phone);
    }

    public void setNok2NameOfSecondary(String name) {
         setDomainValue("nok2NameOfSecondary", name);
    }

    public void setNok2RelationshipToPatient(String relation) {
         setDomainValue("nok2RelationshipToPatient", relation);
    }

    public boolean isNok2AddressSameAsPatients() {
        return (nok2AddressSameAsPatients.compareTo("1") == 0);
    }

    public void setNok2AddressSameAsPatients(boolean same) {
        if (same) {
             setDomainValue("nok2AddressSameAsPatients", "Y");
        } else {
             setDomainValue("nok2AddressSameAsPatients", "N");
        }
    }

    public void setNok2StreetAddressLine1(String addressLine) {
         setDomainValue("nok2StreetAddressLine1", addressLine);
    }

    public void setNok2StreetAddressLine2(String addressLine) {
         setDomainValue("nok2StreetAddressLine2", addressLine);
    }

    public void setNok2StreetAddressLine3(String addressLine) {
         setDomainValue("nok2StreetAddressLine3",  addressLine);
    }

    public void setNok2City(String city) {
         setDomainValue("nok2City", city);
    }

    public void setNok2State(FMState state) {
        setDomainValue("nok2State", Integer.parseInt(state.getIEN()));
    }

    public void setNok2ZipCode(String zip) {
        setDomainValue("nok2ZipCode", ((zip.length() > 5) ? zip.substring(0, 5) : zip));
        setDomainValue("nok2Zip4", zip);
    }

    public void setNok2PhoneNumber(String phone) {
         setDomainValue("nok2PhoneNumber", phone);
    }

    public void setNok2WorkPhoneNumber(String phone) {
         setDomainValue("nok2WorkPhoneNumber", phone);
    }

    public void setSpouseEmployerName(String name) {
         setDomainValue("spouseEmployerName", name);
    }

    public void setSpouseOccupation(String occupation) {
         setDomainValue("spouseOccupation", occupation);
    }

    public void setSpouseEmploymentStatus(String status) {
         setDomainValue("spouseEmploymentStatus", status);
    }

    public void setSpouseRetirementDate(Date date) {
         setDomainValue("spouseRetirementDate", date);
    }

    public void setSpouseEmployerStreetLine1(String addressLine) {
         setDomainValue("spouseEmployerStreetLine1", addressLine);
    }

    public void setSpouseEmployerStreetLine2(String addressLine) {
         setDomainValue("spouseEmployerStreetLine2", addressLine);
    }

    public void setSpouseEmployerStreetLine3(String addressLine) {
         setDomainValue("spouseEmployerStreetLine3", addressLine);
    }

    public void setSpouseEmployerCity(String city) {
         setDomainValue("spouseEmployerCity", city);
    }

    public void setSpouseEmployerState(FMState state) {
        setDomainValue("spouseEmployerState", Integer.parseInt(state.getIEN()));
    }

    public void setSpouseEmployerZipCode(String zip) {
         setDomainValue("spouseEmployerZipCode", ((zip.length() > 5) ? zip.substring(0, 5) : zip));
         setDomainValue("spouseEmployerZip4", zip);
    }

    public void setSpouseEmployerPhoneNumber(String phone) {
         setDomainValue("spouseEmployerPhoneNumber", phone);
    }

    public void setVaGuardian(String name) {
         setDomainValue("vaGuardian",  name);
    }

    public void setVaGuardianRelationship(String relation) {
         setDomainValue("vaGuardianrelationship", relation);
    }

    public void setVaGuardianStreetAddress1(String addressLine) {
         setDomainValue("vaGuardianStreetAddress1",  addressLine);
    }

    public void setVaGuardianStreetAddress2(String addressLine) {
         setDomainValue("vaGuardianStreetAddress2", addressLine);
    }

    public void setVaGuardianCity(String city) {
         setDomainValue("vaGuardianCity", city);
    }

    public void setVaGuardianState(FMState state) {
        setDomainValue("vaGuardianState", Integer.parseInt(state.getIEN()));
    }

    public void setVaGuardianZip(String zip) {
         setDomainValue("vaGuardianZip", ((zip.length() > 5) ? zip.substring(0, 5) : zip));
         setDomainValue("vaGuardianZip4", zip);
    }

    public void setVaGuardianPhone(String phone) {
         setDomainValue("vaGuardianPhone",  phone);
    }

    public void setGuardianCivil(String name) {
        setDomainValue("guardianCivil", name);

    }

    public void setCivilGuardianInstitution(String institution) {
        setDomainValue("civilGuardianInstitution", institution);
    }

    public void setCivilGuardianDateRuledIncompetent(Date date) {
        setDomainValue("civilGuardianDateRuledIncompetent", date);

    }

    public void setCivilGuardianRelationship(String relation) {
        setDomainValue("civilGuardianRelationship", relation);

    }

    public void setCivilGuardianStreetAddress1(String addressLine) {
        setDomainValue("civilGuardianStreetAddress1", addressLine);
    }

    public void setCivilGuardianStreetAddress2(String addressLine) {
        setDomainValue("civilGuardianStreetAddress2", addressLine);
    }

    public void setCivilGuardianCity(String city) {
        setDomainValue("civilGuardianCity", city);
    }

    public void setCivilGuardianState(FMState state) {
        setDomainValue("civilGuardianState", Integer.parseInt(state.getIEN()));
    }

    public void setCivilGuardianZip(String zip) {
        setDomainValue("civilGuardianZip", ((zip.length() > 5) ? zip.substring(0, 5) : zip));
        setDomainValue("civilGuardianZip4", zip);
    }

    public void setCivilGuardianPhone(String phone) {
        setDomainValue("civilGuardianPhone", phone);
    }

    public void setEmployerName(String name) {
        setDomainValue("employerName", name);
    }

    public void setEmploymentStatus(String status) {
        setDomainValue("employmentStatus", status);
    }

    public void setDateOfRetirement(Date date) {
        setDomainValue("dateOfRetirement", date);
    }

    public void setGovernmentAgency(String agencyName) {
        setDomainValue("governmentAgency", agencyName);
    }

    public void setEmployerStreetLine1(String addrLine) {
        setDomainValue("employerStreetLine1", addrLine);
    }

    public void setEmployerStreetLine2(String addrLine) {
        setDomainValue("employerStreetLine2", addrLine);
    }

    public void setEmployerStreetLine3(String addrLine) {
        setDomainValue("employerStreetLine3", addrLine);
    }

    public void setEmployerCity(String city) {
        setDomainValue("employerCity", city);
    }

    public void setEmployerState(FMState state) {
        setDomainValue("employerState", Integer.parseInt(state.getIEN()));
    }

    public void setEmployerZipCode(String zip) {
        setDomainValue("employerZipCode", ((zip.length() > 5) ? zip.substring(0, 5) : zip));
        setDomainValue("employerZip4", zip);
    }

    public void setEmployerPhoneNumber(String phone) {
        setDomainValue("employerPhoneNumber", phone);
    }

    public void setEmergencyName(String name) {
        setDomainValue("emergencyName", name);
    }

    public void setEmergencyContactChangeDateTime(Date date) {
        setDomainValue("emergencyContactChangeDateTime", date);
    }

    public boolean isEmergencyContactSameAsNok() {
        return (emergencyContactSameAsNok.compareTo("Y") == 0);
    }

    public void setEmergencyContactSameAsNok(boolean same) {
        if (same) {
            setDomainValue("emergencyContactSameAsNok", "Y");
        } else {
            setDomainValue("emergencyContactSameAsNok", "N");
        }
    }

    public void setEmergencyRelationshipToPatient(String relation) {
        setDomainValue("emergencyRelationshipToPatient", relation);
    }

    public void setEmergencyStreetAddressLine1(String addrLine) {
        setDomainValue("emergencyStreetAddressLine1", addrLine);
    }

    public void setEmergencyStreetAddressLine2(String addrLine) {
        setDomainValue("emergencyStreetAddressLine2", addrLine);
    }

    public void setEmergencyStreetAddressLine3(String addrLine) {
        setDomainValue("emergencyStreetAddressLine3", addrLine);
    }

    public void setEmergencyCity(String city) {
        setDomainValue("emergencyCity", city);
    }

    public void setEmergencyState(FMState state) {
        setDomainValue("emergencyState", Integer.parseInt(state.getIEN()));
    }

    public void setEmergencyZipCode(String zip) {
        setDomainValue("emergencyZipCode", ((zip.length() > 5) ? zip.substring(0, 5) : zip));
        setDomainValue("emergencyZip4", zip);
    }

    public void setEmergencyPhoneNumber(String phone) {
        setDomainValue("emergencyPhoneNumber", phone);
    }

    public void setEmergencyWorkPhoneNumber(String phone) {
        setDomainValue("emergencyWorkPhoneNumber", phone);
    }

    public void setEmergency2NameOfSecondaryContact(String name) {
        setDomainValue("emergency2NameOfSecondaryContact", name);
    }

    public void setEmergency2ContactChangeDateTime(Date date) {
        setDomainValue("emergency2ContactChangeDateTime", date);
    }

    public void setEmergency2RelationshipToPatient(String relation) {
        setDomainValue("emergency2RelationshipToPatient", relation);
    }

    public void setEmergency2StreetAddressLine1(String addrLine) {
        setDomainValue("emergency2StreetAddressLine1", addrLine);
    }

    public void setEmergency2StreetAddressLine2(String addrLine) {
        setDomainValue("emergency2StreetAddressLine2", addrLine);
    }

    public void setEmergency2StreetAddressLine3(String addrLine) {
        setDomainValue("emergency2StreetAddressLine3", addrLine);
    }

    public void setEmergency2City(String city) {
        setDomainValue("emergency2City", city);
    }

    public void setEmergency2State(FMState state) {
        setDomainValue("emergency2State", Integer.parseInt(state.getIEN()));
    }

    public void setEmergency2ZipCode(String zip) {
        setDomainValue("emergency2ZipCode", ((zip.length() > 5) ? zip.substring(0, 5) : zip));
        setDomainValue("emergency2Zip4", zip);
    }

    public void setEmergency2PhoneNumber(String phone) {
        setDomainValue("emergency2PhoneNumber", phone);
    }

    public void setEmergency2WorkPhoneNumber(String phone) {
        setDomainValue("emergency2WorkPhoneNumber", phone);
    }

    public void setDesigneeName(String name) {
         setDomainValue("designeeName", name);
    }

    public boolean isDesigneeSameAsNok() {
        return (designeeSameAsNok.compareTo("Y") == 0);
    }

    public void setDesigneeSameAsNok(boolean same) {
        if (same) {
             setDomainValue("designeeSameAsNok", "Y");
        } else {
             setDomainValue("designeeSameAsNok", "N");
        }
    }

    public void setDesigneeChangeDateTime(Date date) {
         setDomainValue("designeeChangeDateTime", date);
    }

    public void setDesigneeRelationshipToPatient(String relation) {
         setDomainValue("designeeRelationshipToPatient", relation);
    }

    public void setDesigneeStreetAddressLine1(String addrLine) {
         setDomainValue("designeeStreetAddressLine1", addrLine);
    }

    public void setDesigneeStreetAddressLine2(String addrLine) {
         setDomainValue("designeeStreetAddressLine2", addrLine);
    }

    public void setDesigneeStreetAddressLine3(String addrLine) {
         setDomainValue("designeeStreetAddressLine3", addrLine);
    }

    public void setDesigneeCity(String city) {
         setDomainValue("designeeCity", city);
    }

    public void setDesigneeState(FMState state) {
        setDomainValue("designeeState", Integer.parseInt(state.getIEN()));
    }

    public void setDesigneeZipCode(String zip) {
         setDomainValue("designeeZipCode", ((zip.length() > 5) ? zip.substring(0, 5) : zip));
         setDomainValue("designeeZip4", zip);

    }

    public void setDesigneePhoneNumber(String phone) {
         setDomainValue("designeePhoneNumber", phone);
    }

    public void setDesigneeWorkPhoneNumber(String phone) {
         setDomainValue("designeeWorkPhoneNumber", phone);
    }

    public void setName(FMNameComponents nameComp) {
        setDomainValue("name", nameComp.getName());
    }

    public void setContact(ContactInfo contact, StateRepository stateRepo) {

        switch (contact.getType()) {
            case GUARDIAN:
                setGuardian(contact, stateRepo);
                break;
            case EMERGENCY:
                setEmergency(contact, stateRepo);
                break;
            case NEXT_OF_KIN:
                setNextOfKin(contact, stateRepo);
                break;
            case DESIGNEE:
                setDesignee(contact, stateRepo);
                break;
            case EMPLOYER:
                setEmployer(contact, stateRepo);
                break;
            case SPOUSE_EMPLOYER:
                setSpouseEmployer(contact, stateRepo);

        }
    }

    public void setGuardian(ContactInfo contact, StateRepository stateRepo) {
        if (contact.isVAGuardian()) {
            setVaGuardian(contact.getName());
            setVaGuardianStreetAddress1(contact.getStreet1());
            setVaGuardianStreetAddress2(contact.getStreet1());
            setVaGuardianCity(contact.getCity());
            setVaGuardianPhone(contact.getPhoneNumber());
            setVaGuardianRelationship(contact.getRelationshipToPatient());
            setVaGuardianZip(contact.getZip());
            setVaGuardianState(stateRepo.getState(contact.getState()));



        } else {
            setGuardianCivil(contact.getName());
            setCivilGuardianStreetAddress1(contact.getStreet1());
            setCivilGuardianStreetAddress2(contact.getStreet1());
            setCivilGuardianCity(contact.getCity());
            setCivilGuardianPhone(contact.getPhoneNumber());
            setCivilGuardianRelationship(contact.getRelationshipToPatient());
            setCivilGuardianZip(contact.getZip());
            setCivilGuardianState(stateRepo.getState(contact.getState()));
        }
    }

    public void setEmergency(ContactInfo contact, StateRepository stateRepo) {
        if (contact.isPrimary()) {
            setEmergencyName(contact.getName());
            setEmergencyStreetAddressLine1(contact.getStreet1());
            setEmergencyStreetAddressLine2(contact.getStreet1());
            setEmergencyCity(contact.getCity());
            setEmergencyPhoneNumber(contact.getPhoneNumber());
            setEmergencyRelationshipToPatient(contact.getRelationshipToPatient());
            setEmergencyZipCode(contact.getZip());
            setEmergencyState(stateRepo.getState(contact.getState()));
            setEmergencyWorkPhoneNumber(contact.getAltPhoneNumber());


        } else {
            setEmergency2NameOfSecondaryContact(contact.getName());
            setEmergency2StreetAddressLine1(contact.getStreet1());
            setEmergency2StreetAddressLine2(contact.getStreet1());
            setEmergency2City(contact.getCity());
            setEmergency2PhoneNumber(contact.getPhoneNumber());
            setEmergency2RelationshipToPatient(contact.getRelationshipToPatient());
            setEmergency2ZipCode(contact.getZip());
            setEmergency2State(stateRepo.getState(contact.getState()));
            setEmergency2WorkPhoneNumber(contact.getAltPhoneNumber());
        }
    }

    public void setNextOfKin(ContactInfo contact, StateRepository stateRepo) {
        if (contact.isPrimary()) {
            setNokNameOfPrimary(contact.getName());
            setNokStreetAddressLine1(contact.getStreet1());
            setNokStreetAddressLine2(contact.getStreet1());
            setNokCity(contact.getCity());
            setNokPhoneNumber(contact.getPhoneNumber());
            setNokRelationshipToPatient(contact.getRelationshipToPatient());
            setNokZipCode(contact.getZip());
            setNokState(stateRepo.getState(contact.getState()));
            setNokWorkPhoneNumber(contact.getAltPhoneNumber());


        } else {
            setNok2NameOfSecondary(contact.getName());
            setNok2StreetAddressLine1(contact.getStreet1());
            setNok2StreetAddressLine2(contact.getStreet1());
            setNok2City(contact.getCity());
            setNok2PhoneNumber(contact.getPhoneNumber());
            setNok2RelationshipToPatient(contact.getRelationshipToPatient());
            setNok2ZipCode(contact.getZip());
            setNok2State(stateRepo.getState(contact.getState()));
            setNok2WorkPhoneNumber(contact.getAltPhoneNumber());

        }
    }

    public void setDesignee(ContactInfo contact, StateRepository stateRepo) {

        setDesigneeName(contact.getName());
        setDesigneeStreetAddressLine1(contact.getStreet1());
        setDesigneeStreetAddressLine2(contact.getStreet1());
        setDesigneeCity(contact.getCity());
        setDesigneePhoneNumber(contact.getPhoneNumber());
        setDesigneeRelationshipToPatient(contact.getRelationshipToPatient());
        setDesigneeZipCode(contact.getZip());
        setDesigneeState(stateRepo.getState(contact.getState()));
        setDesigneeWorkPhoneNumber(contact.getAltPhoneNumber());


    }

    public void setEmployer(ContactInfo contact, StateRepository stateRepo) {

        setEmployerName(contact.getName());
        setEmployerStreetLine1(contact.getStreet1());
        setEmployerStreetLine2(contact.getStreet1());
        setEmployerCity(contact.getCity());
        setEmployerPhoneNumber(contact.getPhoneNumber());
        setEmployerZipCode(contact.getZip());
        setEmployerState(stateRepo.getState(contact.getState()));

    }

    public void setSpouseEmployer(ContactInfo contact, StateRepository stateRepo) {

        setSpouseEmployerName(contact.getName());
        setSpouseEmployerStreetLine1(contact.getStreet1());
        setSpouseEmployerStreetLine2(contact.getStreet1());
        setSpouseEmployerCity(contact.getCity());
        setSpouseEmployerPhoneNumber(contact.getPhoneNumber());
        setSpouseEmployerZipCode(contact.getZip());
        setSpouseEmployerState(stateRepo.getState(contact.getState()));

    }
    public Date getIncompCivilDate()
    {
        return incompCivilDate;
    }
    public Date getIncompVADate()
    {
        return incompVADate;
    }
    public void setIncompCivilDate(Date date)
    {
        setDomainValue("incompCivilDate", date);
    }
    public void setIncompVADate(Date date)
    {
        setDomainValue("incompVADate", date);
    }
    public void setRatedIncompetent(boolean incomp)
    {
        if (incomp)
            setDomainValue("ratedIncompetent", "1");
        else
            setDomainValue("ratedIncompetent", "0");
    }

    @Override
    public String toString() {
        String str = super.toString();
        str = str + "\n";
        str = str + "Rated Incompetent=[" + ratedIncompetent() + "]\n";
        str = str + "Civil Guardian Name=[" + getGuardianCivil()  +
                "] Civil Guardian Address=[" + getCivilGuardianStreetAddress1() +
                "] Civil Guardian City=[" + getCivilGuardianCity() +
                "] Civil Guardian State=[" + getCivilGuardianStateValue() +
                "] Civil Guardian Zip=[" + getCivilGuardianZip() +
                "] Civil Guardian Relationship=[" + getCivilGuardianRelationship()+
                "] Date ruled incompetent civil=[" + getIncompCivilDate() + "]\n";
        str = str + "VA Guardian Name=[" + getVaGuardian() +
                "] VA Guardian Address=[" + getVaGuardianStreetAddress1() +
                "] VA Guardian City=[" + getVaGuardianCity() +
                "] VA Guardian State=[" + getVaGuardianStateValue() +
                "] VA Guardian Zip=[" + getVaGuardianZip() +
                "] VA Guardian Relationship: " + getVaGuardianRelationship()+ 
                "] Date ruled incompetent VA=["+ getIncompVADate() + "]\n";

        str = str + "Employer Info=[" + getEmployerName()
                + "] EmployerStreetLine1=[" + getEmployerStreetLine1()
                + "] Employer City=[" + getEmployerCity()
                + "] Employer State=[" + getEmployerStateValue()
                + "] Employer Zip Code=[" + getEmployerZipCode()
                + "] Employer Phone Number=[" + getEmployerPhoneNumber() + "]";
        return str;
    }
}
