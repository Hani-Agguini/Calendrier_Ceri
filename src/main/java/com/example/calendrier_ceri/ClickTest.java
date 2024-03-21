package com.example.calendrier_ceri;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class ClickTest extends Application {
    @Override
    public void start(Stage primaryStage) {
        Rectangle rect = new Rectangle(100, 100, Color.BLUE);
        rect.setOnMouseClicked(event -> {
            System.out.println("Rectangle cliqué !");
        });

        StackPane root = new StackPane(rect);
        Scene scene = new Scene(root, 300, 250);

        primaryStage.setTitle("Test Clic");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
