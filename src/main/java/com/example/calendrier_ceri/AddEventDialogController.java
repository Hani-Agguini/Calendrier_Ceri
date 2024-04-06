package com.example.calendrier_ceri;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddEventDialogController {

    @FXML
    private TextField summaryField, enseignantField, matiereField, salleField, descriptionField, locationField;


    @FXML
    private void handleAddEvent() {
        String summary = summaryField.getText();
        String enseignant = enseignantField.getText();
        String matiere = matiereField.getText();
        String salle = salleField.getText();
        String description = descriptionField.getText();
        String location = locationField.getText();
        closeDialog();
    }

    @FXML
    private void handleCancel() {
        closeDialog();
    }

    private void closeDialog() {
        Stage stage = (Stage) summaryField.getScene().getWindow();
        stage.close();
    }
}
