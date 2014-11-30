/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package imagepreprocessing.filter.impl;

import imagepreprocessing.filter.ImageFilter;
import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 *
 * @author Stupi
 */
public class LungCrop implements ImageFilter{
    
    private BufferedImage originalImage;
    private BufferedImage filteredImage;

    @Override
    public BufferedImage processImage(BufferedImage image) {

        originalImage = image;

        int width = image.getWidth();
        int height = image.getHeight();
       
    
        int black = 0;
        
        
        int startX = 10;
        leftLoop:
        for (; startX < width; startX++) {
            for (int j = 10; j < height; j++) {
                int color = new Color(originalImage.getRGB(startX, j)).getRed();
                if (color != black) {
                    break leftLoop;
                }
            }
        }

        int endX = width-10;
        rightLoop:
        for (;  endX > 0; endX--) {
            for (int j = 10; j < height; j++) {
                int color = new Color(originalImage.getRGB(endX, j)).getRed();
                if (color != black) {
                    break rightLoop;
                }
            } 
        }
        
        int endY = height-10;
        downLoop:
        for (; endY > 0; endY--) {
            for (int i = 10;  i < width; i++) {
                int color = new Color(originalImage.getRGB(i, endY)).getRed();
                if (color != black) {
                    break downLoop;
                }
            }
        }
 
        int startY = 10;
        upLoop:
        for (; startY < height; startY++) {
            for (int i = 10; i < width; i++) {
                int color = new Color(originalImage.getRGB(i, startY)).getRed();
                if (color != black) {
                    break upLoop;
                }
            }
        }
        
        
       
        
        
        
//        System.out.println(startX+" "+endX+" "+""+startY+" "+endY);
        int newWidth = endX-startX+1;
        int newHeight = endY-startY+1;
        
        filteredImage = new BufferedImage(newWidth, newHeight, originalImage.getType());
        
        for (int i = 0; i < newWidth; i++) {
            for (int j = 0; j < newHeight; j++) {
                int x = startX + i;
                int y = startY + j;
                int rgb = new Color(originalImage.getRGB(x, y)).getRGB();
                filteredImage.setRGB(i, j, rgb );
            }
        }
        BufferedImage firstImage = FinalFilter.getInstance().getOriginalImage();
        createSmallerOriginal(firstImage, startX, endX, startY, endY);
        
        return filteredImage;
    }

    @Override
    public String toString() {
        return "Lung crop";
    }
    
    private void createSmallerOriginal (BufferedImage firstImage, int startX, int endX, int startY, int endY) {
        
        int newWidth = endX-startX+1;
        int newHeight = endY-startY+1;
        
        BufferedImage smallerOriginal = new BufferedImage(newWidth, newHeight, firstImage.getType());
        
        for (int i = 0; i < newWidth; i++) {
            for (int j = 0; j < newHeight; j++) {
                int rgb = new Color(firstImage.getRGB(startX+i, startY+j)).getRGB();
                smallerOriginal.setRGB(i, j, rgb);
            }
        }
        
        FinalFilter.getInstance().setSmallerOriginal(smallerOriginal);
        
    }
    
    
}
