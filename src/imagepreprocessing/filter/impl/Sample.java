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
import neurophClasses.FractionHSLData;

/**
 *
 * @author Mihailo Stupar
 */
public class Sample implements ImageFilter{

    private BufferedImage originalImage;
    private BufferedImage filteredImage;
    
    
    @Override
    public BufferedImage processImage(BufferedImage image) {
    
        double [][] hslValues;
        
        originalImage = image;
        
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        
        filteredImage = new BufferedImage(width, height, originalImage.getType());
        
        FractionHSLData fraction = new FractionHSLData(originalImage);
        fraction.fillFlattenedHueValues();
        
        hslValues = fraction.getLightnessValues();
        
        int pos = 0;
        
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int color = (int) Math.round(hslValues[i][j]*255);
                int alpha = new Color(originalImage.getRGB(j, i)).getAlpha();
                int rgb = PreprocessingHelper.colorToRGB(alpha, color, color, color);
                filteredImage.setRGB(j, i, rgb);
            }
        }
        
        
        
        return filteredImage;
    }

    @Override
    public String toString() {
        return "Sample";
    }
    
    
    
}
