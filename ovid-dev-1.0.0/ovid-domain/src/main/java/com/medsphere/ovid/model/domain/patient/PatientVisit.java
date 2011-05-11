/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mgreer
 */
package com.medsphere.ovid.model.domain.patient;

import com.medsphere.fmdomain.FMVisit;
import com.medsphere.fmdomain.FMV_Exam;
import com.medsphere.fmdomain.FMV_Immunization;
import com.medsphere.fmdomain.FMV_HealthFactors;
import com.medsphere.fmdomain.FMV_CPT;
import com.medsphere.fmdomain.FMV_PatientEd;
import com.medsphere.fmdomain.FMV_POV;
import com.medsphere.fmdomain.FMV_Provider;
import com.medsphere.fmdomain.FMV_SkinTest;
import com.medsphere.fmdomain.FMV_Treatment;

import java.util.ArrayList;
import java.util.Collection;

public class PatientVisit implements Comparable<PatientVisit> {
    private FMVisit visit;
    private ArrayList<FMV_Exam> exams;
    private ArrayList<FMV_Immunization> immunizations;
    private ArrayList<FMV_HealthFactors> healthFactors;
    private ArrayList<FMV_CPT> currentProcedureCodes;
    private ArrayList<FMV_PatientEd> education;
    private ArrayList<FMV_POV> purposes;
    private ArrayList<FMV_Provider> providers;
    private ArrayList<FMV_SkinTest> skinTests;
    private ArrayList<FMV_Treatment> treatments;

    public PatientVisit(FMVisit visit)
    {
        this.visit = visit;
        exams = new ArrayList<FMV_Exam>();
        immunizations = new ArrayList<FMV_Immunization>();
        healthFactors = new ArrayList<FMV_HealthFactors>();
        currentProcedureCodes = new ArrayList<FMV_CPT>();
        education = new ArrayList<FMV_PatientEd>();
        purposes = new ArrayList<FMV_POV>();
        providers = new ArrayList<FMV_Provider>();
        skinTests = new ArrayList<FMV_SkinTest>();
        treatments = new ArrayList<FMV_Treatment>();
    }
    public FMVisit getVisit()
    {
        return visit;
    }
    public Collection<FMV_Exam> getExams()
    {
        return exams;
    }
     public Collection<FMV_Immunization> getImmunizations()
    {
        return immunizations;
    }
     public Collection<FMV_HealthFactors> getHealthFactors()
    {
        return healthFactors;
    }
     public Collection<FMV_CPT> getCurrentProcedureCodes()
    {
        return currentProcedureCodes;
    }
     public Collection<FMV_PatientEd> getPatientEd()
    {
        return education;
    }
     public Collection<FMV_POV> getPOVs()
    {
        return purposes;
    }
     public Collection<FMV_Provider> getProviders()
    {
        return providers;
    }
     public Collection<FMV_SkinTest> getSkinTests()
    {
        return skinTests;
    }
     public Collection<FMV_Treatment> getTreatments()
    {
        return treatments;
    }
     @Override
     public int compareTo(PatientVisit pv)
     {
         int value = pv.getVisit().getVisitDate().compareTo(getVisit().getVisitDate());
         if (value==0)
             return Integer.parseInt(pv.getVisit().getIEN()) - Integer.parseInt(getVisit().getIEN());
         return value;
     }
     public boolean equals(PatientVisit pv)
     {
        return (pv.getVisit().getIEN().equals(getVisit().getIEN()));
     }
    public String toString()
    {
        String str = "Visit Record: " + visit + "\n";
        for (FMV_Exam exam : exams)
            str = str + "\tExam Record: " + exam + "\n";
        for (FMV_Immunization immune : immunizations)
            str = str + "\tImmunization Record: " + immune + "\n";
        for (FMV_HealthFactors healthFactor : healthFactors)
            str = str + "\tHealth Factor Record: " + healthFactor + "\n";
        for (FMV_CPT cpt : currentProcedureCodes)
            str = str + "\tCPT Record " + cpt + "\n";
        for (FMV_PatientEd edu : education)
            str = str + "\tPatientEd Record: " + edu + "\n";
        for (FMV_POV pov : purposes)
            str = str + "\tPurpose of Visit Record: " + pov + "\n";
        for (FMV_Provider provider : providers)
            str = str + "\tProvider Record: " + provider + "\n";
        for (FMV_SkinTest skinTest : skinTests)
            str = str + "\tSkin Test Record: " + skinTest + "\n";
        for (FMV_Treatment treatment : treatments)
            str = str + "\tTreatment Record: " + treatment + "\n";
        return str;
    }
}
