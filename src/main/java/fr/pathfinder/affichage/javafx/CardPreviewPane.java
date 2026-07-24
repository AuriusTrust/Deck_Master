package fr.pathfinder.affichage.javafx;

import fr.pathfinder.carte.AbilityCard;
import fr.pathfinder.carte.ActionCost;
import fr.pathfinder.carte.DefenseType;
import fr.pathfinder.carte.ItemCard;
import fr.pathfinder.carte.SpellCard;
import fr.pathfinder.carte.AbilityCard;
import fr.pathfinder.carte.Card;
import fr.pathfinder.carte.ItemCard;
import fr.pathfinder.carte.SpellCard;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import fr.pathfinder.carte.ActionCost;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

/**
 * Composant JavaFX chargé de dessiner l'aperçu visuel d'une carte.
 */
public class CardPreviewPane extends Pane {

        private static final double LARGEUR_CARTE = 350;
        private static final double HAUTEUR_CARTE = 490;
        private static final double LARGEUR_CONTENU = 316;
        private static final double LARGEUR_DESCRIPTION = 306;
        private static final double LARGEUR_ACTIONS = 80;
        private static final double LARGEUR_DETAILS =
                        LARGEUR_CONTENU - LARGEUR_ACTIONS;
        private static final double HAUTEUR_LIGNE_DETAILS = 42;
        private static final double ESPACEMENT_CONTENU = 5;

        private static final String IMAGE_SORT = "/assets/cards/card_sort.png";

        private static final String IMAGE_CAPACITE = "/assets/cards/card_capacite.png";

        private static final String IMAGE_OBJET = "/assets/cards/card_objet.png";

        private final Label titreCarte;
        private final Label traditionCarte;
        private final Label niveauCarte;
        private final Label descriptionCarte;
        private final Label traitsCarte;
        private final Label actionsCarte;
        private final ImageView fondCarte;
        private final Label detailsCarte;
        private final HBox ligneDetailsActions;
        private final VBox contenuDynamique;

        private Font policeTitre;

        public CardPreviewPane() {

                setPrefSize(
                                LARGEUR_CARTE,
                                HAUTEUR_CARTE);

                setMinSize(
                                LARGEUR_CARTE,
                                HAUTEUR_CARTE);

                setMaxSize(
                                LARGEUR_CARTE,
                                HAUTEUR_CARTE);

                InputStream fichierImage = getClass().getResourceAsStream(
                                IMAGE_SORT);

                if (fichierImage == null) {

                        throw new IllegalStateException(
                                        "Image introuvable : "
                                                        + IMAGE_SORT);
                }

                Image imageCarte = new Image(fichierImage);

                fondCarte = new ImageView(imageCarte);

                fondCarte.setFitWidth(
                                LARGEUR_CARTE);

                fondCarte.setFitHeight(
                                HAUTEUR_CARTE);

                fondCarte.setPreserveRatio(false);

                fondCarte.setLayoutX(0);
                fondCarte.setLayoutY(0);

                titreCarte = new Label("Titre");
                traditionCarte = new Label("Tradition");
                traitsCarte = new Label("");
                actionsCarte = new Label("");
                niveauCarte = new Label("0");
                descriptionCarte = new Label("");
                detailsCarte = new Label("");

                try {

                        InputStream policeStream = getClass().getResourceAsStream(
                                        "/assets/fonts/Taroca Regular.TTF");

                        if (policeStream != null) {

                                policeTitre = Font.loadFont(
                                                policeStream,
                                                16);
                        }

                } catch (Exception e) {

                        e.printStackTrace();
                }
                titreCarte.setLayoutX(18);
                titreCarte.setLayoutY(8);

                titreCarte.setPrefWidth(256);
                titreCarte.setPrefHeight(27);

                titreCarte.setTextFill(Color.WHITE);
                if (policeTitre != null) {

                        titreCarte.setFont(
                                        policeTitre);

                } else {

                        titreCarte.setFont(
                                        Font.font(
                                                        "Serif",
                                                        FontWeight.BOLD,
                                                        FontPosture.ITALIC,
                                                        24));
                }

                traitsCarte.setLayoutX(30);
                traitsCarte.setLayoutY(37);

                traitsCarte.setPrefWidth(240);
                traitsCarte.setPrefHeight(20);

                traitsCarte.setTextFill(
                                Color.web("#ffffff"));

                traitsCarte.setFont(
                                Font.font(
                                                "Arial",
                                                FontWeight.BOLD,
                                                10));

                niveauCarte.setLayoutX(288);
                niveauCarte.setLayoutY(14);

                niveauCarte.setPrefWidth(36);
                niveauCarte.setPrefHeight(36);

                niveauCarte.setTextFill(Color.web("#5A3A00"));
                niveauCarte.setAlignment(Pos.CENTER);

                niveauCarte.setFont(
                                Font.font(
                                                "Serif",
                                                FontWeight.BOLD,
                                                20));

                actionsCarte.setPrefWidth(80);
                actionsCarte.setPrefHeight(28);
                actionsCarte.setMinWidth(LARGEUR_ACTIONS);
                actionsCarte.setMaxWidth(LARGEUR_ACTIONS);

                actionsCarte.setAlignment(Pos.CENTER);

                actionsCarte.setTextFill(
                                Color.web("#5A3A00"));

                actionsCarte.setFont(
                                Font.font(
                                                "Arial",
                                                FontWeight.BOLD,
                                                22));

                actionsCarte.setTextOverrun(
                                javafx.scene.control.OverrunStyle.CLIP);

                descriptionCarte.setWrapText(true);
                descriptionCarte.setAlignment(Pos.TOP_LEFT);
                descriptionCarte.setMinHeight(0);
                descriptionCarte.setMaxWidth(LARGEUR_DESCRIPTION);
                descriptionCarte.setMaxHeight(Double.MAX_VALUE);
                descriptionCarte.setTextOverrun(
                                javafx.scene.control.OverrunStyle.CLIP);

                descriptionCarte.setFont(
                                Font.font(
                                                "Serif",
                                                14));

                detailsCarte.setWrapText(true);
                detailsCarte.setMinWidth(0);
                detailsCarte.setPrefWidth(LARGEUR_DETAILS);
                detailsCarte.setMaxWidth(LARGEUR_DETAILS);
                detailsCarte.setMinHeight(
                                javafx.scene.layout.Region.USE_PREF_SIZE);
                detailsCarte.setTextOverrun(
                                javafx.scene.control.OverrunStyle.CLIP);

                detailsCarte.setTextFill(
                                Color.web("#5A3A00"));

                detailsCarte.setFont(
                                Font.font(
                                                "Arial",
                                FontWeight.BOLD,
                                11));

                /*
                 * Les détails occupent uniquement l'espace situé à gauche
                 * des symboles d'actions. La ligne grandit verticalement
                 * lorsque le texte nécessite davantage de lignes.
                 */
                ligneDetailsActions = new HBox(
                                detailsCarte,
                                actionsCarte);
                ligneDetailsActions.setMinHeight(
                                HAUTEUR_LIGNE_DETAILS);
                ligneDetailsActions.setPrefWidth(
                                LARGEUR_CONTENU);

                HBox.setHgrow(
                                detailsCarte,
                                Priority.ALWAYS);

                /*
                 * La description suit automatiquement la hauteur calculée
                 * de la ligne précédente et utilise tout l'espace restant.
                 */
                contenuDynamique = new VBox(
                                ESPACEMENT_CONTENU,
                                ligneDetailsActions,
                                descriptionCarte);
                contenuDynamique.setLayoutX(30);
                contenuDynamique.setLayoutY(58);
                contenuDynamique.setPrefWidth(
                                LARGEUR_CONTENU);
                contenuDynamique.setPrefHeight(374);
                contenuDynamique.setFillWidth(true);

                VBox.setVgrow(
                                descriptionCarte,
                                Priority.ALWAYS);

                getChildren().add(fondCarte);
                getChildren().add(titreCarte);
                getChildren().add(traitsCarte);
                getChildren().add(niveauCarte);
                getChildren().add(contenuDynamique);
        }

        /**
         * Met à jour tous les textes et indicateurs visibles dans l'aperçu.
         */
        public void afficherCarte(
                        String titre,
                        int niveau,
                        String description,
                        String tradition,
                        String traits,
                        ActionCost coutAction,
                        String details) {

                titreCarte.setText(titre);

                niveauCarte.setText(
                                niveau == 0 ? "" : String.valueOf(niveau));

                descriptionCarte.setText(
                                description == null ? "" : description);

                String informations = "";

                if (tradition != null && !tradition.isBlank()) {
                        informations = tradition;
                }

                if (traits != null && !traits.isBlank()) {

                        if (!informations.isBlank()) {
                                informations += " | ";
                        }

                        informations += traits.replace(",", " •");
                }

                traitsCarte.setText(informations);

                actionsCarte.setText(
                                symboleAction(coutAction));

                detailsCarte.setText(
                                details == null ? "" : details);
        }

        /**
         * Configure l'aperçu complet à partir d'une carte métier.
         *
         * @param carte carte à représenter
         */
        public void afficherCarte(Card carte) {

                if (carte == null) {
                        afficherCarte("", 0, "", "", "", null, "");
                        afficherFondPour(null);
                        return;
                }

                String tradition = "";
                String traits = "";
                String details = "";
                ActionCost coutAction = ActionCost.AUCUNE;

                if (carte instanceof SpellCard sort) {

                        if (!sort.getTraditions().isEmpty()) {
                                tradition = sort.getTraditions()
                                                .get(0)
                                                .toString();
                        }

                        traits = sort.getTraits();
                        coutAction = sort.getCoutIncantation();

                        List<String> lignePrincipale = new ArrayList<>();
                        ajouterInformation(
                                        lignePrincipale,
                                        "Portée",
                                        sort.getPortee());
                        ajouterInformation(
                                        lignePrincipale,
                                        "Durée",
                                        sort.getDuree());

                        List<String> lignes = new ArrayList<>();

                        if (!lignePrincipale.isEmpty()) {
                                lignes.add(
                                                String.join(
                                                                " | ",
                                                                lignePrincipale));
                        }

                        if (sort.getDefense() != null
                                        && sort.getDefense()
                                                        != DefenseType.AUCUNE) {
                                lignes.add(
                                                "Défense : "
                                                                + sort.getDefense());
                        }

                        details = String.join("\n", lignes);

                } else if (carte instanceof AbilityCard capacite) {

                        traits = capacite.getTraits();
                        coutAction = capacite.getCoutAction();

                        List<String> lignes = new ArrayList<>();
                        ajouterInformation(
                                        lignes,
                                        "Prérequis",
                                        capacite.getPrerequis());
                        ajouterInformation(
                                        lignes,
                                        "Déclencheur",
                                        capacite.getDeclencheur());

                        details = String.join("\n", lignes);

                } else if (carte instanceof ItemCard objet) {

                        traits = objet.getTraits();
                        coutAction = objet.getActivation();

                        List<String> informations = new ArrayList<>();
                        ajouterInformation(
                                        informations,
                                        "Prix",
                                        objet.getPrix());
                        ajouterInformation(
                                        informations,
                                        "Usage",
                                        objet.getEmplacement());

                        details = String.join(
                                        " | ",
                                        informations);
                }

                afficherFondPour(carte);

                afficherCarte(
                                carte.getNom(),
                                carte.getNiveau(),
                                carte.getDescription(),
                                tradition,
                                traits,
                                coutAction,
                                details);
        }

        /**
         * Ajoute une information légendée uniquement lorsque sa valeur est
         * réellement renseignée.
         */
        private void ajouterInformation(
                        List<String> informations,
                        String libelle,
                        String valeur) {

                if (valeur != null && !valeur.isBlank()) {
                        informations.add(
                                        libelle
                                                        + " : "
                                                        + valeur);
                }
        }

        private String symboleAction(
                        ActionCost coutAction) {

                if (coutAction == null) {
                        return "";
                }

                return switch (coutAction) {

                        case ACTION_LIBRE -> "◇";
                        case UNE_ACTION -> "◆";
                        case DEUX_ACTIONS -> "◆◆";
                        case TROIS_ACTIONS -> "◆◆◆";
                        case REACTION -> "↺";

                        default -> "";
                };
        }

        /**
         * Sélectionne le fond graphique correspondant au type concret de la carte.
         *
         * @param carte carte dont le type détermine le fond
         */
        public void afficherFondPour(
                        Card carte) {

                String cheminImage;

                if (carte instanceof SpellCard) {

                        cheminImage = IMAGE_SORT;

                } else if (carte instanceof AbilityCard) {

                        cheminImage = IMAGE_CAPACITE;

                } else if (carte instanceof ItemCard) {

                        cheminImage = IMAGE_OBJET;

                } else {

                        cheminImage = IMAGE_SORT;
                }

                InputStream fichierImage = getClass()
                                .getResourceAsStream(
                                                cheminImage);

                if (fichierImage == null) {

                        throw new IllegalStateException(
                                        "Image introuvable : "
                                                        + cheminImage);
                }

                fondCarte.setImage(
                                new Image(fichierImage));
        }
}
