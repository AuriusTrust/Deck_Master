package fr.pathfinder.impression;

import fr.pathfinder.carte.Card;

/**
 * Regroupe une carte et le nombre d'exemplaires à imprimer.
 */
public class PrintDeckEntry {

    private final Card carte;
    private int quantite;

    public PrintDeckEntry(Card carte) {
        this.carte = carte;
        this.quantite = 1;
    }

    public Card getCarte() {
        return carte;
    }

    public int getQuantite() {
        return quantite;
    }

    public void incrementer() {
        quantite++;
    }

    public void decrementer() {
        if (quantite > 0) {
            quantite--;
        }
    }
}
