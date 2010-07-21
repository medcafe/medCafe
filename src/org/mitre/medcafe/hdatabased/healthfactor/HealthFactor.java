/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
