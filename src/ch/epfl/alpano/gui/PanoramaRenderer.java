/**
 * PanoramaRenderer
 * 
 * @author Andrea Scalisi (259183)
 * @author Gerald Sula (257396)
 */
package ch.epfl.alpano.gui;

import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import ch.epfl.alpano.Panorama;

public interface PanoramaRenderer {

    /**
     * Allows to draw the image of a panorama by usinag a panorama and an
     * ImagePainter
     * 
     * @param pan     the panorama
     * @param painter the painter
     * @return Image of the panorama
     */
    static Image renderPanorama(Panorama pan, ImagePainter painter) {

        WritableImage wi = new WritableImage(pan.parameters().width(), pan.parameters().height());
        PixelWriter pi = wi.getPixelWriter();

        for (int i = 0; i < pan.parameters().width(); i++) {
            for (int j = 0; j < pan.parameters().height(); j++) {
                pi.setColor(i, j, painter.colorAt(i, j));
            }
        }

        return wi;
    }
}
