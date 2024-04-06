package com.example.calendrier_ceri;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.EventObject;

public class choix_Formations {


    @FXML
    private ToggleGroup toggleGroup;

    @FXML
    private void handleConfirmSelection(ActionEvent event) throws IOException {
        // Obtenez la formation choisie
        RadioButton selectedFormation = (RadioButton) toggleGroup.getSelectedToggle();
        if (selectedFormation != null) {
            String formation = selectedFormation.getText();
            System.out.println("Formation sélectionnée : " + formation);

            // Chargez le calendrier et configurez la formation
            FXMLLoader loader = new FXMLLoader(getClass().getResource("calendrier.fxml"));
            Parent root = loader.load();
            calendrierController controller = loader.getController();
            controller.setFormation(formation); // Passez la formation choisie

            // Affichez le calendrier
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } else {
            System.out.println("Aucune formation n'a été sélectionnée.");
        }
    }


}
