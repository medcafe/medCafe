package org.mitre.medcafe.util;

import java.util.logging.Level;
import java.util.logging.Logger;
import com.medsphere.ovid.domain.ov.OvidDomainException;
import java.util.*;
import org.mitre.hdata.hrf.patientinformation.*;
import com.medsphere.vistalink.*;
import com.medsphere.fileman.*;
import com.medsphere.ovid.domain.ov.PatientRepository;
import com.medsphere.fmdomain.FMPatient;
import gov.va.med.vistalink.adapter.cci.VistaLinkConnection;

/**
 *  This class implements an interface to a back-end Vista repostory.  The Medsphere (http://www.medsphere.com/) version of VistA, named OpenVista, is
 *  used via the medsphere ovid library
 */
public class VistaRepository extends Repository
{

    public final static String KEY = VistaRepository.class.getName();
    public final static Logger log = Logger.getLogger( KEY );
    static{log.setLevel(Level.FINER);}

    public VistaRepository()
    {
        type = "VistA";
    }

    /**
     *  Given a patient id, get the patient info
     */
    public Patient getPatient( String id ){
        return null;
    }

    /**
     *  Get a list of patient identifiers
     */
    public List<String> getPatients(){
        VistaLinkPooledConnection conn = null;
        List<String> ret = new ArrayList<String>();
        try {
            conn = PatientRepository.getDirectConnection(credentials[0], credentials[1], credentials[2], credentials[3]);
            if (conn==null) {
                log.severe("Connection to repository failed.  Credentials were " + Arrays.toString(credentials));
                return null;
            }
            for (FMPatient lpatient : new PatientRepository(conn).getAllPatients()) {
                ret.add( lpatient.getEnterprisePatientIdentifier() );
            }
        }
        catch (OvidDomainException e) {
            log.log(Level.SEVERE, "Error retrieving patient list", e);
            return null;
        }
        finally {
            if( conn != null )
            {
                conn.close();
            }
        }
        return ret;
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
