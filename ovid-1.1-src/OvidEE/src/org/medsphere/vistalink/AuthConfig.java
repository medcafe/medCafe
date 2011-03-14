/*
 * Copyright (C) 2007-2009  Medsphere Systems Corporation
 * All rights reserved.
 *
 * This source code contains the intellectual property
 * of its copyright holder(s), and is made available
 * under a license. If you do not know the terms of
 * the license, please stop and do not read further.
 *
 * Please read LICENSES for detailed information about
 * the license this source code file is available under.
 * Questions should be directed to legal@medsphere.com
 *
 */
package org.medsphere.vistalink;

import java.util.HashMap;
import java.util.Map;

import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;

class AuthConfig extends Configuration {

    private AppConfigurationEntry[] vistaConfig = new AppConfigurationEntry[1];

    AuthConfig(String ip, String port) {
        vistaConfig[0] = getAppConfigurationEntry(ip, port);
    }

    @Override
    public AppConfigurationEntry[] getAppConfigurationEntry(String name) {
        // don't care about the name... always return same entry
        return vistaConfig;
    }

    @Override
    public void refresh() {
        // nothing to implement
    }

    /**
     * return an JAAS-compatible, VistaLink-compatible application configuration entry for a given ip and port.
     * @param ip
     * @param port
     * @return
     */
    private AppConfigurationEntry getAppConfigurationEntry(String ip, String port) {

        Map<String, String> optionMap = new HashMap<String, String>();
        optionMap.put("gov.va.med.vistalink.security.ServerAddressKey", ip);
        optionMap.put("gov.va.med.vistalink.security.ServerPortKey", port);
        //TODO need SPI key?
        AppConfigurationEntry myEntry = new AppConfigurationEntry("gov.va.med.vistalink.security.VistaLoginModule",
                AppConfigurationEntry.LoginModuleControlFlag.REQUISITE, optionMap);
        return myEntry;
    }
}
