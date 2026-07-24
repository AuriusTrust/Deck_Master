package fr.pathfinder.carte;

/**
 * Traditions magiques auxquelles un sort peut appartenir.
 */
public enum Tradition {

    ARCANIQUE("Arcanique"),
    DIVINE("Divine"),
    OCCULTE("Occulte"),
    PRIMORDIALE("Primordiale");

    private final String libelle;

    Tradition(String libelle) {
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
