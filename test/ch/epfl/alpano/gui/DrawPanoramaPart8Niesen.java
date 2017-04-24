package ch.epfl.alpano.gui;


import java.io.File;

import javax.imageio.ImageIO;

import ch.epfl.alpano.Panorama;
import ch.epfl.alpano.PanoramaComputer;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.DiscreteElevationModel;
import ch.epfl.alpano.dem.HgtDiscreteElevationModel;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

final class DrawPanoramaPart8Niesen {
    final static File HGT_FILE1 = new File("N46E007.hgt");
    final static File HGT_FILE2 = new File("N46E006.hgt");

    public static void main(String[] as) throws Exception {
        double t=System.currentTimeMillis();

        try (DiscreteElevationModel dDEM =
                new HgtDiscreteElevationModel(HGT_FILE1)) {
            ContinuousElevationModel cDEM = new ContinuousElevationModel(dDEM);

            Panorama p = new PanoramaComputer(cDEM)
                    .computePanorama(PredefinedPanoramas.NIESEN.panoramaParameters());

            ChannelPainter gray =
                    ChannelPainter.maxDistanceToNeighbors(p)
                    .sub(500)
                    .div(4500)
                    .clamped()
                    .inverted();

            ChannelPainter distance = p::distanceAt;
            ChannelPainter opacity =
                    distance.map(d -> d == Float.POSITIVE_INFINITY ? 0 : 1);

            ImagePainter l = ImagePainter.gray(gray, opacity);

            Image i = PanoramaRenderer.renderPanorama(p, l);
            ImageIO.write(SwingFXUtils.fromFXImage(i, null),
                    "png",
                    new File("niesen-profile.png"));

            //image en couleur
            ChannelPainter h = (x,y)->360*distance.div(100000).cycling().valueAt(x, y);

            ChannelPainter s = distance.div(200000).clamped().inverted();

            ChannelPainter slope = p::slopeAt;
            ChannelPainter b = (x,y)->0.3f+0.7f*slope.mul(2).div((float)Math.PI).inverted().valueAt(x, y);

            ImagePainter painter = ImagePainter.hsb(h, s, b, opacity);
            Image col = PanoramaRenderer.renderPanorama(p, painter);
            ImageIO.write(SwingFXUtils.fromFXImage(col, null),
                    "png",
                    new File("niesen-shaded.png"));


        }

        System.out.println(System.currentTimeMillis()-t);
    }
}


