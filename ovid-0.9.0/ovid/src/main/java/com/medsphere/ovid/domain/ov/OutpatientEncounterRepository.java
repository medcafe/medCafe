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
import com.medsphere.fileman.FMScreen;
import com.medsphere.fileman.FMScreenEquals;
import com.medsphere.fileman.FMScreenField;
import com.medsphere.fileman.FMScreenValue;
import com.medsphere.fmdomain.FMOutpatientEncounter;
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
 * Handle OV IO for PatientEncounter
 */

public class OutpatientEncounterRepository extends OvidSecureRepository {
    public OutpatientEncounterRepository(VistaLinkConnection conn) {
        super( conn );
    }

    public Collection<FMOutpatientEncounter> getOutpatientEncounterByPatientDFN(String patientDFN) throws OvidDomainException {
        Collection<FMOutpatientEncounter> encounters = new ArrayList<FMOutpatientEncounter>();

        try {
            ResAdapter adapter = obtainVistaLinkAdapter();
            
            FMQueryList query = new FMQueryList(adapter, FMOutpatientEncounter.getFileInfoForClass());
            FMScreen byDFN = new FMScreenEquals(new FMScreenField("PATIENT"), new FMScreenValue(patientDFN));
            query.getField("LOCATION").setInternal(false);
            query.setScreen(byDFN);
            FMResultSet results = query.execute();
            if (results != null) {
                if (results.getError() != null) {
                    throw new OvidVistaLinkConnectionException(results.getError());
                }
                while (results.next()) {
                    encounters.add(new FMOutpatientEncounter(results));
                }
            }
        } catch (OvidVistaLinkConnectionException e) {
            throw new OvidDomainException(e);
        } catch (ResException e) {
            throw new OvidDomainException(e);
        }
        return encounters;
    }

    public static void main(String[] args) throws OvidDomainException {
        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.INFO);
        VistaLinkPooledConnection conn = getDirectConnection("VL1234", "VL1234!!!");
        if (conn==null) {
            return;
        }
        OutpatientEncounterRepository repo = new OutpatientEncounterRepository(conn);
        int lineNo = 0;
        for (FMOutpatientEncounter enc : repo.getOutpatientEncounterByPatientDFN("2")) {
            System.out.print(++lineNo);
            System.out.println("  [" + enc.getDateTimeCreated() + "] [" + enc.getValue(".04") + "]");
        }
        conn.close();
    }
}
