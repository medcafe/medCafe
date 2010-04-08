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
import org.projecthdata.hdata.schemas._2009._06.allergy.*;
import org.projecthdata.hdata.schemas._2009._06.core.*;
import org.projecthdata.hdata.schemas._2009._06.patient_information.*;
import org.projecthdata.hdata.schemas._2009._06.medication.*;

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
            //set DOB
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
    public Map<String, String> getPatients(){
        Map<String, String> ret = new HashMap<String,String>();
        try {
            if( setConnection( ) )
            {
                for (FMPatient lpatient : new PatientRepository(conn).getAllPatients()) {
                    // ret.add( lpatient.getEnterprisePatientIdentifier() );
                    // log.finer(lpatient.getIEN());
                    //ret.put( new String[]{"id",lpatient.getIEN()}, new String[]{"name", lpatient.getName()} );
                    ret.put( lpatient.getIEN(),  lpatient.getName() );
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
            // log.finer("closing connection");
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
            throw new RuntimeException("Invalid number of credentials.  You must proivide <host> <port> <ovid-access-code> <ovid-verify-code>" );
        }
    	this.credentials = credentials;
    }

    public List<Allergy> getAllergies( String id )
    {
        List<Allergy> list = new ArrayList<Allergy>();
        try {
            if( setConnection( ) )
            {
                log.finer("Connection made...");
                PatientItemRepository r = new PatientItemRepository(conn, conn, "MSC PATIENT DASHBOARD");
                log.finer("HERE!! " + r.toString());
                Collection<IsAPatientItem> vista_list = r.getAllergies(id);
                // an ArrayList of PatientAllergies objects is returned -  converty to hData Allergy type
                for( IsAPatientItem a : vista_list )
                {
                    Allergy allergy = new Allergy();  //hData type
                    PatientAllergy pa = (PatientAllergy) a;  //vista (ovid) type
                    //populate

                    Product c = new Product();
                    c.setValue( pa.getMessage() );
                    allergy.setProduct( c );
                    //set time for adverse reaction
                    if( pa.getDateTime() != null )
                    {
                        GregorianCalendar cal = new GregorianCalendar();
                        cal.setTime( pa.getDateTime() );
                        DatatypeFactory factory = DatatypeFactory.newInstance();
                        DateRange d = new DateRange();
                        d.setLow(factory.newXMLGregorianCalendar(cal));
                        allergy.setAdverseEventDate( d );
                    }

                    Reaction re = new Reaction();
                    re.setValue( pa.getReaction() );
                    allergy.setReaction( re );

                    // System.out.println(pa.toString());
                    //add to the list
                    list.add(allergy);
                }
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

    public List<Medication> getMedications( String id )
    {
        List<Medication> list = new ArrayList<Medication>();
        try {
            if( setConnection( ) )
            {
                PatientItemRepository r = new PatientItemRepository(conn, conn, "MSC PATIENT DASHBOARD");
                Collection<IsAPatientItem> vista_list = r.getMedications(id);
                // an ArrayList of PatientAllergies objects is returned -  converty to hData Medication type
                for( IsAPatientItem a : vista_list )
                {
                    Medication medication = new Medication();  //hData type
                    PatientMedication pa = (PatientMedication) a;  //vista (ovid) type
                    //populate
                    //message -> narrative
                    medication.setNarrative( pa.getMessage() );
                    //set time for adverse reaction
                    if( pa.getDateTime() != null )
                    {
                        GregorianCalendar cal = new GregorianCalendar();
                        cal.setTime( pa.getDateTime() );
                        DatatypeFactory factory = DatatypeFactory.newInstance();
                        medication.getEffectiveTime().add(factory.newXMLGregorianCalendar(cal));
                    }

                    String medname = pa.getMedName();
                    MedicationInformation m = new MedicationInformation();
                    MedicationInformation.ManufacturedMaterial mm = new MedicationInformation.ManufacturedMaterial();
                    m.setManufacturedMaterial(mm);
                    mm.setFreeTextBrandName(medname);
                    medication.setMedicationInformation( m );

                    Dose d = new Dose();
                    d.setValue( pa.getDose() );
                    medication.setDose( d );

                    CodedValue c = new CodedValue();
                    c.setValue( pa.getDelivery() );
                    medication.setDeliveryMethod( c );

                    medication.setPatientInstructions( pa.getFrequency() );
                    //add to the list
                    list.add(medication);
                }
                log.finer("Number of medications for patient " + id + " is " + list.size() );
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
