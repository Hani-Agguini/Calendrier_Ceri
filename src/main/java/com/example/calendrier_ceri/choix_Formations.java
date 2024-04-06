package com.example.calendrier_ceri;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.EventObject;
import java.util.ResourceBundle;

public class choix_Formations implements Initializable {

    @FXML
    private ToggleGroup uniqueToggleGroup;
    @FXML
    private AnchorPane choixPane;
    boolean isProf =false;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        applyDarkMode(ThemeManager.darkModeActiveProperty().get());
        ThemeManager.darkModeActiveProperty().addListener((obs, oldVal, isDarkMode) -> {
            applyDarkMode(isDarkMode);
        });
    }
    private void applyDarkMode(boolean isDarkMode) {
        if (isDarkMode) {
            if (choixPane != null) {
                choixPane.getStyleClass().remove("style");
                choixPane.getStyleClass().add("dark-mode");
            }
        } else {
            if (choixPane != null) {
                choixPane.getStyleClass().remove("dark-mode");
                choixPane.getStyleClass().add("style");
            }
        }
    }
    @FXML
    private void handleConfirmSelection(ActionEvent event) throws IOException {
        // Obtenez la formation choisie
        RadioButton selectedFormation = (RadioButton) uniqueToggleGroup.getSelectedToggle();
        if (selectedFormation != null) {
            String formation = selectedFormation.getText();
            System.out.println("Formation sélectionnée : " + formation);

            // Chargez le calendrier et configurez la formation
            FXMLLoader loader = new FXMLLoader(getClass().getResource("calendrier.fxml"));
            Parent root = loader.load();
            calendrierController controller = loader.getController();
            controller.setProfVisibility(isProf);
            controller.setFormation(formation); // Passez la formation choisie

            // Affichez le calendrier
            Scene scene = new Scene(root,Mainapp.dim1,Mainapp.dim2);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } else {
            System.out.println("Aucune formation n'a été sélectionnée.");
        }
    }


}
