package org.mitre.medcafe.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;

/*
 * A class to store system wide constants
 */
public class Constants {
	
	public static final String NEWLINE =System.getProperty("line.separator");

	public final static String FILE_SEPARATOR = System.getProperty("file.separator");
	
	public final static String RUN_DIR = System.getProperty("user.dir");

	public final static String EMPTY_STR = "";

	
	public final static String MEDCAFE_USER ="medcafe.user";
	public static String BASE_PATH ="";

	public static final String COPY_TEMPLATE = "copyTemplate";
	public static final String POPULATE_TEXT = "popText";
	
	public static final String GENERAL_WIDGETS = "general_widgets";
	public static final String PATIENT_WIDGETS = "patient_widgets";
	public static final String OUR_VISTA = "OurVista";
	
	public static final String WEST_PANE = "west";
	public static final String EAST_PANE = "east";
	public static final String NORTH_PANE = "north";
	public static final String SOUTH_PANE = "south";
	public static final String CENTER_PANE = "center";
	
}