/*
 *  Copyright 2010 The MITRE Corporation (http://www.mitre.org/). All Rights Reserved.
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
