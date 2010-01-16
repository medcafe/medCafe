package test.org.mitre.medcafe.util;

import java.io.StringWriter;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.*;
import java.util.logging.*;
import org.junit.*;
import org.mitre.medcafe.util.*;
import org.json.JSONObject;
import org.json.JSONArray;

public class VelocityUtilTest
{

    public final static String KEY = VelocityUtilTest.class.getName();
    public final static Logger log = Logger.getLogger( KEY );
    static{log.setLevel(Level.FINER);}


    @Test
    public void testJson2Map()
        throws Exception
    {
        JSONObject obj = new JSONObject( "{\"years\":[{\"months\":[6,7,8,9,10,11,12],\"year\":2008},{\"months\":[1,2,3,4,5,6,7,8],\"year\":2009}]}");
        Map map = VelocityUtil.json2Map(obj);
        assertTrue(1==map.size());
        Object years = map.get("years");
        assertFalse( years == null );
        assertTrue( "Years is of type " + years.getClass().getName(), years instanceof List);
        List l = (List) years;
        Object x = l.get(0);
        assertTrue( x instanceof Map);
        Object year = ((Map)x).get("year");
        assertEquals("2008", String.valueOf(year));
        Object months = ((Map)x).get("months");
        assertTrue( months instanceof List);
        assertTrue( ((List)months).size() == 7);
    }

    @Test
    public void testApplyTemplate()
        throws Exception
    {
        JSONObject obj = new JSONObject( "{\"years\":[{\"months\":[6,7,8,9,10,11,12],\"year\":2008},{\"months\":[1,2,3,4,5,6,7,8],\"year\":2009}]}");
        StringWriter writer = new StringWriter();
        VelocityUtil.applyTemplate( obj, "templates/hellojson.vm", writer);
        String results = writer.toString();
        assertTrue(results.contains("<optgroup label=\"2009\">"));
        assertTrue(results.contains("<option value=\"8/2009\">8/2009</option>"));

    }

    /**
     * Sets up the test fixture.
     * (Called before every test case method.)
     */
    @Before
    public void setUp() throws Exception {
    }


    /**
     * Tears down the test fixture.
     * (Called after every test case method.)
     */
    @After
    public void tearDown() {
    }



}
