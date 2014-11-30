/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imagepreprocessing.filter.impl;

import imagepreprocesing.statistic.Statistics;
import imagepreprocessing.filter.ImageFilter;
import imagepreprocessing.helper.Point;
import imagepreprocessing.helper.PreprocessingHelper;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Stupi
 */
public class ClearLowNoise implements ImageFilter {

    private BufferedImage originalImage;
    private BufferedImage filteredImage;

    private int treshold = 14;

    @Override
    public BufferedImage processImage(BufferedImage image) {

        originalImage = image;
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        boolean[][] visited = new boolean[width][height];
//        boolean[][] visitedFill = new boolean[width][height];
        filteredImage = new BufferedImage(width, height, originalImage.getType());

        int white = 255;
        int black = 0;

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int color = new Color(originalImage.getRGB(i, j)).getRed();
                if (color == black) {
                    visited[i][j] = true;
                    int alpha = new Color(originalImage.getRGB(i, j)).getAlpha();
                    int rgb = PreprocessingHelper.colorToRGB(alpha, black, black, black);
                    filteredImage.setRGB(i, j, rgb);
                }
                if (color == white && visited[i][j] == false) {
                    List<Point> list = BFS(i, j, visited);
                    if (list.size() < treshold) {
                        for (Point p : list) {
                            int alpha = new Color(originalImage.getRGB(p.x, p.y)).getAlpha();
                            int rgb = PreprocessingHelper.colorToRGB(alpha, black, black, black);
                            filteredImage.setRGB(p.x, p.y, rgb);
                        }
                    } else {
                        Statistics.getPointList().add((ArrayList<Point>) list);
                        for (Point p : list) {
                            int alpha = new Color(originalImage.getRGB(p.x, p.y)).getAlpha();
                            int rgb = PreprocessingHelper.colorToRGB(alpha, white, white, white);
                            filteredImage.setRGB(p.x, p.y, rgb);
                        }
                    }
                }
            }
        }
        Statistics.printPerimeters();
        return filteredImage;
    }

    public List<Point> BFS(int i, int j, boolean[][] visited) {
        LinkedList<Point> queue = new LinkedList<>();
        Point p = new Point(i, j);
        queue.addLast(p);
        List<Point> list = new ArrayList<>();
        list.add(p);
        while (!queue.isEmpty()) {
            p = queue.removeFirst();
            for (int k = p.x - 1; k <= p.x + 1; k++) {
                for (int l = p.y - 1; l <= p.y + 1; l++) {
                    if (k >= 0 && k < originalImage.getWidth() && l >= 0 && l < originalImage.getHeight()) {
                        int color = new Color(originalImage.getRGB(k, l)).getRed();
                        if (color == 255 && !visited[k][l]) {
                            list.add(new Point(k, l));
                            queue.addLast(new Point(k, l));
                            visited[k][l] = true;
                        }
                    }
                }
            }
        }
        return list;
    }

}
