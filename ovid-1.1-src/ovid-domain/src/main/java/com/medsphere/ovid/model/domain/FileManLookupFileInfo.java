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

public class FileManLookupFileInfo {

    private String filename="";
    private String filenum="";
    private String openRef="";

    public FileManLookupFileInfo()
    {
        super();
    }
    /*
     * This constructor uses the strings constants for the files it refers to
     * where the constant is a tab delimited string with the filename first, the
     * file number second, and the storage location of the file third
     * 
     * For example the GMR ALLERGIES file is named 'GMR ALLERGIES', the file number is '120.82',
     * and the storage location is 'GMRD(120.82,' so the lookup context is
     * "GMR ALLERGIES\t120.82\tGMRD(120.82,"
     */
    public FileManLookupFileInfo(String lookupContext)
    {
        String[] parts = lookupContext.split("\t");
        if (parts.length==3)
        {
            filename = parts[0];
            filenum = parts[1];
            openRef = parts[2];
        }
        else
        {
            filename = lookupContext;
        }
    }
    public FileManLookupFileInfo(String openRef, String filenum, String filename)
    {
        this.openRef = openRef;
        this.filenum = filenum;
        this.filename = filename;
    }
    public void setOpenRef(String openRef)
    {
        this.openRef = openRef;
    }
    public String getOpenRef()
    {
        return openRef;
    }
    public void setFilenum(String filenum)
    {
        this.filenum = filenum;
    }
    public String getFilenum()
    {
        return filenum;
    }
    public void setFilename(String filename)
    {
        this.filename= filename;
    }
    public String getFilename()
    {
        return filename;
    }
    @Override
    public String toString()
    {
         return ""
              +" Fileman open Reference to file root=["+getOpenRef() + "]"
              +" file name=["+getFilename() + "]"
              +" file number=["+getFilenum() + "]"
            
             ;
    }

}
