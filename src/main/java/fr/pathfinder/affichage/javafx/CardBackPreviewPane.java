package fr.pathfinder.affichage.javafx;

import java.net.URL;

import fr.pathfinder.carte.AbilityCard;
import fr.pathfinder.carte.Card;
import fr.pathfinder.carte.ItemCard;
import fr.pathfinder.carte.SpellCard;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * Composant JavaFX chargé d'afficher le verso statique d'une carte.
 */
public class CardBackPreviewPane extends Pane {

    private static final double LARGEUR_CARTE = 350;
    private static final double HAUTEUR_CARTE = 490;

    private static final String IMAGE_SORT = "/assets/cards/back_sort.png";
    private static final String IMAGE_CAPACITE = "/assets/cards/back_capacite.png";
    private static final String IMAGE_OBJET = "/assets/cards/back_objet.png";

    private final ImageView fondCarte;

    public CardBackPreviewPane() {

        setPrefSize(LARGEUR_CARTE, HAUTEUR_CARTE);
        setMinSize(LARGEUR_CARTE, HAUTEUR_CARTE);
        setMaxSize(LARGEUR_CARTE, HAUTEUR_CARTE);

        fondCarte = new ImageView();
        fondCarte.setFitWidth(LARGEUR_CARTE);
        fondCarte.setFitHeight(HAUTEUR_CARTE);
        fondCarte.setPreserveRatio(false);

        getChildren().add(fondCarte);
    }

    /**
     * Affiche le fond de verso correspondant au type concret de la carte.
     * Une carte absente laisse volontairement l'aperçu vide.
     *
     * @param carte carte dont le verso doit être affiché
     */
    public void afficherVerso(Card carte) {

        if (carte == null) {
            fondCarte.setImage(null);
            return;
        }

        String cheminImage;

        if (carte instanceof SpellCard) {
            cheminImage = IMAGE_SORT;
        } else if (carte instanceof AbilityCard) {
            cheminImage = IMAGE_CAPACITE;
        } else if (carte instanceof ItemCard) {
            cheminImage = IMAGE_OBJET;
        } else {
            throw new IllegalArgumentException(
                    "Type de carte non pris en charge pour le verso : "
                            + carte.getClass().getName());
        }

        URL ressourceImage = getClass().getResource(cheminImage);

        if (ressourceImage == null) {
            throw new IllegalStateException(
                    "Image de verso introuvable : " + cheminImage);
        }

        fondCarte.setImage(
                new Image(ressourceImage.toExternalForm()));
    }
}
