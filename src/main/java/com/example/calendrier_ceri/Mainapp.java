package com.example.calendrier_ceri;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Mainapp extends Application {
    static int dim1=800;
    static int dim2=500;
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("pageAcceuil.fxml"));
        Scene scene = new Scene(root, dim1, dim2);
        primaryStage.setTitle("Calendrier CERI");
        primaryStage.setScene(scene);

        // Ajouter des écouteurs pour ajuster la taille de la fenêtre dynamiquement
        primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> {
            // Ici, vous pouvez ajuster dim1 ou effectuer d'autres actions avec la nouvelle valeur
            dim1 = newVal.intValue();
        });

        primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> {
            // Ici, vous pouvez ajuster dim2 ou effectuer d'autres actions avec la nouvelle valeur
            dim2 = newVal.intValue();
        });

        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}