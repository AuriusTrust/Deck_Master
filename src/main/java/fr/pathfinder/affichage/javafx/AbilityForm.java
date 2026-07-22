package fr.pathfinder.affichage.javafx;

import fr.pathfinder.carte.AbilityCard;
import fr.pathfinder.carte.ActionCost;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class AbilityForm extends VBox {

    private final TextField champTraits;
    private final ComboBox<ActionCost> champCoutAction;
    private final TextField champPrerequis;
    private final TextArea champDeclencheur;

    public AbilityForm() {

        super(10);

        Label labelTraits =
                new Label("Traits");

        champTraits =
                new TextField();

        champTraits.setPromptText(
                "Exemple : Guerrier, Attaque");

        Label labelCoutAction =
                new Label("Coût en actions");

        champCoutAction =
                new ComboBox<>();

        champCoutAction.getItems().addAll(
                ActionCost.values());

        Label labelPrerequis =
                new Label("Prérequis");

        champPrerequis =
                new TextField();

        champPrerequis.setPromptText(
                "Laisser vide s'il n'y en a pas");

        Label labelDeclencheur =
                new Label("Déclencheur");

        champDeclencheur =
                new TextArea();

        champDeclencheur.setWrapText(true);
        champDeclencheur.setPrefRowCount(3);

        getChildren().addAll(
                labelTraits,
                champTraits,
                labelCoutAction,
                champCoutAction,
                labelPrerequis,
                champPrerequis,
                labelDeclencheur,
                champDeclencheur);

        masquer();
    }

    public void afficher(
            AbilityCard capacite) {

        champTraits.setText(
                valeurOuVide(
                        capacite.getTraits()));

        champCoutAction.setValue(
                capacite.getCoutAction());

        champPrerequis.setText(
                valeurOuVide(
                        capacite.getPrerequis()));

        champDeclencheur.setText(
                valeurOuVide(
                        capacite.getDeclencheur()));

        setVisible(true);
        setManaged(true);
    }

    public void sauvegarderDans(
            AbilityCard capacite) {

        capacite.setTraits(
                champTraits.getText());

        capacite.setCoutAction(
                champCoutAction.getValue() == null
                        ? ActionCost.AUCUNE
                        : champCoutAction.getValue());

        capacite.setPrerequis(
                champPrerequis.getText());

        capacite.setDeclencheur(
                champDeclencheur.getText());
    }

    public void masquer() {

        vider();

        setVisible(false);
        setManaged(false);
    }

    private void vider() {

        champTraits.clear();
        champCoutAction.setValue(null);
        champPrerequis.clear();
        champDeclencheur.clear();
    }

    private String valeurOuVide(
            String valeur) {

        return valeur == null
                ? ""
                : valeur;
    }
}