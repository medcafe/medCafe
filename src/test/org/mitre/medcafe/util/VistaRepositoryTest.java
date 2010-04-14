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
import org.projecthdata.hdata.schemas._2009._06.medication.*;

public class VistaRepositoryTest {

    public final static String KEY = VistaRepositoryTest.class.getName();
    public final static Logger log = Logger.getLogger( KEY );
    // static{log.setLevel(Level.FINER);}
    private static VistaRepository repo = null;

    /**
     * Sets up the test fixture.
     * (Called before every test case method.)
     */
    @BeforeClass
    public static void setUp() throws Exception {
        repo = new VistaRepository();
        boolean gotOne = false;
        String host = "192.168.56.101";

        if( InetAddress.getByName(host).isReachable(3000) )
        {
            repo.setName("JeffVista");
            repo.setCredentials( host, "8002", "OV1234", "OV1234!!" );
            // setCredentials( host, "8002", "PU1234", "PU5678!!" );
            gotOne = true;
        }
        else
        {
            host = "128.29.109.7";  //"medcafe.mitre.org";
            if( InetAddress.getByName(host).isReachable(3000) )
            {
                repo.setName("OurVista");
                repo.setCredentials( "128.29.109.7", "8002", "OV1234", "OV1234!!" );
                gotOne = true;
            }
        }
        log.finer( "Using " + repo.getName() );

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
    @AfterClass
    public static void tearDown() {
        repo.onShutdown();
    }

    @Test
    public void persistantConnectionTest() throws Exception
    {
        long start = System.currentTimeMillis();
        Map<String, String> pats = repo.getPatients();
        assertFalse(pats == null );
        log.finer( "first pull: " + (System.currentTimeMillis() - start ) / 1000);
        pats = repo.getPatients();
        assertFalse(pats == null );
        log.finer( "total after 2 pulls: " + (System.currentTimeMillis() - start ) / 1000);
        Patient pat = repo.getPatient("7");
        log.finer( "after grabbing patient #7 " + (System.currentTimeMillis() - start ) / 1000);
    }

    @Test
    public void testGetPatients() throws Exception
    {
        Map<String, String> pats = repo.getPatients();
        assertTrue( pats != null );
        assertTrue( "Supposed to have >6 patients.  Received " + pats.size(), pats.size() >= 6);
        assertTrue( pats.keySet().contains("7"));
    }

    @Test
    public void testGetPatient() throws Exception
    {
        Patient pat = repo.getPatient("7");
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
        List<Allergy> list = repo.getAllergies("7");
        assertFalse( "Nothing returned from getAllergies()" , list == null );
        assertTrue( "need some allergies in there", list.size() > 0 );
        Gson gson = new Gson();
        for( Allergy item : list )
        {
            log.finer(gson.toJson(item));
        }
    }

    @Test
    public void testGetMedications() throws Exception
    {
        List<Medication> list = repo.getMedications("7");
        assertFalse( "Nothing returned from getMedications()" , list == null );
        assertTrue( "need some medications in there", list.size() > 0 );
        Gson gson = new Gson();
        for( Medication item : list )
        {
            log.finer(gson.toJson(item));
        }
    }
}

