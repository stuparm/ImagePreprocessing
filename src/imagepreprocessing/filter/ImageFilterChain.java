/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imagepreprocessing.filter;

import imagepreprocessing.helper.FilteredImage;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Process images by applying all filters in chain 
 * @author Sanja
 */
public class ImageFilterChain implements ImageFilter {

    private List<ImageFilter> filters = new ArrayList<>();
    
    /**
     * Add filter to chain
     * @param filter filter to be added
     */
    public void addFilter(ImageFilter filter){
        filters.add(filter);
    }
    /**
     * Remove filter from chain
     * @param filter filter to be removed
     * @return true if filter is removed 
     */
    public boolean removeFilter(ImageFilter filter){
        return filters.remove(filter);
    }

    /**
     * Apply all filters from a chain on image 
     * @param image image to process
     * @return processed image
     */
    @Override
    public BufferedImage processImage(BufferedImage image) {
        
        BufferedImage tempImage = image;
        for (ImageFilter filter : filters) {
            BufferedImage filteredImage = filter.processImage(tempImage);
            tempImage = filteredImage;
        }

        return tempImage;

    }
    /**
     * Returns images of all stages in processing
     * Used for testing 
     * @param image
     * @return 
     */
    public List<FilteredImage> processImageTest(BufferedImage image){
        List<FilteredImage> list = new ArrayList<>();
        BufferedImage tempImage = image;
        for (ImageFilter filter : filters) {
            BufferedImage processedImage = filter.processImage(tempImage);
            String filterName = filter.toString();
            FilteredImage filteredImage = new FilteredImage(processedImage,filterName);
            list.add(filteredImage);
            tempImage = processedImage;
        }

        return list;
    }

}
