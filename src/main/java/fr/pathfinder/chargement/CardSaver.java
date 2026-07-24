package fr.pathfinder.chargement;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.OutputStreamWriter;
import java.io.Writer;

import fr.pathfinder.carte.*;

/**
 * Sérialise le catalogue de cartes dans le fichier JSON de l'application.
 */
public class CardSaver {

    /**
     * Convertit les cartes en données sérialisables et écrit le fichier JSON.
     *
     * @param deck catalogue à sauvegarder
     */
    public void sauvegarder(Deck deck) {

        try {

            CardDatabase database = new CardDatabase();

            database.cards = new ArrayList<>();

            for (Card carte : deck.getCartes()) {

                CardData data = new CardData();

                data.nom = carte.getNom();

                data.niveau = carte.getNiveau();

                data.description = carte.getDescription();

                if (carte instanceof SpellCard) {

                    SpellCard sort = (SpellCard) carte;

                    data.type = "SORT";

                    data.ecole = sort.getEcole();

                    data.portee = sort.getPortee();

                    data.duree = sort.getDuree();

                    data.traits = sort.getTraits();

                    if (sort.getCoutIncantation() != null) {

                        data.coutIncantation = sort.getCoutIncantation().name();

                    }

                    if (sort.getDefense() != null) {

                        data.defense = sort.getDefense().name();

                    }

                    if (sort.getTraditions() != null
                            && !sort.getTraditions().isEmpty()) {

                        data.traditions = sort.getTraditions()
                                .stream()
                                .map(Enum::name)
                                .toList();

                    }

                } else if (carte instanceof ItemCard) {

                    ItemCard objet = (ItemCard) carte;

                    data.type = "OBJET";

                    data.emplacement = objet.getEmplacement();

                    data.prixObjet = objet.getPrix();

                    data.traitsObjet = objet.getTraits();

                    if (objet.getActivation() != null) {

                        data.activationObjet = objet.getActivation().name();
                    }
                } else if (carte instanceof AbilityCard) {

                    AbilityCard capacite = (AbilityCard) carte;

                    data.type = "CAPACITE";

                    /*
                     * Ancien champ conservé temporairement
                     * pour compatibilité avec les anciennes données.
                     */
                    data.actions = capacite.getNombreActions();

                    data.traitsCapacite = capacite.getTraits();
                    data.prerequis = capacite.getPrerequis();
                    data.declencheur = capacite.getDeclencheur();

                    if (capacite.getCoutAction() != null) {
                        data.coutAction = capacite.getCoutAction().name();
                    }
                }

                database.cards.add(data);

            }

            ObjectMapper mapper = new ObjectMapper();

            mapper.enable(
                    SerializationFeature.INDENT_OUTPUT);

            try (Writer writer = new OutputStreamWriter(
                    new FileOutputStream("src/main/resources/cards.json"),
                    StandardCharsets.UTF_8)) {

                mapper.writeValue(writer, database);

            }
            System.out.println(
                    "Sauvegarde effectuée.");

        } catch (Exception e) {

            System.out.println(
                    "Erreur sauvegarde : "
                            + e.getMessage());

        }

    }

}
