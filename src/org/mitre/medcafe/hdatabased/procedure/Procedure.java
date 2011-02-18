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
