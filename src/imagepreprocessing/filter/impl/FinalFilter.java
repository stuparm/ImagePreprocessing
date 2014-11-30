/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package imagepreprocessing.filter.impl;

import java.awt.image.BufferedImage;

/**
 *
 * @author Mihailo Stupar
 */
public class FinalFilter {
    
    private int width;
    private int height;

    private BufferedImage originalImage;
    private BufferedImage smallerOriginal;
    
    
    private static FinalFilter instance = null;
    
    public static FinalFilter getInstance () {
        if (instance == null)
            instance = new FinalFilter();
        return instance;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setOriginalImage(BufferedImage originalImage) {
        this.originalImage = originalImage;
    }

    public BufferedImage getOriginalImage() {
        return originalImage;
    }

    public void setSmallerOriginal(BufferedImage smallerOriginal) {
        this.smallerOriginal = smallerOriginal;
    }

    public BufferedImage getSmallerOriginal() {
        return smallerOriginal;
    }

    
    
    
    
    
}
