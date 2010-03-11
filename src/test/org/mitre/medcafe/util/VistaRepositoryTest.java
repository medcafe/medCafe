package test.org.mitre.medcafe.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.gson.*;
import gov.va.med.vistalink.adapter.cci.VistaLinkConnection;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;
import org.junit.*;
import org.mitre.medcafe.restlet.*;
import org.mitre.medcafe.util.*;
import org.projecthdata.hdata.schemas._2009._06.allergy.*;
import org.projecthdata.hdata.schemas._2009._06.core.*;
import org.projecthdata.hdata.schemas._2009._06.patient_information.*;


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
            // setCredentials( host, "8002", "PU1234", "PU5678!!" );
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
        // long start = System.currentTimeMillis();
        // boolean success = setConnection();
        // log.finer( "Setting connection took: " + (System.currentTimeMillis() - start)/1000 + " seconds.  Successful? " + success);
        // assertTrue( success );
        // closeConnection();
    }


    /**
     * Tears down the test fixture.
     * (Called after every test case method.)
     */
    @After
    public void tearDown() {

    }

    @Test
    public void testGetPatients() throws Exception
    {
        Map<String, String> pats = getPatients();
        assertTrue( pats != null );
        assertTrue( "Supposed to have >6 patients.  Received " + pats.size(), pats.size() >= 6);
        assertTrue( pats.keySet().contains("7"));
    }

    @Test
    public void testGetPatient() throws Exception
    {
        Patient pat = getPatient("7");
        assertFalse( "Patient was not found.", pat == null );
        // JSONObject p = new JSONObject(pat);
        Gson gson = new Gson();
        String p = gson.toJson(pat);
        log.finer(p.toString());
        assertFalse( p == null );
        assertTrue( p.toString().contains("\"id\":\"7\"") );
        assertTrue( p.toString().contains("1975") );
        assertTrue( p.toString().contains("FEMALE") );
    }

    @Test
    public void testGetAllergies() throws Exception
    {
        List<Allergy> list = getAllergies("7");
        assertFalse( "Nothing returned from getAllergies()" , list == null );
        assertTrue( "need some allergies in there", list.size() > 0 );
        Gson gson = new Gson();
        for( Allergy item : list )
        {
            log.finer(gson.toJson(item));
        }
    }

}
