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

package com.medsphere.common.type;

import java.text.NumberFormat;
import java.text.ParseException;

import org.testng.Assert;
import org.testng.annotations.Test;

public class SoftwareVersion implements IsASoftwareVersion {
    private Integer major = 0;
    private Integer minor = 0;
    private Integer revision = null;
    private Integer build = null;
    private String tag = null;

    public SoftwareVersion(Integer major, Integer minor, Integer revision, Integer build, String tag) throws SoftwareVersionException {
        if (major == null || minor == null) {
            throw new SoftwareVersionException("Invalid version.  Major and/or Minor is null");
        }
        this.major = major;
        this.minor = minor;
        this.revision = revision;
        this.build = build;
        this.tag = tag;
    }

    public SoftwareVersion(Integer major, Integer minor, Integer revision, Integer build) throws SoftwareVersionException {
        this(major, minor, revision, build, null);
    }

    public SoftwareVersion(Integer major, Integer minor, Integer revision) throws SoftwareVersionException {
        this(major, minor, revision, null, null);
    }

    public SoftwareVersion(Integer major, Integer minor) throws SoftwareVersionException {
        this(major, minor, null, null, null);
    }

    public SoftwareVersion(String versionString) throws SoftwareVersionException {
        if (versionString == null) {
            throw new SoftwareVersionException("version string is null");
        }

        String[] parts = versionString.split("(\\.)|(\\-)", 5);
        if (parts == null || parts.length <= 1) {
            throw new SoftwareVersionException("invalid version string: " + versionString);
        }

        try {
            if (parts.length >= 1) {
                this.major = NumberFormat.getInstance().parse(parts[0]).intValue();
            }
            if (parts.length >= 2) {
                this.minor = NumberFormat.getInstance().parse(parts[1]).intValue();
            }
            if (parts.length >= 3) {
                this.revision = NumberFormat.getInstance().parse(parts[2]).intValue();
            }
            if (parts.length >= 4) {
                this.build = NumberFormat.getInstance().parse(parts[3]).intValue();
            }
            if (parts.length >= 5) {
                this.tag = parts[4];
            }

            if (this.major == null || this.minor == null) {
                throw new SoftwareVersionException("Invalid version.  Major and/or Minor is null");
            }

        } catch (ParseException e) {
            throw new SoftwareVersionException(e);
        }
    }

    public boolean isGreaterThan(IsASoftwareVersion other) {
        boolean result = false;

        if (this.equals(other)) {
            return false;
        }

        if (this.getMajor().intValue() > other.getMajor()) {
            return true;
        }
        if (this.getMajor().intValue() >= other.getMajor().intValue()) {
            if (this.getMinor().intValue() > other.getMinor().intValue()) {
                return true;
            }
        }
        if (this.getMajor().intValue() >= other.getMajor().intValue()) {
            if (this.getMinor().intValue() >= other.getMinor().intValue()) {
                if (this.getRevision() != null) {
                    if (other.getRevision() != null) {
                        if (this.getRevision().intValue() > other.getRevision().intValue()) {
                            return true;
                        }
                    } else {
                        return true;
                    }
                }
            }
        }


        if (this.getMajor().intValue() >= other.getMajor().intValue()) {
            if (this.getMinor().intValue() >= other.getMinor().intValue()) {
                if (this.getRevision() != null) {
                    if (other.getRevision() != null) {
                        if (this.getRevision().intValue() >= other.getRevision().intValue()) {
                            if (this.getBuild() != null) {
                                if (other.getBuild() != null) {
                                    if (this.getBuild().intValue() > other.getBuild().intValue()) {
                                        return true;
                                    }
                                } else {
                                    return true;
                                }
                            }
                        }
                    } else {
                        return true;
                    }
                }
            }
        }

        if (this.getMajor().intValue() >= other.getMajor().intValue()) {
            if (this.getMinor().intValue() >= other.getMinor().intValue()) {
                if (this.getRevision() != null) {
                    if (other.getRevision() != null) {
                        if (this.getRevision().intValue() >= other.getRevision().intValue()) {
                            if (this.getBuild() != null) {
                                if (other.getBuild() != null) {
                                    if (this.getBuild().intValue() >= other.getBuild().intValue()) {
                                        if (this.tag != null) {
                                            if (other.getTag() != null) {
                                                if (this.tag.compareTo(other.getTag()) > 0) {
                                                    return true;
                                                }
                                            } else {
                                                return true;
                                            }
                                        }
                                    }
                                } else {
                                    return true;
                                }
                            }
                        }
                    } else {
                        return true;
                    }
                }
            }
        }

        return result;
    }

    public Integer getMajor() {
        return major;
    }
    public Integer getMinor() {
        return minor;
    }
    public Integer getRevision() {
        return revision;
    }

    public Integer getBuild() {
        return build;
    }

    public String getTag() {
        return tag;
    }

    public String toString() {
        String result = new String("");

        if (major != null) {
            result += major;
        } else {
            return result;
        }

        if (minor != null ) {
            result += "." + minor;
        } else {
            return result;
        }

        if (revision != null) {
            result += "." + revision;
        } else {
            return result;
        }

        if (build != null) {
            result += "." + build;
        } else {
            return result;
        }

        if (tag != null && tag.length() > 0) {
            result += "-" + tag;
        }

        return result;

    }


    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = super.hashCode();
        result = PRIME * result + ((revision == null) ? 0 : revision.hashCode());
        result = PRIME * result + ((major == null) ? 0 : major.hashCode());
        result = PRIME * result + ((minor == null) ? 0 : minor.hashCode());
        result = PRIME * result + ((build == null) ? 0 : build.hashCode());
        result = PRIME * result + ((tag == null) ? 0 : tag.hashCode());
        return result;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (getClass() != obj.getClass())
            return false;
        final SoftwareVersion other = (SoftwareVersion) obj;
        if (revision == null) {
            if (other.revision != null)
                return false;
        } else if (!revision.equals(other.revision))
            return false;
        if (major == null) {
            if (other.major != null)
                return false;
        } else if (!major.equals(other.major))
            return false;
        if (minor == null) {
            if (other.minor != null)
                return false;
        } else if (!minor.equals(other.minor))
            return false;
        if (build == null) {
            if (other.build != null)
                return false;
        } else if (!build.equals(other.build))
            return false;
        if (tag == null) {
            if (other.tag != null)
                return false;
        } else if (!tag.equals(other.tag))
            return false;
        return true;
    }

    public static class TestSoftwareVersion {
        @Test public void testInstantiatingAVersion() throws SoftwareVersionException {

            Assert.assertEquals(new SoftwareVersion(1,2,3,4, "a").toString(), "1.2.3.4-a");
            Assert.assertEquals(new SoftwareVersion(1,2,3,4).toString(), "1.2.3.4");
            Assert.assertEquals(new SoftwareVersion(1,2,3).toString(), "1.2.3");
            Assert.assertEquals(new SoftwareVersion(1,2).toString(), "1.2");

        }

        @Test(expectedExceptions={SoftwareVersionException.class}) public void makeSureThatANullMajorThrowsAnException() throws SoftwareVersionException {
            new SoftwareVersion(null, 2, 3 ,4);
        }
        @Test(expectedExceptions={SoftwareVersionException.class}) public void makeSureThatANullMinorThrowsAnException() throws SoftwareVersionException {
            new SoftwareVersion(1, null, 3 ,4);
        }
        @Test(expectedExceptions={SoftwareVersionException.class}) public void makeSureThatANullVersionStringThrowsAnException() throws SoftwareVersionException {
            new SoftwareVersion(null);
        }
        @Test(expectedExceptions={SoftwareVersionException.class}) public void makeSureThatAPoorlyFormedVersionStringThrowsAnException() throws SoftwareVersionException {
            new SoftwareVersion("this shouldn't work");
        }

        @Test(expectedExceptions={SoftwareVersionException.class}) public void makeSureThatAVersionStringWithOnlyOnePartThrowsAnException() throws SoftwareVersionException {
            new SoftwareVersion("1");
        }

        @Test public void testParsingVersionStringsOnInstantiation() throws SoftwareVersionException {
            Assert.assertEquals(new SoftwareVersion("1.2").toString(), "1.2");
            Assert.assertEquals(new SoftwareVersion("1.2.3").toString(), "1.2.3");
            Assert.assertEquals(new SoftwareVersion("1.2.3.4").toString(), "1.2.3.4");
            Assert.assertEquals(new SoftwareVersion("1.2.3.4-alpha").toString(), "1.2.3.4-alpha");
        }

        @Test(expectedExceptions={SoftwareVersionException.class}) public void makeSureThatAVersionStringWithNonNumericMajorThrowsAnException() throws SoftwareVersionException {
            new SoftwareVersion("major.2.3.4-apha");
        }
        @Test(expectedExceptions={SoftwareVersionException.class}) public void makeSureThatAVersionStringWithNonNumericMinorThrowsAnException() throws SoftwareVersionException {
            new SoftwareVersion("1.minor.3.4-apha");
        }
        @Test(expectedExceptions={SoftwareVersionException.class}) public void makeSureThatAVersionStringWithNonNumericRevisionThrowsAnException() throws SoftwareVersionException {
            new SoftwareVersion("1.2.revision.4-apha");
        }
        @Test(expectedExceptions={SoftwareVersionException.class}) public void makeSureThatAVersionStringWithNonNumericBuildThrowsAnException() throws SoftwareVersionException {
            new SoftwareVersion("1.2.3.build-apha");
        }
        @Test public void makeSureThatATagWithMultipleDashesInItWorksOk() throws SoftwareVersionException {
            Assert.assertEquals(new SoftwareVersion("1.2.3.4-tag-1").toString(), "1.2.3.4-tag-1");
        }

        @Test public void testOutGreaterThan() throws SoftwareVersionException {
            Assert.assertTrue(new SoftwareVersion("1.2").isGreaterThan(new SoftwareVersion("1.1")));
            Assert.assertTrue(new SoftwareVersion("1.2.2").isGreaterThan(new SoftwareVersion("1.2.1.1000")));
            Assert.assertFalse(new SoftwareVersion("1.2").isGreaterThan(new SoftwareVersion("1.2")));
            Assert.assertFalse(new SoftwareVersion("1.2").isGreaterThan(new SoftwareVersion("1.2.1")));

            Assert.assertTrue(new SoftwareVersion("1.2.3.4").isGreaterThan(new SoftwareVersion("1.2.3.3")));
            Assert.assertTrue(new SoftwareVersion("1.2.3.4").isGreaterThan(new SoftwareVersion("1.2.3.3-alpha")));

            Assert.assertTrue(new SoftwareVersion("1.2.3.4-alpha").isGreaterThan(new SoftwareVersion("1.2.3.4")));
            Assert.assertTrue(new SoftwareVersion("1.2.3.4-alpha-2").isGreaterThan(new SoftwareVersion("1.2.3.4-alpha-1")));
            Assert.assertTrue(new SoftwareVersion("1.2.3.4-beta").isGreaterThan(new SoftwareVersion("1.2.3.4-alpha")));

            Assert.assertTrue(new SoftwareVersion("0001.0002.0003.0004").isGreaterThan(new SoftwareVersion("1.2.3")));
        }

        @Test public void testEquals() throws SoftwareVersionException {
            Assert.assertEquals(new SoftwareVersion(1,2,3,4), new SoftwareVersion("1.2.3.4"));
            Assert.assertEquals(new SoftwareVersion(1,2,3,4, "tag"), new SoftwareVersion("1.2.3.4-tag"));
            Assert.assertEquals(new SoftwareVersion(1,2), new SoftwareVersion("1.2"));
            Assert.assertEquals(new SoftwareVersion(1,2,3), new SoftwareVersion("1.2.3"));
        }

    }


}
