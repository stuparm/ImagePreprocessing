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
public class LungErosion implements ImageFilter{

    private BufferedImage originalImage;
    private BufferedImage filteredImage;
    
    
    @Override
    public BufferedImage processImage(BufferedImage image) {
        
        originalImage = image;
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        
        filteredImage = new BufferedImage(width, height, originalImage.getType());
        
        
        boolean [][] visited = new boolean[width][height];
        int black = 0;
        int white = 255;
        for (int i = 3; i < width-3; i++) {
            for (int j = 3; j < height-3; j++) {
                if (isFilledDiamond(i, j)) {
//                if (isFilled(i, j)) {
                    int alpha = new Color(originalImage.getRGB(i, j)).getAlpha();
                    int rgb = PreprocessingHelper.colorToRGB(alpha, black, black, black);
                    filteredImage.setRGB(i, j, rgb);
                    visited[i][j] = true;
                }     
            }
        }
        
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (!visited[i][j]) {
                    int alpha = new Color(originalImage.getRGB(i, j)).getAlpha();
                    int rgb = PreprocessingHelper.colorToRGB(alpha,white, white, white);
                    filteredImage.setRGB(i, j, rgb);
                }
                
                
//                int gray = new Color(filteredImage.getRGB(i, j)).getRed();
//                if (gray != black) {
//                    
//                    int alpha = new Color(originalImage.getRGB(i, j)).getAlpha();
//                    int rgb = PreprocessingHelper.colorToRGB(alpha,white, white, white);
//                    filteredImage.setRGB(i, j, rgb);
//                }
                    
            }
        }

        return filteredImage;
        
        
    }

    private boolean isFilledDiamond(int i, int j) {
        
         int startX = i-2;
         int startY = j-2;
         int count = 0;
         if (isBlack(startX, startY+2)) count++;
         
         if (isBlack(startX+1, startY+1)) count++;
         if (isBlack(startX+1, startY+2)) count++;
         if (isBlack(startX+1, startY+3)) count++;
         
         if (isBlack(startX+2, startY)) count++;
         if (isBlack(startX+2, startY+1)) count++;
         if (isBlack(startX+2, startY+2)) count++;
         if (isBlack(startX+2, startY+3)) count++;
         if (isBlack(startX+2, startY+4)) count++;
         
         if (isBlack(startX+3, startY+1)) count++;
         if (isBlack(startX+3, startY+2)) count++;
         if (isBlack(startX+3, startY+3)) count++;
         
         if (isBlack(startX+4, startY+2)) count++;

         return count>=10;
    }
    
    
    private boolean isBlack (int x, int y) {
         int color = new Color(originalImage.getRGB(x, y)).getRed();
         int black = 0;
         return (color == black);
    }
   
    @Override
    public String toString() {
        return "Lung Erosion";
    }
    
    
}
