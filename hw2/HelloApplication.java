package com.example.hw2;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class HelloApplication extends Application {
    private Image originalImage; // Store the original image

    @Override
    public void start(Stage stage) {
        VBox root = new VBox();
        HBox h1 = new HBox();
        HBox h2 = new HBox();

        root.getChildren().add(h1);
        root.getChildren().add(h2);

        Button openImage = new Button("Select Image");
        ComboBox<String> choice = new ComboBox<>();
        choice.getItems().addAll("Grayscale", "Inversion", "Blur");
        Button process = new Button("Process Image");
        Button save = new Button("Save Image");

        h1.getChildren().addAll(openImage, choice, process, save);

        ImageView orig = new ImageView();
        ImageView result = new ImageView();

        h2.getChildren().addAll(orig, result);

        orig.setFitWidth(600);
        orig.setFitHeight(600);
        result.setFitWidth(600);
        result.setFitHeight(600);

        // Open Image Button Action
        openImage.setOnAction((ActionEvent event) -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new ExtensionFilter(
                    "Image Files", "*.jpeg", "*.png", "*.jpg", "*.webp"
            ));
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                originalImage = new Image(file.toURI().toString());
                orig.setImage(originalImage);
            }
        });

        // Process Image Button Action
        process.setOnAction((ActionEvent event) -> {
            if (originalImage != null && choice.getValue() != null) {
                WritableImage processedImage = null;
                switch (choice.getValue()) {
                    case "Grayscale":
                        processedImage = Gray.grayscale(originalImage);
                        break;
                    case "Inversion":
                        processedImage = Invert.invertColors(originalImage);
                        break;
                    case "Blur":
                        processedImage = Blur.blur(originalImage); // Assuming blur method is in scope
                        break;
                }
                if (processedImage != null) {
                    result.setImage(processedImage);
                }
            }
        });

        // Save Image Button Action (Without javax.imageio)
        save.setOnAction((ActionEvent event) -> {
            if (result.getImage() != null) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().add(new ExtensionFilter("PNG Files", "*.png"));
                File file = fileChooser.showSaveDialog(stage);
                if (file != null) {
                    try {
                        saveImageAsPNG(result.getImage(), file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        Scene scene = new Scene(root, 1280, 720);
        stage.setTitle("Image Processing");
        stage.setScene(scene);
        stage.show();
    }

    private void saveImageAsPNG(Image image, File file) throws IOException {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        WritableImage writableImage = new WritableImage(width, height);
        PixelReader pixelReader = image.getPixelReader();
        PixelWriter pixelWriter = writableImage.getPixelWriter();

        // Copy image pixels to writable image
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = pixelReader.getColor(x, y);
                pixelWriter.setColor(x, y, color);
            }
        }

        // Convert the WritableImage to a byte array for PNG format
        ByteBuffer buffer = ByteBuffer.allocate(4 * width * height);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = writableImage.getPixelReader().getColor(x, y);
                buffer.put((byte) (color.getRed() * 255));
                buffer.put((byte) (color.getGreen() * 255));
                buffer.put((byte) (color.getBlue() * 255));
                buffer.put((byte) (color.getOpacity() * 255));
            }
        }

        // Write the byte data to the file as PNG
        try (FileOutputStream fos = new FileOutputStream(file);
             FileChannel channel = fos.getChannel()) {
            channel.write(buffer);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
