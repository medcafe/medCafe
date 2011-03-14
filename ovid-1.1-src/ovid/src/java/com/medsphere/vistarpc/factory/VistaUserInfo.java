// <editor-fold defaultstate="collapsed">
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
// </editor-fold>


package com.medsphere.vistarpc.factory;

@Deprecated
class VistaUserInfo {

    private String server = null;
    private String port = null;
    private String accessCode = null;
    private String verifyCode = null;

    public VistaUserInfo(String server, String port, String accessCode, String verifyCode) {
        this.server = server;
        this.port = port;
        this.accessCode = accessCode;
        this.verifyCode = verifyCode;
    }

    public String getServer() {
        return server;
    }

    public String getPort() {
        return port;
    }

    public String getAccessCode() {
        return accessCode;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((accessCode == null) ? 0 : accessCode.hashCode());
        result = prime * result + ((port == null) ? 0 : port.hashCode());
        result = prime * result + ((server == null) ? 0 : server.hashCode());
        result = prime * result
                + ((verifyCode == null) ? 0 : verifyCode.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        VistaUserInfo other = (VistaUserInfo) obj;
        if (accessCode == null) {
            if (other.accessCode != null)
                return false;
        } else if (!accessCode.equals(other.accessCode))
            return false;
        if (port == null) {
            if (other.port != null)
                return false;
        } else if (!port.equals(other.port))
            return false;
        if (server == null) {
            if (other.server != null)
                return false;
        } else if (!server.equals(other.server))
            return false;
        if (verifyCode == null) {
            if (other.verifyCode != null)
                return false;
        } else if (!verifyCode.equals(other.verifyCode))
            return false;
        return true;
    }


}
