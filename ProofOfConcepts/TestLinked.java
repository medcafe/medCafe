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

/**
 *  Global location for configurations. Also contains some useful constants.
 */
 public class TestLinked
{
    public static void main( String[] args )
        throws Exception
    {
        InputStream in = new FileInputStream( args[0] );
        Properties config = new Properties();
        config.load( in );
        in.close();
        /* reorder the Properties object so they are in the order in the list - TODO - run test.  */
        Map m = new LinkedHashMap(config);
        for( Object a : m.keySet() )
        {
            System.out.println(a + "=" + m.get(a));
        }

    }
}
