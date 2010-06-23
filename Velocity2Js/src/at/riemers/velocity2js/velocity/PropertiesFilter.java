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
 * PropertiesFilter.java
 *
 * Created on 11. Mai 2007, 14:19
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package at.riemers.velocity2js.velocity;

import java.io.File;
import java.io.FilenameFilter;

/**
 *
 * @author tobias
 */
class PropertiesFilter implements FilenameFilter {
    
    private String baseName;
    
    public PropertiesFilter(String baseName) {
        this.baseName = baseName;
    }
    
    public boolean accept(File dir, String name) {
        return (name.endsWith(".properties") && name.startsWith(baseName));
    }
}