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
package org.medsphere.ovid.test.unit.junit;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 *
 * @author apardue
 */

public class OvidJUnitTestSuite  {

    public static Test suite() {
        TestSuite suite = new TestSuite();

        suite.addTestSuite(ConnectionResourceTest.class);
        return suite;

    }

}