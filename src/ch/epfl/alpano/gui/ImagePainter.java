/**
 * ImagePainter
 * 
 * @author Andrea Scalisi (259183)
 * @author Gerald Sula (257396)
 */
package ch.epfl.alpano.gui;

import javafx.scene.paint.Color;

public interface ImagePainter {
    abstract Color colorAt(int x, int y);

    /**
     * Defines the colors of an Image by a point's attributes
     * 
     * @param hue the hue
     * @param saturation the saturation
     * @param brightness the brightness
     * @param opacity  the opacity
     * @return An imagePainter taking into account the hue, saturation, brightness and opacity
     */
    static ImagePainter hsb(ChannelPainter hue, ChannelPainter saturation,
            ChannelPainter brightness, ChannelPainter opacity) {
        return (x, y) -> Color.hsb(hue.valueAt(x, y), saturation.valueAt(x, y),
                brightness.valueAt(x, y), opacity.valueAt(x, y));
    }

    /**
     * Defines the colors of a gray Image by a point's attributes
     * @param hue the hue of gray
     * @param opacity the opacity
     * @return n imagePainter taking into account the hue and opacity
     */
    static ImagePainter gray(ChannelPainter hue, ChannelPainter opacity) {
        return (x, y) -> Color.gray(hue.valueAt(x, y), opacity.valueAt(x, y));
    }

}
