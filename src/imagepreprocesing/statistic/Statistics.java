/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imagepreprocesing.statistic;

import imagepreprocessing.helper.Point;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Stupi
 */
public class Statistics {

    static ArrayList<ArrayList<Point>> pointList = new ArrayList<ArrayList<Point>>();

    public static ArrayList<ArrayList<Point>> getPointList() {
        return pointList;
    }

    public void setPointList(ArrayList<ArrayList<Point>> pointList) {
        this.pointList = pointList;
    }

    public static int calcultePerimeter(ArrayList<Point> contour) {
        boolean[][] matrix = new boolean[contour.size()][contour.size()]; //Promenimo ovo
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        for (Point p : contour) {
            if (p.x < minX) {
                minX = p.x;
            }
            if (p.y < minY) {
                minY = p.y;
            }
        }
        for (Point p : contour) {
            matrix[p.x - minX][p.y - minY] = true;
        }

        int perimeter = 0;

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j]) {
                    if ((i - 1) > 0 && matrix[i - 1][j] == false) {
                        perimeter++;
                        continue;
                    }
                    if ((i + 1) < matrix.length && matrix[i+1][j] == false) {
                        perimeter++;
                        continue;
                    }
                    if ((j - 1) > 0 && matrix[i][j-1] == false) {
                        perimeter++;
                        continue;
                    }
                    if ((j + 1) > matrix[0].length && matrix[i][j+1] == false) {
                        perimeter++;
                        continue;
                    }
                }
            }
        }
        return perimeter;
    }
    
    public static void printPerimeters() {
        System.out.println("===");
        for (ArrayList<Point> element : pointList) {
            
            int result = Statistics.calcultePerimeter(element);
            System.out.println(element.size()+"   "+result);
        }
    }

}
