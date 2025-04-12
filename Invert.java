package com.example.hw2;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class Invert {
    public static WritableImage invertColors(Image image) {
        if (image == null) return null;

        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        WritableImage output = new WritableImage(width, height);
        PixelWriter writer = output.getPixelWriter();
        PixelReader reader = image.getPixelReader();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color original = reader.getColor(x, y);

                Color inverted = new Color(
                        1.0 - original.getRed(),
                        1.0 - original.getGreen(),
                        1.0 - original.getBlue(),
                        original.getOpacity()
                );
                writer.setColor(x, y, inverted);
            }
        }
        return output;
    }
}
