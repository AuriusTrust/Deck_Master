package fr.pathfinder.affichage.javafx;

import java.io.InputStream;
import java.net.URL;

import fr.pathfinder.carte.AbilityCard;
import fr.pathfinder.carte.Card;
import fr.pathfinder.carte.ItemCard;
import fr.pathfinder.carte.SpellCard;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * Composant JavaFX chargé d'afficher le verso statique d'une carte.
 */
public class CardBackPreviewPane extends Pane {

    private static final double LARGEUR_CARTE = 350;
    private static final double HAUTEUR_CARTE = 490;

    private static final String IMAGE_SORT = "/assets/cards/back_sort.png";
    private static final String IMAGE_CAPACITE = "/assets/cards/back_capacite.png";
    private static final String IMAGE_OBJET = "/assets/cards/back_objet.png";

    private static final String POLICE_TAROCA = "/assets/fonts/Taroca Regular.TTF";
    private static final String POLICE_DWARVES = "/assets/fonts/Dwarves.TTF";
    private static final String TITRE_PRINCIPAL = "TOURGRIM BRISEFORGE";

    /*
     * Positions initiales des textes dans les cartouches du verso.
     * Ces valeurs sont volontairement regroupées pour faciliter les ajustements.
     */
    private static final double TEXTE_X = 25;
    private static final double LARGEUR_TEXTE = 300;
    private static final double POSITION_TITRE_Y = 82;
    private static final double POSITION_SOUS_TITRE_Y = 101;
    private static final double POSITION_TYPE_Y = 410;

    private static final double TAILLE_TITRE = 11;
    private static final double TAILLE_SOUS_TITRE = 12;
    private static final double TAILLE_TYPE = 20;
    private static final double ESPACEMENT_TITRE = 1;

    private static final Color COULEUR_TEXTE = Color.web("#F2D58A");

    private final ImageView fondCarte;
    private final HBox titrePrincipal;
    private final Label sousTitre;
    private final Label typeCarte;

    public CardBackPreviewPane() {

        setPrefSize(LARGEUR_CARTE, HAUTEUR_CARTE);
        setMinSize(LARGEUR_CARTE, HAUTEUR_CARTE);
        setMaxSize(LARGEUR_CARTE, HAUTEUR_CARTE);

        fondCarte = new ImageView();
        fondCarte.setFitWidth(LARGEUR_CARTE);
        fondCarte.setFitHeight(HAUTEUR_CARTE);
        fondCarte.setPreserveRatio(false);

        Font policeTitre = chargerPolice(
                POLICE_DWARVES,
                TAILLE_TITRE);

        Font policeTaroca = chargerPolice(
                POLICE_TAROCA,
                TAILLE_TITRE);

        titrePrincipal = creerTitreCentre(policeTitre);
        titrePrincipal.setVisible(false);

        sousTitre = creerLabelCentre(
                POSITION_SOUS_TITRE_Y,
                Font.font(
                        policeTaroca.getFamily(),
                        FontWeight.NORMAL,
                        TAILLE_SOUS_TITRE));

        typeCarte = creerLabelCentre(
                POSITION_TYPE_Y,
                Font.font(
                        policeTaroca.getFamily(),
                        FontWeight.BOLD,
                        TAILLE_TYPE));

        getChildren().addAll(
                fondCarte,
                titrePrincipal,
                sousTitre,
                typeCarte);
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
            titrePrincipal.setVisible(false);
            sousTitre.setText("");
            typeCarte.setText("");
            return;
        }

        String cheminImage;
        String texteSousTitre;
        String texteType;

        if (carte instanceof SpellCard) {
            cheminImage = IMAGE_SORT;
            texteSousTitre = "Gardien de la Malrune";
            texteType = "MAGIE";
        } else if (carte instanceof AbilityCard) {
            cheminImage = IMAGE_CAPACITE;
            texteSousTitre = "Torpille de Feu et de Métal";
            texteType = "CAPACITÉ";
        } else if (carte instanceof ItemCard) {
            cheminImage = IMAGE_OBJET;
            texteSousTitre = "Vérité incarnée";
            texteType = "OBJET";
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

        titrePrincipal.setVisible(true);
        sousTitre.setText(texteSousTitre);
        typeCarte.setText(texteType);
    }

    private HBox creerTitreCentre(Font police) {

        HBox conteneurTitre = new HBox(ESPACEMENT_TITRE);

        conteneurTitre.setLayoutX(TEXTE_X);
        conteneurTitre.setLayoutY(POSITION_TITRE_Y);
        conteneurTitre.setPrefWidth(LARGEUR_TEXTE);
        conteneurTitre.setAlignment(Pos.CENTER);

        for (char caractere : TITRE_PRINCIPAL.toCharArray()) {
            Text lettre = new Text(String.valueOf(caractere));

            lettre.setFont(police);
            lettre.setFill(COULEUR_TEXTE);

            conteneurTitre.getChildren().add(lettre);
        }

        return conteneurTitre;
    }

    private Label creerLabelCentre(
            double positionY,
            Font police) {

        Label label = new Label();

        label.setLayoutX(TEXTE_X);
        label.setLayoutY(positionY);
        label.setPrefWidth(LARGEUR_TEXTE);
        label.setAlignment(Pos.CENTER);
        label.setTextFill(COULEUR_TEXTE);
        label.setFont(police);

        return label;
    }

    private Font chargerPolice(
            String cheminPolice,
            double taille) {

        try (InputStream policeStream = getClass().getResourceAsStream(
                cheminPolice)) {

            if (policeStream != null) {
                Font policeChargee = Font.loadFont(
                        policeStream,
                        taille);

                if (policeChargee != null) {
                    return policeChargee;
                }
            }
        } catch (Exception exception) {
            // La police standard ci-dessous garantit un aperçu utilisable.
        }

        return Font.font("Serif", taille);
    }
}
