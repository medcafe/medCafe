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
 * I18NBundle.java
 *
 * Created on 11. Mai 2007, 10:36
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package at.riemers.velocity2js.velocity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.PropertyResourceBundle;

/**
 *
 * @author tobias
 */
public class I18NBundle {
    
    private PropertyResourceBundle bundle;
    private String locale = "";
    
    public I18NBundle() {
        
    }
    
    /** Creates a new instance of I18NBundle */
    public I18NBundle(String dir, String resource) throws FileNotFoundException, IOException {
        bundle = new PropertyResourceBundle(new FileInputStream(new File(dir + File.separator +resource)));
        if (resource.indexOf('_') > 0) {
            int e = resource.lastIndexOf('.');
            if (e<0 || e>resource.length()) {
                e = resource.length();
            }
            locale = resource.substring(resource.indexOf('_'), e);
        }
    }

    public PropertyResourceBundle getBundle() {
        return bundle;
    }

    public String getLocale() {
        return locale;
    }
    
}
