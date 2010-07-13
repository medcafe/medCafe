package org.mitre.medcafe.util;

import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

import java.util.*;
import org.projecthdata.hdata.schemas._2009._06.patient_information.*;
import org.projecthdata.hdata.schemas._2009._06.allergy.*;
import org.projecthdata.hdata.schemas._2009._06.medication.*;
import org.projecthdata.hdata.schemas._2009._06.condition.*;
import org.projecthdata.hdata.schemas._2009._06.support.*;
import org.projecthdata.hdata.schemas._2009._06.immunization.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import com.medsphere.fileman.FMRecord;

/**
 *  This class represents a data Repository for MedCafe.  This allows for common functionality no matter if the underlying repository is VistA or hData or C32
 *  or other
 */
public class hDataRepository extends Repository
{

    public final static String KEY = hDataRepository.class.getName();
    public final static Logger log = Logger.getLogger( KEY );
    // static{log.setLevel(Level.FINER);}

    public hDataRepository()
    {
        type = "hData";
    }

    /**
     *  Given a patient id, get the patient info
     */
    public Patient getPatient( String patientId ){
        try
        {
            JAXBContext jc = JAXBContext.newInstance("org.projecthdata.hdata.schemas._2009._06.patient_information");
            Unmarshaller u = jc.createUnmarshaller();
            URL url = new URL( credentials[0] + "/hData-REST/resources/hDataRecord/patient/patientinformation/12345.xml" );
            URLConnection conn = url.openConnection();
            Patient p = (Patient)u.unmarshal(conn.getInputStream() );
            return p;
        }
        catch (Exception e) {
            log.log(Level.SEVERE, "Error retrieving patient " + patientId, e);
            return null;
        }
    }

    /**
     *  Get a list of patient identifiers
     */
    public Map<String, String> getPatients(){
        Map<String, String> ret = new HashMap<String,String>();
        ret.put( "000000001",  "Dr. Robert Bruce" );
        return ret;
    }

    /**
     *  Get a set of allergies specific to a patient
     */
    public List<Allergy> getAllergies( String patientId ){
        try
        {
            JAXBContext jc = JAXBContext.newInstance("org.projecthdata.hdata.schemas._2009._06.allergy");
            Unmarshaller u = jc.createUnmarshaller();
            URL url = new URL( credentials[0] + "/hData-REST/resources/hDataRecord/patient/adversereactions/allergies/937321.xml" );
            URLConnection conn = url.openConnection();
            Allergy p = (Allergy)u.unmarshal(conn.getInputStream() );
            List<Allergy> ret = new ArrayList<Allergy>();
            ret.add(p);
            return ret;
        }
        catch (Exception e) {
            log.log(Level.SEVERE, "Error retrieving patient " + patientId, e);
            return null;
        }
    }

    /**
     *  Get a set of medications specific to a patient
     */
    public List<Medication> getMedications( String patientId ){
        try
        {
            JAXBContext jc = JAXBContext.newInstance("org.projecthdata.hdata.schemas._2009._06.medication");
            Unmarshaller u = jc.createUnmarshaller();
            URL url = new URL( credentials[0] + "/hData-REST/resources/hDataRecord/patient/medications/IBU-200-12312.xml" );
            URLConnection conn = url.openConnection();
            Medication p = (Medication)u.unmarshal(conn.getInputStream() );
            List<Medication> ret = new ArrayList<Medication>();
            ret.add(p);
            return ret;
        }
        catch (Exception e) {
            log.log(Level.SEVERE, "Error retrieving patient " + patientId, e);
            return null;
        }
    }
    /**
     *  Get a support (contact) list for a patient.
     */
    public List<Support> getSupportInfo(String patientId) throws NotImplementedException
    {
    	  throw new NotImplementedException("getSupportInfo is not implemented for hDataRepository");

    }
     /**
     *  Get a list of immunizations for a patient.
     */
    public List<Immunization> getImmunizations(String id) throws NotImplementedException
    {
    	  throw new NotImplementedException("getImmunizations is not implemented for hDataRepository");

    }


    /**
     *    Problem List
     */
     public List<org.projecthdata.hdata.schemas._2009._06.condition.Condition> getProblems(String patientId) throws NotImplementedException
     {
     		throw new NotImplementedException("getProblems is not implemented for hDataRepository");
     }
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
    public Collection<FMRecord> getTimeLineInfo(String ien) throws NotImplementedException{
    	throw new NotImplementedException("getTimeLineInfo is not implemented for hDataRepository");
    }

    /* Still to be implemented*/
	@Override
	public Map<String, String> getPatientByName(String family, String given,
			String middle) {
		// TODO Auto-generated method stub
		return null;
	}

}
