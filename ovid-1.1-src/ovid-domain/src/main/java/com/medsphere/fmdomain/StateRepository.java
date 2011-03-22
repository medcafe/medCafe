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
 
 package com.medsphere.fmdomain;

import java.util.HashMap;
import com.medsphere.ovid.domain.ov.OvidSecureRepository;
import com.medsphere.ovid.domain.ov.OvidDomainException;
import com.medsphere.fileman.FMResultSet;
import com.medsphere.fileman.FMQueryList;
import com.medsphere.resource.ResAdapter;
import com.medsphere.vistarpc.RPCConnection;
import com.medsphere.resource.ResException;

/**
 *
 * @author mgreer
 */
public class StateRepository extends OvidSecureRepository {

    private HashMap<String, FMState> stateList;
    private HashMap<Integer, FMState> stateCodeList;


    public StateRepository(RPCConnection conn) throws OvidDomainException {
        super(conn, conn);
        stateList = new HashMap<String, FMState>();
        stateCodeList = new HashMap<Integer, FMState>();
        try {

            ResAdapter adapter = obtainServerRPCAdapter();

            FMQueryList query = new FMQueryList(adapter, FMState.getFileInfoForClass());

            FMResultSet results = query.execute();
            if (results != null) {
                if (results.getError() != null) {
                    throw new OvidDomainException(results.getError());
                }
                while (results.next()) {
                    FMState state = new FMState(results);
                    stateList.put(state.getAbbrev(), state);
                    stateList.put(state.getName(),state);
                    stateCodeList.put(Integer.parseInt(state.getIEN()), state);
                }
            }
        } catch (OvidDomainException e) {
            throw e;
        } catch (ResException e) {
            throw new OvidDomainException(e.getMessage());
        }



    }

    public FMState getState(String abbrev) {
        return stateList.get(abbrev);
    }
    public FMState getStateFromCode(Integer code)
    {
        return stateCodeList.get(code);
    }
    public String toString()
    {
        String str = "";

        for (FMState state: stateList.values())
            str = str + state.toString() + "\n";
        return str;
    }
}
