/**
 * Interval2D
 * 
 * @author Andrea Scalisi (259183)
 * @author Gerald Sula (257396)
 */
package ch.epfl.alpano;

import java.util.Objects;

public final class Interval2D 
{
    private final Interval1D iX,iY;
    
    /**
     * Creates the cartesian product of two uni-dimensional intervals
     * @param iX the first interval
     * @param iY the second interval
     * @throws throws NullPointerException id either iX or iY are null
     */
    public Interval2D(Interval1D iX, Interval1D iY){
        
        this.iX=Objects.requireNonNull(iX,"null x coordinate");
        this.iY=Objects.requireNonNull(iY,"null y coordinate");     
    }
    
    /**
     *  Returns the first uni-dimensional interval
     * @return the first interval
     */
    public Interval1D iX(){
        return iX;
    }
    
    /**
     Returns the second uni-dimensional interval
     * @return the second interval
     */
    public Interval1D iY(){
        return iY;
    }
    
    /**
     * Checks if the bi-dimensional interval contains the pair (x,y)
     * @param x the first element to be checked
     * @param y the second element to be checked
     * @return true if the pair is contained in the bi-dimensional interval contains the pair (x,y)
     */
    public boolean contains(int x, int y){
        return (iX().contains(x)&&iY().contains(y));
    }
    /**
     * Returns the size of the bi-dimensional interval
     * @return the size of the bi-dimensional interval
     */
    public int size(){
        return iX().size()*iY().size();
    }
    
    /**
     * Checks if two bi-dimenional intervals are unionable
     * @param that the second bi-dimensional interval
     * @return true if the two bi-dimenional intervals are unionable
     */
    public boolean isUnionableWith(Interval2D that){
        //First case, the first pair of intervals is unionable, an the second pair is the same
        if(this.iX().isUnionableWith(that.iX())&&this.iY().equals(that.iY()))return true;
        
        //Second case, the second pair of intervals is unionable, an the first pair is the same

        if(this.iY().isUnionableWith(that.iY())&&this.iX().equals(that.iX()))return true;
        
        //Third case, one of the bi-dimensional intervals contains the other
        Interval2D a=this;
        Interval2D b =that;
        int c=0;
        do{
        if(a.iX().contains(b.iX().includedFrom())&&a.iX().contains(b.iX().includedTo())
                &&a.iY().contains(b.iY().includedFrom())&&a.iY().contains(b.iY().includedTo())) return true;
        c++;
        a=that;
        b=this;
        }while(c<=1);
        
       
        return false;
    }
    
    /**
     * Calculates the size of a intersection between two bi-dimensional intervals
     * @param that the second  bi-dimensional interval
     * @return the size of a intersection between two bi-dimensional intervals
     */
    public int sizeOfIntersectionWith(Interval2D that){
        return this.iX().sizeOfIntersectionWith(that.iX())*this.iY().sizeOfIntersectionWith(that.iY());
    }
    
    /**
     * Unites inclusively (englobante) two bi-dimensional intervals into one
     * @param that that the second bi-dimensional interval
     * @return a new bi-dimensional interval that unites two bi-dimensional intervals into one
     */
    public Interval2D boundingUnion(Interval2D that){
        return new Interval2D(this.iX().boundingUnion(that.iX()), this.iY().boundingUnion(that.iY()));
    }
    
    /**
     * Unites two bi-dimensional intervals into one
     * @param that that that the second bi-dimensional interval
     * @return a new bi-dimensional interval that unites two bi-dimensional intervals into one
     * @throws throws IllegalArgumentException if either of the two pairs of uni-dimensional intervals 
     *         that compose the two bi-dimensional intervals are not unionizable
     */
    public Interval2D union(Interval2D that){
        if(!(this.isUnionableWith(that))){
            throw new IllegalArgumentException();
        }
        return new Interval2D(this.iX().union(that.iX()), this.iY().union(that.iY()));
    }
    
    /**
     * Redefinition of the method equals from Object
     * @return true if the two objects are both intervals, have the same hash code, same bounds, and the second one is not null
     */
    @Override
    public boolean equals(Object thatO){
        if(thatO==null)return false;
        if(thatO.getClass()!=this.getClass())return false;
        Interval2D that= (Interval2D)thatO;  
        return (this.iX().equals(that.iX())&&this.iY().equals(that.iY()));
    }
    
    /**
     * Redefinition of the method toString from Object
     * @return a String that indicates the lower and the upper bounds of the bi-dimensional interval
     */
    @Override
    public String toString(){
        return this.iX().toString()+"x"+this.iY().toString();
    }
    
    /**
     * Redefinition of the method hashCode 
     * @return returns a new hash-code created using the first pair and second of bounds of the bi-dimensional interval
     */
    @Override
    public int hashCode() {
      return Objects.hash(this.iX(), this.iY());
    }

}
