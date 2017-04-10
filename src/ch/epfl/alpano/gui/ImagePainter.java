package ch.epfl.alpano.gui;

import javafx.scene.paint.Color;

public interface ImagePainter {
    abstract Color colorAt(int x, int y);
    
    static ImagePainter hsb(ChannelPainter hue, ChannelPainter saturation, ChannelPainter brightness, ChannelPainter opacity)
    {
        return (x,y)->Color.hsb(hue.valueAt(x, y), saturation.valueAt(x, y), brightness.valueAt(x, y), opacity.valueAt(x, y));
    }
    static ImagePainter gray(ChannelPainter hue,ChannelPainter opacity)
    {
        return (x,y)->Color.gray(hue.valueAt(x, y),opacity.valueAt(x, y));
    }

}
