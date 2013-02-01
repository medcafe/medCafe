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
package org.mitre.medcafe.model;

import java.io.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import java.util.*;

import org.json.JSONException;
import org.json.JSONObject;

import org.mitre.medcafe.util.DbConnection;
import org.mitre.medcafe.util.DatabaseUtility;
import org.mitre.medcafe.util.WebUtils;
/**
 *  Representation of the text data
 *  @author: Gail Hamilton
 */
public class Image
{
	//"images":
	//[{"id":"Discharge Summary",
	//"category":"Smoker",
	//"source":"176_NON-SMOKER.pdf",
	//"param":"127.0.0.1:8080/medcafe/images/patients/1/176_NON-SMOKER.pdf",
	//"name":"Discharge Summary",
	//"file_date":"2010-05-14","thumb":"176_NON-SMOKER_thumb.png"}
	
	private String id ="";
	private String category ="";
	private String source ="";
	private String param ="";
	
	private String name ="";
	private String file_date ="";
	private String thumb = "";
	
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getParam() {
		return param;
	}
	public void setParam(String param) {
		this.param = param;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFile_date() {
		return file_date;
	}
	public void setFile_date(String file_date) {
		this.file_date = file_date;
	}
	
	public static String getDivs(Image image)
	{

		StringBuffer strBuf = new StringBuffer();
		strBuf.append("<div class=\"item\">");
        strBuf.append("<a class=\"item\" href=\" "+ image.getParam() + " \"><img class=\"content\" src=\"" + image.getThumb() +  "\" /></a>");
        strBuf.append("<img class=\"content\" href=\"" + image.getSource()+ "\" src=\"" + image.getThumb()+"\"/>");
        strBuf.append("<div class=\"caption\">" + image.getName()+"</div> </div>");
        
        return strBuf.toString();
	}
	public String getThumb() {
		return thumb;
	}
	public void setThumb(String thumb) {
		this.thumb = thumb;
	}
}