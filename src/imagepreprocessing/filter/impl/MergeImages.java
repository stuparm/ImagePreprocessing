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
 * @author Stupi
 */
public class MergeImages implements ImageFilter{
    
    private BufferedImage originalImage;
    private BufferedImage filteredImage;
    
    
    @Override
    public BufferedImage processImage(BufferedImage image) {
        
        
        originalImage = image;
        
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        filteredImage = new BufferedImage(width, height, originalImage.getType());
        
        
        BufferedImage firstImage = FinalFilter.getInstance().getOriginalImage();
        mergeTwoImages(firstImage);

        return filteredImage;
    
    }
    public void mergeTwoImages(BufferedImage firstImage) {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        int black = 0;
        int white = 255;
        

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int rgb;

                int oldColor = new Color(originalImage.getRGB(i, j)).getRed();
                if (oldColor == white) {
                    int alpha = new Color(firstImage.getRGB(i, j)).getAlpha();
                    rgb = PreprocessingHelper.colorToRGB(alpha, black, black, black);
                } else {
                    rgb = new Color(firstImage.getRGB(i, j)).getRGB();
                }
                filteredImage.setRGB(i, j, rgb);
            }
        }

    }

    @Override
    public String toString() {
        return "Merge Images";
    }

    
    
    
}
