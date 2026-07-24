package fr.pathfinder.carte;

import java.util.ArrayList;
import java.util.List;

/**
 * Collection modifiable des cartes du catalogue principal.
 */
public class Deck {

    private List<Card> cartes;

    public Deck() {
        cartes = new ArrayList<>();
    }

    /**
     * Ajoute une carte au catalogue.
     *
     * @param carte carte à ajouter
     */
    public void ajouterCarte(Card carte) {
        cartes.add(carte);
    }

    /**
     * Supprime la carte située à l'index indiqué.
     *
     * @param index position de la carte
     * @return carte supprimée
     */
    public Card supprimerCarte(int index) {
        return cartes.remove(index);
    }
    public Card getCarte(int index) {
    return cartes.get(index);
}

    public List<Card> getCartes() {
        return cartes;
    }

    public int taille() {
        return cartes.size();
    }
}
