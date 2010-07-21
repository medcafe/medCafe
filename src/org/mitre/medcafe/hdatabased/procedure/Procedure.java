/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mgreer
 */
 package org.mitre.medcafe.hdatabased.procedure;

import java.util.List;
import java.util.ArrayList;
import javax.xml.datatype.XMLGregorianCalendar;

import org.projecthdata.hdata.schemas._2009._06.core.InstanceIdentifier;
import org.projecthdata.hdata.schemas._2009._06.core.Actor;
import org.projecthdata.hdata.schemas._2009._06.core.Informant;

public class Procedure {
    private InstanceIdentifier procedureId;
    private ProcedureCode procedureType;
    private String procedureFreeTextType;
    private XMLGregorianCalendar procedureDate;
    private Actor performer;
    private List<Informant> informationSource;
    private String narrative;

    public Procedure()
    {
        super();
        informationSource = new ArrayList<Informant>();

    }
    public void setProcedureId(InstanceIdentifier id)
    {
        procedureId = id;
    }

    public void setProcedureType(ProcedureCode type)
    {
        procedureType = type;
    }
    public void setProcedureFreeTextType(String text)
    {
        procedureFreeTextType = text;
    }
    public void setProcedureDate(XMLGregorianCalendar date)
    {
        procedureDate = date;
    }
    public void setPerformer(Actor perform)
    {
        performer = perform;
    }
    public void setNarrative(String narrate)
    {
        narrative = narrate;
    }
    public List<Informant> getInformationSource()
    {
        return informationSource;
    }
    public InstanceIdentifier getProcedureId()
    {
        return procedureId;
    }
    public ProcedureCode getProcedureType()
    {
        return procedureType;
    }
    public String getProcedureFreeTextType()
    {
        return procedureFreeTextType;
    }
    public XMLGregorianCalendar getProcedureDate()
    {
        return procedureDate;
    }
    public Actor getPerformer()
    {
        return performer;
    }
    public String getNarrative()
    {
        return narrative;
    }
}
