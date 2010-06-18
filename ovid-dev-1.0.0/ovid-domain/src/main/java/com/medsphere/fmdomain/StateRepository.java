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
