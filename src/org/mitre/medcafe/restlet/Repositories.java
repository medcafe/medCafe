/*
 * Copyright 2010 The MITRE Corporation (http://www.mitre.org/). All Rights
 * Reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.mitre.medcafe.restlet;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.InetAddress;
import java.text.ParseException;
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
 * Collection of all Repositories
 */
public class Repositories {

  protected static Map<String, Repository> repos = new HashMap<String, Repository>();
  protected static final int TIMEOUT = 8000; // I recommend 3 seconds at least
  protected static final String REPOSITORY_SETUP = "Repositories.xml";
  public final static String KEY = Repositories.class.getName();
  public final static Logger log = Logger.getLogger(KEY);

  public Repositories() {

  }

  public static Map<String, Repository> getRepositories() {
    return Collections.unmodifiableMap(repos);
  }

  /**
   * Repositories for testing - until an external representation is arrived at,
   * anyway
   */
  public static void setDefaultRepositories() throws Exception {
    File file = new File(Constants.CONFIG_DIR, REPOSITORY_SETUP);
    Document repositoryDoc = XMLProcessing.createXMLDoc(file);
    NodeList repositoryNodes = null;
    Node repositoryNode = null;
    Element repositoryElmnt = null;

    repositoryNodes = repositoryDoc
        .getElementsByTagName(Repository.REPOSITORIES);
    if (repositoryNodes.getLength() > 0) {
      repositoryNode = repositoryNodes.item(0);
    }
    else {
      throw new ParseException(
          "Error on parsing xml document : could not find any nodes", 0);
    }

    log.finer("Repositories setDefaultRepositories got repositories");

    if (repositoryNode.getNodeType() == Node.ELEMENT_NODE) {
      repositoryElmnt = (Element) repositoryNode;
    }
    else {
      throw new ParseException(
          "Error on parsing xml document : could not find any elements", 0);
    }
    repositoryNodes = repositoryElmnt
        .getElementsByTagName(Repository.REPOSITORY_ITEM);

    log.finer("Repositories setDefaultRepositories number of components "
        + repositoryNodes.getLength());
    repos = new HashMap<String, Repository>();
    Repository r = null;

    log.config("Number of repositories: " + repositoryNodes.getLength());
    for (int i = 0; i < repositoryNodes.getLength(); i++) {

      Node node = repositoryNodes.item(i);
      if (node.getNodeType() != Node.ELEMENT_NODE) {
        log.config("improper repository element");
        continue;
      }
      repositoryElmnt = (Element) node;
      NodeList typeList = repositoryElmnt
          .getElementsByTagName(Repository.REPOSITORY_TYPE);
      String reposType = "";
      String reposName = "";
      String host = "";

      if (typeList.getLength() == 0) {
        log.config("improper repository element, specify type");
        continue;
      }

      reposType = typeList.item(0).getTextContent();
      NodeList nameList = repositoryElmnt
          .getElementsByTagName(Repository.REPOSITORY_NAME);

      if (nameList.getLength() == 0) {
        log.config("improper repository element, specify name(s)");
        continue;
      }

      reposName = nameList.item(0).getTextContent();
      NodeList hostList = repositoryElmnt.getElementsByTagName(Repository.HOST);

      if (hostList.getLength() == 0) {
        log.config("improper repository element, specify host(s)");
        continue;
      }

      host = hostList.item(0).getTextContent();
      HashMap<String, String> credMap = new HashMap<String, String>();
      NodeList creds = repositoryElmnt
          .getElementsByTagName(Repository.CREDENTIALS);

      if (creds.getLength() == 0) {
        log.config("improper repository element, specify credentials");
        continue;
      }

      Element credElement = (Element) creds.item(0);
      NodeList credList = credElement.getChildNodes(); // returns
      // text nodes
      // only odd ones contain names
      // of elements
      for (int j = 1; j < credList.getLength(); j = j + 2) {
        Node credNode = credList.item(j);

        String key = credNode.getNodeName();
        String value = credElement.getElementsByTagName(key).item(0)
            .getTextContent();
        log.finer(j + ": " + key + ": " + value);
        credMap.put(key, value);
      }

      try {
        Class repClass = Class.forName(reposType);
        InetAddress address = InetAddress.getByName(host);
        log.info(address.getHostAddress());
        if (address.isReachable(TIMEOUT)) {
          log.info("connecting to " + host);
          Class[] argList = new Class[1];
          argList[0] = credMap.getClass();
          Constructor repConst = repClass.getDeclaredConstructor(argList);
          Object[] objList = new Object[1];
          objList[0] = credMap;
          r = (Repository) repConst.newInstance(objList);
          r.setName(reposName);
          repos.put(r.getName(), r);
          log.config("Got " + reposName + " connection");

        }
        else {
          log.config("could not connect to " + host);
        }
      } catch (Exception e) {
        log.severe(e.getStackTrace().toString());
      }
    }
  }

  public static void onShutdown() {
    for (Repository r : repos.values()) {
      r.onShutdown();
    }

  }

  public static List<String> getRepositoryNames() {
    return new ArrayList<String>(repos.keySet());
  }

  public static Repository getRepository(String name) {
    return repos.get(name);
  }

}
