package org.mitre.medcafe.util;

// import org.mitre.hdata.hrf.core.*;
import com.medsphere.fileman.*;
import com.medsphere.fmdomain.*;
import com.medsphere.ovid.domain.ov.*;
import com.medsphere.ovid.model.domain.patient.*;
import com.medsphere.vistalink.*;
import gov.va.med.vistalink.adapter.cci.VistaLinkConnection;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.*;
import org.json.*;
import org.projecthdata.hdata.schemas._2009._06.core.*;
import org.projecthdata.hdata.schemas._2009._06.patient_information.*;

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
    public Patient getPatient( String id ){
        try {
            FMPatient filemanPat = null;
            if( setConnection( ) )
            {
                PatientRepository patientRepository = new PatientRepository(conn);
                // for (FMPatient lpatient : patientRepository.getAllPatients()) {
                //     if( lpatient.getId().equals(id) )
                //     {
                //         filemanPat = lpatient;
                //         log.finer( "Last DUZ: " + patientRepository.getDUZForLastConnection() );
                //         break;
                //     }
                // }
                filemanPat = patientRepository.getPatientByIEN(id);
                log.finer("\t" + filemanPat.getIENS());
                for (FMField field : filemanPat.getFields()) {
                    if (filemanPat.getValue(field.getName()) != null) {
                        log.finer("\t" +field.getName() + ": " + filemanPat.getValue(field.getName()));
                    }
                }
            }
            if( filemanPat == null ) return null;

            log.finer(filemanPat.getName());
            Patient ret = new Patient();

            //set  name
            Name name = new Name();
            String[] nameParts = filemanPat.getName().split(",");
            List<String> given = name.getGiven();
            given.add( nameParts[1]);
            name.setLastname( nameParts[0] );
            ret.setName( name  );
            ret.setId( filemanPat.getIEN() );

            //set gender
            Gender g = new Gender();
            g.setDisplayName( filemanPat.getSex() );
            ret.setGender( g );
            log.finer(String.valueOf(filemanPat.getDob()));
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime( filemanPat.getDob() );
            DatatypeFactory factory = DatatypeFactory.newInstance();
            ret.setBirthtime( factory.newXMLGregorianCalendar(cal));
            return ret;
        }
        catch (OvidDomainException e) {
            log.log(Level.SEVERE, "Error retrieving patient " + id, e);
            return null;
        }
        catch (DatatypeConfigurationException e) {
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
                    log.finer(lpatient.getIEN());
                    ret.add( lpatient.getIEN() );
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
        conn = OvidSecureRepository.getDirectConnection(credentials[0], credentials[1], credentials[2], credentials[3]);
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
            log.finer("closing connection");
            // conn.returnToPool();
            conn.close();
            conn = null;
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


    protected Collection<IsAPatientItem> getAllergies( String id )
    {
        Collection<IsAPatientItem> list = null;
        try {
            if( setConnection( ) )
            {
                log.finer("Connection made...");
                PatientItemRepository r = new PatientItemRepository(conn, conn, "MSC PATIENT DASHBOARD");
                log.finer("HERE!! " + r.toString());
                list = r.getAllergies(id);
                // an ArrayList of PatientAllergies objects is returned
            }
            else log.warning("BAD CONNECTION");

        } catch (Throwable e) {
            log.throwing(KEY, "Error retreiving PatientItems", e );
        }
        finally {
            if (conn != null) conn.close();
            return list;
        }
    }
}
