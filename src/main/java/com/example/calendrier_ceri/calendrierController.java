package com.example.calendrier_ceri;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;


import static com.example.calendrier_ceri.Parseur.parceFichier;


public class calendrierController implements Initializable {

    ZonedDateTime dateFocus;
    ZonedDateTime today;

    static boolean prof =false;
    @FXML
    private ComboBox<String> comboMatiere;
    @FXML
    private ComboBox<String> comboGroupe;
    @FXML
    private ComboBox<String> comboSalle;
    @FXML
    private ComboBox<String> comboTypeCours;
    @FXML
    private Label year;

    @FXML
    private Label month;

    @FXML
    private FlowPane calendar;

    @FXML
    private AnchorPane weekView;

    @FXML
    private FlowPane monthView;
    @FXML
    private StackPane calendarContainer;
    @FXML
    private AnchorPane calendrierPane;
    @FXML
    private Button ajouterEvenementBtn; // Assurez-vous que c'est bien lié à votre FXML
    @FXML
    private Button reserversalle;
    public void setProfVisibility(boolean isVisible) {
        reserversalle.setVisible(isVisible);
    }



    private void applyDarkMode(boolean isDarkMode) {
        if (calendrierPane != null) {
            if (isDarkMode) {
                calendrierPane.getStyleClass().remove("style");
                calendrierPane.getStyleClass().add("dark-mode");
            } else {
                calendrierPane.getStyleClass().remove("dark-mode");
                calendrierPane.getStyleClass().add("style");
            }
        }
    }
    public void choix_formation(ActionEvent event) throws IOException {
        System.out.println("Choix de formation");
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        double width = stage.getWidth();
        double height = stage.getHeight();

        Parent connexionView = FXMLLoader.load(getClass().getResource("choix_Formations.fxml"));

        Scene choixView = new Scene(connexionView, width, height);

        // Appliquer la nouvelle scène au stage (fenêtre) existant sans changer la taille
        stage.setScene(choixView);


        stage.show();
    }
    private String formationFileName;
    private List<CalendarActivity> allActivities = new ArrayList<>();
    private List<CalendarActivity> currentFilteredActivities;

    public void setFormation(String formation) {
        this.formationFileName = formation+ ".ics";
        loadActivities();
    }
    private void loadActivities() {
        if (formationFileName != null && !formationFileName.isEmpty()) {
            allActivities = parceFichier(formationFileName);
            refreshCalendar();
        } else {
            System.out.println("Le nom du fichier de formation n'est pas défini.");
        }
    }

    @FXML
    private ToggleGroup toggleGroup;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dateFocus = ZonedDateTime.now();
        today = ZonedDateTime.now();
        List<String> matieres = Arrays.asList("","ANGLAIS", "ANAGEMENT PAR LES PROC", "GESTION DE PROJET","APPLICATION DE MSI");
        List<String> groupes = Arrays.asList("","ALT", "Cla");
        List<String> salles = Arrays.asList("","Amphi Ada", "Amphi Blaise");
        List<String> typesCours = Arrays.asList("","TP","TD","CM");

        comboMatiere.getItems().setAll(matieres);
        comboGroupe.getItems().setAll(groupes);
        comboSalle.getItems().setAll(salles);
        comboTypeCours.getItems().setAll(typesCours);
        comboMatiere.valueProperty().addListener((obs, oldVal, newVal) -> updateCalendarView());
        comboGroupe.valueProperty().addListener((obs, oldVal, newVal) -> updateCalendarView());
        comboSalle.valueProperty().addListener((obs, oldVal, newVal) -> updateCalendarView());
        comboTypeCours.valueProperty().addListener((obs, oldVal, newVal) -> updateCalendarView());
        refreshCalendar();
        applyDarkMode(ThemeManager.darkModeActiveProperty().get());
        ThemeManager.darkModeActiveProperty().addListener((obs, oldVal, isDarkMode) -> {
            applyDarkMode(isDarkMode);
        });
        calendrierPane.sceneProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Raccourci pour "back"
                newValue.getAccelerators().put(
                        new KeyCodeCombination(KeyCode.LEFT, KeyCombination.CONTROL_DOWN),
                        () -> back(null));

                // Raccourci pour "forward"
                newValue.getAccelerators().put(
                        new KeyCodeCombination(KeyCode.RIGHT, KeyCombination.CONTROL_DOWN),
                        () -> forward(null));
            }
        });
    }
    private void updateCalendarView() {
        String matiereSelected = comboMatiere.getValue();
        String groupeSelected = comboGroupe.getValue();
        String salleSelected = comboSalle.getValue();
        String typeCoursSelected = comboTypeCours.getValue();
        currentFilteredActivities = filterActivities(
                allActivities,
                matiereSelected,
                groupeSelected,
                salleSelected,
                typeCoursSelected
        );
        refreshCalendar();
    }
    public List<CalendarActivity> filterActivities(List<CalendarActivity> allActivities, String matiere, String groupe, String salle, String typeCours) {

        return allActivities.stream()
                .filter(activity -> matiere == null || matiere.isEmpty() || activity.getMatiere().contains(matiere))
                .filter(activity -> groupe == null || groupe.isEmpty() || activity.getDescription().contains(groupe))
                .filter(activity -> salle == null || salle.isEmpty() || activity.getDescription().contains(salle))
                .filter(activity -> typeCours == null || typeCours.isEmpty() || activity.getSummary().contains(typeCours))
                .collect(Collectors.toList());
    }

    private boolean isMonthView = true;
    @FXML
    void ajouterEvenementAction(ActionEvent event) throws IOException {
        showAddEventDialog1();
    }

    @FXML
    void reserversalle(ActionEvent event) throws IOException{
        showAddEventDialog();
    }
    @FXML
    void back(ActionEvent event) {
        if (isMonthView) {
            dateFocus = dateFocus.minusMonths(1);
            refreshCalendar();
        } else {
            dateFocus = dateFocus.minusWeeks(1);
            refreshCalendar();
        }

    }

    @FXML
    void forward(ActionEvent event) {
        if (isMonthView) {
            dateFocus = dateFocus.plusMonths(1);
            refreshCalendar();
        } else {
            dateFocus = dateFocus.plusWeeks(1);
            refreshCalendar();
        }

    }


    @FXML
    void showWeekView(ActionEvent event) {
        isMonthView = false;
        calendar.getChildren().clear();
        refreshCalendar();
    }

    @FXML
    void showMonthView(ActionEvent event) {
        isMonthView = true;
        calendar.getChildren().clear();
        refreshCalendar();
    }

    void refreshCalendar() {
        List<CalendarActivity> activitiesToDisplay = (currentFilteredActivities != null) ? currentFilteredActivities : allActivities;

        if (isMonthView) {
            Map<Integer, List<CalendarActivity>> activitiesForMonth = getCalendarActivitiesMonth(dateFocus, activitiesToDisplay);
            drawCalendar(dateFocus, activitiesForMonth);
        } else {
            ZonedDateTime weekStart = dateFocus.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            Map<Integer, List<CalendarActivity>> activitiesForWeek = getCalendarActivitiesWeek(weekStart, (currentFilteredActivities != null) ? currentFilteredActivities : allActivities);
            drawCalendarWeek(weekStart, activitiesForWeek);
        }
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
                if(activity.getMatiere()==null){
                    activity.setMatiere("salle reserver");
                }
                String fullText = "   "+activity.getMatiere() + " " + activity.getSalle() + " " + activity.getStartDateTime();
                Label activityLabel = new Label(fullText);
                if (activity.isExam()) {
                    activityLabel.getStyleClass().add("exam-label");
                } else {
                    activityLabel.getStyleClass().remove("exam-label");
                }
                activityLabel.getStyleClass().clear();
                if (activity.isExam()) {
                    activityLabel.setTextFill(Color.RED);
                }

                activityLabel.setWrapText(true);
                activityLabel.setMaxWidth(rectangleHeight - 5);
                activityLabel.setEllipsisString("...");
                activityBox.getChildren().add(activityLabel);

                activityLabel.setOnMouseClicked(mouseEvent -> {
                    openActivityDetailsWindow(date, activities);
                });

                VBox.setMargin(activityLabel, new Insets(0, 5, 0, 5));
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
        ZonedDateTime weekEnd = weekStart.plusDays(7);
        Map<Integer, List<CalendarActivity>> weekActivities = new HashMap<>();


        for (CalendarActivity event : allEvents) {
            ZonedDateTime eventStart = event.getStartDateTime();
            ZonedDateTime eventEnd = event.getEndDateTime();

            if (eventStart != null && !eventStart.isBefore(weekStart) && eventStart.isBefore(weekEnd)) {
                int dayOfWeek = eventStart.getDayOfWeek().getValue();
                System.out.println("heeeeeeeeeeee"+dayOfWeek);
                weekActivities.computeIfAbsent(dayOfWeek, k -> new ArrayList<>()).add(event);
            }
        }

        return weekActivities;
    }

    void drawCalendarWeek(ZonedDateTime weekStart, Map<Integer, List<CalendarActivity>> activitiesForWeek) {
        calendar.getChildren().clear();
        year.setText(String.valueOf(weekStart.getYear()));
        month.setText(weekStart.format(DateTimeFormatter.ofPattern("'Semaine' w")));

        double calendarWidth = calendar.getPrefWidth();
        double spacingH = calendar.getHgap(); // L'espacement horizontal entre les cellules

        double cellWidth = (calendarWidth / 7) - spacingH;

        for (int dayOfWeek = 1; dayOfWeek <= 7; dayOfWeek++) {
            ZonedDateTime currentDay = weekStart.plusDays(dayOfWeek - 1);
            List<CalendarActivity> dayActivities = activitiesForWeek.getOrDefault(dayOfWeek, new ArrayList<>());

            VBox dayColumn = new VBox(5); // Espace vertical entre les activités
            dayColumn.setPrefWidth(cellWidth);

            // Ajoutez le numéro du jour au début de la colonne du jour
            Label dayNumberLabel = new Label(String.valueOf(currentDay.getDayOfMonth()));
            dayNumberLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 12;");
            dayColumn.getChildren().add(dayNumberLabel);

            if (dayActivities.isEmpty()) {
                // Si aucune activité, ajoutez un StackPane bleu vide
                StackPane emptyCell = new StackPane();
                emptyCell.setStyle("-fx-background-color: lightblue;");
                emptyCell.setPrefSize(cellWidth, HEIGHT_PER_HOUR); // Définissez une hauteur par défaut pour le carré vide
                dayColumn.getChildren().add(emptyCell);
            } else {
                for (CalendarActivity activity : dayActivities) {
                    StackPane activityCell = createActivityCell(activity, cellWidth, currentDay);
                    dayColumn.getChildren().add(activityCell);
                }
            }

            calendar.getChildren().add(dayColumn);
        }
    }

    private static final int HEIGHT_PER_HOUR = 60;

    private StackPane createActivityCell(CalendarActivity activity, double cellWidth, ZonedDateTime currentDay) {
        Duration duration = Duration.between(activity.getStartDateTime(), activity.getEndDateTime());
        double hours = duration.toHours();
        double cellHeight = hours * HEIGHT_PER_HOUR;

        StackPane activityCell = new StackPane();
        activityCell.setPrefSize(cellWidth, cellHeight);
        if(currentDay.toLocalDate().isEqual(today.toLocalDate())){
            activityCell.setStyle("-fx-background-color: lightblue;-fx-font-size: 10;-fx-border-color: red;");
        }else{
            activityCell.setStyle("-fx-background-color: lightblue;-fx-font-size: 10;");}

        Label titleLabel = new Label(activity.getMatiere());
        titleLabel.setStyle(" -fx-font-size: 11;");

        titleLabel.setWrapText(true);
        activityCell.getChildren().add(titleLabel);

        activityCell.setOnMouseClicked(mouseEvent -> {
            openActivityDetailsWindow(currentDay, Collections.singletonList(activity));
        });

        return activityCell;
    }

    public String formatTeacherEmail(String fullName) {
        String emailName = fullName.toLowerCase();
        emailName = emailName.replace(" ", ".");
        return emailName;
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
                activityDetails.setStyle("-fx-border-color: lightblue; -fx-border-width: 1; -fx-background-color: lightblue;");
                if(activity.getMatiere().equals("")){
                    activity.setMatiere("Salle Reserver");
                }
                Label matiere = new Label("Matière: " + activity.getMatiere());
                Label enseignant = new Label("Enseignant: " + activity.getEnseignant());
                enseignant.setOnMouseClicked(e -> {
                    if (Desktop.isDesktopSupported()) {
                        try {
                            Desktop desktop = Desktop.getDesktop();
                            if (desktop.isSupported(Desktop.Action.MAIL)) {
                                // Assurez-vous que l'adresse e-mail et le sujet sont correctement encodés.
                                String nomens = formatTeacherEmail(activity.getEnseignant());
                                String mail =nomens+"@univ-avignon.fr";
                                String email = URLEncoder.encode(mail, StandardCharsets.UTF_8.toString());
                                String subject = URLEncoder.encode("Sujet du message", StandardCharsets.UTF_8.toString());
                                String body = URLEncoder.encode("Corps du message", StandardCharsets.UTF_8.toString());

                                URI mailto = new URI("mailto:" + email);
                                desktop.mail(mailto);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });

                Label location = new Label("Location: " + activity.getSalle());
                Label startDateTime = new Label("Début: " + activity.getStartDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                Label endDateTime = new Label("Fin: " + activity.getEndDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                if(activity.isExam() == true){
                    location.setTextFill(Color.RED);
                    startDateTime.setTextFill(Color.RED);
                    endDateTime.setTextFill(Color.RED);
                    matiere.setTextFill(Color.RED);
                    enseignant.setTextFill(Color.RED);
                }
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

    public void showAddEventDialog1() {
        // Création du Stage (fenêtre)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Ajouter un événement");
        dialogStage.initModality(Modality.WINDOW_MODAL);

        // Création du GridPane pour organiser les composants de la fenêtre
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Ajout des composants (labels et textfields)
        grid.add(new Label("Résumé:"), 0, 0);
        TextField summaryField = new TextField();
        grid.add(summaryField, 1, 0);

        grid.add(new Label("Enseignant:"), 0, 1);
        TextField teacherField = new TextField();
        grid.add(teacherField, 1, 1);

        grid.add(new Label("Matière:"), 0, 2);
        TextField subjectField = new TextField();
        grid.add(subjectField, 1, 2);

        /*grid.add(new Label("Salle:"), 0, 3);
        TextField roomField = new TextField();
        grid.add(roomField, 1, 3);*/

        grid.add(new Label("Description:"), 0, 4);
        TextField descriptionField = new TextField();
        grid.add(descriptionField, 1, 4);

        grid.add(new Label("Lieu:"), 0, 5);
        TextField locationField = new TextField();
        grid.add(locationField, 1, 5);

        grid.add(new Label("Date Debut:"), 0, 6);
        TextField dateStart = new TextField();
        grid.add(dateStart, 1, 6);

        grid.add(new Label("Date Fin:"), 0, 7);
        TextField fin = new TextField();
        grid.add(fin, 1, 7);

        grid.add(new Label("Examen:"), 0, 8);
        CheckBox examCheckBox = new CheckBox();
        grid.add(examCheckBox, 1, 8);

        // Boutons Ajouter et Annuler
        Button addButton = new Button("Ajouter");
        Button cancelButton = new Button("Annuler");

        HBox buttonsHBox = new HBox(10);
        buttonsHBox.getChildren().addAll(addButton, cancelButton);
        grid.add(buttonsHBox, 1, 9);

        // Action pour le bouton Annuler
        cancelButton.setOnAction(e -> dialogStage.close());

        // Action pour le bouton Ajouter
        addButton.setOnAction(e -> {
            // Récupération des données saisies
            String summary = summaryField.getText();
            String teacher = teacherField.getText();
            String subject = subjectField.getText();
            //String room = roomField.getText();
            String description = descriptionField.getText();
            String location = locationField.getText();
            String dateDebut =dateStart.getText();
            String dateFin= fin.getText();
            boolean isExam = examCheckBox.isSelected();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").withZone(ZoneId.systemDefault());


            try {
                ZonedDateTime startDateTime = ZonedDateTime.parse(dateDebut, formatter);
                ZonedDateTime endDateTime = ZonedDateTime.parse(dateFin, formatter);


                addEventToIcsFile("fichier_ics\\"+Connexion.pseudo+".ics", startDateTime, endDateTime, summary, location, description, teacher, location,isExam);
                dialogStage.close();
            } catch (DateTimeParseException ex) {
                System.err.println("Format de date ou d'heure incorrect: " + ex.getMessage());
            }
        });

        Scene scene = new Scene(grid);
        dialogStage.setScene(scene);
        dialogStage.showAndWait();
    }
    public void showAddEventDialog() {
        // Création du Stage (fenêtre)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Ajouter un événement");
        dialogStage.initModality(Modality.WINDOW_MODAL);

        // Création du GridPane pour organiser les composants de la fenêtre
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Ajout des composants (labels et textfields)
        grid.add(new Label("Résumé:"), 0, 0);
        TextField summaryField = new TextField();
        grid.add(summaryField, 1, 0);

        grid.add(new Label("Enseignant:"), 0, 1);
        TextField teacherField = new TextField();
        grid.add(teacherField, 1, 1);

        grid.add(new Label("Matière:"), 0, 2);
        TextField subjectField = new TextField();
        grid.add(subjectField, 1, 2);

        grid.add(new Label("Salle:"), 0, 3);
        TextField roomField = new TextField();
        grid.add(roomField, 1, 3);

        grid.add(new Label("Description:"), 0, 4);
        TextField descriptionField = new TextField();
        grid.add(descriptionField, 1, 4);

        grid.add(new Label("Lieu:"), 0, 5);
        TextField locationField = new TextField();
        grid.add(locationField, 1, 5);

        grid.add(new Label("Date Debut:"), 0, 6);
        TextField dateStart = new TextField();
        grid.add(dateStart, 1, 6);

        grid.add(new Label("Date Fin:"), 0, 7);
        TextField fin = new TextField();
        grid.add(fin, 1, 7);

        grid.add(new Label("Examen:"), 0, 8);
        CheckBox examCheckBox = new CheckBox();
        grid.add(examCheckBox, 1, 8);

        // Boutons Ajouter et Annuler
        Button addButton = new Button("Ajouter");
        Button cancelButton = new Button("Annuler");

        HBox buttonsHBox = new HBox(10);
        buttonsHBox.getChildren().addAll(addButton, cancelButton);
        grid.add(buttonsHBox, 1, 9);

        // Action pour le bouton Annuler
        cancelButton.setOnAction(e -> dialogStage.close());

        // Action pour le bouton Ajouter
        addButton.setOnAction(e -> {
            // Récupération des données saisies
            String summary = summaryField.getText();
            String teacher = teacherField.getText();
            String subject = subjectField.getText();
            String room = roomField.getText();
            String description = descriptionField.getText();
            String location = locationField.getText();
            String dateDebut =dateStart.getText();
            String dateFin= fin.getText();
            boolean isExam = examCheckBox.isSelected();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").withZone(ZoneId.systemDefault());


            try {
                ZonedDateTime startDateTime = ZonedDateTime.parse(dateDebut, formatter);
                ZonedDateTime endDateTime = ZonedDateTime.parse(dateFin, formatter);

                // Vérifiez si la salle est libre
                if (isSalleEtEnseignantLibres(room,teacher, startDateTime, endDateTime)) {
                    addEventToIcsFile("fichier_ics\\"+formationFileName, startDateTime, endDateTime, summary, location, description, teacher, room, isExam);
                    dialogStage.close();
                } else {
                    // Afficher une notification indiquant que la salle est déjà réservée
                    showAlertSalleReservee();
                }
            } catch (DateTimeParseException ex) {
                System.err.println("Format de date ou d'heure incorrect: " + ex.getMessage());
            }
        });

        Scene scene = new Scene(grid);
        dialogStage.setScene(scene);
        dialogStage.showAndWait();
    }


    public void addEventToIcsFile(String filePath, ZonedDateTime startDateTime, ZonedDateTime endDateTime,
                                  String summary, String location, String description, String enseignant, String salle,boolean isExam) {
        try (FileWriter fw = new FileWriter(filePath, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {

            String dtStamp = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'"));
            String dtStart = startDateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'"));
            String dtEnd = endDateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'"));
            String uid = "Cours-" + startDateTime.toEpochSecond() + "-Index-Education"; // UID exemple, peut être généré différemment

            // Création de la représentation de l'événement
            String event =
                    "BEGIN:VEVENT\r\n" +
                            "CATEGORIES:HYPERPLANNING\r\n" +
                            "DTSTAMP:" + dtStamp + "\r\n" +
                            "LAST-MODIFIED:" + dtStamp + "\r\n" +
                            "UID:" + uid + "\r\n" +
                            "DTSTART:" + dtStart + "\r\n" +
                            "DTEND:" + dtEnd + "\r\n" +
                            "SUMMARY;LANGUAGE=fr:" + summary + " - " + enseignant + " - " + salle + " - TP\r\n" +
                            "LOCATION;LANGUAGE=fr:" + location + "\r\n" +
                            "DESCRIPTION;LANGUAGE=fr:Matière : " + description + "\\nEnseignant : " + enseignant + "\\nSalle : " + location + "\\nType : TP\\n\r\n" +
                            "X-ALT-DESC;FMTTYPE=text/html:Matière : " + description + "<br/>Enseignant : " + enseignant + "<br/>Salle : " + location + "<br/>Type : TP<br/>\r\n" +
                            "ISEXAM:"+isExam+ "\r\n" +
                            "END:VEVENT\r\n";

            out.println(event);
        } catch (IOException e) {
            System.err.println("An error occurred while writing to the .ics file: " + e.getMessage());
        }
    }
    private boolean isSalleLibre(String salle, ZonedDateTime debut, ZonedDateTime fin) {
        for (CalendarActivity activite : allActivities) {
            if (activite.getSalle().equals(salle) &&
                    activite.getStartDateTime().isBefore(fin) && activite.getEndDateTime().isAfter(debut)) {
                return false;
            }
        }
        return true;
    }
    private boolean isSalleEtEnseignantLibres(String salle, String enseignant, ZonedDateTime debut, ZonedDateTime fin) {
        for (CalendarActivity activite : allActivities) {
            ZonedDateTime eventStart = activite.getStartDateTime();
            ZonedDateTime eventEnd = activite.getEndDateTime();

            // Vérifiez que les dates de début et de fin ne sont pas nulles
            if (eventStart != null && eventEnd != null) {
                boolean seChevauche = eventStart.isBefore(fin) && eventEnd.isAfter(debut);
                boolean salleOccupee = activite.getSalle().equals(salle) && seChevauche;
                boolean enseignantOccupe = activite.getEnseignant().equals(enseignant) && seChevauche;

                if (salleOccupee || enseignantOccupe) {
                    return false;
                }
            } else {

            }
        }
        return true; // La salle et l'enseignant sont libres
    }
    private void showAlertSalleReservee() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Salle Réservée");
        alert.setHeaderText(null);
        alert.setContentText("La salle est déjà réservée à l'horaire sélectionné.");
        alert.showAndWait();
    }



}