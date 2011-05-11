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

import com.medsphere.fileman.FMQueryList;
import com.medsphere.fileman.FMResultSet;
import com.medsphere.fileman.FMRetrieve;
import com.medsphere.fileman.FMScreen;
import com.medsphere.fileman.FMScreenEquals;
import com.medsphere.fileman.FMScreenField;
import com.medsphere.fileman.FMScreenValue;
import com.medsphere.fmdomain.FMHospitalLocation;
import com.medsphere.fmdomain.FMInstitution;
import com.medsphere.fmdomain.FMWardLocation;
import com.medsphere.ovid.connection.OvidVistaLinkConnectionException;
import com.medsphere.resource.ResAdapter;
import com.medsphere.resource.ResException;
import com.medsphere.vistalink.VistaLinkPooledConnection;
import gov.va.med.vistalink.adapter.cci.VistaLinkConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Handle OV IO for HospitalLocation
 */
public class HospitalLocationRepository extends OvidSecureRepository  {

    private Logger logger = Logger.getLogger(HospitalLocationRepository.class);
    public HospitalLocationRepository(VistaLinkConnection conn) {
        super( conn );
    }

    /**
     * Get a HospitalLocation by IEN
     * @param ien
     * @return
     * @throws com.medsphere.ovid.domain.ov.OvidDomainException
     */
    public FMHospitalLocation getHospitalLocationByIEN(String ien) throws OvidDomainException {
        FMHospitalLocation location = null;
        try {
            ResAdapter adapter = obtainVistaLinkAdapter();
            location = new FMHospitalLocation();
            location.setIEN(ien);
            FMRetrieve query = new FMRetrieve(adapter);
            query.setRecord(location);
            query.setInternal(true);
            query.execute(); 
        } catch (OvidVistaLinkConnectionException e) {
            throw new OvidDomainException(e);
        } catch (ResException e) {
            throw new OvidDomainException(e);
        }
        
        return location;
        
    }

    public FMWardLocation getWardLocationByName(String name) throws OvidDomainException {
        FMWardLocation ward = null;
        try {
            ResAdapter adapter = obtainVistaLinkAdapter();
            FMQueryList query = new FMQueryList(adapter, FMWardLocation.getFileInfoForClass());
            FMScreen byName = new FMScreenEquals(new FMScreenField(".01"), new FMScreenValue(name));
            query.setScreen(byName);
            FMResultSet results = query.execute();
            if (results != null) {
                if (results.getError() != null) {
                    throw new OvidDomainException(results.getError());
                }
                if (results.next()) {
                    ward = new FMWardLocation(results);
                }

            }
        } catch (OvidVistaLinkConnectionException e) {
            throw new OvidDomainException(e);
        } catch (ResException e) {
            throw new OvidDomainException(e);
        }


        return ward;

    }
    /**
     * Get a list of HospitalLocations by TYPE  (e.g. "WARD");
     * @param type
     * @return
     * @throws com.medsphere.ovid.domain.ov.OvidDomainException
     */
    public Collection<FMHospitalLocation> getHospitalLocationsByType(String type) throws OvidDomainException {
        Collection<FMHospitalLocation> locations = new ArrayList<FMHospitalLocation>();
        
        try {
            ResAdapter adapter = obtainVistaLinkAdapter();
            FMQueryList query = new FMQueryList(adapter, FMHospitalLocation.getFileInfoForClass());
            query.getField("TYPE").setInternal(false);
            query.getField("INSTITUTION").setInternal(true);
            FMScreen byType = new FMScreenEquals(new FMScreenField("TYPE"), new FMScreenValue(type));            
            query.setScreen(byType);
            FMResultSet results = query.execute();
            if (results != null) {
                if (results.getError() != null) {
                    throw new OvidDomainException(results.getError());
                }
                while (results.next()) {
                    locations.add(new FMHospitalLocation(results));
                }
            }
        } catch (OvidVistaLinkConnectionException e) {
            throw new OvidDomainException(e);
        } catch (ResException e) {
            throw new OvidDomainException(e);
        }
        
        return locations;
    }
    
    /**
     * find a single Institution via the IEN
     * @param ien
     * @return
     * @throws com.medsphere.ovid.domain.ov.OvidDomainException
     */
    public FMInstitution getInstitutionByIEN(String ien) throws OvidDomainException {
        FMInstitution institution = null;
        try {
            ResAdapter adapter = obtainVistaLinkAdapter();
            institution = new FMInstitution();
            institution.setIEN(ien);
            FMRetrieve query = new FMRetrieve(adapter);     
            query.setRecord(institution);
            query.setInternal(false);
            query.execute(); 
        } catch (OvidVistaLinkConnectionException e) {
            throw new OvidDomainException(e);
        } catch (ResException e) {
            throw new OvidDomainException(e);
        }

        return institution;
    }

    /**
     * Get a single list of institutions who are parents of the list of locations.
     * @param locations
     * @return
     * @throws com.medsphere.ovid.domain.ov.OvidDomainException
     */
    public Collection<FMInstitution> getInstitutions(Collection<FMHospitalLocation> locations) throws OvidDomainException {
        HashMap<Integer, FMInstitution> institutions = new HashMap<Integer, FMInstitution>();            
        
        for (FMHospitalLocation location : locations) {
            Integer instNumber = location.getInstitution();
            if (!institutions.containsKey(instNumber)) {
                FMInstitution inst = getInstitutionByIEN(instNumber.toString());
                if (inst != null) {
                    institutions.put(instNumber, inst);
                }
            }
        }
        return institutions.values();
    }
    
    public static void main(String[] args) throws OvidDomainException {
        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.INFO);
        VistaLinkPooledConnection conn = getDirectConnection("VL1234", "VL1234!!!");
        if (conn==null) {
            return;
        }
        HospitalLocationRepository repo = new HospitalLocationRepository(conn);
        for (Integer i = 1; i < 100; i++) {
            FMHospitalLocation location = repo.getHospitalLocationByIEN(i.toString());
            if (location.getName() == null) {
                break;
            }
            System.out.println("[" + location.getName() + "] [" + location.getAbbreviation() + "] [" + location.getType() + "]");
        }
        
        System.out.println("==== querying by WARD =====");
        Collection<FMHospitalLocation> locations = repo.getHospitalLocationsByType("W");
        for (FMHospitalLocation location : locations) {
            System.out.println("[" + location.getIEN() + "] [" + location.getName() + "] [" + location.getAbbreviation() + "] [" + location.getType() + "] [" + location.getInstitution() + "]");            
        }

        System.out.println("==== getting institution list for wards =====");
        Collection<FMInstitution> institutions = repo.getInstitutions(locations);
        for (FMInstitution inst : institutions) {
            System.out.println("[" + inst.getIEN() + "] [" + inst.getName() + "]");            
        }
        conn.close();
    }
}
