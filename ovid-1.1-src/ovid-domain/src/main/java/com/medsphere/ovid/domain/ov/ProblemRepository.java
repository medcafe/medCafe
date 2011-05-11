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
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;


import com.medsphere.fileman.FMQueryByIENS;
import com.medsphere.fileman.FMQueryFind;
import com.medsphere.fileman.FMResultSet;
import com.medsphere.fmdomain.FMDiagnosisICD;
import com.medsphere.fmdomain.FMProblem;
import com.medsphere.resource.ResAdapter;
import com.medsphere.resource.ResException;
import com.medsphere.vistarpc.RPCBrokerConnection;
import com.medsphere.vistarpc.RPCConnection;
import com.medsphere.vistarpc.RPCException;

public class ProblemRepository extends OvidSecureRepository {

    public ProblemRepository(RPCConnection serverConnection) {
        super(null, serverConnection);
    }

    public Collection<FMProblem> getActiveProblemsForDFN(String dfn) throws OvidDomainException {

        Calendar cal = GregorianCalendar.getInstance();
        cal.set(1950, Calendar.JANUARY, 01);
        Date beginDate = cal.getTime();

       return getActiveProblemsForDFN(dfn, beginDate, new Date());
    }

    public Collection<FMProblem> getActiveProblemsForDFN(String dfn, Date beginDate, Date endDate) throws OvidDomainException {
        Collection<FMProblem> problems = new ArrayList<FMProblem>();

        for (FMProblem problem : getProblemsForDFN(dfn, false)) {
             if ("ACTIVE".equalsIgnoreCase(problem.getStatus())) {
                 Date cmprDate = null;
                 if (problem.getDateOfOnset() != null) {
                     cmprDate = problem.getDateOfOnset();
                 } else if (problem.getDateRecorded() != null) {
                     cmprDate = problem.getDateRecorded();
                 }
                 if (cmprDate != null) {
                     if (cmprDate.equals(beginDate) || cmprDate.after(beginDate)) {
                         if (cmprDate.equals(endDate) || cmprDate.before(endDate)) {
                             problems.add(problem);
                         }
                     }
                 } else {
                     problems.add(problem);
                 }
             }
        }
        getDiagnosisInfo(problems);
        return problems;
    }

    public Collection<FMProblem> getProblemsForDFN(String dfn, Date beginDate, Date endDate) throws OvidDomainException {
        Collection<FMProblem> problems = new ArrayList<FMProblem>();

        for (FMProblem problem : getProblemsForDFN(dfn, false)) {
             Date cmprDate = null;
             if (problem.getDateOfOnset() != null) {
                 cmprDate = problem.getDateOfOnset();
             } else if (problem.getDateRecorded() != null) {
                 cmprDate = problem.getDateRecorded();
             }
             if (cmprDate != null) {
                 if (cmprDate.equals(beginDate) || cmprDate.after(beginDate)) {
                     if (cmprDate.equals(endDate) || cmprDate.before(endDate)) {
                         problems.add(problem);
                     }
                 }
             } else {
                 problems.add(problem);
             }
         }

        getDiagnosisInfo(problems);
        return problems;

    }

    public Collection<FMProblem> getProblemsForDFN(String dfn, boolean internal) throws OvidDomainException {
        Collection<FMProblem> problems = new ArrayList<FMProblem>();
        try {

            ResAdapter adapter = obtainServerRPCAdapter();

            FMQueryFind query = new FMQueryFind(adapter, FMProblem.getFileInfoForClass());
            query.setIndex("AC", dfn);

            query.getField("DIAGNOSIS").setInternal(true);
            query.getField("PATIENT NAME").setInternal(internal);
            query.getField("PROVIDER NARRATIVE").setInternal(internal);
            query.getField("FACILITY").setInternal(internal);
            query.getField("USER LAST MODIFIED").setInternal(internal);
            query.getField("PROBLEM").setInternal(internal);
            query.getField("ENTERED BY").setInternal(internal);
            query.getField("RESPONSIBLE PROVIDER").setInternal(internal);
            query.getField("SERVICE").setInternal(internal);
            query.getField("CLINIC").setInternal(internal);
            query.getField("RECORDING PROVIDER").setInternal(internal);
            FMResultSet results = query.execute();
            if (results != null) {
                if (results.getError() != null) {
                    throw new OvidDomainException(results.getError());
                }
                while (results.next()) {
                    problems.add(new FMProblem(results));
                }
            }
        } catch (ResException e) {
            throw new OvidDomainException(e);
        }


        return problems;
    }
    public Collection<FMProblem> getProblemsForIENS(Collection<String> iens) throws OvidDomainException {
        return getProblemsForIENS(iens, false);
    }

    public Collection<FMProblem> getProblemsForIENS(Collection<String> iens, boolean internal) throws OvidDomainException {
        Collection<FMProblem> problems = new ArrayList<FMProblem>();

        try {

            ResAdapter adapter = obtainServerRPCAdapter();

            FMQueryByIENS query = new FMQueryByIENS(adapter, FMProblem.getFileInfoForClass());
            query.setIENS(iens);

            query.getField("DIAGNOSIS").setInternal(true);
            query.getField("PATIENT NAME").setInternal(internal);
            query.getField("PROVIDER NARRATIVE").setInternal(internal);
            query.getField("FACILITY").setInternal(internal);
            query.getField("USER LAST MODIFIED").setInternal(internal);
            query.getField("PROBLEM").setInternal(internal);
            query.getField("ENTERED BY").setInternal(internal);
            query.getField("RESPONSIBLE PROVIDER").setInternal(internal);
            query.getField("SERVICE").setInternal(internal);
            query.getField("CLINIC").setInternal(internal);
            query.getField("RECORDING PROVIDER").setInternal(internal);
            FMResultSet results = query.execute();
            if (results != null) {
                if (results.getError() != null) {
                    throw new OvidDomainException(results.getError());
                }
                while (results.next()) {
                    problems.add(new FMProblem(results));
                }
            }
        } catch (ResException e) {
            throw new OvidDomainException(e);
        }


        return problems;
    }

    public void getDiagnosisInfo(FMProblem problem) throws OvidDomainException {
        Collection<FMProblem> list = new ArrayList<FMProblem>();
        list.add(problem);
        getDiagnosisInfo(list);
    }

    public void getDiagnosisInfo(Collection<FMProblem> problems) throws OvidDomainException {
        try {

            if (problems.size() == 0) {
                return;
            }
            ResAdapter adapter = obtainServerRPCAdapter();

            FMQueryByIENS query = new FMQueryByIENS(adapter, FMDiagnosisICD.getFileInfoForClass());
            Collection<String> uniqueList = new HashSet<String>();
            for (FMProblem problem : problems) {
                if (!uniqueList.contains(problem.getDiagnosis())) {
                    query.addIEN(problem.getDiagnosis());
                    uniqueList.add(problem.getDiagnosis());
                }
            }

            query.getField("MAJOR DIAGNOSTIC CATEGORY").setInternal(false);

            FMResultSet results = query.execute();
            if (results != null) {
                if (results.getError() != null) {
                    throw new OvidDomainException(results.getError());
                }
                while (results.next()) {
                    FMDiagnosisICD diag = new FMDiagnosisICD(results);
                    for (FMProblem problem : problems) {
                        if (problem.getDiagnosis().equals(diag.getIEN())) {
                            problem.setIcdDiagnosis(diag);
                        }
                    }
                }
            }
        } catch (ResException e) {
            throw new OvidDomainException(e);
        }

    }
    public static void main(String[] args) {
        //BasicConfigurator.configure();
        //Logger.getRootLogger().setLevel(Level.ERROR);
        RPCConnection userConn = null;
        RPCConnection serverConn = null;

        try {

            if (args == null || args.length == 0) {
                args = new String[] { "localhost", "9201", "PU1234", "PU1234!!", "OV1234", "OV1234!!"};
            }

            if (args.length < 5) {
                System.err.println("usage: ProblemRepository <host> <port> <user-access-code> <user-verify-code> <ovid-access-code> <ovid-verify-code>");
                return;
            }

            userConn = new RPCBrokerConnection(args[0], new Integer(args[1]), args[2], args[3]);
            serverConn = new RPCBrokerConnection(args[0], new Integer(args[1]), args[4], args[5]);
            if (serverConn==null || userConn==null) {
                return;
            }

            ProblemRepository repo = new ProblemRepository(serverConn);
            int start, end;
            start = end = 2;//14484;

            for (Integer i = start; i <= end; i++) {
                System.out.println("*** dfn: " + i);
                Collection<FMProblem> problems = repo.getActiveProblemsForDFN(i.toString());
                repo.getDiagnosisInfo(problems);
                for (FMProblem problem : problems) {
                    System.out.println(problem);
                }

            }
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (RPCException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (OvidDomainException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (userConn != null) {
                try {
                    userConn.close();
                } catch (RPCException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            if (serverConn != null) {
                try {
                    serverConn.close();
                } catch (RPCException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        System.exit(0);

    }

}
