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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.medsphere.common.cache.GenericCacheException;

import com.medsphere.common.util.TimeKeeperDelegate;

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

import com.medsphere.fileman.FMScreenEquals;
import com.medsphere.fileman.FMScreenField;

import com.medsphere.fileman.FMScreenValue;

import com.medsphere.fmdomain.FMDrug;
import com.medsphere.fmdomain.FMDrugSynonym;
import com.medsphere.fmdomain.FMGMRAllergies;
import com.medsphere.fmdomain.FMVAGeneric;
import com.medsphere.fmdomain.FMAllergyDrugClass;
import com.medsphere.fmdomain.FMIngredientsDrugIdentifier;
import com.medsphere.fmdomain.FMDrugIngredients;
import com.medsphere.fmdomain.FMAllergySynonym;
import com.medsphere.fmdomain.FMAllergyDrugIngredient;
import com.medsphere.fmdomain.FMVADrugClass;


import com.medsphere.ovid.model.domain.AllergyAgent;
import com.medsphere.ovid.model.domain.Allergen;
import com.medsphere.ovid.model.domain.FileManLookupFileInfo;

import com.medsphere.resource.ResAdapter;
import com.medsphere.resource.ResException;
import com.medsphere.vistarpc.RPCBrokerConnection;
import com.medsphere.vistarpc.RPCConnection;
import com.medsphere.vistarpc.RPCException;

public class AllergyAgentRepository extends OvidSecureRepository {
    public static final String GMR_ALLERGIES = "GMR ALLERGIES\t120.82\tGMRD(120.82,";
    public static final String DRUG = "DRUG\t50\tPSDRUG(";
    public static final String NATIONAL_DRUG = "VA GENERIC\t50.6\tPSNDF(50.6,";
    public static final String DRUG_INGREDIENTS = "DRUG INGREDIENTS\t50.416\tPS(50.416,";
    public static final String DRUG_CLASS = "VA DRUG CLASS\t50.605\tPS(50.605,";
    public static final String ORCONTEXT = "OR CPRS GUI CHART";
    private Logger logger = LoggerFactory.getLogger(PatientAllergyRepository.class);

    public AllergyAgentRepository(RPCConnection connection) {
        super(null, connection);
    }
    public TreeSet<AllergyAgent> getAllAllergyAgents() throws OvidDomainException
    {
        TreeSet<AllergyAgent>  agents = new TreeSet<AllergyAgent>();

        agents.addAll(getAllGMRAllergyAgents());
        agents.addAll(getAllVAGenericAgents());

        agents.addAll(getAllDrugAllergyAgents());
        agents.addAll(getAllDrugIngredientAgents());
        agents.addAll(getAllDrugClassAgents());
        return agents;
    }
    public Collection<AllergyAgent> getAllGMRAllergyAgents() throws OvidDomainException{
        
       Collection<FMGMRAllergies> allergies = getAllGMRAllergies(true);
        FileManLookupFileInfo gmrFile = new FileManLookupFileInfo(GMR_ALLERGIES);
        
        Collection<AllergyAgent> agentList = new ArrayList<AllergyAgent>();
        for(FMGMRAllergies allergy : allergies)
        {
            Allergen allergen = new Allergen(allergy.getIEN(), allergy.getName(), allergy.getAllergyType());
            AllergyAgent agent = new AllergyAgent(gmrFile, allergen, createDisplayName(allergy.getName()), allergy);
            agentList.add(agent);
            if (allergy.getSynonyms() != null)
            {
                for (FMAllergySynonym syn : allergy.getSynonyms())
                {
                    agent = new AllergyAgent(gmrFile, allergen, createDisplayName(syn.getSynonym()), allergy);
                    agentList.add(agent);
                }
            }
        }

        return agentList;


    }
    public Collection<FMGMRAllergies> getAllGMRAllergies(boolean synonyms) throws OvidDomainException
    {
        return getAllGMRAllergies(synonyms, false, false, false, false);
    }
    public Collection<FMGMRAllergies> getAllGMRAllergies(boolean synonyms, boolean ingredients, boolean ingredIntern, boolean classes,
            boolean classesIntern) throws OvidDomainException
    {
        Collection<FMGMRAllergies> allergies = new ArrayList<FMGMRAllergies>();
        RPCConnection connection = null;

        try {
            TimeKeeperDelegate.start("Getting Connection");
            connection = getServerConnection();
            TimeKeeperDelegate.stopAndLog("Getting Connection");

            TimeKeeperDelegate.start("Getting GMR Allergies");
            ResAdapter adapter = obtainServerRPCAdapter();

            FMQueryList query = new FMQueryList(adapter, FMGMRAllergies.getFileInfoForClass());


            FMResultSet results = query.execute();
            if (results != null) {
                if (results.getError() != null) {
                    throw new OvidDomainException(results.getError());
                }
                while (results.next()) {
                    allergies.add(new FMGMRAllergies(results));
                }
            }
        } catch (ResException e) {
            throw new OvidDomainException(e);
        } finally {
            TimeKeeperDelegate.stopAndLog("Getting Allergies");
        }
        if (synonyms)
        {
            resolveSynonyms(allergies);
        }
        if (ingredients)
        {
            resolveAllergyDrugIngredients(allergies, ingredIntern);
        }
        if (classes)
        {
            resolveAllergyDrugClasses(allergies, classesIntern);
        }
        return allergies;
    }
        public Collection<AllergyAgent> getAllVAGenericAgents() throws OvidDomainException{

       Collection<FMVAGeneric> allergies = getAllVAGeneric();
        FileManLookupFileInfo natDrugFile = new FileManLookupFileInfo(NATIONAL_DRUG);

        Collection<AllergyAgent> agentList = new ArrayList<AllergyAgent>();
        for(FMVAGeneric allergy : allergies)
        {
            Allergen allergen = new Allergen(allergy.getIEN(), allergy.getName(), "D");
            AllergyAgent agent = new AllergyAgent(natDrugFile, allergen, createDisplayName(allergy.getName()), allergy);
            agentList.add(agent);

        }

        return agentList;


    }

    public Collection<FMVAGeneric> getAllVAGeneric() throws OvidDomainException
    {
        Collection<FMVAGeneric> allergies = new ArrayList<FMVAGeneric>();


        try {
            TimeKeeperDelegate.start("Getting Connection");

            TimeKeeperDelegate.stopAndLog("Getting Connection");

            TimeKeeperDelegate.start("Getting VA Generic");
            ResAdapter adapter = obtainServerRPCAdapter();

            FMQueryList query = new FMQueryList(adapter, FMVAGeneric.getFileInfoForClass());


            FMResultSet results = query.execute();
            if (results != null) {
                if (results.getError() != null) {
                    throw new OvidDomainException(results.getError());
                }
                while (results.next()) {
                    allergies.add(new FMVAGeneric(results));
                }
            }
        } catch (ResException e) {
            throw new OvidDomainException(e);
        } finally {
            TimeKeeperDelegate.stopAndLog("Getting VA Generic");
        }

        return allergies;
    }
    public Collection<AllergyAgent> getAllDrugAllergyAgents() throws OvidDomainException
    {
        FileManLookupFileInfo drugFile = new FileManLookupFileInfo(DRUG);
        Collection<AllergyAgent> agentList = new ArrayList<AllergyAgent>();
        DrugRepository drugRepo = new DrugRepository(getServerConnection());
        Collection<FMDrug> drugList = drugRepo.getActiveDrugList(true);
       for (FMDrug drug: drugList)
       {

            Allergen allergen = new Allergen(drug.getIEN(), drug.getGenericName(), "D");
            AllergyAgent agent;
            boolean inserted = false;
            if (drug.getSynonyms() != null)
            {
                for (FMDrugSynonym syn : drug.getSynonyms())
                {
                    char test = syn.getSynonym().toLowerCase().charAt(0);
                    if (test>='a' && test<='z')
                    {
                    agent = new AllergyAgent(drugFile, allergen, createDisplayName(syn.getSynonym()), drug);
                    agentList.add(agent);
                    inserted = true;
                    }
                }
           }
                if (inserted)
                {

            agent = new AllergyAgent(drugFile, allergen, createDisplayName(drug.getGenericName()), drug);
            agentList.add(agent);
                }
            
       }
       return agentList;
    }
     public Collection<AllergyAgent> getAllDrugIngredientAgents() throws OvidDomainException{

       Collection<FMDrugIngredients> allergies = getAllDrugIngredients(false);
        FileManLookupFileInfo ingredFile = new FileManLookupFileInfo(DRUG_INGREDIENTS);

        Collection<AllergyAgent> agentList = new ArrayList<AllergyAgent>();
        for(FMDrugIngredients allergy : allergies)
        {
            Allergen allergen = new Allergen(allergy.getIEN(), allergy.getName(), "D");
            AllergyAgent agent = new AllergyAgent(ingredFile, allergen, createDisplayName(allergy.getName()), allergy);
            agentList.add(agent);

        }

        return agentList;


    }

    public Collection<FMDrugIngredients> getAllDrugIngredients(boolean getIdentifiers) throws OvidDomainException
    {
        Collection<FMDrugIngredients> allergies = new ArrayList<FMDrugIngredients>();


        try {


            TimeKeeperDelegate.start("Getting GMR Allergies");
            ResAdapter adapter = obtainServerRPCAdapter();

            FMQueryList query = new FMQueryList(adapter, FMDrugIngredients.getFileInfoForClass());


            FMResultSet results = query.execute();
            if (results != null) {
                if (results.getError() != null) {
                    throw new OvidDomainException(results.getError());
                }
                while (results.next()) {
                    allergies.add(new FMDrugIngredients(results));
                }
            }
        } catch (ResException e) {
            throw new OvidDomainException(e);
        } finally {
            TimeKeeperDelegate.stopAndLog("Getting Allergies");
        }
        if (getIdentifiers)
        {
            resolveIdentifiers(allergies);
        }

        return allergies;
    }
    public Collection<AllergyAgent> getAllDrugClassAgents() throws OvidDomainException{

       Collection<FMVADrugClass> allergies = getAllDrugClasses();
        FileManLookupFileInfo classFile = new FileManLookupFileInfo(DRUG_CLASS);

        Collection<AllergyAgent> agentList = new ArrayList<AllergyAgent>();
        for(FMVADrugClass allergy : allergies)
        {
            Allergen allergen = new Allergen(allergy.getIEN(), allergy.getClassification(), "D");
            AllergyAgent agent = new AllergyAgent(classFile, allergen, createDisplayName(allergy.getClassification()), allergy);
            agentList.add(agent);

        }

        return agentList;


    }

    public Collection<FMVADrugClass> getAllDrugClasses() throws OvidDomainException
    {
        Collection<FMVADrugClass> allergies = new ArrayList<FMVADrugClass>();


        try {


            TimeKeeperDelegate.start("Getting Drug Classes");
            ResAdapter adapter = obtainServerRPCAdapter();

            FMQueryList query = new FMQueryList(adapter, FMVADrugClass.getFileInfoForClass());


            FMResultSet results = query.execute();
            if (results != null) {
                if (results.getError() != null) {
                    throw new OvidDomainException(results.getError());
                }
                while (results.next()) {
                    allergies.add(new FMVADrugClass(results));
                }
            }
        } catch (ResException e) {
            throw new OvidDomainException(e);
        } finally {
            TimeKeeperDelegate.stopAndLog("Getting Drug Classes");
        }


        return allergies;
    }
     /**
     * resolve reactions for a single allergy
     * @param allergy
     */
    private void resolveSynonyms(FMGMRAllergies allergy) {
        Collection<FMGMRAllergies> allergies = new ArrayList<FMGMRAllergies>();
        allergies.add(allergy);
        resolveSynonyms(allergies);
    }

    private void resolveSynonyms(Collection<FMGMRAllergies> allergies) {


        if (allergies != null) {
            for (FMGMRAllergies allergy : allergies) {
                FMAllergySynonym synonymSubfile = allergy.getSynonym();




                try {
                    FMQueryList query = new FMQueryList(obtainServerRPCAdapter(), synonymSubfile.getFile());

                    FMResultSet results = query.execute();
                    while (results.next()) {
                        FMAllergySynonym entry = new FMAllergySynonym(results);
                        allergy.addSynonym(entry);
                    }

                } catch (OvidDomainException e) {
                    logger.error("Domain exception", e);
                } catch (ResException e) {
                    logger.error("Resource exception", e);

                }
            }

        }
    }
      private void resolveIdentifiers(FMDrugIngredients allergy) {
        Collection<FMDrugIngredients> allergies = new ArrayList<FMDrugIngredients>();
        allergies.add(allergy);
        resolveIdentifiers(allergies);
    }

    private void resolveIdentifiers(Collection<FMDrugIngredients> allergies) {


        if (allergies != null) {
            for (FMDrugIngredients allergy : allergies) {
                FMIngredientsDrugIdentifier identSubfile = allergy.getDrugIdentifier();




                try {
                    FMQueryList query = new FMQueryList(obtainServerRPCAdapter(), identSubfile.getFile());

                    FMResultSet results = query.execute();
                    while (results.next()) {
                        FMIngredientsDrugIdentifier entry = new FMIngredientsDrugIdentifier(results);
                        allergy.addIdentifier(entry);
                    }

                } catch (OvidDomainException e) {
                    logger.error("Domain exception", e);
                } catch (ResException e) {
                    logger.error("Resource exception", e);

                }
            }

        }
    }
     private void resolveAllergyDrugIngredients(FMGMRAllergies allergy, boolean internal) {
        Collection<FMGMRAllergies> allergies = new ArrayList<FMGMRAllergies>();
        allergies.add(allergy);
        resolveAllergyDrugIngredients(allergies, internal);
    }

    private void resolveAllergyDrugIngredients(Collection<FMGMRAllergies> allergies, boolean internal) {


        if (allergies != null) {
            for (FMGMRAllergies allergy : allergies) {
                FMAllergyDrugIngredient ingredientSubfile = allergy.getDrugIngredient();




                try {
                    FMQueryList query = new FMQueryList(obtainServerRPCAdapter(), ingredientSubfile.getFile());
                    query.getField("DRUG INGREDIENT").setInternal(internal);
                    FMResultSet results = query.execute();
                    while (results.next()) {
                        FMAllergyDrugIngredient entry = new FMAllergyDrugIngredient(results);
                        allergy.addDrugIngredient(entry);
                    }

                } catch (OvidDomainException e) {
                    logger.error("Domain exception", e);
                } catch (ResException e) {
                    logger.error("Resource exception", e);

                }
            }

        }
    }
    private void resolveAllergyDrugClasses(FMGMRAllergies allergy, boolean internal) {
        Collection<FMGMRAllergies> allergies = new ArrayList<FMGMRAllergies>();
        allergies.add(allergy);
        resolveAllergyDrugClasses(allergies, internal);
    }

    private void resolveAllergyDrugClasses(Collection<FMGMRAllergies> allergies, boolean internal) {


        if (allergies != null) {
            for (FMGMRAllergies allergy : allergies) {
                FMAllergyDrugClass classSubfile = allergy.getDrugClass();




                try {
                    FMQueryList query = new FMQueryList(obtainServerRPCAdapter(), classSubfile.getFile());
                    query.getField("VA DRUG CLASSES").setInternal(internal);
                    FMResultSet results = query.execute();
                    while (results.next()) {
                        FMAllergyDrugClass entry = new FMAllergyDrugClass(results);
                        allergy.addDrugClass(entry);
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
            AllergyAgentRepository allergyRepository = new AllergyAgentRepository(conn);
            Collection<AllergyAgent> allergies = allergyRepository.getAllAllergyAgents();


            for (AllergyAgent allergy : allergies) {
                System.out.println("Allergy: " + allergy.toString());


                }

            

            System.out.println("got " + allergies.size());



        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();


        } catch (RPCException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
    }
    public String createDisplayName(String displayName)
    {
        String[] parts = displayName.split("[ .]\\d{1,}[ A-Za-z]");
    /*    if (!parts[0].equals(displayName) && parts[0].length()<8)
        {
            System.out.println(displayName + ": "+ parts[0]);
        }*/
        return parts[0];
    }
}

