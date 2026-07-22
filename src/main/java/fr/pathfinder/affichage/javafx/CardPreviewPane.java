package fr.pathfinder.affichage.javafx;

import fr.pathfinder.carte.AbilityCard;
import fr.pathfinder.carte.ActionCost;
import fr.pathfinder.carte.ItemCard;
import fr.pathfinder.carte.SpellCard;
import fr.pathfinder.carte.AbilityCard;
import fr.pathfinder.carte.Card;
import fr.pathfinder.carte.ItemCard;
import fr.pathfinder.carte.SpellCard;

import java.io.InputStream;

import fr.pathfinder.carte.ActionCost;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class CardPreviewPane extends Pane {

        private static final double LARGEUR_CARTE = 350;
        private static final double HAUTEUR_CARTE = 490;

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

                actionsCarte.setLayoutX(266);
                actionsCarte.setLayoutY(58);

                actionsCarte.setPrefWidth(80);
                actionsCarte.setPrefHeight(28);

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

                descriptionCarte.setLayoutX(30);
                descriptionCarte.setLayoutY(105);

                descriptionCarte.setPrefWidth(306);
                descriptionCarte.setPrefHeight(327);

                descriptionCarte.setWrapText(true);
                descriptionCarte.setAlignment(Pos.TOP_LEFT);

                descriptionCarte.setFont(
                                Font.font(
                                                "Serif",
                                                14));

                detailsCarte.setLayoutX(30);
                detailsCarte.setLayoutY(58);

                detailsCarte.setPrefWidth(306);
                detailsCarte.setPrefHeight(42);

                detailsCarte.setWrapText(true);

                detailsCarte.setTextFill(
                                Color.web("#5A3A00"));

                detailsCarte.setFont(
                                Font.font(
                                                "Arial",
                                                FontWeight.BOLD,
                                                11));

                getChildren().add(fondCarte);
                getChildren().add(titreCarte);
                getChildren().add(traitsCarte);
                getChildren().add(actionsCarte);
                getChildren().add(niveauCarte);
                getChildren().add(descriptionCarte);
                getChildren().add(detailsCarte);
                detailsCarte.toFront();
        }

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