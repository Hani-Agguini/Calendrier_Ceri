module com.example.calendrier_ceri {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.mnode.ical4j.core;
    requires java.desktop;


    opens com.example.calendrier_ceri to javafx.fxml;
    exports com.example.calendrier_ceri;
}