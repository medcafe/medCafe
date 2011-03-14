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
package com.medsphere.ovid.domain.lab;

import java.text.ParseException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.medsphere.fileman.FMUtil;
import com.medsphere.fmdomain.FMLaboratoryTest;
import com.medsphere.fmdomain.rpms.FMVLab;
import com.medsphere.ovid.model.domain.patient.PatientResult;

public class OVLabResult extends PatientResult implements IsAnOVLabResult {

    private Logger logger = LoggerFactory.getLogger(OVLabResult.class);
    private String orderId;
    private String labType;
    private String orderableItemId;
    private String labReferenceId;
    private String accountNumber;
    private String visitIEN;
    private String visitLocation;
    private String visitCategory;
    private String visitDate;
    private Date specimenDate;
    private Date completedDate;
    private String verifyingPerson;
    private String specimen;
    private String loincCode;
    private String shortAccessionNumber;
    private String longAccessionNumber;
    private String orderingPhysician;
    private Date receivedDate;
    private String requestingLocation;
    private String accessionInstitution;
    private String labTestIEN;
    private String labTestName;
    private FMLaboratoryTest laboratoryTest;

    public OVLabResult(String id, String message, String status, Date dateTime) {
        super(id, message, status, dateTime);
        if (id != null) {
            String[] ordParts = id.split(":", -1);
            if (ordParts.length > 1) {
                orderId = ordParts[1];
            }
        }
    }

    public String getOrderId() {
        return orderId;
    }

    public String getLabType() {
        return labType;
    }

    public String getOrderableItemId() {
        return orderableItemId;
    }

    public String getLabReferenceId() {
        return labReferenceId;
    }

    public Date getSpecimenDate() {
        return specimenDate;
    }

    public Date getCompletedDate() {
        return completedDate;
    }

    public String getVerifyingPerson() {
        return verifyingPerson;
    }

    public String getSpecimen() {
        return specimen;
    }

    public String getAccessionNumber() {
        return (longAccessionNumber != null) ? longAccessionNumber : shortAccessionNumber;
    }

    public String getOrderingPhysician() {
        return orderingPhysician;
    }

    public Date getReceivedDate() {
        return receivedDate;
    }

    public String getRequestingLocation() {
        return requestingLocation;
    }

    public String getAccessionInstitution() {
        return accessionInstitution;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getVisitIEN() {
        return visitIEN;
    }

    public String getVisitDate() {
        return visitDate;
    }

    public String getVisitLocation() {
        return visitLocation;
    }

    public String getVisitCategory() {
        return visitCategory;
    }

    public String getLabTestIEN() {
        return labTestIEN;
    }

    public String getLabTestName() {
        return labTestName;
    }

    public String getLoincCode() {
        return loincCode;
    }

    public FMLaboratoryTest getLaboratoryTest() {
        return laboratoryTest;
    }

    public void setLaboratoryTest(FMLaboratoryTest laboratoryTest) {
        this.laboratoryTest = laboratoryTest;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime
                * result
                + ((accessionInstitution == null) ? 0 : accessionInstitution.hashCode());
        result = prime * result
                + ((accountNumber == null) ? 0 : accountNumber.hashCode());
        result = prime * result
                + ((completedDate == null) ? 0 : completedDate.hashCode());
        result = prime * result
                + ((labReferenceId == null) ? 0 : labReferenceId.hashCode());
        result = prime * result
                + ((labTestIEN == null) ? 0 : labTestIEN.hashCode());
        result = prime * result
                + ((labTestName == null) ? 0 : labTestName.hashCode());
        result = prime * result + ((labType == null) ? 0 : labType.hashCode());
        result = prime * result
                + ((laboratoryTest == null) ? 0 : laboratoryTest.hashCode());
        result = prime * result
                + ((loincCode == null) ? 0 : loincCode.hashCode());
        result = prime
                * result
                + ((longAccessionNumber == null) ? 0 : longAccessionNumber.hashCode());
        result = prime * result + ((orderId == null) ? 0 : orderId.hashCode());
        result = prime * result
                + ((orderableItemId == null) ? 0 : orderableItemId.hashCode());
        result = prime
                * result
                + ((orderingPhysician == null) ? 0 : orderingPhysician.hashCode());
        result = prime * result
                + ((receivedDate == null) ? 0 : receivedDate.hashCode());
        result = prime
                * result
                + ((requestingLocation == null) ? 0 : requestingLocation.hashCode());
        result = prime
                * result
                + ((shortAccessionNumber == null) ? 0 : shortAccessionNumber.hashCode());
        result = prime * result
                + ((specimen == null) ? 0 : specimen.hashCode());
        result = prime * result
                + ((specimenDate == null) ? 0 : specimenDate.hashCode());
        result = prime * result
                + ((verifyingPerson == null) ? 0 : verifyingPerson.hashCode());
        result = prime * result
                + ((visitCategory == null) ? 0 : visitCategory.hashCode());
        result = prime * result
                + ((visitDate == null) ? 0 : visitDate.hashCode());
        result = prime * result
                + ((visitIEN == null) ? 0 : visitIEN.hashCode());
        result = prime * result
                + ((visitLocation == null) ? 0 : visitLocation.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        OVLabResult other = (OVLabResult) obj;
        if (accessionInstitution == null) {
            if (other.accessionInstitution != null) {
                return false;
            }
        } else if (!accessionInstitution.equals(other.accessionInstitution)) {
            return false;
        }
        if (accountNumber == null) {
            if (other.accountNumber != null) {
                return false;
            }
        } else if (!accountNumber.equals(other.accountNumber)) {
            return false;
        }
        if (completedDate == null) {
            if (other.completedDate != null) {
                return false;
            }
        } else if (!completedDate.equals(other.completedDate)) {
            return false;
        }
        if (labReferenceId == null) {
            if (other.labReferenceId != null) {
                return false;
            }
        } else if (!labReferenceId.equals(other.labReferenceId)) {
            return false;
        }
        if (labTestIEN == null) {
            if (other.labTestIEN != null) {
                return false;
            }
        } else if (!labTestIEN.equals(other.labTestIEN)) {
            return false;
        }
        if (labTestName == null) {
            if (other.labTestName != null) {
                return false;
            }
        } else if (!labTestName.equals(other.labTestName)) {
            return false;
        }
        if (labType == null) {
            if (other.labType != null) {
                return false;
            }
        } else if (!labType.equals(other.labType)) {
            return false;
        }
        if (laboratoryTest == null) {
            if (other.laboratoryTest != null) {
                return false;
            }
        } else if (!laboratoryTest.equals(other.laboratoryTest)) {
            return false;
        }
        if (loincCode == null) {
            if (other.loincCode != null) {
                return false;
            }
        } else if (!loincCode.equals(other.loincCode)) {
            return false;
        }
        if (longAccessionNumber == null) {
            if (other.longAccessionNumber != null) {
                return false;
            }
        } else if (!longAccessionNumber.equals(other.longAccessionNumber)) {
            return false;
        }
        if (orderId == null) {
            if (other.orderId != null) {
                return false;
            }
        } else if (!orderId.equals(other.orderId)) {
            return false;
        }
        if (orderableItemId == null) {
            if (other.orderableItemId != null) {
                return false;
            }
        } else if (!orderableItemId.equals(other.orderableItemId)) {
            return false;
        }
        if (orderingPhysician == null) {
            if (other.orderingPhysician != null) {
                return false;
            }
        } else if (!orderingPhysician.equals(other.orderingPhysician)) {
            return false;
        }
        if (receivedDate == null) {
            if (other.receivedDate != null) {
                return false;
            }
        } else if (!receivedDate.equals(other.receivedDate)) {
            return false;
        }
        if (requestingLocation == null) {
            if (other.requestingLocation != null) {
                return false;
            }
        } else if (!requestingLocation.equals(other.requestingLocation)) {
            return false;
        }
        if (shortAccessionNumber == null) {
            if (other.shortAccessionNumber != null) {
                return false;
            }
        } else if (!shortAccessionNumber.equals(other.shortAccessionNumber)) {
            return false;
        }
        if (specimen == null) {
            if (other.specimen != null) {
                return false;
            }
        } else if (!specimen.equals(other.specimen)) {
            return false;
        }
        if (specimenDate == null) {
            if (other.specimenDate != null) {
                return false;
            }
        } else if (!specimenDate.equals(other.specimenDate)) {
            return false;
        }
        if (verifyingPerson == null) {
            if (other.verifyingPerson != null) {
                return false;
            }
        } else if (!verifyingPerson.equals(other.verifyingPerson)) {
            return false;
        }
        if (visitCategory == null) {
            if (other.visitCategory != null) {
                return false;
            }
        } else if (!visitCategory.equals(other.visitCategory)) {
            return false;
        }
        if (visitDate == null) {
            if (other.visitDate != null) {
                return false;
            }
        } else if (!visitDate.equals(other.visitDate)) {
            return false;
        }
        if (visitIEN == null) {
            if (other.visitIEN != null) {
                return false;
            }
        } else if (!visitIEN.equals(other.visitIEN)) {
            return false;
        }
        if (visitLocation == null) {
            if (other.visitLocation != null) {
                return false;
            }
        } else if (!visitLocation.equals(other.visitLocation)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "OVLabResult={"
                + ((labTestIEN != null) ? " [labTestIEN=" + labTestIEN + "]" : "")
                + ((labTestName != null) ? " [labTestName=" + labTestName + "]" : "")
                + ((orderId != null) ? " [orderId=" + orderId + "]" : "")
                + ((labType != null) ? " [labType=" + labType + "]" : "")
                + ((orderableItemId != null) ? " [orderableItemId=" + orderableItemId + "]" : "")
                + ((labReferenceId != null) ? " [labReferenceId=" + labReferenceId + "]" : "")
                + ((specimenDate != null) ? " [specimenDate=" + specimenDate + "]" : "")
                + ((completedDate != null) ? " [completedDate=" + completedDate + "]" : "")
                + ((verifyingPerson != null) ? " [verifyingPerson=" + verifyingPerson + "]" : "")
                + ((loincCode != null) ? " [loincCode=" + loincCode + "]" : "")
                + ((specimen != null) ? " [specimen=" + specimen + "]" : "")
                + ((longAccessionNumber != null) ? " [longAccessionNumber=" + longAccessionNumber + "]" : "")
                + ((shortAccessionNumber != null) ? " [shortAccessionNumber=" + shortAccessionNumber + "]" : "")
                + ((orderingPhysician != null) ? " [orderingPhysician=" + orderingPhysician + "]" : "")
                + ((receivedDate != null) ? " [receivedDate=" + receivedDate + "]" : "")
                + ((requestingLocation != null) ? " [requestingLocation=" + requestingLocation + "]" : "")
                + ((accessionInstitution != null) ? " [accessionInstitution=" + accessionInstitution + "]" : "")
                + ((accountNumber != null) ? " [accountNumber=" + accountNumber + "]" : "")
                + ((visitIEN != null) ? " [visitIEN=" + visitIEN + "]" : "")
                + ((visitLocation != null) ? " [visitLocation=" + visitLocation + "]" : "")
                + ((visitDate != null) ? " [visitDate=" + visitDate + "]" : "")
                + ((visitCategory != null) ? " [visitCategory=" + visitCategory + "]" : "")
                + ((laboratoryTest != null) ? " [laboratoryTest=" + laboratoryTest + "]" : "")
                + " } "
                + super.toString();
    }

    public void addPanelInfo(String[] parts) {
        if (parts == null) {
            return;
        }
        if (parts.length > 3) {
            String[] labTestInfo = parts[3].split(";", -1);
            if (labTestInfo.length > 0) {
                labTestIEN = labTestInfo[0];
            }
            if (labTestInfo.length > 1) {
                labTestName = labTestInfo[1];
            }
            if (labTestInfo.length > 2) {
                loincCode = labTestInfo[2];
            }
        }
        if (parts.length > 4) {
            setStatus(parts[4]);
        }
        if (parts.length > 5) {
            labType = parts[5];
        }
        if (parts.length > 6) {
            orderableItemId = parts[6];
        }
        if (parts.length > 7) {
            labReferenceId = parts[7];
        }

        if (parts.length > 8) {
            accountNumber = parts[8];
        }
        if (parts.length > 9) {
            visitIEN = parts[9];
        }
        if (parts.length > 10) {
            String[] visitInfo = parts[10].split(";", -1);
            if (visitInfo.length > 0) {
                visitLocation = visitInfo[0];
            }
            if (visitInfo.length > 1) {
                visitDate = visitInfo[1];
            }
            if (visitInfo.length > 2) {
                visitCategory = visitInfo[2];
            }
        }

        if (parts.length > 11) {
            longAccessionNumber = parts[11];
        }
        if (parts.length > 12) {
            try {
                specimenDate = FMUtil.fmDateToDate(parts[12]);
            } catch (ParseException e) {
                logger.info("Parse error", e);
            }
        }
        if (parts.length > 13) {
            try {
                completedDate = FMUtil.fmDateToDate(parts[13]);
            } catch (ParseException e) {
                logger.info("Parse error", e);
            }
        }
        if (parts.length > 14) {
            verifyingPerson = parts[14];
        }
        if (parts.length > 15) {
            specimen = parts[15];
        }
        if (parts.length > 16) {
            shortAccessionNumber = parts[16];
        }
        if (parts.length > 17) {
            orderingPhysician = parts[17];
        }
        if (parts.length > 18) {
            try {
                receivedDate = FMUtil.fmDateToDate(parts[18]);
            } catch (ParseException e) {
                logger.info("Parse error", e);
            }
        }
        if (parts.length > 19) {
            requestingLocation = parts[19];
        }
        if (parts.length > 20) {
            accessionInstitution = parts[20];
        }

    }

    public void addPanelInfo(FMVLab panel) {
    }
}
