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
package org.medsphere.connection;

import com.medsphere.common.util.TimeKeeper;
import com.medsphere.common.util.TimeKeeperException;
import com.medsphere.vistarpc.RPCConnection;
import com.medsphere.vistarpc.RPCException;
import com.medsphere.vistarpc.RPCResponse;
import com.medsphere.vistarpc.VistaRPC;
import org.medsphere.auth.VistaSubject;
import org.medsphere.datasource.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Proxies any connection type, and logs to debug the execution time of RPCs.
 */
public class ProfiledConnection extends ProxyConnection {

    public final static String DELEGATE_TYPE = "profiledDelegateBrokerType";
    private final BrokerTimer timer;

    /**
     * Creates a profiled connection with the given delegate.
     * @param delegate The delegate to use. Must not be null.
     * @throws VistaConnectionException Thrown if delegate is null.
     */
    public ProfiledConnection(RPCConnection delegate) throws VistaConnectionException {
        super(delegate);
        timer = initTimer(null);
    }

    /**
     * Creates a profiled connection. The following property is required by
     * profiled connections:
     * <ul>
     * <li><code>profiledDelegateBrokerType</code> - The broker type used to
     * create delegate connection.</li>
     * </ul>
     * @param subject Subject used by some connection types. May be null.
     * @param vcp Profiled connections use the property listed above. All others
     * are ignored and passed to {@link ServiceLocator} to create a delegate.
     * @throws VistaConnectionException - On connection or authentication errors
     * in the delegate.
     */
    public ProfiledConnection(VistaSubject subject, VistaConnectionProperties vcp) throws VistaConnectionException {
        super(subject, vcp, DELEGATE_TYPE);
        timer = initTimer(vcp);
    }

    private BrokerTimer initTimer(VistaConnectionProperties vcp) throws VistaConnectionException {
        if (getDelegate() == null) {
            throw new VistaConnectionException("ProfiledConnection initialized will null delegate");
        }
        String brokerType = null;
        if (vcp != null) {
            brokerType = vcp.get(DELEGATE_TYPE);
        }
        if (brokerType == null) {
            brokerType = getDelegate().getClass().getSimpleName();
        }
        return new BrokerTimer(brokerType);
    }

    @Override
    public synchronized RPCResponse execute(VistaRPC rpc) throws RPCException {
        RPCResponse response = null;
        try {
            timer.start(rpc);
            response = super.execute(rpc);
        } finally {
            timer.stop();
        }
        return response;
    }

    private static class BrokerTimer {

        private static Logger logger = LoggerFactory.getLogger(BrokerTimer.class);
        private static TimeKeeper tk = new TimeKeeper();
        private final String brokerType;
        private String currentTag;

        public BrokerTimer(String brokerType) {
            this.brokerType = brokerType;
        }

        public void log(String msg) {
            logger.debug(msg);
        }

        public void start(VistaRPC rpc) {
            try {
                StringBuilder sb = new StringBuilder();
                sb.append("Broker type=").append(brokerType);
                sb.append(", RPC=").append(rpc.toString());
                currentTag = sb.toString();
                tk.start(currentTag);
            } catch (TimeKeeperException e) {
                logger.error("Timekeeper exception in profiler", e);
            }
        }

        public void stop() {
            try {
                tk.stop(currentTag);
                log(tk.getDisplayString(currentTag));
            } catch (TimeKeeperException e) {
                logger.error("Timekeeper exception in profiler", e);
            } finally {
                tk.clear(currentTag);
            }
        }
    }
}
