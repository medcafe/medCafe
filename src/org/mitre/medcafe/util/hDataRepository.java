package org.mitre.medcafe.util;

import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

import java.util.*;
import org.projecthdata.hdata.schemas._2009._06.patient_information.*;
import org.projecthdata.hdata.schemas._2009._06.allergy.*;
import org.projecthdata.hdata.schemas._2009._06.medication.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 *  This class represents a data Repository for MedCafe.  This allows for common functionality no matter if the underlying repository is VistA or hData or C32
 *  or other
 */
public class hDataRepository extends Repository
{

    public final static String KEY = hDataRepository.class.getName();
    public final static Logger log = Logger.getLogger( KEY );
    static{log.setLevel(Level.FINER);}

    public hDataRepository()
    {
        type = "hData";
    }

    /**
     *  Given a patient id, get the patient info
     */
    public Patient getPatient( String patientId ){ return null; }

    /**
     *  Get a list of patient identifiers
     */
    public Map<String, String> getPatients(){ return null; }

    /**
     *  Get a set of allergies specific to a patient
     */
    public List<Allergy> getAllergies( String patientId ){ return null; }

    /**
     *  Get a set of medications specific to a patient
     */
    public List<Medication> getMedications( String patientId ){ return null; }

    /**
     * Type property.
     */
    protected String type = null;

    public static void main(String[] args)
        throws Exception
    {
        JAXBContext jc = JAXBContext.newInstance("org.projecthdata.hdata.schemas._2009._06.patient_information");
        Unmarshaller u = jc.createUnmarshaller();
        URL url = new URL( args[0] );
        URLConnection conn = url.openConnection();
        Patient p = (Patient)u.unmarshal(conn.getInputStream() );
        System.out.println(p.getRace());
    }

}
