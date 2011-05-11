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
package com.medsphere.ovid.domain.ov;

import java.util.HashMap;
import java.util.Map;

public enum DisplayGroupType {

    ALL_SERVICES("ALL SERVICES"),
    PHARMACY("PHARMACY"),
    INPATIENT_MEDICATIONS("INPATIENT MEDICATIONS"),
    OUTPATIENT_MEDICATIONS("OUTPATIENT MEDICATIONS"),
    LABORATORY("LABORATORY"),
    CHEMISTRY("CHEMISTRY"),
    HEMATOLOGY("HEMATOLOGY"),
    MICROBIOLOGY("MICROBIOLOGY"),
    GENERAL_RADIOLOGY("GENERAL RADIOLOGY"),
    DIETETICS("DIETETICS"),
    CONSULTS("CONSULTS"),
    VITALS_MEASUREMENTS("VITALS/MEASUREMENTS"),
    NURSING("NURSING"),
    ACTIVITY("ACTIVITY"),
    SURGERY("SURGERY"),
    SUMMARY_ORDER("SUMMARY ORDER"),
    MAS("M.A.S."),
    OTHER_HOSPITAL_SERVICES("OTHER HOSPITAL SERVICES"),
    ORDER_ENTRY_RESULTS_REPORTING("ORDER ENTRY/RESULTS REPORTING"),
    SUPPLIES_DEVICES("SUPPLIES/DEVICES"),
    UNIT_DOSE_MEDICATIONS("UNIT DOSE MEDICATIONS"),
    IV_MEDICATIONS("IV MEDICATIONS"),
    BLOOD_BANK("BLOOD BANK"),
    ANATOMIC_PATHOLOGY("ANATOMIC PATHOLOGY"),
    ELECTRON_MICROSCOPY("ELECTRON MICROSCOPY"),
    SURGICAL_PATHOLOGY("SURGICAL PATHOLOGY"),
    AUTOPSY("AUTOPSY"),
    CYTOLOGY("CYTOLOGY"),
    IMAGING("IMAGING"),
    CT_SCAN("CT SCAN"),
    MAGNETIC_RESONANCE_IMAGING("MAGNETIC RESONANCE IMAGING"),
    ANGIO_NEURO_INTERVENTIONAL("ANGIO/NEURO/INTERVENTIONAL"),
    CARDIOLOGY_STUDIES_NUC_MED("CARDIOLOGY STUDIES (NUC MED)"),
    NUCLEAR_MEDICINE("NUCLEAR MEDICINE"),
    ULTRASOUND("ULTRASOUND"),
    VASCULAR_LAB("VASCULAR LAB"),
    MAMMOGRAPHY("MAMMOGRAPHY"),
    DIET_ORDERS("DIET ORDERS"),
    TUBEFEEDINGS("TUBEFEEDINGS"),
    DIET_ADDITIONAL_ORDERS("DIET ADDITIONAL ORDERS"),
    EARLY_LATE_TRAYS("EARLY/LATE TRAYS"),
    PROCEDURES("PROCEDURES"),
    ALLERGIES("ALLERGIES"),
    DIAGNOSIS("DIAGNOSIS"),
    CONDITION("CONDITION"),
    PRECAUTIONS("PRECAUTIONS"),
    DIETETIC_CONSULTS("DIETETIC CONSULTS"),
    NON_VA_MEDICATIONS("NON-VA MEDICATIONS"),
    OUTPATIENT_MEALS("OUTPATIENT MEALS"),
    PARENTERAL_NUTRITION("PARENTERAL NUTRITION"),
    CLINIC_ORDERS("CLINIC ORDERS"),;
    private final String displayString;

    DisplayGroupType(String type) {
        this.displayString = type;

    }

    @Override
    public String toString() {
        return displayString;
    }
    /**
     * here is where you map incoming type strings to known types.  For example,
     * if "char*" needs to be mapped to a STRING, then add it to the list of
     * STRING datatype mappings.  Note that mappings are caseless.
     */
    private static Map<DisplayGroupType, String[]> typeMap = new HashMap<DisplayGroupType, String[]>() {

        {
            put(DisplayGroupType.ALL_SERVICES, new String[]{"all services", "all"});
            put(DisplayGroupType.PHARMACY, new String[]{"pharmacy", "meds", "rx"});
            put(DisplayGroupType.INPATIENT_MEDICATIONS, new String[]{"inpatient medications", "inpt. meds", "i rx"});
            put(DisplayGroupType.OUTPATIENT_MEDICATIONS, new String[]{"outpatient medications", "out. meds", "o rx"});
            put(DisplayGroupType.LABORATORY, new String[]{"laboratory", "lab"});
            put(DisplayGroupType.CHEMISTRY, new String[]{"chemistry", "chemistry", "ch"});
            put(DisplayGroupType.HEMATOLOGY, new String[]{"hematology", "hematology", "hema"});
            put(DisplayGroupType.MICROBIOLOGY, new String[]{"microbiology", "microbiology", "mi"});
            put(DisplayGroupType.GENERAL_RADIOLOGY, new String[]{"general radiology", "radiology", "rad"});
            put(DisplayGroupType.DIETETICS, new String[]{"dietetics", "diet", "diet"});
            put(DisplayGroupType.CONSULTS, new String[]{"consults", "consults", "cslt"});
            put(DisplayGroupType.VITALS_MEASUREMENTS, new String[]{"vitals/measurements", "vitals", "v/m"});
            put(DisplayGroupType.NURSING, new String[]{"nursing", "nursing", "nurs"});
            put(DisplayGroupType.ACTIVITY, new String[]{"activity", "activity", "act"});
            put(DisplayGroupType.SURGERY, new String[]{"surgery", "surgery", "surg"});
            put(DisplayGroupType.SUMMARY_ORDER, new String[]{"summary order", "sum"});
            put(DisplayGroupType.MAS, new String[]{"m.a.s.", "a/d/t", "adt"});
            put(DisplayGroupType.OTHER_HOSPITAL_SERVICES, new String[]{"other hospital services", "other", "other"});
            put(DisplayGroupType.ORDER_ENTRY_RESULTS_REPORTING, new String[]{"order entry/results reporting", "oe/rr", "oe/rr"});
            put(DisplayGroupType.SUPPLIES_DEVICES, new String[]{"supplies/devices", "supplies", "sply"});
            put(DisplayGroupType.UNIT_DOSE_MEDICATIONS, new String[]{"unit dose medications", "inpt. meds", "ud rx"});
            put(DisplayGroupType.IV_MEDICATIONS, new String[]{"iv medications", "infusion", "iv rx"});
            put(DisplayGroupType.BLOOD_BANK, new String[]{"blood bank", "blood bank", "bb"});
            put(DisplayGroupType.ANATOMIC_PATHOLOGY, new String[]{"anatomic pathology", "anat. path.", "ap"});
            put(DisplayGroupType.ELECTRON_MICROSCOPY, new String[]{"electron microscopy", "electron microscopy", "em"});
            put(DisplayGroupType.SURGICAL_PATHOLOGY, new String[]{"surgical pathology", "surg. path.", "sp"});
            put(DisplayGroupType.AUTOPSY, new String[]{"autopsy", "autopsy", "au"});
            put(DisplayGroupType.CYTOLOGY, new String[]{"cytology", "cytology", "cy"});
            put(DisplayGroupType.IMAGING, new String[]{"imaging", "imaging", "xray"});
            put(DisplayGroupType.CT_SCAN, new String[]{"ct scan", "ct scan", "ct"});
            put(DisplayGroupType.MAGNETIC_RESONANCE_IMAGING, new String[]{"magnetic resonance imaging", "mri", "mri"});
            put(DisplayGroupType.ANGIO_NEURO_INTERVENTIONAL, new String[]{"angio/neuro/interventional", "angio/neuro", "ani"});
            put(DisplayGroupType.CARDIOLOGY_STUDIES_NUC_MED, new String[]{"cardiology studies (nuc med)", "cardiology", "card"});
            put(DisplayGroupType.NUCLEAR_MEDICINE, new String[]{"nuclear medicine", "nuclear med", "nm"});
            put(DisplayGroupType.ULTRASOUND, new String[]{"ultrasound", "ultrasound", "us"});
            put(DisplayGroupType.VASCULAR_LAB, new String[]{"vascular lab", "vascular lab", "vas"});
            put(DisplayGroupType.MAMMOGRAPHY, new String[]{"mammography", "mammography", "mam"});
            put(DisplayGroupType.DIET_ORDERS, new String[]{"diet orders", "diet", "do"});
            put(DisplayGroupType.TUBEFEEDINGS, new String[]{"tubefeedings", "tubefeeding", "tf"});
            put(DisplayGroupType.DIET_ADDITIONAL_ORDERS, new String[]{"diet additional orders", "diet add'l", "d ao"});
            put(DisplayGroupType.EARLY_LATE_TRAYS, new String[]{"early/late trays", "early/late trays", "e/l t"});
            put(DisplayGroupType.PROCEDURES, new String[]{"procedures", "procedures", "proc"});
            put(DisplayGroupType.ALLERGIES, new String[]{"allergies", "allergy", "alg"});
            put(DisplayGroupType.DIAGNOSIS, new String[]{"diagnosis", "diagnosis", "dx"});
            put(DisplayGroupType.CONDITION, new String[]{"condition", "condition", "cond"});
            put(DisplayGroupType.PRECAUTIONS, new String[]{"precautions", "precautions", "prec"});
            put(DisplayGroupType.DIETETIC_CONSULTS, new String[]{"dietetic consults", "diet consult", "d con"});
            put(DisplayGroupType.NON_VA_MEDICATIONS, new String[]{"non-va medications", "home meds", "nv rx"});
            put(DisplayGroupType.OUTPATIENT_MEALS, new String[]{"outpatient meals", "outpt meal", "meal"});
            put(DisplayGroupType.PARENTERAL_NUTRITION, new String[]{"parenteral nutrition", "tpn", "tpn"});
            put(DisplayGroupType.CLINIC_ORDERS, new String[]{"clinic orders", "clinic orders", "c rx"});
        }
    };

    public static DisplayGroupType fromString(String type) {

        DisplayGroupType returnType = ALL_SERVICES;

        for (DisplayGroupType key : typeMap.keySet()) {
            for (String value : typeMap.get(key)) {
                if (value.equalsIgnoreCase(type)) {
                    returnType = key;
                    break;
                }
            }
        }

        return returnType;
    }
}
