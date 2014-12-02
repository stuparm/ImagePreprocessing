/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package imagepreprocessing.ocr.workflow;

import imagepreprocessing.filter.ImageFilter;
import imagepreprocessing.filter.ImageFilterChain;
import java.awt.image.BufferedImage;

/**
 *
 * @author Mihailo
 */
public class Filters {
    
    private BufferedImage image;
    private ImageFilterChain filterChain;
    
    public Filters() {
        image = Share.getInstance().getImage();
        filterChain = new ImageFilterChain();   
    }
    
    public void addFilter (ImageFilter filter) {
        filterChain.addFilter(filter);
    }
    
    public void processImage () {
        
        BufferedImage filtered = filterChain.processImage(image);
        Share.getInstance().setImage(filtered);
    }
    
    
    
    
}
