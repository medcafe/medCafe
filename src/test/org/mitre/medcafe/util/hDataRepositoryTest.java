package test.org.mitre.medcafe.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.gson.*;
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

public class hDataRepositoryTest {

    public final static String KEY = hDataRepositoryTest.class.getName();
    public final static Logger log = Logger.getLogger( KEY );
        private static hDataRepository repo = null;
    // static{log.setLevel(Level.FINER);}

    /**
     * Sets up the test fixture.
     * (Called before every test case method.)
     */
    @Before
    public void setUp() throws Exception {
        boolean gotOne = false;
        HashMap<String, String> credMap = new HashMap<String, String>();
        String host = "192.168.56.102";
        if( InetAddress.getByName(host).isReachable(3000) )
        {
        		credMap.put("hostURL", "http://"+host);
        		credMap.put("port", "8080");

				repo = new hDataRepository(credMap);
				repo.setName("JeffhData");
            gotOne = true;
            
        }
        else
        {
            host="128.29.109.25";
            if( InetAddress.getByName(host).isReachable(3000) )
            {
    
                credMap.put("hostURL", "http://" + host);
                credMap.put("port", "8080");
               	repo = new hDataRepository(credMap);
               	repo.setName("OurHdata");
                gotOne = true;
            }
        }
        if (repo != null)
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
    @After
    public void tearDown() {

    }

    /*
    @Test
    public void testGetPatients() throws Exception
    {
        Map<String, String> pats = getPatients();
        assertTrue( pats != null );
        assertTrue( "Supposed to have >6 patients.  Received " + pats.size(), pats.size() >= 6);
        assertTrue( pats.keySet().contains("7"));
    }
    */
    @Test
    public void testGetPatient() throws Exception
    {
        Patient pat = repo.getPatient("00000000001");
        assertFalse( "Patient was not found.", pat == null );
        // JSONObject p = new JSONObject(pat);
        Gson gson = new Gson();
        String p = gson.toJson(pat);
        log.finer(p.toString());
        assertFalse( p == null );
        assertTrue( p.toString().contains("\"id\":\"00000000001\"") );
        assertTrue( p.toString().contains("Edinburgh") );
        assertTrue( p.toString().contains("male") );
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

