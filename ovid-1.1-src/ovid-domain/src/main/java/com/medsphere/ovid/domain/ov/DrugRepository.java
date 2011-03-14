// <editor-fold defaultstate="collapsed">
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
// </editor-fold>


/*
 * Repository class for obtaining drug information.
 */
package com.medsphere.ovid.domain.ov;

import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.medsphere.common.cache.GenericCacheException;
import com.medsphere.common.util.TimeKeeperDelegate;
import com.medsphere.fileman.FMQueryByIENS;
import com.medsphere.fileman.FMQueryList;
import com.medsphere.fileman.FMResultSet;
import com.medsphere.fileman.FMScreenIsNull;
import com.medsphere.fileman.FMUtil;
import com.medsphere.fmdomain.FMDrug;
import com.medsphere.fmdomain.FMDrugSynonym;
import com.medsphere.ovid.model.DrugCache;
import com.medsphere.resource.ResAdapter;
import com.medsphere.resource.ResException;
import com.medsphere.rpcresadapter.RPCResAdapter;
import com.medsphere.vistarpc.RPCBrokerConnection;
import com.medsphere.vistarpc.RPCConnection;
import com.medsphere.vistarpc.RPCException;

/**
 * Get Drug info from file 50
 *
 */
public class DrugRepository extends OvidSecureRepository {
    private Logger logger = LoggerFactory.getLogger(DrugRepository.class);

    private boolean useCache = false;

    public DrugRepository(RPCConnection connection) {
        this(connection, false);
    }

    public DrugRepository(RPCConnection connection, boolean useCache) {
        super(null, connection);
        this.useCache = useCache;
    }

    /**
     * Get all drugs with a blank inactive date.
     * @return
     * @throws OvidDomainException
     */
    public Collection<FMDrug> getActiveDrugList() throws OvidDomainException {
        Collection<FMDrug> list = new ArrayList<FMDrug>();

        try {
            TimeKeeperDelegate.start("Getting Connection");
            RPCConnection connection = getServerConnection();
            try {
                connection.setContext(FMUtil.FM_RPC_CONTEXT);
            } catch (RPCException ex) {
                throw new OvidDomainException(ex);
            }
            TimeKeeperDelegate.stopAndLog("Getting Connection");

            TimeKeeperDelegate.start("getting ActiveDrugList");
            ResAdapter adapter =
                    new RPCResAdapter(connection,
                    FMUtil.FM_RPC_NAME);
            FMQueryList query = new FMQueryList(adapter, FMDrug.getFileInfoForClass());
            // modify query
            query.setScreen(new FMScreenIsNull("100", true));
            query.removeField("100"); // remove inactive date from query results, since we're screening it

            FMResultSet results = query.execute();
            if (results != null) {
                if (results.getError() != null) {
                    throw new OvidDomainException(results.getError());
                }
                while (results.next()) {
                    list.add(new FMDrug(results));
                }
            }
        } catch (ResException e) {
            throw new OvidDomainException(e);
        } finally {
            TimeKeeperDelegate.stopAndLog("getting ActiveDrugList");
        }

        return list;
    }

    /**
     * return info for a single drug
     * @param drugIen
     * @return
     */
    public FMDrug getDrugWithSynonym(String drugIen) {
        Collection<String> iens = new ArrayList<String>();
        iens.add(drugIen);
        Collection<FMDrug> drugs = getDrugsWithSynonyms(iens);
        if (drugs != null && drugs.size() > 0) {
            return (FMDrug) drugs.toArray()[0];
        } else {
            return null;
        }
    }
    /**
     * resolve Synonyms for a list of drugs.
     * @param drugIens
     * @return
     */
    public Collection<FMDrug> getDrugsWithSynonyms(Collection<String> drugIens) {
        Collection<FMDrug> drugs = new ArrayList<FMDrug>();
        Collection<String> iens = new ArrayList<String>(drugIens); // make a copy that we can manipulate

        if (useCache) {
            for (String ien : drugIens) {
                try {
                    FMDrug d = DrugCache.getInstance().getByKey(ien);
                    if (d != null) {
                        drugs.add(d);
                        iens.remove(ien);
                    }
                } catch (GenericCacheException ex) {

                }

            }
        }
        // iens might be empty if all were resolved through the cache
        if (iens.size() > 0) {
            try {
                FMQueryByIENS query = new FMQueryByIENS(obtainServerRPCAdapter(), FMDrug.getFileInfoForClass());
                query.getField("UNIT").setInternal(false);
                query.setIENS(iens);

                FMResultSet results = query.execute();
                if (results != null) {
                    if (results.getError() != null) {
                        throw new OvidDomainException(results.getError());
                    }
                    while (results.next()) {
                        FMDrug d = new FMDrug(results);
                        getSynonymForDrug(d);
                        drugs.add(d);
                        if (useCache) {
                            DrugCache.getInstance().addToCache(d.getIEN(), d);
                        }
                    }
                }

            } catch (ResException ex) {
                logger.error("Resource exception", ex);
            } catch (OvidDomainException ex) {
                logger.error("Domain exception", ex);
            }
        }

        return drugs;
    }

    private void getSynonymForDrug(FMDrug drug) {

        FMDrugSynonym synonymField  = drug.getSynonym();
        try {
            FMQueryList query = new FMQueryList(obtainServerRPCAdapter(), synonymField.getFile());
            FMResultSet results = query.execute();
            if (results.getError() != null) {
                throw new OvidDomainException(results.getError());
            }
            while (results.next()) {
                drug.addSynonym(new FMDrugSynonym(results));
            }
        } catch (OvidDomainException e) {
            logger.error("Domain exception", e);
        } catch (ResException e) {
            logger.error("Resource exception", e);
        }

    }

    public static void main(String[] args) throws OvidDomainException {
        //BasicConfigurator.configure();
        RPCConnection conn = null;
        try {
            args = new String[] { "localhost", "9201", "OV1234", "OV1234!!"};
            if (args.length < 4) {
                System.err.println("usage: DrugRepository <host> <port> <ovid-access-code> <ovid-verify-code>");
                return;
            }

            conn = new RPCBrokerConnection(args[0], new Integer(args[1]), args[2], args[3]);

            if (conn==null) {
                return;
            }
            Collection<FMDrug> list = new DrugRepository(conn).getActiveDrugList();
            for (FMDrug drug : list) {
                System.out.println("Drug: " + drug.getIEN() + ", " + drug.getGenericName() + ", " + drug.getNdc()); // + "," + drug.getSynonym().getSynonym());
            }
            System.out.println("got " + list.size() + " drugs.");
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (RPCException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }
}
