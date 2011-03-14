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

package com.medsphere.ovid.common.uuid;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class KeyGenerator {

    public KeyGenerator() {
    }

    public String getUUID() {
        return UUID.randomUUID().toString();
    }

    public class TestKeyGenerator {

        private final int nTests = 7500;
        private List<String> slots = new ArrayList<String>(nTests);

        @BeforeMethod public void clearSlots() {
            slots.clear();
        }
        @Test public void getManyUUIDUsingAutomaticVariable() {
            KeyGenerator kg = new KeyGenerator();
            for (int i = 0; i < nTests; i++) {
                Assert.assertFalse(isInMap(kg.getUUID()));
            }
            Assert.assertEquals(slots.size(), nTests);
        }

        @Test public void getManyUUIDUsingHeapVariable() {

            for (int i = 0; i < nTests; i++) {
                Assert.assertFalse(isInMap(new KeyGenerator().getUUID()));
            }
            Assert.assertEquals(slots.size(), nTests);
        }

        private boolean isInMap(String uuid) {
            boolean result = false;
            if (slots.contains(uuid)) {
                result = true;
            } else {
                slots.add(uuid);
            }
            return result;
        }
    }
}
