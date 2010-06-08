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

/*
 * domain repository code for Order information.
 */
package com.medsphere.ovid.domain.ov;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.medsphere.common.cache.GenericCacheException;
import com.medsphere.common.cache.GenericCacheReaper;
import com.medsphere.common.util.TimeKeeperDelegate;
import com.medsphere.fileman.FMQueryFind;
import com.medsphere.fileman.FMQueryList;
import com.medsphere.fileman.FMResultSet;
import com.medsphere.fileman.FMRetrieve;
import com.medsphere.fileman.FMScreen;
import com.medsphere.fileman.FMScreenAnd;
import com.medsphere.fileman.FMScreenEquals;
import com.medsphere.fileman.FMScreenField;
import com.medsphere.fileman.FMScreenGreaterThan;
import com.medsphere.fileman.FMScreenOr;
import com.medsphere.fileman.FMScreenValue;
import com.medsphere.fileman.FMUtil;
import com.medsphere.fmdomain.FMDisplayGroup;
import com.medsphere.fmdomain.FMOrder;
import com.medsphere.fmdomain.FMOrderDialog;
import com.medsphere.fmdomain.FMOrderStatus;
import com.medsphere.fmdomain.FMOrderableItem;
import com.medsphere.ovid.model.DisplayGroupCache;
import com.medsphere.ovid.model.OrderDialogCache;
import com.medsphere.ovid.model.OrderStatusCache;
import com.medsphere.resource.ResAdapter;
import com.medsphere.resource.ResException;
import com.medsphere.vistarpc.RPCBrokerConnection;
import com.medsphere.vistarpc.RPCConnection;
import com.medsphere.vistarpc.RPCException;

public class OrderRepository extends OvidSecureRepository {

    private Logger logger = Logger.getLogger(OrderRepository.class);

    public OrderRepository(RPCConnection connection) {
        super(null, connection);
    }

    public Collection<FMOrder> getOrdersForPatientIEN(String patientIEN) throws OvidDomainException {
        Calendar beginningOfTime = GregorianCalendar.getInstance();
        beginningOfTime.set(1860, 1, 1);
        return getOrdersForPatientIEN(patientIEN, beginningOfTime.getTime());
     }

    public Collection<FMOrder> getOrdersForPatientByTypeAndStatus(Collection<String> patientIENs, Date afterDate, Collection<DisplayGroupType> displayGroupList, Collection<OrderStatusType> orderStatusList) throws OvidDomainException {
        Collection<FMOrder> orders = new ArrayList<FMOrder>();
        RPCConnection connection = null;

        logger.debug("input date: " + afterDate + " : " + FMUtil.dateToFMDate(afterDate));

        try {
            TimeKeeperDelegate.start("Getting Connection");
            connection = getServerConnection();
            TimeKeeperDelegate.stopAndLog("Getting Connection");

            TimeKeeperDelegate.start("Getting Orders");
            ResAdapter adapter = obtainServerRPCAdapter();

            FMQueryList query = new FMQueryList(adapter, FMOrder.getFileInfoForClass());
            FMScreen byPatientScreen = null;
            for (String patientIEN : patientIENs) {
                if (byPatientScreen == null) {
                    byPatientScreen = new FMScreenEquals(new FMScreenField(".02"), new FMScreenValue(patientIEN + ";DPT("));
                } else {
                    byPatientScreen = new FMScreenOr(byPatientScreen, new FMScreenEquals(new FMScreenField(".02"), new FMScreenValue(patientIEN + ";DPT(")));
                }
            }

            FMScreen afterDateScreen = new FMScreenGreaterThan(new FMScreenField("31"), new FMScreenValue(FMUtil.dateToFMDate(afterDate)));
            query.getField("7").setInternal(false);
            query.getField("TO").setInternal(false);
            query.getField("STATUS").setInternal(false);
            FMScreen andScreen = new FMScreenAnd(byPatientScreen, afterDateScreen);
            FMScreen displayGroupScreen = null;

            if (displayGroupList != null) {
                for (DisplayGroupType displayGroupType : displayGroupList) {
                    FMDisplayGroup displayGroup = getDisplayGroupByName(displayGroupType.toString());
                    if (displayGroup != null) {
                        if (displayGroupScreen == null) {
                            displayGroupScreen = new FMScreenEquals(new FMScreenField("23"), new FMScreenValue(displayGroup.getIEN()));
                        } else {
                            displayGroupScreen = new FMScreenOr(displayGroupScreen, new FMScreenEquals(new FMScreenField("23"), new FMScreenValue(displayGroup.getIEN())));
                        }

                    }
                 }
            }

            FMScreen orderStatusScreen = null;

            if (orderStatusList != null) {
                for (OrderStatusType os : orderStatusList) {
                    FMOrderStatus orderStatus = getOrderStatusByName(os.toString());
                    if (orderStatus != null) {
                        if (orderStatusScreen == null) {
                            orderStatusScreen = new FMScreenEquals(new FMScreenField("5"), new FMScreenValue(orderStatus.getIEN()));
                        } else {
                            orderStatusScreen = new FMScreenOr(orderStatusScreen, new FMScreenEquals(new FMScreenField("5"), new FMScreenValue(orderStatus.getIEN())));
                        }

                    }
                }
            }

            if (displayGroupScreen != null && orderStatusScreen != null) {
                query.setScreen(new FMScreenAnd(andScreen, new FMScreenAnd(displayGroupScreen, orderStatusScreen)));
            } else if (displayGroupScreen != null) {
                query.setScreen(new FMScreenAnd(andScreen, displayGroupScreen));
            } else if (orderStatusScreen != null) {
                query.setScreen(new FMScreenAnd(andScreen, orderStatusScreen));
            } else {
                query.setScreen(andScreen);
            }

            FMResultSet results = query.execute();
            if (results != null) {
                if (results.getError() != null) {
                    throw new OvidDomainException(results.getError());
                }
                while (results.next()) {
                    orders.add(new FMOrder(results));
                }
            }
        } catch (ResException e) {
            throw new OvidDomainException(e);
        } finally {
            TimeKeeperDelegate.stopAndLog("Getting Orders");
        }

        return orders;

    }

    public Collection<FMOrder> getOrdersForPatientByTypeAndStatus(String patientIEN, Date afterDate, Collection<DisplayGroupType> displayGroupList, Collection<OrderStatusType> orderStatusList) throws OvidDomainException {
        Collection<String> iens = new ArrayList<String>(1);
        iens.add(patientIEN);
        return getOrdersForPatientByTypeAndStatus(iens, afterDate, displayGroupList, orderStatusList);
    }

    public Collection<FMOrder> getOrdersForPatientIEN(String patientIEN, Date afterDate) throws OvidDomainException {
        Collection<FMOrder> orders = new ArrayList<FMOrder>();
        RPCConnection connection = null;

        logger.debug("input date: " + afterDate + " : " + FMUtil.dateToFMDate(afterDate));

        try {
            TimeKeeperDelegate.start("Getting Connection");
            connection = getServerConnection();
            TimeKeeperDelegate.stopAndLog("Getting Connection");

            TimeKeeperDelegate.start("Getting Orders");
            ResAdapter adapter = obtainServerRPCAdapter();

            FMQueryList query = new FMQueryList(adapter, FMOrder.getFileInfoForClass());

            FMScreen byPatientScreen = new FMScreenEquals(new FMScreenField(".02"), new FMScreenValue(patientIEN + ";DPT("));
            FMScreen afterDateScreen = new FMScreenGreaterThan(new FMScreenField("31"), new FMScreenValue(FMUtil.dateToFMDate(afterDate)));

            query.getField("ITEM ORDERED").setInternal(false);
            query.getField("TO").setInternal(false);
            query.getField("STATUS").setInternal(false);

            query.setScreen(new FMScreenAnd(byPatientScreen, afterDateScreen));
            FMResultSet results = query.execute();
            if (results != null) {
                if (results.getError() != null) {
                    throw new OvidDomainException(results.getError());
                }
                while (results.next()) {
                    orders.add(new FMOrder(results));
                }
            }
        }  catch (ResException e) {
            throw new OvidDomainException(e);
        } finally {
            TimeKeeperDelegate.stopAndLog("Getting Orders");
        }

        return orders;

    }
    /**
     * get list of all ORDER STATUS (100.01) entries.
     * @return
     * @throws com.medsphere.ovid.domain.ov.OvidDomainException
     */
    public Collection<FMOrderStatus> getAllOrderStatuses() throws OvidDomainException {
        Collection<FMOrderStatus> statuses = new ArrayList<FMOrderStatus>();
        RPCConnection connection = null;

        try {
            TimeKeeperDelegate.start("Getting Connection");
            connection = getServerConnection();
            TimeKeeperDelegate.stopAndLog("Getting Connection");

            TimeKeeperDelegate.start("Getting AllOrderStatuses");
            ResAdapter adapter = obtainServerRPCAdapter();
            FMQueryList query = new FMQueryList(adapter, FMOrderStatus.getFileInfoForClass());
            FMResultSet results = query.execute();
            if (results != null) {
                if (results.getError() != null) {
                    throw new OvidDomainException(results.getError());
                }
                while (results.next()) {
                    statuses.add(new FMOrderStatus(results));
                }
            }
        } catch (ResException e) {
            throw new OvidDomainException(e);
        } finally {
            TimeKeeperDelegate.stop("Getting AllOrderStatuses");
        }

        return statuses;
    }

    private FMOrderStatus getOrderStatusByName(String name) throws OvidDomainException {
        FMOrderStatus orderStatus = null;
        RPCConnection connection = null;

        try {
            orderStatus = OrderStatusCache.getInstance().getByKey(name);
            if (orderStatus != null) {
                return orderStatus;
            }
        } catch (GenericCacheException ex) {

        }

        try {
            TimeKeeperDelegate.start("Getting Connection");
            connection = getServerConnection();
            TimeKeeperDelegate.stopAndLog("Getting Connection");

            TimeKeeperDelegate.start("Getting OrderStatusByName");
            ResAdapter adapter = obtainServerRPCAdapter();
            FMQueryFind query = new FMQueryFind(adapter, FMOrderStatus.getFileInfoForClass());
            query.setIndex("B", name);
            query.setScreen(new FMScreenEquals(new FMScreenField(".01"), new FMScreenValue(name)));
            FMResultSet results = query.execute();
            if (results != null) {
                if (results.getError() != null) {
                    throw new OvidDomainException(results.getError());
                }
                if (results.next()) {
                    orderStatus = new FMOrderStatus(results);
                }
            }
        } catch (ResException e) {
            throw new OvidDomainException(e);
        } finally {
            TimeKeeperDelegate.stopAndLog("Getting OrderStatusByName");
        }

        if (orderStatus != null) {
            OrderStatusCache.getInstance().addToCache(name, orderStatus);
        }

        return orderStatus;

    }

    private Collection<FMOrder> getOrders() throws OvidDomainException {
        Collection<FMOrder> orders = new ArrayList<FMOrder>();
        RPCConnection connection = null;

        try {
            TimeKeeperDelegate.start("Getting Connection");
            connection = getServerConnection();
            TimeKeeperDelegate.stopAndLog("Getting Connection");

            TimeKeeperDelegate.start("Getting Orders");
            ResAdapter adapter = obtainServerRPCAdapter();
            FMQueryList query = new FMQueryList(adapter, FMOrder.getFileInfoForClass());
            FMResultSet results = query.execute();
            if (results != null) {
                if (results.getError() != null) {
                    throw new OvidDomainException(results.getError());
                }
                while (results.next()) {
                    orders.add(new FMOrder(results));
                }
            }
        } catch (ResException e) {
            throw new OvidDomainException(e);
        } finally {
            TimeKeeperDelegate.stopAndLog("Getting Orders");
        }

        return orders;

    }

    private Collection<FMOrderableItem> getOrderableItems() throws OvidDomainException {
        Collection<FMOrderableItem> orderableItems = new ArrayList<FMOrderableItem>();
        RPCConnection connection = null;

        try {
            TimeKeeperDelegate.start("Getting Connection");
            connection = getServerConnection();
            TimeKeeperDelegate.stopAndLog("Getting Connection");

            TimeKeeperDelegate.start("Getting OrderableItems");
            ResAdapter adapter = obtainServerRPCAdapter();
            FMQueryList query = new FMQueryList(adapter, FMOrderableItem.getFileInfoForClass());
            FMResultSet results = query.execute();
            if (results != null) {
                if (results.getError() != null) {
                    throw new OvidDomainException(results.getError());
                }
                while (results.next()) {
                    orderableItems.add(new FMOrderableItem(results));
                }
            }
        }  catch (ResException e) {
            throw new OvidDomainException(e);
        } finally {
            TimeKeeperDelegate.stopAndLog("Getting OrderableItems");
        }

        return orderableItems;

    }

    public FMOrderDialog getOrderDialogFromPointer(String pointer) throws OvidDomainException {
        FMOrderDialog orderDialog = null;
        RPCConnection connection = null;
        if (pointer == null) {
            return null;
        }
        try {
            if (pointer != null) {
                orderDialog = OrderDialogCache.getInstance().getByKey(pointer);
                if (orderDialog != null) {
                    return orderDialog;
                }
            }
        } catch (GenericCacheException ex) {

        }

        try {
            TimeKeeperDelegate.start("Getting Connection");
            connection = getServerConnection();
            TimeKeeperDelegate.stopAndLog("Getting Connection");

            TimeKeeperDelegate.start("Getting OrderDialog");
            ResAdapter adapter = obtainServerRPCAdapter();

            String ien = "";
            String[] parts = pointer.split(";");
            if (parts.length != 2) {
                throw new OvidDomainException("Unable to determine IEN from pointer passed: " + pointer);
            }
            if (! "101.41".equals(parts[1])) {
                return null;
            } else {
                ien = parts[0];
            }
            orderDialog = new FMOrderDialog();
            orderDialog.setIEN(ien);
            FMRetrieve query = new FMRetrieve(adapter);
            query.setRecord(orderDialog);
            query.execute();

        } catch (ResException e) {
            throw new OvidDomainException(e);
        } finally {
            TimeKeeperDelegate.stopAndLog("Getting OrderDialog");
        }

        if (orderDialog != null) {
            OrderDialogCache.getInstance().addToCache(pointer, orderDialog);
        }
        return orderDialog;

    }
    private Collection<FMDisplayGroup> getDisplayGroups() throws OvidDomainException {
        Collection<FMDisplayGroup> displayGroups = new ArrayList<FMDisplayGroup>();
        RPCConnection connection = null;

        try {
            TimeKeeperDelegate.start("Getting Connection");
            connection = getServerConnection();
            TimeKeeperDelegate.stopAndLog("Getting Connection");

            TimeKeeperDelegate.start("Getting DisplayGroups");
            ResAdapter adapter = obtainServerRPCAdapter();
            FMQueryList query = new FMQueryList(adapter, FMDisplayGroup.getFileInfoForClass());
            FMResultSet results = query.execute();
            if (results != null) {
                if (results.getError() != null) {
                    throw new OvidDomainException(results.getError());
                }
                while (results.next()) {
                    displayGroups.add(new FMDisplayGroup(results));
                }
            }
        } catch (ResException e) {
            throw new OvidDomainException(e);
        } finally {
            TimeKeeperDelegate.stopAndLog("Getting DisplayGroups");
        }

        return displayGroups;

    }

    private FMDisplayGroup getDisplayGroupByName(String name) throws OvidDomainException {
        FMDisplayGroup displayGroup = null;
        RPCConnection connection = null;

        try {
            displayGroup = DisplayGroupCache.getInstance().getByKey(name);
            if (displayGroup != null) {
                return displayGroup;
            }
        } catch (GenericCacheException ex) {

        }

        try {
            TimeKeeperDelegate.start("Getting Connection");
            connection = getServerConnection();
            TimeKeeperDelegate.stopAndLog("Getting Connection");

            TimeKeeperDelegate.start("Getting DisplayGroupByName");
            ResAdapter adapter = obtainServerRPCAdapter();
            FMQueryFind query = new FMQueryFind(adapter, FMDisplayGroup.getFileInfoForClass());
            query.setIndex("B", name);
            query.setScreen(new FMScreenEquals(new FMScreenField(".01"), new FMScreenValue(name)));
            FMResultSet results = query.execute();
            if (results != null) {
                if (results.getError() != null) {
                    throw new OvidDomainException(results.getError());
                }
                if (results.next()) {
                    displayGroup = new FMDisplayGroup(results);
                }
            }
        } catch (ResException e) {
            throw new OvidDomainException(e);
        } finally {
            TimeKeeperDelegate.stopAndLog("Getting DisplayGroupByName");
        }

        if (displayGroup != null) {
            DisplayGroupCache.getInstance().addToCache(name, displayGroup);
        }

        return displayGroup;

    }

    private void getByDisplayGroup(String dfn, Collection<DisplayGroupType> displayGroups) throws OvidDomainException {
        logger.debug("getting by display groups");
        Calendar beginningOfTime = GregorianCalendar.getInstance();
        beginningOfTime.set(1860, 1, 1);
        Collection<FMOrder> orders = getOrdersForPatientByTypeAndStatus(dfn, beginningOfTime.getTime(), displayGroups, null);
        for (FMOrder order : orders) {
            System.out.println("Order: " + order.getIEN() + ", " + order.getOrderNumber() + ", " + order.getObjectOfOrder() + ", " + order.getPatientIEN() + " : " + order.getDialogPointer() + " : " + order.getDisplayGroup() + "[" + order.getDisplayGroupString() + "]" + " : " + order.getItemOrdered() + ", " + order.getDialogPointer() + " : " + order.getStartDate() + " : " + order.getStopDate() + " : " + order.getStatusString());
            FMOrderDialog od = getOrderDialogFromPointer(order.getDialogPointer());
            if (od != null) {
                System.out.println("\torder dialog: " + od.getName() + ", " + od.getDisplayText() + ", " + od.getDisplayGroupString());
            }
        }
        logger.debug("got " + orders.size());

    }

    private void getByOrderStatus(String dfn, Collection<OrderStatusType> orderStatusList) throws OvidDomainException {
       logger.debug("getting by order status");
        Calendar beginningOfTime = GregorianCalendar.getInstance();
        beginningOfTime.set(1860,1,1);
        Collection<FMOrder> orders = getOrdersForPatientByTypeAndStatus(dfn, beginningOfTime.getTime(), null, orderStatusList);
        for (FMOrder order : orders) {
            System.out.println("Order: " + order.getIEN() + ", " + order.getOrderNumber() + ", "
                    + order.getObjectOfOrder() + ", " + order.getPatientIEN() + " : " + order.getDialogPointer() + " : " + order.getDisplayGroup() + "[" + order.getDisplayGroupString() + "]"
                    + " : " + order.getItemOrdered() + ", " + order.getDialogPointer() + " : " +  order.getStartDate() + " : " + order.getStopDate() + " : " + order.getStatusString());
            FMOrderDialog od = getOrderDialogFromPointer(order.getDialogPointer());
            if (od != null) {
                System.out.println("\torder dialog: " + od.getName() + ", " + od.getDisplayText() + ", " + od.getDisplayGroupString());
            }
        }
        logger.debug("got " + orders.size());

    }

    private void getByDisplayGroupAndOrderStatus(String dfn, Collection<DisplayGroupType> displayGroups, Collection<OrderStatusType> orderStatusList) throws OvidDomainException {
        Collection<String> dfns = new ArrayList<String>(1);
        dfns.add(dfn);
        getByDisplayGroupAndOrderStatus(dfns, displayGroups, orderStatusList);
    }

    private void getByDisplayGroupAndOrderStatus(Collection<String> dfns, Collection<DisplayGroupType> displayGroups, Collection<OrderStatusType> orderStatusList) throws OvidDomainException {
         logger.debug("getting by display groups and order status");
        Calendar beginningOfTime = GregorianCalendar.getInstance();
        beginningOfTime.set(1860,1,1);
        Collection<FMOrder> orders = getOrdersForPatientByTypeAndStatus(dfns, beginningOfTime.getTime(), displayGroups, orderStatusList);
        for (FMOrder order : orders) {
             logger.debug("Order: " + order.getIEN() + ", " + order.getOrderNumber() + ", "
                    + order.getObjectOfOrder() + ", " + order.getPatientIEN() + " : " + order.getDialogPointer() + " : " + order.getDisplayGroup() + "[" + order.getDisplayGroupString() + "]"
                    + " : " + order.getItemOrdered() + ", " + order.getDialogPointer() + " : " +  order.getStartDate() + " : " + order.getStopDate() + " : " + order.getStatusString());
            FMOrderDialog od = getOrderDialogFromPointer(order.getDialogPointer());
            if (od != null) {
                 logger.debug("\torder dialog: " + od.getName() + ", " + od.getDisplayText() + ", " + od.getDisplayGroupString());
            }
        }
         logger.debug("got " + orders.size());

    }

    private void testGetBigListsOfThings(RPCConnection conn) throws OvidDomainException {
            Collection<FMOrderStatus> statuses = new OrderRepository(conn).getAllOrderStatuses();
            for (FMOrderStatus status : statuses) {
                System.out.println("OrderStatus: " + status.getIEN() + ", " + status.getName() + ", " + status.getShortName() + "," + status.getAbbreviation());
            }


            Collection<FMOrder> orders = new OrderRepository(conn).getOrders();
            for (FMOrder order : orders) {
                logger.debug("Order: " + order.getIEN() + ", " + order.getOrderNumber() + ", "
                        + order.getObjectOfOrder() + ", " + order.getPatientIEN());
            }
            logger.debug("Got " + orders.size() + " orders");


            Collection<FMOrderableItem> orderableItems = new OrderRepository(conn).getOrderableItems();
            for (FMOrderableItem item : orderableItems) {
                System.out.println("Orderable Item: " + item.getIEN() + ", " + ", " + item.getId() + ", " + item.getName() + ", " + item.getPackageName() + ", " +
                        item.getDisplayGroup() + ", " + item.getLabSection());
            }
            System.out.println("Got " + orderableItems.size() + " items");

            Collection<FMDisplayGroup> displayGroups = new OrderRepository(conn).getDisplayGroups();
            for (FMDisplayGroup group : displayGroups) {
                logger.debug("Display Group: " + group.getIEN() + ", " + ", " + group.getName() + ", " + group.getMixedName() + ", " + group.getShortName());
            }
            logger.debug("Got " + displayGroups.size() + " groups");
//            conn.close();
    }

    public static void main(String[] args) throws OvidDomainException, GenericCacheException {
        RPCConnection conn = null;
        try {
            BasicConfigurator.configure();
            Logger.getRootLogger().setLevel(Level.INFO);
            TimeKeeperDelegate.setLogLevel(Level.INFO);

            args = new String[] { "localhost", "9201", "OV1234", "OV1234!!"};
            if (args.length < 4) {
                System.err.println("usage: PatientRepository <host> <port> <ovid-access-code> <ovid-verify-code>");
                return;
            }

            conn = new RPCBrokerConnection(args[0], new Integer(args[1]), args[2], args[3]);
            //new OrderRepository(conn).testGetBigListsOfThings(conn);


            if (conn==null) {
                return;
            }
            OrderRepository orderRepository = new OrderRepository(conn);
            Collection<FMOrder> orders = orderRepository.getOrdersForPatientIEN("2");
            for (FMOrder order : orders) {
                System.out.println("Order: " + order.getIEN() + ", " + order.getOrderNumber() + ", "
                        + order.getObjectOfOrder() + ", " + order.getPatientIEN() + " : " + order.getDialogPointer() + " : " + order.getDisplayGroup() + "[" + order.getDisplayGroupString() + "]"
                        + " : " + order.getItemOrdered() + ", " + order.getDialogPointer() + " : " +  order.getStartDate() + " : " + order.getStopDate() + " : " + order.getStatusString());
                FMOrderDialog od = orderRepository.getOrderDialogFromPointer(order.getDialogPointer());
                if (od != null) {
                    System.out.println("\torder dialog: " + od.getName() + ", " + od.getDisplayText() + ", " + od.getDisplayGroupString());
                }
            }

            System.out.println("got " + orders.size());
            FMDisplayGroup dg = orderRepository.getDisplayGroupByName(DisplayGroupType.DIET_ADDITIONAL_ORDERS.toString());
            System.out.println("display group " + DisplayGroupType.DIET_ORDERS + ": " + dg.getIEN() + ", " + dg.getMixedName() + ", " + dg.getShortName());

            FMOrderStatus os = orderRepository.getOrderStatusByName(OrderStatusType.ACTIVE.toString());
            System.out.println("order status " + OrderStatusType.ACTIVE + ": " + os.getIEN() + ", " + os.getAbbreviation() + ", " + os.getShortName());

            Collection<DisplayGroupType> displayGroups = new ArrayList<DisplayGroupType>();
            displayGroups.add(DisplayGroupType.DIET_ORDERS);
            displayGroups.add(DisplayGroupType.DIET_ADDITIONAL_ORDERS);
            orderRepository.getByDisplayGroup("2", displayGroups);
            Collection<OrderStatusType> orderStatusList = new ArrayList<OrderStatusType>();
            orderStatusList.add(OrderStatusType.ACTIVE);
            orderStatusList.add(OrderStatusType.UNRELEASED);
            orderRepository.getByOrderStatus("2", orderStatusList);
            Collection<String> dfns = new ArrayList<String>();
            dfns.add("2");
            dfns.add("3");
            dfns.add("4");
            dfns.add("5");
            dfns.add("6");
            dfns.add("7");
            dfns.add("8");
            dfns.add("9");
            orderRepository.getByDisplayGroupAndOrderStatus(dfns, displayGroups, orderStatusList);
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (RPCException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            GenericCacheReaper.getInstance().removeCache(OrderDialogCache.getInstance());
            GenericCacheReaper.getInstance().removeCache(OrderStatusCache.getInstance());
            GenericCacheReaper.getInstance().removeCache(DisplayGroupCache.getInstance());
        }
    }

}
