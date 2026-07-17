package fr.pathfinder.carte;

public class AbilityCard extends Card {
    private String nombreActions;


    public AbilityCard(
            String nom,
            int niveau,
            String description,
            String nombreActions) {

        super(nom, niveau, description, CardType.CAPACITE);

        this.nombreActions = nombreActions;
    }


    public String getNombreActions() {
        return nombreActions;
    }


    @Override
    public String afficherDetails() {

        return 
            "Nom : " + getNom() + "\n" +
            "Type : Capacité\n" +
            "Niveau : " + getNiveau() + "\n" +
            "Description : " + getDescription() + "\n" +
            "Nombre d'action : " + getNombreActions();
    }
}
