package com.medsphere.ovid.security;

import gov.va.med.vistalink.security.CallbackHandlerUnitTest;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.apache.log4j.Logger;

import com.medsphere.vistalink.VistaLinkRPCConnection;
import com.medsphere.vistarpc.RPCConnection;
import gov.va.med.vistalink.security.VistaKernelPrincipalImpl;
import gov.va.med.vistalink.security.VistaLinkTokenBasedCallbackHandler;
import javax.security.auth.callback.CallbackHandler;

public class VistaLinkAuthenticator extends UserAuthenticator {
    private Logger logger = Logger.getLogger(RPCBrokerAuthenticator.class);
    private static VistaLinkAuthenticator _instance = null;
    private VistaLinkAuthenticator() {
    }

    public static UserAuthenticator getInstance() {
        if (_instance == null) {
            _instance = new VistaLinkAuthenticator();
        }
        return _instance;
    }

    @Override
    public boolean authenticate(Subject subject, RPCConnectionProperties properties) {
        String jaasName = properties.get("jaasVistaLinkName");
        String accessCode = properties.get("vistaAccessCode");
        String verifyCode = properties.get("vistaVerifyCode");
        String token = properties.get("token");
        if (jaasName==null || accessCode==null || verifyCode==null) {
            logger.error("JAAS name, access code, and verify code were not all specified");
            return false;
        }
        System.out.println("authenticating " + jaasName + " subject " + subject);
        try {
            CallbackHandler callbackHandler;
            if (token!=null) {
                callbackHandler = new VistaLinkTokenBasedCallbackHandler(token, "");
            } else {
                callbackHandler = new CallbackHandlerUnitTest(accessCode, verifyCode, "");
            }
            final LoginContext lc = new LoginContext(jaasName, subject, callbackHandler);
            lc.login();

            VistaKernelPrincipalImpl vkPrincipal = VistaKernelPrincipalImpl.getKernelPrincipal(lc.getSubject());
            RPCConnection conn = new VistaLinkRPCConnection(vkPrincipal.getAuthenticatedConnection(), lc);
            VistaConnectionPrincipal principal = new VistaConnectionPrincipal(conn);
            subject.getPrincipals().add(principal);
         } catch (LoginException ex) {
            System.out.println("authentication error: " + ex.getMessage());
            logger.error(ex);
            return false;
        } catch (Exception e) {
            System.out.println("error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;

    }

}
