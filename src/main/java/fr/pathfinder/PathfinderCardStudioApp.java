package fr.pathfinder;

import fr.pathfinder.carte.Card;
import fr.pathfinder.carte.Deck;
import fr.pathfinder.service.CardService;
import fr.pathfinder.carte.AbilityCard;
import fr.pathfinder.carte.ItemCard;
import fr.pathfinder.carte.SpellCard;
import fr.pathfinder.carte.ActionCost;
import fr.pathfinder.carte.DefenseType;
import fr.pathfinder.carte.Tradition;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.ChoiceDialog;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;

public class PathfinderCardStudioApp extends Application {

        @Override
        public void start(Stage stage) {

                CardService cardService = new CardService();
                Deck deck = cardService.chargerDeck();

                /*
                 * Partie gauche :
                 * liste des cartes.
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
                 * Partie droite :
                 * formulaire de la carte sélectionnée.
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
                Label labelTradition = new Label("Tradition");

                Label labelTraits = new Label("Traits");

                TextField champTraits = new TextField();

                ComboBox<Tradition> champTradition = new ComboBox<>();
                champTradition.getItems().addAll(Tradition.values());

                Label labelIncantation = new Label("Temps d'incantation");

                ComboBox<ActionCost> champIncantation = new ComboBox<>();
                champIncantation.getItems().addAll(ActionCost.values());

                Label labelDefense = new Label("Défense");

                ComboBox<DefenseType> champDefense = new ComboBox<>();
                champDefense.getItems().addAll(DefenseType.values());

                Label labelPortee = new Label("Portée");

                TextField champPortee = new TextField();

                Label labelDuree = new Label("Durée");

                TextField champDuree = new TextField();

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

                Button boutonAjouter = new Button("Ajouter");
                Button boutonSupprimer = new Button("Supprimer");
                boutonSupprimer.setDisable(true);
                Button boutonSauvegarder = new Button("Sauvegarder");

                boutonSauvegarder.setDisable(true);

                VBox formulaire = new VBox(
                                10,
                                titreFormulaire,
                                labelNom,
                                champNom,
                                labelNiveau,
                                champNiveau,
                                formulaireSort,
                                labelDescription,
                                champDescription);

                formulaire.setPadding(
                                new Insets(20));

                formulaire.setPrefWidth(600);

                /*
                 * Quand une carte est sélectionnée,
                 * ses informations remplissent le formulaire.
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

                                                                boutonSauvegarder.setDisable(true);
                                                                boutonSupprimer.setDisable(true);

                                                                formulaireSort.setVisible(false);
                                                                formulaireSort.setManaged(false);

                                                                champTradition.setValue(null);
                                                                champTraits.clear();
                                                                champIncantation.setValue(null);
                                                                champDefense.setValue(null);
                                                                champPortee.clear();
                                                                champDuree.clear();
                                                                return;
                                                        }

                                                        champNom.setText(
                                                                        nouvelleCarte.getNom());

                                                        champNiveau.setText(
                                                                        String.valueOf(
                                                                                        nouvelleCarte.getNiveau()));
                                                        
                                                        champDescription.setText(
                                                                        nouvelleCarte.getDescription());
                                                        if (nouvelleCarte instanceof SpellCard sort) {

                                                                formulaireSort.setVisible(true);
                                                                formulaireSort.setManaged(true);

                                                                champTraits.setText(sort.getTraits());

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

                                                        } else {

                                                                formulaireSort.setVisible(false);
                                                                formulaireSort.setManaged(false);

                                                                champTraits.clear();
                                                        }

                                                        boutonSauvegarder.setDisable(false);
                                                        boutonSupprimer.setDisable(false);
                                                });
                boutonAjouter.setOnAction(event -> {

                        ChoiceDialog<String> dialogueType = new ChoiceDialog<>(
                                        "Sort",
                                        "Sort",
                                        "Objet",
                                        "Capacité");

                        dialogueType.setTitle("Ajouter une carte");
                        dialogueType.setHeaderText(
                                        "Choisissez le type de carte");
                        dialogueType.setContentText("Type :");

                        dialogueType.showAndWait().ifPresent(typeChoisi -> {

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

                                deck.ajouterCarte(nouvelleCarte);

                                listeCartes.getItems().add(
                                                nouvelleCarte);

                                listeCartes
                                                .getSelectionModel()
                                                .select(nouvelleCarte);

                                cardService.sauvegarderDeck(deck);
                        });
                });
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

                                                        deck.getCartes()
                                                                        .remove(carteSelectionnee);

                                                        listeCartes.getItems()
                                                                        .remove(carteSelectionnee);

                                                        cardService.sauvegarderDeck(
                                                                        deck);

                                                        System.out.println(
                                                                        "Carte supprimée.");
                                                }
                                        });
                });
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

                                System.out.println(
                                                "Niveau invalide.");

                                return;
                        }

                        carteSelectionnee.setDescription(
                                        champDescription.getText());
                        if (carteSelectionnee instanceof SpellCard sort) {

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

                        cardService.sauvegarderDeck(deck);

                        listeCartes.refresh();

                        System.out.println(
                                        "Carte sauvegardée.");
                });

                HBox actionsGauche = new HBox(
                                10,
                                boutonAjouter,
                                boutonSupprimer);

                HBox actionsDroite = new HBox(
                                boutonSauvegarder);

                actionsDroite.setAlignment(Pos.CENTER_RIGHT);

                BorderPane barreActions = new BorderPane();

                barreActions.setLeft(actionsGauche);
                barreActions.setRight(actionsDroite);
                barreActions.setPadding(new Insets(10));
                BorderPane root = new BorderPane();

                root.setLeft(listeCartes);
                root.setCenter(formulaire);
                root.setBottom(barreActions);

                listeCartes.setPrefWidth(300);

                Scene scene = new Scene(root, 1000, 600);

                stage.setTitle(
                                "Pathfinder Card Studio");

                stage.setScene(scene);
                stage.show();
        }

        public static void main(String[] args) {
                launch(args);
        }
}