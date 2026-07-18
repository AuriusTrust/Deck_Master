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

public class CardSaver {

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

                    ItemCard item = (ItemCard) carte;

                    data.type = "OBJET";

                    data.emplacement = item.getEmplacement();

                } else if (carte instanceof AbilityCard) {

                    AbilityCard capacite = (AbilityCard) carte;

                    data.type = "CAPACITE";

                    data.actions = capacite.getNombreActions();

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