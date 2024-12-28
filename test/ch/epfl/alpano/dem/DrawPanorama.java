package ch.epfl.alpano.dem;

import static java.lang.Math.*;

import java.io.File;

import javax.imageio.ImageIO;

import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.Panorama;
import ch.epfl.alpano.PanoramaComputer;
import ch.epfl.alpano.PanoramaParameters;
import ch.epfl.alpano.gui.ChannelPainter;
import ch.epfl.alpano.gui.ImagePainter;
import ch.epfl.alpano.gui.PanoramaRenderer;
import ch.epfl.alpano.gui.PredefinedPanoramas;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

final class DrawPanorama {
    final static File HGT_FILE = new File("N46E007.hgt");

    final static int IMAGE_WIDTH = 500;
    final static int IMAGE_HEIGHT = 200;

    final static double ORIGIN_LON = toRadians(7.65);
    final static double ORIGIN_LAT = toRadians(46.73);
    final static int ELEVATION = 600;
    final static double CENTER_AZIMUTH = toRadians(180);
    final static double HORIZONTAL_FOV = toRadians(60);
    final static int MAX_DISTANCE = 100_000;

    final static PanoramaParameters PARAMS =
      new PanoramaParameters(new GeoPoint(ORIGIN_LON,
                                          ORIGIN_LAT),
                             ELEVATION,
                             CENTER_AZIMUTH,
                             HORIZONTAL_FOV,
                             MAX_DISTANCE,
                             IMAGE_WIDTH,
                             IMAGE_HEIGHT);

    public static void main(String[] as) throws Exception {
      try (DiscreteElevationModel dDEM =
           new HgtDiscreteElevationModel(HGT_FILE)) {
        ContinuousElevationModel cDEM =
          new ContinuousElevationModel(dDEM);
//        Panorama p = new PanoramaComputer(cDEM)
//          .computePanorama(PARAMS);
        
        Panorama p = new PanoramaComputer(cDEM)
                .computePanorama(PredefinedPanoramas.NIESEN.panoramaParameters());
        
        
       // Labelizer lab=new Labelizer(cDEM, GazetteerParser.readSummitsFrom(new File("alps.txt")));
       // lab.labels(PredefinedPanoramas.NIESEN.panoramaParameters());

//        BufferedImage i =
//          new BufferedImage(IMAGE_WIDTH,
//                            IMAGE_HEIGHT,
//                            TYPE_INT_RGB);
//
//        for (int x = 0; x < IMAGE_WIDTH; ++x) {
//          for (int y = 0; y < IMAGE_HEIGHT; ++y) {
//            float d = p.distanceAt(x, y);
//            int c = (d == Float.POSITIVE_INFINITY)
//              ? 0x87_CE_EB
//              : gray((d - 2_000) / 15_000);
//            i.setRGB(x, y, c);
//          }
//        }
//
//        ImageIO.write(i, "png", new File("niesen.png"));
        
//        ChannelPainter gray = ChannelPainter.maxDistanceToNeighbors(p)
//                .sub(500)
//                .div(4500)
//                .clamped()
//                .inverted();
//
//              ChannelPainter distance = p::distanceAt;
//              ChannelPainter opacity = distance.map(d -> d == Float.POSITIVE_INFINITY ? 0 : 1);
//
//              ImagePainter l = ImagePainter.gray(gray, opacity);
//
//              Image j = PanoramaRenderer.renderPanorama(p, l);
//              ImageIO.write(SwingFXUtils.fromFXImage(j, null), "png", new File("niesen-profile.png"));
        
        ChannelPainter distance = p::distanceAt;
        ChannelPainter slope = p::slopeAt;
        
        ChannelPainter h = distance
                .div(100000)
                .cycling()
                .mul(360);
        
        ChannelPainter s = distance
                .div(200000)
                .clamped()
                .inverted();
        
        ChannelPainter b = slope
                .mul(2)
                .div((float)Math.PI)
                .inverted()
                .mul(0.7f)
                .add(0.3f);
                
        ChannelPainter o = distance.map(d -> d == Float.POSITIVE_INFINITY ? 0 : 1);
         
         ImagePainter l = ImagePainter.hsb(h, s, b, o);
         Image j = PanoramaRenderer.renderPanorama(p, l);
         ImageIO.write(SwingFXUtils.fromFXImage(j, null), "png", new File("niesen-profile-color.png"));
         
         
         
       ChannelPainter gray = ChannelPainter.maxDistanceToNeighbors(p)
       .sub(500)
       .div(4500)
       .clamped()
       .inverted();

       ImagePainter l2 = ImagePainter.gray(gray, o);

       Image i = PanoramaRenderer.renderPanorama(p, l2);
       ImageIO.write(SwingFXUtils.fromFXImage(i, null), "png", new File("niesen-profile.png"));
      }
            
    }

  }