// <editor-fold defaultstate="collapsed">
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
// </editor-fold>


package com.medsphere.ovid.model.domain.patient;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 */
@XmlType(name="PatientAllergy")
@XmlRootElement(name="PatientAllergy")
public class PatientAllergy extends PatientItem {
    private String severity;
    private String signs;

    private String observationType;
    private String allergyType;
    private Date originationDate;
    private String originator;
    private Date verificationDate;
    private String verifier;
    private String mechanism;
    private Boolean verified;

    protected PatientAllergy() {}
    public PatientAllergy(String id, String message) {
        super(id, message, null, null, PatientItemType.Allergy);
    }

    public PatientAllergy(String id, String message, String severity, String signs) {
        this(id, message);
        this.severity = severity;
        this.signs = signs;
    }
    public String getSeverity() {
        return severity;
    }
    public String getSigns() {
        return signs;
    }

    // optional attributes
    public String getObservationType() {
        return observationType;
    }
    public void setObservationType(String observationType) {
        this.observationType = observationType;
    }
    public String getAllergyType() {
        return allergyType;
    }
    public void setAllergyType(String allergyType) {
        this.allergyType = allergyType;
    }
    public Date getOriginationDate() {
        return originationDate;
    }
    public void setOriginationDate(Date originationDate) {
        this.originationDate = originationDate;
    }
    public String getOriginator() {
        return originator;
    }
    public void setOriginator(String originator) {
        this.originator = originator;
    }
    public Date getVerificationDate() {
        return verificationDate;
    }
    public void setVerificationDate(Date verificationDate) {
        this.verificationDate = verificationDate;
    }
    public String getVerifier() {
        return verifier;
    }
    public void setVerifier(String verifier) {
        this.verifier = verifier;
    }
    public String getMechanism() {
        return mechanism;
    }
    public void setMechanism(String mechanism) {
        this.mechanism = mechanism;
    }
    public Boolean isVerified() {
        return verified;
    }
    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    @Override
    public String toString() {
        return super.toString() + " severity=["+severity+"] signs=[" + signs + "]"
        + ((observationType != null) ? " observationType=[" + observationType + "]" : "")
        + ((allergyType != null) ? " allergyType=[" + allergyType + "]" : "")
        + ((originationDate != null) ? " originationDate=[" + originationDate + "]" : "")
        + ((originator != null) ? " originator=[" + originator + "]" : "")
        + ((verificationDate != null) ? " verificationDate=[" + verificationDate + "]" : "")
        + ((verifier != null) ? " verifier=[" + verifier + "]" : "")
        + ((mechanism != null) ? " mechanism=[" + mechanism + "]" : "")
        + ((verified != null) ? " verified=[" + verified + "]" : "")
       ;
    }
}
