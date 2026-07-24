package fr.pathfinder.application;

import fr.pathfinder.affichage.Menu;
import fr.pathfinder.carte.Deck;
import fr.pathfinder.service.CardService;

/**
 * Coordonne le démarrage de l'ancienne interface console.
 */
public class Application {

    /**
     * Charge le catalogue puis démarre la boucle du menu console.
     */
    public void demarrer() {

        afficherTitre();

        CardService cardService = new CardService();

        Deck deck = cardService.chargerDeck();

        Menu menu = new Menu();
        menu.lancerMenu(deck, cardService);
    }

    private void afficherTitre() {

        System.out.println("=================================");
        System.out.println(" Pathfinder Card Studio");
        System.out.println(" Version 0.1.0");
        System.out.println(" Forge initialisee");
        System.out.println("=================================");
    }
}
