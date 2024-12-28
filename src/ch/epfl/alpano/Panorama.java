/**
 * Panorama
 * 
 * @author Andrea Scalisi (259183)
 * @author Gerald Sula (257396)
 */

package ch.epfl.alpano;

import static java.util.Arrays.fill;
import static java.util.Objects.requireNonNull;

public final class Panorama {

    private final PanoramaParameters parameters;
    private final float[] distance, longitude, latitude, elevation, slope;

    /**
     * Creates anew Panorama
     * @param parameters an array of float where the parameters of the Panorama are stored
     * @param distance an array of float where the distance of the Panorama are stored
     * @param longitude an array of float where the longitude of the Panorama are stored
     * @param latitude an array of float where the latitude of the Panorama are stored
     * @param elevation an array of float where  the elevation of the Panorama are stored
     * @param slope an array of float where the slope of the Panorama are stored
     */
    private Panorama(PanoramaParameters parameters, float[] distance, float[] longitude, float[] latitude, float[] elevation, float[] slope) {
        
        this.parameters = parameters;
        this.distance = distance;
        this.longitude = longitude;
        this.latitude = latitude;
        this.elevation = elevation;
        this.slope = slope;
    }

    /**
     * 
     * @return return the PanoramaParameters used to construct this Panorama
     */
    public PanoramaParameters parameters() {
        
        return parameters;
    }

    /**
     * 
     * @param x x coordinate of the sample
     * @param y y coordinate of the sample
     * @return the horizontal distance from the ObserverPosition to the index x, y, in meters
     * @throws IndexOutOfBoundsException if the sample is not valid
     * 
     */
    public float distanceAt(int x, int y) {
        
        checkIndex(x, y);
        return distance[parameters.linearSampleIndex(x, y)];
    }

    /**
     * 
     * @param x x coordinate of the sample
     * @param y y coordinate of the sample
     * @param d default distance in meters
     * @return the horizontal distance from the ObserverPosition to the index x, y 
     *            or the default distance d if the sample is not valid, in meters
     * 
     */
    public float distanceAt(int x, int y, float d) {
        
        if (!(parameters.isValidSampleIndex(x, y))) {
            return d;
        } else {
            return distance[parameters.linearSampleIndex(x, y)];
        }
    }

    /**
     * 
     * @param x x coordinate of the sample
     * @param y y coordinate of the sample
     * @return the longitude of the Panorama at the index x, y, in radians
     * @throws IndexOutOfBoundsException if the sample is not valid
     * 
     */
    public float longitudeAt(int x, int y) {
        
        checkIndex(x, y);
        
        return longitude[parameters.linearSampleIndex(x, y)];
    }

    /**
     * 
     * @param x x coordinate of the sample
     * @param y y coordinate of the sample
     * @return the latitude of the Panorama at the index x, y, in radians
     * @throws IndexOutOfBoundsException if the sample is not valid
     * 
     */
    public float latitudeAt(int x, int y) {
        
        checkIndex(x, y);
        
        return latitude[parameters.linearSampleIndex(x, y)];
    }

    /**
     * 
     * @param x x coordinate of the sample
     * @param y y coordinate of the sample
     * @return the elevation of the Panorama at the index x, y, in meters
     * @throws IndexOutOfBoundsException if the sample is not valid
     * 
     */
    public float elevationAt(int x, int y) {
        
        checkIndex(x, y);
        
        return elevation[parameters.linearSampleIndex(x, y)];
    }

    /**
     * 
     * @param x x coordinate of the sample
     * @param y y coordinate of the sample
     * @return the slope of the Panorama at the index x, y, in radians
     * @throws IndexOutOfBoundsException if the sample is not valid
     * 
     */
    public float slopeAt(int x, int y) {
        
        checkIndex(x, y);
        
        return slope[parameters.linearSampleIndex(x, y)];
    }

    /**
     * Checks the index of a sample
     * @param x x coordinate of the sample
     * @param y y coordinate of the sample
     * @throws IndexOutOfBoundsException if the sample is not valid
     */
    private void checkIndex(int x, int y) {
        
        if (!(parameters.isValidSampleIndex(x, y))) {
            throw new IndexOutOfBoundsException("Invalid index");
        }
    }

    public static final class Builder {
        
        private final PanoramaParameters parameters;
        private float[] distance, longitude, latitude, elevation, slope;
        private boolean called = false;

        /**
         * Creates a Panorama Builder
         * @param parameters the parameters of the Panorama (non null)
         * @throws NullPointerException if parameters is null
         */
        public Builder(PanoramaParameters parameters) {
            
            this.parameters = requireNonNull(parameters, "Null PanoramaParameters");
            int size = parameters.height()*parameters.width();
            distance = new float[size];
            longitude = new float[size];
            latitude = new float[size];
            elevation = new float[size];
            slope = new float[size];
            fill(distance, Float.POSITIVE_INFINITY);
        }

        /**
         * 
         * @param x x coordinate of the sample
         * @param y y coordinate of the sample
         * @param distance the distance to be set at the sample in meters
         * @return the current builder
         * @throws IllegalStateException if the build() method has already been called for this Builder
         * @throws IndexOutOfBoundsException if the sample is not valid
         * 
         */
        public Builder setDistanceAt(int x, int y, float distance) {
            
            checkIndex(x, y);
            
            if (called) {
                throw new IllegalStateException("This builder has already been built and cannot be modified");
            }
            
            this.distance[parameters.linearSampleIndex(x, y)] = distance;
            
            return this;
        }

        /**
         * 
         * @param x x coordinate of the sample
         * @param y y coordinate of the sample
         * @param longitude the longitude to be set at the sample in radians
         * @return the current builder
         * @throws IllegalStateException if the build() method has already been called for this Builder
         * @throws IndexOutOfBoundsException if the sample is not valid
         * 
         */
        public Builder setLongitudeAt(int x, int y, float longitude) {
            
            checkIndex(x, y);
            
            if (called){
                throw new IllegalStateException("This builder has already been built and cannot be modified");
            }
            
            this.longitude[parameters.linearSampleIndex(x, y)] = longitude;
            
            return this;
        }

        /**
         * 
         * @param x x coordinate of the sample
         * @param y y coordinate of the sample
         * @param latitude the latitude to be set at the sample in radians
         * @return the current builder
         * @throws IllegalStateException if the build() method has already been called for this Builder
         * @throws IndexOutOfBoundsException if the sample is not valid
         * 
         */
        public Builder setLatitudeAt(int x, int y, float latitude) {
            
            checkIndex(x, y);
            
            if (called) {
                throw new IllegalStateException("This builder has already been built and cannot be modified");
            }
            
            this.latitude[parameters.linearSampleIndex(x, y)] = latitude;
            
            return this;
        }

        /**
         * 
         * @param x x coordinate of the sample
         * @param y y coordinate of the sample
         * @param elevation the elevation to be set at the sample in meters
         * @return the current builder
         * @throws IllegalStateException if the build() method has already been called for this Builder
         * @throws IndexOutOfBoundsException if the sample is not valid
         * 
         */
        public Builder setElevationAt(int x, int y, float elevation) {
            
            checkIndex(x, y);
            
            if (called){
                throw new IllegalStateException("This builder has already been built and cannot be modified");
            }
            
            this.elevation[parameters.linearSampleIndex(x, y)] = elevation;
            
            return this;
        }

        /**
         * 
         * @param x x coordinate of the sample
         * @param y y coordinate of the sample
         * @param slope the slope to be set at the sample in radians
         * @return the current builder
         * @throws IllegalStateException if the build() method has already been called for this Builder
         * @throws IndexOutOfBoundsException if the sample is not valid
         * 
         */
        public Builder setSlopeAt(int x, int y, float slope) {
            
            checkIndex(x, y);
            
            if (called){
                throw new IllegalStateException("This builder has already been built and cannot be modified");
            }
            
            this.slope[parameters.linearSampleIndex(x, y)] = slope;
            
            return this;
        }

        /**
         * checks the index of a sample
         * @param x x coordinate of the sample
         * @param y y coordinate of the sample
         * @throws IndexOutOfBoundsException if the sample is not valid
         */
        private void checkIndex(int x, int y) {
            
            if (!(parameters.isValidSampleIndex(x, y))) {
                throw new IndexOutOfBoundsException("This builder has already been built and cannot be modified");
            }
        }

        /**
         * Builds a Panorama from the current builder
         * @return the new Panorama built from the current builder
         * @throws IllegalStateException if the build method has already been called on the current PanoramaBuider
         */
        public Panorama build() {
            
            if (called){
                throw new IllegalStateException("The method build() has already been called for this builder");
            }
            
            called = true;
            
            return new Panorama(parameters, distance, longitude, latitude, elevation, slope);
        }
    }
}
