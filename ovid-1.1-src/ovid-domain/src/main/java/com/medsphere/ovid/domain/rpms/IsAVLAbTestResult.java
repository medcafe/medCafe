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
package com.medsphere.ovid.domain.rpms;

import com.medsphere.fmdomain.FMLaboratoryTest;
import java.util.Date;

/**
 *
 */
public interface IsAVLAbTestResult {

    public String getLabTest();
    public FMLaboratoryTest getLaboratoryTest();
    public String getAccessionNumber();
    public void setLaboratoryTest(FMLaboratoryTest laboratoryTest);
    public String getResults();
    public String getParentRecord();
    public String getIEN();
    public String getAbnormal();
    public String getReferenceLow();
    public String getReferenceHigh();
    public String getCurrentStatusFlag();
    public Double getOrder();
    public String getVisit();
    public String getClinic();
    public String getLoincCode();
    public Date getResultDateAndTime();
    public Date getCollectionDateAndTime();
    public String getUnits();

}
