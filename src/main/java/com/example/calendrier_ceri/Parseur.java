package com.example.calendrier_ceri;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parseur {
    void lireFichier(String nomFichier) {
        nomFichier = "fichier_ics/" + nomFichier;
        try {
            File fichier = new File(nomFichier);
            Scanner scanner = new Scanner(fichier);
            while (scanner.hasNextLine()) {
                String ligne = scanner.nextLine();
                System.out.println(ligne);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    public static List<CalendarActivity> parceFichier(String filename) {
        List<CalendarActivity> evenements = null;
        try {
            File file = new File("fichier_ics/"+filename);
            Scanner scanner = new Scanner(file);
            evenements = new ArrayList<>();

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if (line.startsWith("BEGIN:VEVENT")) {
                    StringBuilder description = new StringBuilder();
                    StringBuilder loc = new StringBuilder();
                    LocalDateTime dtStart = null;
                    LocalDateTime dtEnd = null;
                    StringBuilder summary = new StringBuilder();
                    StringBuilder enseignant = new StringBuilder();
                    StringBuilder matiere = new StringBuilder();
                    StringBuilder salle = new StringBuilder();

                    while (!line.startsWith("END:VEVENT")) {
                        if (line.startsWith("LOCATION")) {
                            loc.append(line.substring(line.indexOf(':') + 1));
                            line = readContinuedLines(scanner, loc);
                        }
                        if (line.startsWith("DESCRIPTION")) {
                            description.append(line.substring(line.indexOf(':') + 1));
                            line = readContinuedLines(scanner, description);
                            enseignant.append(trouverEnseignant(description.toString()));
                            matiere.append(trouverMatiere(description.toString()));
                            salle.append(trouverSalle((description.toString())));
                        }
                        if (line.startsWith("SUMMARY")) {
                            summary.append(line.substring(line.indexOf(':') + 1));
                            line = readContinuedLines(scanner, summary);
                        }
                        if (line.startsWith("DTSTART:")) {
                            dtStart = LocalDateTime.ofEpochSecond(parseDateTime(line.substring("DTSTART:".length())), 0, ZoneOffset.UTC);
                        }
                        if (line.startsWith("DTEND:")) {
                            dtEnd = LocalDateTime.ofEpochSecond(parseDateTime(line.substring("DTEND:".length())), 0, ZoneOffset.UTC);
                        }
                        if (!line.startsWith("END:VEVENT") && scanner.hasNextLine()) {
                            line = scanner.nextLine();
                        }
                    }
                    ZonedDateTime zonedStartDateTime = toZonedDateTime(dtStart);
                    ZonedDateTime zonedEndDateTime = toZonedDateTime(dtEnd);
                    // Affiche les informations de l'événement
                    evenements.add(new CalendarActivity(zonedStartDateTime,summary.toString(),enseignant.toString(),matiere.toString(), zonedEndDateTime,description.toString(),loc.toString(),salle.toString()));
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return evenements; // Déplacer le return ici pour retourner tous les événements
    }
    public static ZonedDateTime toZonedDateTime(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            // Gérer le cas où localDateTime est null. Retourner null ou une valeur par défaut.
            return null; // Ou utilisez une valeur par défaut si approprié
        }
        ZoneId zoneId = ZoneId.of("UTC"); // Ou un autre fuseau horaire approprié
        return ZonedDateTime.of(localDateTime, zoneId);
    }
    private static String readContinuedLines(Scanner scanner, StringBuilder output) {
        String line = "";
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            if (line.startsWith(" ")) { // Si la ligne est une continuation
                output.append(line.substring(1)); // Ajoutez le contenu sans l'espace initial
            } else {
                break; // Sortie de la boucle si ce n'est pas une continuation
            }
        }
        return line;
    }

    public static String trouverEnseignant(String texte) {
        // Utilisation d'une expression régulière pour trouver le nom de l'enseignant
        Pattern pattern = Pattern.compile("Enseignant\\s*:\\s*([A-Za-z\\s]+)\\s*(?:\\\\n|$)");
        Matcher matcher = pattern.matcher(texte);

        if (matcher.find()) {
            String enseignant = matcher.group(1).trim(); // Le premier groupe correspond au nom de l'enseignant
            return enseignant;
        } else {
            return null;
        }
    }
    public static String trouverMatiere(String texte) {
        // Utilisation d'une expression régulière pour trouver le nom de la matière
        Pattern pattern = Pattern.compile("Matière\\s*:\\s*([^\\\\]+)\\s*(?:\\\\n|$)");
        Matcher matcher = pattern.matcher(texte);

        if (matcher.find()) {
            String matiere = matcher.group(1).trim(); // Le premier groupe correspond au nom de la matière
            return matiere;
        } else {
            return null;
        }
    }
    public static String trouverSalle(String texte) {
        // Utilisation d'une expression régulière pour trouver le nom de la matière
        Pattern pattern = Pattern.compile("Salle\\s*:\\s*([^\\\\]+)\\s*(?:\\\\n|$)");
        Matcher matcher = pattern.matcher(texte);

        if (matcher.find()) {
            String matiere = matcher.group(1).trim(); // Le premier groupe correspond au nom de la matière
            return matiere;
        } else {
            return null;
        }
    }
    private static long parseDateTime(String dateTimeStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'");
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, formatter);
        return dateTime.toEpochSecond(ZoneOffset.UTC);
    }

    private static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime != null) {
            return dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        } else {
            return "Non spécifié";
        }
    }
}