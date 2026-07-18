package fr.pathfinder.carte;

public class AbilityCard extends Card {

    private String traits;
    private String prerequis;
    private String declencheur;
    private ActionCost coutAction;

    /*
     * Ancien constructeur conservé temporairement
     * pour rester compatible avec CardLoader.
     */
    public AbilityCard(
            String nom,
            int niveau,
            String description,
            String nombreActions) {

        super(
                nom,
                niveau,
                description,
                CardType.CAPACITE);

        this.traits = "";
        this.prerequis = "";
        this.declencheur = "";
        this.coutAction =
                convertirNombreActions(nombreActions);
    }

    private ActionCost convertirNombreActions(
            String nombreActions) {

        if (nombreActions == null
                || nombreActions.isBlank()) {

            return ActionCost.AUCUNE;
        }

        return switch (nombreActions.trim()) {

            case "Réaction", "REACTION" ->
                    ActionCost.REACTION;

            case "Action libre", "ACTION_LIBRE" ->
                    ActionCost.ACTION_LIBRE;

            case "1", "1 action", "UNE_ACTION" ->
                    ActionCost.UNE_ACTION;

            case "2", "2 actions", "DEUX_ACTIONS" ->
                    ActionCost.DEUX_ACTIONS;

            case "3", "3 actions", "TROIS_ACTIONS" ->
                    ActionCost.TROIS_ACTIONS;

            default ->
                    ActionCost.AUTRE;
        };
    }

    public String getTraits() {
        return traits;
    }

    public void setTraits(String traits) {
        this.traits = traits;
    }

    public String getPrerequis() {
        return prerequis;
    }

    public void setPrerequis(String prerequis) {
        this.prerequis = prerequis;
    }

    public String getDeclencheur() {
        return declencheur;
    }

    public void setDeclencheur(String declencheur) {
        this.declencheur = declencheur;
    }

    public ActionCost getCoutAction() {
        return coutAction;
    }

    public void setCoutAction(ActionCost coutAction) {
        this.coutAction = coutAction;
    }

    /*
     * Méthode temporairement conservée pour CardSaver.
     */
    public String getNombreActions() {
        return coutAction.name();
    }

    @Override
    public String afficherDetails() {

        return
                "Nom : " + getNom() + "\n" +
                "Type : Don / Capacité\n" +
                "Niveau : " + getNiveau() + "\n" +
                "Traits : " + traits + "\n" +
                "Coût : " + coutAction + "\n" +
                "Prérequis : " + prerequis + "\n" +
                "Déclencheur : " + declencheur + "\n" +
                "Description : " + getDescription();
    }
}