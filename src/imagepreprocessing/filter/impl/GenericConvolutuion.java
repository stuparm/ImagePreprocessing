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
public class GenericConvolutuion implements ImageFilter {

   private BufferedImage originalImage;
    private BufferedImage filteredImage;

    private double [][] kernel = {{0,0,0},{0,1,0},{0,0,0}};
    private boolean normalize = true;
    
    private double treshold;
    private boolean binarize;

    public void setNormalize(boolean normalize) {
        this.normalize = normalize;
    }

    public void setTreshold(double treshold) {
        this.treshold = treshold;
    }

    public void setBinarize(boolean binarize) {
        this.binarize = binarize;
    }

    public void setKernel(double[][] kernel) {
        if (kernel.length % 2 == 0) {
            System.out.println("ERROR!");
        }
        this.kernel = kernel;
    }
    
    
    
    @Override
    public BufferedImage processImage(BufferedImage image) {

        originalImage = image;
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        filteredImage = new BufferedImage(width, height, originalImage.getType());
        
        int radius = kernel.length/2;
        
        if (normalize) {
            normalizeKernel();
        }
        
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                
                double result = convolve(i, j, radius);
                
                if (binarize) {
                    if (result > treshold) 
                        result = 255;
                    else
                        result = 0;
                }
                
                int gray = (int)Math.round(result);
                
                int alpha = new Color(originalImage.getRGB(i, j)).getAlpha();
                int rgb =PreprocessingHelper.colorToRGB(alpha, gray, gray, gray);
                
                filteredImage.setRGB(i, j, rgb);
            }
        }
        
        
        

        return filteredImage;
    }

    
    public double convolve(int x, int y, int radius) {
        
        double sum = 0;
        int kernelI = 0;
        for (int i = x-radius; i <= x+radius; i++) {
            int kernelJ = 0;
            for (int j = y-radius; j <= y+radius; j++) {              
                if (i>0 && i<originalImage.getWidth() && j>0 && j<originalImage.getHeight()) {                
                    int color = new Color(originalImage.getRGB(i, j)).getRed();
                    sum = sum + color*kernel[kernelI][kernelJ];
                    
                }
                kernelJ++;
            }
            kernelI++;
        }
        
        return sum;
    } 

    @Override
    public String toString() {
        return "Generic convolution";
    }
    
    public void normalizeKernel() {
        int n = 0;
        for (int i = 0; i < kernel.length; i++) {
            for (int j = 0; j < kernel.length; j++) {
                n+=kernel[i][j];
            }
            
        }
        for (int i = 0; i < kernel.length; i++) {
            for (int j = 0; j < kernel.length; j++) {
                kernel[i][j] = kernel[i][j]/n;
            }
            
        }
    }
    
    
}
