/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mgreer
 */
package org.mitre.medcafe.hdatabased.treatment;

import java.util.List;
import java.util.ArrayList;

import org.projecthdata.hdata.schemas._2009._06.provider.*;
import org.projecthdata.hdata.schemas._2009._06.core.DateRange;
import org.projecthdata.hdata.schemas._2009._06.comment.*;


public class Treatment {
    private List<Provider> providers;
    private TreatmentType treatmentType;
    private DateRange encounterDate;
    private Comment comment;

    public Treatment()
    {
        super();
        providers = new ArrayList<Provider>();

    }
    public void setTreatmentType(TreatmentType type)
    {
        treatmentType = type;
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

    public TreatmentType getTreatmentType()
    {
        return treatmentType;
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

