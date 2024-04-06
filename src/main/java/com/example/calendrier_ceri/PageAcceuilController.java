package com.example.calendrier_ceri;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PageAcceuilController implements Initializable {


    @FXML
    private TextField pseudoField;
    @FXML
    private PasswordField passwordField;

    @FXML
    private AnchorPane acceuilPane;


    @FXML
    private void connexionAction(ActionEvent event) throws IOException {
        String pseudo = pseudoField.getText();
        String motDePasse = passwordField.getText();
        Connexion connexion = new Connexion();
        if (connexion.verifier_utilisateur(pseudo,motDePasse)){
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            double width = stage.getWidth();
            double height = stage.getHeight();

            Parent connexionView = FXMLLoader.load(getClass().getResource("choix_Formations.fxml"));

            Scene choixView = new Scene(connexionView, width, height);

            // Appliquer la nouvelle scène au stage (fenêtre) existant sans changer la taille
            stage.setScene(choixView);


            stage.show();
        }else {
            System.out.println("Connexion pas réussie");
        }
    }

    @FXML
    private void dark_mode(ActionEvent event) {
        System.out.println("Changement de mode sombre");
        ThemeManager.toggleDarkMode();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Initialisation du contrôleur de la page d'accueil");
        // Ici, vous pouvez initialiser ce qui est nécessaire pour votre contrôleur
        ThemeManager.darkModeActiveProperty().addListener((obs, oldVal, isDarkMode) -> {
            if (isDarkMode) {
                System.out.println("Mode sombre activé");
                acceuilPane.getStyleClass().remove("style");
                acceuilPane.getStyleClass().add("dark-mode");
            } else {
                System.out.println("Mode sombre désactivé");
                acceuilPane.getStyleClass().remove("dark-mode");
                acceuilPane.getStyleClass().add("style");
            }
        });
    }

}
