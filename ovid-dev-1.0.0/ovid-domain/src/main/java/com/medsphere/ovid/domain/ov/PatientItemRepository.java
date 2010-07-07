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
package com.medsphere.ovid.domain.ov;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import com.medsphere.common.cache.GenericCacheReaper;
import com.medsphere.fileman.FMFile;
import com.medsphere.fileman.FMQueryList;
import com.medsphere.fileman.FMRecord;
import com.medsphere.fileman.FMResultSet;
import com.medsphere.fileman.FMScreen;
import com.medsphere.fileman.FMScreenEquals;
import com.medsphere.fileman.FMScreenIEN;
import com.medsphere.fileman.FMScreenOr;
import com.medsphere.fileman.FMScreenValue;
import com.medsphere.fileman.FMScreenField;
import com.medsphere.fileman.FMUtil;
import com.medsphere.fileman.FMField.FIELDTYPE;

import com.medsphere.fmdomain.FMV_Immunization;

import com.medsphere.ovid.model.DisplayGroupCache;
import com.medsphere.ovid.model.OrderStatusCache;
import com.medsphere.ovid.model.domain.patient.IsAPatientItem;
import com.medsphere.ovid.model.domain.patient.PatientAlert;
import com.medsphere.ovid.model.domain.patient.PatientAllergy;
import com.medsphere.ovid.model.domain.patient.PatientEvent;
import com.medsphere.ovid.model.domain.patient.PatientItemType;
import com.medsphere.ovid.model.domain.patient.PatientMedication;
import com.medsphere.ovid.model.domain.patient.PatientNursingOrder;
import com.medsphere.ovid.model.domain.patient.PatientNursingTask;
import com.medsphere.ovid.model.domain.patient.PatientOrder;
import com.medsphere.ovid.model.domain.patient.PatientProblem;
import com.medsphere.ovid.model.domain.patient.PatientResult;
import com.medsphere.ovid.model.domain.patient.PatientUnsignedOrder;
import com.medsphere.ovid.model.domain.patient.PatientUnverifiedOrder;
import com.medsphere.ovid.model.domain.patient.PatientVitalEvent;
import com.medsphere.ovid.model.domain.patient.ResultDetail;
import com.medsphere.ovid.model.domain.patient.VitalSignDetail;
import com.medsphere.ovid.model.domain.patient.PatientImmunization;
import com.medsphere.resource.ResAdapter;
import com.medsphere.resource.ResException;
import com.medsphere.vistarpc.RPCArray;
import com.medsphere.vistarpc.RPCBrokerConnection;
import com.medsphere.vistarpc.RPCConnection;
import com.medsphere.vistarpc.RPCException;
import com.medsphere.vistarpc.RPCResponse;
import com.medsphere.vistarpc.VistaRPC;
import com.medsphere.vistarpc.RPCResponse.ResponseType;
import com.medsphere.fmdomain.FMV_Immunization;


/**
 * This class handles patient task items like
 * unverified orders, alerts, results, etc. as modeled
 * in the CareManagement RPCs
 */
public class PatientItemRepository extends OvidSecureRepository {
    private Logger logger = Logger.getLogger(PatientItemRepository.class);
    private String fmContext;

    public PatientItemRepository(RPCConnection connection, RPCConnection serverConnection, String fmContext) {
        super(connection, serverConnection);
        this.fmContext = fmContext;
    }

    public Collection<IsAPatientItem> getQuickItems(String duz, String patientDfn, Date startDate, Date stopDate) throws OvidDomainException {
        Collection<IsAPatientItem> collection = new ArrayList<IsAPatientItem>();
        collection.addAll(getNursingTasks(patientDfn));
        collection.addAll(getAlerts(patientDfn, duz));
        collection.addAll(getProblems(patientDfn));
        collection.addAll(getAllergies(patientDfn));
        collection.addAll(getEvents(patientDfn, duz));
        collection.addAll(getMedications(patientDfn));
        return collection;
    }

    public Collection<IsAPatientItem> getAllItems(String duz, String patientDfn, Date startDate, Date stopDate) throws OvidDomainException {
        Collection<IsAPatientItem> collection = new ArrayList<IsAPatientItem>();
        collection.addAll(getNursingTasks(patientDfn));
        collection.addAll(getAlerts(patientDfn, duz));
        collection.addAll(getProblems(patientDfn));
        collection.addAll(getAllergies(patientDfn));
        collection.addAll(getEvents(patientDfn, duz));
        collection.addAll(getMedications(patientDfn));
        collection.addAll(getNursingOrders(patientDfn, startDate, stopDate, duz));
        collection.addAll(getUnsignedOrdersByUser(patientDfn, startDate, stopDate, duz));
        collection.addAll(getUnverifiedOrders(patientDfn, startDate, stopDate, duz));
        collection.addAll(getResults(patientDfn, duz, startDate, stopDate, false));
        collection.addAll(getVitals(patientDfn, startDate, stopDate));
        return collection;
    }

    public Collection<IsAPatientItem> getNursingTasks(String patientDfn) throws OvidDomainException {
        Collection<IsAPatientItem> collection = new ArrayList<IsAPatientItem>();
        RPCConnection rpcConnection;

        try {
            rpcConnection = getConnection();
            rpcConnection.setContext(fmContext);
        } catch (RPCException ex) {
            throw new OvidDomainException(ex);
        }

        VistaRPC rpc = new VistaRPC("ORRC TASKS BY PATIENT", ResponseType.ARRAY);
        rpc.setParam(1, patientDfn);
        try {
            RPCResponse response = rpcConnection.execute(rpc);
            String items[] = response.getArray();
            if (isEmptyResult(items)) {
                return collection;
            }
            for (String item : items) {
                String parts[] = item.split("\\^",-1);
                String id = parts[0];
                String message = parts[1];
                String datePart = parts[6];
                while (datePart.length()<14) {
                    datePart += "0";
                }
                Date date = parseHL7TimeFormat(datePart);
                String status = parts[5];
                collection.add(new PatientNursingTask(id, message, status, date));
            }
        } catch (ParseException ex) {
            logger.error(ex);
        } catch (RPCException ex) {
            logger.error(ex);
        }
        return collection;
    }

    public Collection<IsAPatientItem> getAlerts(String patientDfn, String duz) throws OvidDomainException {
        Collection<IsAPatientItem> collection = new ArrayList<IsAPatientItem>();
        RPCConnection rpcConnection;
        try {
            rpcConnection = getConnection();
            rpcConnection.setContext(fmContext);
        } catch (RPCException e) {
            throw new OvidDomainException(e);
        }

        VistaRPC rpc = new VistaRPC("ORRC ALERTS BY PATIENT", ResponseType.ARRAY);
        rpc.setParam(1, patientDfn);
        rpc.setParam(2, duz);

        String items[] = null;
        try {
            RPCResponse response = rpcConnection.execute(rpc);
            items = response.getArray();
            if (isEmptyResult(items)) {
                return collection;
            }
            for (int idx = 0; idx<items.length; ++idx) {
                if (items[idx].startsWith("Item=")) {
                    String parts[] = items[idx].split("\\^",-1);
                    String id = parts[0].substring(5, parts[0].length());
                    String message = parts[1];
                    String datePart = parts[2];
                    if (datePart.length()!=19) {
                        // Pad zeros for missing HH, mm, or ss
                        StringBuffer sb = new StringBuffer(datePart.substring(0, datePart.length()-5));
                        for (int i=19-datePart.length(); i>0; --i) {
                            sb.append("0");
                        }
                        sb.append(datePart.substring(datePart.length()-5));
                        datePart = sb.toString();
                    }
                    Date dateTime = null;
                    try {
                        dateTime = parseHL7TimeFormat(datePart);
                    } catch (ParseException ex) {
                        logger.error(ex);
                    }

                    collection.add(new PatientAlert(id, message, dateTime));
                }
            }
        } catch (RPCException ex) {
            throw new OvidDomainException(ex);
        }

        return collection;

    }

    public Collection<PatientProblem> getProblems(String patientDfn) throws OvidDomainException{
        Collection<PatientProblem> collection = new ArrayList<PatientProblem>();
        RPCConnection rpcConnection;
        try {
            rpcConnection = getConnection();
            rpcConnection.setContext(fmContext);
        } catch (RPCException e) {
            throw new OvidDomainException(e);
        }

        VistaRPC rpc = new VistaRPC("ORQQPL LIST", ResponseType.ARRAY);
        rpc.setParam(1, patientDfn);
        rpc.setParam(2, "A");
        try {
            RPCResponse response = rpcConnection.execute(rpc);
            String items[] = response.getArray();
            for (String item : items) {
                String parts[] = item.split("\\^", -1);
                if (!parts[0].isEmpty()) {
                    if (parts.length >= 5) {
                        try {
                            collection.add(new PatientProblem(parts[0], parts[1], parts[2], parts[3], FMUtil.fmDateToDate(parts[4]), FMUtil.fmDateToDate(parts[5])));
                        } catch (ParseException e) {
                            collection.add(new PatientProblem(parts[0], parts[1]));
                        }
                    } else {
                        collection.add(new PatientProblem(parts[0], parts[1]));
                    }
                }
            }
        } catch (RPCException ex) {
            throw new OvidDomainException(ex);
        }
        return collection;
    }

    public Collection<IsAPatientItem> getAllergies(String patientDfn) throws OvidDomainException {
        return getAllergies(patientDfn, false);
    }

    public Collection<IsAPatientItem> getAllergies(String patientDfn, boolean withDetails) throws OvidDomainException {

        Collection<IsAPatientItem> collection = new ArrayList<IsAPatientItem>();
        Collection<String> allergyIds = new ArrayList<String>();

        RPCConnection rpcConnection;
        try {
            rpcConnection = getConnection();
            rpcConnection.setContext(fmContext);
        } catch (RPCException e) {
            throw new OvidDomainException(e);
        }

        VistaRPC rpc = new VistaRPC("ORQQAL LIST", ResponseType.ARRAY);
        rpc.setParam(1, patientDfn);
        try {
            RPCResponse response = rpcConnection.execute(rpc);
            String items[] = response.getArray();
            for (String item : items) {
                String parts[] = item.split("\\^", 4);
                if (parts.length >= 4) {
                    collection.add(new PatientAllergy(parts[0], parts[1], parts[2], parts[3]));
                } else if (!parts[0].isEmpty()) {
                    collection.add(new PatientAllergy(parts[0], parts[1]));
                }
                if (withDetails && !parts[0].isEmpty()) {
                    allergyIds.add(parts[0]);
                }
            }
        } catch (RPCException ex) {
            throw new OvidDomainException(ex);
        }

        if (withDetails == true) {
            fetchAllergyDetails(collection, allergyIds);
        }
        return collection;
    }


    private void fetchAllergyDetails(Collection<IsAPatientItem> collection, Collection<String> allergyIds) throws OvidDomainException {
        if (collection.size() > 0 && allergyIds.size() > 0) {
            FMFile patientAllergy = new FMFile("120.8");
            patientAllergy.addField("1", FIELDTYPE.VARIABLE_POINTER); // GMR ALLERGY
            patientAllergy.addField("3.1"); // allergy type
            patientAllergy.addField("4", FIELDTYPE.DATE); // origination date/time
            patientAllergy.addField("5", FIELDTYPE.POINTER_TO_FILE); // originator
            patientAllergy.addField("20", FIELDTYPE.DATE); // verification date/time
            patientAllergy.addField("21", FIELDTYPE.POINTER_TO_FILE); // verifier
            patientAllergy.addField("6", FIELDTYPE.SET_OF_CODES); // HISTORICAL/OBSERVED
            patientAllergy.addField("22", FIELDTYPE.SET_OF_CODES); // entered in error
            patientAllergy.addField("17", FIELDTYPE.SET_OF_CODES); // mechanism
            patientAllergy.addField("19", FIELDTYPE.SET_OF_CODES); // verified

            try {
                FMQueryList query;
                ResAdapter adapter = obtainServerRPCAdapter();
                query = new FMQueryList( adapter, patientAllergy );
                FMScreen byIens = null;
                for (String ien : allergyIds) {
                    if (byIens == null) {
                        byIens = new FMScreenEquals(new FMScreenIEN(), new FMScreenValue(ien));
                    } else {
                        byIens = new FMScreenOr(byIens, new FMScreenEquals(new FMScreenIEN(), new FMScreenValue(ien)));
                    }
                }
                query.setScreen(byIens);
                query.getField("5").setInternal(false);
                query.getField("21").setInternal(false);
                FMResultSet results = query.execute();
                while (results.next()) {
                    FMRecord entry = new FMRecord(results);
                    for (IsAPatientItem item : collection) {
                        if (item instanceof PatientAllergy) {
                            PatientAllergy allergy = (PatientAllergy)item;
                            if (allergy.getId().equals(entry.getIEN())) {
                                allergy.setAllergyType(entry.getValue("3.1"));
                                allergy.setMechanism(entry.getValue("17"));
                                allergy.setObservationType(entry.getValue("6"));
                                allergy.setOriginator(entry.getValue("5"));
                                allergy.setVerifier(entry.getValue("21"));
                                try {
                                    if (entry.getValue("4") != null) {
                                        allergy.setOriginationDate(FMUtil.fmDateToDate(entry.getValue("4")));
                                    }
                                    if (entry.getValue("20") != null) {
                                        allergy.setVerificationDate(FMUtil.fmDateToDate(entry.getValue("20")));
                                    }
                                } catch (ParseException e) {
                                    logger.error(e);
                                }

                                if ("YES".equalsIgnoreCase(entry.getValue("19"))) {
                                    allergy.setVerified(Boolean.TRUE);
                                } else if ("NO".equalsIgnoreCase(entry.getValue("19"))) {
                                    allergy.setVerified(Boolean.FALSE);
                                }
                            }
                        }
                    }
                    logger.debug("type: " + entry.getValue("3.1")
                            + ", gmr allergy: " + entry.getValue("1")
                            + ", originationDate: " + entry.getValue("4")
                            + ", originator: " + entry.getValue("5")
                            + ", verificationDate: " + entry.getValue("20")
                            + ", verifier: " + entry.getValue("21")
                            + ", historical/observed: " + entry.getValue("6")
                            + ", entered in error: " + entry.getValue("22")
                            + ", verified: " + entry.getValue("19")
                         );
                }

            } catch (OvidDomainException e) {
                throw e;
            } catch (ResException e) {
                throw new OvidDomainException(e);
            }

        }
    }
    public Collection<IsAPatientItem> getMedications(String patientDfn) throws OvidDomainException {
        Collection<IsAPatientItem> collection = new ArrayList<IsAPatientItem>();
        RPCConnection rpcConnection;
        try {
            rpcConnection = getConnection();
            rpcConnection.setContext(fmContext);
        } catch (RPCException e) {
            throw new OvidDomainException(e);
        }

        VistaRPC activeRpc = new VistaRPC("ORWPS ACTIVE", ResponseType.ARRAY);
        activeRpc.setParam(1, patientDfn);
        RPCArray orders = new RPCArray();
        try {
            RPCResponse activeResponse = rpcConnection.execute(activeRpc);
            String items[] = activeResponse.getArray();
            if (isEmptyResult(items)) {
                return collection;
            }
            int orderNum = 0;
            for (String item : items) {
                if (item.startsWith("~")) {
                    String parts[] = item.split("\\^", 11);
                    String orderID = parts[8].split(";", 2)[0];
                    orders.put(Integer.toString(orderNum++), "OR1:"+orderID);
                }
            }
            if (orderNum!=0) {
                VistaRPC ordersRpc = new VistaRPC("ORRC ORDERS BY ID", ResponseType.ARRAY);
                ordersRpc.setParam(1, orders);
                RPCResponse orderResponse = rpcConnection.execute(ordersRpc);
                String orderItems[] = orderResponse.getArray();
                PatientMedication med = null;
                for (String orderItem : orderItems) {
                    // System.out.println(orderItem);
                    /* This will work like a SAX parser - it's event driven */
                    //when a new item is started, add it to the collection right away...subsequent if's will populate it further
                    if (orderItem.startsWith("Item")) {
                        String parts[] = orderItem.split("\\^",-1);
                        String idParts[] = parts[0].split(":",-1);
                        String orderID = idParts[1];
                        String message = parts[1];
                        String datePart = parts[2];
                        while (datePart.length()<14) {
                            datePart += "0";
                        }
                        Date date = null;
                        try {
                            date = parseMedTimeFormat(datePart);
                        } catch (ParseException ex) {
                            date = null;
                        }
                        String status = parts[3];
                        med = new PatientMedication(orderID, message, status, date);
                        collection.add(med);
                    }
                    //now look for other things to process
                    if(orderItem.startsWith("Text=Medication:"))
                    {
                        //ex  Text=Medication:                   QUINAPRIL TAB
                        String[] meat = orderItem.split(":");
                        med.setMedName( meat[1].trim() );
                    }
                    if(orderItem.startsWith("Text=Instructions:"))
                    {
                        //ex  Text=Instructions:                 500MG ORAL ONCE
                        String[] meat = orderItem.substring(20).trim().split(" ");
                        med.setDose( meat[0] );
                        med.setDelivery( meat[1] );
                        med.setFrequency( meat[2] );
                    }
                    if(orderItem.startsWith("Text=Start:"))
                    {
                        //ex Text=Start:                        Mar 12, 2010@12:43:33
                        String meat = orderItem.substring(20).trim();
                        SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy@HH:mm:ss");
                        Date date = format.parse( meat );
                        med.setDateTime( date );
                    }
                }
            }
        } catch (Exception ex) {
            throw new OvidDomainException(ex);
        }
        return collection;
    }


    public Collection<IsAPatientItem> getEvents(String patientDfn, String duz) throws OvidDomainException {
        Collection<IsAPatientItem> collection = new ArrayList<IsAPatientItem>();
        RPCConnection rpcConnection;
        try {
            rpcConnection = getConnection();
            rpcConnection.setContext(fmContext);
        } catch (RPCException e) {
            throw new OvidDomainException(e);
        }

        VistaRPC rpc = new VistaRPC("ORRC EVENTS BY PATIENT", ResponseType.ARRAY);
        rpc.setParam(1, patientDfn);
        rpc.setParam(2, duz);

        String items[] = null;
        try {
            RPCResponse response = rpcConnection.execute(rpc);
            items = response.getArray();
            if (isEmptyResult(items)) {
                return collection;
            }
            for (int idx = 0; idx<items.length; ++idx) {
                if (items[idx].startsWith("Item=")) {
                    String parts[] = items[idx].split("\\^",-1);
                    String id = parts[0].substring(5, parts[0].length());
                    String message = parts[1];
                    String datePart = parts[2];
                    if (datePart.length()!=19) {
                        // Pad zeros for missing HH, mm, or ss
                        StringBuffer sb = new StringBuffer(datePart.substring(0, datePart.length()-5));
                        for (int i=19-datePart.length(); i>0; --i) {
                            sb.append("0");
                        }
                        sb.append(datePart.substring(datePart.length()-5));
                        datePart = sb.toString();
                    }
                    Date dateTime = null;
                    try {
                        dateTime = parseHL7TimeFormat(datePart);
                    } catch (ParseException ex) {
                        logger.error(ex);
                    }

                    collection.add(new PatientEvent(id, message, dateTime));
                }
            }
        } catch (RPCException ex) {
            throw new OvidDomainException(ex);
        }

        return collection;
    }
    public Collection<PatientImmunization> getImmunizations(String patientDfn) throws OvidDomainException {
        Collection<PatientImmunization> collection = new ArrayList<PatientImmunization>();

			try {

            ResAdapter adapter = obtainServerRPCAdapter();

            FMQueryList query = new FMQueryList(adapter, FMV_Immunization.getFileInfoForClass());

            FMScreen screen = null;
       
                    screen = new FMScreenEquals(new FMScreenField("PATIENT NAME"), new FMScreenValue(patientDfn));
             
            
            

            query.setScreen(screen);
            query.setPacked(false);

            query.getField("IMMUNIZATION").setInternal(false);
            query.getField("PATIENT NAME").setInternal(false);
            query.getField("VISIT").setInternal(false);
            query.getField("DIAGNOSIS").setInternal(false);
            query.getField("DIAGNOSIS 2").setInternal(false);
            query.getField("DIAGNOSIS 3").setInternal(false);
            query.getField("DIAGNOSIS 4").setInternal(false);
            query.getField("DIAGNOSIS 5").setInternal(false);
            query.getField("DIAGNOSIS 6").setInternal(false);
            query.getField("DIAGNOSIS 7").setInternal(false);
            query.getField("DIAGNOSIS 8").setInternal(false);
            query.getField("ORDERING PROVIDER").setInternal(false);
            query.getField("ENCOUNTER PROVIDER").setInternal(false);
          
            FMResultSet results = query.execute();
            if (results != null) {
                if (results.getError() != null) {
                    throw new OvidDomainException(results.getError());
                }
                while (results.next()) {
                    FMV_Immunization immune = new FMV_Immunization(results);
                    PatientImmunization patImmune = new PatientImmunization(immune.getVisitDate().toString(), immune.getImmunizationValue(), immune.getEventDate(),
                    immune.getDiagnosisValue(), immune.getDiagnosis2Value(), immune.getDiagnosis3Value(), immune.getDiagnosis4Value(), immune.getDiagnosis5Value(),
                    immune.getDiagnosis6Value(), immune.getDiagnosis7Value(), immune.getDiagnosis8Value(),
    					  immune.getSeries(), immune.getReaction(), immune.getContraindicated(), immune.getRemarks(),
    					  immune.getOrderingProviderValue(), immune.getEncounterProviderValue());
    					  collection.add(patImmune);

                }
            } else {
                return null;
            }
        } catch (ResException e) {
            throw new OvidDomainException(e);
        }
       
    

    


        return collection;
    }

   public Collection<IsAPatientItem> getNursingOrders(String patientDfn, Date startDate, Date stopDate, String duz) throws OvidDomainException {
       return getOrders(PatientItemType.NursingOrder, patientDfn, startDate, stopDate, duz);
   }

   public Collection<IsAPatientItem> getUnverifiedOrders(String patientDfn, Date startDate, Date stopDate, String duz) throws OvidDomainException {
       return getOrders(PatientItemType.UnverifiedOrder, patientDfn, startDate, stopDate, duz);
   }

   public Collection<IsAPatientItem> getUnsignedOrdersByUser(String patientDfn, Date startDate, Date stopDate, String duz) throws OvidDomainException {
       return getOrders(PatientItemType.UnsignedOrder, patientDfn, startDate, stopDate, duz);
   }

    private Collection<IsAPatientItem> getOrders(PatientItemType type, String patientDfn, Date startDate, Date stopDate, String duz) throws OvidDomainException {
        Collection<IsAPatientItem> collection = new ArrayList<IsAPatientItem>();
        RPCConnection rpcConnection;
        try {
            rpcConnection = getConnection();
            rpcConnection.setContext(fmContext);
        } catch (RPCException e) {
            throw new OvidDomainException(e);
        }

        VistaRPC rpc = new VistaRPC("ORRC ORDERS BY PATIENT", ResponseType.ARRAY);
        rpc.setParam(1, patientDfn);
        switch (type) {
            case NursingOrder:
                rpc.setParam(2, "ORN");
                break;
            case UnverifiedOrder:
                rpc.setParam(2, "ORV");
                break;
            case UnsignedOrder:
                rpc.setParam(2, "ORU");
                break;
            default:
                rpc.setParam(2, "ORN");
        }

        rpc.setParam(3, duz);
        rpc.setParam(4, "0");
        rpc.setParam(5, getHL7TimeFormat().format(startDate));
        rpc.setParam(6, getHL7TimeFormat().format(stopDate));
        //System.out.println("getting orders by type " + type + " for dfn " + patientDfn + ", start date = " + startDate + ", end date = " + stopDate);
        String items[] = null;
        try {
            RPCResponse response = rpcConnection.execute(rpc);
            items = response.getArray();
            if (isEmptyResult(items)) {
                return collection;
            }

            IsAPatientItem item = null;

            for (int idx = 0; idx<items.length; ++idx) {
                if (items[idx].startsWith("Item=")) {
                    if (item != null) {
                        collection.add(item);
                        item = null;
                    }

                    String parts[] = items[idx].split("\\^",-1);
                    String id = parts[0].substring(5, parts[0].length());
                    String message = parts[1];
                    String datePart = parts[2];
                    if (datePart.length()!=19) {
                        // Pad zeros for missing HH, mm, or ss
                        StringBuffer sb = new StringBuffer(datePart.substring(0, datePart.length()-5));
                        for (int i=19-datePart.length(); i>0; --i) {
                            sb.append("0");
                        }
                        sb.append(datePart.substring(datePart.length()-5));
                        datePart = sb.toString();
                    }
                    Date dateTime;
                    try {
                        dateTime = parseHL7TimeFormat(datePart);
                    } catch (ParseException ex) {
                        dateTime = null;
                    }
                    String status = parts[3];
                     switch (type) {
                        case NursingOrder:
                            item = new PatientNursingOrder(id, message, status, dateTime);
                            break;
                        case UnverifiedOrder:
                            item = new PatientUnverifiedOrder(id, message, status, dateTime);
                            break;
                        case UnsignedOrder:
                            item = new PatientUnsignedOrder(id, message, status, dateTime);
                            break;
                    }
                } else if (items[idx].startsWith("Order=")) {
                    if (item == null) {
                        logger.error("Found Order= not preceded with Item= record");
                    } else {
                        ((PatientOrder) item).addDetail(items[idx].substring(6, items[idx].length()));
                    }
                }
            }
            if (item != null) {
                collection.add(item);
            }

        } catch (RPCException ex) {
            throw new OvidDomainException(ex);
        }

        return collection;
    }


    public Collection<IsAPatientItem> getResults(String patientDfn, String duz, Date startDate, Date stopDate, boolean details) throws OvidDomainException {
        Collection<IsAPatientItem> collection = new ArrayList<IsAPatientItem>();
        RPCConnection rpcConnection;
        try {
            rpcConnection = getConnection();
            rpcConnection.setContext(fmContext);
        } catch (RPCException e) {
            throw new OvidDomainException(e);
        }
        VistaRPC rpc = new VistaRPC("ORRC RESULTS BY DATE", ResponseType.ARRAY);
        //System.out.println("getResults: duz == " + duz + ", dfn = "+ patientDfn);
        rpc.setParam(1, patientDfn);
        rpc.setParam(2, duz);
        rpc.setParam(3, getHL7TimeFormat().format(startDate));
        rpc.setParam(4, getHL7TimeFormat().format(stopDate));
        if (details) {
            rpc.setParam(5, "1");
        } else {
            rpc.setParam(5, "0");
        }
        String items[] = null;
        try {
            RPCResponse response = rpcConnection.execute(rpc);
            items = response.getArray();
            if (isEmptyResult(items)) {
                return collection;
            }
            IsAPatientItem item = null;

            for (int idx = 0; idx<items.length; ++idx) {
                //System.out.println("RESULT: " + items[idx]);
                if (items[idx].startsWith("Item=")) {
                    if (item != null) {
                        collection.add(item);
                        item = null;
                    }
                    String parts[] = items[idx].split("\\^",-1);
                    String id = parts[0].substring(5, parts[0].length());
                    String message = parts[1];
                    String datePart = parts[2];

                    if ("-1".equals(datePart)) {

                    } else if (datePart.length()!=19) {
                        // Pad zeros for missing HH, mm, or ss
                        StringBuffer sb = new StringBuffer(datePart.substring(0, datePart.length()-5));
                        for (int i=19-datePart.length(); i>0; --i) {
                            sb.append("0");
                        }
                        sb.append(datePart.substring(datePart.length()-5));
                        datePart = sb.toString();
                    }

                    Date dateTime;
                    try {
                        dateTime = parseHL7TimeFormat(datePart);
                    } catch (ParseException ex) {
                        dateTime = null;
                    }
                    //String status = parts[3];
                    //collection.add(new PatientResult(id, message, "", dateTime));
                    item = new PatientResult(id, message, "", dateTime);
                } else if (items[idx].startsWith("Text=")) {
                    ((PatientResult) item).addText(items[idx].substring(5, items[idx].length()));
                } else if (items[idx].startsWith("Cmnt=")) {
                    ((PatientResult) item).addComment(items[idx].substring(5, items[idx].length()));
                }  else if (items[idx].startsWith("Data=")) {
                    String parts[] = items[idx].split("\\^",-1);
                    String testName = parts[0].substring(5, parts[0].length());
                    String value = parts[1];
                    String units = parts[2];
                    String referenceRange = parts[3];
                    String indicator = parts[4];
                    ((PatientResult) item).addDetail(new ResultDetail(testName, value, units, referenceRange, indicator));
                }
            }
           if (item != null) {
                collection.add(item);
            }

        } catch (RPCException ex) {
            throw new OvidDomainException(ex);
        }

         return collection;
    }

    public IsAPatientItem getResultsByID(String ID) throws OvidDomainException {

        RPCConnection rpcConnection;
        try {
            rpcConnection = getConnection();
            rpcConnection.setContext(fmContext);
        } catch (RPCException e) {
            throw new OvidDomainException(e);
        }
        VistaRPC rpc = new VistaRPC("ORRC RESULTS BY ID", ResponseType.ARRAY);
        //System.out.println("getResults: duz == " + duz + ", dfn = "+ patientDfn);
        RPCArray ids = new RPCArray();
        ids.put("0", ID);
        rpc.setParam(1, ids);

        String items[] = null;
        try {
            RPCResponse response = rpcConnection.execute(rpc);
            items = response.getArray();
            if (isEmptyResult(items)) {
                return null;
            }
            IsAPatientItem item = null;

            for (int idx = 0; idx<items.length; ++idx) {
                //System.out.println("RESULT: " + items[idx]);
                if (items[idx].startsWith("Item=")) {
                    String parts[] = items[idx].split("\\^",-1);
                    String id = parts[0].substring(5, parts[0].length());
                    String message = parts[1];
                    String datePart = parts[2];

                    if ("-1".equals(datePart)) {

                    } else if (datePart.length()!=19) {
                        // Pad zeros for missing HH, mm, or ss
                        StringBuffer sb = new StringBuffer(datePart.substring(0, datePart.length()-5));
                        for (int i=19-datePart.length(); i>0; --i) {
                            sb.append("0");
                        }
                        sb.append(datePart.substring(datePart.length()-5));
                        datePart = sb.toString();
                    }

                    Date dateTime;
                    try {
                        dateTime = parseHL7TimeFormat(datePart);
                    } catch (ParseException ex) {
                        dateTime = null;
                    }
                    //String status = parts[3];
                    //collection.add(new PatientResult(id, message, "", dateTime));
                    item = new PatientResult(id, message, "", dateTime);
                } else if (items[idx].startsWith("Text=")) {
                    ((PatientResult) item).addText(items[idx].substring(5, items[idx].length()));
                } else if (items[idx].startsWith("Cmnt=")) {
                    ((PatientResult) item).addComment(items[idx].substring(5, items[idx].length()));
                }  else if (items[idx].startsWith("Data=")) {
                    String parts[] = items[idx].split("\\^",-1);
                    String testName = parts[0].substring(5, parts[0].length());
                    String value = parts[1];
                    String units = parts[2];
                    String referenceRange = parts[3];
                    String indicator = parts[4];
                    ((PatientResult) item).addDetail(new ResultDetail(testName, value, units, referenceRange, indicator));
                }
            }

            return item;


        } catch (RPCException ex) {
            throw new OvidDomainException(ex);
        }


    }

    public Collection<IsAPatientItem> getVitals(String patientDfn, Date startDate, Date stopDate) throws OvidDomainException {
        Collection<IsAPatientItem> collection = new ArrayList<IsAPatientItem>();
        RPCConnection rpcConnection;
        try {
            rpcConnection = getConnection();
            rpcConnection.setContext(fmContext);
        } catch (RPCException e) {
            throw new OvidDomainException(e);
        }
        VistaRPC rpc = new VistaRPC("ORRC VITALS BY PATIENT", ResponseType.ARRAY);
        //System.out.println("getResults: duz == " + duz + ", dfn = "+ patientDfn);
        rpc.setParam(1, patientDfn);
        rpc.setParam(2, getHL7TimeFormat().format(startDate));
        rpc.setParam(3, getHL7TimeFormat().format(stopDate));
        rpc.setParam(4, "1");

        String items[] = null;
        try {
            RPCResponse response = rpcConnection.execute(rpc);
            items = response.getArray();
            if (isEmptyResult(items)) {
                return collection;
            }
            IsAPatientItem item = null;

            for (int idx = 0; idx<items.length; ++idx) {
                //System.out.println("RESULT: " + items[idx]);
                if (items[idx].startsWith("Item=")) {
                    if (item != null) {
                        collection.add(item);
                        item = null;
                    }
                    String parts[] = items[idx].split("\\^",-1);
                    String id = parts[0].substring(5, parts[0].length());
                    String datePart = parts[2];

                    Date dateTime;
                    try {
                        dateTime = FMUtil.fmDateToDate(datePart); //dateTime = parseHL7TimeFormat(datePart);
                    } catch (ParseException ex) {
                        dateTime = null;
                    }
                    item = new PatientVitalEvent(id, dateTime);
                } else if (items[idx].startsWith("Data=")) {
                    String parts[] = items[idx].split("\\^",-1);
                    String name = parts[0].substring(5, parts[0].length());
                    String value = parts[1];
                    String units = parts[2];
                    String metric = parts[3];
                    String metricUnits = parts[4];
                    String bmi = parts[5];
                    String so2 = parts[6];
                    String indicator = parts[7];
                    String qualifiers = parts[8];
                    ((PatientVitalEvent) item).addDetail(new VitalSignDetail(name, value, units, metric, metricUnits, bmi, so2, indicator, qualifiers));
                }
            }
            if (item != null) {
                collection.add(item);
            }
        } catch (RPCException ex) {
            throw new OvidDomainException(ex);
        }

        return collection;
    }

    public IsAPatientItem getVitalsByID(String ID) throws OvidDomainException {
        IsAPatientItem item = null;
        RPCConnection rpcConnection;
        try {
            rpcConnection = getConnection();
            rpcConnection.setContext(fmContext);
        } catch (RPCException e) {
            throw new OvidDomainException(e);
        }
        VistaRPC rpc = new VistaRPC("ORRC VITALS BY ID", ResponseType.ARRAY);
        //System.out.println("getResults: duz == " + duz + ", dfn = "+ patientDfn);
        RPCArray ids = new RPCArray();
        ids.put("1", ID);
        rpc.setParam(1, ids);

        String items[] = null;
        try {
            RPCResponse response = rpcConnection.execute(rpc);
            items = response.getArray();
            if (isEmptyResult(items)) {
                return null;
            }

            for (int idx = 0; idx<items.length; ++idx) {
                System.out.println("VITAL: " + items[idx]);
                if (items[idx].startsWith("Item=")) {
                     String parts[] = items[idx].split("\\^",-1);
                    String id = parts[0].substring(5, parts[0].length());
                    String datePart = parts[2];

                    Date dateTime;
                    try {
                        dateTime = FMUtil.fmDateToDate(datePart); //dateTime = parseHL7TimeFormat(datePart);
                    } catch (ParseException ex) {
                        dateTime = null;
                    }
                    item = new PatientVitalEvent(id, dateTime);
                } else if (items[idx].startsWith("Data=")) {
                    String parts[] = items[idx].split("\\^",-1);
                    String name = parts[0].substring(5, parts[0].length());
                    String value = parts[1];
                    String units = parts[2];
                    String metric = parts[3];
                    String metricUnits = parts[4];
                    String bmi = parts[5];
                    String so2 = parts[6];
                    String indicator = parts[7];
                    String qualifiers = parts[8];
                    ((PatientVitalEvent) item).addDetail(new VitalSignDetail(name, value, units, metric, metricUnits, bmi, so2, indicator, qualifiers));
                }
            }

        } catch (RPCException ex) {
            throw new OvidDomainException(ex);
        }

        return item;
    }

    private SimpleDateFormat getHL7TimeFormat() {
        return new SimpleDateFormat("yyyyMMddHHmmssZ");
    }

    private String normalizeDate(String dateString) {
        if (dateString.length()>=14) {
            return dateString;
        }
        StringBuilder sb = new StringBuilder(dateString);
        for (int i=14-dateString.length(); i>0; --i) {
            sb.append("0");
        }
        return sb.toString();
    }

    private Date parseMedTimeFormat(String dateString) throws ParseException {
        return new SimpleDateFormat("yyyyMMddHHmmss").parse(normalizeDate(dateString));
    }

    private Date parseHL7TimeFormat(String dateString) throws ParseException {
        String [] parts= dateString.split("[+-]");
        if (parts.length!=2) {
            throw new ParseException("No timezone or incorrect format [" + dateString + "]", 0);
        }

        StringBuilder sb = new StringBuilder(normalizeDate(parts[0]));
        sb.append(dateString.contains("+") ? "+" : "-");
        sb.append(parts[1]);
        return getHL7TimeFormat().parse(sb.toString());
    }

    public static void main(String[] args) throws OvidDomainException {
        RPCConnection conn = null;
        RPCConnection serverConn = null;
        BasicConfigurator.configure();
        org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.WARN);

        try {

            args = new String[] { "openvista.medsphere.org", "9201", "DB1234", "DB1234!!"};
            if (args.length < 4) {
                System.err.println("usage: PatientList <host> <port> <access-code> <verify-code>");
                return;
            }


            conn = new RPCBrokerConnection(args[0], new Integer(args[1]), args[2], args[3]);
            String duz = conn.getDUZ();
            if (conn==null) {
                System.err.println("unable to get connection.  is vistalink running?");
            }
            System.out.println("duz is: " + duz);
            Calendar cal = GregorianCalendar.getInstance();
            cal.set(2000, Calendar.JANUARY, 1);
            Collection<IsAPatientItem> list = new PatientItemRepository(conn, conn, "MSC PATIENT DASHBOARD").getAllItems(duz, "2", cal.getTime(), new Date());
            for (IsAPatientItem task : list) {
                System.out.println(task.toString());
            }
            System.out.println("total: " + list.size());

            IsAPatientItem res = new PatientItemRepository(conn, conn, "MSC PATIENT DASHBOARD").getResultsByID("ORR:12");
            if (res != null) {
                System.out.println(res.toString());
            }


        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (RPCException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
                if (serverConn != null) {
                    serverConn.close();
                }
            } catch (RPCException e) {}
//            GenericCacheReaper.getInstance().removeCache(OrderStatusCache.getInstance());
//            GenericCacheReaper.getInstance().removeCache(DisplayGroupCache.getInstance());

        }
        System.exit(0);
    }
}
