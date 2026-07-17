package fr.pathfinder.service;

import fr.pathfinder.carte.Deck;
import fr.pathfinder.chargement.CardLoader;
import fr.pathfinder.chargement.CardSaver;

public class CardService {

    private final CardLoader loader;
    private final CardSaver saver;

    public CardService() {
        loader = new CardLoader();
        saver = new CardSaver();
    }

    public Deck chargerDeck() {
        return loader.chargerCartes();
    }

    public void sauvegarderDeck(Deck deck) {
        saver.sauvegarder(deck);
    }
}