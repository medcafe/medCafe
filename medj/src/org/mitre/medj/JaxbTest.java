package org.mitre.medj;

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.*;

import javax.xml.bind.*;

import org.mitre.medj.jaxb.ContinuityOfCareRecord;

import com.google.gson.*;


public class JaxbTest
{

    public final static String KEY = JaxbTest.class.getName();
    public final static Logger log = Logger.getLogger( KEY );
    // static{log.setLevel(Level.FINER);}


    public static void main(String[] args)
        throws Exception
    {
        try
        {
            JAXBContext jc = JAXBContext.newInstance("org.mitre.medj.jaxb");
            Unmarshaller u = jc.createUnmarshaller();
            u.setEventHandler(new javax.xml.bind.helpers.DefaultValidationEventHandler());
            // URL url = new URL( "simple.ccr.xml" );
            // URLConnection conn = url.openConnection();
            ContinuityOfCareRecord p = (ContinuityOfCareRecord)u.unmarshal(new File(args[0]) );
            Gson gson = new Gson();
            // Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString = gson.toJson(p);
            System.out.println(jsonString);

        }
        catch (Exception e) {
            log.log(Level.SEVERE, "Error retrieving ContinuityOfCareRecord ", e);
        }
    }

}
