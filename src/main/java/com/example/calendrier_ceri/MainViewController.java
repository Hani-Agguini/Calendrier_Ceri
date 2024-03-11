package com.example.calendrier_ceri;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainViewController {
    public void handleWeekView(ActionEvent event) throws Exception {
        // Chargez la vue de la semaine
        Parent weekView = FXMLLoader.load(getClass().getResource("calendrierSemaine.fxml"));
        Scene weekScene = new Scene(weekView);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(weekScene);
        stage.show();
    }

    public void handleMonthView(ActionEvent event) throws Exception {
        // Chargez la vue du mois
        Parent monthView = FXMLLoader.load(getClass().getResource("calendrier.fxml"));
        Scene monthScene = new Scene(monthView);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(monthScene);
        stage.show();
    }
}
