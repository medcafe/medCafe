package org.mitre.medcafe.restlet;

import com.medsphere.ovid.model.domain.patient.*;
import java.util.*;
import org.mitre.medcafe.util.*;
import java.net.*;

/**
 *  Collection of all Repositories
 */
public class Repositories
{

    protected static Map<String, Repository> repos = new HashMap<String, Repository>();
    protected static final int TIMEOUT = 8000; // I recommend 3 seconds at least

    public Repositories()
    {

    }

    public static Map<String, Repository> getRepositories()
    {
        return Collections.unmodifiableMap(repos);
    }

    /**
     *  Repositories for testing - until an external representation is arrived at, anyway
     */
    public static void setDefaultRepositories()
    {
        repos = new HashMap<String, Repository>();
        Repository r = new VistaRepository();
        r.setName("JeffVista");
        String host = "192.168.56.101";
        r.setCredentials( host, "9201", "SM1234", "SM1234!!" );
        // r.setCredentials( host, "8002", "PU1234", "PU5678!!" );
        try
        {
            if( InetAddress.getByName(host).isReachable(TIMEOUT) )
            {
                repos.put(r.getName(), r);
            }
        }catch (Exception e) {}

        host = "openvista.medsphere.org";
        try
        {
        if( InetAddress.getByName(host).isReachable(TIMEOUT) )
        {
            r = new VistaRepository();
            r.setName("MedsphereVista");
            r.setCredentials( host, "8002", "OV1234", "OV1234!!" );
            repos.put(r.getName(), r);
        }
        }catch (Exception e) {}

        host = "medcafe.mitre.org";
        try
        {
            if( InetAddress.getByName(host).isReachable(TIMEOUT) )
            {
                r = new VistaRepository();
                r.setName("OurVista");
                r.setCredentials( "128.29.109.7", "9201", "OV1234", "OV1234!!" );
                repos.put(r.getName(), r);
		System.out.println("Got ourVista connection");
            }
        }catch (Exception e) {System.out.println(e.getMessage());}

        host = "192.168.56.102";
        try
        {
            if( InetAddress.getByName(host).isReachable(TIMEOUT) )
            {
                r = new hDataRepository( );
                r.setName("JeffhData");
                r.setCredentials( "http://" + host + ":8080" );
                repos.put(r.getName(), r);
            }
        }catch (Exception e) {}

        host="medcafe-hdata.mitre.org";
        try
        {
            if( InetAddress.getByName(host).isReachable(TIMEOUT) )
            {
                r = new hDataRepository( );
                r.setName("OurHdata");
                r.setCredentials( "http://" + host + ":8080" );
                repos.put(r.getName(), r);
		System.out.println("Got hdata connection");
            }
        }catch (Exception e) { System.out.println(e.getMessage());}

    }

    public static void onShutdown()
    {
        for( Repository r : repos.values() )
        {
            r.onShutdown();
        }

    }


    public static List<String> getRepositoryNames()
    {
        return new ArrayList(repos.keySet());
    }


    public static Repository getRepository(String name)
    {
        return repos.get(name);
    }
}
