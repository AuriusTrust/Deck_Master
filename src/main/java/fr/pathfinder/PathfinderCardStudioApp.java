package fr.pathfinder;

import fr.pathfinder.carte.Card;
import fr.pathfinder.carte.Deck;
import fr.pathfinder.service.CardService;

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

        Button boutonSauvegarder = new Button("Sauvegarder");

        boutonSauvegarder.setDisable(true);

        VBox formulaire = new VBox(
                10,
                titreFormulaire,
                labelNom,
                champNom,
                labelNiveau,
                champNiveau,
                labelDescription,
                champDescription,
                boutonSauvegarder);

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

                                return;
                            }

                            champNom.setText(
                                    nouvelleCarte.getNom());

                            champNiveau.setText(
                                    String.valueOf(
                                            nouvelleCarte.getNiveau()));

                            champDescription.setText(
                                    nouvelleCarte.getDescription());

                            boutonSauvegarder.setDisable(false);
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

            cardService.sauvegarderDeck(deck);

            listeCartes.refresh();

            System.out.println(
                    "Carte sauvegardée.");
        });

        BorderPane root = new BorderPane();

        root.setLeft(listeCartes);
        root.setCenter(formulaire);

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