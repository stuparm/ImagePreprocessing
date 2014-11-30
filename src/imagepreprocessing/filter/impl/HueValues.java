/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package imagepreprocessing.filter.impl;

import imagepreprocessing.filter.ImageFilter;
import imagepreprocessing.helper.PreprocessingHelper;
import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 *
 * @author Mihailo
 */
public class HueValues implements ImageFilter{
    
     private BufferedImage originalImage;
    private BufferedImage filteredImage;
	
    @Override
    public BufferedImage processImage(BufferedImage image) {
		
	originalImage = image;
		
	int width = originalImage.getWidth();
	int height = originalImage.getHeight();
		
	filteredImage = new BufferedImage(width, height, originalImage.getType());
        
        
        double red;
        double green;
        double blue;

        double Cmax;
        double Cmin;

        double delta;

        for (int j = 0; j < width; j++) {
            for (int i = 0; i < height; i++) {

                Color color = new Color(originalImage.getRGB(j, i));

                red = color.getRed();
                green = color.getGreen();
                blue = color.getBlue();

                red = red / 255;
                green = green / 255;
                blue = blue / 255;

                Cmax = Math.max(red, Math.max(green, blue));
                Cmin = Math.min(red, Math.min(green, blue));

                delta = Cmax - Cmin;

                double hue = 0;
                if (delta != 0) {
                    if (Cmax == red) {
                        hue = 60 * (((green - blue) / delta) % 6);
                    }
                    if (Cmax == green) {
                        hue = 60 * (((blue - red) / delta) + 2);
                    }
                    if (Cmax == blue) {
                        hue = 60 * ((red - green) / delta + 4);
                    }
                } else {
                    double a = (2 * red - green - blue) / 2;
                    double b = (green - blue) * Math.sqrt(3) / 2;
                    hue = Math.atan2(b, a);
                }
                hue =  hue / 360; //podeli sa 360 da vrednot bude izmedju 0-1
                
                int alpha = new Color(originalImage.getRGB(j, i)).getAlpha();
                int hueGray = (int)Math.round(hue*255);
                
                int rgb = PreprocessingHelper.colorToRGB(alpha, hueGray, hueGray, hueGray);
                
                filteredImage.setRGB(j, i, rgb);
                
                
            }
        }

        return filteredImage;
    }

    @Override
    public String toString() {
        return "Hue Values";
    }
    
    
    
    
    
}
