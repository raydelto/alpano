package ch.epfl.alpano;

import java.util.Arrays;
import java.util.Objects;

public final class Panorama {

    private final PanoramaParameters parameters;
    private final float[] distance, longitude, latitude, elevation, slope;

    private Panorama(PanoramaParameters parameters, float[] distance,
            float[] longitude, float[] latitude, float[] elevation,
            float[] slope) {
        this.parameters = parameters;
        this.distance = distance;
        this.longitude = longitude;
        this.latitude = latitude;
        this.elevation = elevation;
        this.slope = slope;
    }

    private PanoramaParameters parameters() {
        return parameters;
    }

    private float distanceAt(int x, int y) {
        checkIndex(x, y);
        return distance[parameters.linearSampleIndex(x, y)];
    }

    private float distanceAt(int x, int y, int d) {
        if (!(parameters.isValidSampleIndex(x, y))) {
            return d;
        } else {
            return distance[parameters.linearSampleIndex(x, y)];
        }
    }

    private float longitudeAt(int x, int y) {
        checkIndex(x, y);
        return longitude[parameters.linearSampleIndex(x, y)];
    }

    private float latitudeAt(int x, int y) {
        checkIndex(x, y);
        return latitude[parameters.linearSampleIndex(x, y)];
    }

    private float elevationAt(int x, int y) {
        checkIndex(x, y);
        return elevation[parameters.linearSampleIndex(x, y)];
    }

    private float slopeAt(int x, int y) {
        checkIndex(x, y);
        return slope[parameters.linearSampleIndex(x, y)];
    }

    private void checkIndex(int x, int y) {
        if (!(parameters.isValidSampleIndex(x, y))) {
            throw new IndexOutOfBoundsException();
        }
    }

    public static final class Builder {
        private final PanoramaParameters parameters;
        private float[] distance, longitude, latitude, elevation, slope;
        private boolean called = false;

        public Builder(PanoramaParameters parameters) {
            this.parameters = Objects.requireNonNull(parameters);
            Arrays.fill(distance, Float.POSITIVE_INFINITY);
        }

        // qe te gjitha kto dun check if included in bounds?
        public Builder setDistanceAt(int x, int y, float distance) {
            if (called) throw new IllegalStateException();
            this.distance[parameters.linearSampleIndex(x, y)] = distance;
            return this;
        }

        public Builder setLongitudeAt(int x, int y, float longitude) {
            if (called) throw new IllegalStateException();
            this.longitude[parameters.linearSampleIndex(x, y)] = longitude;
            return this;
        }

        public Builder setLatitudeAt(int x, int y, float latitude) {
            if (called) throw new IllegalStateException();
            this.latitude[parameters.linearSampleIndex(x, y)] = latitude;
            return this;
        }

        public Builder setElevationAt(int x, int y, float elevation) {

            if (called) throw new IllegalStateException();
            this.elevation[parameters.linearSampleIndex(x, y)] = elevation;
            return this;
        }

        public Builder setSlopeAt(int x, int y, float slope) {

            if (called) throw new IllegalStateException();
            this.slope[parameters.linearSampleIndex(x, y)] = slope;
            return this;
        }

        private void checkIndex(int x, int y) {
            if (!(parameters.isValidSampleIndex(x, y))) {
                throw new IndexOutOfBoundsException();
            }
        }

        public Panorama build() {
            if (called) throw new IllegalStateException();
            called = true;
            return new Panorama(parameters, distance, longitude, latitude,
                    elevation, slope);
        }
    }
}
