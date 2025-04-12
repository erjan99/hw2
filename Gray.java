package com.example.hw2;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class Gray {
    public static WritableImage grayscale(Image image) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        WritableImage newImage = new WritableImage(width, height);
        PixelReader reader = image.getPixelReader();
        PixelWriter writer = newImage.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = reader.getColor(x, y);
                int red = (int) (color.getRed() * 255);
                int green = (int) (color.getGreen() * 255);
                int blue = (int) (color.getBlue() * 255);
                int avg = (red + green + blue) / 3;

                writer.setColor(x, y, new Color(avg / 255.0, avg / 255.0, avg / 255.0, color.getOpacity()));
            }
        }
        return newImage;
    }
}
