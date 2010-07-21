/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mgreer
 */
package org.mitre.medcafe.hdatabased.patienteducation;


import java.util.List;
import java.util.ArrayList;

import org.projecthdata.hdata.schemas._2009._06.provider.*;
import org.projecthdata.hdata.schemas._2009._06.comment.*;
import org.projecthdata.hdata.schemas._2009._06.core.DateRange;



public class PatientEducation {
    private List<Provider> providers;
    private Topic topic;
    private PatientUnderstanding patientUnderstanding;
    private DateRange encounterDate;
    private Comment comment;

    public PatientEducation()
    {
        super();
        providers = new ArrayList<Provider>();
    }
    public void setTopic(Topic topic)
    {
        this.topic = topic;
    }
    public void setPatientUnderstanding(PatientUnderstanding understand)
    {
        patientUnderstanding = understand;
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

    public Topic getTopic()
    {
        return topic;
    }
    public PatientUnderstanding getPatientUnderstanding()
    {
        return patientUnderstanding;
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
