package fr.pathfinder.carte;

/**
 * Défenses pouvant être ciblées par un sort.
 */
public enum DefenseType {

    AUCUNE("Aucune"),
    CLASSE_ARMURE("Classe d'armure"),
    REFLEXES("Réflexes"),
    VIGUEUR("Vigueur"),
    VOLONTE("Volonté");

    private final String libelle;

    DefenseType(String libelle) {
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
