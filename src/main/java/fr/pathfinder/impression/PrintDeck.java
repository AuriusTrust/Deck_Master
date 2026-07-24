package fr.pathfinder.impression;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.pathfinder.carte.Card;

/**
 * Modèle temporaire du deck à imprimer.
 *
 * <p>Chaque carte distincte est représentée par une entrée portant sa quantité.</p>
 */
public class PrintDeck {

    private final List<PrintDeckEntry> entrees;

    public PrintDeck() {
        entrees = new ArrayList<>();
    }

    /**
     * Ajoute un exemplaire d'une carte, ou incrémente son entrée existante.
     * La comparaison repose volontairement sur l'identité de l'instance.
     *
     * @param carte carte à ajouter
     */
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

    /**
     * Retire un exemplaire et supprime l'entrée lorsque sa quantité atteint zéro.
     *
     * @param carte carte dont un exemplaire doit être retiré
     */
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

    /**
     * Supprime immédiatement une entrée complète, quelle que soit sa quantité.
     *
     * @param entree entrée à supprimer
     */
    public void supprimerEntree(PrintDeckEntry entree) {

        if (entree == null) {
            return;
        }

        entrees.remove(entree);
    }

    /**
     * Retourne une vue non modifiable des entrées d'impression.
     *
     * @return entrées actuellement préparées
     */
    public List<PrintDeckEntry> getEntrees() {
        return Collections.unmodifiableList(entrees);
    }

    /**
     * Calcule le nombre total d'exemplaires à imprimer.
     *
     * @return somme des quantités de toutes les entrées
     */
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
