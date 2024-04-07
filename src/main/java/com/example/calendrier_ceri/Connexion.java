package com.example.calendrier_ceri;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Connexion {
    static String pseudo="";
    private static class Utilisateur {
        String type;
        String pseudo;
        String motDePasse;

        String mode_pref="light";

        Utilisateur(String type, String pseudo, String motDePasse, String mode_pref) {
            this.type = type;
            this.pseudo = pseudo;
            this.motDePasse = motDePasse;
            this.mode_pref = mode_pref;
        }
    }

    List<Utilisateur> lire_fichier(String nomFichier) {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        nomFichier = "fichier_connexion/" + nomFichier;
        try {
            File fichier = new File(nomFichier);
            Scanner scanner = new Scanner(fichier);
            while (scanner.hasNextLine()) {
                String ligne = scanner.nextLine();
                String[] elements = ligne.split("\\s+");
                if (elements.length >= 3) {
                    String type = elements[0];
                    String pseudo = elements[1];
                    String motDePasse = elements[2];
                    String mode_pref = elements[3];
                    utilisateurs.add(new Utilisateur(type, pseudo, motDePasse, mode_pref));
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("fichier introuvable.");
            e.printStackTrace();
        }
        return utilisateurs;
    }
    boolean verifier_utilisateur(String pseudo, String motDePasse) {
        List<Utilisateur> utilisateurs = lire_fichier("logging.txt");
        for (Utilisateur utilisateur : utilisateurs) {
            if (utilisateur.pseudo.equals(pseudo) && utilisateur.motDePasse.equals(motDePasse)) {
                System.out.println("Connexion réussie.");
                return true ;
            }
        }
        System.out.println("Connexion échouée.");
        return false;
    }

    boolean verifier_prof(String pseudo, String motDePasse) {
        List<Utilisateur> utilisateurs = lire_fichier("logging.txt");
        for (Utilisateur utilisateur : utilisateurs) {
            if (utilisateur.pseudo.equals(pseudo) && utilisateur.motDePasse.equals(motDePasse) && utilisateur.type.equals("ENS")) {
                System.out.println("Connexion réussie pour un prof.");
                return true;
            }
        }
        System.out.println("Connexion échouée.");
        return false;
    }

    void modifier_mode(String pseudo, String mode) {
        List<Utilisateur> utilisateurs = lire_fichier("logging.txt");
        for (Utilisateur utilisateur : utilisateurs) {
            if (utilisateur.pseudo.equals(pseudo)) {
                utilisateur.mode_pref = mode;
            }
            ecrire_fichier("logging.txt", utilisateurs);
        }
    }
    void ecrire_fichier(String nomFichier, List<Utilisateur> utilisateurs) {
        nomFichier = "fichier_connexion/" + nomFichier;
        try {
            java.io.FileWriter fichier = new java.io.FileWriter(nomFichier);
            for (Utilisateur utilisateur : utilisateurs) {
                fichier.write(utilisateur.type + " " + utilisateur.pseudo + " " + utilisateur.motDePasse + " " + utilisateur.mode_pref + "\n");
            }
            fichier.close();
        } catch (java.io.IOException e) {
            System.out.println("Erreur lors de l'écriture du fichier.");
            e.printStackTrace();
        }
    }

    String get_mode(String pseudo) {
        List<Utilisateur> utilisateurs = lire_fichier("logging.txt");
        for (Utilisateur utilisateur : utilisateurs) {
            if (utilisateur.pseudo.equals(pseudo)) {
                return(utilisateur.mode_pref);
            }
        }return "light";
    }

    public static void main(String[] args) {
        Connexion connexion = new Connexion();
        System.out.println(connexion.verifier_utilisateur("hani", "2000"));
        connexion.modifier_mode("hani", "dark");
        System.out.println(connexion.get_mode("hani"));

    }
}
