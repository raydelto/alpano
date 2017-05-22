/**
 * Alpano
 * 
 * @author Andrea Scalisi (259183)
 * @author Gerald Sula (257396)
 */
package ch.epfl.alpano.gui;


import static ch.epfl.alpano.Azimuth.toOctantString;
import static ch.epfl.alpano.gui.PredefinedPanoramas.ALPES_JURA;
import static ch.epfl.alpano.summit.GazetteerParser.readSummitsFrom;
import static java.lang.Math.toDegrees;
import static java.lang.String.format;
import static javafx.beans.binding.Bindings.bindContent;
import static javafx.geometry.Pos.CENTER_RIGHT;
import static javafx.scene.text.TextAlignment.CENTER;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.DiscreteElevationModel;
import ch.epfl.alpano.dem.HgtDiscreteElevationModel;
import ch.epfl.alpano.summit.GazetteerParser;
import ch.epfl.alpano.summit.Summit;

public final class Alpano extends Application {

    private final int hTabWidth=4, hTabHeight=2;
    private final String [][] hgtNames = {{"N45E006.hgt", "N45E007.hgt", "N45E008.hgt", "N45E009.hgt"}, {"N46E006.hgt", "N46E007.hgt", "N46E008.hgt", "N46E009.hgt"}};
    private HgtDiscreteElevationModel [][] hTab = new HgtDiscreteElevationModel[hTabHeight][hTabWidth];
    private DiscreteElevationModel DEM;
    private List<Summit> summitList;
    private PanoramaParametersBean parametersBean;
    private PanoramaComputerBean computerBean;
    private TextArea area;


    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        for(int i=0; i<hTabHeight; ++i){
            for(int j=0; j<hTabWidth; ++j){
                hTab[i][j] = new HgtDiscreteElevationModel(new File(hgtNames[i][j]));
            } 
        }

        DEM = createDem(hTab);
        summitList = readSummitsFrom(new File("alps.txt"));
        parametersBean = new PanoramaParametersBean(ALPES_JURA);
        computerBean = new PanoramaComputerBean(new ContinuousElevationModel(DEM), summitList);
        computerBean.setParameters(ALPES_JURA);
        
        ImageView panoView = getPanoView();
        Pane labelsPane = new Pane(); 

        GridPane paramsGrid=getGridPane();
        


        StackPane panoGroup = new StackPane(panoView,labelsPane);
        ScrollPane panoScrollPane = new ScrollPane(panoGroup);

        StackPane updateNotice = getUpdateNotice();


        StackPane panoPane = new StackPane(panoScrollPane,updateNotice);



        BorderPane root = new BorderPane(panoPane);
        root.setBottom(paramsGrid);
        Scene scene = new Scene(root);

        

        labelsPane.prefWidthProperty().bind(parametersBean.widthProperty());
        labelsPane.prefHeightProperty().bind(parametersBean.heightProperty());
        bindContent( labelsPane.getChildren(),computerBean.getLabels());
        labelsPane.setMouseTransparent(true);

        BooleanExpression check=computerBean.parametersProperty().isNotEqualTo(parametersBean.parametersProperty());
        updateNotice.visibleProperty().bind(check);

        primaryStage.setTitle("Alpano");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private StackPane getUpdateNotice(){
        
        StackPane updateNotice = new StackPane();
        Text updateText = new Text("Les paramètres du panorama ont changé.\n Cliquez ici pour mettre à jour."); 
        updateText.setFont(new Font(40));
        updateText.setTextAlignment(CENTER);
        updateNotice.getChildren().add(updateText);
        Color color = new Color(1,1,1,0.9);
        updateNotice.setBackground(new Background( new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
        updateNotice.setOnMouseClicked(x->{
            computerBean.setParameters(parametersBean.parametersProperty().getValue());
        });
        
        return updateNotice;
    }
    private GridPane getGridPane()
    {
        StringConverter<Integer> stringConverteFourDecimal = new FixedPointStringConverter(4);
        StringConverter<Integer> stringConverteZeroDecimal = new IntegerStringConverter();
        TextField tfLat=createField(7, stringConverteFourDecimal, parametersBean.observerLatitudeProperty());
        TextField tfLong=createField(7, stringConverteFourDecimal, parametersBean.observerLongitudeProperty());
        TextField tfAlt=createField(4, stringConverteZeroDecimal, parametersBean.observerElevationProperty());
        TextField tfAzim=createField(3, stringConverteZeroDecimal, parametersBean.centerAzimuthProperty());
        TextField tfAngle=createField(3, stringConverteZeroDecimal, parametersBean.horizontalFieldOfViewProperty());
        TextField tfVisib=createField(3, stringConverteZeroDecimal, parametersBean.maxDistanceProperty());
        TextField tfLarg=createField(4, stringConverteZeroDecimal, parametersBean.widthProperty());
        TextField tfHaut=createField(4, stringConverteZeroDecimal, parametersBean.heightProperty());

        ChoiceBox<Integer> choice =new ChoiceBox<>();
        choice.getItems().addAll(0, 1, 2);
        StringConverter<Integer> stringConverterChoice = new LabeledListStringConverter("non", "2×", "4×");
        choice.valueProperty().bindBidirectional(parametersBean.superSamplingExponentProperty());
        choice.setConverter(stringConverterChoice);

        area = new TextArea();
        area.setEditable(false);
        area.setPrefRowCount(2);
        
        List<Node> nodeList= new ArrayList<>(Arrays.asList(new Label("Latitude (°)"),tfLat,new Label("Longitude (°)"),tfLong,new Label("Altitude (m)"),tfAlt,new Label("Azimuth (°)"),
                tfAzim,new Label("Angle de vue (°)"),tfAngle,new Label("Visibilité (km)"),tfVisib,new Label("Largeur (px)"),tfLarg,new Label("Hauteur (px)"),tfHaut,new Label("Surechantillionage"), choice));

        GridPane paramsGrid=new GridPane();
        for(int i=0;i<3;i++)
        {
            for(int j=0; j<6;j++)
            {
                paramsGrid.add(nodeList.get(i*6+j), j,i);
            }
        }

        paramsGrid.add(area,7,0,40,3);
        return paramsGrid;
    }
    private ImageView getPanoView(){
        
        ImageView panoView = new ImageView();
        panoView.fitWidthProperty().bind(parametersBean.widthProperty());
        panoView.imageProperty().bind(computerBean.imageProperty());
        panoView.setPreserveRatio(true);
        panoView.setSmooth(true);


        panoView.setOnMouseMoved(x -> {

            double longitude, latitude, distance, azimuth, elevation, altitude;
            double posX = x.getX();
            double posY = x.getY();
            String direction;
            longitude = toDegrees(computerBean.getPanorama().longitudeAt((int)posX, (int)posY));
            latitude = toDegrees(computerBean.getPanorama().latitudeAt((int)posX, (int)posY));
            distance = computerBean.getPanorama().distanceAt((int)posX, (int)posY)/1000;
            azimuth = computerBean.getPanorama().parameters().azimuthForX(posX);
            direction = toOctantString(azimuth, "N", "E", "S", "W");
            azimuth = toDegrees(azimuth);
            elevation = toDegrees(computerBean.getPanorama().parameters().altitudeForY(posY));
            altitude = (computerBean.getPanorama().elevationAt((int)posX, (int)posY));

            StringBuilder sb = new StringBuilder();
            sb.append("Position : ").append(format("%.4f", longitude)).append("°N ").append(format("%.4f", latitude)).append("°E")
            .append("\n Distance : ").append(format("%.1f", distance)).append(" km")
            .append("\n Altitude : ").append(format("%.0f", altitude)).append(" m")
            .append("\n Azimuth : ").append(format("%.1f", azimuth)).append("°(").append(direction).append(") Elévation : ").append(format("%.1f", elevation)).append("°");

            area.setText(sb.toString());

        });

        panoView.setOnMouseClicked(x ->{
            double longitude, latitude;
            longitude = toDegrees(computerBean.getPanorama().longitudeAt((int)x.getX(), (int)x.getY()));
            latitude = toDegrees(computerBean.getPanorama().latitudeAt((int)x.getX(), (int)x.getY()));
            String longitudeStr = format((Locale)null, "%.6f", longitude);
            String latitudeStr = format((Locale)null, "%.6f", latitude);
            String qy = "mlat="+latitudeStr+"&mlon="+longitudeStr;  
            String fg = "map=15/"+latitudeStr+"/"+longitudeStr;  
            URI osmURI;
            try {
                osmURI = new URI("http", "www.openstreetmap.org", "/", qy, fg);
                Desktop.getDesktop().browse(osmURI);
            } catch (URISyntaxException | IOException e) {

                e.printStackTrace();
            }

        });
        
        return panoView;
    }
    
    private TextField createField(int columnCount, StringConverter<Integer> stringConverter, ObjectProperty<Integer> property){
        TextField tf=new TextField();
        tf.setAlignment(CENTER_RIGHT);
        TextFormatter<Integer> formatter =   new TextFormatter<>(stringConverter);
        formatter.valueProperty().bindBidirectional(property);
        tf.setPrefColumnCount(columnCount);
        tf.setTextFormatter(formatter);
        return tf;


    }
    private DiscreteElevationModel createDem (HgtDiscreteElevationModel [][] hTab){
        DiscreteElevationModel temp[]=new DiscreteElevationModel[hTab.length];
        for(int i=0;i<hTab.length;i++){
            temp[i]=hTab[i][0].union(hTab[i][1]);
            for(int j=1;j<hTab[i].length-1;j++){
                temp[i]=temp[i].union(hTab[i][j+1]);
            }
        }
        return temp[0].union(temp[1]);
    }
}