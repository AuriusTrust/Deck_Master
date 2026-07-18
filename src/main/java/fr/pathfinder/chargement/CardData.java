package fr.pathfinder.chargement;

import java.util.List;

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

    public String traits;

    public String coutIncantation;

    public String defense;

    public String portee;

    public String duree;

    /*
     * Champs spécifiques aux autres cartes.
     */
    public String emplacement;

    /*
     * Champs spécifiques aux capacités / dons.
     */
    public String actions;

    public String traitsCapacite;

    public String prerequis;

    public String declencheur;

    public String coutAction;
}