/**
 * Alpano
 * 
 * @author Andrea Scalisi (259183)
 * @author Gerald Sula (257396)
 */
package ch.epfl.alpano.gui;


import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import ch.epfl.alpano.Azimuth;
import ch.epfl.alpano.dem.CompositeDiscreteElevationModel;
import ch.epfl.alpano.dem.CompositeDiscreteElevationModelTest;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.DiscreteElevationModel;
import ch.epfl.alpano.dem.HgtDiscreteElevationModel;
import ch.epfl.alpano.summit.GazetteerParser;
import ch.epfl.alpano.summit.Summit;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanExpression;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import static java.lang.Math.toDegrees;



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
    

    TextField tfLat=new TextField(String.valueOf(computerBean.getPanorama().parameters().observerPosition().latitude()));
    tfLat.setAlignment(Pos.CENTER_RIGHT);
    tfLat.setPrefColumnCount(7);
    TextField tfLong=new TextField();
    tfLong.setAlignment(Pos.CENTER_RIGHT);
    tfLong.setPrefColumnCount(7);
    TextField tfAlt=new TextField();
    tfAlt.setAlignment(Pos.CENTER_RIGHT);
    tfAlt.setPrefColumnCount(4);
    TextField tfAzim=new TextField();
    tfAzim.setPrefColumnCount(3);
    tfAzim.setAlignment(Pos.CENTER_RIGHT);
    TextField tfAngle=new TextField();
    tfAngle.setAlignment(Pos.CENTER_RIGHT);
    tfAngle.setPrefColumnCount(3);
    TextField tfVisib=new TextField();
    tfVisib.setAlignment(Pos.CENTER_RIGHT);
    tfVisib.setPrefColumnCount(3);
    TextField tfLarg=new TextField();
    tfLarg.setAlignment(Pos.CENTER_RIGHT);
    tfLarg.setPrefColumnCount(4);
    TextField tfHaut=new TextField();
    tfHaut.setAlignment(Pos.CENTER_RIGHT);
    tfHaut.setPrefColumnCount(4);
    
    List<Node> nodeList= new ArrayList<>(Arrays.asList(new Label("Latitude (°)"),tfLat,new Label("Longitude (°)"),tfLong,new Label("Altitude (m)"),tfAlt,new Label("Azimuth (°)"),
            tfAzim,new Label("Angle de vue (°)"),tfAngle,new Label("Visibilité (km)"),tfVisib,new Label("Largeur (px)"),tfLarg,new Label("Hauteur (px)"),tfHaut,new Label("Surechantillionage"), new Label("Surechantillionage")));
    
    GridPane paramsGrid=new GridPane();
    for(int i=0;i<3;i++)
    {
        for(int j=0; j<6;j++)
        {
            paramsGrid.add(nodeList.get(i*6+j), j,i);
        }
    }
    
   
    
    StackPane panoGroup = new StackPane( panoView,labelsPane);//order makes a difference!
    ScrollPane panoScrollPane = new ScrollPane(panoGroup);
    Text updateText = new Text(); 
    
    StackPane updateNotice = new StackPane(updateText);
    Color color = new Color(1,1,1,0.9);
    updateNotice.setBackground(new Background( new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
    StackPane panoPane = new StackPane(updateNotice, panoScrollPane);
    
    BorderPane root = new BorderPane(panoPane);
    root.setBottom(paramsGrid);
    Scene scene = new Scene(root);
    
    panoView.fitWidthProperty().bind(parametersBean.widthProperty());
    panoView.imageProperty().bind(computerBean.imageProperty());
    panoView.setPreserveRatio(true);
    panoView.setSmooth(true);
    
    
    panoView.setOnMouseMoved(x -> {
        
        double longitude, latitude, distance, azimuth, elevation;
        int altitude;
        double posX = x.getX();
        double posY = x.getY();
        String direction;
        longitude = toDegrees(computerBean.getPanorama().longitudeAt((int)posX, (int)posY));
        latitude = toDegrees(computerBean.getPanorama().latitudeAt((int)posX, (int)posY));
        distance = computerBean.getPanorama().distanceAt((int)posX, (int)posY)/1000;
        azimuth = computerBean.getPanorama().parameters().azimuthForX(posX);
        direction = Azimuth.toOctantString(azimuth, "(N)", "(E)", "(S)", "(W)");//kujdes kto 
        azimuth = toDegrees(azimuth);
        elevation = toDegrees(computerBean.getPanorama().parameters().altitudeForY(posY));
        altitude = (int)(computerBean.getPanorama().elevationAt((int)posX, (int)posY));
        //System.out.println("Longitude : "+longitude+", latitude : "+latitude);
        //System.out.println("Distance : "+distance);
        //System.out.println("Altitude : "+altitude);
        //System.out.println("Azimuth : "+azimuth+" "+direction+", Elevation : "+elevation);
                
        
    });
    
    panoView.setOnMouseClicked(x ->{
        double longitude, latitude;
        longitude = toDegrees(computerBean.getPanorama().longitudeAt((int)x.getX(), (int)x.getY()));
        latitude = toDegrees(computerBean.getPanorama().latitudeAt((int)x.getX(), (int)x.getY()));
        String longitudeStr = String.format((Locale)null, "%.6f", longitude);
        String latitudeStr = String.format((Locale)null, "%.6f", latitude);
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
    
   //BooleanExpression check=computerBean.parametersProperty().isNotEqualTo(parametersBean.parametersProperty());
    //System.out.println(check.get()); 
     

    primaryStage.setTitle("Alpano");
    primaryStage.setScene(scene);
    primaryStage.show();
  }
}