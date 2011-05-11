// <editor-fold defaultstate="collapsed">
/*
 * Copyright (C) 2007-2009  Medsphere Systems Corporation
 * All rights reserved.
 *
 * This source code contains the intellectual property
 * of its copyright holder(s), and is made available
 * under a license. If you do not know the terms of
 * the license, please stop and do not read further.
 *
 * Please read LICENSES for detailed information about
 * the license this source code file is available under.
 * Questions should be directed to legal@medsphere.com
 *
 */
// </editor-fold>

package com.medsphere.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class PropertyFileLocator {

    private Properties properties = null;
    private String filePath = null;

    private PropertyFileLocator() {}
    public PropertyFileLocator(String resourceDirectory, String fileName) throws IOException {
        properties = loadPropertiesFromFile(resourceDirectory, fileName);
    }

    public Properties getProperties() {
        return properties;
    }

    private Properties loadPropertiesFromFile(String resourceDirectory, String fileName) {
        Properties props = new Properties();

        String location = resourceDirectory + File.separatorChar + fileName;
        FileInputStream fis = null;

            try {
                fis = new FileInputStream(location);
            } catch (FileNotFoundException e) {
            }
            if (fis != null) {
                try {
                    properties.load(new FileInputStream(location));
                    filePath = new File(location).getAbsolutePath();
                } catch (IOException e) {
                }
            } else {
                location = '/' + resourceDirectory + '/' + fileName;
                InputStream inputStream = this.getClass().getResourceAsStream(location);
                try {
                    if (inputStream != null) {
                        try {
                            props.load(inputStream);
                            filePath = this.getClass().getResource(location).toExternalForm();
                        } catch (IOException e) {
                        }
                    }
                } finally {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                        }
                    }
                }
           }

       return props;
    }

    public static class TestPropertyFileLocator {
        public TestPropertyFileLocator() {}

        @BeforeClass public void setUp() {
            try {
                FileWriter fw = new FileWriter(new File("unittest.properties"));
                fw.write("property.array = value1\n");
                fw.write("property.array = value2\n");
                fw.close();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }

        @AfterClass public void tearDown() {
            new File("unittest.properties").deleteOnExit();
        }

        @Test public void testItAllOutHere() throws IOException {
            PropertyFileLocator pfl = new PropertyFileLocator(".", "unittest.properties");
        }
    }

    public String getFilePath() {
        return filePath;
    }

}
