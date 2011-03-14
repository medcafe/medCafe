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
package com.medsphere.ovid.domain.lab;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.medsphere.common.cache.GenericCacheException;
import com.medsphere.fileman.FMFile;
import com.medsphere.fileman.FMQueryByIENS;
import com.medsphere.fileman.FMQueryList;
import com.medsphere.fileman.FMRecord;
import com.medsphere.fileman.FMResultSet;
import com.medsphere.fileman.FMRetrieve;
import com.medsphere.fileman.FMField.FIELDTYPE;
import com.medsphere.fmdomain.FMLaboratoryTest;
import com.medsphere.ovid.domain.ov.OvidDomainException;
import com.medsphere.ovid.domain.ov.OvidSecureRepository;
import com.medsphere.ovid.model.LaboratoryTestCache;
import com.medsphere.resource.ResAdapter;
import com.medsphere.resource.ResException;
import com.medsphere.vistarpc.RPCConnection;

public abstract class LabRepository extends OvidSecureRepository {

	public abstract Collection<IsAnOVLabResult> getResults(String patientDfn, Date startDate, Date stopDate) throws OvidDomainException;
	public abstract Collection<IsAnOVLabResult> getResults(String patientDfn, Date startDate, Date stopDate, boolean resolveLabTests) throws OvidDomainException;
	
	public LabRepository(RPCConnection connection) {
		super(connection);
	}

	public LabRepository(RPCConnection connection,
			RPCConnection serverConnection) {
		super(connection, serverConnection);
	}

	/**
	 * Get Laboratory Test info from file 60
	 * @param ien
	 * @return
	 * @throws OvidDomainException
	 */
	public FMLaboratoryTest getLaboratoryTest(String ien) throws OvidDomainException {
	
	    Collection<String> iens = new ArrayList<String>();
	    iens.add(ien);
	    Collection<FMLaboratoryTest> laboratoryTests = getLaboratoryTest(iens);
	    FMLaboratoryTest laboratoryTest = null;
	    if (laboratoryTests.size() > 0) {
	        laboratoryTest = (FMLaboratoryTest)laboratoryTests.toArray()[0];
	    }
	
	    return laboratoryTest;
	}

	/**
	 * Get Laboratory Test info from file 60
	 * @param iens
	 * @return
	 * @throws OvidDomainException
	 */
	public Collection<FMLaboratoryTest> getLaboratoryTest(Collection<String> iens) throws OvidDomainException {
	    Collection<FMLaboratoryTest> laboratoryTests = new ArrayList<FMLaboratoryTest>();
	    try {
	
	        ResAdapter adapter = obtainServerRPCAdapter();
	
	        FMQueryByIENS query = new FMQueryByIENS(adapter, FMLaboratoryTest.getFileInfoForClass());
	        for (String ien : iens) {
	            if (LaboratoryTestCache.getInstance().getByKey(ien) != null) {
	                laboratoryTests.add(LaboratoryTestCache.getInstance().getByKey(ien));
	            } else {
	                query.addIEN(ien);
	            }
	        }
	
	        if (query.getIENS() == null || query.getIENS().size() == 0) {
	            return laboratoryTests;
	        }
	        query.getField("LAB COLLECTION SAMPLE").setInternal(true);
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
	        query.addField("9002096.01", FIELDTYPE.POINTER_TO_FILE); // loinc pointer (MFI) to 9002096.21
	        query.addField("9002096.02", FIELDTYPE.FREE_TEXT); // loinc name mfi
	        query.getField("9002096.01").setInternal(false);
	
	        FMResultSet results = query.execute();
	        if (results != null) {
	            if (results.getError() != null) {
	                throw new OvidDomainException(results.getError());
	            }
	            while (results.next()) {
	                FMLaboratoryTest laboratoryTest = new FMLaboratoryTest(results);
	                String loincCode = (results.getValue("9002096.01") != null) ? results.getValue("9002096.01") : results.getValue("9002096.02");
	                if (loincCode != null) {
	                    laboratoryTest.setLoincCode(loincCode);
	                }
	                getSpecimenInfo(laboratoryTest);
	                laboratoryTests.add(laboratoryTest);
	                LaboratoryTestCache.getInstance().addToCache(laboratoryTest.getIEN(), laboratoryTest);
	            }
	        }
	    } catch (ResException e) {
	        throw new OvidDomainException(e);
	    } catch (GenericCacheException e) {
	        throw new OvidDomainException(e);
	    }
	
	    return laboratoryTests;
	}

	private void getSpecimenInfo(FMLaboratoryTest lab) throws OvidDomainException {
	    if (lab == null || lab.getLabCollectionSample() == null) {
	        return;
	    }
	
	    try {
	        ResAdapter adapter = obtainServerRPCAdapter();
	        FMRecord collectionSample = new FMRecord("62"); // file COLLECTION SAMPLE
	        collectionSample.setIEN(lab.getLabCollectionSample());
	        FMRetrieve collectionSampleQuery = new FMRetrieve(adapter);
	        collectionSampleQuery.setRecord(collectionSample);
	        collectionSampleQuery.addField(".01", FIELDTYPE.FREE_TEXT); // name
	        collectionSampleQuery.addField("2", FIELDTYPE.POINTER_TO_FILE); // DEFAULT SPECIMEN, points to file 61
	        collectionSampleQuery.execute();
	
	        String specimenIEN = collectionSample.getValue("2");
	        String collectionSampleName = collectionSample.getValue(".01");
	
	        if (specimenIEN != null) {
	
	            FMRecord topographyField = new FMRecord("61"); // TOPOGRAPHY FIELD
	            topographyField.setIEN(specimenIEN);
	            FMRetrieve topopgraphyFieldQuery = new FMRetrieve(adapter);
	            topopgraphyFieldQuery.setRecord(topographyField);
	            topopgraphyFieldQuery.addField(".01", FIELDTYPE.FREE_TEXT); // NAME
	            topopgraphyFieldQuery.addField(".07", FIELDTYPE.FREE_TEXT); // ICD0 CODE
	            topopgraphyFieldQuery.addField(".08", FIELDTYPE.FREE_TEXT); // HL7 CODE
	            topopgraphyFieldQuery.addField("2", FIELDTYPE.FREE_TEXT); // SNOMED CODE
	            topopgraphyFieldQuery.addField("2.5", FIELDTYPE.SET_OF_CODES); // SEX SPECIFIC
	            topopgraphyFieldQuery.execute();
	            lab.setCollectionSampleName(collectionSampleName);
	            lab.setSpecimenName(topographyField.getValue(".01"));
	            lab.setSpecimenSnomedCode(topographyField.getValue("2"));
	        }
	     } catch (ResException e) {
	        throw new OvidDomainException(e);
	    } finally {
	
	    }
	
	}

    private static Map<String, String> cptCache = new ConcurrentHashMap<String, String>();
    private static final int cptCacheSize = 1000;

    protected String getCPTCodeForPointer(String cptPointer) throws OvidDomainException {
        if (cptCache.containsKey(cptPointer)) {
            return cptCache.get(cptPointer);
        }
        String cptCode = null;
        try {
            ResAdapter adapter = obtainServerRPCAdapter();
            FMRecord ihsLabCPTCode = new FMRecord("IHS LAB CPT CODE"); // file 9009021
            ihsLabCPTCode.setIEN(cptPointer);
            FMRetrieve query = new FMRetrieve(adapter);
            query.setRecord(ihsLabCPTCode);
            query.addField(".01", FIELDTYPE.POINTER_TO_FILE);
            query.execute();

            FMFile secondarySubfile = new FMFile("1101"); // 1101 is the field number for CPT CODE
            secondarySubfile.addField(".01").setInternal(false); // Resolve internal, get the name
            secondarySubfile.setParentRecord(ihsLabCPTCode); // So the query knows which set of subfile records

            FMQueryList secondaryQuery = new FMQueryList(adapter, secondarySubfile);
            FMResultSet results = secondaryQuery.execute();
            if (results != null) {
                if (results.getError() != null) {
                    throw new OvidDomainException(results.getError());
                }
                if (results.next()) {
                    cptCode = results.getValue(".01");
                    if (cptCache.size() > cptCacheSize) {
                        cptCache.remove((String)cptCache.keySet().toArray()[0]);
                    }
                    cptCache.put(cptPointer, cptCode);
                }
            }

            return cptCode;
        } catch (ResException e) {
            throw new OvidDomainException(e);
        } finally {

        }

    }
	
}