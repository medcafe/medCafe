/*
 * Copyright 2007 Tobias Riemer
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Velocity2Js.java
 *
 * Created on 27. March 2007, 16:07
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package at.riemers.velocity2js.velocity;


import java.io.BufferedWriter;
import java.io.File;

import java.io.FileOutputStream;

import java.io.OutputStreamWriter;

import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import java.util.ResourceBundle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.velocity.Template;

import org.apache.velocity.app.Velocity;

import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import org.apache.velocity.runtime.parser.node.SimpleNode;


/**
 *
 * @author tobias
 */
public class Velocity2Js {
    
    static Log log = LogFactory.getLog(Velocity2Js.class);
    
    /** Creates a new instance of Main */
    public Velocity2Js() {
    }
    
    public static void init() throws Exception {
        Velocity.init();
    }
    
    public static void init(Properties p) throws Exception {
        Velocity.init(p);
    }
    
    
    public static void setLog(Log l) throws Exception {
        log = l;
    }
    
    
    
    public static void generate(String templateName, String functionName, Writer writer, ResourceBundle bundle) throws Exception {
        
        Template template =  null;
        
        //try {
        template = Velocity.getTemplate(templateName);
        /*} catch( ResourceNotFoundException rnfe ) {
            log.error("Example : error : cannot find template " + templateName );
            errors.add(rnfe);
        } catch( ParseErrorException pee ) {
            //Velocity2JsParseErrorException vpex = new Velocity2JsParseErrorException(pee, )
            log.error("Example : Syntax error in template " + templateName + ":" + pee );
            errors.add(pee);
        }*/
        
        if ( template != null) {
            template.process();
            SimpleNode node = (SimpleNode) template.getData();
            VelocityVisitor visitor = new VelocityVisitor(bundle);
            writer.write(visitor.visit(node, functionName).toString());
        }
    }
    
    public static String createFunctionName(String fileName) {
        
        int s = 0;
        
        if (fileName.startsWith("/")) {
            s = 1;
        }
        
        int e = fileName.lastIndexOf(".");
        if (e<0) { e = fileName.length(); }
        
        String functionName = fileName.substring(s,e).replaceAll("/", "_");
        functionName = functionName.replaceAll("\\\\", "_");
        return "v2js_" + functionName;
    }
    
    
    
    
    
    public static void generateDir(String templateDir, String javascriptDir, List<I18NBundle> bundles) throws Exception {
        
        Properties p = new Properties();
        p.setProperty("resource.loader" , "file");
        
        p.setProperty("file.resource.loader.description", "Velocity File Resource Loader");
        p.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.FileResourceLoader");
        
        p.setProperty( "file.resource.loader.path", templateDir);
        Velocity2Js.init(p);
        
        File tDir = new File(templateDir);
        
        for(I18NBundle bundle : bundles) {
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(javascriptDir + "/vel2js" + bundle.getLocale() + ".js"), "UTF8"));
            
            //BufferedWriter out = new BufferedWriter(new FileWriter(new File(javascriptDir + "/vel2js" + bundle.getLocale() + ".js")));
            process(tDir, null, out, bundle.getBundle());
            out.flush();
            out.close();
        }
        
        
    }
    
    private static void process(File file, String dir, BufferedWriter out, ResourceBundle bundle) throws Exception {
        if (file.isDirectory()) {
            
            if (dir == null) {
                dir = "";
            } else {
                int index = file.getPath().lastIndexOf(File.separatorChar);
                if (dir.equals("")) {
                    dir = dir + file.getName();
                } else {
                    dir = dir + "/" + file.getName();
                }
            }
            
            File[] files = file.listFiles();
            
            for(int i=0;i<files.length;i++) {
                process(files[i], dir, out, bundle);
            }
        } else {
            if (file.getName().endsWith(".vm")) {
                processTemplate(dir + "/" + file.getName(), out, bundle);
            }
        }
    }
    
    private static void processTemplate(String inputFile, BufferedWriter out, ResourceBundle bundle) throws Exception {
        
        log.info("processing: " + inputFile);
        
        String functionName = Velocity2Js.createFunctionName(inputFile);
        generate(inputFile, functionName, out, bundle);
        
        out.newLine();
        out.newLine();
        
    }
    
    public static List<I18NBundle> getBundles(String resource) {
        List<I18NBundle> bundles = new ArrayList<I18NBundle>();
        if (resource != null) {
            try {
                File rb = new File(resource);
                String rbDirName = rb.getParent();
                String resourceName = rb.getName();
                //log("[velocity2js] - " + resourceName);
                if (rbDirName == null) {
                    rbDirName = ".";
                }
                File rbDir = new File(rbDirName);
                String[] files = rbDir.list(new PropertiesFilter(resourceName));
                for(String file : files) {
                    bundles.add(new I18NBundle(rbDirName, file));
                }
            } catch (Exception ex) {
                log.error("[velocity2js] - " + ex.toString(), ex);
            }
        } else {
            bundles.add(new I18NBundle());
        }
        return bundles;
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            if (args.length == 0) {
                printUsage();
                return;
            }
            
            if (args[0].equals("-d") && args.length >= 3) {
                String resource = null;
                if (args.length >= 4) {
                    resource = args[3];
                }
                List<I18NBundle> bundles = getBundles(resource);
                
                Velocity2Js.generateDir(args[1], args[2], bundles);
                return ;
            }
            
            if (args[0].equals("-f") && args.length >= 4) {
                
                Properties p = new Properties();
                p.setProperty("resource.loader" , "file");
                
                p.setProperty("file.resource.loader.description", "Velocity File Resource Loader");
                p.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.FileResourceLoader");
                
                p.setProperty("file.resource.loader.path", args[1]);
                
                Velocity2Js.init(p);
                
                String function = createFunctionName(args[2]);
                String resource = null;
                if (args.length >= 5) {
                    resource = args[4];
                }
                List<I18NBundle> bundles = getBundles(resource);
                
                for(I18NBundle bundle : bundles) {
                    String fname = args[3];
                    int e = args[3].lastIndexOf('.');
                    if (e<=0 || e > args[3].length()) e = args[3].length();
                    fname = args[3].substring(0, e) + bundle.getLocale() + args[3].substring(e);
                                        
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fname), "UTF8"));
                    Velocity2Js.generate(args[2], function, writer, bundle.getBundle());
                    writer.flush();
                    writer.close();
                }
                
                return ;
            }
            
            printUsage();
            return ;
            
        } catch( ResourceNotFoundException rnfe ) {
            log.error("[velocity2js] : cannot find template : " + rnfe.getMessage() );
        } catch( ParseErrorException pee ) {
            log.error("[velocity2js] :  Syntax error in template :" + pee );
        } catch( Exception ex ) {
            System.out.flush();
            log.error("[velocity2js] :  unknown error " + ex.getMessage());
            ex.printStackTrace(System.out);
            System.out.println(ex.getLocalizedMessage());
            System.exit(1);
        }
    }
    
    private static void printUsage() {
        System.out.println("usage Velocity2Js -f <base-dir> <input-file> <output-file> [resource-file]");
        System.out.println("or");
        System.out.println("usage Velocity2Js -d <template-dir> <output-dir> [resource-file]");
    }
    
    
    
    
    
}
