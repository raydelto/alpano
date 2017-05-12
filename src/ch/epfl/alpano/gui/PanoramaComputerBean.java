package ch.epfl.alpano.gui;

import javafx.scene.image.Image;
import java.io.File;
import java.util.List;

import javax.imageio.ImageIO;

import ch.epfl.alpano.Panorama;
import ch.epfl.alpano.PanoramaComputer;
import ch.epfl.alpano.PanoramaParameters;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import ch.epfl.alpano.summit.Summit;

import static javafx.collections.FXCollections.observableArrayList;
public class PanoramaComputerBean {
    private final ObjectProperty<PanoramaUserParameters>  panoramaUserParamProperty;
    private ObjectProperty<Panorama> panoramaProperty;
    private ObjectProperty<Image> imageProperty;//shif mos ke importu gabim
    private ObjectProperty<ObservableList<Node>> labelsProperty;
    private ObservableList<Node> labelsList;
    private PanoramaComputer panComp;
    private Labelizer labels;
    
    PanoramaComputerBean(ContinuousElevationModel cem, List<Summit> summits)
    {
        //new PanoramaComputer(cem).computePanorama(new PanoramaParameters(cem., observerElevation, centerAzimuth, horizontalFieldOfView, maxDistance, width, height))
        //panoramaUserParamProperty=new PanoramaUserParameters(cem., observerLatitude, observerElevation, centerAzimuth, horizonrtalFieldOfView, maxDistance, width, height, samplingExponent)
   
        panComp= new PanoramaComputer(cem);
        labels= new Labelizer(cem, summits);
        labelsList = observableArrayList();
//        ObservableList<Node> labelList=new observableArrayList();
        panoramaUserParamProperty= new SimpleObjectProperty<>();
        panoramaProperty= new SimpleObjectProperty<>();
        imageProperty=new SimpleObjectProperty<>();
        labelsProperty=new SimpleObjectProperty<>(labelsList);
        
        panoramaUserParamProperty.addListener((b,o,n)->update());
        
        
    
    }
    
    ObjectProperty<PanoramaUserParameters> parametersProperty()
    {
        return panoramaUserParamProperty;
    }
    PanoramaUserParameters getParameters()
    {
       return panoramaUserParamProperty.get();
    }
    void setParameters(PanoramaUserParameters newParameters)
    {
        panoramaUserParamProperty.set(newParameters);
    }
    ReadOnlyObjectProperty<Panorama> panoramaProperty()
    {
        return panoramaProperty;
        
    }
    Panorama getPanorama()
    {
        return panoramaProperty.get();
        
    }
    ReadOnlyObjectProperty<Image> imageProperty()
    {
        return imageProperty;
        
    }
    Image getImage()
    {
        return imageProperty.get();
    }
    ReadOnlyObjectProperty<ObservableList<Node>> labelsProperty()
    {
        return labelsProperty;
        
    }
    ObservableList<Node> getLabels()
    {
        return labelsProperty.get();
    }
    private void update()
    {
        PanoramaParameters param=panoramaUserParamProperty.get().panoramaParameters();
        labelsList.setAll(labels.labels(param));// jo cast apo jo
        panoramaProperty.set(panComp.computePanorama(param));
        
        Panorama p=panoramaProperty.get();
        ChannelPainter distance = p::distanceAt;
        ChannelPainter opacity =
                distance.map(d -> d == Float.POSITIVE_INFINITY ? 0 : 1);

//        ImagePainter l = ImagePainter.gray(gray, opacity);
//
//        imageProperty.set(PanoramaRenderer.renderPanorama(p, l)); per me vone
        

        ChannelPainter h = (x,y)->360*distance.div(100000).cycling().valueAt(x, y);

        ChannelPainter s = distance.div(200000).clamped().inverted();

        ChannelPainter slope = p::slopeAt;
        ChannelPainter b = (x,y)->0.3f+0.7f*slope.mul(2).div((float)Math.PI).inverted().valueAt(x, y);

        ImagePainter painter = ImagePainter.hsb(h, s, b, opacity);
        imageProperty.set(PanoramaRenderer.renderPanorama(p, painter));
       
    }
    

}
