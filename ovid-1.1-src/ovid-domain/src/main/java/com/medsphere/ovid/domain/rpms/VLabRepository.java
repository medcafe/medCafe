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
package com.medsphere.ovid.domain.rpms;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Map;
import java.util.WeakHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.medsphere.cia.CIABrokerConnection;
import com.medsphere.common.cache.GenericCacheException;
import com.medsphere.fileman.FMQueryFind;
import com.medsphere.fileman.FMQueryList;
import com.medsphere.fileman.FMResultSet;
import com.medsphere.fileman.FMScreen;
import com.medsphere.fileman.FMScreenAnd;
import com.medsphere.fileman.FMScreenEquals;
import com.medsphere.fileman.FMScreenField;
import com.medsphere.fileman.FMScreenGreaterThan;
import com.medsphere.fileman.FMScreenIsNull;
import com.medsphere.fileman.FMScreenOr;
import com.medsphere.fileman.FMScreenValue;
import com.medsphere.fileman.FMUtil;
import com.medsphere.fileman.FMField.FIELDTYPE;
import com.medsphere.fmdomain.FMLaboratoryTest;
import com.medsphere.fmdomain.rpms.FMVLab;
import com.medsphere.ovid.domain.lab.IsAnOVLabResult;
import com.medsphere.ovid.domain.lab.LabRepository;
import com.medsphere.ovid.domain.lab.OVLabResult;
import com.medsphere.ovid.domain.ov.OvidDomainException;
import com.medsphere.ovid.model.LaboratoryTestCache;
import com.medsphere.resource.ResAdapter;
import com.medsphere.resource.ResException;
import com.medsphere.vistarpc.RPCBrokerConnection;
import com.medsphere.vistarpc.RPCConnection;
import com.medsphere.vistarpc.RPCException;

public class VLabRepository extends LabRepository {

    private Logger logger = LoggerFactory.getLogger(VLabRepository.class);

    public VLabRepository(RPCConnection serverConnection) {
        super(null, serverConnection);
    }

    @Override
    public Collection<IsAnOVLabResult> getResults(String patientDfn,
            Date startDate, Date stopDate) throws OvidDomainException {
        return getResults(patientDfn, startDate, stopDate, false);
    }

    @Override
    public Collection<IsAnOVLabResult> getResults(String patientDfn, Date startDate, Date stopDate, boolean resolveLabTests) throws OvidDomainException {

        Collection<IsAVLAbTestResult> vlabResults = getVLabByDFN(patientDfn, startDate, stopDate, false);
        if (resolveLabTests) {
            resolveLaboratoryTest(vlabResults);
        }
        Map<IsAVLAbTestResult, Collection<IsAVLAbTestResult>> panelMap = createLabPanelMap(vlabResults);

        for (IsAVLAbTestResult panel : panelMap.keySet()) {
            FMVLab labPanel = (FMVLab) panel;
            OVLabResult ovLab = new OVLabResult(labPanel.getIEN(), labPanel.getResults(), labPanel.getCurrentStatusFlag(), labPanel.getResultDateAndTime());
            ovLab.setLaboratoryTest(labPanel.getLaboratoryTest());
            ovLab.addPanelInfo(labPanel);
        }

        return null;
    }

    public Collection<IsAVLAbTestResult> getVLabByDFN(String dfn, boolean internal)
            throws OvidDomainException {
        Calendar cal = GregorianCalendar.getInstance();
        cal.set(1950, Calendar.JANUARY, 1);
        return getVLabByDFN(dfn, cal.getTime(), new Date(), internal);
    }

    public Collection<IsAVLAbTestResult> getVLabByDFN(String dfn, Date beginDate,
            Date endDate, boolean internal) throws OvidDomainException {
        Collection<IsAVLAbTestResult> labs = new ArrayList<IsAVLAbTestResult>();
        try {

            ResAdapter adapter = obtainServerRPCAdapter();

            FMQueryFind query = new FMQueryFind(adapter, FMVLab.getFileInfoForClass());
            query.setIndex("AC", dfn);
            query.getField("LAB TEST").setInternal(true);
            query.getField("PATIENT NAME").setInternal(internal);
            query.getField("VISIT").setInternal(internal);
            query.getField("SITE").setInternal(internal);
            query.getField("LOINC CODE").setInternal(internal);
            query.getField("COLLECTION SAMPLE").setInternal(internal);
            query.getField("ORDERING PROVIDER").setInternal(internal);
            query.getField("CLINIC").setInternal(internal);
            query.getField("ENCOUNTER PROVIDER").setInternal(internal);
            query.getField("ANCILLARY POV").setInternal(internal);
            query.getField("ORDERING LOCATION").setInternal(internal);

            FMScreen afterDateScreen = new FMScreenGreaterThan(
                    new FMScreenField("1211"), new FMScreenValue(FMUtil.dateToFMDate(beginDate)));
            FMScreen beforeDateScreen = new FMScreenGreaterThan(
                    new FMScreenValue(FMUtil.dateToFMDate(endDate)),
                    new FMScreenField("1211"));

            FMScreen afterOrEqualDateScreen = new FMScreenOr(afterDateScreen,
                    new FMScreenEquals(new FMScreenField("1211"),
                    new FMScreenValue(FMUtil.dateToFMDate(beginDate))));
            FMScreen beforeOrEqualDateScreen = new FMScreenOr(beforeDateScreen,
                    new FMScreenEquals(new FMScreenField("1211"),
                    new FMScreenValue(FMUtil.dateToFMDate(endDate))));

            query.setScreen(new FMScreenAnd(beforeOrEqualDateScreen,
                    afterOrEqualDateScreen));

            FMResultSet results = query.execute();
            if (results != null) {
                if (results.getError() != null) {
                    throw new OvidDomainException(results.getError());
                }
                while (results.next()) {
                    labs.add(new FMVLab(results));
                }
            }
        } catch (ResException e) {
            e.printStackTrace();
            throw new OvidDomainException(e);
        }
        return labs;
    }

    public void resolveLaboratoryTest(Collection<IsAVLAbTestResult> labs) throws OvidDomainException {
        Collection<FMLaboratoryTest> laboratoryTests = new ArrayList<FMLaboratoryTest>();
        Collection<String> labIens = new HashSet<String>();

        for (IsAVLAbTestResult lab : labs) {
            labIens.add(lab.getLabTest());
        }
        laboratoryTests = getLaboratoryTest(labIens);
        for (IsAVLAbTestResult lab : labs) {
            for (FMLaboratoryTest laboratoryTest : laboratoryTests) {
                findVLabTestsWithEncoding(laboratoryTest);
                if (lab.getLabTest().equals(laboratoryTest.getIEN())) {
                    lab.setLaboratoryTest(laboratoryTest);
                    break;
                }
            }
        }
    }

    public void findVLabTestsWithEncoding(FMLaboratoryTest laboratoryTest) throws OvidDomainException {

        try {
            if (LaboratoryTestCache.getInstance().getByKey(
                    laboratoryTest.getIEN()) != null) {
                return; // already looked up this one.
            }
            ResAdapter adapter = obtainServerRPCAdapter();

            FMQueryFind query = new FMQueryFind(adapter, FMVLab.getFileInfoForClass());

            query.setIndex("B", laboratoryTest.getIEN());

            query.setNumber(1);

            FMScreen loincScreen = new FMScreenIsNull("LOINC CODE", false);
            FMScreen cptPtrScreen = new FMScreenIsNull("CPT PTR", false);
            query.setScreen(new FMScreenOr(loincScreen, cptPtrScreen));

            FMResultSet results = query.execute();

            if (results != null) {
                if (results.getError() != null) {
                    throw new OvidDomainException(results.getError());
                }
                if (results.next()) {
                    FMVLab vlab = new FMVLab(results);
                    if (vlab.getLoincCode() != null) {
                        laboratoryTest.setLoincCode(vlab.getLoincCode());
                    }
                    if (vlab.getCptPtr() != null) {
                        String cptCode = getCPTCodeForPointer(vlab.getCptPtr());
                        laboratoryTest.setCptCode(cptCode);
                    }
                }
                LaboratoryTestCache.getInstance().addToCache(
                        laboratoryTest.getIEN(), laboratoryTest);
            }
        } catch (ResException e) {
            throw new OvidDomainException(e);
        } catch (GenericCacheException e) {
            throw new OvidDomainException(e);
        }

    }

    public Map<IsAVLAbTestResult, Collection<IsAVLAbTestResult>> createLabPanelMap(Collection<IsAVLAbTestResult> labs) {

        Map<IsAVLAbTestResult, Collection<IsAVLAbTestResult>> labMap = new WeakHashMap<IsAVLAbTestResult, Collection<IsAVLAbTestResult>>();
        for (IsAVLAbTestResult testResult : labs) {
            if (testResult instanceof FMVLab) {
                FMVLab lab = (FMVLab) testResult;
                if (lab.getResults() == null || lab.getParentRecord() == null) {
                    logger.debug("Cataloging parent of: " + lab.getIEN());
                    labMap.put(lab, new ArrayList<IsAVLAbTestResult>());
                } else {
                    logger.debug("\tLooking for parent id: "
                            + lab.getParentRecord());
                    IsAVLAbTestResult parent = findParent(lab, labMap);
                    if (parent == null) {
                        System.err.println("ERROR: Parent not found for: "
                                + lab);
                        continue;
                    } else {
                        labMap.get(parent).add(lab);
                    }
                }
            }
        }
        return labMap;
    }

    public Map<String, Collection<IsAVLAbTestResult>> groupByAccessionNumber(Map<IsAVLAbTestResult, Collection<IsAVLAbTestResult>> panelMap) {
        Map<String, Collection<IsAVLAbTestResult>> accessionToPanelMap = new WeakHashMap<String, Collection<IsAVLAbTestResult>>();
        for (IsAVLAbTestResult panel : panelMap.keySet()) {
            String accession = panel.getAccessionNumber();
            if (!accessionToPanelMap.containsKey(accession)) {
                accessionToPanelMap.put(accession, new ArrayList<IsAVLAbTestResult>());
            }

            accessionToPanelMap.get(accession).add(panel);
        }
        return accessionToPanelMap;
    }

    private IsAVLAbTestResult findParent(IsAVLAbTestResult child, Map<IsAVLAbTestResult, Collection<IsAVLAbTestResult>> labMap) {
        IsAVLAbTestResult parent = null;

        for (IsAVLAbTestResult lab : labMap.keySet()) {
            if (lab.getIEN().equals(child.getParentRecord())) {
                parent = lab;
                break;
            }
        }

        return parent;
    }

    private void printLabPanelMap(Map<IsAVLAbTestResult, Collection<IsAVLAbTestResult>> labMap, boolean simple) {
        for (IsAVLAbTestResult parent : labMap.keySet()) {
            if (simple) {
                if (labMap.get(parent).size() == 0) {
                    if (parent.getResults() == null) {
                        logger.debug("LOOK: " + parent);
                    }
                    logger.debug(parent.getLaboratoryTest().getName() + " "
                            + parent.getResults() + " "
                            + parent.getReferenceLow() + ": "
                            + parent.getReferenceHigh() + " "
                            + parent.getAbnormal());
                } else {
                    logger.debug(parent.getLaboratoryTest().getName());
                }
            } else {
                logger.debug("PARENT: " + parent);
            }
            for (IsAVLAbTestResult child : labMap.get(parent)) {
                if (simple) {
                    logger.debug("\t" + child.getLaboratoryTest().getName()
                            + " " + child.getResults() + " "
                            + child.getReferenceLow() + ": "
                            + child.getReferenceHigh() + " "
                            + child.getAbnormal());
                } else {
                    logger.debug("\t\tCHILD: " + child);
                }
            }
        }
    }

    @Deprecated
    private Collection<FMLaboratoryTest> findLabTest() throws OvidDomainException {
        Collection<FMLaboratoryTest> laboratoryTests = new ArrayList<FMLaboratoryTest>();
        try {

            ResAdapter adapter = obtainServerRPCAdapter();

            FMQueryList query = new FMQueryList(adapter, FMLaboratoryTest.getFileInfoForClass());

            query.getField("LAB COLLECTION SAMPLE").setInternal(false);
            query.getField("PROCEDURE (SNOMED)").setInternal(false);
            query.getField("HIGHEST URGENCY ALLOWED").setInternal(false);
            query.getField("FORCED URGENCY").setInternal(false);
            query.getField("NATIONAL VA LAB CODE").setInternal(false);
            query.getField("RESULT NLT CODE").setInternal(false);
            query.getField("EDIT CODE").setInternal(false);
            query.getField("EXECUTE ON DATA REVIEW").setInternal(false);
            query.getField("REQUIRED COMMENT").setInternal(false);
            query.getField("DATA NAME").setInternal(false);

            // these fields are not available in all file 60 implementations...
            query.addField("9002096.01", FIELDTYPE.POINTER_TO_FILE); // loinc
            // pointer
            // (MFI)
            // to
            // 9002096.21
            query.addField("9002096.02", FIELDTYPE.FREE_TEXT); // loinc name mfi
            query.getField("9002096.01").setInternal(false);

            query.setScreen(new FMScreenEquals(new FMScreenField(".01"),
                    new FMScreenValue("%")));

            FMResultSet results = query.execute();
            if (results != null) {
                if (results.getError() != null) {
                    throw new OvidDomainException(results.getError());
                }
                while (results.next()) {
                    FMLaboratoryTest laboratoryTest = new FMLaboratoryTest(
                            results);
                    String loincCode = (results.getValue("9002096.01") != null) ? results.getValue("9002096.01")
                            : results.getValue("9002096.02");
                    if (loincCode != null) {
                        logger.debug("got loinc");
                        laboratoryTest.setLoincCode(loincCode);
                    }
                    laboratoryTests.add(laboratoryTest);
                    LaboratoryTestCache.getInstance().addToCache(
                            laboratoryTest.getIEN(), laboratoryTest);
                }
            }
        } catch (ResException e) {
            throw new OvidDomainException(e);
        }

        return laboratoryTests;

    }

    public static void main(String[] args) {
        //BasicConfigurator.configure();
        //Logger.getRootLogger().setLevel(Level.ERROR);
        RPCConnection userConn = null;
        RPCConnection serverConn = null;

        try {

            if (args == null || args.length == 0) {
                args = new String[]{"localhost", "9201", "PU1234",
                            "PU1234!!", "OV1234", "OV1234!!"};
            }

            if (args.length < 5) {
                System.err.println("usage: VLabRepository <host> <port> <user-access-code> <user-verify-code> <ovid-access-code> <ovid-verify-code>");
                return;
            }

            userConn = new RPCBrokerConnection(args[0], new Integer(args[1]),
                    args[2], args[3]);
            serverConn = new CIABrokerConnection("cbvmkmrdev02.medsphere.com",
                    9200, "OV1234", "OV1234!!", null, "KMREHR");// new
            // RPCBrokerConnection(args[0],
            // new
            // Integer(args[1]),
            // args[4],
            // args[5]);
            // serverConn = new
            // CIABrokerConnection("cbvmqadevtest01.medsphere.com", 9210,
            // "FS12345", "FS12345!!", null, "EHRD");//new
            // RPCBrokerConnection(args[0], new Integer(args[1]), args[4],
            // args[5]);
            if (serverConn == null || userConn == null) {
                return;
            }

            VLabRepository repo = new VLabRepository(serverConn);
            repo.getCPTCodeForPointer("488");
            int start, end;
            start = end = 14484; // 41519; // 14484;
            for (Integer i = start; i <= end; i++) {
                System.out.println("*** dfn: " + i);
                Collection<IsAVLAbTestResult> labs = repo.getVLabByDFN(
                        i.toString(), false);
                repo.resolveLaboratoryTest(labs);
                for (IsAVLAbTestResult lab : labs) {
                    System.out.println(lab);
                }
                repo.printLabPanelMap(repo.createLabPanelMap(labs), true);
            }

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
