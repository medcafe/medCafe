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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import com.medsphere.fileman.FMFile;
import com.medsphere.fileman.FMQueryByIENS;
import com.medsphere.fileman.FMRecord;
import com.medsphere.fileman.FMResultSet;
import com.medsphere.resource.ResAdapter;
import com.medsphere.resource.ResException;
import com.medsphere.vistarpc.RPCConnection;

public class NameComponentRepository extends OvidSecureRepository {

    public NameComponentRepository(RPCConnection serverConnection) {
        super(null, serverConnection);

    }
    public void fetchNameComponents(SupportsNameComponents entity) throws OvidDomainException {
        Collection<SupportsNameComponents> collection = new ArrayList<SupportsNameComponents>();
        collection.add(entity);
        fetchNameComponents(collection);
    }

    public void fetchNameComponents(Collection<SupportsNameComponents> entities) throws OvidDomainException {
        if (entities.size() > 0) {
            FMFile nameComponent = new FMFile("20");
            nameComponent.addField("1"); // family name
            nameComponent.addField("2"); // given name
            nameComponent.addField("3"); // middle name
            nameComponent.addField("4"); // prefix
            nameComponent.addField("5"); // suffix
            nameComponent.addField("6"); // degree
            nameComponent.addField(".03"); // iens

            try {
                FMQueryByIENS query;
                ResAdapter adapter = obtainServerRPCAdapter();
                query = new FMQueryByIENS( adapter, nameComponent );
                Collection<String> iens = new HashSet<String>();
                for (SupportsNameComponents entity : entities) {
                    iens.add(entity.getIEN());
                }
                query.setIENS(iens);
                FMResultSet results = query.execute();
                while (results.next()) {
                    FMRecord entry = new FMRecord(results);
                    for (SupportsNameComponents entity : entities) {
                        if ((entity.getIEN()+",").equals(entry.getValue(".03"))) {
                            entity.setFamilyName(entry.getValue("1"));
                            entity.setGivenName(entry.getValue("2"));
                            entity.setMiddleName(entry.getValue("3"));
                            entity.setPrefix(entry.getValue("4"));
                            entity.setSuffix(entry.getValue("5"));
                            entity.setDegree(entry.getValue("6"));
                        }
                    }
                }

            } catch (OvidDomainException e) {
                throw e;
            } catch (ResException e) {
                throw new OvidDomainException(e);
            }

        }

    }

}
