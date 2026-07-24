package fr.pathfinder.impression;

import fr.pathfinder.carte.Card;

/**
 * Regroupe une carte et le nombre d'exemplaires à imprimer.
 */
public class PrintDeckEntry {

    private final Card carte;
    private int quantite;

    /**
     * Crée une entrée contenant un premier exemplaire de la carte.
     *
     * @param carte carte représentée par l'entrée
     */
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

    /**
     * Ajoute un exemplaire à imprimer.
     */
    public void incrementer() {
        quantite++;
    }

    /**
     * Retire un exemplaire sans autoriser une quantité négative.
     */
    public void decrementer() {
        if (quantite > 0) {
            quantite--;
        }
    }
}
