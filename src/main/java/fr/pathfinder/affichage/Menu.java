package fr.pathfinder.affichage;

import java.util.Scanner;

import fr.pathfinder.carte.AbilityCard;
import fr.pathfinder.carte.Card;
import fr.pathfinder.carte.Deck;
import fr.pathfinder.carte.ItemCard;
import fr.pathfinder.carte.SpellCard;
import fr.pathfinder.service.CardService;

public class Menu {

    private final Scanner scanner;

    public Menu() {

        scanner = new Scanner(
                System.in);
    }

    public void afficherBienvenue() {

        System.out.println(
                "Bienvenue dans votre atelier de création de cartes Pathfinder !");

    }

    private void rechercherUneCarte(Deck deck) {

        System.out.print("Recherche : ");

        String recherche = scanner.nextLine();

        deck.getCartes().forEach(carte -> {

            if (carte.getNom()
                    .toLowerCase()
                    .contains(recherche.toLowerCase())) {

                System.out.println("----------------");
                System.out.println(
                        carte.afficherDetails());

            }

        });

    }

    public void lancerMenu(Deck deck, CardService cardService) {

        boolean quitter = false;

        while (!quitter) {

            afficherOptions();

            String choix = scanner.nextLine();

            switch (choix) {

                case "1":

                    afficherCartes(deck);

                    break;

                case "2":

                    rechercherUneCarte(deck);

                    break;

                case "3":

                    ajouterUneCarte(deck, cardService);

                    break;

                case "4":

                    supprimerUneCarte(deck, cardService);

                    break;

                case "5":

                    quitter = true;

                    System.out.println(
                            "Fermeture de Pathfinder Card Studio...");

                    break;

                default:

                    System.out.println(
                            "Choix invalide.");

            }

        }

    }

    private int demanderUnNombre(String message) {

        while (true) {

            System.out.print(message);

            String valeur = scanner.nextLine();

            try {

                return Integer.parseInt(valeur);

            } catch (NumberFormatException e) {

                System.out.println(
                        "Veuillez entrer un nombre valide.");

            }
        }
    }

    private void afficherOptions() {

        System.out.println();
        System.out.println("=================================");
        System.out.println("Menu principal");
        System.out.println("=================================");

        System.out.println("1 - Afficher les cartes");
        System.out.println("2 - Rechercher une carte");
        System.out.println("3 - Ajouter une carte");
        System.out.println("4 - Supprimer une carte");
        System.out.println("5 - Quitter");

        System.out.print("> ");

    }

    public void afficherCartes(Deck deck) {

        for (Card carte : deck.getCartes()) {

            System.out.println("----------------");
            System.out.println(carte.afficherDetails());

        }

        System.out.println("----------------");
        System.out.println(
                "Nombre de cartes chargées : " + deck.taille());
    }

    private void ajouterUneCarte(
            Deck deck,
            CardService cardService) {

        System.out.println();
        System.out.println("=================================");
        System.out.println("Création d'une carte");
        System.out.println("=================================");
        System.out.println("1 - Sort");
        System.out.println("2 - Objet");
        System.out.println("3 - Capacité");
        System.out.println("0 - Retour");
        System.out.print("> ");

        String choix = scanner.nextLine();

        switch (choix) {

            case "1":
                ajouterUnSort(deck);
                break;

            case "2":
                ajouterUnObjet(deck);
                break;

            case "3":
                ajouterUneCapacite(deck);
                break;

            case "0":
                System.out.println("Retour au menu principal.");
                return;

            default:
                System.out.println("Type de carte invalide.");
                return;
        }

        cardService.sauvegarderDeck(deck);
        System.out.println("Carte ajoutée avec succès !");
    }

    private void ajouterUnSort(Deck deck) {

        System.out.println("=== Création d'un nouveau sort ===");

        System.out.print("Nom du sort : ");
        String nom = scanner.nextLine();

        int niveau = demanderUnNombre("Niveau du sort : ");

        System.out.print("Description : ");
        String description = scanner.nextLine();

        System.out.print("École de magie : ");
        String ecole = scanner.nextLine();

        SpellCard sort = new SpellCard(
                nom,
                niveau,
                description,
                ecole);

        deck.ajouterCarte(sort);
    }

    private void ajouterUnObjet(Deck deck) {

        System.out.println("=== Création d'un nouvel objet ===");

        System.out.print("Nom de l'objet : ");
        String nom = scanner.nextLine();

        int niveau = demanderUnNombre("Niveau de l'objet : ");

        System.out.print("Description : ");
        String description = scanner.nextLine();

        System.out.print("Emplacement : ");
        String emplacement = scanner.nextLine();

        ItemCard objet = new ItemCard(
                nom,
                niveau,
                description,
                emplacement);

        deck.ajouterCarte(objet);
    }

    private void ajouterUneCapacite(Deck deck) {

        System.out.println("=== Création d'une nouvelle capacité ===");

        System.out.print("Nom de la capacité : ");
        String nom = scanner.nextLine();

        int niveau = demanderUnNombre("Niveau de la capacité : ");

        System.out.print("Description : ");
        String description = scanner.nextLine();

        System.out.print("Nombre d'actions : ");
        String nombreActions = scanner.nextLine();

        AbilityCard capacite = new AbilityCard(
                nom,
                niveau,
                description,
                nombreActions);

        deck.ajouterCarte(capacite);
    }
    private void supprimerUneCarte(
        Deck deck,
        CardService cardService) {

    if (deck.taille() == 0) {

        System.out.println(
                "Aucune carte à supprimer.");

        return;
    }

    System.out.println();
    System.out.println("=== Suppression d'une carte ===");

    for (int i = 0; i < deck.taille(); i++) {

        Card carte = deck.getCartes().get(i);

        System.out.println(
                (i + 1) + " - " + carte.getNom());
    }

    int numero = demanderUnNombre(
            "Numéro de la carte à supprimer : ");

    if (numero < 1 || numero > deck.taille()) {

        System.out.println(
                "Numéro invalide.");

        return;
    }

    Card carteSupprimee =
            deck.supprimerCarte(numero - 1);

    cardService.sauvegarderDeck(deck);

    System.out.println(
            "Carte supprimée : "
                    + carteSupprimee.getNom());
}
}