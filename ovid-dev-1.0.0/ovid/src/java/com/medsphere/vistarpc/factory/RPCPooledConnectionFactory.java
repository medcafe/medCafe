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

package com.medsphere.vistarpc.factory;

import com.medsphere.vistarpc.RPCException;
import java.util.LinkedList;
import org.apache.log4j.Logger;

public abstract class RPCPooledConnectionFactory {
    public abstract RPCPooledConnection getConnection();
    private Logger logger = Logger.getLogger(RPCBrokerPooledConnectionFactory.class);
    protected final LinkedList<RPCPooledConnection> available = new LinkedList<RPCPooledConnection>();
    protected LinkedList<RPCPooledConnection> inUse = new LinkedList<RPCPooledConnection>();


    public void acquireConnection(RPCPooledConnection connection) {
        logger.debug("acquire connection");
        synchronized (available) {
            if (inUse.indexOf(connection)!=-1) {
                inUse.remove(connection);
                available.offer(connection);
            }
        }
    }

    public void emptyPool() {
        logger.debug("emptyPool");
        synchronized (available) {
            for (RPCPooledConnection conn : new LinkedList<RPCPooledConnection>( available )) {
                available.remove( conn );
                try {
                    conn.close();
                } catch (RPCException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            available.clear();
        }
    }

    public void removeFromPool(RPCPooledConnection conn) {
        logger.debug("dispose");
        synchronized (available) {
            if (inUse.indexOf(conn)!=-1) {
                inUse.remove(conn);
            }
            if (available.indexOf(conn)!=-1) {
                available.remove(conn);
            }
        }
    }

}
