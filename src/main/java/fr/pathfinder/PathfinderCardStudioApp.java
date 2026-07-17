package fr.pathfinder;

import fr.pathfinder.carte.Card;
import fr.pathfinder.carte.Deck;
import fr.pathfinder.service.CardService;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class PathfinderCardStudioApp extends Application {

    @Override
    public void start(Stage stage) {

        CardService cardService =
                new CardService();

        Deck deck =
                cardService.chargerDeck();

        ListView<Card> listeCartes =
                new ListView<>();

        TextArea detailsCarte =
                new TextArea();

        detailsCarte.setEditable(false);

        for (Card carte : deck.getCartes()) {

            listeCartes.getItems().add(carte);

        }

        listeCartes.setCellFactory(param ->
                new javafx.scene.control.ListCell<>() {

                    @Override
                    protected void updateItem(
                            Card carte,
                            boolean empty) {

                        super.updateItem(
                                carte,
                                empty);

                        if (empty || carte == null) {

                            setText(null);

                        } else {

                            setText(carte.getNom());

                        }
                    }
                });

        listeCartes
                .getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        (observable,
                         ancienneCarte,
                         nouvelleCarte) -> {

                            if (nouvelleCarte != null) {

                                detailsCarte.setText(
                                        nouvelleCarte
                                                .afficherDetails());

                            }

                        });

        BorderPane root =
                new BorderPane();

        root.setLeft(listeCartes);
        root.setCenter(detailsCarte);

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