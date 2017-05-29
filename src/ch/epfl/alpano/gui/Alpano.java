/**
 * Alpano
 * 
 * @author Andrea Scalisi (259183)
 * @author Gerald Sula (257396)
 */
package ch.epfl.alpano.gui;

import static ch.epfl.alpano.Azimuth.toOctantString;
import static ch.epfl.alpano.gui.PredefinedPanoramas.ALPES_JURA;
import static ch.epfl.alpano.gui.PredefinedPanoramas.FINSTERAARHORN;
import static ch.epfl.alpano.gui.PredefinedPanoramas.MONT_RACINE;
import static ch.epfl.alpano.gui.PredefinedPanoramas.NIESEN;
import static ch.epfl.alpano.gui.PredefinedPanoramas.PLAGE_PELICAN;
import static ch.epfl.alpano.gui.PredefinedPanoramas.TOUR_SAUVABELIN;
import static ch.epfl.alpano.summit.GazetteerParser.readSummitsFrom;
import static java.lang.Math.pow;
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

import javax.imageio.ImageIO;

import javafx.application.Application;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
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
import ch.epfl.alpano.summit.Summit;

public final class Alpano extends Application {

    private final static String [][] HGTNAMES = {{"N45E006.hgt", "N45E007.hgt", "N45E008.hgt", "N45E009.hgt"}, {"N46E006.hgt", "N46E007.hgt", "N46E008.hgt", "N46E009.hgt"}};
    private final static int HTABWIDTH = HGTNAMES[0].length, HTABHEIGHT = HGTNAMES.length;
    private HgtDiscreteElevationModel [][] hTab = new HgtDiscreteElevationModel[HTABHEIGHT][HTABWIDTH];
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

        //Creating an array filled with all the HGT files
        for(int i=0; i<HTABHEIGHT; ++i){
            for(int j=0; j<HTABWIDTH; ++j){
                hTab[i][j] = new HgtDiscreteElevationModel(new File(HGTNAMES[i][j]));
            } 
        }

        DEM = createDem();
        summitList = readSummitsFrom(new File("alps.txt"));
        parametersBean = new PanoramaParametersBean(ALPES_JURA);
        computerBean = new PanoramaComputerBean(new ContinuousElevationModel(DEM), summitList);

        ImageView panoView = getPanoView();
        Pane labelsPane = new Pane(); 
        GridPane paramsGrid=getGridPane();

        
        Image image = new Image("dem.png");
        ImageView imgView= new ImageView(image);
        StackPane panoGroup = new StackPane(imgView,panoView,labelsPane);
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

    /**
     * Creates the notice that indicates that the parameters have been modified
     * @return StackPane corresponding to the update notice
     */
    private StackPane getUpdateNotice(){

        StackPane updateNotice = new StackPane();
        String clicktxt= "Les paramètres du panorama ont changé.\n Cliquez ici pour mettre à jour.";
        Text updateText = new Text(clicktxt); 
        updateText.setFont(new Font(40));
        updateText.setTextAlignment(CENTER);
        updateNotice.getChildren().add(updateText);
        
        Color color = new Color(1,1,1,0.9);
        updateNotice.setBackground(new Background( new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
        
        updateNotice.setOnMousePressed(x-> updateText.setText("Veuillez patienter"));
        updateNotice.setOnMouseClicked(x->{ 
            computerBean.setParameters(parametersBean.parametersProperty().getValue());
            updateText.setText(clicktxt);
           
        });

        return updateNotice;
    }

    /**
     * Creates the GridPane where the user can adjust the parameters
     * @return GridPane bottom left with the parametersBean
     */
    private GridPane getGridPane(){

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
        ChoiceBox<Integer> choicePaint =new ChoiceBox<>();
        choicePaint.getItems().addAll(0, 1, 2, 3);
        StringConverter<Integer> stringConverterChoicePint = new LabeledListStringConverter("Default", "Dark","Winter", "Black And White");
        choicePaint.valueProperty().bindBidirectional(parametersBean.painterProperty());
        choicePaint.setConverter(stringConverterChoicePint);
        
        ChoiceBox<PanoramaUserParameters> choicePredef =new ChoiceBox<>();
        choicePredef.getItems().addAll(ALPES_JURA,FINSTERAARHORN,MONT_RACINE,NIESEN,PLAGE_PELICAN,TOUR_SAUVABELIN);
       
        
        //StringConverter<PanoramaUserParameters> stringConverterChoicePredef = new LabeledListStringConverter("non", "2×", "4×");
        //choicePredef.valueProperty().getValue().observerElevation().bind(computerBean.parametersProperty());
       //choicePredef.setConverter(stringConverterChoicePredef);
        
        area = new TextArea();
        area.setEditable(false);
        area.setPrefRowCount(2);

        List<Node> nodeList= new ArrayList<>(Arrays.asList(new Label("Latitude (°)"),tfLat,new Label("Longitude (°)"),tfLong,new Label("Altitude (m)"),tfAlt,new Label("Azimuth (°)"),
                tfAzim,new Label("Angle de vue (°)"),tfAngle,new Label("Visibilité (km)"),tfVisib,new Label("Largeur (px)"),tfLarg,new Label("Hauteur (px)"),tfHaut,new Label("Surechantillionage"), choice));

        GridPane paramsGrid=new GridPane();

        for(int i=0;i<3;i++){
            for(int j=0; j<6;j++){
                paramsGrid.add(nodeList.get(i*6+j), j,i);
            }
        }

        paramsGrid.add(choicePaint, 7, 0);
        paramsGrid.add(choicePredef, 7, 1);
        paramsGrid.add(area,8,0,40,3);
        paramsGrid.setAlignment(Pos.CENTER);
        paramsGrid.setHgap(10);
        paramsGrid.setVgap(3);
        paramsGrid.setPadding(new Insets(7, 5, 5, 5));

        return paramsGrid;
    }

    /**
     * Creates the ImageView of the Panorama
     * Adds a listener on the mouse moved, that stores all the parameters of the Panorama at the position indicated by the mouse
     * Adds a lister on the mouse clicked, once left clicked it opens the website "openstreetmap.org" at the position that corresponds 
     * to the parameters at the position indicated by the mouse
     * @return ImageView containing the Panorama
     */
    private ImageView getPanoView(){

        ImageView panoView = new ImageView();
        panoView.fitWidthProperty().bind(parametersBean.widthProperty());
        panoView.imageProperty().bind(computerBean.imageProperty());
        panoView.setPreserveRatio(true);
        panoView.setSmooth(true);

        panoView.setOnMouseMoved(x -> {

            double longitude, latitude, distance, azimuth, elevation, altitude;
            double pow = pow(2, computerBean.getParameters().superSamplingExponent());
            double posX = x.getX()*pow;
            double posY = x.getY()*pow;
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
            sb.append("Position : ").append(format("%.4f", longitude)).append("°").append(longitude > 0 ? "N" : "S").append(" ").append(format("%.4f", latitude)).append("°").append(latitude > 0 ? "E" : "W")
            .append("\n Distance : ").append(format("%.1f", distance)).append(" km")
            .append("\n Altitude : ").append(format("%.0f", altitude)).append(" m")
            .append("\n Azimuth : ").append(format("%.1f", azimuth)).append("°(").append(direction).append(") Elévation : ").append(format("%.1f", elevation)).append("°");

            area.setText(sb.toString());

        });

        panoView.setOnMouseClicked(x ->{

            double longitude, latitude;
            double pow = pow(2, computerBean.getParameters().superSamplingExponent());
            int posX = (int)(x.getX()*pow);
            int posY = (int)(x.getY()*pow);
            longitude = toDegrees(computerBean.getPanorama().longitudeAt(posX, posY));
            latitude = toDegrees(computerBean.getPanorama().latitudeAt(posX, posY));
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

    /**
     * Creates a TextField for the given parameters 
     * @param columnCount the maximum length of the TextField
     * @param stringConverter a String converter that converts form int to String and from int to String
     * @param property the property of the parameter corresponding to the TextField
     * @return TextField corresponding to the given parameters
     */
    private TextField createField(int columnCount, StringConverter<Integer> stringConverter, ObjectProperty<Integer> property){

        TextField tf=new TextField();
        tf.setAlignment(CENTER_RIGHT);
        TextFormatter<Integer> formatter =   new TextFormatter<>(stringConverter);
        formatter.valueProperty().bindBidirectional(property);
        tf.setPrefColumnCount(columnCount);
        tf.setTextFormatter(formatter);

        return tf;
    }

    /**
     * This method creates a DEM doing the union between all the HGT files that has been loaded in HTAB
     * @return Discrete Elevation Model of all the HGT files
     */
    private DiscreteElevationModel createDem (){

        DiscreteElevationModel temp[]=new DiscreteElevationModel[HTABHEIGHT];

        for(int i=0;i<HTABHEIGHT;i++){
            temp[i]=hTab[i][0].union(hTab[i][1]);
            for(int j=1;j<HTABWIDTH-1;j++){
                temp[i]=temp[i].union(hTab[i][j+1]);
            }
        }
        
        //We suppose that there are always only 2 rows in HTAB 
        return temp[0].union(temp[1]);
    }
}