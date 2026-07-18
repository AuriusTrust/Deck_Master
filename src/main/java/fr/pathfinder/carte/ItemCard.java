package fr.pathfinder.carte;

public class ItemCard extends Card {

    private String emplacement;

    private String prix;

    private String traits;

    private ActionCost activation;

    public ItemCard(
            String nom,
            int niveau,
            String description,
            String emplacement) {

        super(
                nom,
                niveau,
                description,
                CardType.OBJET);

        this.emplacement = emplacement;

        this.prix = "";

        this.traits = "";

        this.activation = ActionCost.AUCUNE;
    }

    public String getEmplacement() {
        return emplacement;
    }

    public void setEmplacement(
            String emplacement) {

        this.emplacement = emplacement;
    }

    public String getPrix() {
        return prix;
    }

    public void setPrix(
            String prix) {

        this.prix = prix;
    }

    public String getTraits() {
        return traits;
    }

    public void setTraits(
            String traits) {

        this.traits = traits;
    }

    public ActionCost getActivation() {
        return activation;
    }

    public void setActivation(
            ActionCost activation) {

        this.activation = activation;
    }

    @Override
    public String afficherDetails() {

        return
                "Nom : " + getNom() + "\n" +
                "Type : Objet\n" +
                "Niveau : " + getNiveau() + "\n" +
                "Prix : " + prix + "\n" +
                "Traits : " + traits + "\n" +
                "Activation : " + activation + "\n" +
                "Emplacement : " + emplacement + "\n" +
                "Description : " + getDescription();
    }
}