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
import java.util.Date;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.medsphere.fileman.FMFile;
import com.medsphere.fileman.FMQueryByIENS;
import com.medsphere.fileman.FMQueryList;
import com.medsphere.fileman.FMRecord;
import com.medsphere.fileman.FMResultSet;
import com.medsphere.fileman.FMScreen;
import com.medsphere.fileman.FMScreenAnd;
import com.medsphere.fileman.FMScreenEquals;
import com.medsphere.fileman.FMScreenField;
import com.medsphere.fileman.FMScreenIEN;
import com.medsphere.fileman.FMScreenValue;
import com.medsphere.fmdomain.FMNewPerson;
import com.medsphere.fmdomain.FMPersonClass;
import com.medsphere.resource.ResAdapter;
import com.medsphere.resource.ResException;
import com.medsphere.vistarpc.RPCBrokerConnection;
import com.medsphere.vistarpc.RPCConnection;
import com.medsphere.vistarpc.RPCException;

public class NewPersonRepository extends OvidSecureRepository {

    private Logger logger = LoggerFactory.getLogger(NewPersonRepository.class);

    public NewPersonRepository(RPCConnection serverConnection) {
        super(null, serverConnection);
    }

    public Collection<FMNewPerson> getNewPersonsByIEN(Collection<String> iens) throws OvidDomainException {
        Collection<FMNewPerson> newPersons = new ArrayList<FMNewPerson>();

        try {
            ResAdapter adapter = obtainServerRPCAdapter();

            FMQueryByIENS query = new FMQueryByIENS(adapter, FMNewPerson.getFileInfoForClass());
            query.getField("LANGUAGE").setInternal(false);
            query.getField("STATE").setInternal(false);
            query.getField("TITLE").setInternal(false);
            query.getField("PROVIDER CLASS").setInternal(false);
            query.setIENS(iens);
            FMResultSet results = query.execute();
            if (results != null) {
                if (results.getError() != null) {
                    throw new OvidDomainException(results.getError());
                }
                while (results.next()) {
                    newPersons.add(new FMNewPerson(results));
                }
            }
        } catch (ResException e) {
            throw new OvidDomainException(e);
        } finally {
        }

        return newPersons;
    }

    public FMNewPerson getNewPersonByIEN(String iens) throws OvidDomainException {
        try {
            ResAdapter adapter = obtainServerRPCAdapter();

            FMQueryByIENS query = new FMQueryByIENS(adapter, FMNewPerson.getFileInfoForClass());
            query.getField("LANGUAGE").setInternal(false);
            query.getField("STATE").setInternal(false);
            query.getField("TITLE").setInternal(false);
            query.getField("PROVIDER CLASS").setInternal(false);
            query.setIENS(new String[] {iens});
            FMResultSet results = query.execute();
            if (results != null) {
                if (results.getError() != null) {
                    throw new OvidDomainException(results.getError());
                }
                while (results.next()) {
                    return new FMNewPerson(results);
                }
            }
        } catch (ResException e) {
            throw new OvidDomainException(e);
        } finally {
        }

        return null;
    }

    /**
     * Get a list of NewPerson entries who have active order writing authorization;
     * @param type
     * @return
     * @throws com.medsphere.ovid.domain.ov.OvidDomainException
     */
    public Collection<FMNewPerson> getOrderAuthors(String partialName) throws OvidDomainException {
        Date now = new Date();
        FMScreen screen;
        FMScreen authorizedOrderer = new FMScreenEquals(new FMScreenField("AUTHORIZED TO WRITE MED ORDERS"), new FMScreenValue("1"));
        if (partialName != null) {
            FMScreen byName = new FMScreenEquals(new FMScreenField("NAME"), new FMScreenValue(partialName));
            screen = new FMScreenAnd(byName, authorizedOrderer);
        } else {
            screen = authorizedOrderer;
        }
        return getScreenedNewPersons(screen);
    }

    
    private Collection<FMNewPerson> getScreenedNewPersons(FMScreen screen) throws OvidDomainException {
        Collection<FMNewPerson> persons = new ArrayList<FMNewPerson>();

        try {
            ResAdapter adapter = obtainServerRPCAdapter();
            FMQueryList query = new FMQueryList(adapter, FMNewPerson.getFileInfoForClass());
            query.setScreen(screen);
            FMResultSet results = query.execute();
            if (results != null) {
                if (results.getError() != null) {
                    throw new OvidDomainException(results.getError());
                }
                while (results.next()) {
                    persons.add(new FMNewPerson(results));
                }
            }
        } catch (ResException e) {
            throw new OvidDomainException(e);
        }

        return persons;
    }
    
    public Collection<FMPersonClass> getPersonClass(FMNewPerson newPerson) {
        Collection<FMPersonClass> personClassList = new ArrayList<FMPersonClass>();
        try {
            ResAdapter adapter = obtainServerRPCAdapter();
            // Now, list the subfiles person class field of new person
            FMFile secondarySubfile = new FMFile("8932.1"); // 8932.1 is the field number from new person for person class
            secondarySubfile.addField(".01").setInternal(true); // we want the number
            secondarySubfile.setParentRecord(newPerson); // So the query knows which set of subfile records
            try {
                FMQueryList secondaryQuery = new FMQueryList( adapter, secondarySubfile );
                FMResultSet results = secondaryQuery.execute();

                while (results.next()) {
                    FMRecord entry = new FMRecord(results);
                    FMPersonClass personClass = getPersonClassByPersonClassNumber(entry.getValue(".01"));
                    if (personClass != null) {
                        personClassList.add(personClass);
                    }

                }
            } catch (ResException e) {
                e.printStackTrace();
                throw e;
            }

        } catch (ResException e) {
            logger.error("Resource exeception", e);
        } catch (OvidDomainException e) {
            logger.error("Domain exception", e);
        } finally {

        }

        return personClassList;
    }

    private FMPersonClass getPersonClassByPersonClassNumber(String ien) throws OvidDomainException {
        try {
            ResAdapter adapter = obtainServerRPCAdapter();

            FMQueryList query = new FMQueryList(adapter, FMPersonClass.getFileInfoForClass());
            FMScreen byIENScreen = new FMScreenEquals(new FMScreenIEN(), new FMScreenValue(ien));

            query.setScreen(byIENScreen);
            FMResultSet results = query.execute();
            if (results != null) {
                if (results.getError() != null) {
                    throw new OvidDomainException(results.getError());
                }
                if (results.next()) {
                    return new FMPersonClass(results);
                }
            }
        } catch (ResException e) {
            throw new OvidDomainException(e);
        } finally {
        }
        return null;

    }

    public static void main(String[] args) throws OvidDomainException {
        //BasicConfigurator.configure();
        //Logger.getRootLogger().setLevel(Level.INFO);
        RPCConnection conn = null;
        args = new String[] { "localhost", "9201", "OV1234", "OV1234!!"};
        if (args.length < 4) {
            System.err.println("usage: PatientRepository <host> <port> <ovid-access-code> <ovid-verify-code>");
            return;
        }

        try {
            conn = new RPCBrokerConnection(args[0], new Integer(args[1]), args[2], args[3]);
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (RPCException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (conn==null) {
            return;
        }
        NewPersonRepository repo = new NewPersonRepository(conn);
        Collection<String> samples = new ArrayList<String>();
        for (Integer i = 1; i < 100; i++) {
            samples.add(i.toString());
        }
        Collection<FMNewPerson> samplePersons = repo.getNewPersonsByIEN(samples);
        for (Iterator<FMNewPerson> personIter = samplePersons.iterator() ; personIter.hasNext() ; ) {
            FMNewPerson person = personIter.next();
            System.out.println("[" + person.getName() + "] [" + person.getInitial() + "] [" + person.getAuthOrderWriter() + "]");
        }


        System.out.println("==== querying by Order Authors =====");
        Collection<FMNewPerson> authors = repo.getOrderAuthors("D");
        for (Iterator<FMNewPerson> personIter = authors.iterator() ; personIter.hasNext() ; ) {
            FMNewPerson person = personIter.next();
            System.out.println("[" + person.getName() + "] [" + person.getInitial() + "] [" + person.getAuthOrderWriter() + "]");
        }
    }
}
