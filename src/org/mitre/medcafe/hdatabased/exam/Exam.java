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
package org.mitre.medcafe.hdatabased.exam;

import java.util.List;
import java.util.ArrayList;

import org.projecthdata.hdata.schemas._2009._06.provider.*;
import org.projecthdata.hdata.schemas._2009._06.core.DateRange;
import org.projecthdata.hdata.schemas._2009._06.comment.*;


public class Exam {
    private List<Provider> providers;
    private ExamType examType;
    private DateRange encounterDate;
    private Comment comment;
    private ExamResult result;

    public Exam()
    {
        super();
        providers = new ArrayList<Provider>();

    }
    public void setExamType(ExamType type)
    {
        examType = type;
    }

    public void setEncounterDate(DateRange date)
    {
        encounterDate = date;
    }
    public void setComment(Comment comment)
    {
        this.comment = comment;
    }
    public void setExamResult(ExamResult exResult)
    {
    	result = exResult;
    }
    public List<Provider> getProviders()
    {
        return providers;
    }

    public ExamType getExamType()
    {
        return examType;
    }

    public DateRange getEncounterDate()
    {
        return encounterDate;
    }
    public Comment getComment()
    {
        return comment;
    }
	 public ExamResult getExamResult()
	 {
	     return result;
	 }
}
