/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package imagepreprocessing.filter.impl;

import imagepreprocessing.filter.ImageFilter;
import java.awt.image.BufferedImage;

/**
 *
 * @author Mihailo
 */
public class OrientationFilter implements ImageFilter{
    
    private BufferedImage originalImage;
    private BufferedImage filteredImage;
	
    @Override
    public BufferedImage processImage(BufferedImage image) {
		
        originalImage =  image;
		
	int width = originalImage.getWidth();
	int height = originalImage.getHeight();
		
	filteredImage = new BufferedImage(width, height, originalImage.getType());
        
        
        
        
        
        
        
        
        
        
        
        
        
        return filteredImage;
    }
    
    
}
