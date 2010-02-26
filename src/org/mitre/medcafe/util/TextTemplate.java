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

import java.io.*;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex .*;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

/**
 *  Representation of the text data
 *  @author: Gail Hamilton
 */
public class TextTemplate
{
	private String userId = "";
	private String title = "";
	private String text = "";
	
	public static final String SAVE_TEXT_TEMPLATE_INSERT = "INSERT INTO text_templates (username, subject, template) values (?,?,?,?)";
	public static final String SAVE_TEXT_TEMPLATE_UPDATE = "UPDATE text_templates SET note = ? where  username = ?  and subject = ?";
	public static final String SAVE_TEXT_TEMPLATE_SELECT_CNT = "SELECT count(*) from text_templates where username = ? and subject = ?";
	public static final String SAVE_TEXT_TEMPLATE_SELECT = "SELECT template from text_templates where username = ?  and subject = ?";
	public static final String SAVE_TEXT_TEMPLATES_SELECT = "SELECT subject, template from text_templates where username = ? ";
	public static final String SAVE_TEXT_TEMPLATE_DELETE = "DELETE from text_templates where username = ? and subject= ?";
	  
	public TextTemplate( String userId, String title, String text)	
	{
		this( userId, title);
		
		this.text = text;
	}
	
	public TextTemplate(String userId, String title)	
	{
		this( userId);
		this.title = title;
		
	}
	
	public TextTemplate( String userId)	
	{
		
		this.userId = userId;
	}
	
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	  

}
