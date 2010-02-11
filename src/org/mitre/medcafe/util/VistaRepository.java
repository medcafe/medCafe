package org.mitre.medcafe.util;

import org.json.*;
import org.projecthdata.hdata.buildingblocks.core.*;
import com.medsphere.fileman.*;
import com.medsphere.fmdomain.FMPatient;
import com.medsphere.ovid.domain.ov.OvidDomainException;
import com.medsphere.ovid.domain.ov.PatientRepository;
import com.medsphere.vistalink.*;
import gov.va.med.vistalink.adapter.cci.VistaLinkConnection;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
// import org.mitre.hdata.hrf.core.*;

/**
 *  This class implements an interface to a back-end Vista repostory.  The Medsphere (http://www.medsphere.com/) version of VistA, named OpenVista, is
 *  used via the medsphere ovid library
 */
public class VistaRepository extends Repository
{

    public final static String KEY = VistaRepository.class.getName();
    public final static Logger log = Logger.getLogger( KEY );
    static{log.setLevel(Level.FINER);}

    protected VistaLinkPooledConnection conn = null;

    public VistaRepository()
    {
        type = "VistA";
    }

    /**
     *  Given a patient id, get the patient info
     */
    public JSONObject getPatient( String id ){
            JSONObject ret = new JSONObject();
        try {
            FMPatient filemanPat = null;
            JSONObject patientData = new JSONObject();
            if( setConnection( ) )
            {
                for (FMPatient lpatient : new PatientRepository(conn).getAllPatients()) {
                    if( lpatient.getId().equals(id) )
                    {
                        filemanPat = lpatient;
                        break;
                    }
                }
            }
            patientData.put("id", filemanPat.getId());
            patientData.put("gender",filemanPat.getSex());
            patientData.put("dob",filemanPat.getDob());
            ret.put("patient", patientData);
            // patientData.put("",filemanPat.getXXX());
            // Patient ret = new Patient();
            // ret.setId( filemanPat.getId() );
            // if( filemanPat.getSex().equals("FEMALE") )
            // {
            //     ret.setGender(Gender.FEMALE);
            // }
            // else if( filemanPat.getSex().equals("MALE"))
            // {
            //     ret.setGender(Gender.MALE);
            // }
            // else
            //     ret.setGender(Gender.UNDIFFERENTIATED);
            // log.finer(String.valueOf(filemanPat.getDob()));
            // // g.setValue(  );
            // ret.setGender( g );
            // ret.setBirthtime(  );
            return ret;
        }
        catch (OvidDomainException e) {
            log.log(Level.SEVERE, "Error retrieving patient " + id, e);
            return null;
        }
        catch (org.json.JSONException e) {
            log.throwing(KEY, "Error assembling return object", e);
            return null;
        }
        finally
        {
            closeConnection();
        }

    }

    /**
     *  Get a list of patient identifiers
     */
    public List<String> getPatients(){
        List<String> ret = new ArrayList<String>();
        try {
            if( setConnection( ) )
            {
                for (FMPatient lpatient : new PatientRepository(conn).getAllPatients()) {
                    // ret.add( lpatient.getEnterprisePatientIdentifier() );
                    ret.add( lpatient.getId() );
                }
            }
            else
                return null;
            return ret;
        }
        catch (OvidDomainException e) {
            log.log(Level.SEVERE, "Error retrieving patient list", e);
            return null;
        }
        finally
        {
            closeConnection();
        }
    }


    /**
     *  sets the passed VistaLinkPooledConnection
     *  @return true if conneciton worked.  False otherwise.
     */
    protected boolean setConnection() throws OvidDomainException
    {
        if( conn != null )
        {
            return true;
        }
        conn = PatientRepository.getDirectConnection(credentials[0], credentials[1], credentials[2], credentials[3]);
        if (conn==null) {
            log.severe("Connection to repository failed.  Credentials were " + Arrays.toString(credentials));
            return false;
        }
        return true;
    }

    /**
     *  closes the passed VistaLinkPooledConnection
     */
    protected void closeConnection()
    {
        if( conn != null )
        {
            // conn.returnToPool();
            conn.close();
        }
    }

    /**
     * Set credentials property.
     *
     *@param credentials New credentials property.
     */
    public void setCredentials(String... credentials) {
        if( credentials.length < 4 )
        {
            throw new NullPointerException("Invalid number of credentials.  You must proivide <host> <port> <ovid-access-code> <ovid-verify-code>" );
        }
    	this.credentials = credentials;
    }



}
