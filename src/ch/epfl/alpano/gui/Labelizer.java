package ch.epfl.alpano.gui;

import java.util.List;

import ch.epfl.alpano.PanoramaParameters;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.summit.Summit;
import javafx.scene.Node;
import javafx.scene.shape.*;
import javafx.scene.text.*;

public final class Labelizer {
    private final ContinuousElevationModel cev;
    private final List <Summit> summitList;
    public Labelizer(ContinuousElevationModel cev, List <Summit> summitList) {
        this.cev=cev;
        this.summitList=summitList;
    }
    public List<Node> labels(PanoramaParameters param)
    {
        param.
    }
    private boolean isVisible(PanoramaParameters p, Summit s)
    {
        double xDist=p.observerPosition().distanceTo(s.position());
       if(xDist>=Math.sqrt(Math.pow(p.observerElevation()-s.elevation(), 2)+Math.pow(xDist,2)))
       {
           return false;
       }
    }

}
