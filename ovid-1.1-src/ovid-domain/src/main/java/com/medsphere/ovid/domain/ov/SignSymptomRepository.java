/*
 *  Copyright 2011 The MITRE Corporation (http://www.mitre.org/). All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.medsphere.ovid.domain.ov;

import java.util.ArrayList;
import java.util.List;
import java.util.Collection;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Set;
import java.util.Date;
import java.util.TreeSet;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.medsphere.common.cache.GenericCacheException;

import com.medsphere.common.util.TimeKeeperDelegate;
import com.medsphere.fileman.FMField.FIELDTYPE;

import com.medsphere.fileman.FMInsert;
import com.medsphere.fileman.FMQueryList;
import com.medsphere.fileman.FMScreenOr;
import com.medsphere.fileman.FMUtil;
import com.medsphere.fileman.FMScreenIEN;
import com.medsphere.fileman.FMUpdate;
import com.medsphere.fileman.FMResultSet;
import com.medsphere.fileman.FMRetrieve;
import com.medsphere.fileman.FMScreen;
import com.medsphere.vistarpc.RPCArray;
import com.medsphere.vistarpc.VistaRPC;
import com.medsphere.vistarpc.RPCResponse;
import com.medsphere.vistarpc.RPCResponse.ResponseType;
import com.medsphere.fileman.FMRecord;
import com.medsphere.fileman.FMScreenEquals;
import com.medsphere.fileman.FMScreenField;

import com.medsphere.fileman.FMScreenValue;

import com.medsphere.fmdomain.FMSignSymptom;

import com.medsphere.resource.ResAdapter;
import com.medsphere.resource.ResException;
import com.medsphere.vistarpc.RPCBrokerConnection;
import com.medsphere.vistarpc.RPCConnection;
import com.medsphere.vistarpc.RPCException;

public class SignSymptomRepository extends OvidSecureRepository {

    public static final String ORCONTEXT = "OR CPRS GUI CHART";
    private Logger logger = LoggerFactory.getLogger(PatientAllergyRepository.class);

    public SignSymptomRepository(RPCConnection connection) {
        super(null, connection);
    }

    public TreeSet<FMSignSymptom> getAllSignsSymptoms(boolean withSynonyms) throws OvidDomainException{
        TreeSet<FMSignSymptom> symptoms = new TreeSet<FMSignSymptom>();
        RPCConnection connection = null;



        try {
            TimeKeeperDelegate.start("Getting Connection");
            connection = getServerConnection();
            TimeKeeperDelegate.stopAndLog("Getting Connection");

            TimeKeeperDelegate.start("Getting Signs and Symptoms");
            ResAdapter adapter = obtainServerRPCAdapter();

            FMQueryList query = new FMQueryList(adapter, FMSignSymptom.getFileInfoForClass());


            FMResultSet results = query.execute();
            if (results != null) {
                if (results.getError() != null) {
                    throw new OvidDomainException(results.getError());
                }
                while (results.next()) {
                    FMSignSymptom symptom = new FMSignSymptom(results);
                    symptoms.add(symptom);
                }
            }
        } catch (ResException e) {
            throw new OvidDomainException(e);
        } finally {
            TimeKeeperDelegate.stopAndLog("Getting Allergies");
        }
        if (withSynonyms)
        {
            resolveSynonyms(symptoms);
        }
        return symptoms;


    }
    public Collection<FMSignSymptom> getSymptomByIEN(String symptomIEN, boolean withSynonyms) throws OvidDomainException
    {
        Collection<String> iens = new ArrayList<String>();
        iens.add(symptomIEN);
        return getSymptomsByIEN(iens, withSynonyms);
    }

    public Collection<FMSignSymptom> getSymptomsByIEN(Collection<String> symptomIENs, boolean withSynonyms) throws OvidDomainException {
        Collection<FMSignSymptom> symptoms = new ArrayList<FMSignSymptom>();

        try {
            TimeKeeperDelegate.start("Getting Connection");
            TimeKeeperDelegate.stopAndLog("Getting Connection");

            TimeKeeperDelegate.start("Getting Symptoms");
            ResAdapter adapter = obtainServerRPCAdapter();
            FMQueryList query = new FMQueryList(adapter, FMSignSymptom.getFileInfoForClass());
            FMScreen byIENScreen = null;
            Iterator<String> ienIter = symptomIENs.iterator();
            while (ienIter.hasNext()) {
                FMScreen newIENScreen = new FMScreenEquals(new FMScreenIEN(), new FMScreenValue(ienIter.next()));
                if (byIENScreen == null) {
                    byIENScreen = newIENScreen;
                } else {

                    byIENScreen = new FMScreenOr(byIENScreen, newIENScreen);
                }
            }



            query.setScreen(byIENScreen);
            FMResultSet results = query.execute();
            if (results != null) {
                if (results.getError() != null) {
                    throw new OvidDomainException(results.getError());
                }
                while (results.next()) {
                    symptoms.add(new FMSignSymptom(results));
                }
            }
        } catch (ResException e) {
            throw new OvidDomainException(e);
        } finally {
            TimeKeeperDelegate.stopAndLog("Getting Symptoms");
        }
        if (withSynonyms)
        {
            resolveSynonyms(symptoms);
        }
        return symptoms;
    }


        /**
     * resolve synonyms for a single symptom
     * @param symptom
     */
    private void resolveSynonyms(FMSignSymptom symptom) {
        Collection<FMSignSymptom> symptoms = new ArrayList<FMSignSymptom>();
        symptoms.add(symptom);
        resolveSynonyms(symptoms);
    }

    /**
     * resolve the synonyms for a collection of symptoms
     * @param symptoms
     */

    private void resolveSynonyms(Collection<FMSignSymptom> symptoms) {


        if (symptoms != null) {
            for (FMSignSymptom symptom : symptoms) {
                FMRecord synonymSubfile = symptom.getSynonymReference();





                try {
                    FMQueryList query = new FMQueryList(obtainServerRPCAdapter(), synonymSubfile.getFile());
                    query.addField("SYNONYM", FIELDTYPE.FREE_TEXT);
                    FMResultSet results = query.execute();
                    while (results.next()) {
                        FMRecord entry = new FMRecord(results);
                        symptom.addSynonym(entry.getValue("SYNONYM"));
       
                    }

                } catch (OvidDomainException e) {
                    logger.error("Domain exception", e);
                } catch (ResException e) {
                    logger.error("Resource exception", e);

                }
            }

        }
    }
   
    public static void main(String[] args) throws OvidDomainException, GenericCacheException {
        RPCConnection conn = null;


        try {
            //BasicConfigurator.configure();
            //Logger.getRootLogger().setLevel(Level.INFO);

            args = new String[]{"medcafe.mitre.org", "9201", "SM1234", "SM1234!!"};


            if (args.length < 4) {
                System.err.println("usage: PatientRepository <host> <port> <ovid-access-code> <ovid-verify-code>");


                return;


            }

            conn = new RPCBrokerConnection(args[0], new Integer(args[1]), args[2], args[3]);
            //new OrderRepository(conn).testGetBigListsOfThings(conn);




            if (conn == null) {
                return;


            }
            SignSymptomRepository symptomRepository = new SignSymptomRepository(conn);
            Collection<FMSignSymptom> symptoms = symptomRepository.getAllSignsSymptoms(true);


            for (FMSignSymptom symptom : symptoms) {
                System.out.println(symptom);


               

            }

            System.out.println("got " + symptoms.size());



        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();


        } catch (RPCException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
    }
}

