package fr.pathfinder.affichage.javafx;

import fr.pathfinder.carte.ActionCost;
import fr.pathfinder.carte.DefenseType;
import fr.pathfinder.carte.SpellCard;
import fr.pathfinder.carte.Tradition;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class SpellForm extends VBox {

    private final ComboBox<Tradition> champTradition;
    private final TextField champTraits;
    private final ComboBox<ActionCost> champIncantation;
    private final ComboBox<DefenseType> champDefense;
    private final TextField champPortee;
    private final TextField champDuree;

    public SpellForm() {

        super(10);

        Label labelTradition =
                new Label("Tradition");

        champTradition =
                new ComboBox<>();

        champTradition.getItems().addAll(
                Tradition.values());

        Label labelTraits =
                new Label("Traits");

        champTraits =
                new TextField();

        champTraits.setPromptText(
                "Exemple : Feu, Manipulation");

        Label labelIncantation =
                new Label("Temps d'incantation");

        champIncantation =
                new ComboBox<>();

        champIncantation.getItems().addAll(
                ActionCost.values());

        Label labelDefense =
                new Label("Défense");

        champDefense =
                new ComboBox<>();

        champDefense.getItems().addAll(
                DefenseType.values());

        Label labelPortee =
                new Label("Portée");

        champPortee =
                new TextField();

        champPortee.setPromptText(
                "Exemple : 30 mètres");

        Label labelDuree =
                new Label("Durée");

        champDuree =
                new TextField();

        champDuree.setPromptText(
                "Exemple : instantanée");

        getChildren().addAll(
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

        masquer();
    }

    public void afficher(SpellCard sort) {

        champTraits.setText(
                valeurOuVide(sort.getTraits()));

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
                valeurOuVide(sort.getPortee()));

        champDuree.setText(
                valeurOuVide(sort.getDuree()));

        setVisible(true);
        setManaged(true);
    }

    public void sauvegarderDans(
            SpellCard sort) {

        sort.getTraditions().clear();

        if (champTradition.getValue() != null) {

            sort.getTraditions().add(
                    champTradition.getValue());
        }

        sort.setTraits(
                champTraits.getText());

        sort.setCoutIncantation(
                champIncantation.getValue() == null
                        ? ActionCost.AUCUNE
                        : champIncantation.getValue());

        sort.setDefense(
                champDefense.getValue() == null
                        ? DefenseType.AUCUNE
                        : champDefense.getValue());

        sort.setPortee(
                champPortee.getText());

        sort.setDuree(
                champDuree.getText());
    }

    public void masquer() {

        vider();

        setVisible(false);
        setManaged(false);
    }

    private void vider() {

        champTradition.setValue(null);
        champTraits.clear();
        champIncantation.setValue(null);
        champDefense.setValue(null);
        champPortee.clear();
        champDuree.clear();
    }

    private String valeurOuVide(
            String valeur) {

        return valeur == null
                ? ""
                : valeur;
    }
}