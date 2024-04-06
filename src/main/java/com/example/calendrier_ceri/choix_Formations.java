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
    public  String formation;


    public String getFormation() {
        RadioButton selectedFormation = (RadioButton) toggleGroup.getSelectedToggle();
        if (selectedFormation != null) {
            String formation = selectedFormation.getText();
            System.out.println("Formation sélectionnée : " + formation);
            return formation;
        } else {
            System.out.println("Aucune formation n'a été sélectionnée.");
            return null;
        }
    }

    @FXML
    private ToggleGroup toggleGroup;

    @FXML
    private void handleConfirmSelection(ActionEvent event) throws IOException {

    }


}
