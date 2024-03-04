package com.example.calendrier_ceri;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

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
    public static void main(String[] args) {
        try {
            File file = new File("fichier_ics/s8.ics");
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if (line.startsWith("BEGIN:VEVENT")) {
                    StringBuilder description = new StringBuilder();
                    StringBuilder loc = new StringBuilder();
                    LocalDateTime dtStart = null;
                    LocalDateTime dtEnd = null;

                    while (!line.startsWith("END:VEVENT")) {
                        if (line.startsWith("LOCATION")) {
                            loc.append(line.substring(line.indexOf(':') + 1));
                            line = readContinuedLines(scanner, loc);
                        }
                       if (line.startsWith("DESCRIPTION")) {
                            description.append(line.substring(line.indexOf(':') + 1));
                            line = readContinuedLines(scanner, description);
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

                    // Affiche les informations de l'événement
                    System.out.println("Description: " + description.toString());
                    System.out.println("loc: " + loc.toString());
                    System.out.println("Date de début: " + formatDateTime(dtStart));
                    System.out.println("Date de fin: " + formatDateTime(dtEnd));
                    System.out.println("-------------------------------");
                }
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
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

