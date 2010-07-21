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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Arrays;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.medsphere.fileman.FMField;
import com.medsphere.fileman.FMFile;
import com.medsphere.fileman.FMQueryFind;
import com.medsphere.fileman.FMQueryList;
import com.medsphere.fileman.FMRecord;
import com.medsphere.fileman.FMResultSet;
import com.medsphere.fileman.FMScreen;
import com.medsphere.fileman.FMScreenEquals;
import com.medsphere.fileman.FMScreenField;
import com.medsphere.fileman.FMScreenOr;
import com.medsphere.fileman.FMScreenValue;

import com.medsphere.fmdomain.FMVisit;
import com.medsphere.fmdomain.FMV_POV;
import com.medsphere.fmdomain.FMV_Provider;
import com.medsphere.fmdomain.FMV_Exam;
import com.medsphere.fmdomain.FMV_Immunization;
import com.medsphere.fmdomain.FMV_HealthFactors;
import com.medsphere.fmdomain.FMV_CPT;
import com.medsphere.fmdomain.FMV_SkinTest;
import com.medsphere.fmdomain.FMV_PatientEd;
import com.medsphere.fmdomain.FMV_Treatment;
import com.medsphere.fmdomain.FMICD_Diagnosis;
import com.medsphere.fmdomain.FMCPT;
import com.medsphere.resource.ResAdapter;
import com.medsphere.resource.ResException;

import com.medsphere.vistarpc.RPCConnection;


import com.medsphere.ovid.model.domain.patient.PatientVisit;

public class PatientVisitRepository extends OvidSecureRepository {

    private Logger logger = Logger.getLogger(PatientRepository.class);

    public PatientVisitRepository(RPCConnection serverConnection) {
        super(null, serverConnection);
    }

    public PatientVisitRepository(RPCConnection connection, RPCConnection serverCconnection) {
        super(connection, serverCconnection);
    }



    public void getDetail(HashMap<String, PatientVisit> visitMap, Collection<String> iens) throws OvidDomainException {

        Collection<String> diagCodes = new HashSet<String>();
        ResAdapter adapter;
        Collection<FMRecord> needDiagnosisRecord = new ArrayList<FMRecord>();
        FMV_POV pov = new FMV_POV();
        FMScreen byVisitIEN = null;

        for (String visitIEN : iens) {
            if (byVisitIEN == null) {
                byVisitIEN = new FMScreenEquals(new FMScreenField("VISIT"), new FMScreenValue(visitIEN));
            } else {
                byVisitIEN = new FMScreenOr(byVisitIEN, new FMScreenEquals(new FMScreenField("VISIT"), new FMScreenValue(visitIEN)));
            }
        }
        RPCConnection connection = null;
        try {
            connection = getServerConnection();
            adapter = obtainServerRPCAdapter();
            FMQueryList query = new FMQueryList(adapter, FMV_POV.getFileInfoForClass());
            query.setScreen(byVisitIEN);
            query.getField("POV").setInternal(false);
            query.getField("PATIENT NAME").setInternal(false);
            query.getField("PROVIDER NARRATIVE").setInternal(false);
            query.getField("CLINICAL TERM").setInternal(false);
            query.getField("PROBLEM LIST ENTRY").setInternal(false);
            query.getField("ORDERING PROVIDER").setInternal(false);
            query.getField("ENCOUNTER PROVIDER").setInternal(false);
            FMResultSet results = query.execute();
            if (results != null) {
                if (results.getError() != null) {
                    throw new OvidDomainException(results.getError());
                }
                while (results.next()) {
                    pov = new FMV_POV(results);
                    PatientVisit vis = visitMap.get(pov.getVisitIEN().toString());
                    diagCodes.add(pov.getPovValue());
                    needDiagnosisRecord.add(pov);
                    vis.getPOVs().add(pov);
                }
            }
        } catch (ResException e) {
            throw new OvidDomainException(e);
        }

        FMV_Provider provider = new FMV_Provider();

        try {

            FMQueryList query = new FMQueryList(adapter, FMV_Provider.getFileInfoForClass());
            query.setScreen(byVisitIEN);
            query.getField("PROVIDER").setInternal(false);
            query.getField("PATIENT NAME").setInternal(false);
            query.getField("PERSON CLASS").setInternal(false);
            FMResultSet results = query.execute();
            if (results != null) {
                if (results.getError() != null) {
                    throw new OvidDomainException(results.getError());
                }
                while (results.next()) {
                    provider = new FMV_Provider(results);
                    PatientVisit vis = visitMap.get(provider.getVisitIEN().toString());
                    vis.getProviders().add(provider);
                }
            }
        } catch (ResException e) {
            throw new OvidDomainException(e);
        }
        FMV_Exam exam = new FMV_Exam();

        try {

            FMQueryList query = new FMQueryList(adapter, FMV_Exam.getFileInfoForClass());
            query.setScreen(byVisitIEN);
            query.getField("EXAM").setInternal(false);
            query.getField("PATIENT NAME").setInternal(false);
            query.getField("ORDERING PROVIDER").setInternal(false);
            query.getField("ENCOUNTER PROVIDER").setInternal(false);
            FMResultSet results = query.execute();
            if (results != null) {
                if (results.getError() != null) {
                    throw new OvidDomainException(results.getError());
                }
                while (results.next()) {
                    exam = new FMV_Exam(results);
                    PatientVisit vis = visitMap.get(exam.getVisitIEN().toString());
                    vis.getExams().add(exam);
                }
            }
        } catch (ResException e) {
            throw new OvidDomainException(e);
        }
         FMV_Immunization immune = new FMV_Immunization();

        try {

            FMQueryList query = new FMQueryList(adapter, FMV_Immunization.getFileInfoForClass());
            query.setScreen(byVisitIEN);
            query.getField("IMMUNIZATION").setInternal(false);
            query.getField("PATIENT NAME").setInternal(false);
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
                    immune = new FMV_Immunization(results);
                    PatientVisit vis = visitMap.get(immune.getVisitIEN().toString());
                    vis.getImmunizations().add(immune);
                    Collection<String> diagKeyList = immune.getDiagnosesKeyList();
                    if (diagKeyList.size()!=0){
                        diagCodes.addAll(diagKeyList);
                        needDiagnosisRecord.add(immune);
                    }
                }
            }
        } catch (ResException e) {
            throw new OvidDomainException(e);
        }
        FMV_HealthFactors factor = new FMV_HealthFactors();

        try {

            FMQueryList query = new FMQueryList(adapter, FMV_HealthFactors.getFileInfoForClass());
            query.setScreen(byVisitIEN);
            query.getField("HEALTH FACTOR").setInternal(false);
            query.getField("PATIENT NAME").setInternal(false);
            query.getField("ORDERING PROVIDER").setInternal(false);
            query.getField("ENCOUNTER PROVIDER").setInternal(false);
            FMResultSet results = query.execute();
            if (results != null) {
                if (results.getError() != null) {
                    throw new OvidDomainException(results.getError());
                }
                while (results.next()) {
                    factor = new FMV_HealthFactors(results);
                    PatientVisit vis = visitMap.get(factor.getVisitIEN().toString());
                    vis.getHealthFactors().add(factor);
                }
            }
        } catch (ResException e) {
            throw new OvidDomainException(e);
        }
        FMV_CPT cpt = new FMV_CPT();

        try {

            FMQueryList query = new FMQueryList(adapter, FMV_CPT.getFileInfoForClass());
            query.setScreen(byVisitIEN);
            query.getField("CPT").setInternal(false);
            query.getField("PATIENT NAME").setInternal(false);
            query.getField("PROVIDER NARRATIVE").setInternal(false);
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
                    cpt = new FMV_CPT(results);
                    fillCPTRecord(cpt);
                    PatientVisit vis = visitMap.get(cpt.getVisitIEN().toString());
                    vis.getCurrentProcedureCodes().add(cpt);
                    Collection<String> diagKeyList = cpt.getDiagnosesKeyList();
                    if (diagKeyList.size()!=0){
                        diagCodes.addAll(diagKeyList);
                        needDiagnosisRecord.add(cpt);
                    }
                }
            }
        } catch (ResException e) {
            throw new OvidDomainException(e);
        }
        FMV_PatientEd patientEd = new FMV_PatientEd();

        try {

            FMQueryList query = new FMQueryList(adapter, FMV_PatientEd.getFileInfoForClass());
            query.setScreen(byVisitIEN);
            query.getField("TOPIC").setInternal(false);
            query.getField("PATIENT NAME").setInternal(false);
            query.getField("ORDERING PROVIDER").setInternal(false);
            query.getField("ENCOUNTER PROVIDER").setInternal(false);
            FMResultSet results = query.execute();
            if (results != null) {
                if (results.getError() != null) {
                    throw new OvidDomainException(results.getError());
                }
                while (results.next()) {
                    patientEd = new FMV_PatientEd(results);
                    PatientVisit vis = visitMap.get(patientEd.getVisitIEN().toString());
                    vis.getPatientEd().add(patientEd);
                }
            }
        } catch (ResException e) {
            throw new OvidDomainException(e);
        }
        FMV_SkinTest skinTest = new FMV_SkinTest();

        try {

            FMQueryList query = new FMQueryList(adapter, FMV_SkinTest.getFileInfoForClass());
            query.setScreen(byVisitIEN);
            query.getField("SKIN TEST").setInternal(false);
            query.getField("PATIENT NAME").setInternal(false);
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
                    skinTest = new FMV_SkinTest(results);
                    PatientVisit vis = visitMap.get(skinTest.getVisitIEN().toString());
                    vis.getSkinTests().add(skinTest);
                    Collection<String> diagKeyList = skinTest.getDiagnosesKeyList();
                    if (diagKeyList.size()!=0){
                        diagCodes.addAll(diagKeyList);
                        needDiagnosisRecord.add(skinTest);
                    }
                }
            }
        } catch (ResException e) {
            throw new OvidDomainException(e);
        }
        FMV_Treatment treatment = new FMV_Treatment();

        try {

            FMQueryList query = new FMQueryList(adapter, FMV_Treatment.getFileInfoForClass());
            query.setScreen(byVisitIEN);
            query.getField("TREATMENT").setInternal(false);
            query.getField("PATIENT NAME").setInternal(false);
            query.getField("PROVIDER NARRATIVE").setInternal(false);
            query.getField("ORDERING PROVIDER").setInternal(false);
            query.getField("ENCOUNTER PROVIDER").setInternal(false);
            FMResultSet results = query.execute();
            if (results != null) {
                if (results.getError() != null) {
                    throw new OvidDomainException(results.getError());
                }
                while (results.next()) {
                    treatment = new FMV_Treatment(results);
                    PatientVisit vis = visitMap.get(treatment.getVisitIEN().toString());
                    vis.getTreatments().add(treatment);
                }
            }
        } catch (ResException e) {
            throw new OvidDomainException(e);
        }
        HashMap<String, FMICD_Diagnosis> diagMap = new HashMap<String, FMICD_Diagnosis>();



        FMScreen byDiagnosisCode = null;

        for (String diagCode : diagCodes) {
            if (byDiagnosisCode == null) {
                byDiagnosisCode = new FMScreenEquals(new FMScreenField("CODE NUMBER"), new FMScreenValue(diagCode));
            } else {
                byDiagnosisCode = new FMScreenOr(byDiagnosisCode, new FMScreenEquals(new FMScreenField("CODE NUMBER"), new FMScreenValue(diagCode)));
            }
        }

        try {
            connection = getServerConnection();
            adapter = obtainServerRPCAdapter();
            FMQueryList query = new FMQueryList(adapter, FMICD_Diagnosis.getFileInfoForClass());
            query.setScreen(byDiagnosisCode);

            query.getField("MAJOR DIAGNOSTIC CATEGORY").setInternal(false);
            FMResultSet results = query.execute();
            if (results != null) {
                if (results.getError() != null) {
                    throw new OvidDomainException(results.getError());
                }
                while (results.next()) {
                    FMICD_Diagnosis fmDiag = new FMICD_Diagnosis(results);
                    diagMap.put(fmDiag.getIcd9(), fmDiag);
                }
            }
        } catch (ResException e) {
            throw new OvidDomainException(e);
        }
        if (diagMap.size()>0)
        {
            for (FMRecord record : needDiagnosisRecord)
            {
                if (record instanceof FMV_POV){
                    pov = (FMV_POV) record;
                    pov.setDiagnosis(diagMap.get(pov.getPovValue()));
                }
                else
                {
                    Collection<String>codes = null;
                    ArrayList<FMICD_Diagnosis> recordDiagnosis = new ArrayList<FMICD_Diagnosis>();
                    if (record instanceof FMV_Immunization)
                    {
                        FMV_Immunization multiRecord = (FMV_Immunization) record;
                        codes = multiRecord.getDiagnosesKeyList();
                        multiRecord.setDiagnoses(recordDiagnosis);

                    }
                    else
                        if (record instanceof FMV_CPT)
                    {
                        FMV_CPT multiRecord = (FMV_CPT) record;
                        codes = multiRecord.getDiagnosesKeyList();
                        multiRecord.setDiagnoses(recordDiagnosis);
                    }
                        else
                        {
                            FMV_SkinTest multiRecord = (FMV_SkinTest) record;
                            codes = multiRecord.getDiagnosesKeyList();
                            multiRecord.setDiagnoses(recordDiagnosis);
                        }
                    for (String code : codes)
                        recordDiagnosis.add(diagMap.get(code));

                }
            }
        }

    }


    public Collection<PatientVisit> getVisitsByPatientDFN(String patientDfn) throws OvidDomainException {
        HashMap<String, PatientVisit> visitMap = new HashMap<String, PatientVisit>();
        ArrayList<String> visitIens = new ArrayList<String>();
        Collection<PatientVisit> patVisit = new ArrayList<PatientVisit>();
        try {
            ResAdapter adapter = obtainServerRPCAdapter();

            FMQueryFind query = new FMQueryFind(adapter, FMVisit.getFileInfoForClass());
            query.setScreen(new FMScreenEquals(new FMScreenField("PATIENT NAME"), new FMScreenValue(patientDfn)));
            query.getField("PARENT VISIT LINK").setInternal(false);
            query.getField("LOC. OF ENCOUNTER").setInternal(false);
            query.getField("HOSPITAL LOCATION").setInternal(false);

            FMResultSet results = query.execute();
            if (results != null) {
                if (results.getError() != null) {
                    throw new OvidDomainException(results.getError());
                }
                while (results.next()) {
                    FMVisit fmVisit = new FMVisit(results);
                    visitMap.put(fmVisit.getIEN(), new PatientVisit(fmVisit));
                    visitIens.add(fmVisit.getIEN());
                }
            }
        } catch (ResException e) {
            throw new OvidDomainException(e);
        }
        getDetail(visitMap, visitIens);

        patVisit.addAll(visitMap.values());

        return patVisit;

    }
    public void fillCPTRecord(FMV_CPT cpt) throws OvidDomainException
    {
         FMScreen byCPTCode = new FMScreenEquals( new FMScreenField("CPT CODE"), new FMScreenValue(cpt.getCptValue()));



        try {
            RPCConnection connection = getServerConnection();
            ResAdapter  adapter = obtainServerRPCAdapter();
            FMQueryList query = new FMQueryList(adapter, FMCPT.getFileInfoForClass());
            query.setScreen(byCPTCode);

            query.getField("CPT CATEGORY").setInternal(false);
            FMResultSet results = query.execute();
            if (results != null) {
                if (results.getError() != null) {
                    throw new OvidDomainException(results.getError());
                }

                FMCPT cptRecord = new FMCPT(results);
                cpt.setCPTRecord(cptRecord);
            }
        } catch (ResException e) {
            throw new OvidDomainException(e);
        }
         catch (OvidDomainException e){
             throw e;
         }
    }
     
}
