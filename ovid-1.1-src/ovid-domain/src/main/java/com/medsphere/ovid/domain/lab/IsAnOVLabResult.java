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

import java.util.Collection;

import com.medsphere.fmdomain.FMLaboratoryTest;
import com.medsphere.ovid.model.domain.patient.ResultDetail;


public interface IsAnOVLabResult {

	public String getLabTestIEN();
	public Collection<ResultDetail> getDetails();
	public void setLaboratoryTest(FMLaboratoryTest lab);
}
