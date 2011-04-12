/*
 *  Copyright 2010 Jeffrey Hoyt.  All rights reserved.
 */

package org.mitre.medj.servlets;

import com.google.gson.*;
import java.io.*;
import java.util.logging.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.xml.bind.*;
import javax.xml.transform.stream.*;
import org.mitre.medj.*;
import org.mitre.medj.jaxb.*;

/**
 *
 *
 * @author     jchoyt
 * @created
 */
public class Convert extends HttpServlet
{

    public final static String KEY = Convert.class.getName();
    public final static Logger log = Logger.getLogger( KEY );
    // static{log.setLevel(Level.FINER);}

    /**
     *  Empty constuctor must call super()
     */
     public Convert()
    {
        super();
    }

    /**
     *  Description of the Method
     *
     */
    @Override
    public void doGet( HttpServletRequest request, HttpServletResponse response )
        throws IOException, ServletException
    {
        response.sendRedirect("http://medcafe.org");
    }


    /**
     *  Description of the Method
     *
     */
    @Override
    public void doPost( HttpServletRequest request, HttpServletResponse response )
        throws IOException, ServletException
    {
        PrintWriter out = response.getWriter();
        String sourceXml = WebUtils.getRequiredParameter( request, "source_xml" );
        try
        {
            JAXBContext jc = JAXBContext.newInstance("org.mitre.medj.jaxb");
            Unmarshaller u = jc.createUnmarshaller();
            // URL url = new URL( "simple.ccr.xml" );
            // URLConnection conn = url.openConnection();
            ContinuityOfCareRecord p = (ContinuityOfCareRecord)u.unmarshal(new StreamSource( new StringReader( sourceXml ) ) );
            Gson gson = new Gson();
            // Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString = gson.toJson(p);
            out.write(jsonString);
        }
        catch (Exception e) {
            out.write("There was an error parsing your XML or creating the JSON.  Please double check your input and ensure it is a valid CCR document.");
            log.log(Level.SEVERE, "Error retrieving ContinuityOfCareRecord ", e);
        }
    }

}


