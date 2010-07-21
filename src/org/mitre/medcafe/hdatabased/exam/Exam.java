/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
