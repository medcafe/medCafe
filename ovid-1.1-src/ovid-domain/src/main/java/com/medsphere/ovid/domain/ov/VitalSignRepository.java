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
package com.medsphere.ovid.domain.ov;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.medsphere.fileman.FMFile;
import com.medsphere.fileman.FMQueryFind;
import com.medsphere.fileman.FMQueryList;
import com.medsphere.fileman.FMRecord;
import com.medsphere.fileman.FMResultSet;
import com.medsphere.fileman.FMScreen;
import com.medsphere.fileman.FMScreenIsNull;
import com.medsphere.fileman.FMUtil;
import com.medsphere.fmdomain.FMVMeasurement;
import com.medsphere.fmdomain.FMVMeasurementType;
import com.medsphere.resource.ResAdapter;
import com.medsphere.resource.ResException;
import com.medsphere.vistarpc.RPCBrokerConnection;
import com.medsphere.vistarpc.RPCConnection;
import com.medsphere.vistarpc.RPCException;
import com.medsphere.vistarpc.VistaRPC;
import com.medsphere.vistarpc.RPCResponse.ResponseType;

public class VitalSignRepository extends OvidSecureRepository {
    private Logger logger = LoggerFactory.getLogger(VitalSignRepository.class);

    public VitalSignRepository(RPCConnection connection, RPCConnection serverConnection) {
        super(connection, serverConnection);
        setContext("OR CPRS GUI CHART");
    }

    public List<IsAVitalSign> getVitalsGridForPatient(String dfn, Date startDate, Date endDate) throws OvidDomainException {

        try {
            RPCConnection rpcConnection = getConnection();
            rpcConnection.setContext(getContext());
            VistaRPC behovmGrid = new VistaRPC("BEHOVM GRID", ResponseType.ARRAY);
            behovmGrid.setParam(1, dfn); // DFN
            behovmGrid.setParam(2, FMUtil.dateToFMDate(startDate)); // from
            behovmGrid.setParam(3, FMUtil.dateToFMDate(endDate)); // to
            behovmGrid.setParam(4, "");
            behovmGrid.setParam(5, "");
            behovmGrid.setParam(6, ""); // visit
            behovmGrid.setParam(7, "-1"); // metric
            behovmGrid.setParam(8, "3"); // significant digits
            //behovmGrid.setParam(9, "1"); // 1 - use "taken" date/times
            logger.debug("Calling vitals grid RPC: " + behovmGrid);
            String rpcResults[] = rpcConnection.execute(behovmGrid).getArray();
            return getReadingsFromGrid(rpcResults, dfn);
        } catch (RPCException ex) {
            throw new OvidDomainException(ex);
        }

    }

    private List<IsAVitalSign> getReadingsFromGrid(String[] rpcResults, String patientDFN) throws OvidDomainException {
        List<IsAVitalSign> list = new ArrayList<IsAVitalSign>();

        class TypeRow {

            public String name;
            public String id;
            public String units;
            public String refRange;

            private TypeRow(String string) {
                String parts[] = string.split("\\^", -1);
                id = parts[0];
                name = parts[2];
                units = parts[4];
                if (!(parts[5].isEmpty() && parts[5].isEmpty())) {
                    refRange = parts[5] + "^" + parts[6];
                } else {
                    refRange = "";
                }
            }
        }
        int gridOffset = 0;

        String counts[] = rpcResults[gridOffset++].split("\\^", -1);
        int typeCount = Integer.parseInt(counts[0]);
        int dateCount = Integer.parseInt(counts[1]);
        int measurementCount = Integer.parseInt(counts[2]);
        TypeRow[] types = new TypeRow[typeCount + 1];
        for (int i = 1; i <= typeCount; ++i) {
            logger.debug("type: " + rpcResults[gridOffset]);
            types[i] = new TypeRow(rpcResults[gridOffset++]);
        }
        Date[] dates = new Date[dateCount + 1];
        for (int i = 0; i < dateCount; ++i) {
            logger.debug("date: " + rpcResults[gridOffset]);
            String parts[] = rpcResults[gridOffset++].split("\\^", -1);

            try {
                dates[Integer.parseInt(parts[0])] = FMUtil.fmDateToDate(parts[1]);
            } catch (ParseException ex) {
                throw new OvidDomainException(ex);
            }
        }
        for (int i = 0; i < measurementCount; ++i) {
            logger.debug("measurement: " + rpcResults[gridOffset]);
            String parts[] = rpcResults[gridOffset++].split("\\^", -1);
            String ien = parts[4];
            TypeRow typeRow = types[Integer.parseInt(parts[1])];

            VitalSign vitalSign = new VitalSign(ien, typeRow.id, typeRow.name, parts[2], dates[Integer.parseInt(parts[0])],
                    typeRow.units, typeRow.refRange, parts[5].split("~", -1));
            list.add(vitalSign);
        }

        return list;
    }


    public Collection<IsAVitalSign> getVitalSignsForDFN(String dfn, boolean fetchQualifiers, boolean includeEnteredInError) throws OvidDomainException {
        Collection<IsAVitalSign> vlist = new ArrayList<IsAVitalSign>();
        try {
            ResAdapter adapter = obtainServerRPCAdapter();

            FMQueryFind query = new FMQueryFind(adapter, FMVMeasurement.getFileInfoForClass());
            query.getField("VALUE").setInternal(true);
            query.getField("TYPE").setInternal(true);
            query.getField("PATIENT NAME").setInternal(false);
            query.getField("VISIT").setInternal(false);
            query.getField("ENTERED BY").setInternal(false);
            query.getField("ERROR ENTERED BY").setInternal(false);
            query.getField("ORDERING PROVIDER").setInternal(false);
            query.getField("CLINIC").setInternal(false);
            query.getField("ENCOUNTER PROVIDER").setInternal(false);
            query.getField("PARENT").setInternal(false);
            query.getField("ORDERING LOCATION").setInternal(false);
            query.setIndex("AC", dfn);

            if (!includeEnteredInError) {
                FMScreen screen = new FMScreenIsNull("ENTERED IN ERROR", true);
                query.setScreen(screen);
            }

            FMResultSet results = query.execute();
            if (results != null) {
                if (results.getError() != null) {
                    throw new OvidDomainException(results.getError());
                }
                while (results.next()) {
                    FMVMeasurement vm = new FMVMeasurement(results);
                    if (fetchQualifiers) {
                        getQualifiers(vm);
                    }
                    vlist.add(vm);
                }
            }
        } catch (ResException e) {
            throw new OvidDomainException(e);
        } finally {
        }

        return vlist;
    }

    public void getQualifiers(Collection<FMVMeasurement> vMeasurements) throws OvidDomainException {
        for (FMVMeasurement vm : vMeasurements) {
            getQualifiers(vm);
        }
    }

    public void getQualifiers(FMVMeasurement vMeasurement) throws OvidDomainException {
        try {
            ResAdapter adapter = obtainServerRPCAdapter();
            FMFile qualifier = vMeasurement.getQualifierSubfile();

            FMQueryList query = new FMQueryList(adapter,  qualifier);
            FMResultSet results = query.execute();
            while (results.next()) {
                FMRecord entry = new FMRecord(results);
                if (entry != null && entry.getValue(".01") != null) {
                    vMeasurement.addQualifier(entry.getValue(".01"));
                }
            }
        } catch (ResException e) {
            throw new OvidDomainException(e);
        } finally {}

    }


    private Map<String,FMVMeasurementType> measurementTypeMap = null;

    public FMVMeasurementType getMeasurementTypeByIEN(String ien) throws OvidDomainException {
        if (measurementTypeMap == null) {
            loadMeasurementTypes();
        }
        return measurementTypeMap.get(ien);
    }

    private void loadMeasurementTypes() throws OvidDomainException {
        measurementTypeMap = new HashMap<String,FMVMeasurementType>();
        try {
            ResAdapter adapter = obtainServerRPCAdapter();

            FMQueryList query = new FMQueryList(adapter, FMVMeasurementType.getFileInfoForClass());
            query.getField("CPT CODE").setInternal(false);
            FMResultSet results = query.execute();
            if (results != null) {
                if (results.getError() != null) {
                    throw new OvidDomainException(results.getError());
                }
                while (results.next()) {
                    FMVMeasurementType type = new FMVMeasurementType(results);
                    measurementTypeMap.put(type.getIEN(), type);
                }
            }
        } catch (ResException e) {
            throw new OvidDomainException(e);
        } finally {
        }

    }

    public static void main(String[] args) {
        //BasicConfigurator.configure();
        //Logger.getRootLogger().setLevel(Level.ERROR);
        RPCConnection userConn = null;
        RPCConnection serverConn = null;

        try {

            if (args == null || args.length == 0) {
                args = new String[] { "localhost", "9201", "PU1234", "PU1234!!", "OV1234", "OV1234!!"};
            }

            if (args.length < 5) {
                System.err.println("usage: ImmunizationRepository <host> <port> <user-access-code> <user-verify-code> <ovid-access-code> <ovid-verify-code>");
                return;
            }

            userConn = new RPCBrokerConnection(args[0], new Integer(args[1]), args[2], args[3]);
            serverConn = new RPCBrokerConnection(args[0], new Integer(args[1]), args[4], args[5]);
            if (serverConn==null || userConn==null) {
                return;
            }

            Calendar cal = GregorianCalendar.getInstance();
            cal.set(2000, Calendar.JANUARY, 1);
            VitalSignRepository repo = new VitalSignRepository(userConn, serverConn);
            for (Integer i = 14484; i <= 14484; i++) {
                List<IsAVitalSign> list = repo.getVitalsGridForPatient(i.toString(), cal.getTime(), new Date());
                Collections.sort(list, new IsAVitalSign.SortByDate());

                System.out.println("*** dfn: " + i);
                for (IsAVitalSign vitalSign : list) {
                    System.out.println(vitalSign);
                }
                Collection<IsAVitalSign> vlist = repo.getVitalSignsForDFN(i.toString(), false, true);
                System.out.println("*** VMeasurement");
                for (IsAVitalSign vm : vlist) {
                    //repo.getQualifiers(vm);
                    System.out.println(vm);
                }
            }
            System.out.println(repo.getMeasurementTypeByIEN("1"));
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (RPCException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (OvidDomainException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (userConn != null) {
                try {
                    userConn.close();
                } catch (RPCException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            if (serverConn != null) {
                try {
                    serverConn.close();
                } catch (RPCException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        System.exit(0);

    }


}
