package fr.pathfinder.carte;

import java.util.ArrayList;
import java.util.List;

public class Deck {

    private List<Card> cartes;

    public Deck() {
        cartes = new ArrayList<>();
    }

    public void ajouterCarte(Card carte) {
        cartes.add(carte);
    }

    public Card supprimerCarte(int index) {
        return cartes.remove(index);
    }

    public List<Card> getCartes() {
        return cartes;
    }

    public int taille() {
        return cartes.size();
    }
}