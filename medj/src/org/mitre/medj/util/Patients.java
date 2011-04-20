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
package org.mitre.medj.util;


import java.util.*;
import java.util.logging.Logger;
import org.mitre.medj.jaxb.ContinuityOfCareRecord;

/**
 *  Collection of all Repositories
 */
public class Patients
{

    protected static Map<String, ContinuityOfCareRecord> patients = new HashMap<String, ContinuityOfCareRecord>();
    protected static final int TIMEOUT = 8000; // I recommend 3 seconds at least
	
	public final static String KEY = Patients.class.getName();
    public final static Logger log = Logger.getLogger( KEY );
    // static{log.setLevel(Level.FINER);}
    public Patients()
    {
    }

    public static Map<String, ContinuityOfCareRecord> getPatients()
    {
        return Collections.unmodifiableMap(patients);
    }

  
    public static List<String> getPatientNames()
    {
        return new ArrayList<String>(patients.keySet());
    }


    public static ContinuityOfCareRecord getCCR(String name)
    {
        return patients.get(name);
    }
    
    public static void addPatient(String patientId, ContinuityOfCareRecord ccr)
    {
    	patients.put(patientId, ccr);
    }
}
