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

import com.medsphere.fmdomain.FMPatient_Allergies;
import com.medsphere.fmdomain.FMPatientAllergyReaction;
import com.medsphere.fmdomain.FMPatientAllergyComment;
import com.medsphere.fmdomain.InsertFileManRecordException;
import com.medsphere.fmdomain.InvalidFileManFieldValueException;
import com.medsphere.fmdomain.ModifiedKeyFileManFieldException;

import com.medsphere.ovid.model.domain.AllergyAgent;
import com.medsphere.ovid.model.domain.Allergen;


import com.medsphere.resource.ResAdapter;
import com.medsphere.resource.ResException;
import com.medsphere.vistarpc.RPCBrokerConnection;
import com.medsphere.vistarpc.RPCConnection;
import com.medsphere.vistarpc.RPCException;

public class PatientAllergyRepository extends OvidSecureRepository {

    public static final String ORCONTEXT = "OR CPRS GUI CHART";
    private Logger logger = LoggerFactory.getLogger(PatientAllergyRepository.class);

    public PatientAllergyRepository(RPCConnection connection) {
        super(null, connection);
    }

    public Collection<FMPatient_Allergies> getAllAllergyEntries() throws OvidDomainException{
        Collection<FMPatient_Allergies> allergies = new ArrayList<FMPatient_Allergies>();
        RPCConnection connection = null;



        try {
            TimeKeeperDelegate.start("Getting Connection");
            connection = getServerConnection();
            TimeKeeperDelegate.stopAndLog("Getting Connection");

            TimeKeeperDelegate.start("Getting Allergies");
            ResAdapter adapter = obtainServerRPCAdapter();

            FMQueryList query = new FMQueryList(adapter, FMPatient_Allergies.getFileInfoForClass());


            FMResultSet results = query.execute();
            if (results != null) {
                if (results.getError() != null) {
                    throw new OvidDomainException(results.getError());
                }
                while (results.next()) {
                    allergies.add(new FMPatient_Allergies(results));
                }
            }
        } catch (ResException e) {
            throw new OvidDomainException(e);
        } finally {
            TimeKeeperDelegate.stopAndLog("Getting Allergies");
        }
        resolveReactions(allergies);
        resolveComments(allergies);

        return allergies;


    }
    public Collection<FMPatient_Allergies> getAllergiesForPatientIEN(String patientIEN, boolean internal) throws OvidDomainException {
        Collection<FMPatient_Allergies> allergies = new ArrayList<FMPatient_Allergies>();
        RPCConnection connection = null;



        try {
            TimeKeeperDelegate.start("Getting Connection");
            connection = getServerConnection();
            TimeKeeperDelegate.stopAndLog("Getting Connection");

            TimeKeeperDelegate.start("Getting Allergies");
            ResAdapter adapter = obtainServerRPCAdapter();

            FMQueryList query = new FMQueryList(adapter, FMPatient_Allergies.getFileInfoForClass());

            FMScreen byPatientScreen = new FMScreenEquals(new FMScreenField(".01"), new FMScreenValue(patientIEN));
            query.getField("ORIGINATOR").setInternal(internal);


            query.setScreen(byPatientScreen);
            FMResultSet results = query.execute();
            if (results != null) {
                if (results.getError() != null) {
                    throw new OvidDomainException(results.getError());
                }
                while (results.next()) {
                    allergies.add(new FMPatient_Allergies(results));
                }
            }
        } catch (ResException e) {
            throw new OvidDomainException(e);
        } finally {
            TimeKeeperDelegate.stopAndLog("Getting Allergies");
        }
        resolveReactions(allergies, internal);
        resolveComments(allergies);

        return allergies;


    }

    public Collection<FMPatient_Allergies> getAllergiesByIEN(Collection<String> allergyIENs, boolean internal) throws OvidDomainException {
        Collection<FMPatient_Allergies> allergies = new ArrayList<FMPatient_Allergies>();

        try {
            TimeKeeperDelegate.start("Getting Connection");
            TimeKeeperDelegate.stopAndLog("Getting Connection");

            TimeKeeperDelegate.start("Getting Allergies");
            ResAdapter adapter = obtainServerRPCAdapter();
            FMQueryList query = new FMQueryList(adapter, FMPatient_Allergies.getFileInfoForClass());
                        query.getField("ORIGINATOR").setInternal(internal);
            FMScreen byIENScreen = null;
            Iterator<String> ienIter = allergyIENs.iterator();
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
                    allergies.add(new FMPatient_Allergies(results));
                }
            }
        } catch (ResException e) {
            throw new OvidDomainException(e);
        } finally {
            TimeKeeperDelegate.stopAndLog("Getting Allergies");
        }
        resolveReactions(allergies, internal);
        resolveComments(allergies);
        return allergies;
    }

    /**
     * resolve comments for a single allergy
     * @param allergy
     */
    private void resolveComments(FMPatient_Allergies allergy) {
        Collection<FMPatient_Allergies> allergies = new ArrayList<FMPatient_Allergies>();
        allergies.add(allergy);
        resolveComments(allergies);
    }

    /**
     * resolve the comments for a list of allergies
     * @param allergiess
     */
    private void resolveComments(Collection<FMPatient_Allergies> allergies) {


        if (allergies != null) {
            for (FMPatient_Allergies allergy : allergies) {


                FMPatientAllergyComment commentSubfile = allergy.getCommentReference();




                try {
                    ResAdapter adapter = obtainServerRPCAdapter();
                    FMQueryList query = new FMQueryList(adapter, commentSubfile.getFile());



                    FMResultSet results = query.execute();
                    
                    while (results.next()) {
                        FMPatientAllergyComment entry = new FMPatientAllergyComment(results);
                        FMRetrieve retrieve = new FMRetrieve(adapter);
                        entry.setParent(allergy);
                        retrieve.setRecord(entry);

                        retrieve.execute();
                        allergy.addComments(entry);
                    }

                } catch (OvidDomainException e) {
                    logger.error("Domain exception", e);
                } catch (ResException e) {
                    logger.error("Resource exception", e);

                }
            }

        }
    }    /**
     * resolve reactions for a single allergy
     * @param allergy
     */
    private void resolveReactions(FMPatient_Allergies allergy, boolean internal) {
        Collection<FMPatient_Allergies> allergies = new ArrayList<FMPatient_Allergies>();
        allergies.add(allergy);
        resolveReactions(allergies, internal);
    }
    private void resolveReactions(FMPatient_Allergies allergy)
    {
        resolveReactions(allergy, true);
    }
    /**
     * resolve the reactions for a collection of allergies
     * @param allergies
     */
    private void resolveReactions(Collection<FMPatient_Allergies> allergies)
    {
        resolveReactions(allergies, true);
    }
    private void resolveReactions(Collection<FMPatient_Allergies> allergies, boolean internal) {


        if (allergies != null) {
            for (FMPatient_Allergies allergy : allergies) {
                FMPatientAllergyReaction reactionSubfile = allergy.getReactionReference();




                try {
                    FMQueryList query = new FMQueryList(obtainServerRPCAdapter(), reactionSubfile.getFile());
                    query.getField("REACTION").setInternal(internal);
                    query.getField("ENTERED BY").setInternal(internal);
                    FMResultSet results = query.execute();
                    while (results.next()) {
                        FMPatientAllergyReaction entry = new FMPatientAllergyReaction(results);
                        allergy.addReactions(entry);
                    }

                } catch (OvidDomainException e) {
                    logger.error("Domain exception", e);
                } catch (ResException e) {
                    logger.error("Resource exception", e);

                }
            }

        }
    }
    public void addAllergy(FMPatient_Allergies allergy) throws OvidDomainException, InsertFileManRecordException
    {
        try
        {
        addAllergy(allergy, null, null);
        }
        catch(OvidDomainException ovidE)
        {
            throw ovidE;
        }
        catch(InsertFileManRecordException insertE)
        {
            throw insertE;
        }
    }
    public void setReactant(FMPatient_Allergies allergy, AllergyAgent agent) throws ModifiedKeyFileManFieldException
    {
    	allergy.setAllergy(agent.getLookupFile().getFilenum(), agent.getAllergen().getIen());
    }

    public void addAllergy(FMPatient_Allergies allergy, Date reactDate, Integer severityCode) throws OvidDomainException, InsertFileManRecordException {
        RPCConnection conn;
        try {
            conn = getServerConnection();
            conn.setContext("OR CPRS GUI CHART");



            VistaRPC rpc = new VistaRPC("ORWDAL32 SAVE ALLERGY", ResponseType.SINGLE_VALUE);



            rpc.setParam(1, "");                                   // Supposedly IEN of the entry in PATIENT ALLERGIES file
            rpc.setParam(2, String.valueOf(allergy.getPatient())); // patientID                      // IEN of patient in File #2
            RPCArray list = new RPCArray();
            String fileCode = "";
            if (allergy.getAllergyReactantFileNum().equals("120.82"))
            {
                fileCode="GMRD(120.82,";
            }
            else if (allergy.getAllergyReactantFileNum().equals("50"))
            {
                fileCode="PSDRUG(";
            }
            else if (allergy.getAllergyReactantFileNum().equals("50.6"))
            {
                fileCode="PSNDF(50.6,";
            }
            else if (allergy.getAllergyReactantFileNum().equals("50.416")||allergy.getAllergyReactantFileNum().equals("50.605"))
            {
                fileCode="PS("+allergy.getAllergyReactantFileNum()+",";
            }
            String fileString = allergy.getReactant().trim()+"^"+allergy.getAllergyReactantFileIEN().trim()+ ";" + fileCode;

            list.put("\"GMRAGNT\"", fileString);  // Causative Agent:  PEANUT with IEN of 4048 in file # 50.416
            list.put("\"GMRATYPE\"", allergy.getAllergyType()+"^");                   // Allergy type: Drug
            if (allergy.getMechanism() != null )
            {
            list.put("\"GMRANATR\"", allergy.getMechanism()+ "^");        // Nature of Reaction:  Pharmacological
            }
            else
            {
                list.put("\"GMRANATR\"", FMPatient_Allergies.UNKNOWN_MECHANISM_CODE+ "^");
            }
            list.put("\"GMRAORIG\"", String.valueOf(allergy.getOriginatorIEN()));
            // IEN of originator in the NEW PERSON file
            if (allergy.getOriginationDateTime()!= null)
            {
            list.put("\"GMRAORDT\"", FMUtil.dateToFMDate(allergy.getOriginationDateTime()));             // Origination date in FM date format
            }
            else
            {
                throw new InsertFileManRecordException("Must have an origination date to insert an allergy!");
            }
            List<FMPatientAllergyReaction> reactions = allergy.getNewReactions();
            if (reactions != null && !reactions.isEmpty())
            {
            list.put("\"GMRASYMP\",0", String.valueOf(reactions.size()));                        // Number of reaction entries in REACTIONS subfile
            for (int i=0; i<reactions.size(); i++)
            {

            list.put("\"GMRASYMP\","+String.valueOf(i+1), String.valueOf(reactions.get(i).getReactionIEN())+"^^"+FMUtil.dateToFMDate(reactions.get(i).getDateEntered())+"^^");          // Reaction entry 1, IEN 15 in SIGN/SYMPTOMS file is "CONFUSION"

                }
            }
            if (allergy.getObservedOrHist() == null)
            {
                throw new InsertFileManRecordException("Must be denoted observed or historical allergy");
            }
            list.put("\"GMRAOBHX\"", allergy.getObservedOrHist() + "^");
            // Allergy is o for OBSERVED or h for HISTORICAL
            if (reactDate != null)
            {
            list.put("\"GMRARDT\"", FMUtil.dateToFMDate(reactDate));                 // Reaction Date in FM date format
            }
            if (severityCode != null && (severityCode.intValue()<4 && severityCode.intValue()>0))
            {
            list.put("\"GMRASEVR\"", severityCode.toString());
            }
            // Severity of the reaction is 2 for Moderate (though this doesn't seem to be updating)
            List<FMPatientAllergyComment> comments = allergy.getNewComments();
            if (comments != null && !comments.isEmpty())
            {
            list.put("\"GMRACMTS\",0", String.valueOf(comments.size()));

            // Number of lines of entry in comment subfile
            for (int i=0; i<comments.size(); i++)
            {
            list.put("\"GMRACMTS\","+ String.valueOf(i+1), comments.get(i).getComments());    //  Line 1 of comment
                }
            }
            rpc.setParam(3, list);

            try {
                RPCResponse response = conn.execute(rpc);
                String rpcResults = response.getString();

 
                if (!rpcResults.equals("0")) {
                    throw new InsertFileManRecordException("Error inserting allergy record " + (rpcResults.split("\\^"))[1]);

                }



            } catch (RPCException ex) {
                System.out.println("RPC call failed" + ex.getMessage());
                throw new OvidDomainException(ex);
            }
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (RPCException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }




    }

    private void insertComment(FMPatient_Allergies allergy, FMPatientAllergyComment comment) throws OvidDomainException{
       try {
            ResAdapter adapter = obtainServerRPCAdapter();

            FMInsert insert = new FMInsert(adapter);

           comment.setParent(allergy);
            insert.setEntry(comment);

            FMResultSet results = insert.execute();
            if (results == null || results.getError() != null) {
                logger.error("Error, unable to insert because " + results.getError());
            } else {
               // System.out.println("added as IENS " + comment.getIENS());
                allergy.addComments(comment);
            }
        } catch (OvidDomainException ovidE) {
            throw ovidE;
        } catch (ResException resE) {
            logger.error("Error unable to insert because " + resE.getMessage());
        }
    }

    private void insertReaction(FMPatient_Allergies allergy, FMPatientAllergyReaction reaction) throws OvidDomainException{
    try {
            ResAdapter adapter = obtainServerRPCAdapter();

            FMInsert insert = new FMInsert(adapter);

           reaction.setParent(allergy);
            insert.setEntry(reaction);

            FMResultSet results = insert.execute();
            if (results == null || results.getError() != null) {
                logger.error("Error, unable to insert because " + results.getError());
            } else {
               // System.out.println("added as IENS " + reaction.getIENS());
                allergy.addReactions(reaction);

            }

        } catch (OvidDomainException ovidE) {
            throw ovidE;
        } catch (ResException resE) {
            logger.error("Error unable to insert because " + resE.getMessage());
        }
    }

    private void updateComment(FMPatient_Allergies allergy, FMPatientAllergyComment comment) throws OvidDomainException {
          try {
            ResAdapter adapter = obtainServerRPCAdapter();

            FMUpdate update = new FMUpdate(adapter);

            Set<String> modFieldSet = comment.getModifiedFields();
            if (modFieldSet.size() > 0) {
                update.setEntry(comment);

                FMResultSet results = update.execute();
                if (results == null || results.getError() != null) {
                    logger.error("Error, unable to update because " + results.getError());
                } else {
                   // System.out.println("updated IENS " + comment.getIENS());

                }
            } } catch (OvidDomainException ovidE) {
            throw ovidE;
        } catch (ResException resE) {
            logger.error("Error unable to update because " + resE.getMessage());
        }


    }

    private void updateReaction(FMPatient_Allergies allergy, FMPatientAllergyReaction reaction)
    throws OvidDomainException
    {
         try {
            ResAdapter adapter = obtainServerRPCAdapter();

            FMUpdate update = new FMUpdate(adapter);

            Set<String> modFieldSet = reaction.getModifiedFields();
            if (modFieldSet.size() > 0) {
                update.setEntry(reaction);

                FMResultSet results = update.execute();
                if (results == null || results.getError() != null) {
                    logger.error("Error, unable to update because " + results.getError());
                } else {
                    //System.out.println("updated IENS " + reaction.getIENS());

                }
            } } catch (OvidDomainException ovidE) {
            throw ovidE;
        } catch (ResException resE) {
            logger.error("Error unable to update because " + resE.getMessage());
        }
    }

    public void updateAllergy(FMPatient_Allergies allergy)
            throws OvidDomainException {
        try {
            ResAdapter adapter = obtainServerRPCAdapter();

            FMUpdate update = new FMUpdate(adapter);

            Set<String> modFieldSet = allergy.getModifiedFields();
            if (modFieldSet.size() > 0) {
                update.setEntry(allergy);

                FMResultSet results = update.execute();
                if (results == null || results.getError() != null) {
                    logger.error("Error, unable to update because " + results.getError());
                } else {
                    //System.out.println("updated IEN " + allergy.getIEN());

                }
            }
            HashMap<String, FMPatientAllergyComment> commentMap = allergy.getComments();
            for (String key: commentMap.keySet())
            {
                FMPatientAllergyComment comment = commentMap.get(key);
                
                    updateComment(allergy, comment);
              
            }
             List<FMPatientAllergyComment> commentList = allergy.getNewComments();
            if (commentList!= null)
            {
            for (int i = 0; i<commentList.size(); i++)
            {
                insertComment(allergy, commentList.get(i));
            }
            commentList = null;
            }

            HashMap<String, FMPatientAllergyReaction> reactionMap = allergy.getReactions();
            if (reactionMap !=null)
            {
            for (String key: reactionMap.keySet())
            {
                FMPatientAllergyReaction reaction = reactionMap.get(key);
                updateReaction(allergy, reaction);
                }
            }
            List<FMPatientAllergyReaction> reactionList = allergy.getNewReactions();
            if (reactionList!= null)
            {
            for (int i = 0; i<reactionList.size(); i++)
            {
                insertReaction(allergy, reactionList.get(i));
            }
            reactionList = null;
            }
        } catch (OvidDomainException ovidE) {
            throw ovidE;
        } catch (ResException resE) {
            logger.error("Error unable to update because " + resE.getMessage());
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
            PatientAllergyRepository allergyRepository = new PatientAllergyRepository(conn);
            Collection<FMPatient_Allergies> allergies = allergyRepository.getAllergiesForPatientIEN("10", false);


            for (FMPatient_Allergies allergy : allergies) {
                System.out.println("Allergy: " + allergy.getIEN() + ", " + allergy.getMechanism() + ", "
                        + allergy.getAllergyType() + ", " + allergy.getAllergyValue() + " : " + allergy.getObservedOrHist() + " : " + allergy.getOriginatorName() + "[" + allergy.getPatientName() + "]"
                        + " : " + allergy.getReactant() + ", ");


                if (allergy.getComments() != null) {

                    for (String iens : allergy.getComments().keySet()) {
                        System.out.println(iens + " : " + allergy.getComments().get(iens));


                    }
                }
                if (allergy.getReactions() != null) {
                    for (String iens : allergy.getReactions().keySet()) {
                        System.out.println(iens + " : " + allergy.getReactions().get(iens));


                    }
                }

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
}
