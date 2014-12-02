/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package imagepreprocessing.ocr.workflow;

import imagepreprocessing.filter.impl.GrayscaleFilter;
import imagepreprocessing.filter.impl.SegmentationOCRFilter;
import java.util.ArrayList;


/**
 *
 * @author Mihailo
 */
public class Test {
    
    public static void main(String[] args) {
        
        
        String pathText = "C:/Users/Mihailo/Desktop/OCR workflow/part_75.txt";
        String pathImage = "C:/Users/Mihailo/Desktop/OCR workflow/part_75.png";
        Reader.readImage(pathImage);
        Reader.readText(pathText);
        
        
        Filters filters = new Filters();
        filters.addFilter(new GrayscaleFilter());
        filters.addFilter(new SegmentationOCRFilter());
        filters.processImage();
        
        System.out.println("Creating letters...");
        String locationFolder = "C:\\Users\\Mihailo\\Desktop\\OCR workflow\\letters\\";
        LetterCreator letterCreator = new LetterCreator(locationFolder);
        letterCreator.createLetterImage(80, 80); //dimensions of the image
        
        ArrayList<String> letterLabels = Share.getInstance().getLetterLabels();
        
        
    }
    
    
    
}
