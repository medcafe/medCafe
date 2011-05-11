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
 * Variable.java
 *
 * Created on 10. April 2007, 15:29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package at.riemers.velocity2js.jsrenderer;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tobias
 */
public class Variable {
    
    private List<String> ids = new ArrayList<String>();
    private int level = 0;
    private boolean local = false;
    
    /** Creates a new instance of Variable */
    public Variable(String name) {
        this.ids.add(name);
    }
    
     public Variable(String name, int level) {
        this.ids.add(name);
        this.setLevel(level);
        this.setLocal(true);
    }
    
    public String toString() {
        
        String s = "";
        
        if (isLocal()) {
        //    s = "v2jsv_";
        }
        
        boolean first = true;
        for(String id : ids) {
            if (!first) {
                s += "." + id;
            } else {
                s += id;
            }
            first = false;
        }
        return s;
    }
    
    public void addId(String id) {
        ids.add(id);
    }
    
    public String getReference() {
        return ids.get(0);
    }
    
    public String getName() {
        return toString();
    }
    
    public boolean equals(Object obj) {
        if (obj instanceof Variable) {
            return getName().equals(((Variable) obj).getName());
        }
        return false;
    }
    
    public int hashCode() {
        return getName().hashCode();
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isLocal() {
        return local;
    }

    public void setLocal(boolean local) {
        this.local = local;
    }
    
}
