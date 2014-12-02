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
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Mihailo
 */
public class OCRSeparationFilter implements ImageFilter {

    private BufferedImage originalImage;
    private int width;
    private int height;

    private boolean[][] visited;

    private int letterWidth;
    private int letterHeight;
    private String location;

    private int[] counts;

    private int[] linePositions = null;
    private ArrayList<String> letterLabels;

    private String text;
    private int seqNum = 0;

    public OCRSeparationFilter() {
    }

    public OCRSeparationFilter(int letterWidth, int letterHeight, String location, String text) {
        this.letterWidth = letterWidth;
        this.letterHeight = letterHeight;
        this.location = location;
        this.text = text;
        letterLabels = new ArrayList<>();
    }

    @Override
    public BufferedImage processImage(BufferedImage image) {

        originalImage = image;
        width = originalImage.getWidth();
        height = originalImage.getHeight();

        prepare();

        visited = new boolean[height][width];

        int color;
        int white = 255;

        for (int line = 0; line < linePositions.length; line++) {
            for (int k = -1; k <= 1; k++) {
                int i = linePositions[line]+k;
                if (i == -1 || i == height)
                    continue;;
//        for (int i = 0; i < height; i++) {

                for (int j = 0; j < width; j++) {

                    color = new Color(originalImage.getRGB(j, i)).getRed();
                    if (color == white) {
                        visited[i][j] = true;
                    } else {
                        BFStraverse(i, j);

                    }

                }
            }
        }
        return originalImage;
    }

    private void BFStraverse(int startI, int startJ) {

        int gapWidth = letterWidth / 5 * 2;  //start x coordinate of letter, 2/5 itended
        int gapHeight = letterHeight / 5 * 2;  //start y coordinate of letter 

        LinkedList<String> queue = new LinkedList<>();

        BufferedImage letter = new BufferedImage(letterWidth, letterHeight, originalImage.getType());
        int alpha = new Color(originalImage.getRGB(startJ, startI)).getAlpha();
        int white = PreprocessingHelper.colorToRGB(alpha, 255, 255, 255);
        int black = PreprocessingHelper.colorToRGB(alpha, 0, 0, 0);

        // fill all letter image with white pixels
        for (int i = 0; i < letterHeight; i++) {
            for (int j = 0; j < letterWidth; j++) {
                letter.setRGB(j, i, white);
            }
        }

        int countPixels = 0; // ignore dots
        String positions = startI + " " + startJ;
        visited[startI][startJ] = true;
        queue.addLast(positions);
        while (!queue.isEmpty()) {
            String pos = queue.removeFirst();
            String[] posArray = pos.split(" ");
            int H = Integer.parseInt(posArray[0]); // H-height
            int W = Integer.parseInt(posArray[1]); // W-width
            visited[H][W] = true;

            int posW = W - startJ + gapWidth;
            int posH = H - startI + gapHeight;

            countPixels++;

            letter.setRGB(posW, posH, black);

            int color;
            int blackInt = 0;
            for (int i = H - 1; i <= H + 1; i++) {
                for (int j = W - 1; j <= W + 1; j++) {
                    if (i >= 0 && j >= 0 && i < originalImage.getHeight() && j < originalImage.getWidth()) {
                        if (!visited[i][j]) {
                            color = new Color(originalImage.getRGB(j, i)).getRed();
                            if (color == blackInt) {
                                visited[i][j] = true;
                                String tmpPos = i + " " + j;
                                queue.addLast(tmpPos);
                            }
                        }
                    }
                }
            }
        }

        if (countPixels < 35) { //da ne bi uzimao male crtice, tacke
            return;
        }

        String name = createName();
        saveToFile(letter, seqNum+"_"+name); //potrebno je izbaciti seqNum i ostaviti samo name
        
        seqNum++;

    }

    private void saveToFile(BufferedImage img, String letterName) {
        File outputfile = new File(location + letterName + ".png");
        try {
            ImageIO.write(img, "png", outputfile);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void prepare() {
        counts = new int[52];
        for (int i = 0; i < counts.length; i++) {
            counts[i] = 1;
        }
        String pom = "";
        for (int i = 0; i < text.length(); i++) {
            if (Character.isLetter(text.charAt(i))) {
                pom += text.charAt(i);
            }
        }
        text = pom;
        //====================================================
        if (linePositions == null) {
            linePositions = new int[height];

            for (int i = 0; i < linePositions.length; i++) {
                linePositions[i] = i;
            }
        }

    }

    private String createName() {

        int offsetBIG = 65;
        int offsetSMALL = 97;
        int offsetARRAY = 26;

        char c = text.charAt(seqNum);
        int key = c;
//        System.out.println(key+" "+c);
        int number;
        if (key < 95) { //smallLetter
            number = counts[key - offsetBIG];
            counts[key - offsetBIG]++;
        } else { //big letter
            number = counts[key - offsetSMALL + offsetARRAY];
            counts[key - offsetSMALL + offsetARRAY]++;
        }
        String name = c + "_" + number;
        letterLabels.add(c+"");
        return name;
    }

    public void setLinePositions(int[] linePositions) {
        this.linePositions = linePositions;
    }

    public ArrayList<String> getLetterLabels() {
        return letterLabels;
    }

    
    
    
}
