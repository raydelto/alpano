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
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
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
    Pane labelsPane = new Pane(); //TODO args
    StackPane panoGroup = new StackPane(labelsPane, panoView);
    ScrollPane panoScrollPane = new ScrollPane(panoGroup);
    Text updateText = new Text(); 
    StackPane updateNotice = new StackPane(updateText);
    StackPane panoPane = new StackPane(updateNotice, panoScrollPane);
    
    BorderPane root = new BorderPane(panoPane);
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
        direction = Azimuth.toOctantString(azimuth, "(N)", "(E)", "(S)", "(W)");
        azimuth = toDegrees(azimuth);
        elevation = toDegrees(computerBean.getPanorama().parameters().altitudeForY(posY));
        altitude = (int)(computerBean.getPanorama().elevationAt((int)posX, (int)posY));
        System.out.println("Longitude : "+longitude+", latitude : "+latitude);
        System.out.println("Distance : "+distance);
        System.out.println("Altitude : "+altitude);
        System.out.println("Azimuth : "+azimuth+" "+direction+", Elevation : "+elevation);
                
        
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

    primaryStage.setTitle("Alpano");
    primaryStage.setScene(scene);
    primaryStage.show();
  }
}