package com.example.calendrier_ceri;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

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
    private void connexionAction(ActionEvent event) throws IOException {
        String pseudo = pseudoField.getText();
        String motDePasse = passwordField.getText();
        // Appeler votre fonction avec le pseudo et le mot de passe
        Connexion connexion = new Connexion();
        if (connexion.verifier_utilisateur(pseudo,motDePasse)){
            Parent monthView = FXMLLoader.load(getClass().getResource("choix_Formations.fxml"));
            Scene monthScene = new Scene(monthView);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(monthScene);
            stage.show();
        }else {
            System.out.println("Connexion pas resussi ");

        }
    }

}
