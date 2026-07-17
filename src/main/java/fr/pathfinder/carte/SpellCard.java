package fr.pathfinder.carte;


public class SpellCard extends Card {


    private String ecole;


    public SpellCard(
            String nom,
            int niveau,
            String description,
            String ecole) {

        super(nom, niveau, description, CardType.SORT);

        this.ecole = ecole;
    }


    public String getEcole() {
        return ecole;
    }


    @Override
    public String afficherDetails() {

        return 
            "Nom : " + getNom() + "\n" +
            "Type : Sort\n" +
            "Niveau : " + getNiveau() + "\n" +
            "École : " + ecole + "\n" +
            "Description : " + getDescription();
    }
}