/*
 *  Copyright 2010 The MITRE Corporation (http://www.mitre.org/). All Rights Reserved.
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
package org.mitre.medcafe.util;

import java.io.*;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex .*;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

/**
 *  Global location for configurations. Also contains some useful constants.
 *  @author: Jeffrey Hoyt
 */
public class Config
{

    public final static String KEY = Config.class.getName();
    public final static Logger log = Logger.getLogger( KEY );
    // static{log.setLevel(Level.FINER);}
    /**
     *  Description of the Field
     */
    public static final String EMPTY_STR = "";

    /**
     *  The new line symbol(s) for this OS - either \r, \n, or \r\n
     */
    public static final String NEWLINE = System.getProperty( "line.separator" );

    /**
     *  Map of
     */
    protected static Map<Pattern, String> formats = new LinkedHashMap<Pattern, String>();

    protected static String webapp = "/medcafe";

    public static final String FORMAT_FILE = "formats.props";

    /**
     *  Description of the Method
     *
     *@param  propertiesFileLocation  Description of Parameter
     *@since
     */
    public static void init( String propertiesFileLocation )
    {
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(propertiesFileLocation + FORMAT_FILE ));
            String line;
            while( (line = br.readLine()) != null )
            {
                line = line.trim();
                if( line.startsWith("#") )
                {  //comment
                    continue;
                }
                //TODO - trim off line-ending comments?
                int brk = line.indexOf('=');
                if( brk == -1 )
                {  //bad line, skip it
                    continue;
                }
                String pattern = line.substring(0, brk).trim();
                String output = line.substring(brk + 1).trim();
                try
                {
                    formats.put( Pattern.compile(pattern), output);
                    log.finer("Successfully compiled Pattern for |" + pattern + "|");
                }
                catch (PatternSyntaxException e)
                {  //bad regex
                    log.warning( "Bad regex found in formats.props: " + pattern );
                    //keep going, though
                }
            }
            br.close();
            // updateProxy();
            return;
        }
        catch ( Exception e )
        {
            log.throwing( KEY, "init", e );
        }
    }


    public static String getFormat(String relurl)
    {
        log.finer("Testing against: " + relurl );
        for( Pattern p : formats.keySet() )
        {
            Matcher m = p.matcher( relurl );
            if( m.matches() )
            {
                return new String(formats.get(p));
            }
            log.finer("Testing " + m.toString() + ": no match" );
        }
        return null;
    }

    /**
     *  Sets webapp base path (should always be "medcafe"
     */
    public static void setWebapp(String webapp)
    {
        Config.webapp = webapp;
    }

    public static String getWebapp()
    {
        return webapp;
    }

}
