import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.*;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    public static BorderPane borderPane = new BorderPane();
    public void start (Stage primaryStage){
        primaryStage.setTitle("Laboratoire #7");
        primaryStage.setHeight(520);
        primaryStage.setWidth(520);
        primaryStage.setResizable(true);

        //MENUS

        Menu importer = new Menu("Importer");
        Menu exporter = new Menu("Exporter");

        MenuItem lignes = new MenuItem("Lignes");
        MenuItem regions = new MenuItem("Région");
        MenuItem barres = new MenuItem("Barres");

        MenuItem png = new MenuItem("PNG");
        MenuItem gif = new MenuItem("GIF");

        importer.getItems().addAll(lignes, regions, barres);
        exporter.getItems().addAll(png, gif);

        MenuBar menuBar = new MenuBar(importer, exporter);



        //GRAPHIQUES

        //Lignes
                      //defining the axes
        final CategoryAxis xAxisLC = new CategoryAxis();
        final NumberAxis yAxisLC = new NumberAxis();
        xAxisLC.setLabel("Mois");
        yAxisLC.setLabel("Température");

                   //creating the chart
        final LineChart<String, Number> lineChart =
                new LineChart<String, Number>(xAxisLC, yAxisLC);
        lineChart.setTitle("Températures Moyennes");


        //Region
        final CategoryAxis xAxisAC = new CategoryAxis();
        final NumberAxis yAxisAC = new NumberAxis();
        xAxisAC.setLabel("Mois");
        yAxisAC.setLabel("Température");

        final AreaChart<String,Number> areaChart =
                new AreaChart<String, Number>(xAxisAC, yAxisAC);
        areaChart.setTitle("Températures Moyennes");

        //Barres
        final CategoryAxis xAxisBC = new CategoryAxis();
        final NumberAxis yAxisBC = new NumberAxis();
        xAxisBC.setLabel("Mois");
        yAxisBC.setLabel("Température");

        final BarChart<String,Number> barChart =
                new BarChart<String, Number>(xAxisBC, yAxisBC);
        barChart.setTitle("Températures Moyennes");

        borderPane.setTop(menuBar);


        //ACTIONS
        //Ouvrir


        FileChooser fc = new FileChooser();
        fc.setTitle("Veuillez sélectionner un fichier");
        fc.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        "Fichier DAT", "*.dat"));

        int[] type = new int[1];  // 1 = lineChart  2 = areaChart  3 = barChart

        lignes.setOnAction((event) -> {
            type[0] = 1;
            File fichier = fc.showOpenDialog(primaryStage);
            lineChart.getData().clear();
            lineChart.getData().addAll(chercherDonnees(fichier));
            borderPane.setCenter(lineChart);


        });

        regions.setOnAction((event) -> {
            type[0] = 2;
           File fichier = fc.showOpenDialog(primaryStage);
           areaChart.getData().clear();
            areaChart.getData().addAll(chercherDonnees(fichier));
            borderPane.setCenter(areaChart);


        });

        barres.setOnAction((event) -> {
            type[0] = 3;
            File fichier = fc.showOpenDialog(primaryStage);
           barChart.getData().clear();
            barChart.getData().addAll(chercherDonnees(fichier));

            borderPane.setCenter(barChart);


        });

        //Sauvegarder

        String[] format = new String[1];
        png.setOnAction(event -> {
            format[0] = "png";

            switch (type[0]){
                case 1 : saveAsPng(lineChart, format[0] ); break;
                case 2 : saveAsPng(areaChart, format[0] ); break;
                case 3 : saveAsPng(barChart, format[0] ); break;
            }

        });

        gif.setOnAction(event -> {
            format[0] = "gif";
            switch (type[0]){
                case 1 : saveAsPng(lineChart, format[0] ); break;
                case 2 : saveAsPng(areaChart, format[0] ); break;
                case 3 : saveAsPng(barChart, format[0] ); break;
            }
        });

        primaryStage.setScene(new Scene(borderPane));
        primaryStage.show();
    }


    public static XYChart.Series chercherDonnees(File fc){
        XYChart.Series serieDonnees = new XYChart.Series();

        serieDonnees.setName("Données");
        try {
            List<String> list = Files.readAllLines(Paths.get(fc.getPath())); //lire les lignes du fichier
            String[] mois = {list.get(0)};
            String[] degres = {list.get(1)};

            mois = mois[0].split(",");
            degres = degres[0].split(",");

            serieDonnees.getData().add(new XYChart.Data(mois[0].trim(),  Float.parseFloat(degres[0 ])));
            serieDonnees.getData().add(new XYChart.Data(mois[1].trim(),  Float.parseFloat(degres[1 ])));
            serieDonnees.getData().add(new XYChart.Data(mois[2].trim(),  Float.parseFloat(degres[2 ])));
            serieDonnees.getData().add(new XYChart.Data(mois[3].trim(),  Float.parseFloat(degres[3 ])));
            serieDonnees.getData().add(new XYChart.Data(mois[4].trim(),  Float.parseFloat(degres[4 ])));
            serieDonnees.getData().add(new XYChart.Data(mois[5].trim(),  Float.parseFloat(degres[5 ])));
            serieDonnees.getData().add(new XYChart.Data(mois[6].trim(),  Float.parseFloat(degres[6 ])));
            serieDonnees.getData().add(new XYChart.Data(mois[7].trim(),  Float.parseFloat(degres[7 ])));

        }catch (IOException e){}
        return serieDonnees;
    }

    public void saveAsPng(Chart chart, String format) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer sous");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        "Fichier image", "*." + format));

        WritableImage image = chart.snapshot(new SnapshotParameters(), null);

        File enregistre = fileChooser.showSaveDialog(null);
        try {

            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", enregistre);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

