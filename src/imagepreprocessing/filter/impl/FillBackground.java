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
import java.util.LinkedList;

/**
 *
 * @author Stupi
 */
public class FillBackground implements ImageFilter {

    private BufferedImage originalImage;
    private BufferedImage filteredImage;

    @Override
    public BufferedImage processImage(BufferedImage image) {

        originalImage = image;

        int width = image.getWidth();
        int height = image.getHeight();
        filteredImage = new BufferedImage(width, height, originalImage.getType());

        boolean[][] visited = new boolean[width][height];
        BFS(1, 1, visited);

        fillBlack(visited);

        return filteredImage;
    }

    public void BFS(int startI, int startJ, boolean[][] visited) {
        LinkedList<Point> queue = new LinkedList<Point>();

        Point point = new Point(startI, startJ);
        visited[startI][startJ] = true;
        queue.addLast(point);

        int white = 255;
        int gray = 125;
        int black = 0;

        while (!queue.isEmpty()) {
            Point p = queue.removeFirst();
            int x = p.x;
            int y = p.y;
            visited[x][y] = true;
            int alpha = new Color(originalImage.getRGB(x, y)).getAlpha();
            int rgb = PreprocessingHelper.colorToRGB(alpha, gray, gray, gray);
            filteredImage.setRGB(x, y, rgb);

            for (int i = x - 1; i <= x + 1; i++) {
                for (int j = y - 1; j <= y + 1; j++) {
                    if (i >= 0 && j >= 0 && i < originalImage.getWidth() && j < originalImage.getHeight()) {
                        if (!visited[i][j]) {
                            int color = new Color(originalImage.getRGB(i, j)).getRed();
                            if (color == white) {
                                visited[i][j] = true;
                                queue.addLast(new Point(i, j));
                            }
                        }
                    }
                } //i
            } //j

        }//while

    }

    public void fillBlack (boolean [][] visited) {
        int black = 0;
        int white = 255;
        
        int width = visited.length;
        int height = visited[0].length;
        
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int rgb;
                int alpha = new Color(originalImage.getRGB(i, j)).getAlpha();
                if (!visited[i][j]) {
                    
                    rgb = PreprocessingHelper.colorToRGB(alpha, black, black, black);
                    
                }
                else {
                    rgb = PreprocessingHelper.colorToRGB(alpha, white, white, white);
                }
                filteredImage.setRGB(i, j, rgb);
                
            }
        }
        
        
    }

    

    class Point {

        int x;
        int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

    }

    @Override
    public String toString() {
        return "Fill with gray";
    }

}
