package test.org.mitre.medcafe.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.medsphere.fileman.*;
import com.medsphere.fmdomain.FMPatient;
import com.medsphere.ovid.domain.ov.OvidDomainException;
import com.medsphere.ovid.domain.ov.PatientRepository;
import com.medsphere.vistalink.*;
import gov.va.med.vistalink.adapter.cci.VistaLinkConnection;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.*;
import org.mitre.hdata.hrf.patientinformation.*;
import org.mitre.medcafe.restlet.*;
import org.mitre.medcafe.util.*;
import java.net.*;
import org.json.JSONObject;


public class VistaRepositoryTest extends VistaRepository {

    public final static String KEY = VistaRepositoryTest.class.getName();
    public final static Logger log = Logger.getLogger( KEY );
    static{log.setLevel(Level.FINER);}

    /**
     * Sets up the test fixture.
     * (Called before every test case method.)
     */
    @Before
    public void setUp() throws Exception {
        boolean gotOne = false;
        String host = "192.168.56.101";

        if( InetAddress.getByName(host).isReachable(3000) )
        {
            setName("JeffVista");
            setCredentials( host, "8002", "OV1234", "OV1234!!" );
            gotOne = true;
        }
        else
        {
            host = "mmrc.mitre.org";
            if( InetAddress.getByName(host).isReachable(3000) )
            {
                setName("OurVista");
                setCredentials( "128.29.109.7", "8002", "OV1234", "OV1234!!" );
                gotOne = true;
            }
        }
        log.finer( "Using " + getName() );

        assertTrue( gotOne );
        long start = System.currentTimeMillis();
        boolean success = setConnection();
        log.finer( "Setting connection took: " + (System.currentTimeMillis() - start)/1000 + " seconds.  Successful? " + success);
        assertTrue( success );
    }


    /**
     * Tears down the test fixture.
     * (Called after every test case method.)
     */
    @After
    public void tearDown() {

    }


    @Test
    public void testConnection() throws Exception
    {
        List<String> pats = getPatients();
        assertTrue( pats != null );
        assertTrue( "Supposed to have >6 patients.  Received " + pats.size(), pats.size() >= 6);
    }


    @Test
    public void testGetPatient() throws Exception
    {
        JSONObject p = getPatient("TST900000101");
        log.finer(p.toString());
        assertFalse( p == null );
        assertTrue( p.toString().contains("TST900000101") );
        assertTrue( p.toString().contains("1955") );
        assertTrue( p.toString().contains("gender") );
    }

}
