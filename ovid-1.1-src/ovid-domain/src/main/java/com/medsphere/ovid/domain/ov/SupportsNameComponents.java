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
package com.medsphere.ovid.domain.ov;

public interface SupportsNameComponents {
    public String getName();
    public String getFamilyName();
    public String getGivenName();
    public String getMiddleName();
    public String getFileNumber();
    public String getFieldNumber();
    public void setFamilyName(String familyName);
    public void setGivenName(String givenName);
    public void setMiddleName(String middleName);
    public void setPrefix(String prefix);
    public void setSuffix(String suffix);
    public void setDegree(String degree);
    public String getIEN();

}
