package org.mitre.medcafe.restlet;

import java.util.*;
import org.mitre.medcafe.util.*;
import java.net.*;

/**
 *  Collection of all Repositories
 */
public class Repositories
{

    protected static Map<String, Repository> repos = new HashMap<String, Repository>();
    protected static final int TIMEOUT = 5000; // I recommend 3 seconds at least

    public Repositories()
    {

    }

    /**
     *  Repositories for testing - until an external representation is arrived at, anyway
     */
    public static void setDefaultRepositories()
    {
        repos = new HashMap<String, Repository>();
        Repository r = new VistaRepository();
        r.setName("JeffVista");
        String host = "172.16.52.130";
        r.setCredentials( host, "8002", "OV1234", "OV1234!!" );
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

        host = "mmrc.mitre.org";
        try
        {
            if( InetAddress.getByName(host).isReachable(TIMEOUT) )
            {
                r = new VistaRepository();
                r.setName("OurVista");
                r.setCredentials( "128.29.109.7", "8002", "OV1234", "OV1234!!" );
                repos.put(r.getName(), r);
            }
        }catch (Exception e) {}
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
