package com.example.calendrier_ceri;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Connexion {
    private static class Utilisateur {
        String type;
        String pseudo;
        String motDePasse;

        Utilisateur(String type, String pseudo, String motDePasse) {
            this.type = type;
            this.pseudo = pseudo;
            this.motDePasse = motDePasse;
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
                    utilisateurs.add(new Utilisateur(type, pseudo, motDePasse));
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

    public static void main(String[] args) {
        Connexion connexion = new Connexion();
        System.out.println(connexion.verifier_utilisateur("hani", "2000"));
    }
}
