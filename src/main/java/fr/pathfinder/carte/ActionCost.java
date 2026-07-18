package fr.pathfinder.carte;

public enum ActionCost {

    AUCUNE("Aucune"),
    ACTION_LIBRE("Action libre"),
    REACTION("Réaction"),
    UNE_ACTION("1 action"),
    DEUX_ACTIONS("2 actions"),
    TROIS_ACTIONS("3 actions"),
    UNE_MINUTE("1 minute"),
    DIX_MINUTES("10 minutes"),
    UNE_HEURE("1 heure"),
    AUTRE("Autre");

    private final String libelle;

    ActionCost(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }

    @Override
    public String toString() {
        return libelle;
    }
}