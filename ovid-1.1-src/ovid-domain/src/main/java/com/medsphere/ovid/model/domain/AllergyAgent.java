/*
 *  Copyright 2011 The MITRE Corporation (http://www.mitre.org/). All Rights Reserved.
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

package com.medsphere.ovid.model.domain;

import com.medsphere.fileman.FMRecord;
public class AllergyAgent implements Comparable<AllergyAgent>{
    private FileManLookupFileInfo lookupFile=null;
   
    private String displayName = "";
    private Allergen allergen = null;
    private FMRecord allergenRecord;
    public AllergyAgent()
    {
        super();

    }
    public AllergyAgent(String ien, String filename, String filenum, String openRef,
            String name, String official, String allergyType, FMRecord allRec)
    {
        this(new FileManLookupFileInfo(openRef, filenum, filename), new Allergen(ien, official, allergyType), name, allRec);
       
    }
    public AllergyAgent(FileManLookupFileInfo lookupFile, Allergen allergen, String name, FMRecord allRec)
    {
        this.lookupFile = lookupFile;
        this.allergen = allergen;
        
        this.displayName= name;
        this.allergenRecord = allRec;

    }
    public Allergen getAllergen()
    {
        return allergen;
    }
    public void setAllergen(Allergen allergen)
    {
        this.allergen = allergen;
    }

    public void setLookupFile(FileManLookupFileInfo lookup)
    {
        this.lookupFile = lookup;
    }
    public FileManLookupFileInfo getLookupFile()
    {
        return lookupFile;
    }
       public void setDisplayName(String name)
    {
        this.displayName = name;
    }
    public String getDisplayName()
    {
        return displayName;
    }
    public int compareTo(AllergyAgent agent)
    {
        return displayName.compareToIgnoreCase(agent.getDisplayName());
    }
    public void setAllergenRecord(FMRecord allRec)
    {
        allergenRecord = allRec;
    }
    public FMRecord getAllergenRecord()
    {
        return allergenRecord;
    }
   
    @Override
    public String toString()
    {
        return ""
              +" LookupFileInfo=[" + getLookupFile().toString() +"]"
              +" Allergen =["+ getAllergen().toString() +"]"
              +" display name=[" + getDisplayName() + "]"
              + " Allergen Record File Name=[" + allergenRecord.getFileName() + "]"
              + " Allergen Record=[" + getAllergenRecord() + "]"
             ;           
    }

}
