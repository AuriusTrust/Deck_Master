package fr.pathfinder.carte;

import java.util.ArrayList;
import java.util.List;

/**
 * Carte représentant un sort et ses propriétés d'incantation.
 */
public class SpellCard extends Card {

    /*
     * Conservé uniquement pour les anciennes cartes
     * pré-Remaster.
     */
    private String ecole;

    private List<Tradition> traditions;
    private ActionCost coutIncantation;
    private DefenseType defense;
    private String portee;
    private String duree;
    private String traits;

    /*
     * Ancien constructeur conservé pour ne pas casser
     * CardLoader et les cartes déjà enregistrées.
     */
    public SpellCard(
            String nom,
            int niveau,
            String description,
            String ecole) {

        super(
                nom,
                niveau,
                description,
                CardType.SORT);

        this.ecole = ecole;

        this.traditions = new ArrayList<>();
        this.coutIncantation = ActionCost.AUCUNE;
        this.defense = DefenseType.AUCUNE;
        this.portee = "";
        this.duree = "";
        this.traits = "";
    }

    public String getEcole() {
        return ecole;
    }

    public void setEcole(String ecole) {
        this.ecole = ecole;
    }

    public List<Tradition> getTraditions() {
        return traditions;
    }

    public void setTraditions(
            List<Tradition> traditions) {

        this.traditions = traditions;
    }

    public ActionCost getCoutIncantation() {
        return coutIncantation;
    }

    public void setCoutIncantation(
            ActionCost coutIncantation) {

        this.coutIncantation = coutIncantation;
    }

    public DefenseType getDefense() {
        return defense;
    }

    public void setDefense(
            DefenseType defense) {

        this.defense = defense;
    }

    public String getPortee() {
        return portee;
    }

    public void setPortee(String portee) {
        this.portee = portee;
    }

    public String getDuree() {
        return duree;
    }

    public String getTraits() {
        return traits;
    }

    public void setTraits(String traits) {
        this.traits = traits;
    }

    public void setDuree(String duree) {
        this.duree = duree;
    }

    @Override
    public String afficherDetails() {

        String traditionsTexte =
                traditions.isEmpty()
                        ? "Non renseignées"
                        : traditions.toString();

        return
                "Nom : " + getNom() + "\n" +
                "Type : Sort\n" +
                "Rang : " + getNiveau() + "\n" +
                "Traditions : " + traditionsTexte + "\n" +
                "Traits : " + traits + "\n" +
                "Incantation : "
                        + coutIncantation + "\n" +
                "Portée : " + portee + "\n" +
                "Durée : " + duree + "\n" +
                "Défense : " + defense + "\n" +
                "Ancienne école : " + ecole + "\n" +
                "Description : "
                        + getDescription();
    }
}
