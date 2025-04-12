package com.example.hw2;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class Blur {
    public static WritableImage blur(Image image) {
        if (image == null) return null;

        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        PixelReader reader = image.getPixelReader();
        WritableImage result = new WritableImage(width, height);
        PixelWriter writer = result.getPixelWriter();

        int radius = 9;
        int diameter = 2 * radius + 1;
        double[][] kernel = createBoxKernel(diameter);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double r = 0, g = 0, b = 0, a = 0;
                double weightSum = 0;

                for (int ky = -radius; ky <= radius; ky++) {
                    for (int kx = -radius; kx <= radius; kx++) {
                        int nx = x + kx;
                        int ny = y + ky;

                        if (nx >= 0 && nx < width && ny >= 0 && ny < height) {
                            Color color = reader.getColor(nx, ny);
                            double weight = kernel[ky + radius][kx + radius];
                            r += color.getRed() * weight;
                            g += color.getGreen() * weight;
                            b += color.getBlue() * weight;
                            a += color.getOpacity() * weight;
                            weightSum += weight;
                        }
                    }
                }

                Color blurredColor = new Color(
                        r / weightSum,
                        g / weightSum,
                        b / weightSum,
                        a / weightSum
                );
                writer.setColor(x, y, blurredColor);
            }
        }

        return result;
    }

    private static double[][] createBoxKernel(int size) {
        double[][] kernel = new double[size][size];
        double value = 1.0 / (size * size);

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                kernel[y][x] = value;
            }
        }
        return kernel;
    }
}
