package test.org.mitre.schemastore.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mitre.medcafe.restlet.*;
import org.restlet.ext.json.*;
import org.restlet.representation.*;
import org.restlet.resource.*;

public class TreeNodeResourceTest {

    JsonRepresentation test1 = "";
    String format1 = "";
    String result1 = "";
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

    @Test
    public void testCustomFormat() {
        assertEquals( result1, format1 );
    }

    @Test
    public void testParseFields()
    {
        String input = "This is <: how :> we parse out <:fields:> from the formats in order to figure out which fields we need."
        Set<String> parsedFields = treeResource.parseFields( input );
        assertEquals(2, parsedFields.size());
        assertTrue(parsedFields.contains("how"));
        assertTrue(parsedFields.contains("fields"));
    }

//     private void testProposed( MappingCell proposed )
//     {
//         assertEquals(1, proposed.getId().intValue());
//         assertEquals(13, proposed.getMappingId().intValue());
//         assertEquals(49, proposed.getInput()[0].intValue());
//         assertEquals(72, proposed.getOutput().intValue());
//         assertEquals(new Double(0.5), proposed.getScore());
//         assertEquals("Author", proposed.getAuthor());
//         assertEquals("notes", proposed.getNotes());
//         assertEquals("org.mitre.schemastore.model.mapfunctions.IdentityFunction", proposed.getFunctionClass());
//         assertFalse( proposed.getValidated() );
//     }
}

