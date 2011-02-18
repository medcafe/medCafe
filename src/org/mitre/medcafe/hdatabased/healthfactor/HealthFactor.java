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

package org.mitre.medcafe.hdatabased.healthfactor;

import java.util.List;
import java.util.ArrayList;

import org.projecthdata.hdata.schemas._2009._06.provider.*;
import org.projecthdata.hdata.schemas._2009._06.core.DateRange;
import org.projecthdata.hdata.schemas._2009._06.comment.*;
import org.projecthdata.hdata.schemas._2009._06.allergy.Severity;

public class HealthFactor {
    private List<Provider> providers;
    private Factor healthFactor;
    private Severity severity;
    private DateRange encounterDate;
    private Comment comment;

    public HealthFactor()
    {
        super();
        providers = new ArrayList<Provider>();

    }
    public void setHealthFactor(Factor factor)
    {
        healthFactor = factor;
    }
    public void setSeverity(Severity severity)
    {
        this.severity = severity;
    }
    public void setEncounterDate(DateRange date)
    {
        encounterDate = date;
    }
    public void setComment(Comment comment)
    {
        this.comment = comment;
    }
    public List<Provider> getProviders()
    {
        return providers;
    }

    public Factor getHealthFactor()
    {
        return healthFactor;
    }
    public Severity getSeverity()
    {
        return severity;
    }
    public DateRange getEncounterDate()
    {
        return encounterDate;
    }
    public Comment getComment()
    {
        return comment;
    }


}
