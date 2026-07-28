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
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
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
import javafx.scene.transform.Scale;

/**
 * Exporte les rectos et versos du deck d'impression dans un document PDF A4.
 */
public class PdfExporter {

    private static final int COLONNES = 3;
    private static final int LIGNES = 3;
    private static final int CARTES_PAR_PAGE = COLONNES * LIGNES;

    private static final float POINTS_PAR_MM = 72f / 25.4f;
    private static final float LARGEUR_CARTE_MM = 63.5f;
    private static final float HAUTEUR_CARTE_MM = 88.9f;
    private static final float ESPACEMENT_MM = 2f;

    private static final double LARGEUR_APERCU = 350;
    private static final double HAUTEUR_APERCU = 490;
    private static final double FACTEUR_ECHELLE_CAPTURE =
            750d / LARGEUR_APERCU;

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

    /**
     * Génère un gabarit recto-verso permettant de contrôler l'orientation,
     * les dimensions et l'alignement physique de la grille d'impression.
     *
     * @param sortie fichier PDF de calibration à générer
     * @throws IOException si le fichier ne peut pas être écrit
     */
    public void exporterCalibration(
            Path sortie) throws IOException {

        if (sortie == null) {
            throw new IllegalArgumentException(
                    "Le chemin de sortie est obligatoire.");
        }

        Path cheminAbsolu = sortie.toAbsolutePath();
        Path dossierParent = cheminAbsolu.getParent();

        if (dossierParent != null) {
            Files.createDirectories(dossierParent);
        }

        try (PDDocument document = new PDDocument()) {
            ajouterPageCalibration(document, false);
            ajouterPageCalibration(document, true);
            document.save(cheminAbsolu.toFile());
        }
    }

    private void ajouterPageCalibration(
            PDDocument document,
            boolean verso) throws IOException {

        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        float largeurPage = page.getMediaBox().getWidth();
        float hauteurPage = page.getMediaBox().getHeight();

        float largeurCarte =
                LARGEUR_CARTE_MM * POINTS_PAR_MM;
        float hauteurCarte =
                HAUTEUR_CARTE_MM * POINTS_PAR_MM;
        float espacement =
                ESPACEMENT_MM * POINTS_PAR_MM;

        float largeurGrille =
                COLONNES * largeurCarte
                        + (COLONNES - 1) * espacement;
        float hauteurGrille =
                LIGNES * hauteurCarte
                        + (LIGNES - 1) * espacement;

        float origineX =
                (largeurPage - largeurGrille) / 2;
        float origineY =
                (hauteurPage - hauteurGrille) / 2;

        PDType1Font policeNormale = new PDType1Font(
                Standard14Fonts.FontName.HELVETICA);
        PDType1Font policeGrasse = new PDType1Font(
                Standard14Fonts.FontName.HELVETICA_BOLD);

        try (PDPageContentStream contenu = new PDPageContentStream(
                document,
                page)) {

            ecrireTexteCalibration(
                    contenu,
                    policeGrasse,
                    12,
                    origineX,
                    hauteurPage - 18,
                    verso
                            ? "CALIBRATION VERSO - BORD LONG"
                            : "CALIBRATION RECTO");

            for (int position = 0;
                    position < CARTES_PAR_PAGE;
                    position++) {

                int numeroCarte = position + 1;
                int colonne = position % COLONNES;
                int ligne = position / COLONNES;

                if (verso) {
                    colonne = calculerColonneVerso(colonne);
                }

                float x = origineX
                        + colonne * (largeurCarte + espacement);
                float y = origineY
                        + (LIGNES - 1 - ligne)
                        * (hauteurCarte + espacement);

                dessinerCarteCalibration(
                        contenu,
                        policeNormale,
                        policeGrasse,
                        numeroCarte,
                        x,
                        y,
                        largeurCarte,
                        hauteurCarte);
            }
        }
    }

    private void dessinerCarteCalibration(
            PDPageContentStream contenu,
            PDType1Font policeNormale,
            PDType1Font policeGrasse,
            int numeroCarte,
            float x,
            float y,
            float largeurCarte,
            float hauteurCarte) throws IOException {

        contenu.setLineWidth(2.5f);
        contenu.addRect(
                x,
                y,
                largeurCarte,
                hauteurCarte);
        contenu.stroke();

        ecrireTexteCalibration(
                contenu,
                policeGrasse,
                24,
                x + 10,
                y + hauteurCarte - 30,
                Integer.toString(numeroCarte));

        float centreX = x + largeurCarte / 2;
        float centreY = y + hauteurCarte / 2;
        float tailleRepere = 7;

        contenu.setLineWidth(1.5f);
        contenu.moveTo(
                centreX - tailleRepere,
                centreY);
        contenu.lineTo(
                centreX + tailleRepere,
                centreY);
        contenu.moveTo(
                centreX,
                centreY - tailleRepere);
        contenu.lineTo(
                centreX,
                centreY + tailleRepere);
        contenu.stroke();

        float baseFleche = y + hauteurCarte * 0.58f;
        float sommetFleche = y + hauteurCarte * 0.79f;
        float demiPointe = 13;

        contenu.setLineWidth(4);
        contenu.moveTo(
                centreX,
                baseFleche);
        contenu.lineTo(
                centreX,
                sommetFleche);
        contenu.moveTo(
                centreX,
                sommetFleche);
        contenu.lineTo(
                centreX - demiPointe,
                sommetFleche - demiPointe);
        contenu.moveTo(
                centreX,
                sommetFleche);
        contenu.lineTo(
                centreX + demiPointe,
                sommetFleche - demiPointe);
        contenu.stroke();

        ecrireTexteCalibration(
                contenu,
                policeGrasse,
                11,
                centreX - 15,
                baseFleche - 16,
                "HAUT");

        ecrireTexteCalibration(
                contenu,
                policeNormale,
                8,
                x + 8,
                y + 23,
                String.format(
                        java.util.Locale.ROOT,
                        "X = %.2f pt (%.2f mm)",
                        x,
                        x / POINTS_PAR_MM));
        ecrireTexteCalibration(
                contenu,
                policeNormale,
                8,
                x + 8,
                y + 11,
                String.format(
                        java.util.Locale.ROOT,
                        "Y = %.2f pt (%.2f mm)",
                        y,
                        y / POINTS_PAR_MM));
    }

    private void ecrireTexteCalibration(
            PDPageContentStream contenu,
            PDType1Font police,
            float taille,
            float x,
            float y,
            String texte) throws IOException {

        contenu.beginText();
        contenu.setFont(police, taille);
        contenu.newLineAtOffset(x, y);
        contenu.showText(texte);
        contenu.endText();
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

        float largeurCarte =
                LARGEUR_CARTE_MM * POINTS_PAR_MM;
        float hauteurCarte =
                HAUTEUR_CARTE_MM * POINTS_PAR_MM;
        float espacement =
                ESPACEMENT_MM * POINTS_PAR_MM;

        float largeurGrille =
                COLONNES * largeurCarte
                        + (COLONNES - 1) * espacement;
        float hauteurGrille =
                LIGNES * hauteurCarte
                        + (LIGNES - 1) * espacement;

        float origineX =
                (largeurPage - largeurGrille) / 2;
        float origineY =
                (hauteurPage - hauteurGrille) / 2;

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

                float x = origineX
                        + colonne * (largeurCarte + espacement);

                float y = origineY
                        + (LIGNES - 1 - ligne)
                        * (hauteurCarte + espacement);

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
        parametres.setTransform(new Scale(
                FACTEUR_ECHELLE_CAPTURE,
                FACTEUR_ECHELLE_CAPTURE));

        WritableImage capture = apercu.snapshot(
                parametres,
                null);

        return SwingFXUtils.fromFXImage(
                capture,
                null);
    }
}
