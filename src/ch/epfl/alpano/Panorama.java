package ch.epfl.alpano;

public final class Panorama {

    private final PanoramaParameters parameters;
    private final float[] distance, longitude, latitude, elevation, slope;
    private Panorama(PanoramaParameters parameters, float[] distance, float[] longitude, float[] latitude, float[] elevation, float[] slope){
        this.parameters= parameters;
        this.distance = distance;
        this.longitude=longitude;
        this.latitude=latitude;
        this.elevation=elevation;
        this.slope = slope;
    }
    
    private PanoramaParameters parameters(){
        return parameters;
    }
    
    private float distanceAt(int x, int y){
        checkIndex(x, y);
        return distance[parameters.linearSampleIndex(x, y)];
    }
    
    private float distanceAt(int x, int y, int d){
        if(!(parameters.isValidSampleIndex(x, y))){
            return d;
        }
        else{
            return distance[parameters.linearSampleIndex(x, y)];
        }
    }
    
    private float longitudeAt(int x, int y){
        checkIndex(x, y);
        return longitude[parameters.linearSampleIndex(x, y)];
    }
    
    private float latitudeAt(int x, int y){
        checkIndex(x, y);
        return latitude[parameters.linearSampleIndex(x, y)];
    }
    
    private float elevationAt(int x, int y){
        checkIndex(x, y);
        return elevation[parameters.linearSampleIndex(x, y)];
    }
    
    private float slopeAt(int x, int y){
        checkIndex(x, y);
        return slope[parameters.linearSampleIndex(x, y)];
    }
    
    private void checkIndex(int x, int y){
        if(!(parameters.isValidSampleIndex(x, y))){
            throw new IndexOutOfBoundsException();
        }
    }
}
