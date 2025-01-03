/**
 * PanoramaParameters
 * 
 * @author Andrea Scalisi (259183)
 * @author Gerald Sula (257396)
 */
package ch.epfl.alpano;

import static java.util.Objects.requireNonNull;
import static ch.epfl.alpano.Azimuth.canonicalize;
import static ch.epfl.alpano.Azimuth.isCanonical;
import static ch.epfl.alpano.Math2.PI2;
import static ch.epfl.alpano.Math2.angularDistance;
import static ch.epfl.alpano.Preconditions.checkArgument;

public final class PanoramaParameters {

    private final GeoPoint observerPosition;
    private final int observerElevation;
    private final double centerAzimuth;
    private final double horizontalFieldOfView;
    private final int maxDistance;
    private final int width;
    private final int height;
    private final double delta;
    private final double vericalFieldOfView;
    private final double middleHeight;
    private final double middleWidth;

    /**
     * Constructs a new PanoramaParameters
     * 
     * @param observerPosition      a geopoint corresponding to the position of the
     *                              observer
     * @param observerElevation     the observer elevation in meters
     * @param centerAzimuth         the center azimuth in radians
     * @param horizontalFieldOfView the horizontal field of view from the observer
     *                              in radians
     * @param maxDistance           the maximum field of view from the observer, in
     *                              meters
     * @param width                 a sample that indicates the width of the
     *                              panorama (a double)
     * @param height                a sample that indicates the height of the
     *                              panorama (a double)
     * @throws NullPointerException     if the observerPosition is null
     * @throws IllegalArgumentException if the azimuth is not canonical
     * @throws IllegalArgumentException if the horizontalFieldOfView is not between
     *                                  0 and PI/2
     * @throws IllegalArgumentException if either maxDistance, width or height are
     *                                  negatives
     */
    public PanoramaParameters(GeoPoint observerPosition, int observerElevation, double centerAzimuth,
            double horizontalFieldOfView, int maxDistance, int width, int height) {

        this.observerPosition = requireNonNull(observerPosition, "Null observerPosition");
        checkArgument(isCanonical(centerAzimuth), "Azimuth must be canonical");
        checkArgument(horizontalFieldOfView > 0 && horizontalFieldOfView <= PI2,
                "horizintalFieldOfView must be between 0 (excluded) and PI/2 (included)");
        checkArgument(maxDistance > 0 && width > 0 && height > 0, "maxDistance, height and width must be positive");

        this.observerElevation = observerElevation;
        this.centerAzimuth = centerAzimuth;
        this.horizontalFieldOfView = horizontalFieldOfView;
        this.maxDistance = maxDistance;
        this.width = width;
        this.height = height;
        this.delta = horizontalFieldOfView / (width - 1);
        this.vericalFieldOfView = delta * (height - 1);
        this.middleHeight = (height - 1) / 2.0;
        this.middleWidth = (width - 1) / 2.0;
    }

    /**
     * 
     * @return the position of the observer of the Panorama
     */
    public GeoPoint observerPosition() {

        return observerPosition;
    }

    /**
     * 
     * @return the elevation of the observer of the Panorama, in meters
     */
    public int observerElevation() {

        return observerElevation;
    }

    /**
     * 
     * @return the center azimuth of the Panorama, in radians
     */
    public double centerAzimuth() {

        return centerAzimuth;
    }

    /**
     * 
     * @return the horizontal field of view of the Panorama, in radians
     */
    public double horizontalFieldOfView() {

        return horizontalFieldOfView;
    }

    /**
     * 
     * @return the maximal sight distance of the Panorama, in meters
     */
    public int maxDistance() {

        return maxDistance;
    }

    /**
     * 
     * @return the width of the Panorama, a sample
     */
    public int width() {

        return width;
    }

    /**
     * 
     * @return the height of the Panorama, a sample
     */
    public int height() {

        return height;
    }

    /**
     * 
     * @return the vertical field of view of the Panorama, in radians
     */
    public double verticalFieldOfView() {

        return vericalFieldOfView;
    }

    /**
     * 
     * @return the middle height of the Panorama, a sample
     */
    private double middleHeight() {

        return middleHeight;
    }

    /**
     * 
     * @return the middle width of the Panorama, a sample
     */
    private double middleWidth() {

        return middleWidth;
    }

    /**
     * Calculates the azimuth for a given sample x
     * 
     * @param x the sample
     * @return the azimuth corresponding to the sample x, in radians
     * @throws IllegalArgumentException if the given sample is invalid for this
     *                                  Panorama
     */
    public double azimuthForX(double x) {

        checkArgument(x >= 0 && x <= width - 1, "Invalid sample for this Panorama");

        if (x == middleWidth()) {
            return centerAzimuth;
        } else {
            return canonicalize((x - middleWidth()) * delta + centerAzimuth);
        }
    }

    /**
     * Calculates the sample x for a given azimuth
     * 
     * @param a the azimuth in radians
     * @return the sample corresponding to the azimuth
     * @throws IllegalArgumentException if the given azimuth is invalid for this
     *                                  Panorama
     */
    public double xForAzimuth(double a) {

        double deltaAzimuth = angularDistance(centerAzimuth, a);
        checkArgument(Math.abs(deltaAzimuth) <= horizontalFieldOfView() / 2.0, "Invalid azimuth for this Panorama");

        return (deltaAzimuth) / delta + middleWidth();
    }

    /**
     * Calculates the altitude for a given sample y
     * 
     * @param y the sample
     * @return the altitude corresponding to the sample y, in radians
     * @throws IllegalArgumentException if the given sample is invalid for this
     *                                  Panorama
     */
    public double altitudeForY(double y) {

        checkArgument(y >= 0 && y <= height - 1, "Invalid sample for this panorama");

        if (y == middleHeight()) {
            return 0;
        } else {
            return verticalFieldOfView() / 2.0 - y * delta;
        }
    }

    /**
     * Calculates the sample y for a given altitude
     * 
     * @param a the altitude in radians
     * @return the sample corresponding to the altitude y
     * @throws IllegalArgumentException if the given altitude is invalid for this
     *                                  Panorama
     */
    public double yForAltitude(double a) {

        checkArgument(a >= -verticalFieldOfView() / 2 && a <= verticalFieldOfView() / 2,
                "Invalid altitude for this Panorama");

        if (a == 0) {
            return middleHeight();
        } else {
            return (verticalFieldOfView() / 2.0 - a) / delta;
        }
    }

    /**
     * Checks if a sample index is valid
     * 
     * @param x sample
     * @param y sample
     * @return true if the sample index is valid, false otherwise
     */
    boolean isValidSampleIndex(int x, int y) {

        if (x >= 0 && x <= width - 1 && y >= 0 && y <= height - 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Calculates the linearSampleIndex of 2 points from the Panorama
     * 
     * @param x sample
     * @param y sample
     * @return an integer that corresponds to the linearSampleIndex
     */
    int linearSampleIndex(int x, int y) {

        return width * y + x;
    }
}
