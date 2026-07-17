package fr.pathfinder;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class PathfinderCardStudioApp extends Application {

    @Override
    public void start(Stage stage) {

        Label label =
                new Label("Bienvenue dans Pathfinder Card Studio");

        StackPane root =
                new StackPane(label);

        Scene scene =
                new Scene(root, 800, 600);

        stage.setTitle(
                "Pathfinder Card Studio");

        stage.setScene(scene);

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}