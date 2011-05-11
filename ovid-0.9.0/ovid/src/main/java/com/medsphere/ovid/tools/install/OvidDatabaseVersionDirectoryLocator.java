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

/*
 * OvidDatabaseVersionDirectoryLocator
 * This class locates ordered sets of directories that contain .sql used
 * to create and/or upgrade a database from one revision to another.
 */
package com.medsphere.ovid.tools.install;

import com.medsphere.common.type.IsASoftwareVersion;
import com.medsphere.common.type.SoftwareVersion;
import com.medsphere.common.type.SoftwareVersionException;
import com.medsphere.common.util.VersionDirectoryLocator;
import com.medsphere.ovid.domain.database.OvidDatabaseVersionRepository;

public class OvidDatabaseVersionDirectoryLocator extends VersionDirectoryLocator {
    
        private String application = "ovid";
        
    	public OvidDatabaseVersionDirectoryLocator(String baseDir, String application) {
		super(baseDir);
                this.application = application;
	}

	@Override
	public IsASoftwareVersion getCurrentVersion() throws SoftwareVersionException {
            OvidDatabaseVersionRepository repository = new OvidDatabaseVersionRepository(application);
            return repository.getLatestOvidDatabaseVersion(application);
	}

	@Override
	protected boolean isVersionDirectory(String name) {
		return name.matches("^\\d{1,2}\\.\\d{1,3}\\.\\d{1,5}\\.{0,1}(\\d){0,5}.*$");
	}

	@Override
	protected IsASoftwareVersion newInstance(String v) throws SoftwareVersionException {
		return new SoftwareVersion(v);
	}

}
