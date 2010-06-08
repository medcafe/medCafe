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
package com.medsphere.ovid.connection.concurrent.rpc;

import java.sql.SQLException;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;

import com.medsphere.vistarpc.RPCConnection;
import com.medsphere.vistarpc.RPCException;
import com.medsphere.vistarpc.RPCResponse;
import com.medsphere.vistarpc.VistaRPC;

public class ConcurrentVistaRPC {

    private class RPCInfo {

        private VistaRPC rpc;
        private RPCConnection connection;

        @SuppressWarnings("unused")
        private RPCInfo() {
        }

        public RPCInfo(VistaRPC rpc, RPCConnection connection) {
            this.rpc = rpc;
            this.connection = connection;
        }

        public VistaRPC getRpc() {
            return rpc;
        }

        public RPCConnection getConnection() {
            return connection;
        }
    }
    private static Logger logger = Logger.getLogger(ConcurrentVistaRPC.class);
    private static ConcurrentVistaRPC _instance = null;
    private static ConcurrentHashMap<CanResolveAVistaRPC, RPCInfo> queuedQueries = null;
    private static ConcurrentHashMap<IsAThreadedRPCRunner, CanResolveAVistaRPC> runningThreads = null;
    private static final int numberOfConcurrentQueries = 15;

    private ConcurrentVistaRPC() {
        queuedQueries = new ConcurrentHashMap<CanResolveAVistaRPC, RPCInfo>();
        runningThreads = new ConcurrentHashMap<IsAThreadedRPCRunner, CanResolveAVistaRPC>();

    }

    public static ConcurrentVistaRPC getInstance() {
        logger.debug("Instantiating ConcurrentVistaRPC");
        if (_instance == null) {
            _instance = new ConcurrentVistaRPC();
        }
        return _instance;
    }

    public void execute(VistaRPC rpc, RPCConnection connection, CanResolveAVistaRPC domainObject) throws RPCException {
        reapCompletedThreads();

        while (!queuedQueries.isEmpty() && runningThreads.size() < numberOfConcurrentQueries) {
            CanResolveAVistaRPC queuedDomainObject = (CanResolveAVistaRPC) queuedQueries.keySet().toArray()[0];
            RPCInfo rpcInfo = queuedQueries.get(queuedDomainObject);
            queuedQueries.remove(queuedDomainObject);
            runningThreads.put(new QueryThreadPool(rpcInfo.getRpc(), rpcInfo.getConnection()), queuedDomainObject);
        }

        // now, either start a thread for this query or add it to the queued queries.
        if (runningThreads.size() < numberOfConcurrentQueries) {
            runningThreads.put(new QueryThreadPool(rpc, connection), domainObject);
        } else {
            queuedQueries.put(domainObject, new RPCInfo(rpc, connection));
        }

    }

    public void waitForQueriesToComplete() throws RPCException {
        while (!queuedQueries.isEmpty() || !runningThreads.isEmpty()) {
            do {
                reapCompletedThreads();
            } while (!runningThreads.isEmpty());

            while (!queuedQueries.isEmpty() && runningThreads.size() < numberOfConcurrentQueries) {
                CanResolveAVistaRPC queuedDomainObject = (CanResolveAVistaRPC) queuedQueries.keySet().toArray()[0];
                RPCInfo rpcInfo = queuedQueries.get(queuedDomainObject);
                queuedQueries.remove(queuedDomainObject);
                runningThreads.put(new QueryThreadPool(rpcInfo.getRpc(), rpcInfo.getConnection()), queuedDomainObject);
            }
        }
    }

    private void reapCompletedThreads() throws RPCException {
        for (IsAThreadedRPCRunner queryThread : runningThreads.keySet()) {
            if (!queryThread.isAlive()) {
                CanResolveAVistaRPC domainObject = runningThreads.get(queryThread);
                domainObject.setReaped(true);
                if (queryThread.getRPCException() != null) {
                    runningThreads.remove(queryThread);
                    logger.error(queryThread.getRPCException());
                    throw new RPCException(queryThread.getRPCException().getMessage());
                } else {
                    domainObject.processResult(queryThread.getRPCResponse());
                    runningThreads.remove(queryThread);
                }
            }
        }

    }

    // two implementations of IsAThreadedRPCRunner, QueryThread is a regular Thread, QueryThreadPool using callable
    // thread pools... a little better.
    private class QueryThread extends Thread implements IsAThreadedRPCRunner {

        private RPCConnection connection;
        private RPCResponse response;
        private VistaRPC rpc;
        private RPCException rpcException;

        private QueryThread() {
        }

        public QueryThread(VistaRPC rpc, RPCConnection connection) {
            System.out.println("(QueryThread) rpc is: " + rpc.getName());
            this.connection = connection;
            this.rpc = rpc;
            start();
        }

        @Override
        public void run() {

            try {
                response = connection.execute(rpc);
            } catch (RPCException e) {
                rpcException = e;
            }

        }

        @Override
        public RPCException getRPCException() {
            return rpcException;
        }

        @Override
        public RPCResponse getRPCResponse() {
            return response;
        }
    };
    private static ExecutorService executor = Executors.newFixedThreadPool(numberOfConcurrentQueries);

    private class QueryThreadPool implements IsAThreadedRPCRunner, Callable<RPCResponse> {

        private RPCConnection connection;
        private RPCResponse response;
        private VistaRPC rpc;
        private RPCException rpcException;
        private Future<?> future;

        private QueryThreadPool() {
        }

        public QueryThreadPool(VistaRPC rpc, RPCConnection connection) {
            System.out.println("(QueryThread) rpc is: " + rpc.getName());
            this.connection = connection;
            this.rpc = rpc;
            future = executor.submit(this);
        }

        public RPCResponse call() throws SQLException {

            try {
                response = connection.execute(rpc);
            } catch (RPCException e) {
                rpcException = e;
            }

            return response;
        }

        public RPCException getRPCException() {
            return rpcException;
        }

        public boolean isAlive() {
            return !future.isDone();
        }

        @Override
        public RPCResponse getRPCResponse() {
            return response;
        }
    };
}
