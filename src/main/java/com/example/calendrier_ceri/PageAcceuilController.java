package com.example.calendrier_ceri;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PageAcceuilController extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("pageAcceuil.fxml"));
        primaryStage.setTitle("Calendrier CERI");
        primaryStage.setScene(new Scene(root, 400, 400));
        primaryStage.show();
    }
    @FXML
    private TextField pseudoField;
    @FXML
    private PasswordField passwordField;

    @FXML
    private void connexionAction(ActionEvent event) {
        String pseudo = pseudoField.getText();
        String motDePasse = passwordField.getText();
        // Appeler votre fonction avec le pseudo et le mot de passe
        Connexion connexion = new Connexion();
        System.out.println(connexion.verifier_utilisateur(pseudo, motDePasse));
    }

}
