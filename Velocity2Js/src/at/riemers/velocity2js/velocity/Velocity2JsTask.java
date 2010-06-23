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
 * Velocity2JsTask.java
 *
 * Created on 10. April 2007, 16:20
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package at.riemers.velocity2js.velocity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;

import java.io.IOException;
import java.io.OutputStreamWriter;

import java.util.List;
import java.util.Properties;
import java.util.PropertyResourceBundle;

import java.util.Vector;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

import org.apache.tools.ant.types.FileSet;

import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;


/**
 *
 * @author tobias
 */
public class Velocity2JsTask extends Task {
    
    private String templateDir;
    private String javascriptDir;
    private String resource;
    private String javascriptName = "vel2js";
    private final Vector<FileSet> filesets = new Vector<FileSet>();
    
    /** Creates a new instance of Velocity2JsTask */
    public Velocity2JsTask() {
    }

    @Override
    public void execute() throws BuildException {
        try {
            
            
            // check if templateDir is relative or absolute
            if (!new File(templateDir).isAbsolute()) {
                templateDir = new File(getProject().getBaseDir() + File.separator + templateDir).getCanonicalPath();
            }
            
            // check if javascriptDir is relative or absolute
            if (!new File(javascriptDir).isAbsolute()) {
                javascriptDir = new File(getProject().getBaseDir() + File.separator + javascriptDir).getCanonicalPath();
            }
            
            if (resource != null) {
                if (!new File(resource).isAbsolute()) {
                    resource = new File(getProject().getBaseDir() + File.separator + resource).getCanonicalPath();
                }
            }
            
            if (filesets.isEmpty()) {
                log("[velocity2js] - no fileset defined", Project.MSG_ERR);
                return ;
            }
            
            
            List<I18NBundle> bundles = Velocity2Js.getBundles(resource);
            
            Properties p = new Properties();
            p.setProperty("resource.loader" , "file");
            
            p.setProperty("file.resource.loader.description", "Velocity File Resource Loader");
            p.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.FileResourceLoader");
            
            p.setProperty( "file.resource.loader.path", templateDir);
            Velocity2Js.init(p);
            
            
            if (bundles.isEmpty()) {
                generate(getJavascriptName() + ".js", null);
            } else {
                for(I18NBundle bundle : bundles) {
                    generate(getJavascriptName() + bundle.getLocale() + ".js", bundle.getBundle());
                }
            }
            
            
            
        } catch( Exception ex ) {
            for(StackTraceElement trace : ex.getStackTrace()) {
                log("[velocity2js] - " + trace.toString(),  Project.MSG_ERR);
            }
            log("[velocity2js] - " + ex.toString(),  Project.MSG_ERR);
        }
        // Velocity2Js.generateDir(getTemplateDir(), getJavascriptDir());
        
        
    }
    
    
    public String getTemplateDir() {
        return templateDir;
    }
    
    public void setTemplateDir(String templateDir) {
        this.templateDir = templateDir;
    }
    
    public String getJavascriptDir() {
        return javascriptDir;
    }
    
    public void setJavascriptDir(String javascriptDir) {
        this.javascriptDir = javascriptDir;
    }
    
    
    public void addFileset(FileSet set) {
        filesets.addElement(set);
    }
    
    private String createTemplateName(String fileName, String templateDir) {
        File template = new File(fileName);
        File dir = new File(templateDir);
        try {
            if (template.getCanonicalPath().length() > dir.getCanonicalPath().length() &&
                    template.getCanonicalPath().startsWith(dir.getCanonicalPath())) {
                return template.getCanonicalPath().substring(dir.getCanonicalPath().length()+1);
            }
            return template.getName();
        } catch (Exception e) {}
        return fileName;
    }
    
    public String getResource() {
        return resource;
    }
    
    public void setResource(String resource) {
        this.resource = resource;
    }
    
    private void generate(String fileName, PropertyResourceBundle bundle) throws IOException, Exception {
        
        log("[velocity2js] - generating " + fileName + "...", Project.MSG_INFO);
        
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(javascriptDir + "/" + fileName), "UTF8"));
        for(FileSet fs : filesets) {
            DirectoryScanner ds = fs.getDirectoryScanner(this.getProject());
            File dir = fs.getDir(this.getProject());
            String[] srcs = ds.getIncludedFiles();
            for (int j = 0; j < srcs.length; j++) {
                try {
                    log("[velocity2js] - compiling: " + srcs[j] + "...");
                    String templateName = createTemplateName(dir.getAbsolutePath() + File.separator + srcs[j], templateDir);
                    Velocity2Js.generate(templateName , Velocity2Js.createFunctionName(templateName), out, bundle);
                } catch( ResourceNotFoundException rnfe ) {
                    log("cannot find template : " + rnfe.getMessage(),  Project.MSG_ERR);
                } catch( ParseErrorException pee ) {
                    log(templateDir + File.separator + pee.getTemplateName() + ":" +  pee.getLineNumber() + ": " +
                            pee.getMessage(),  Project.MSG_ERR);
                    
                }
            }
        }
        
        out.flush();
        out.close();
    }
    
    public String getJavascriptName() {
        return javascriptName;
    }
    
    public void setJavascriptName(String javascriptName) {
        if (javascriptName.endsWith(".js")) {
            this.javascriptName = javascriptName.substring(0,javascriptName.length()-3);
        } else {
            this.javascriptName = javascriptName;
        }
        
    }
    
    
    
    
    
}
