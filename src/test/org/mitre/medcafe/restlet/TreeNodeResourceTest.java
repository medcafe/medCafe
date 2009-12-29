package test.org.mitre.medcafe.restlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mitre.medcafe.restlet.*;
import org.restlet.ext.json.*;
import org.restlet.representation.*;
import org.restlet.resource.*;

public class TreeNodeResourceTest {

    public final static String KEY = TreeNodeResourceTest.class.getName();
    public final static Logger log = Logger.getLogger( KEY );
    // static{log.setLevel(Level.FINER);}

    TreeNodeResource treeResource = null;

    /**
     * Sets up the test fixture.
     * (Called before every test case method.)
     */
    @Before
    public void setUp() {
        treeResource = new TreeNodeResource();
    }

    /**
     * Tears down the test fixture.
     * (Called after every test case method.)
     */
    @After
    public void tearDown() {
        treeResource = null;
    }

    /* @Test
    public void testCustomFormat() throws Exception
    {
        Representation result = treeResource.customFormat( test1, format1);
        assertEquals( "MedsphereVista", result.getText() );
    } */

    @Test
    public void testParseFields()
    {
        String input = "This is <: how :> we parse out <:fields:> from the formats in order to figure out which fields we need.";
        String[] parsedFields = treeResource.parseFields( input );
        assertEquals(2, parsedFields.length);
        for(String s : parsedFields )
        {
            // System.out.println(s);
            assertTrue(s.equals("how") || s.equals("fields"));
        }
    }

    @Test
    public void testProcess() throws Exception
    {
        //testing leaf node - pulling from an object
        JsonRepresentation test0 = new JsonRepresentation( "{\"name\":\"MedsphereVista\",\"type\":\"VistA\"} ");
        String format0 = "<:type:>";
        StringBuilder ret = new StringBuilder(  );
        String[] fields = treeResource.parseFields( format0 );
        String[] tokens = fields[0].split("\\.");
        treeResource.process(ret, format0, test0.getJsonObject(), tokens, new ArrayList<String>(), "");
        String answer = ret.toString();
        assertEquals( "VistA", answer );

        //testing leaf node - pulling from an array
        format0 = "<:name:>";
        test0 = new JsonRepresentation( "{\"name\":[\"MedsphereVista\",\"VAVista\"]}");
        ret = new StringBuilder(  );
        fields = treeResource.parseFields( format0 );
        tokens = fields[0].split("\\.");
        treeResource.process(ret, format0, test0.getJsonObject(), tokens, new ArrayList<String>(), "");
        answer = ret.toString();
        assertEquals( "MedsphereVistaVAVista", answer );

        //now adding depth
        JsonRepresentation test1 = new JsonRepresentation( "{\"repositories\":[{\"name\":\"MedsphereVista\",\"type\":\"VistA\"}, {\"name\":\"OurVista\",\"type\":\"VistA\"}]} ");
        String format1 = "<:repositories.name:>";
        ret = new StringBuilder(  );
        fields = treeResource.parseFields( format1 );
        tokens = fields[0].split("\\.");
        treeResource.process(ret, format1, test1.getJsonObject(), tokens, new ArrayList<String>(), "");
        answer = ret.toString();
        assertEquals( "MedsphereVistaOurVista", answer );

        //more complicated version.  JSON comes from http://www.json.org/example.html
        ret = new StringBuilder(  );
        JsonRepresentation test = new JsonRepresentation("{\"menu\": {" +
          "\"id\": \"file\"," +
          "\"value\": \"File\"," +
          "\"popup\": {" +
            "\"menuitem\": [" +
              "{\"value\": \"New\", \"onclick\": \"CreateNewDoc()\"}," +
              "{\"value\": \"Open\", \"onclick\": \"OpenDoc()\"}," +
              "{\"value\": \"Close\", \"onclick\": \"CloseDoc()\"}" +
            "]" +
          "}" +
        "}}");
        format1 = "<:menu.popup.menuitem.onclick:>";
        fields = treeResource.parseFields( format1 );
        tokens = fields[0].split("\\.");
        treeResource.process(ret, format1, test.getJsonObject(), tokens, new ArrayList<String>(), "");
        answer = ret.toString();
        assertEquals( "CreateNewDoc()OpenDoc()CloseDoc()", answer );

        //now exercising formats
        test1 = new JsonRepresentation( "{\"repositories\":[{\"name\":\"MedsphereVista\",\"type\":\"VistA\"}, {\"name\":\"OurVista\",\"type\":\"Other\"}]} ");
        format1 = "<:repositories.name:> is of type <:repositories.type:>\n";
        ret = new StringBuilder(  );
        fields = treeResource.parseFields( format1 );
        tokens = fields[0].split("\\.");
        treeResource.process(ret, format1, test1.getJsonObject(), tokens, Arrays.asList("type"), "");
        answer = ret.toString();
        assertEquals( "MedsphereVista is of type VistA\nOurVista is of type Other\n", answer );
    }
}

