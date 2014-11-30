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
public class InverseDilatation implements ImageFilter{
  
    
    private BufferedImage originalImage;
    private BufferedImage filteredImage;

    private int width;
    private int height;
    
    private int [][] kernel;
    @Override
    public BufferedImage processImage(BufferedImage image) {
        
        originalImage = image;
        
        width = originalImage.getWidth();
        height = originalImage.getHeight();
        
        filteredImage = new BufferedImage(width, height, originalImage.getType());
        
        kernel = create3X3Kernel(); //============================================================
        
        int white = 255;
        int black = 0;
        
        for (int i = 3; i <= width-3; i++) {
            for (int j = 3; j <= height-3; j++) {
                int color = new Color(originalImage.getRGB(i, j)).getRed();
                if (color == white) {
//                    convolve(i, j);
                    convolveDiamond(i, j); //================================================================
                }
                else {
                    int alpha = new Color(originalImage.getRGB(i, j)).getAlpha();
                    int rgb = PreprocessingHelper.colorToRGB(alpha, black, black, black);
                    filteredImage.setRGB(i, j, rgb);
                }         
            }
        }
        return filteredImage;
    }
    
    private int [][] createKernel () {
        int [][] kernel = { {0,1,1,1,0},
                            {1,1,1,1,1},
                            {1,1,1,1,1},
                            {1,1,1,1,1},
                            {0,1,1,1,0}
                          };
        return kernel;
    }
    
    private int [][] create3X3Kernel () {
        int [][] kernel = { {0,1,0},
                            {1,1,1},
                            {0,1,0}
                          };
        return kernel;
    }
    
    
    private void convolve (int i, int j) {
        int size = kernel.length/2;
        for (int x = i-size; x <= i+size; x++) {
            for (int y = j-size; y <= j+size; y++) {
                if (x>=0 && y>=0 && x<width && y<height) {
                    int black = 0;
                    int alpha = new Color(originalImage.getRGB(x, y)).getAlpha();
                    int rgb = PreprocessingHelper.colorToRGB(alpha, black, black, black);
                    filteredImage.setRGB(x, y, rgb);
                }
            }
        }
    }
    
    private void convolveDiamond (int i, int j) {
        int size = kernel.length/2;
        int startX = i - size;
        int startY = j - size;
        setRGBBlack(startX , j);
        setRGBBlack(startX+1, startY+1);
        setRGBBlack(startX+1, startY+2);
        setRGBBlack(startX+1, startY+3);
        for (int k = j-size; k <= j+size; k++) {
            setRGBBlack(startX+2, k);
        }
        setRGBBlack(startX+2, startY+1);
        setRGBBlack(startX+2, startY+2);
        setRGBBlack(startX+2, startY+3);
        setRGBBlack(startX+3 , j);

    }
    
    private void setRGBBlack (int i, int j) {
        int black = 255;
        int alpha = new Color(originalImage.getRGB(i, j)).getAlpha();
        int rgb = PreprocessingHelper.colorToRGB(alpha, black, black, black);
        filteredImage.setRGB(i, j, rgb);
    }
    

    @Override
    public String toString() {
        return "Inverse Dilation";
    }
        
    
    
    
}
