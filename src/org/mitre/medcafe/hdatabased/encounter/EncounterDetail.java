/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mgreer
 */

package org.mitre.medcafe.hdatabased.encounter;

import java.util.List;
import java.util.ArrayList;


import org.projecthdata.hdata.schemas._2009._06.encounter.*;
import org.projecthdata.hdata.schemas._2009._06.condition.*;
import org.projecthdata.hdata.schemas._2009._06.immunization.*;
import org.projecthdata.hdata.schemas._2009._06.result.*;

import org.mitre.medcafe.hdatabased.patienteducation.*;
import org.mitre.medcafe.hdatabased.exam.*;
import org.mitre.medcafe.hdatabased.treatment.*;
import org.mitre.medcafe.hdatabased.procedure.*;
import org.mitre.medcafe.hdatabased.healthfactor.*;

public class EncounterDetail extends Encounter {
    private List<Immunization> immunizations;
    private List<Condition> conditions;
    private List<PatientEducation> education;
    private List<Result> results;
    private List<Exam> exams;
    private List<Treatment> treatments;
    private List<Procedure> procedures;
    private List<HealthFactor> healthFactors;
    public static final long serialVersionUID = 12352L;

    public EncounterDetail()
    {
        super();
        immunizations = new ArrayList<Immunization>();
        conditions = new ArrayList<Condition>();
        education = new ArrayList<PatientEducation>();
        results = new ArrayList<Result>();
        exams = new ArrayList<Exam>();
        treatments = new ArrayList<Treatment>();
        procedures = new ArrayList<Procedure>();
        healthFactors = new ArrayList<HealthFactor>();
    }

    public List<Immunization> getImmunizations()
    {
        return immunizations;
    }
    public List<Condition> getConditions()
    {
        return conditions;
    }
    public List<PatientEducation> getEducation()
    {
        return education;
    }
    public List<Result> getResults()
    {
        return results;
    }
    public List<Exam> getExams()
    {
        return exams;
    }
    public List<Treatment> getTreatments()
    {
        return treatments;
    }
    public List<Procedure> getProcedures()
    {
        return procedures;
    }
    public List<HealthFactor> getHealthFactors()
    {
        return healthFactors;
    }
}
