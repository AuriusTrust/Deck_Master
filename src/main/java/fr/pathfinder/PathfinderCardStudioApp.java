package fr.pathfinder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import fr.pathfinder.carte.AbilityCard;
import fr.pathfinder.carte.Card;
import fr.pathfinder.carte.Deck;
import fr.pathfinder.carte.ItemCard;
import fr.pathfinder.carte.SpellCard;
import fr.pathfinder.impression.PdfExporter;
import fr.pathfinder.impression.PrintDeck;
import fr.pathfinder.impression.PrintDeckEntry;
import fr.pathfinder.service.CardService;
import fr.pathfinder.affichage.javafx.ItemForm;
import fr.pathfinder.affichage.javafx.SpellForm;
import fr.pathfinder.affichage.javafx.AbilityForm;
import fr.pathfinder.affichage.javafx.CardBackPreviewPane;
import fr.pathfinder.affichage.javafx.CardPreviewPane;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Interface graphique principale de Pathfinder Card Studio.
 *
 * <p>Elle coordonne le catalogue, les formulaires d'édition, l'aperçu
 * et la préparation du deck d'impression.</p>
 */
public class PathfinderCardStudioApp extends Application {

        @Override
        public void start(Stage stage) {

                CardService cardService = new CardService();
                Deck deck = cardService.chargerDeck();
                PrintDeck printDeck = new PrintDeck();
                PdfExporter pdfExporter = new PdfExporter();

                /*
                 * Liste des cartes.
                 */
                ListView<Card> listeCartes = new ListView<>();
                ListView<PrintDeckEntry> listeImpression = new ListView<>();

                listeCartes.getItems().addAll(
                                deck.getCartes());

                listeCartes.setCellFactory(param -> new ListCell<>() {

                        @Override
                        protected void updateItem(
                                        Card carte,
                                        boolean empty) {

                                super.updateItem(carte, empty);

                                if (empty || carte == null) {
                                        setText(null);
                                } else {
                                        setText(carte.getNom());
                                }
                        }
                });

                /*
                 * Champs communs à toutes les cartes.
                 */
                Label titreFormulaire = new Label("Détails de la carte");

                Label labelNom = new Label("Nom");

                TextField champNom = new TextField();

                Label labelNiveau = new Label("Niveau");

                TextField champNiveau = new TextField();

                Label labelDescription = new Label("Description");

                TextArea champDescription = new TextArea();

                champDescription.setWrapText(true);
                champDescription.setPrefRowCount(10);

                /*
                 * Formulaire spécifique aux sorts.
                 */
                SpellForm formulaireSort = new SpellForm();

                /*
                 * Champs spécifiques aux dons et capacités.
                 */
                AbilityForm formulaireCapacite = new AbilityForm();

                ItemForm formulaireObjet = new ItemForm();

                CardPreviewPane apercuCarte = new CardPreviewPane();
                CardBackPreviewPane apercuVerso = new CardBackPreviewPane();

                /*
                 * Boutons.
                 */
                Button boutonAjouter = new Button("Ajouter");

                Button boutonSupprimer = new Button("Supprimer");

                boutonSupprimer.setDisable(true);

                Button boutonSauvegarder = new Button("Sauvegarder");

                boutonSauvegarder.setDisable(true);

                Button boutonRecto = new Button("Recto");
                Button boutonVerso = new Button("Verso");

                Button boutonAjouterImpression = new Button("Ajouter au deck");

                boutonAjouterImpression.setDisable(true);

                Button boutonIncrementerImpression = new Button("+");
                Button boutonDecrementerImpression = new Button("-");
                Button boutonRetirerImpression = new Button("Retirer du deck");
                Button boutonExporterPdf = new Button("Exporter PDF");
                Button boutonCalibrationRectoVerso = new Button(
                                "Calibration recto-verso");

                Label compteurDeckImpression = new Label("0 cartes — 0 pages");

                Runnable actualiserCompteurDeckImpression = () -> {
                        int nombreCartes = printDeck.taille();
                        int nombrePages = nombreCartes == 0
                                        ? 0
                                        : ((nombreCartes + 8) / 9) * 2;

                        compteurDeckImpression.setText(
                                        nombreCartes
                                                        + " cartes — "
                                                        + nombrePages
                                                        + " pages");
                };

                boutonIncrementerImpression.setDisable(true);
                boutonDecrementerImpression.setDisable(true);
                boutonRetirerImpression.setDisable(true);

                /*
                 * Les actions sur le deck nécessitent une entrée sélectionnée.
                 */
                listeImpression
                                .getSelectionModel()
                                .selectedItemProperty()
                                .addListener((observable, ancienneEntree, nouvelleEntree) -> {

                                        boolean aucuneEntreeSelectionnee = nouvelleEntree == null;

                                        boutonIncrementerImpression.setDisable(
                                                        aucuneEntreeSelectionnee);

                                        boutonDecrementerImpression.setDisable(
                                                        aucuneEntreeSelectionnee);

                                        boutonRetirerImpression.setDisable(
                                                        aucuneEntreeSelectionnee);
                                });

                /*
                 * Formulaire principal.
                 */
                VBox formulaire = new VBox(
                                10,
                                titreFormulaire,
                                labelNom,
                                champNom,
                                labelNiveau,
                                champNiveau,
                                formulaireSort,
                                formulaireCapacite,
                                formulaireObjet,
                                labelDescription,
                                champDescription);

                formulaire.setPadding(
                                new Insets(20));

                formulaire.setPrefWidth(500);

                /*
                 * Changement de carte sélectionnée.
                 */
                listeCartes
                                .getSelectionModel()
                                .selectedItemProperty()
                                .addListener(
                                                (observable,
                                                                ancienneCarte,
                                                                nouvelleCarte) -> {

                                                        if (nouvelleCarte == null) {

                                                                champNom.clear();
                                                                champNiveau.clear();
                                                                champDescription.clear();

                                                                formulaireSort.masquer();
                                                                formulaireCapacite.masquer();
                                                                formulaireObjet.masquer();

                                                                apercuCarte.afficherCarte("", 0, "", "", "", null, "");
                                                                apercuVerso.afficherVerso(null);

                                                                boutonSauvegarder.setDisable(true);
                                                                boutonSupprimer.setDisable(true);
                                                                boutonAjouterImpression.setDisable(true);

                                                                return;
                                                        }

                                                        champNom.setText(
                                                                        nouvelleCarte.getNom());

                                                        champNiveau.setText(
                                                                        String.valueOf(
                                                                                        nouvelleCarte.getNiveau()));

                                                        champDescription.setText(
                                                                        nouvelleCarte.getDescription());

                                                        apercuCarte.afficherCarte(
                                                                        nouvelleCarte);

                                                        apercuVerso.afficherVerso(
                                                                        nouvelleCarte);

                                                        if (nouvelleCarte instanceof SpellCard sort) {

                                                                formulaireSort.afficher(sort);

                                                                formulaireCapacite.setVisible(false);
                                                                formulaireCapacite.setManaged(false);

                                                                formulaireObjet.masquer();

                                                        } else if (nouvelleCarte instanceof AbilityCard capacite) {

                                                                formulaireSort.masquer();

                                                                formulaireCapacite.afficher(capacite);

                                                                formulaireObjet.masquer();

                                                        } else if (nouvelleCarte instanceof ItemCard objet) {

                                                                formulaireSort.masquer();

                                                                formulaireCapacite.setVisible(false);
                                                                formulaireCapacite.setManaged(false);

                                                                formulaireObjet.afficher(objet);

                                                        } else {

                                                                formulaireSort.masquer();

                                                                formulaireCapacite.masquer();

                                                                formulaireObjet.masquer();

                                                        }

                                                        boutonSauvegarder.setDisable(false);
                                                        boutonSupprimer.setDisable(false);
                                                        boutonAjouterImpression.setDisable(false);
                                                });

                /*
                 * Ajouter une carte.
                 */
                boutonAjouter.setOnAction(event -> {

                        ChoiceDialog<String> dialogueType = new ChoiceDialog<>(
                                        "Sort",
                                        "Sort",
                                        "Objet",
                                        "Capacité");

                        dialogueType.setTitle(
                                        "Ajouter une carte");

                        dialogueType.setHeaderText(
                                        "Choisissez le type de carte");

                        dialogueType.setContentText(
                                        "Type :");

                        dialogueType.showAndWait()
                                        .ifPresent(typeChoisi -> {

                                                Card nouvelleCarte;

                                                switch (typeChoisi) {

                                                        case "Objet":

                                                                nouvelleCarte = new ItemCard(
                                                                                "Nouvel objet",
                                                                                1,
                                                                                "",
                                                                                "");

                                                                break;

                                                        case "Capacité":

                                                                nouvelleCarte = new AbilityCard(
                                                                                "Nouvelle capacité",
                                                                                1,
                                                                                "",
                                                                                "1");

                                                                break;

                                                        case "Sort":
                                                        default:

                                                                nouvelleCarte = new SpellCard(
                                                                                "Nouveau sort",
                                                                                1,
                                                                                "",
                                                                                "");

                                                                break;
                                                }

                                                deck.ajouterCarte(
                                                                nouvelleCarte);

                                                listeCartes.getItems().add(
                                                                nouvelleCarte);

                                                listeCartes
                                                                .getSelectionModel()
                                                                .select(nouvelleCarte);

                                                cardService.sauvegarderDeck(
                                                                deck);
                                        });
                });

                /*
                 * Supprimer une carte.
                 */
                boutonSupprimer.setOnAction(event -> {

                        Card carteSelectionnee = listeCartes
                                        .getSelectionModel()
                                        .getSelectedItem();

                        if (carteSelectionnee == null) {
                                return;
                        }

                        Alert confirmation = new Alert(
                                        Alert.AlertType.CONFIRMATION);

                        confirmation.setTitle(
                                        "Suppression");

                        confirmation.setHeaderText(
                                        "Supprimer la carte ?");

                        confirmation.setContentText(
                                        carteSelectionnee.getNom());

                        confirmation.showAndWait()
                                        .ifPresent(reponse -> {

                                                if (reponse == ButtonType.OK) {

                                                        deck.getCartes().remove(
                                                                        carteSelectionnee);

                                                        listeCartes.getItems().remove(
                                                                        carteSelectionnee);

                                                        cardService.sauvegarderDeck(
                                                                        deck);
                                                }
                                        });
                });

                /*
                 * Sauvegarder la carte sélectionnée.
                 */
                boutonSauvegarder.setOnAction(event -> {

                        Card carteSelectionnee = listeCartes
                                        .getSelectionModel()
                                        .getSelectedItem();

                        if (carteSelectionnee == null) {
                                return;
                        }

                        carteSelectionnee.setNom(
                                        champNom.getText());

                        try {

                                carteSelectionnee.setNiveau(
                                                Integer.parseInt(
                                                                champNiveau.getText()));

                        } catch (NumberFormatException e) {

                                Alert erreur = new Alert(
                                                Alert.AlertType.ERROR);

                                erreur.setTitle(
                                                "Niveau invalide");

                                erreur.setHeaderText(
                                                "Le niveau doit être un nombre.");

                                erreur.showAndWait();

                                return;
                        }

                        carteSelectionnee.setDescription(
                                        champDescription.getText());

                        if (carteSelectionnee instanceof SpellCard sort) {

                                formulaireSort.sauvegarderDans(sort);
                        }

                        if (carteSelectionnee instanceof AbilityCard capacite) {

                                formulaireCapacite.sauvegarderDans(
                                                capacite);
                        }

                        if (carteSelectionnee instanceof ItemCard objet) {

                                formulaireObjet.sauvegarderDans(
                                                objet);
                        }

                        cardService.sauvegarderDeck(
                                        deck);

                        listeCartes.refresh();

                        apercuCarte.afficherCarte(
                                        carteSelectionnee);
                });

                boutonAjouterImpression.setOnAction(event -> {

                        Card carteSelectionnee = listeCartes
                                        .getSelectionModel()
                                        .getSelectedItem();

                        if (carteSelectionnee == null) {
                                return;
                        }

                        printDeck.ajouterCarte(
                                        carteSelectionnee);

                        listeImpression.getItems().setAll(
                                        printDeck.getEntrees());

                        listeImpression.refresh();

                        actualiserCompteurDeckImpression.run();
                });

                /*
                 * Ajoute un exemplaire à l'entrée sélectionnée dans le deck.
                 */
                boutonIncrementerImpression.setOnAction(event -> {

                        PrintDeckEntry entreeSelectionnee = listeImpression
                                        .getSelectionModel()
                                        .getSelectedItem();

                        if (entreeSelectionnee == null) {
                                return;
                        }

                        printDeck.ajouterCarte(
                                        entreeSelectionnee.getCarte());

                        listeImpression.getItems().setAll(
                                        printDeck.getEntrees());

                        listeImpression.getSelectionModel().select(
                                        entreeSelectionnee);

                        listeImpression.refresh();

                        actualiserCompteurDeckImpression.run();
                });

                /*
                 * Retire un exemplaire et supprime l'entrée à quantité zéro.
                 */
                boutonDecrementerImpression.setOnAction(event -> {

                        PrintDeckEntry entreeSelectionnee = listeImpression
                                        .getSelectionModel()
                                        .getSelectedItem();

                        if (entreeSelectionnee == null) {
                                return;
                        }

                        printDeck.retirerCarte(
                                        entreeSelectionnee.getCarte());

                        listeImpression.getItems().setAll(
                                        printDeck.getEntrees());

                        if (printDeck.getEntrees().contains(entreeSelectionnee)) {
                                listeImpression.getSelectionModel().select(
                                                entreeSelectionnee);
                        }

                        listeImpression.refresh();

                        actualiserCompteurDeckImpression.run();
                });

                /*
                 * Supprime toute l'entrée, quelle que soit sa quantité.
                 */
                boutonRetirerImpression.setOnAction(event -> {

                        PrintDeckEntry entreeSelectionnee = listeImpression
                                        .getSelectionModel()
                                        .getSelectedItem();

                        if (entreeSelectionnee == null) {
                                return;
                        }

                        printDeck.supprimerEntree(
                                        entreeSelectionnee);

                        listeImpression.getItems().setAll(
                                        printDeck.getEntrees());

                        listeImpression.refresh();

                        actualiserCompteurDeckImpression.run();
                });

                /*
                 * Choisit le fichier de sortie puis délègue toute la génération
                 * du document à PdfExporter.
                 */
                boutonExporterPdf.setOnAction(event -> {

                        FileChooser selecteurFichier = new FileChooser();

                        selecteurFichier.setTitle(
                                        "Exporter le deck en PDF");

                        selecteurFichier.setInitialFileName(
                                        "deck-impression-"
                                                        + LocalDateTime.now()
                                                                        .format(
                                                                                        DateTimeFormatter.ofPattern(
                                                                                                        "yyyy-MM-dd_HH-mm-ss"))
                                                        + ".pdf");

                        selecteurFichier.getExtensionFilters().add(
                                        new FileChooser.ExtensionFilter(
                                                        "Document PDF",
                                                        "*.pdf"));

                        java.io.File fichierSortie = selecteurFichier
                                        .showSaveDialog(stage);

                        if (fichierSortie == null) {
                                return;
                        }

                        try {
                                pdfExporter.exporter(
                                                printDeck,
                                                fichierSortie.toPath());

                                Alert confirmation = new Alert(
                                                Alert.AlertType.INFORMATION);

                                confirmation.setTitle("Export PDF");
                                confirmation.setHeaderText(
                                                "Export terminé");
                                confirmation.setContentText(
                                                fichierSortie.getAbsolutePath());
                                confirmation.showAndWait();

                        } catch (Exception exception) {

                                Alert erreur = new Alert(
                                                Alert.AlertType.ERROR);

                                erreur.setTitle("Export PDF");
                                erreur.setHeaderText(
                                                "Impossible de générer le PDF");
                                erreur.setContentText(
                                                exception.getMessage());
                                erreur.showAndWait();
                        }
                });

                /*
                 * Génère le gabarit utilisé pour contrôler l'alignement
                 * physique des impressions recto-verso.
                 */
                boutonCalibrationRectoVerso.setOnAction(event -> {

                        FileChooser selecteurFichier = new FileChooser();

                        selecteurFichier.setTitle(
                                        "Exporter la calibration recto-verso");

                        selecteurFichier.setInitialFileName(
                                        "calibration-recto-verso.pdf");

                        selecteurFichier.getExtensionFilters().add(
                                        new FileChooser.ExtensionFilter(
                                                        "Document PDF",
                                                        "*.pdf"));

                        java.io.File fichierSortie = selecteurFichier
                                        .showSaveDialog(stage);

                        if (fichierSortie == null) {
                                return;
                        }

                        try {
                                pdfExporter.exporterCalibration(
                                                fichierSortie.toPath());

                                Alert confirmation = new Alert(
                                                Alert.AlertType.INFORMATION);

                                confirmation.setTitle("Export PDF");
                                confirmation.setHeaderText(
                                                "Export terminé");
                                confirmation.setContentText(
                                                fichierSortie.getAbsolutePath());
                                confirmation.showAndWait();

                        } catch (Exception exception) {

                                Alert erreur = new Alert(
                                                Alert.AlertType.ERROR);

                                erreur.setTitle("Export PDF");
                                erreur.setHeaderText(
                                                "Impossible de générer le PDF");
                                erreur.setContentText(
                                                exception.getMessage());
                                erreur.showAndWait();
                        }
                });

                /*
                 * Barre d'actions.
                 */
                HBox actionsGauche = new HBox(
                                10,
                                boutonAjouter,
                                boutonSupprimer);

                BorderPane barreActions = new BorderPane();

                barreActions.setLeft(
                                actionsGauche);

                barreActions.setPadding(
                                new Insets(10));

                /*
                 * Mise en page de la fenêtre.
                 */
                ScrollPane scrollFormulaire = new ScrollPane(formulaire);

                scrollFormulaire.setFitToWidth(true);
                scrollFormulaire.setPrefWidth(500);

                Label titreDeckImpression = new Label("Deck d'impression");

                listeImpression.setCellFactory(param -> new ListCell<>() {

                        @Override
                        protected void updateItem(
                                        PrintDeckEntry entree,
                                        boolean empty) {

                                super.updateItem(entree, empty);

                                setText(empty || entree == null
                                                ? null
                                                : entree.getCarte().getNom()
                                                                + " x"
                                                                + entree.getQuantite());
                        }
                });

                HBox actionsDeckImpression = new HBox(
                                10,
                                boutonIncrementerImpression,
                                boutonDecrementerImpression,
                                boutonRetirerImpression);

                HBox actionsExportPdf = new HBox(
                                10,
                                boutonExporterPdf,
                                boutonCalibrationRectoVerso,
                                compteurDeckImpression);

                actionsExportPdf.setAlignment(Pos.CENTER_LEFT);

                /*
                 * En-tête du deck : titre à gauche et actions à droite.
                 */
                BorderPane enteteDeckImpression = new BorderPane();

                enteteDeckImpression.setLeft(
                                titreDeckImpression);

                enteteDeckImpression.setRight(
                                actionsDeckImpression);

                /*
                 * Colonne du deck d'impression, placée à droite de l'aperçu.
                 */
                VBox panneauDeckImpression = new VBox(
                                10,
                                enteteDeckImpression,
                                listeImpression,
                                actionsExportPdf);

                panneauDeckImpression.setPrefWidth(380);
                panneauDeckImpression.setMinWidth(380);

                /*
                 * Actions liées à la carte affichée dans l'aperçu.
                 */
                HBox actionsApercu = new HBox(
                                10,
                                boutonSauvegarder,
                                boutonAjouterImpression);

                HBox choixFaceApercu = new HBox(
                                10,
                                boutonRecto,
                                boutonVerso);

                StackPane conteneurApercu = new StackPane(
                                apercuCarte,
                                apercuVerso);

                /*
                 * Le recto est visible au démarrage. Le basculement ne modifie
                 * que la vue affichée et n'altère aucune donnée de la carte.
                 */
                apercuVerso.setVisible(false);

                boutonRecto.setOnAction(event -> {
                        apercuCarte.setVisible(true);
                        apercuVerso.setVisible(false);
                });

                boutonVerso.setOnAction(event -> {
                        apercuCarte.setVisible(false);
                        apercuVerso.setVisible(true);
                });

                VBox panneauApercu = new VBox(
                                10,
                                choixFaceApercu,
                                conteneurApercu,
                                actionsApercu);

                HBox panneauDroit = new HBox(
                                10,
                                panneauApercu,
                                panneauDeckImpression);

                panneauDroit.setPadding(new Insets(10));
                panneauDroit.setMinWidth(760);

                BorderPane root = new BorderPane();

                root.setLeft(listeCartes);
                root.setCenter(scrollFormulaire);
                root.setRight(panneauDroit);
                root.setBottom(barreActions);

                listeCartes.setPrefWidth(250);
                listeImpression.setPrefWidth(380);
                listeImpression.setMinWidth(380);
                listeImpression.setPrefHeight(180);

                Scene scene = new Scene(root, 1450, 750);

                stage.getIcons().add(
                                new Image(
                                                getClass().getResourceAsStream(
                                                                "/assets/icons/icone_application.png")));

                stage.setTitle(
                                "Pathfinder Card Studio");

                stage.setScene(scene);
                stage.setMinWidth(1200);
                stage.setMinHeight(700);
                stage.show();
        }

        public static void main(String[] args) {
                launch(args);
        }
}
