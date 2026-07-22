package fr.pathfinder;

import fr.pathfinder.carte.AbilityCard;
import fr.pathfinder.carte.ActionCost;
import fr.pathfinder.carte.Card;
import fr.pathfinder.carte.Deck;
import fr.pathfinder.carte.DefenseType;
import fr.pathfinder.carte.ItemCard;
import fr.pathfinder.carte.SpellCard;
import fr.pathfinder.carte.Tradition;
import fr.pathfinder.service.CardService;
import fr.pathfinder.affichage.javafx.ItemForm;
import fr.pathfinder.affichage.javafx.SpellForm;
import fr.pathfinder.affichage.javafx.AbilityForm;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PathfinderCardStudioApp extends Application {

        @Override
        public void start(Stage stage) {

                CardService cardService = new CardService();
                Deck deck = cardService.chargerDeck();

                /*
                 * Liste des cartes.
                 */
                ListView<Card> listeCartes = new ListView<>();

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

                /*
                 * Boutons.
                 */
                Button boutonAjouter = new Button("Ajouter");

                Button boutonSupprimer = new Button("Supprimer");

                boutonSupprimer.setDisable(true);

                Button boutonSauvegarder = new Button("Sauvegarder");

                boutonSauvegarder.setDisable(true);

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

                formulaire.setPrefWidth(600);

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

                                                                apercuCarte.afficherCarte("",0,"","","",null,"");
                                                                
                                                                boutonSauvegarder.setDisable(true);
                                                                boutonSupprimer.setDisable(true);

                                                                
                                                                return;
                                                        }

                                                        champNom.setText(
                                                                        nouvelleCarte.getNom());

                                                        champNiveau.setText(
                                                                        String.valueOf(
                                                                                        nouvelleCarte.getNiveau()));

                                                        champDescription.setText(
                                                                        nouvelleCarte.getDescription());

                                                        String tradition = "";
                                                        String traits = "";
                                                        String details = "";
                                                        ActionCost coutAction = ActionCost.AUCUNE;

                                                        if (nouvelleCarte instanceof SpellCard sort) {

                                                                if (!sort.getTraditions().isEmpty()) {

                                                                        tradition = sort.getTraditions()
                                                                                        .get(0)
                                                                                        .toString();
                                                                }

                                                                traits = sort.getTraits();
                                                                coutAction = sort.getCoutIncantation();

                                                                details = "Portée : "
                                                                                + valeurOuVide(sort.getPortee())
                                                                                + "\n"
                                                                                + "Durée : "
                                                                                + valeurOuVide(sort.getDuree())
                                                                                + "\n"
                                                                                + "Défense : "
                                                                                + (sort.getDefense() == null
                                                                                                ? ""
                                                                                                : sort.getDefense()
                                                                                                                .toString());

                                                        } else if (nouvelleCarte instanceof AbilityCard capacite) {

                                                                traits = capacite.getTraits();
                                                                coutAction = capacite.getCoutAction();

                                                                details = "Prérequis : "
                                                                                + valeurOuVide(capacite.getPrerequis())
                                                                                + "\n"
                                                                                + "Déclencheur : "
                                                                                + valeurOuVide(capacite
                                                                                                .getDeclencheur());

                                                        } else if (nouvelleCarte instanceof ItemCard objet) {

                                                                traits = objet.getTraits();
                                                                coutAction = objet.getActivation();

                                                                details = "Prix : "
                                                                                + valeurOuVide(objet.getPrix())
                                                                                + "\n"
                                                                                + "Emplacement : "
                                                                                + valeurOuVide(objet.getEmplacement());
                                                        }
                                                        apercuCarte.afficherFondPour(
                                                                        nouvelleCarte);

                                                        apercuCarte.afficherCarte(
                                                                        nouvelleCarte.getNom(),
                                                                        nouvelleCarte.getNiveau(),
                                                                        nouvelleCarte.getDescription(),
                                                                        tradition,
                                                                        traits,
                                                                        coutAction,
                                                                        details);

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
                });

                /*
                 * Barre d'actions.
                 */
                HBox actionsGauche = new HBox(
                                10,
                                boutonAjouter,
                                boutonSupprimer);

                HBox actionsDroite = new HBox(
                                boutonSauvegarder);

                actionsDroite.setAlignment(
                                Pos.CENTER_RIGHT);

                BorderPane barreActions = new BorderPane();

                barreActions.setLeft(
                                actionsGauche);

                barreActions.setRight(
                                actionsDroite);

                barreActions.setPadding(
                                new Insets(10));

                /*
                 * Mise en page de la fenêtre.
                 */
                ScrollPane scrollFormulaire = new ScrollPane(formulaire);

                scrollFormulaire.setFitToWidth(true);

                BorderPane root = new BorderPane();

                root.setLeft(listeCartes);
                root.setCenter(scrollFormulaire);
                root.setRight(apercuCarte);
                root.setBottom(barreActions);

                BorderPane.setMargin(
                                apercuCarte,
                                new Insets(20));

                listeCartes.setPrefWidth(300);

                Scene scene = new Scene(root, 1000, 600);

                stage.setTitle(
                                "Pathfinder Card Studio");

                stage.setScene(scene);
                stage.show();
        }
        
        private String valeurOuVide(String valeur) {
                return valeur == null ? "" : valeur;
        }

        public static void main(String[] args) {
                launch(args);
        }
}