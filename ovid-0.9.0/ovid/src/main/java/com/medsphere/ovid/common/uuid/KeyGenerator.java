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
package com.medsphere.ovid.common.uuid;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Hex;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


public class KeyGenerator {

    private SecureRandom seeder;
    private String middle;

    public KeyGenerator() {
        try {
            InetAddress inet = InetAddress.getLocalHost();
            byte[] bytes = inet.getAddress();
            String inetAddressInHex = new String(Hex.encodeHex(bytes));
            String hashCodeForThis = new String(Hex.encodeHex(intToByteArray(System.identityHashCode(this))));
            middle = inetAddressInHex + hashCodeForThis;
            seeder = new SecureRandom();
            seeder.nextInt();

        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            seeder = new SecureRandom();
            seeder.nextInt();
            middle = new Integer(this.hashCode()).toString();
        }

    }

    public String getUUID() {
        long timeInMillis = System.currentTimeMillis();
        int timeLow = (int) timeInMillis & 0xFFFFFFFF;
        int node = seeder.nextInt();
        String ret = new String(Hex.encodeHex(intToByteArray(timeLow))) + middle + new String(Hex.encodeHex(intToByteArray(node)));
        return ret;
    }

    private byte[] intToByteArray (final int integer) {
        int byteNum = (40 - Integer.numberOfLeadingZeros (integer < 0 ? ~integer : integer)) / 8;
        byte[] byteArray = new byte[4];

        for (int n = 0; n < byteNum; n++)
            byteArray[3 - n] = (byte) (integer >>> (n * 8));

        return (byteArray);
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
