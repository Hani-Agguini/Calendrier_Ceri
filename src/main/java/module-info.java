module com.example.calendrier_ceri {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.calendrier_ceri to javafx.fxml;
    exports com.example.calendrier_ceri;
}