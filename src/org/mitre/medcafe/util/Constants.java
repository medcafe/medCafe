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
   public static String CONFIG_DIR = BASE_PATH + "WEB-INF/";
	public static final String COPY_TEMPLATE = "copyTemplate";
	public static final String POPULATE_TEXT = "popText";

	public static final String GENERAL_WIDGETS = "general_widgets";
	public static final String PATIENT_WIDGETS = "patient_widgets";
	public static final String INTERNAL_WIDGETS = "internal_widgets";
	public static final String OUR_VISTA = "OurVista";
	public static final String HDATA = "OurHdata";

	public static final String WEST_PANE = "west";
	public static final String EAST_PANE = "east";
	public static final String NORTH_PANE = "north";
	public static final String SOUTH_PANE = "south";
	public static final String CENTER_PANE = "center";

	public static final String DEFAULT_PATIENT = "1";
	public static final String  DEFAULT_REPOSITORY="OurVista";
	public static final String  LOCAL_REPOSITORY="local";

	public static final String PATIENT_ID = "patient_id";
	public static final String DEFAULT_SCRIPT = "addDefaultTable";
	public static final String DEFAULT_CLICK_URL = "defaultJSON.jsp";
	public static final boolean DEFAULT_JSON_PROCESS = true;
	public static final boolean DEFAULT_IS_INETTUTS = true;
	public static final String DEFAULT_SCRIPT_FILE = "medCafe.default.js";
	public static final String DEFAULT_CSS_THEME = "css/custom-theme/jquery-ui-1.8.6.custom.css";
	public static final String CSS_THEME = "css_theme";
	public static final String CSS_WIDGET = "css_widget";
	public static final String CSS_WIDGET_FILE = "widget-colors.css";
	
}
