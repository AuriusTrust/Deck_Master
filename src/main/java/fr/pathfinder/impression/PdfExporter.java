package fr.pathfinder.impression;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import fr.pathfinder.affichage.javafx.CardBackPreviewPane;
import fr.pathfinder.affichage.javafx.CardPreviewPane;
import fr.pathfinder.carte.Card;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 * Exporte les rectos et versos du deck d'impression dans un document PDF A4.
 */
public class PdfExporter {

    private static final int COLONNES = 3;
    private static final int LIGNES = 3;
    private static final int CARTES_PAR_PAGE = COLONNES * LIGNES;

    private static final float MARGE = 20;
    private static final float ESPACEMENT = 4;
    private static final float RATIO_CARTE = 490f / 350f;

    private static final double LARGEUR_APERCU = 350;
    private static final double HAUTEUR_APERCU = 490;

    /**
     * Génère un PDF recto-verso contenant jusqu'à neuf cartes par page.
     * Chaque page recto est immédiatement suivie de son verso en miroir
     * horizontal pour une impression retournée sur le grand côté.
     *
     * @param printDeck deck et quantités à exporter
     * @param sortie fichier PDF de destination
     * @throws IOException si le fichier ne peut pas être écrit
     */
    public void exporter(
            PrintDeck printDeck,
            Path sortie) throws IOException {

        if (printDeck == null || printDeck.estVide()) {
            throw new IllegalArgumentException(
                    "Le deck d'impression est vide.");
        }

        if (sortie == null) {
            throw new IllegalArgumentException(
                    "Le chemin de sortie est obligatoire.");
        }

        if (!Platform.isFxApplicationThread()) {
            throw new IllegalStateException(
                    "L'export graphique doit être déclenché depuis JavaFX.");
        }

        Path cheminAbsolu = sortie.toAbsolutePath();
        Path dossierParent = cheminAbsolu.getParent();

        if (dossierParent != null) {
            Files.createDirectories(dossierParent);
        }

        List<Card> cartesAImprimer = developperQuantites(printDeck);

        try (PDDocument document = new PDDocument()) {

            for (int debutPage = 0;
                    debutPage < cartesAImprimer.size();
                    debutPage += CARTES_PAR_PAGE) {

                ajouterPageRecto(
                        document,
                        cartesAImprimer,
                        debutPage);

                ajouterPageVerso(
                        document,
                        cartesAImprimer,
                        debutPage);
            }

            document.save(cheminAbsolu.toFile());
        }
    }

    private List<Card> developperQuantites(PrintDeck printDeck) {

        List<Card> cartes = new ArrayList<>(printDeck.taille());

        for (PrintDeckEntry entree : printDeck.getEntrees()) {
            for (int exemplaire = 0;
                    exemplaire < entree.getQuantite();
                    exemplaire++) {

                cartes.add(entree.getCarte());
            }
        }

        return cartes;
    }

    private void ajouterPageRecto(
            PDDocument document,
            List<Card> cartes,
            int debutPage) throws IOException {

        ajouterPage(
                document,
                cartes,
                debutPage,
                false);
    }

    private void ajouterPageVerso(
            PDDocument document,
            List<Card> cartes,
            int debutPage) throws IOException {

        ajouterPage(
                document,
                cartes,
                debutPage,
                true);
    }

    private void ajouterPage(
            PDDocument document,
            List<Card> cartes,
            int debutPage,
            boolean verso) throws IOException {

        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        float largeurPage = page.getMediaBox().getWidth();
        float hauteurPage = page.getMediaBox().getHeight();

        float largeurCarte = (
                largeurPage
                        - 2 * MARGE
                        - (COLONNES - 1) * ESPACEMENT)
                / COLONNES;

        float hauteurCarte = largeurCarte * RATIO_CARTE;

        try (PDPageContentStream contenu = new PDPageContentStream(
                document,
                page)) {

            int finPage = Math.min(
                    debutPage + CARTES_PAR_PAGE,
                    cartes.size());

            for (int index = debutPage;
                    index < finPage;
                    index++) {

                int positionPage = index - debutPage;
                int colonne = positionPage % COLONNES;
                int ligne = positionPage / COLONNES;

                if (verso) {
                    colonne = calculerColonneVerso(colonne);
                }

                float x = MARGE
                        + colonne * (largeurCarte + ESPACEMENT);

                float y = hauteurPage
                        - MARGE
                        - hauteurCarte
                        - ligne * (hauteurCarte + ESPACEMENT);

                BufferedImage imageCarte = verso
                        ? capturerVerso(cartes.get(index))
                        : capturerRecto(cartes.get(index));

                PDImageXObject imagePdf = LosslessFactory.createFromImage(
                        document,
                        imageCarte);

                contenu.drawImage(
                        imagePdf,
                        x,
                        y,
                        largeurCarte,
                        hauteurCarte);
            }
        }
    }

    private int calculerColonneVerso(int colonneRecto) {
        return COLONNES - 1 - colonneRecto;
    }

    private BufferedImage capturerRecto(Card carte) {

        CardPreviewPane apercu = new CardPreviewPane();
        apercu.afficherCarte(carte);

        return capturerApercu(apercu);
    }

    private BufferedImage capturerVerso(Card carte) {

        CardBackPreviewPane apercu = new CardBackPreviewPane();
        apercu.afficherVerso(carte);

        return capturerApercu(apercu);
    }

    private BufferedImage capturerApercu(Pane apercu) {

        /*
         * Les textes des aperçus utilisent des contrôles JavaFX. Leur skin
         * n'est créé correctement que lorsqu'ils appartiennent à une Scene.
         * Cette scène reste entièrement hors écran et sert uniquement à
         * préparer correctement le nœud avant sa capture.
         */
        StackPane racineTemporaire = new StackPane(apercu);
        racineTemporaire.setPrefSize(
                LARGEUR_APERCU,
                HAUTEUR_APERCU);
        racineTemporaire.setMinSize(
                LARGEUR_APERCU,
                HAUTEUR_APERCU);
        racineTemporaire.setMaxSize(
                LARGEUR_APERCU,
                HAUTEUR_APERCU);

        new Scene(
                racineTemporaire,
                LARGEUR_APERCU,
                HAUTEUR_APERCU);

        racineTemporaire.applyCss();
        racineTemporaire.resize(
                LARGEUR_APERCU,
                HAUTEUR_APERCU);
        racineTemporaire.layout();

        apercu.resizeRelocate(
                0,
                0,
                LARGEUR_APERCU,
                HAUTEUR_APERCU);
        apercu.applyCss();
        apercu.layout();

        SnapshotParameters parametres = new SnapshotParameters();
        parametres.setFill(Color.TRANSPARENT);

        WritableImage capture = apercu.snapshot(
                parametres,
                null);

        return SwingFXUtils.fromFXImage(
                capture,
                null);
    }
}
