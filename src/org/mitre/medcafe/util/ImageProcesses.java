/*
 *  Copyright 2010 The MITRE Corporation (http://www.mitre.org/). All Rights Reserved.
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
package org.mitre.medcafe.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageReadParam;
import javax.imageio.stream.ImageInputStream;

import java.util.NoSuchElementException;

public class ImageProcesses
{
	private static final int WIDTH = 128;
    private static final int HEIGHT = 128;

    public static void scaleImage(String dir, String fileName, String fileLabel) throws IOException
    {
    	String path = dir + "/" + fileName;
        BufferedImage src = ImageIO.read(new File(path));
        // Convert Image to BufferedImage if required.
        BufferedImage image = toBufferedImage(src);
        save(image,dir, fileLabel,  "jpg");  // png okay, j2se 1.4+
        //save(image, "bmp");  // j2se 1.5+

    }
	public static BufferedImage createThumbnail(File imageFile) throws Exception
	 {

			try{
				ImageReader reader;
				ImageReadParam param;

				if (imageFile == null)
				{
					throw new Exception("ImageProcesses: createThumbnail : Image not found");
				}
				ImageInputStream imageStream =
			        ImageIO.createImageInputStream(imageFile);

				reader = ImageIO.getImageReaders(imageStream).next();
				param = reader.getDefaultReadParam();
				param.setSourceSubsampling(6, 6, 0, 0);
				reader.setInput(imageStream,true,true);
				BufferedImage rtnImage = reader.read(0, param);

				reader.dispose();
				imageStream.close();

				return rtnImage;

			}
			catch (IOException e){

				e.printStackTrace();
				throw e;
			}
			catch (IllegalArgumentException e)
			{
				throw new Exception("Please select image files: File Type Warning" + e.getMessage());
			}
			catch (NoSuchElementException e)
			{
				//JOptionPane.showMessageDialog(topWindow, "Please select image files.", "File Type Warning", JOptionPane.WARNING_MESSAGE);
				throw new Exception("Please select image files: File Type Warning" + e.getMessage());

			}
	}

	 private static void save(BufferedImage image, String dir, String fileLabel, String ext) {
	        String fileName = fileLabel + "_thumbNail";
	        File file = new File(dir, fileName + "." + ext);
	        try {
	            ImageIO.write(image, ext, file);  // ignore returned boolean
	        } catch(IOException e) {
	            System.out.println("Write error for " + file.getPath() +
	                               ": " + e.getMessage());
	        }
	    }

	    private static BufferedImage toBufferedImage(Image src) {
	        int w = src.getWidth(null);
	        int h = src.getHeight(null);
	        int type = BufferedImage.TYPE_INT_RGB;  // other options
	        BufferedImage dest = new BufferedImage(w, h, type);
	        Graphics2D g2 = dest.createGraphics();
	        g2.drawImage(src, 0, 0, null);
	        g2.dispose();
	        return dest;
	    }

}
