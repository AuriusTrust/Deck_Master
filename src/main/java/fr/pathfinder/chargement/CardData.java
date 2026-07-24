package fr.pathfinder.chargement;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;

/**
 * Représentation sérialisable d'une carte dans le fichier JSON.
 *
 * <p>Les champs optionnels dépendent du type de carte concerné.</p>
 */
public class CardData {

    public String type;

    public String nom;

    public int niveau;

    public String description;

    /*
     * Compatibilité ancienne version.
     */
    public String ecole;

    /*
     * Champs PF2e Remaster pour les sorts.
     */
    public List<String> traditions;

    @JsonAlias({"traitsObjet", "traitsCapacite"})
    public String traits;

    public String coutIncantation;

    public String defense;

    public String portee;

    public String duree;


    /*
     * Champs spécifiques aux objets.
     */
    public String emplacement;

    public String prixObjet;

    public String activationObjet;

    /*
     * Champs spécifiques aux capacités / dons.
     */
    public String prerequis;

    public String declencheur;

    @JsonAlias("actions")
    public String coutAction;
}
