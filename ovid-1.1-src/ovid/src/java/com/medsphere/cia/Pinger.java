// <editor-fold defaultstate="collapsed">
/*
 * Copyright (C) 2009  Medsphere Systems Corporation
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

package com.medsphere.cia;

class Pinger {
    private final CIABrokerConnection connection;

    Pinger(final CIABrokerConnection connection, final long waitTime) {
        this.connection = connection;
        Thread pinger = new Thread(){
            @Override public void run() {
                long sleepTime = waitTime;
                while(sleepTime>0) {
                    try {
                        Thread.sleep(sleepTime);
                        sleepTime = connection.tryPing();
                    } catch (InterruptedException ex) {
                        break;
                    }
                }
            }
        };
        pinger.start();
    }

}
