package ch.epfl.alpano.gui;

import ch.epfl.alpano.Panorama;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
public interface PanoramaRenderer {
    
    static WritableImage renderPanorama(Panorama pan, ImagePainter painter)
    {
        return new WritableImage(pan, arg1)
    }

}
