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

import java.util.TimerTask;
import java.util.logging.Logger;
import java.util.logging.Level;

public class RepositoryTimerTask extends TimerTask {

 public final static String KEY = DbConnection.class.getName();
    public final static Logger log = Logger.getLogger( KEY );
    //static{log.setLevel(Level.FINER);}
    
    private Repository repo;
    public RepositoryTimerTask(Repository repo)
    {
        super();
        this.repo = repo;
    }
    public void run()
    {
			log.finer("RepositoryTimerTask: updating lookup tables");
        repo.updateLookupTables();

    }
}
