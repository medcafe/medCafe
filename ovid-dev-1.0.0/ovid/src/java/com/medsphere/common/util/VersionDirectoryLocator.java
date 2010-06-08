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

package com.medsphere.common.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.medsphere.common.type.IsASoftwareVersion;
import com.medsphere.common.type.SoftwareVersion;
import com.medsphere.common.type.SoftwareVersionException;

/**
 * This class helps the subclass find directory names beneath the baseDir that are formed as a version
 * directory: e.g. 01.02.03 and have files within that match a string (e.g. files that end with .sql).
 * See HCPCSVersionDirectoryLocator in the pricing module for a example implementation.
 *
 */
public abstract class VersionDirectoryLocator {

    private static Logger logger = Logger.getLogger(VersionDirectoryLocator.class);
    private String baseDir = null;

    private VersionDirectoryLocator() {
    }

    public VersionDirectoryLocator(String baseDir) {
        this.baseDir = baseDir;
    }

    public abstract IsASoftwareVersion getCurrentVersion() throws SoftwareVersionException;

    public Vector<IsASoftwareVersion> getListOfVersionsToBeApplied() throws SoftwareVersionException {

        IsASoftwareVersion currentVersion = getCurrentVersion();
        logger.info("current version is " + currentVersion);
        Vector<IsASoftwareVersion> versions = new Vector<IsASoftwareVersion>();

        File dir = new File(baseDir);
        if (dir.isDirectory()) {
            File[] listOfFiles  = dir.listFiles();
            Arrays.sort(listOfFiles);
            for (File d : listOfFiles) {
                if (d.isDirectory()) {
                    logger.info("looking at file: " + d.getName());
                    if (isVersionDirectory(d.getName())) {
                        logger.info(d.getName() + " is a valid version directory.");
                        IsASoftwareVersion v = newInstance(d.getName());
                        if (v.isGreaterThan(currentVersion)) {
                            logger.info(v + " is greater than current version of " + currentVersion);
                            versions.add(v);
                        }
                    }
                }
            }
        }
        List<IsASoftwareVersion> l = versions.subList(0, versions.size());
        Collections.sort(l, new DirectoryVersionComparator());
        Vector<IsASoftwareVersion> sortedVersions = new Vector<IsASoftwareVersion>(versions.size());
        for (IsASoftwareVersion s : l) {
            sortedVersions.add(s);
        }
        return sortedVersions;
    }

    public class DirectoryVersionComparator implements Comparator<IsASoftwareVersion> {

        public int compare(IsASoftwareVersion arg0, IsASoftwareVersion arg1) {
            if (arg0.isGreaterThan(arg1)) {
                return 1;
            } else if (arg1.isGreaterThan(arg0)) {
                return -1;
            } else {
                return 0;
            }

        }
    }

    public List<String> getFilePathsForVersion(IsASoftwareVersion version, String fileMatchPattern) {
        String path = null;
        File baseDirectory = new File(baseDir);
        List<String> paths = new ArrayList<String>();

        if (baseDirectory != null && baseDirectory.isDirectory()) {
            for (File d : baseDirectory.listFiles()) {
                if (d.isDirectory()) {
                    try {
                        if (new SoftwareVersion(d.getName()).equals(version)) {
                            File[] listOfFiles = d.listFiles();
                            Arrays.sort(listOfFiles);
                            for (File f : listOfFiles) {
                                if (f.isFile()) {
                                    if (f.getName().matches(fileMatchPattern)) {
                                        path = f.getPath();
                                        paths.add(path);
                                    }
                                }
                            }

                        }
                    } catch (SoftwareVersionException e) {
                    }
                }
            }
        }

        return paths;

    }

    public String getDirectoryPathForVersion(IsASoftwareVersion version) {
        File baseDirectory = new File(baseDir);
        String path = null;

        if (baseDirectory != null && baseDirectory.isDirectory()) {
            for (File d : baseDirectory.listFiles()) {
                if (d.isDirectory()) {
                    try {
                        if (new SoftwareVersion(d.getName()).equals(version)) {
                            path = d.getPath();
                        }
                    } catch (SoftwareVersionException e) {
                    }
                }
            }
        }

        return path;

    }

    protected abstract IsASoftwareVersion newInstance(String v) throws SoftwareVersionException;

    protected abstract boolean isVersionDirectory(String name);// {
}
