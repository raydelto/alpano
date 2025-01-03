/**
 * PanoramaComputerBean
 * 
 * @author Andrea Scalisi (259183)
 * @author Gerald Sula (257396)
 */
package ch.epfl.alpano.gui;

import static ch.epfl.alpano.gui.ImagePainter.hsb;
import static ch.epfl.alpano.gui.PanoramaRenderer.renderPanorama;
import static java.lang.Float.POSITIVE_INFINITY;
import static java.lang.Math.PI;
import static javafx.collections.FXCollections.observableArrayList;

import java.util.List;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.Image;
import ch.epfl.alpano.Panorama;
import ch.epfl.alpano.PanoramaComputer;
import ch.epfl.alpano.PanoramaParameters;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.summit.Summit;

public class PanoramaComputerBean {

    private final ObjectProperty<PanoramaUserParameters> panoramaUserParamProperty;
    private final ObjectProperty<Panorama> panoramaProperty;
    private final ObjectProperty<Image> imageProperty;
    private final ObjectProperty<ObservableList<Node>> labelsProperty;
    private final ObservableList<Node> labelsList;
    private final PanoramaComputer panComp;
    private final Labelizer labels;

    /**
     * Creates a Panorama Computer bean
     * 
     * @param cem     the Continuous Elevation Model used for the creation of the
     *                bean
     * @param summits the list of Summits used for the creation of the bean
     */
    PanoramaComputerBean(ContinuousElevationModel cem, List<Summit> summits) {

        panComp = new PanoramaComputer(cem);
        labels = new Labelizer(cem, summits);
        labelsList = observableArrayList();
        panoramaUserParamProperty = new SimpleObjectProperty<>();
        panoramaProperty = new SimpleObjectProperty<>();
        imageProperty = new SimpleObjectProperty<>();
        labelsProperty = new SimpleObjectProperty<>(labelsList);
        panoramaUserParamProperty.addListener((b, o, n) -> update());
    }

    /**
     * Returns the property (ObjectProperty) of the PanoramaUserParameters
     * 
     * @return the property of the PanoramaUserParameters
     */
    ObjectProperty<PanoramaUserParameters> parametersProperty() {

        return panoramaUserParamProperty;
    }

    /**
     * returns the PanoramaUserParameters (value of the stocked property of the
     * bean)
     * 
     * @return the PanoramaUserParameters
     */
    PanoramaUserParameters getParameters() {

        return panoramaUserParamProperty.get();
    }

    /**
     * Sets the value of the bean property of PanoramaUserParameters to the new
     * value
     * 
     * @param newParameters the parameters to be set
     */
    void setParameters(PanoramaUserParameters newParameters) {

        panoramaUserParamProperty.set(newParameters);
    }

    /**
     * Returns the property (ReadOnlyObjectProperty) of the Panorama
     * 
     * @return the property of the Panorama
     */
    ReadOnlyObjectProperty<Panorama> panoramaProperty() {

        return panoramaProperty;
    }

    /**
     * returns the Panorama (value of the stocked property of the bean)
     * 
     * @return the Panorama
     */
    Panorama getPanorama() {

        return panoramaProperty.get();
    }

    /**
     * Returns the property (ReadOnlyObjectProperty) of the Image
     * 
     * @return the property of the Image
     */
    ReadOnlyObjectProperty<Image> imageProperty() {

        return imageProperty;
    }

    /**
     * returns the Image (value of the stocked property of the bean)
     * 
     * @return the Image
     */
    Image getImage() {

        return imageProperty.get();
    }

    /**
     * Returns the property (ReadOnlyObjectProperty) of the Obseravble list of Nodes
     * 
     * @return the property of the List of Nodes
     */
    ReadOnlyObjectProperty<ObservableList<Node>> labelsProperty() {

        return labelsProperty;
    }

    /**
     * returns the Observable List of Nodes (value of the stocked property of the
     * bean)
     * 
     * @return the List of Nodes
     */
    ObservableList<Node> getLabels() {

        return labelsProperty.get();
    }

    /**
     * updates the values stored in the properties to the new values
     */
    private void update() {

        PanoramaParameters param = panoramaUserParamProperty.get().panoramaParameters();
        labelsList.setAll(labels.labels(panoramaUserParamProperty.getValue().panoramaDisplayParameters()));
        panoramaProperty.set(panComp.computePanorama(param));
        Panorama p = panoramaProperty.get();
        ChannelPainter distance = p::distanceAt;
        ChannelPainter slope = p::slopeAt;
        ChannelPainter h, s, b, o = distance.map(d -> d == POSITIVE_INFINITY ? 0 : 1);
        ImagePainter painter;

        // selects the painter
        switch (panoramaUserParamProperty.getValue().painter()) {
            case 3:// black and white

                h = null;
                s = null;
                b = null;
                ChannelPainter gray = ChannelPainter.maxDistanceToNeighbors(p).sub(500).div(45).clamped().inverted();
                painter = ImagePainter.gray(gray, o);
                break;

            case 2:// winter

                h = (x, y) -> 360 * distance.div(100000).cycling().valueAt(x, y);
                s = distance.div(2000).clamped().inverted();
                b = (x, y) -> 0.3f + 0.7f * slope.mul(2).div((float) PI).inverted().valueAt(x, y);
                painter = hsb(h, s, b, o);
                break;

            case 1:// dark

                h = (x, y) -> 360 * distance.div(100000).valueAt(x, y);
                s = distance.div(200000).clamped().inverted();
                b = (x, y) -> 0.3f + 0.7f * slope.mul(2).div((float) PI).valueAt(x, y);
                painter = hsb(h, s, b, o);
                break;

            default:// default

                h = (x, y) -> 360 * distance.div(100000).cycling().valueAt(x, y);
                s = distance.div(200000).clamped().inverted();
                b = (x, y) -> 0.3f + 0.7f * slope.mul(2).div((float) PI).inverted().valueAt(x, y);
                painter = hsb(h, s, b, o);

        }

        imageProperty.set(renderPanorama(p, painter));
    }
}
