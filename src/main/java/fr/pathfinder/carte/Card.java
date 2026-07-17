package fr.pathfinder.carte;

public abstract class Card {

    private String nom;
    private int niveau;
    private String description;
    private CardType type;

    public Card(String nom, int niveau, String description, CardType type) {
        this.nom = nom;
        this.niveau = niveau;
        this.description = description;
        this.type = type;
    }

    public String getNom() {
        return nom;
    }

    public int getNiveau() {
        return niveau;
    }

    public String getDescription() {
        return description;
    }

    public CardType getType() {
        return type;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setNiveau(int niveau) {
        this.niveau = niveau;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    // Chaque carte devra savoir s'afficher elle-même
    public abstract String afficherDetails();
}