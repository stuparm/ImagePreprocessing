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
 * @author Aleksandar Buha
 */
public class LungSplit implements ImageFilter {

    private BufferedImage originalImage;
    private BufferedImage filteredImage;

    private int width;
    private int height;

    @Override
    public BufferedImage processImage(BufferedImage image) {
        originalImage = image;
        width = originalImage.getWidth();
        height = originalImage.getHeight();

        filteredImage = new BufferedImage(width, height, originalImage.getType());

        int minBlack = Integer.MAX_VALUE;
        int count = 0;
        int black = 0;
        int minBlackPosition = 0;
        
        int start = 0;
        leftLoop:
        for (; start < width; start++) {
            for (int j = 0; j < height; j++) {
                int color = new Color(originalImage.getRGB(start, j)).getRed();
                if (color == black) {
                    break leftLoop;
                }
            }
        }

        
        
        int end = width-1;
        rightLoop:
        for (;  end > 0; end--) {
            for (int j = 0; j < height; j++) {
                int color = new Color(originalImage.getRGB(end, j)).getRed();
                if (color == black) {
                    break rightLoop;
                }
            } 
        }
        
        int center = start + (end - start)/2;
        int offset = width/10;
        
        System.out.println(start);
        System.out.println(end);
        
        
        

        for (int i = center-offset; i < center+offset; i++) {
            for (int j = 0; j < height; j++) {
                int color = new Color(originalImage.getRGB(i, j)).getRed();
                if (color == black) {
                    count++;
                }
            }
            if (count < minBlack) {
                minBlackPosition = i;
                minBlack = count;

            }
            count = 0;
        }
        int white = 255;
        System.out.println(minBlackPosition);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int alpha = new Color(originalImage.getRGB(i, j)).getAlpha();
                int color;
                if (i == minBlackPosition) {
                    color = white;
                }
                else{
                    color = new Color(originalImage.getRGB(i, j)).getRed();
                }
                int rgb = PreprocessingHelper.colorToRGB(alpha, color, color, color);
                filteredImage.setRGB(i, j, rgb);
                
            }
        }
//        for (int j = 0; j < height; j++) {
//            int alpha = new Color(originalImage.getRGB(minBlackPosition, j)).getAlpha();
//            int rgb = PreprocessingHelper.colorToRGB(alpha, white, white, white);
//            originalImage.setRGB(minBlackPosition, j, rgb);
//        }

        return filteredImage;
    }

    @Override
    public String toString() {
        return "Lung Split";
    }
    
    

}
