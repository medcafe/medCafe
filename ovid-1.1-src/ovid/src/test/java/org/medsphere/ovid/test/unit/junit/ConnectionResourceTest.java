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

import com.medsphere.vistarpc.RPCConnection;
import com.medsphere.vistarpc.RPCException;
import com.medsphere.vistarpc.RPCResponse;
import com.medsphere.vistarpc.VistaRPC;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.medsphere.connection.MockConnection;
import org.medsphere.connection.VistaConnectionException;
import org.medsphere.connection.VistaConnectionProperties;
import org.medsphere.datasource.ServiceLocator;
import org.medsphere.datasource.VistaDataSource;
import org.medsphere.datasource.VistaServiceLoaderDataSource;
import org.medsphere.datasource.pool.VistaConnectionPoolManager;

/**
 *
 * @author apardue
 */
public class ConnectionResourceTest extends TestCase {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    private VistaConnectionProperties getMockProperties() {
        VistaConnectionProperties props = new VistaConnectionProperties();
        props.put("brokerType", "Mock");
        return props;
    }

    @Test
    public void testInstantiationOfTheServiceLocatorWithAMockDataSource() throws VistaConnectionException {
        VistaDataSource dataSource = ServiceLocator.getInstance().getDataSource(getMockProperties());
        Assert.assertNotNull(dataSource);
    }

    @Test
    public void testThatEmptyPropertiesShouldReturnAGenericDataSource() throws VistaConnectionException {
        Assert.assertTrue(ServiceLocator.getInstance().getDataSource(new VistaConnectionProperties()) instanceof VistaServiceLoaderDataSource);
    }

    @Test
    public void testGettingAConnection() throws VistaConnectionException {
        RPCConnection connection = ServiceLocator.getInstance().getDataSource(getMockProperties()).getConnection();
        Assert.assertNotNull(connection);
    }

    private VistaDataSource getPooledDataSource(String poolName) throws VistaConnectionException {
        VistaConnectionProperties props = getMockProperties();
        props.put("poolName", poolName);
        return ServiceLocator.getInstance().getDataSource(props);
    }

    @Test
    public void testPooling() throws VistaConnectionException, RPCException {
        final String poolName = "JUnitTestPool";
        VistaRPC statusRPC = new VistaRPC(MockConnection.STATUS_RPC_NAME, RPCResponse.ResponseType.ARRAY);

        VistaDataSource dataSource = getPooledDataSource(poolName);
        Assert.assertNotNull(dataSource);

        RPCConnection conn1 = dataSource.getConnection();
        Assert.assertNotNull(conn1);
        String conn1ID = conn1.execute(statusRPC).getArray()[2];
        conn1.close();

        // closing conn1 and getting a new datasource should give us the same connection again
        RPCConnection conn2 = dataSource.getConnection();
        String conn2ID = conn2.execute(statusRPC).getArray()[2];
        Assert.assertEquals(conn1ID, conn2ID);

        // now, don't close conn2 and get another one... should be a different connection
        RPCConnection conn3 = dataSource.getConnection();
        Assert.assertFalse(conn2ID.equals(conn3.execute(statusRPC).getArray()[2]));

        // we haven't closed down either conn2 or conn3, so shutdown the pool
        // which will close them.
        // Then, get a connection and make sure it is a new one.
        VistaConnectionPoolManager.getInstance().shutdownPool(poolName);
        try {
            dataSource.getConnection();
        } catch (VistaConnectionException e) {
            // we expect an error here because we shouldn't be able to get a connection
            // from a pool that has been shutdown
            return;
        }
        Assert.fail("was able to retrieve a connection from a pool that has been shutdown -- should not happen");
        
    }

    @Test
    public void testPinging() throws VistaConnectionException, InterruptedException, RPCException {
        Long intervalInMillis = new Long("10");
        Long waitInMillis = intervalInMillis * 3;
        String poolName = "PingedPool";
        VistaConnectionProperties props = getMockProperties();
        props.put("pingInterval", intervalInMillis.toString());
        props.put("poolName", poolName);

        VistaDataSource ds = ServiceLocator.getInstance().getDataSource(props);
        Assert.assertNotNull(ds);

        VistaRPC statusRPC = new VistaRPC(MockConnection.STATUS_RPC_NAME, RPCResponse.ResponseType.ARRAY);

        RPCConnection conn1 = ds.getConnection(props);
        String conn1ID = conn1.execute(statusRPC).getArray()[2];
        try {
            conn1.close(); // need to close to get pinged
        } catch (RPCException ex) {
            Assert.fail("Got RPC Exception: " + ex.getMessage());
        }

        try {
            Thread.sleep(waitInMillis);
        } catch (InterruptedException ex) {
            Assert.fail("Interrupted in sleep: " + ex.getMessage());
            throw ex;
        }

        // now, let's get the connection back
        RPCConnection conn2 = ds.getConnection(props);
        Assert.assertEquals(conn1ID, conn2.execute(statusRPC).getArray()[2]);

        // Connection should have been pinged by now
        RPCResponse response = conn2.execute(statusRPC);
        String[] parts = response.getArray();
        Assert.assertEquals("XWB IM HERE", parts[0]);
        Long timeStamp = new Long(parts[1]);
        long pad = 200; // milliseconds for execute pad
        // see if the execute timestamp is within the interval plus a pad
        // the pad is needed for an execute time.
        Assert.assertTrue(timeStamp + intervalInMillis + pad >= System.currentTimeMillis());

        try {
            Thread.sleep(waitInMillis);
        } catch (InterruptedException ex) {
            Assert.fail("Interrupted in sleep: " + ex.getMessage());
            throw ex;
        }
        // no ping should have occurred.
        response = conn2.execute(statusRPC);
        String[] parts2 = response.getArray();
        Assert.assertArrayEquals(parts, parts2);

        VistaConnectionPoolManager.getInstance().shutdownPool(poolName);
    }

    @Test
    public void testTimeouts() throws VistaConnectionException, RPCException, InterruptedException {

        Long intervalInMillis = new Long("10");
        String poolName = "TimeoutPool";
        VistaConnectionProperties props = getMockProperties();
        props.put("timeoutInterval", intervalInMillis.toString());
        props.put("poolName", poolName);

        VistaDataSource ds = ServiceLocator.getInstance().getDataSource(props);
        Assert.assertNotNull(ds);

        RPCConnection conn1 = ds.getConnection(props);
        conn1.execute(new VistaRPC("NO OP"));

        try {
            Thread.sleep(intervalInMillis*3);
        } catch (InterruptedException ex) {
            Assert.fail("Interrupted sleep: " + ex.getMessage());
            throw ex;
        }

        try {
            conn1.execute(new VistaRPC("NO OP"));
        } catch (RPCException e) {
            Assert.assertTrue(e.getMessage().matches("^Connection expired.+"));
            return;           
        }

        Assert.fail("should never get here");

    }
}
