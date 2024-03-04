package com.example.calendrier_ceri;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.util.MapTimeZoneCache;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        // Pour éviter une recherche DNS pour les fuseaux horaires
        System.setProperty("net.fortuna.ical4j.timezone.cache.impl", MapTimeZoneCache.class.getName());

        lireFichierIcs("fichier_ics/s8.ics"); // Assurez-vous de mettre à jour le chemin du fichier
        launch(args);
    }

    private static void lireFichierIcs(String filePath) {
        try {
            FileInputStream fin = new FileInputStream(filePath);
            CalendarBuilder builder = new CalendarBuilder();
            Calendar calendar = builder.build(fin);

            for (Object o : calendar.getComponents(Component.VEVENT)) {
                VEvent event = (VEvent) o;
                System.out.println("Début : " + event.getStartDate().getValue());
                System.out.println("Fin : " + event.getEndDate().getValue());
                System.out.println("Description : " + (event.getDescription() != null ? event.getDescription().getValue() : "Pas de description"));
                System.out.println("-----------------------------------------------------");
            }
        } catch (FileNotFoundException e) {
            System.err.println("Fichier iCalendar non trouvé : " + e.getMessage());
        } catch (IOException | ParserException e) {
            System.err.println("Erreur lors de la lecture du fichier iCalendar : " + e.getMessage());
        }
    }
}
