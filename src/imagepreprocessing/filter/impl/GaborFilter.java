/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imagepreprocessing.filter.impl;

import imagepreprocessing.filter.ImageFilter;
import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 *
 * @author Mihailo
 */
public class GaborFilter implements ImageFilter {

    private BufferedImage originalImage;
    private BufferedImage filteredImage;

    private int width;
    private int height;

    private int imageArray[];

    //==============================
    private int[] sobelKernel = {-1, 0, 1, -2, 0, 2, -1, 0, 1};
    private int sobelBlockSize = 3;
    private int orientationBlockSize = 10;
    private double[] orientationField;
    //=============================================
    private int frequency = 8;
    private int TEMPLATE_SIZE = 9;
    private double[][] gaborMatrix = new double[33][TEMPLATE_SIZE * TEMPLATE_SIZE];
    //=============================================

    @Override
    public BufferedImage processImage(BufferedImage image) {

        originalImage = image;

        width = originalImage.getWidth();
        height = originalImage.getHeight();

        filteredImage = new BufferedImage(width, height, originalImage.getType());

        imageArray = originalImage.getRGB(0, 0, width, height, null, 0, width);

        calculateOrientation();
        
        int [] total = filterImage(imageArray, frequency);
        
        filteredImage.setRGB(0, 0, width, height, total, 0, width);
        
        return filteredImage;
    }

    public void calculateOrientation() {
        int sobelSum = 0;
        int area = width * height;
        double[] GY = new double[area];
        double[] GX = new double[area];
        orientationField = new double[area];

        int start = (sobelBlockSize - 1) / 2;
        int end = (sobelBlockSize + 1) / 2;
        // Compute Sobel's gradients GX and GY
        for (int x = start; x < width - end; x++) {
            for (int y = start; y < height - end; y++) {
                sobelSum = 0;
                int pixel = y * width + x;
                for (int x1 = 0; x1 < sobelBlockSize; x1++) {
                    for (int y1 = 0; y1 < sobelBlockSize; y1++) {
                        int x2 = (x - (sobelBlockSize - 1) / 2 + x1);
                        int y2 = (y - (sobelBlockSize - 1) / 2 + y1);
                        float value = (imageArray[y2 * width + x2] & 0xff)
                                * (sobelKernel[y1 * sobelBlockSize + x1]);
                        sobelSum += value;
                    }
                }
                GY[pixel] = sobelSum;
                sobelSum = 0;
                for (int x1 = 0; x1 < sobelBlockSize; x1++) {
                    for (int y1 = 0; y1 < sobelBlockSize; y1++) {
                        int x2 = (x - (sobelBlockSize - 1) / 2 + x1);
                        int y2 = (y - (sobelBlockSize - 1) / 2 + y1);
                        float value = (imageArray[y2 * width + x2] & 0xff)
                                * (sobelKernel[x1 * sobelBlockSize + y1]);
                        sobelSum += value;
                    }
                }
                GX[pixel] = sobelSum;
            }
        }
        start = (orientationBlockSize - 1) / 2;
        end = (orientationBlockSize + 1) / 2;

        int gxlength = GX.length;
        double[] GXGY2 = new double[gxlength];
        double[] GXGX_GYGY = new double[gxlength];
        double gxValue = 0;
        double gyValue = 0;
        for (int i = 0; i < gxlength; i++) {
//            if (background[i] != Color.red.getRGB()) {
            GXGY2[i] = 2 * GX[i] * GY[i];
            gxValue = GX[i];
            gyValue = GY[i];
            GXGX_GYGY[i] = gxValue * gxValue - gyValue * gyValue;
//            }
        }

        double FIx[] = new double[area];
        double FIy[] = new double[area];

        int x;
        int y;
        int tempPixel;
        // Compute final direction vectors
        for (int px = start; px < width - end; px++) {
            for (int py = start; py < height - end; py++) {
                int pixel = py * width + px;
//                if (background[pixel] != Color.yellow.getRGB()) {
                double vx = 0;
                double vy = 0;
                for (int tmpX = 0; tmpX < orientationBlockSize; tmpX++) {
                    for (int tmpY = 0; tmpY < orientationBlockSize; tmpY++) {
                        x = (px - start) + tmpX;
                        y = (py - start) + tmpY;
                        tempPixel = y * width + x;
                        vx += GXGY2[tempPixel];
                        vy += GXGX_GYGY[tempPixel];
                    }
                }
                double tempDirection = 0.5 * Math.atan2(vx, vy);
                orientationField[pixel] = tempDirection;
                FIx[pixel] = Math.cos(2.0 * tempDirection);
                FIy[pixel] = Math.sin(2.0 * tempDirection);
//                }
            }
        }

        double[] gausFilter = new double[]{1, 2, 3, 2, 1, 2, 7, 11, 7, 2, 3, 11, 17, 11, 3, 2, 7, 11, 7, 2, 1, 2, 3,
            2, 1};
        // GAUSS smoothening
        for (int px = 2; px < width - 3; px++) {
            for (int py = 2; py < height - 3; py++) {
                int pixel = py * width + px;
                double sumX = 0;
                double sumY = 0;
                for (int x1 = 0; x1 < 5; x1++) {
                    for (int y1 = 0; y1 < 5; y1++) {
                        int u = (px - 2 + x1);
                        int v = (py - 2 + y1);
                        int pixel1 = v * width + u;
                        sumX += gausFilter[y1 * 5 + x1] * FIx[pixel1];
                        sumY += gausFilter[y1 * 5 + x1] * FIy[pixel1];
                    }
                }
                orientationField[pixel] = 0.5 * Math.atan2(sumY, sumX);
            }
        }

        

    }

    //==========================================================================
    //==========================================================================
    public int[] filterImage(int[] input,  int frequency) {
        this.frequency = frequency;
        computeGaborFilters();
        int area = width * height;

        int start = (TEMPLATE_SIZE - 1) / 2;
        int end = (TEMPLATE_SIZE + 1) / 2;

        int[] total = new int[area];

        for (int x = start; x < width - end; x++) {
            for (int y = start; y < height - end; y++) {
                int pixel = y * width + x;
                double finalPixelValue = 0;
                orientationField[pixel] = Math.round(orientationField[pixel] * 10) / 10.0;
                for (int x1 = -TEMPLATE_SIZE / 2; x1 < TEMPLATE_SIZE / 2 + 1; x1++) {
                    for (int y1 = -TEMPLATE_SIZE / 2; y1 < TEMPLATE_SIZE / 2 + 1; y1++) {
                        int u = (x - x1);
                        int v = (y - y1);
                        int pixel1 = v * width + u;
                        double gaborFilter = gaborMatrix[(int) (orientationField[pixel] * 10) + 16][(y1 + TEMPLATE_SIZE / 2)
                                * (int) (TEMPLATE_SIZE) + (x1 + TEMPLATE_SIZE / 2)];
                        double value = (input[pixel1] & 0xff);
                        finalPixelValue += gaborFilter * value;
                    }

                }
                total[pixel] = (int) finalPixelValue;
            }

        }

        for (int i = 0; i < total.length; i++) {
            if (total[i] > 0) {
                total[i] = Color.white.getRGB();
            } else {
                total[i] = Color.black.getRGB();
            }
        }
        deleteFalseBlackReagions(total);
        return total;
    }

    private void computeGaborFilters() {
        for (int i = -16; i < 17; i++) {
            double orientation = (double) i / 10;
            double sinus = Math.sin(orientation);
            double cosinus = Math.cos(orientation);
            for (int x1 = -TEMPLATE_SIZE / 2; x1 < TEMPLATE_SIZE / 2 + 1; x1++) {
                for (int y1 = -TEMPLATE_SIZE / 2; y1 < TEMPLATE_SIZE / 2 + 1; y1++) {
                    double xteta = x1 * sinus + y1 * cosinus;
                    double yteta = -y1 * sinus + x1 * cosinus;
                    double gaborFilter = Math.exp(-0.5 * ((xteta * xteta / 16.0) + (yteta * yteta / 16.0)))
                            * Math.cos((2.0 * Math.PI * xteta / frequency));
                    int y = y1 + TEMPLATE_SIZE / 2;
                    int x = x1 + TEMPLATE_SIZE / 2;
                    System.out.println(gaborMatrix.length+"  "+gaborMatrix[0].length);
                    gaborMatrix[i + 16][y * (int) (TEMPLATE_SIZE) + x] = gaborFilter;
                }
            }
        }
    }

    private void deleteFalseBlackReagions(int[] total) {
        int start = (TEMPLATE_SIZE - 1) / 2;
        int end = (TEMPLATE_SIZE + 1) / 2;
        for (int x = 0; x < start; x++) {
            for (int y = 0; y < height - end; y++) {
                int pixel = y * width + x;
                total[pixel] = Color.white.getRGB();
            }
        }
        for (int x = width - end; x < width; x++) {
            for (int y = 0; y < height - end; y++) {
                int pixel = y * width + x;
                total[pixel] = Color.white.getRGB();
            }
        }
        for (int x = 0; x < width - end; x++) {
            for (int y = 0; y < start; y++) {
                int pixel = y * width + x;
                total[pixel] = Color.white.getRGB();
            }
        }
        for (int x = 0; x < width - end; x++) {
            for (int y = height - end; y < height; y++) {
                int pixel = y * width + x;
                total[pixel] = Color.white.getRGB();
            }
        }
    }

    @Override
    public String toString() {
        return "Gabor Filter";
    }
    
    
}
