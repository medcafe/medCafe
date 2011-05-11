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
 * Repository class for MedicationLog
 */

package com.medsphere.ovid.domain.ov;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.medsphere.common.util.TimeKeeperDelegate;
import com.medsphere.fileman.FMQueryList;
import com.medsphere.fileman.FMResultSet;
import com.medsphere.fileman.FMScreen;
import com.medsphere.fileman.FMScreenAnd;
import com.medsphere.fileman.FMScreenEquals;
import com.medsphere.fileman.FMScreenField;
import com.medsphere.fileman.FMScreenGreaterThan;
import com.medsphere.fileman.FMScreenValue;
import com.medsphere.fileman.FMUtil;
import com.medsphere.fmdomain.FMMedicationLog;
import com.medsphere.resource.ResAdapter;
import com.medsphere.resource.ResException;
import com.medsphere.vistarpc.RPCBrokerConnection;
import com.medsphere.vistarpc.RPCConnection;
import com.medsphere.vistarpc.RPCException;

public class MedicationLogRepository extends OvidSecureRepository {

    public MedicationLogRepository(RPCConnection connection) {
        super(null, connection);
    }

    public Collection<FMMedicationLog> getMedicationLogAdministrations(String patientIEN, Date afterDate) throws OvidDomainException {
        return getMedicationLogAdministrations (patientIEN, afterDate, null);
    }

    public Collection<FMMedicationLog> getMedicationLogAdministrations(String patientIEN, Date afterDate, Date beforeDate) throws OvidDomainException {
        Collection<FMMedicationLog> admins = new ArrayList<FMMedicationLog>();
        RPCConnection connection = null;

//        logger.debug("input date: " + afterDate + " : " + FMUtil.dateToFMDate(afterDate));

        try {
            TimeKeeperDelegate.start("Getting Connection");
            connection = getServerConnection();
            TimeKeeperDelegate.stopAndLog("Getting Connection");

            TimeKeeperDelegate.start("Getting Admins");
            ResAdapter adapter = obtainServerRPCAdapter();

            FMQueryList query = new FMQueryList(adapter, FMMedicationLog.getFileInfoForClass());
            query.getField("PATIENT NAME").setInternal(true);
            query.getField("ADMINISTRATION MEDICATION").setInternal(false);

            FMScreen byPatientScreen = null;
            if (patientIEN != null) {
                byPatientScreen = new FMScreenEquals(new FMScreenField(".01"), new FMScreenValue(patientIEN));
            }
            
            FMScreen dateScreen = null;
            if (afterDate != null) {
                dateScreen = new FMScreenGreaterThan(new FMScreenField(".06"), new FMScreenValue(FMUtil.dateToFMDate(afterDate)));
            }

            if (beforeDate != null) {
                FMScreen beforeDateScreen = new FMScreenGreaterThan(new FMScreenValue(FMUtil.dateToFMDate(beforeDate)), new FMScreenField (".06"));

                if (dateScreen == null) {
                    dateScreen = beforeDateScreen;
                } else {
                    dateScreen = new FMScreenAnd (dateScreen, beforeDateScreen);
                }
            }

            if (patientIEN != null) {
                if (dateScreen != null) {
                    query.setScreen(new FMScreenAnd(byPatientScreen, dateScreen));
                } else {
                    query.setScreen(byPatientScreen);
                }
            }

            FMResultSet results = query.execute();
            if (results != null) {
                if (results.getError() != null) {
                    throw new OvidDomainException(results.getError());
                }
                while (results.next()) {
                    admins.add(new FMMedicationLog(results));
                }
            }
        } catch (ResException e) {
            throw new OvidDomainException(e);
        } finally {
            TimeKeeperDelegate.stopAndLog("Getting Admins");
        }

        return admins;
    }

    public static void main(String[] args) throws OvidDomainException {
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
            if (conn==null) {
                return;
            }
            MedicationLogRepository medicationLogRepository = new MedicationLogRepository(conn);
            Calendar cal = Calendar.getInstance();
            cal.set(1900, 1, 1);
            Collection<FMMedicationLog> admins = medicationLogRepository.getMedicationLogAdministrations(null, null); // "2", cal.getTime());
            for (FMMedicationLog admin : admins) {
                Logger.getRootLogger().info(admin.getIEN() +": " + admin.getPatientName() + ", " + admin.getActionDateTime() + ", " + admin.getActionBy() + ", " + admin.getMedication()
                        + ", " + admin.getActionStatus() + ", " + admin.getOrderDosage() + ", " + admin.getOrderSchedule() + ", " + admin.getPrnReason());
            }
            System.out.println("got " + admins.size());
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (RPCException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {

        }
    }
}
