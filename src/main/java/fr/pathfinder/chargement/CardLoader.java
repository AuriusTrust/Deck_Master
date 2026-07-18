package fr.pathfinder.chargement;

import java.io.InputStream;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.pathfinder.carte.AbilityCard;
import fr.pathfinder.carte.ActionCost;
import fr.pathfinder.carte.Deck;
import fr.pathfinder.carte.DefenseType;
import fr.pathfinder.carte.ItemCard;
import fr.pathfinder.carte.SpellCard;
import fr.pathfinder.carte.Tradition;


public class CardLoader {


    public Deck chargerCartes() {


        Deck deck = new Deck();


        try {


            ObjectMapper mapper = new ObjectMapper();


            InputStream fichier =
                getClass()
                .getClassLoader()
                .getResourceAsStream("cards.json");

            if (fichier == null) {
                System.out.println("cards.json introuvable !");
                return deck;
            }

            CardDatabase database =
                mapper.readValue(
                    fichier,
                    CardDatabase.class
                );


            for(CardData data : database.cards) {

                if (data.type == null) {

                    System.out.println("Carte ignorée : type manquant pour " + data.nom);

                    continue;

                }

                switch (data.type) {

                    case "SORT":

                        SpellCard sort = new SpellCard(
                                data.nom,
                                data.niveau,
                                data.description,
                                data.ecole);

                        sort.setPortee(
                                data.portee);

                        sort.setDuree(
                                data.duree);

                        if (data.coutIncantation != null) {

                            sort.setCoutIncantation(
                                    ActionCost.valueOf(
                                            data.coutIncantation));
                        }

                        if (data.defense != null) {

                            sort.setDefense(
                                    DefenseType.valueOf(
                                            data.defense));
                        }

                        if (data.traditions != null) {

                            ArrayList<Tradition> traditions = new ArrayList<>();

                            for (String traditionTexte : data.traditions) {

                                traditions.add(
                                        Tradition.valueOf(
                                                traditionTexte));
                            }

                            sort.setTraditions(
                                    traditions);
                        }

                        deck.ajouterCarte(sort);

                        break;


                    case "OBJET":

                        deck.ajouterCarte(
                            new ItemCard(
                                data.nom,
                                data.niveau,
                                data.description,
                                data.emplacement
                            )
                        );

                        break;


                    case "CAPACITE":

                        deck.ajouterCarte(
                            new AbilityCard(
                                data.nom,
                                data.niveau,
                                data.description,
                                data.actions
                            )
                        );

                        break;

                }

            }


        } catch(Exception e) {

            System.out.println(
                "Erreur chargement cartes : "
                + e.getMessage()
            );

        }


        return deck;
    }
}