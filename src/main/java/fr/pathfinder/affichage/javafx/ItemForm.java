package fr.pathfinder.affichage.javafx;

import fr.pathfinder.carte.ActionCost;
import fr.pathfinder.carte.ItemCard;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class ItemForm extends VBox {

    private final TextField champPrix;
    private final TextField champTraits;
    private final ComboBox<ActionCost> champActivation;
    private final TextField champEmplacement;

    public ItemForm() {

        super(10);

        Label labelPrix =
                new Label("Prix");

        champPrix =
                new TextField();

        champPrix.setPromptText(
                "Exemple : 60 po");

        Label labelTraits =
                new Label("Traits");

        champTraits =
                new TextField();

        champTraits.setPromptText(
                "Exemple : Magique, Feu");

        Label labelActivation =
                new Label("Activation");

        champActivation =
                new ComboBox<>();

        champActivation.getItems().addAll(
                ActionCost.values());

        Label labelEmplacement =
                new Label("Emplacement / Usage");

        champEmplacement =
                new TextField();

        champEmplacement.setPromptText(
                "Exemple : tenu à 1 main");

        getChildren().addAll(
                labelPrix,
                champPrix,
                labelTraits,
                champTraits,
                labelActivation,
                champActivation,
                labelEmplacement,
                champEmplacement);

        masquer();
    }

    public void afficher(ItemCard objet) {

        champPrix.setText(
                valeurOuVide(objet.getPrix()));

        champTraits.setText(
                valeurOuVide(objet.getTraits()));

        champActivation.setValue(
                objet.getActivation());

        champEmplacement.setText(
                valeurOuVide(objet.getEmplacement()));

        setVisible(true);
        setManaged(true);
    }

    public void sauvegarderDans(
            ItemCard objet) {

        objet.setPrix(
                champPrix.getText());

        objet.setTraits(
                champTraits.getText());

        objet.setActivation(
                champActivation.getValue() == null
                        ? ActionCost.AUCUNE
                        : champActivation.getValue());

        objet.setEmplacement(
                champEmplacement.getText());
    }

    public void masquer() {

        vider();

        setVisible(false);
        setManaged(false);
    }

    private void vider() {

        champPrix.clear();
        champTraits.clear();
        champActivation.setValue(null);
        champEmplacement.clear();
    }

    private String valeurOuVide(
            String valeur) {

        return valeur == null
                ? ""
                : valeur;
    }
}