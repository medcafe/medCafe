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
package com.medsphere.ovid.tools.install;

import com.medsphere.common.type.IsASoftwareVersion;
import com.medsphere.common.type.SoftwareVersion;
import com.medsphere.common.type.SoftwareVersionException;
import java.util.Date;

public class OvidDatabaseVersion extends SoftwareVersion implements IsASoftwareVersion {

    private String versionString = null;
    private Date appliedDate = null;

    public OvidDatabaseVersion(String versionString, Integer major, Integer minor, Integer revision, Integer build, Date appliedDate) throws SoftwareVersionException {
        super(major, minor, revision, build);
        this.versionString = versionString;
        this.appliedDate = appliedDate;
    }

    public OvidDatabaseVersion(String directoryString) throws SoftwareVersionException {
        super(directoryString);

        versionString = directoryString;
        appliedDate = new Date(System.currentTimeMillis());

    }

    @Override
    public String toString() {
        String vstring = versionString + " [" + getMajor() + "." + getMinor() + "." + getRevision();
        if (getBuild() != null) {
            vstring += "." + getBuild();
        }
        vstring += "] - " + appliedDate;
        return vstring;
    }
}
