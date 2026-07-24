package fr.pathfinder.service;

import fr.pathfinder.carte.Deck;
import fr.pathfinder.chargement.CardLoader;
import fr.pathfinder.chargement.CardSaver;

/**
 * Façade applicative pour charger et sauvegarder le catalogue de cartes.
 */
public class CardService {

    private final CardLoader loader;
    private final CardSaver saver;

    public CardService() {
        loader = new CardLoader();
        saver = new CardSaver();
    }

    /**
     * Charge le catalogue persistant.
     *
     * @return deck reconstruit depuis le fichier JSON
     */
    public Deck chargerDeck() {
        return loader.chargerCartes();
    }

    /**
     * Sauvegarde l'état courant du catalogue.
     *
     * @param deck deck à sérialiser
     */
    public void sauvegarderDeck(Deck deck) {
        saver.sauvegarder(deck);
    }
}
