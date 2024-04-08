package com.example.calendrier_ceri;

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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;

public class PageAcceuilController implements Initializable {

    @FXML
    private TextField pseudoField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private AnchorPane acceuilPane;

    static String darkMode = "";

    @FXML
    private void connexionAction(ActionEvent event) throws IOException {
        String pseudo = pseudoField.getText();
        String motDePasse = passwordField.getText();
        Connexion connexion = new Connexion();
        boolean isProf = connexion.verifier_prof(pseudo, motDePasse);

        if (connexion.verifier_utilisateur(pseudo, motDePasse) || isProf) {
            System.out.println("Connexion réussie");
            if (!darkMode.equals("")) {
                connexion.modifier_mode(pseudo, darkMode);
            }else{
                Connexion.pseudo = pseudo;
                String mode_pref = connexion.get_mode(pseudo);
                if (mode_pref.equals("dark")) {
                    ThemeManager.toggleDarkMode();
                }
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("choix_Formations.fxml"));
            Parent root = loader.load();
            choix_Formations controller = loader.getController();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            double width = stage.getWidth();
            double height = stage.getHeight();
            Scene choixView = new Scene(root, Mainapp.dim1, Mainapp.dim2);
            // Appliquer la nouvelle scène au stage (fenêtre) existant sans changer la taille
            stage.setScene(choixView);
            stage.show();
            stage.show();
        } else {
            System.out.println("Connexion pas réussie");
        }
    }

    @FXML
    private void dark_mode(ActionEvent event) {
        ThemeManager.toggleDarkMode();
        if(ThemeManager.darkModeActiveProperty().get()){
            darkMode = "dark";
        }else {
            darkMode = "light";
        }
        System.out.println("Etat Actuelle : " + darkMode);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        acceuilPane.sceneProperty().addListener(new ChangeListener<Scene>() {
            @Override
            public void changed(ObservableValue<? extends Scene> observableValue, Scene oldScene, Scene newScene) {
                if (oldScene == null && newScene != null) {
                    newScene.getAccelerators().put(
                            new KeyCodeCombination(KeyCode.D, KeyCombination.CONTROL_DOWN),
                            new Runnable() {
                                @Override
                                public void run() {
                                    dark_mode(null); // Assurez-vous que dark_mode peut gérer un argument null
                                }
                            });
                }
            }
        });
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
