package fr.pathfinder.chargement;


import java.io.InputStream;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.pathfinder.carte.*;


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

                switch(data.type) {


                    case "SORT":

                        deck.ajouterCarte(
                            new SpellCard(
                                data.nom,
                                data.niveau,
                                data.description,
                                data.ecole
                            )
                        );

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