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

import com.medsphere.fileman.FMQueryFind;
import com.medsphere.fileman.FMResultSet;
import com.medsphere.fileman.FMScreen;
import com.medsphere.fileman.FMScreenEquals;
import com.medsphere.fileman.FMScreenField;
import com.medsphere.fileman.FMScreenValue;
import com.medsphere.fmdomain.FMPatientMovement;
import com.medsphere.ovid.connection.OvidVistaLinkConnectionException;
import com.medsphere.resource.ResAdapter;
import com.medsphere.resource.ResException;
import com.medsphere.vistalink.VistaLinkPooledConnection;
import gov.va.med.vistalink.adapter.cci.VistaLinkConnection;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;


/**
 * Handle OV IO for PatientMovement
 */

public class PatientMovementRepository extends OvidSecureRepository {
    public PatientMovementRepository(VistaLinkConnection conn) {
        super( conn );
    }

    public Collection<FMPatientMovement> getPatientMovementByPatientDFN(String patientDFN) throws OvidDomainException {
        Collection<FMPatientMovement> movements = new ArrayList<FMPatientMovement>();

        try {
            ResAdapter adapter = obtainVistaLinkAdapter();
            
            FMQueryFind query = new FMQueryFind(adapter, FMPatientMovement.getFileInfoForClass());
            query.setIndex("C", patientDFN); 
            FMScreen byDFN = new FMScreenEquals(new FMScreenField("PATIENT"), new FMScreenValue(patientDFN));
            query.getField("WARD LOCATION").setInternal(false);
            query.setScreen(byDFN);
            FMResultSet results = query.execute();
            if (results != null) {
                if (results.getError() != null) {
                    throw new OvidDomainException(results.getError());
                }
                while (results.next()) {
                    movements.add(new FMPatientMovement(results));
                }
            }
        } catch (OvidVistaLinkConnectionException e) {
            throw new OvidDomainException(e);
        } catch (ResException e) {
            throw new OvidDomainException(e);
        }
        
        return movements;
    }

    public static void main(String[] args) throws OvidDomainException {
        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.INFO);
        VistaLinkPooledConnection conn = getDirectConnection("VL1234", "VL1234!!!");
        if (conn==null) {
            return;
        }
        PatientMovementRepository repo = new PatientMovementRepository(conn);
        int lineNo = 0;
        for (FMPatientMovement pm : repo.getPatientMovementByPatientDFN("2")) {
            System.out.print(++lineNo);
            System.out.println("  [" + pm.getDateTime() + "] [" + pm.getValue(".06") + "] [" + pm.getTransaction() + "]");
        }
        conn.close();
    }
}
