package fr.pathfinder.carte;

public class ItemCard extends Card {
    private String emplacement;


    public ItemCard(
            String nom,
            int niveau,
            String description,
            String emplacement) {

        super(nom, niveau, description, CardType.OBJET);

        this.emplacement = emplacement;
    }


    public String getEmplacement() {
        return emplacement;
    }


    @Override
    public String afficherDetails() {

        return 
            "Nom : " + getNom() + "\n" +
            "Type : Item\n" +
            "Niveau : " + getNiveau() + "\n" +
            "Emplacement : " + getEmplacement() + "\n" +
            "Description : " + getDescription();
    }
}
