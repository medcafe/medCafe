/*
 *  Copyright 2008 The MITRE Corporation (http://www.mitre.org/). All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.beans.*;
import java.io.*;
import java.util.*;
import java.util.regex .*;
import java.io.StringWriter;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.json.*;


/**
 *  Global location for configurations. Also contains some useful constants.
 */
 public class TestVelocity
{
    public static void main( String[] args )
        throws Exception
    {
        TestVelocity v = new TestVelocity();
        // v.run();
        // v.run2();
        v.test();
    }

    public void test() throws Exception
    {
        JSONObject obj = new JSONObject( "{\"years\":[{\"months\":[6,7,8,9,10,11,12],\"year\":2008},{\"months\":[1,2,3,4,5,6,7,8],\"year\":2009}]}");
        JSONArray arr = obj.getJSONArray("years");
        obj = arr.getJSONObject(1);
        Iterator keys = obj.keys();
        while(keys.hasNext())
        {
            String x = (String)keys.next();
            Object o = obj.get(x);
            System.out.println(x + ":" +o.getClass().getName());
        }
    }

    public void run() throws Exception
    {
        /*  first, get and initialize an engine  */
        VelocityEngine ve = new VelocityEngine();
        ve.init();
        /*  next, get the Template  */
        Template t = ve.getTemplate( "helloworld.vm" );
        /*  create a context and add data */
        VelocityContext context = new VelocityContext();
        context.put("name", "World");
        /* now render the template into a StringWriter */
        StringWriter writer = new StringWriter();
        t.merge( context, writer );
        /* show the World */
        System.out.println( writer.toString() );
    }

    public void run2() throws Exception
    {
        /*  first, get and initialize an engine  */
        VelocityEngine ve = new VelocityEngine();
        ve.init();
        /*  next, get the Template  */
        Template t = ve.getTemplate( "hellojson.vm" );
        /*  create a context and add data */
        VelocityContext context = new VelocityContext();

        // JSONObject obj = new JSONObject( "{\"years\":[{\"months\":[6,7,8,9,10,11,12],\"year\":2008},{\"months\":[1,2,3,4,5,6,7,8],\"year\":2009}]}");
        // {"years":[{"months":[6,7,8,9,10,11,12],"year":2008},{"months":[1,2,3,4,5,6,7,8],"year":2009}]}
        // context.put("data", obj);

        List mapdata = new ArrayList();
        Map sub = new HashMap();
        sub.put("year", "2008");
        List m = Arrays.asList(6,7,8,9,10,11,12);
        sub.put("months", m);
        mapdata.add( sub );

        sub = new HashMap();
        sub.put("year", "2009");
        m = Arrays.asList(1,2,3,4,5,6,7,8);
        sub.put("months", m);
        mapdata.add( sub );

        context.put("years", mapdata);

        /* now render the template into a StringWriter */
        StringWriter writer = new StringWriter();
        t.merge( context, writer );
        /* show the World */
        System.out.println( writer.toString() );
    }


}
