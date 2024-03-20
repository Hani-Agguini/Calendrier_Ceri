package com.example.calendrier_ceri;

import java.io.File;
import java.io.FileNotFoundException;
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
    public static List<Evenement> parceFichier() {
        List<Evenement> evenements = null;
        try {
            File file = new File("fichier_ics/m1_general.ics");
            Scanner scanner = new Scanner(file);
            evenements = new ArrayList<>();

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if (line.startsWith("BEGIN:VEVENT")) {
                    StringBuilder description = new StringBuilder();
                    StringBuilder summary = new StringBuilder();
                    StringBuilder enseignant = new StringBuilder();
                    StringBuilder matiere = new StringBuilder();
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
                            enseignant.append(trouverEnseignant(description.toString()));
                            matiere.append(trouverMatiere(description.toString()));

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

                    // Affiche les informations de l'événement
                    evenements.add(new Evenement(description.toString(),summary.toString(),enseignant.toString(),matiere.toString(), loc.toString(), dtStart, dtEnd));
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return evenements; // Déplacer le return ici pour retourner tous les événements
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

    public static void main(String[] args){
        List<Evenement> evenements = parceFichier();
        for (Evenement evenement : evenements) {
            System.out.println("Summary: " + evenement.getSummary());
            System.out.println("Description: " + evenement.getDescription());
            System.out.println("Enseignant: " + evenement.getEnseignant());
            System.out.println("Matière: " + evenement.getMatiere());
            System.out.println("Location: " + evenement.getLocation());
            System.out.println("Start Time: " + formatDateTime(evenement.getDebut()));
            System.out.println("End Time: " + formatDateTime(evenement.getFin()));
            System.out.println("-------------------------------------------------");
        }
        // Exemples d'utilisation
        String description = " Matière : UCE 2 MANAGEMENT PAR LES PROC\\nEnseignant : MARTIN Yannis\\nTD : M1-IA-IL-ALT\\, M1-IA-IL-CLA\\, M1-ILSEN-alt-GR2\\, M1-ILSEN-cla-Gr1\\, M1-SICOM-Alt\\, M1-SICOM-Cla\\nSalle : Amphi Ada\\nType : CM\\n";

        String enseignant = trouverEnseignant(description);
        if (enseignant != null) {
            System.out.println("Enseignant trouvé dans la description : " + enseignant);
        } else {
            System.out.println("Aucun enseignant trouvé dans la description.");
        }
        String matiere = trouverMatiere(description);
        if (matiere != null) {
            System.out.println("Matière trouvée dans la description : " + matiere);
        } else {
            System.out.println("Aucune matière trouvée dans la description.");
        }

    }
}

