/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package imagepreprocesing.ocr.workflow;

import java.awt.image.BufferedImage;

/**
 *
 * @author Mihailo
 */
public class Share {
    
    private String text;
    private BufferedImage image;

    public String getText() {
        return text;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }
    

    private static Share instance = null;
    
    public static Share getInstance() {
        if (instance == null)
            instance = new Share();
        return instance;
    }
    
    
}
