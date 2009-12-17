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
package org.mitre.medcafe.util;


import javax.servlet.ServletRequest;
import javax.servlet.jsp.PageContext;


/**
 *  Miscellaneous utils for use in JSPs and for other web-based areas of the
 *  code.
 *
 * @author     jchoyt
 * @created    April 22, 2004
 */
public class WebUtils
{
    /**
     *  Retrieves a parameter from a passed request. If the parameter is not
     *  found, an NPE is thrown.
     *
     * @param  req                       Request object to look in
     * @param  name                      the name of the field from the html
     *      form
     * @return                           the value from the html form
     *      corresponding to the name parameter
     * @exception  NullPointerException  thrown if the name was not in the html
     *      form
     */
    public static String getRequiredParameter( ServletRequest req, String name )
        throws NullPointerException
    {
        String  ret  = req.getParameter( name );

        if ( ret == null )
        {
            throw new NullPointerException( "This form requires a \"" + name + "\" parameter, which was missing from the submitted request." );
        }
        return ret;
    }


    /**
     *  Retrieves a list of parameters from a passed request in comma delimited
     *  format. If the parameter is not found, an NPE is thrown.
     *
     * @param  req                       Request object to look in
     * @param  name                      the name of the field from the html
     *      form
     * @return                           the value from the html form
     *      corresponding to the name parameter
     * @exception  NullPointerException  thrown if the name was not in the html
     *      form
     */
    public static String getRequiredParameterValues( ServletRequest req, String name )
        throws NullPointerException
    {
        String[]      values  = req.getParameterValues( name );

        if ( values == null )
        {
            throw new NullPointerException( "This form requires a \"" + name + "\" parameter, which was missing from the submitted request." );
        }

        StringBuffer  ret     = new StringBuffer();

        for ( int i = 0; i < values.length; i++ )
        {
            if ( i > 0 )
            {
                ret.append( "," );
            }
            ret.append( values[i] );
        }
        return ret.toString();
    }


    /**
     *  Gets the optionalParameter attribute of the WebUtils class
     *
     * @param  req                       The servlet request object to pull it
     *      out of.
     * @param  name                      Name of the Parameter
     * @param  defalt                    The default value (yes, defalt is
     *      spelled incorrectly - default is a keyword
     * @return                           The optionalParameter value
     * @exception  NullPointerException  Description of the Exception
     */
    public static String getOptionalParameter( ServletRequest req, String name, String defalt )
        throws NullPointerException
    {
        String  ret  = req.getParameter( name );

        if ( ret == null )
        {
            ret = defalt;
        }
        return ret;
    }

  /**
     *  Retrieves a list of parameters from a passed request in comma delimited
     *  format. If the parameter is not found, an NPE is thrown.
     *
     * @param  req                       Request object to look in
     * @param  name                      the name of the field from the html
     *      form
     * @return                           the value from the html form
     *      corresponding to the name parameter
     * @exception  NullPointerException  thrown if the name was not in the html
     *      form
     */
    public static String getOptionalParameterValues( ServletRequest req, String name )
        throws NullPointerException
    {
        String[] values = req.getParameterValues( name );

        if ( values == null )
        {
		return Config.EMPTY_STR;
	}

        StringBuffer  ret     = new StringBuffer();

        for ( int i = 0; i < values.length; i++ )
        {
            if ( i > 0 )
            {
                ret.append( "," );
            }
            ret.append( values[i] );
        }
        return ret.toString();
    }

    /**
     *  Gets the optionalParameter attribute of the WebUtils class
     *
     * @param  req                       Description of the Parameter
     * @param  name                      Description of the Parameter
     * @return                           The optionalParameter value
     * @exception  NullPointerException  Description of the Exception
     */
    public static String getOptionalParameter( ServletRequest req, String name )
        throws NullPointerException
    {
        return getOptionalParameter( req, name, Config.EMPTY_STR );
    }


    /**
     *  Gets the requiredAttribute attribute of the WebUtils class
     *
     * @param  page   Description of the Parameter
     * @param  name   Description of the Parameter
     * @param  scope  Description of the Parameter
     * @return        The requiredAttribute value
     */
    public static String getRequiredAttribute( PageContext page, String name, int scope )
    {
        Object  ret  = page.getAttribute( name, scope );

        if ( ret == null )
        {
            throw new NullPointerException( "This form requires a \"" + name + "\" parameter, which was missing from the submitted request." );
        }
        return ret.toString();
    }


    /**
     *  Gets the optionalAttribute attribute of the WebUtils class
     *
     * @param  page   Description of the Parameter
     * @param  name   Description of the Parameter
     * @param  scope  Description of the Parameter
     * @return        The optionalAttribute value
     */
    public static String getOptionalAttribute( PageContext page, String name, int scope )
    {
        Object  ret  = page.getAttribute( name, scope );

        if ( ret == null )
        {
            return "";
        }
        return ret.toString();
    }


    /**
     *  Validate the form of an email address. <P>
     *
     *  Return <code>true</code> only if
     *  <ul>
     *    <li> when parsed with a "@" delimiter, <code>aEmailAddress</code>
     *    contains two tokens
     *    <li> each token has a positive length, after trimming whitespace
     *    <li> the second token contains a period
     *  </ul>
     *  <P>
     *
     *  The second condition arises since local email addresses, simply of the
     *  form "albert", for example, are valid but almost always undesired.
     *
     * @param  aEmailAddress  Description of the Parameter
     * @return                The validEmailAddress value
     */
    public static boolean isValidEmailAddress( String aEmailAddress )
    {
        if ( aEmailAddress == null )
        {
            return false;
        }

        String[]  tokens  = aEmailAddress.split( "@" );

        return tokens.length == 2 &&
                    tokens[0].trim().length() > 0 &&
                    tokens[1].trim().length() > 0 &&
                    tokens[1].indexOf( "." ) != -1;
    }


    /**
     *  Calculates the display name of a datasource name (which is usually a file name)
     *
     * @param  name  The official name of the datasource (usually a filename)
     * @return       The display value for web pages
     */
    public static String getDatasourceDisplayName( String name )
    {
        if ( name.startsWith( "db_" ) )
        {
            return name.substring( 3, name.indexOf( '.' ) );
        }
        else
        {
            return name;
        }//main database configured in config.properties
    }

}

