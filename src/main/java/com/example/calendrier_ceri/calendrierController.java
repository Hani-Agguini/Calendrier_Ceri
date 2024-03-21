package com.example.calendrier_ceri;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

import static com.example.calendrier_ceri.Parseur.parceFichier;


public class calendrierController implements Initializable {

    ZonedDateTime dateFocus;
    ZonedDateTime today;

    @FXML
    private Text year;

    @FXML
    private Text month;

    @FXML
    private FlowPane calendar;

    List<CalendarActivity> allActivities = parceFichier("m1_alt.ics");
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dateFocus = ZonedDateTime.now();
        today = ZonedDateTime.now();
    }
    @FXML
    void backOneMonth(ActionEvent event) {
        dateFocus = dateFocus.minusMonths(1);
        refreshCalendar();
    }

    @FXML
    void forwardOneMonth(ActionEvent event) {
        dateFocus = dateFocus.plusMonths(1);
        refreshCalendar();
    }

    @FXML
    void backOneWeek(ActionEvent event) {
        dateFocus = dateFocus.minusWeeks(1);
        ZonedDateTime weekStart = dateFocus.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        Map<Integer, List<CalendarActivity>> activitiesForWeek = getCalendarActivitiesWeek(weekStart, allActivities);
        drawCalendarWeek(weekStart, activitiesForWeek);
    }
    @FXML
    void forwardOneWeek(ActionEvent event) {
        dateFocus = dateFocus.plusWeeks(1);
        ZonedDateTime weekStart = dateFocus.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        Map<Integer, List<CalendarActivity>> activitiesForWeek = getCalendarActivitiesWeek(weekStart, allActivities);
        drawCalendarWeek(weekStart, activitiesForWeek);
    }
    @FXML
    void showWeekView(ActionEvent event) {
        ZonedDateTime weekStart = dateFocus.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        Map<Integer, List<CalendarActivity>> activitiesForWeek = getCalendarActivitiesWeek(weekStart, allActivities);
        drawCalendarWeek(weekStart, activitiesForWeek);
    }

    @FXML
    void showMonthView(ActionEvent event) {
        refreshCalendar();
    }

    void refreshCalendar() {
        Map<Integer, List<CalendarActivity>> activitiesForMonth = getCalendarActivitiesMonth(dateFocus, allActivities);
        drawCalendar(dateFocus, activitiesForMonth);
    }

    private void drawCalendar(ZonedDateTime dateFocus, Map<Integer, List<CalendarActivity>> activitiesForMonth) {
        calendar.getChildren().clear();
        year.setText(String.valueOf(dateFocus.getYear()));
        month.setText(String.valueOf(dateFocus.getMonth()));
        ZonedDateTime firstDayOfMonth = dateFocus.withDayOfMonth(1);
        int firstDayOfWeek = firstDayOfMonth.getDayOfWeek().getValue();
        int daysInMonth = firstDayOfMonth.getMonth().length(firstDayOfMonth.toLocalDate().isLeapYear());
        double calendarWidth = calendar.getPrefWidth();
        double calendarHeight = calendar.getPrefHeight();
        double strokeWidth = 1;
        double spacingH = calendar.getHgap();
        double spacingV = calendar.getVgap();
        double cellWidth = (calendarWidth / 7) - strokeWidth - spacingH;
        double cellHeight = (calendarHeight / 6) - strokeWidth - spacingV;
        for (int i = 1; i < firstDayOfWeek; i++) {
            calendar.getChildren().add(createEmptyDateCell(cellWidth, cellHeight));
        }
        for (int day = 1; day <= daysInMonth; day++) {
            ZonedDateTime currentDay = ZonedDateTime.of(dateFocus.getYear(), dateFocus.getMonthValue(), day, 0, 0, 0, 0, dateFocus.getZone());
            List<CalendarActivity> dayActivities = activitiesForMonth.getOrDefault(day, new ArrayList<>());
            StackPane dateCell = createDateCell(currentDay, dayActivities, cellWidth, cellHeight);
            calendar.getChildren().add(dateCell);

        }
    }
    private StackPane createEmptyDateCell(double width, double height) {
        StackPane emptyCell = new StackPane();
        emptyCell.setPrefSize(width, height);
        emptyCell.getStyleClass().add("calendar-empty-cell");

        Rectangle rect = new Rectangle(width, height);
        rect.setFill(Color.TRANSPARENT);
        emptyCell.getChildren().add(rect);
        return emptyCell;
    }

    private StackPane createDateCell(ZonedDateTime date, List<CalendarActivity> activities, double rectangleHeight, double rectangleWidth) {
        StackPane cell = new StackPane();
        cell.setPrefSize(rectangleHeight, rectangleWidth);
        Rectangle rect = new Rectangle(rectangleHeight, rectangleWidth);
        rect.setFill(Color.LIGHTBLUE);
        if (date.toLocalDate().isEqual(today.toLocalDate())) {
            rect.setStroke(Color.RED);
        } else {
            rect.setStroke(Color.LIGHTGRAY);
        }
        Text dayText = new Text(String.valueOf(date.getDayOfMonth()));
        StackPane.setAlignment(dayText, Pos.TOP_LEFT);
        VBox activityBox = new VBox(5);
        activityBox.setAlignment(Pos.TOP_CENTER);

        int activityCount = 0;
        for (CalendarActivity activity : activities) {
            if (activityCount < 2) {
                String fullText = "   "+activity.getMatiere() + "@" + activity.getSalle() + "@ " + activity.getStartDateTime();
                Label activityLabel = new Label(fullText);
                activityLabel.setWrapText(true);
                activityLabel.setMaxWidth(rectangleHeight - 5);
                activityLabel.setEllipsisString("...");
                activityLabel.setOnMouseClicked(mouseEvent -> {
                    openActivityDetailsWindow(date, activities);
                });

                VBox.setMargin(activityLabel, new Insets(0, 5, 0, 5));
                activityBox.getChildren().add(activityLabel);
                activityCount++;
            } else {
                if (activityCount == 2) {
                    Label moreActivities = new Label("...");
                    activityBox.getChildren().add(moreActivities);
                }
                break;
            }
        }

        cell.getChildren().addAll(rect, dayText, activityBox);
        return cell;
    }

    public Map<Integer, List<CalendarActivity>> getCalendarActivitiesMonth(ZonedDateTime dateFocus, List<CalendarActivity> allEvents) {
        Map<Integer, List<CalendarActivity>> calendarActivities = new HashMap<>();

        for (CalendarActivity event : allEvents) {
            ZonedDateTime eventStart = event.getStartDateTime();
            if (eventStart != null) {
                if (eventStart.getYear() == dateFocus.getYear() && eventStart.getMonth() == dateFocus.getMonth()) {
                    int dayOfMonth = eventStart.getDayOfMonth();
                    calendarActivities.computeIfAbsent(dayOfMonth, k -> new ArrayList<>()).add(event);
                }}}

        return calendarActivities;
    }

    public Map<Integer, List<CalendarActivity>> getCalendarActivitiesWeek(ZonedDateTime weekStart, List<CalendarActivity> allEvents) {
        Map<Integer, List<CalendarActivity>> weekActivities = new HashMap<>();
        ZonedDateTime weekEnd = weekStart.plusDays(6);
        for (CalendarActivity event : allEvents) {
            ZonedDateTime eventStart = event.getStartDateTime();
            if (eventStart != null && !eventStart.isBefore(weekStart) && eventStart.isBefore(weekEnd)) {
                int dayOfWeek = eventStart.getDayOfWeek().getValue();
                weekActivities.computeIfAbsent(dayOfWeek, k -> new ArrayList<>()).add(event);
            }
        }
        return weekActivities;
    }
    void drawCalendarWeek(ZonedDateTime weekStart, Map<Integer, List<CalendarActivity>> activitiesForWeek) {
        calendar.getChildren().clear();
        double calendarWidth = calendar.getPrefWidth();
        double calendarHeight = calendar.getPrefHeight();
        double strokeWidth = 1;
        double spacingH = calendar.getHgap(); // L'espacement horizontal entre les cellules
        double spacingV = calendar.getVgap(); // L'espacement vertical entre les rangées de cellules, si applicable

// Calculer la largeur et la hauteur des cellules pour l'affichage hebdomadaire
        double cellWidth = (calendarWidth / 7) - strokeWidth - spacingH; // Diviser par 7 jours de la semaine
        double cellHeight = calendarHeight - strokeWidth - spacingV;
        for (int dayOfWeek = 1; dayOfWeek <= 7; dayOfWeek++) {
            ZonedDateTime currentDay = weekStart.with(DayOfWeek.of(dayOfWeek));
            List<CalendarActivity> dayActivities = activitiesForWeek.getOrDefault(dayOfWeek, new ArrayList<>());
            StackPane dateCell = createDateCell(currentDay, dayActivities, cellWidth,cellHeight);
            calendar.getChildren().add(dateCell);
        }
    }

    private void openActivityDetailsWindow(ZonedDateTime date, List<CalendarActivity> activities) {
        try {
            Stage stage = new Stage();
            stage.setTitle("Activités du " + date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            ScrollPane scrollPane = new ScrollPane();
            VBox layout = new VBox(10);
            scrollPane.setContent(layout);
            layout.setPadding(new Insets(10));
            for (CalendarActivity activity : activities) {
                VBox activityDetails = new VBox(5);
                activityDetails.setPadding(new Insets(10));
                activityDetails.setStyle("-fx-border-color: lightgray; -fx-border-width: 1; -fx-background-color: #f9f9f9;");
                Label matiere = new Label("Matière: " + activity.getMatiere());
                Label enseignant = new Label("Enseignant: " + activity.getEnseignant());
                Label location = new Label("Location: " + activity.getSalle());
                Label startDateTime = new Label("Début: " + activity.getStartDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                Label endDateTime = new Label("Fin: " + activity.getEndDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                activityDetails.getChildren().addAll(matiere,enseignant, location, startDateTime, endDateTime);
                layout.getChildren().add(activityDetails);
            }
            Scene scene = new Scene(scrollPane, 400, 300);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}