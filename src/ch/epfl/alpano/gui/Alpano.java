/**
 * Alpano
 * 
 * @author Andrea Scalisi (259183)
 * @author Gerald Sula (257396)
 */
package ch.epfl.alpano.gui;


import static java.lang.Math.toDegrees;

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
import javafx.geometry.Pos;
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
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;
import ch.epfl.alpano.Azimuth;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.DiscreteElevationModel;
import ch.epfl.alpano.dem.HgtDiscreteElevationModel;
import ch.epfl.alpano.summit.GazetteerParser;
import ch.epfl.alpano.summit.Summit;
import static java.lang.String.format;

public final class Alpano extends Application {
    
    private final String [][] hgtNames = {{"N45E006.hgt", "N45E007.hgt", "N45E008.hgt", "N45E009.hgt"}, {"N46E006.hgt", "N46E007.hgt", "N46E008.hgt", "N46E009.hgt"}};
    private HgtDiscreteElevationModel [][] hTab = new HgtDiscreteElevationModel[2][4];
    private DiscreteElevationModel DEM;
    private List<Summit> summitList;
    
  public static void main(String[] args) {
    Application.launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws IOException {
       
     for(int i=0; i<2; ++i){
         for(int j=0; j<4; ++j){
             hTab[i][j] = new HgtDiscreteElevationModel(new File(hgtNames[i][j]));
         } 
     }
     
     DiscreteElevationModel dem1 = ((hTab[0][0].union(hTab[0][1])).union(hTab[0][2])).union(hTab[0][3]);
     DiscreteElevationModel dem2 =((hTab[1][0].union(hTab[1][1])).union(hTab[1][2])).union(hTab[1][3]);  // CONSTANTS ??
     DEM = dem1.union(dem2);
     summitList = GazetteerParser.readSummitsFrom(new File("alps.txt"));
     PanoramaParametersBean parametersBean = new PanoramaParametersBean(PredefinedPanoramas.ALPES_JURA);
     PanoramaComputerBean computerBean = new PanoramaComputerBean(new ContinuousElevationModel(DEM), summitList);
     computerBean.setParameters(PredefinedPanoramas.ALPES_JURA);
     
     
      
    ImageView panoView = new ImageView();
    Pane labelsPane = new Pane(); 
    

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
    
    TextArea area = new TextArea();
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
   
    
    StackPane panoGroup = new StackPane(panoView,labelsPane);
    ScrollPane panoScrollPane = new ScrollPane(panoGroup);
    
    
    StackPane updateNotice = new StackPane();
    Text updateText = new Text("Les paramètres du panorama ont changé.\n Cliquez ici pour mettre à jour."); 
    updateText.setFont(new Font(40));
    updateText.setTextAlignment(TextAlignment.CENTER);
    updateNotice.getChildren().add(updateText);
    Color color = new Color(1,1,1,0.9);
    updateNotice.setBackground(new Background( new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
    updateNotice.setOnMouseClicked(x->{
        computerBean.setParameters(parametersBean.parametersProperty().getValue());
    });
    
    
    StackPane panoPane = new StackPane(panoScrollPane,updateNotice);
    

    
    BorderPane root = new BorderPane(panoPane);
    root.setBottom(paramsGrid);
    Scene scene = new Scene(root);
    
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
        direction = Azimuth.toOctantString(azimuth, "N", "E", "S", "W");
        azimuth = toDegrees(azimuth);
        elevation = toDegrees(computerBean.getPanorama().parameters().altitudeForY(posY));
        altitude = (computerBean.getPanorama().elevationAt((int)posX, (int)posY));
        //System.out.println("Longitude : "+longitude+", latitude : "+latitude);
        //System.out.println("Distance : "+distance);
        //System.out.println("Altitude : "+altitude);
        //System.out.println("Azimuth : "+azimuth+" "+direction+", Elevation : "+elevation);
        
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
        String qy = "mlat="+latitudeStr+"&mlon="+longitudeStr;  // "query" : partie après le ?
        String fg = "map=15/"+latitudeStr+"/"+longitudeStr;  // "fragment" : partie après le #
        URI osmURI;
        try {
            osmURI = new URI("http", "www.openstreetmap.org", "/", qy, fg);
            java.awt.Desktop.getDesktop().browse(osmURI);
        } catch (URISyntaxException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    });
    
    labelsPane.prefWidthProperty().bind(parametersBean.widthProperty());
    labelsPane.prefHeightProperty().bind(parametersBean.heightProperty());
    Bindings.bindContent( labelsPane.getChildren(),computerBean.getLabels());
    labelsPane.setMouseTransparent(true);
    
   BooleanExpression check=computerBean.parametersProperty().isNotEqualTo(parametersBean.parametersProperty());
   updateNotice.visibleProperty().bind(check);
   //updateNotice.setVisible(true);
   
  
     

    primaryStage.setTitle("Alpano");
    primaryStage.setScene(scene);
    primaryStage.show();
  }
  private TextField createField(int columnCount, StringConverter<Integer> stringConverter, ObjectProperty<Integer> property){
      TextField tf=new TextField();
      tf.setAlignment(Pos.CENTER_RIGHT);
      TextFormatter<Integer> formatter =   new TextFormatter<>(stringConverter);
      formatter.valueProperty().bindBidirectional(property);
      tf.setPrefColumnCount(columnCount);
      tf.setTextFormatter(formatter);
      return tf;
      
      
  }
}