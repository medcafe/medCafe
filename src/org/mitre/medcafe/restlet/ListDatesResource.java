package org.mitre.medcafe.restlet;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;
import org.mitre.medcafe.util.Config;
import org.mitre.medcafe.util.Repository;
import org.restlet.data.Form;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.ResourceException;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;


public class ListDatesResource extends ServerResource {

	 /** The underlying Item object. */
    //Patient item;

    /** The sequence of characters that identifies the resource. */
    String id;
    String repository;
    public final static String KEY = ListDatesResource.class.getName();
    public final static Logger log = Logger.getLogger( KEY );
    // static{log.setLevel(Level.FINER);}

    protected Date startDate = new Date();
    protected Date endDate =  new Date();
    protected String intervalType = Config.EMPTY_STR;


    private static final String MONTHS = "months";
    private static final String YEARS = "years";
    private static final String DAYS = "days";

    @Override
    protected void doInit() throws ResourceException {
        // Get the "type" attribute value taken from the URI template
        Form form = getRequest().getResourceRef().getQueryAsForm();
        String startDateStr = form.getFirstValue("start_date");
        if (startDateStr == null)
        	startDateStr = "06/01/2008";

        String endDateStr = form.getFirstValue("end_date");
        if (endDateStr == null)
        	endDateStr = "08/01/2009";

        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        try {
			startDate = df.parse(startDateStr);
			endDate = df.parse(endDateStr);
        }
        catch (ParseException e)
        {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        intervalType = form.getFirstValue("interval_type");
        if (intervalType == null)
        	intervalType = MONTHS;

    }

    @Get("html")
    public Representation toHtml(){

    	System.out.println("Found ListDatesResource html ");

    	StringBuffer startBuf1 = new StringBuffer();
    	StringBuffer startBuf2 = new StringBuffer();
    	StringBuffer patientImages = new StringBuffer();
    	StringBuffer endBuf = new StringBuffer();
    	try
        {
        	System.out.println("ListDatesResource JSON start");


        	GregorianCalendar start = new GregorianCalendar();
        	start.setTime(startDate);
        	GregorianCalendar end = new GregorianCalendar();
        	end.setTime(endDate);
        	HashMap<Integer, HashMap<Integer, ArrayList<Integer> > >  dayMonthsYears = getDays(start, end);

        	startBuf1.append("<fieldset><label for=\"valueAA\">From:</label><select name=\"valueAA\" id=\"valueAA\">");
        	startBuf2.append("<label for=\"valueBB\">From:</label><select name=\"valueBB\" id=\"valueBB\">");

        	if (intervalType.equals(MONTHS))
        	{
        		ArrayList<Integer> sortYears = new ArrayList<Integer>( dayMonthsYears.keySet());
				Collections.sort(sortYears);

				StringBuffer yearBuf = new StringBuffer();

                for(Integer year: sortYears)
                {

                	yearBuf.append("<optgroup label=\"" + year  + "\">");


                    ArrayList<Integer> sortMonths = new ArrayList<Integer>( dayMonthsYears.get(year).keySet());
    				Collections.sort(sortMonths);

                    for (Integer month: sortMonths)
                    {
                    	yearBuf.append("<option value=\"" + month + "/" + year + "\">" + month + "/" + year + "</option>");
                    	//<option value="01/03">Jan 03</option>
                    }

                    yearBuf.append("</optgroup>");


                }
                startBuf1.append(yearBuf.toString() + "</select>");
                startBuf2.append(yearBuf.toString() + "</select></fieldset>");

        	}
        	else if (intervalType.equals(DAYS))
        	{
        		ArrayList<Integer> sortYears = new ArrayList<Integer>( dayMonthsYears.keySet());
				Collections.sort(sortYears);

                for(Integer year: sortYears)
                {


                    ArrayList<Integer> sortMonths = new ArrayList<Integer>( dayMonthsYears.get(year).keySet());
    				Collections.sort(sortMonths);

                    for (Integer month: sortMonths)
                    {

                    	 ArrayList<Integer> sortDays = dayMonthsYears.get(year).get(month);
                    	 Collections.sort(sortDays);

                    	 for (Integer day: sortDays)
                         {


                         }
                    }

                }
        	}
        	else if (intervalType.equals(YEARS))
        	{

        	}


            //log.finer( obj.toString());

        }
        catch(Exception e)
        {
            log.throwing(KEY, "toHtml()", e);
            return null;
        }

    	return new StringRepresentation( startBuf1.toString() + startBuf2.toString());

    }

    @Get("json")
    public JsonRepresentation toJson(){
        try
        {
        	System.out.println("ListDatesResource JSON start");


        	GregorianCalendar start = new GregorianCalendar();
        	start.setTime(startDate);
        	GregorianCalendar end = new GregorianCalendar();
        	end.setTime(endDate);
        	HashMap<Integer, HashMap<Integer, ArrayList<Integer> > >  dayMonthsYears = getDays(start, end);

        	JSONObject obj = new JSONObject();

        	if (intervalType.equals(MONTHS))
        	{
        		ArrayList<Integer> sortYears = new ArrayList<Integer>( dayMonthsYears.keySet());
				Collections.sort(sortYears);

                for(Integer year: sortYears)
                {

                    JSONObject inner_obj = new JSONObject ();
                    inner_obj.put("year", year);

                    ArrayList<Integer> sortMonths = new ArrayList<Integer>( dayMonthsYears.get(year).keySet());
    				Collections.sort(sortMonths);

                    for (Integer month: sortMonths)
                    {


                    	 inner_obj.append("months", month);
                    }

                    obj.append("years", inner_obj);  //append creates an array for you

                }
        	}
        	else if (intervalType.equals(DAYS))
        	{
        		ArrayList<Integer> sortYears = new ArrayList<Integer>( dayMonthsYears.keySet());
				Collections.sort(sortYears);

                for(Integer year: sortYears)
                {

                    JSONObject inner_obj = new JSONObject ();
                    inner_obj.put("year", year);

                    ArrayList<Integer> sortMonths = new ArrayList<Integer>( dayMonthsYears.get(year).keySet());
    				Collections.sort(sortMonths);

                    for (Integer month: sortMonths)
                    {

                    	JSONObject inner_inner_obj = new JSONObject ();
                    	 inner_inner_obj.put("month", month);
                    	 inner_obj.append("months", inner_inner_obj);

                    	 ArrayList<Integer> sortDays = dayMonthsYears.get(year).get(month);
                    	 Collections.sort(sortDays);

                    	 JSONArray inner_inner_inner_obj = new JSONArray ();

                    	 for (Integer day: sortDays)
                         {
                    		 System.out.println("Days " + day);
                    		 inner_inner_inner_obj.put(day);

                         }
                    	 inner_inner_obj.append("days", inner_inner_inner_obj);
                    }

                    obj.append("years", inner_obj);  //append creates an array for you

                }
        	}
        	else if (intervalType.equals(YEARS))
        	{

        	}


            //log.finer( obj.toString());
           // System.out.println("ListDatesResource JSON " +  obj.toString());
            return new JsonRepresentation(obj);
        }
        catch(Exception e)
        {
            log.throwing(KEY, "toJson()", e);
            return null;
        }


    }


    private HashMap<Integer, HashMap<Integer, ArrayList<Integer> > > getDays( Calendar startDate, Calendar endDate)
    {
    		int previousYear = 0;
    		int previousMonth = -1;

    		HashMap<Integer,HashMap<Integer,ArrayList<Integer>>> yearMonths = new HashMap<Integer, HashMap<Integer, ArrayList<Integer>>>();
    		HashMap<Integer, ArrayList<Integer>> monthDays = new HashMap<Integer, ArrayList<Integer>>();
    		ArrayList<Integer> days = new ArrayList<Integer>();

    	    for(startDate.get(Calendar.DAY_OF_MONTH);
    	    	startDate.compareTo(endDate) <= 0;
    	    	startDate.add(Calendar.DAY_OF_MONTH, 1))
    	    {

	    		if (startDate.get(Calendar.YEAR) > previousYear )
        		{

    	    		monthDays = new HashMap<Integer, ArrayList<Integer>>();

    	    		yearMonths.put(startDate.get(Calendar.YEAR), monthDays);
        			previousYear = startDate.get(Calendar.YEAR);
        			previousMonth = -1;
        		}

	    		int realMonth = startDate.get(Calendar.MONTH) + 1;

    	    	if (startDate.get(Calendar.MONTH) > previousMonth )
        		{


    	    		monthDays = yearMonths.get(startDate.get(Calendar.YEAR));

    	    		days = new ArrayList<Integer>();
    	    		days.add(startDate.get(Calendar.DAY_OF_MONTH ) );
    	    		monthDays.put(realMonth, days);

        			previousMonth = startDate.get(Calendar.MONTH);
        		}
    	    	else
    	    	{
    	    		days = monthDays.get(realMonth);
    	    		days.add(startDate.get(Calendar.DAY_OF_MONTH ) );
    	    		monthDays.put(realMonth, days);
    	    	}
    	    }

    	    return yearMonths;
    }

    //get a list of all the months by year, from start date to end date
    private HashMap<String, ArrayList<String> > getMonths( Calendar startDate, Calendar endDate)
    {
    	ArrayList<String> monthList = new ArrayList<String>();
    	HashMap<String, ArrayList<String>> months = new HashMap<String, ArrayList<String>>();
    	int previousYear = startDate.get(Calendar.YEAR);

    	for(startDate.get(Calendar.MONTH);
    		startDate.compareTo(endDate) <= 0;
    		startDate.add(Calendar.MONTH, 1))
    	{
    		if (startDate.get(Calendar.YEAR) > previousYear )
    		{
    			monthList = new ArrayList<String>();
    			months.put(startDate.get(Calendar.YEAR)+"", monthList);
    		}
    		monthList.add(startDate.get(Calendar.MONTH ) +"");

    	}
    	return months;

    }
}