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
package gov.va.med.vistalink.security;

import gov.va.med.vistalink.security.m.SecurityVOSetupAndIntroText;
import gov.va.med.vistalink.security.m.VistaInstitutionVO;

import java.util.Iterator;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.UnsupportedCallbackException;

public class VistaLinkTokenBasedCallbackHandler extends CallbackHandlerBase {

    private static final Logger logger = Logger.getLogger(CallbackHandlerUnitTest.class.toString());
    private String token;
    private String divisionIen = "";

    /**
     * Creates a simple callback handler that handles the callbacks for logon
     * (w/av code) and post-sign-in text only. It does not handle callbacks for
     * error, select division, or change verify code..
     *
     * @param token
     *            Token to use for logon
     * @param divisionIen
     *            IEN of division to select for multidivisional logins. If not
     *            needed, pass an empty string.
     */
    public VistaLinkTokenBasedCallbackHandler(String token, String divisionIen) {

        super();

        this.token = token;
        this.divisionIen = divisionIen;

    }

    /**
     * Does the change verify code callback
     *
     * @param cvcCallback
     *            change verify code callback
     */
    void doCallbackChangeVc(CallbackChangeVc cvcCallback) throws UnsupportedCallbackException {
        this.doUnsupportedCallback(cvcCallback);
    }

    /**
     * Does the confirm callback
     *
     * @param ccCb
     *            confirm callback
     */
    void doCallbackConfirm(CallbackConfirm confirmCallback) {

        String messageText = confirmCallback.getDisplayMessage();
        // since it's an error, let's display it for quicker troubleshooting
        // (assuming error priority logging is enabled)
        logger.log(Level.SEVERE, messageText);
        confirmCallback.setSelectedOption(CallbackConfirm.KEYPRESS_OK);
    }

    /**
     * Does the logon callback
     *
     * @param logonCallback
     */
    void doCallbackLogon(CallbackLogon logonCallback) {

        SecurityVOSetupAndIntroText setupInfo = logonCallback.getSetupAndIntroTextInfo();
        logger.log(Level.FINE, setupInfo.toString());
        String introText = setupInfo.getIntroductoryText();
        logger.log(Level.FINE, introText);
        logonCallback.setToken(token);
        logonCallback.setSelectedOption(CallbackLogon.KEYPRESS_OK);
    }

    /**
     * Does the select division callback
     *
     * @param divisionCallback
     */
    void doCallbackSelectDivision(CallbackSelectDivision divisionCallback) {

        logger.log(Level.FINE, "Returned divisions: ");
        TreeMap divisionList = (TreeMap) divisionCallback.getDivisionList();
        for (Iterator it = divisionList.keySet().iterator(); it.hasNext();) {
            String divisionNumber = (String) it.next();
            VistaInstitutionVO myDivision = (VistaInstitutionVO) divisionList.get(divisionNumber);
            logger.log(Level.FINE, "Division IEN: " + myDivision.getIen() + " Name: " + myDivision.getName() + " Number: "
                    + myDivision.getNumber());
        }
        divisionCallback.setSelectedDivisionIen(divisionIen);
        divisionCallback.setSelectedOption(CallbackSelectDivision.KEYPRESS_OK);

    }

    /**
     * Does the commit callback
     *
     * @param commitCallback
     */
    void doCallbackCommit(CallbackCommit commitCallback) {
        logger.log(Level.FINE, "CallbackHandlerSwing: starting CallbackCommit");
        // do nothing
    }

    /**
     * processes unsupported callbacks
     *
     * @param callback
     *            the unsupported callback
     * @throws UnsupportedCallbackException
     *             thrown as part of the processing of an unsupported callback
     */
    void doUnsupportedCallback(Callback callback) throws UnsupportedCallbackException {

        String errMsg = "Unsupported callback: '" + callback.getClass() + "'";
        UnsupportedCallbackException e = new UnsupportedCallbackException(callback, errMsg);
        logger.log(Level.SEVERE, errMsg, e);
        throw e;
    }
}
