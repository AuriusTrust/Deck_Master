package fr.pathfinder.impression;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.pathfinder.carte.Card;

public class PrintDeck {

    private final List<PrintDeckEntry> entrees;

    public PrintDeck() {
        entrees = new ArrayList<>();
    }

    public void ajouterCarte(Card carte) {

        if (carte == null) {
            return;
        }

        for (PrintDeckEntry entree : entrees) {
            if (entree.getCarte() == carte) {
                entree.incrementer();
                return;
            }
        }

        entrees.add(new PrintDeckEntry(carte));
    }

    public void retirerCarte(Card carte) {

        if (carte == null) {
            return;
        }

        for (int index = 0; index < entrees.size(); index++) {
            PrintDeckEntry entree = entrees.get(index);

            if (entree.getCarte() == carte) {
                if (entree.getQuantite() > 1) {
                    entree.decrementer();
                } else {
                    entrees.remove(index);
                }
                return;
            }
        }
    }

    public void supprimerEntree(PrintDeckEntry entree) {

        if (entree == null) {
            return;
        }

        entrees.remove(entree);
    }

    public List<PrintDeckEntry> getEntrees() {
        return Collections.unmodifiableList(entrees);
    }

    public int taille() {
        int total = 0;

        for (PrintDeckEntry entree : entrees) {
            total += entree.getQuantite();
        }

        return total;
    }

    public boolean estVide() {
        return entrees.isEmpty();
    }
}
