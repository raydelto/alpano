package ch.epfl.alpano.gui;

import ch.epfl.alpano.Panorama;
import javafx.scene.image.WritableImage;
public interface PanoramaRenderer {
    
    static WritableImage renderPanorama(Panorama pan, ImagePainter painter)
    {
        WritableImage wi=new WritableImage(pan.parameters().width(), pan.parameters().height());
        for(int i=0;i<pan.parameters().width();i++)
        {
            for(int j=0;j<pan.parameters().height();j++)
            {
                wi.getPixelWriter().setColor(i, j, painter.colorAt(i, j));
            }
        }
        return wi;
        
    }

}
