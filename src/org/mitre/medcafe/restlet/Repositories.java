/*
 * Copyright 2010 The MITRE Corporation (http://www.mitre.org/). All Rights
 * Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.mitre.medcafe.restlet;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.mitre.medcafe.util.Constants;
import org.mitre.medcafe.util.Repository;
import org.mitre.medcafe.util.XMLProcessing;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This is called during an initialization process. repositories in the constants config
 * are pulled into memory - tested for connectivity.
 */
public class Repositories {

	static Map<String, Repository>	activeRepositories	= new HashMap<String, Repository>();
	static Map<String, Repository>	offlineRepositories	= new HashMap<String, Repository>();

	static final String				REPOSITORY_SETUP	= "Repositories.xml";

	public final static Logger		log					= Logger.getLogger(Repositories.class.getName());

	public static Map<String, Repository> getRepositories() {
		return Collections.unmodifiableMap(activeRepositories);
	}
	
	public static Map<String, Repository> getOfflineRepositories() {
		return Collections.unmodifiableMap(offlineRepositories);
	}

	private static boolean isOnline(String address, String port_str) {
		try {
			short port = Short.parseShort(port_str);
			Socket sock = new Socket(address, port);
			sock.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// this is called each time through the loop while parsing the xml doc in
	// setDefaultRepositories
	public static void addRepository(Element repository) throws Exception {

		NodeList typeList = repository.getElementsByTagName(Repository.REPOSITORY_TYPE);
		NodeList nameList = repository.getElementsByTagName(Repository.REPOSITORY_NAME);
		NodeList hostList = repository.getElementsByTagName(Repository.HOST);
		NodeList creds = repository.getElementsByTagName(Repository.CREDENTIALS);

		if (hostList.getLength() == 0 || nameList.getLength() == 0
				|| typeList.getLength() == 0 || creds.getLength() == 0) { throw new Exception(
				"incomplete repository element, specify host, name, java class name and credentials"); }

		String classType = typeList.item(0).getTextContent();
		String name = nameList.item(0).getTextContent();
		String host = hostList.item(0).getTextContent();

		// This mapping will save the xml cred tree given by <key>value</key>
		HashMap<String, String> credMap = new HashMap<String, String>();

		Element credElement = (Element) creds.item(0);
		NodeList credList = credElement.getChildNodes();
		for (int j = 1; j < credList.getLength(); j = j + 2) {
			Node credNode = credList.item(j);

			String key = credNode.getNodeName();
			String value = credElement.getElementsByTagName(key).item(0).getTextContent();
			log.finer(j + ": " + key + ": " + value);
			credMap.put(key, value);
		}

		// Actually instantiate the indicated repository

		Class<?>[] argList = new Class[1];
		argList[0] = credMap.getClass();  // the credmap will be passed to the
											// constructor
		Object[] objList = new Object[1];
		objList[0] = credMap;

		Class<?> repClass = Class.forName(classType);
		Constructor<?> repConst = repClass.getDeclaredConstructor(argList);
		Repository repo_obj = (Repository) repConst.newInstance(objList);
		repo_obj.setName(name);

		// can we connect to this host/port combo?
		if (Repositories.isOnline(host, credMap.get("port"))) {
			activeRepositories.put(repo_obj.getName(), repo_obj);
		}
		else {
			offlineRepositories.put(repo_obj.getName(), repo_obj);
			log.info("Repository " + name + " is offline at the moment.");
		}

		log.info("Initialized " + name);
	}

	public static void setDefaultRepositories() throws Exception {
		File file = new File(Constants.CONFIG_DIR, REPOSITORY_SETUP);
		Document repositoryDoc = XMLProcessing.createXMLDoc(file);

		// Expecting one root <repositories> branch
		Node repositoryNode = repositoryDoc.getElementsByTagName(
				Repository.REPOSITORIES).item(0);

		if (repositoryNode == null) { throw new Exception(
				"Error on parsing xml document : could not find any nodes"); }
		if (repositoryNode.getNodeType() != Node.ELEMENT_NODE) { throw new Exception(
				"Error on parsing xml document : could not find any elements"); }

		NodeList repositoryNodes = ((Element) repositoryNode).getElementsByTagName(Repository.REPOSITORY_ITEM);

		System.out.println("Repositories setDefaultRepositories number of components "
				+ repositoryNodes.getLength());
		activeRepositories = new HashMap<String, Repository>();

		System.out.println("Number of repositories: "
				+ repositoryNodes.getLength());
		for (int i = 0; i < repositoryNodes.getLength(); i++) {
			Node node = repositoryNodes.item(i);
			if (node.getNodeType() != Node.ELEMENT_NODE) {
				log.config("improper repository element");
				continue;
			}

			try {
				Repositories.addRepository((Element) node);
			} catch (Exception e) {
				for (StackTraceElement elem : e.getStackTrace())
				{
					log.severe(elem.toString());
				}
			}
		}
	}

	public static void onShutdown() {
		for (Repository r : activeRepositories.values()) {
			r.onShutdown();
		}
	}
	
	public static List<String> getOfflineRepositoryNames() {
		return new ArrayList<String>(offlineRepositories.keySet());
	}

	public static List<String> getRepositoryNames() {
		return new ArrayList<String>(activeRepositories.keySet());
	}

	public static Repository getRepository(String name) {
		return activeRepositories.get(name);
	}

}
