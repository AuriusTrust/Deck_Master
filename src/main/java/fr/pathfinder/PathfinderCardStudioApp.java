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

        listeCartes.setCellFactory(param ->
                new ListCell<>() {

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
        Label titreFormulaire =
                new Label("Détails de la carte");

        Label labelNom =
                new Label("Nom");

        TextField champNom =
                new TextField();

        Label labelNiveau =
                new Label("Niveau");

        TextField champNiveau =
                new TextField();

        Label labelDescription =
                new Label("Description");

        TextArea champDescription =
                new TextArea();

        champDescription.setWrapText(true);
        champDescription.setPrefRowCount(10);

        /*
         * Champs spécifiques aux sorts.
         */
        Label labelTradition =
                new Label("Tradition");

        ComboBox<Tradition> champTradition =
                new ComboBox<>();

        champTradition.getItems().addAll(
                Tradition.values());

        Label labelTraits =
                new Label("Traits");

        TextField champTraits =
                new TextField();

        Label labelIncantation =
                new Label("Temps d'incantation");

        ComboBox<ActionCost> champIncantation =
                new ComboBox<>();

        champIncantation.getItems().addAll(
                ActionCost.values());

        Label labelDefense =
                new Label("Défense");

        ComboBox<DefenseType> champDefense =
                new ComboBox<>();

        champDefense.getItems().addAll(
                DefenseType.values());

        Label labelPortee =
                new Label("Portée");

        TextField champPortee =
                new TextField();

        Label labelDuree =
                new Label("Durée");

        TextField champDuree =
                new TextField();

        VBox formulaireSort = new VBox(
                10,
                labelTradition,
                champTradition,
                labelTraits,
                champTraits,
                labelIncantation,
                champIncantation,
                labelDefense,
                champDefense,
                labelPortee,
                champPortee,
                labelDuree,
                champDuree);

        formulaireSort.setVisible(false);
        formulaireSort.setManaged(false);

        /*
         * Champs spécifiques aux dons et capacités.
         */
        Label labelTraitsCapacite =
                new Label("Traits");

        TextField champTraitsCapacite =
                new TextField();

        Label labelCoutAction =
                new Label("Coût en actions");

        ComboBox<ActionCost> champCoutAction =
                new ComboBox<>();

        champCoutAction.getItems().addAll(
                ActionCost.values());

        Label labelPrerequis =
                new Label("Prérequis");

        TextField champPrerequis =
                new TextField();

        Label labelDeclencheur =
                new Label("Déclencheur");

        TextArea champDeclencheur =
                new TextArea();

        champDeclencheur.setWrapText(true);
        champDeclencheur.setPrefRowCount(3);

        VBox formulaireCapacite = new VBox(
                10,
                labelTraitsCapacite,
                champTraitsCapacite,
                labelCoutAction,
                champCoutAction,
                labelPrerequis,
                champPrerequis,
                labelDeclencheur,
                champDeclencheur);

        formulaireCapacite.setVisible(false);
        formulaireCapacite.setManaged(false);

        /*
         * Boutons.
         */
        Button boutonAjouter =
                new Button("Ajouter");

        Button boutonSupprimer =
                new Button("Supprimer");

        boutonSupprimer.setDisable(true);

        Button boutonSauvegarder =
                new Button("Sauvegarder");

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

                                formulaireSort.setVisible(false);
                                formulaireSort.setManaged(false);

                                champTradition.setValue(null);
                                champTraits.clear();
                                champIncantation.setValue(null);
                                champDefense.setValue(null);
                                champPortee.clear();
                                champDuree.clear();

                                formulaireCapacite.setVisible(false);
                                formulaireCapacite.setManaged(false);

                                champTraitsCapacite.clear();
                                champCoutAction.setValue(null);
                                champPrerequis.clear();
                                champDeclencheur.clear();

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

                            if (nouvelleCarte
                                    instanceof SpellCard sort) {

                                formulaireSort.setVisible(true);
                                formulaireSort.setManaged(true);

                                formulaireCapacite.setVisible(false);
                                formulaireCapacite.setManaged(false);

                                champTraits.setText(
                                        sort.getTraits());

                                if (sort.getTraditions().isEmpty()) {

                                    champTradition.setValue(null);

                                } else {

                                    champTradition.setValue(
                                            sort.getTraditions().get(0));
                                }

                                champIncantation.setValue(
                                        sort.getCoutIncantation());

                                champDefense.setValue(
                                        sort.getDefense());

                                champPortee.setText(
                                        sort.getPortee());

                                champDuree.setText(
                                        sort.getDuree());

                            } else if (nouvelleCarte
                                    instanceof AbilityCard capacite) {

                                formulaireSort.setVisible(false);
                                formulaireSort.setManaged(false);

                                formulaireCapacite.setVisible(true);
                                formulaireCapacite.setManaged(true);

                                champTraitsCapacite.setText(
                                        capacite.getTraits());

                                champCoutAction.setValue(
                                        capacite.getCoutAction());

                                champPrerequis.setText(
                                        capacite.getPrerequis());

                                champDeclencheur.setText(
                                        capacite.getDeclencheur());

                            } else {

                                formulaireSort.setVisible(false);
                                formulaireSort.setManaged(false);

                                formulaireCapacite.setVisible(false);
                                formulaireCapacite.setManaged(false);

                                champTraits.clear();
                                champTraitsCapacite.clear();

                                champTradition.setValue(null);
                                champIncantation.setValue(null);
                                champDefense.setValue(null);
                                champCoutAction.setValue(null);

                                champPortee.clear();
                                champDuree.clear();
                                champPrerequis.clear();
                                champDeclencheur.clear();
                            }

                            boutonSauvegarder.setDisable(false);
                            boutonSupprimer.setDisable(false);
                        });

        /*
         * Ajouter une carte.
         */
        boutonAjouter.setOnAction(event -> {

            ChoiceDialog<String> dialogueType =
                    new ChoiceDialog<>(
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

                                nouvelleCarte =
                                        new ItemCard(
                                                "Nouvel objet",
                                                1,
                                                "",
                                                "");

                                break;

                            case "Capacité":

                                nouvelleCarte =
                                        new AbilityCard(
                                                "Nouvelle capacité",
                                                1,
                                                "",
                                                "1");

                                break;

                            case "Sort":
                            default:

                                nouvelleCarte =
                                        new SpellCard(
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

            Card carteSelectionnee =
                    listeCartes
                            .getSelectionModel()
                            .getSelectedItem();

            if (carteSelectionnee == null) {
                return;
            }

            Alert confirmation =
                    new Alert(
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

            Card carteSelectionnee =
                    listeCartes
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

                Alert erreur =
                        new Alert(
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

            if (carteSelectionnee
                    instanceof SpellCard sort) {

                sort.getTraditions().clear();

                sort.setTraits(
                        champTraits.getText());

                if (champTradition.getValue() != null) {

                    sort.getTraditions().add(
                            champTradition.getValue());
                }

                if (champIncantation.getValue() != null) {

                    sort.setCoutIncantation(
                            champIncantation.getValue());
                }

                if (champDefense.getValue() != null) {

                    sort.setDefense(
                            champDefense.getValue());
                }

                sort.setPortee(
                        champPortee.getText());

                sort.setDuree(
                        champDuree.getText());
            }

            if (carteSelectionnee
                    instanceof AbilityCard capacite) {

                capacite.setTraits(
                        champTraitsCapacite.getText());

                if (champCoutAction.getValue() != null) {

                    capacite.setCoutAction(
                            champCoutAction.getValue());
                }

                capacite.setPrerequis(
                        champPrerequis.getText());

                capacite.setDeclencheur(
                        champDeclencheur.getText());
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

        BorderPane barreActions =
                new BorderPane();

        barreActions.setLeft(
                actionsGauche);

        barreActions.setRight(
                actionsDroite);

        barreActions.setPadding(
                new Insets(10));

        /*
         * Mise en page de la fenêtre.
         */
        ScrollPane scrollFormulaire =
                new ScrollPane(formulaire);

        scrollFormulaire.setFitToWidth(true);

        BorderPane root =
                new BorderPane();

        root.setLeft(
                listeCartes);

        root.setCenter(
                scrollFormulaire);

        root.setBottom(
                barreActions);

        listeCartes.setPrefWidth(300);

        Scene scene =
                new Scene(root, 1000, 600);

        stage.setTitle(
                "Pathfinder Card Studio");

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}