/**
 * Interval1D
 * 
 * @author Andrea Scalisi (259183)
 * @author Gerald Sula (257396)
 */
package ch.epfl.alpano;


import java.util.Arrays;
import java.util.Objects;

public final class Interval1D {
    private  int includedFrom,  includedTo;
    
    /**
     * Creates a uni-dimensional interval, throws IllegalArgumentException is includeTo is strictly smaller than Includedfrom
     * @param includedFrom lower bound of the interval
     * @param includedTo upper bound of the interval
     */
    public Interval1D(int includedFrom, int includedTo){ 
        Preconditions.checkArgument(!(includedTo<includedFrom));
        this.includedFrom=includedFrom;
        this.includedTo=includedTo;
    }
    
    /**
     * 
     * @return returns the lower bound of the interval
     */
    public int includedFrom(){
        return includedFrom;
    }
    
    /**
     * 
     * @return returns the upper bound of the interval
     */
    public int includedTo(){
        return includedTo;
    }
    
    /**
     * size of the interval
     * @return returns the size of the interval
     */
    public int size(){
        return includedTo-includedFrom+1;
    }
    
    /**
     * checks if the element v is contained in the interval
     * @param v the element to be checked
     * @return returns true if the element is contained in the interval
     */
    public boolean contains(int v){
        return (v>=includedFrom()&&v<=includedTo());
    }
    
    /**
     * Calculates the size of a intersection between two intervals, return 0 if the two don't intersect
     * @param that the second interval
     * @return the size of the intersection
     */
    public int sizeOfIntersectionWith(Interval1D that){
        if(!this.isContinuous(that)) return 0;
        int iArr[] = {this.includedTo(),that.includedFrom(),that.includedTo(),this.includedFrom()};
        Arrays.sort(iArr);
        return iArr[2]-iArr[1]+1; //to calculate the size if the intersection, after making sure that the intersections are unionable,
                                //substract the 2° biggest bound to the 3° biggest bound to calculate the size
        
    }
    
    /**
     * Checks if the union of two intervals is possible
     * @param that the second interval
     * @return
     */
    public boolean isUnionableWith(Interval1D that){
        return((this.size()+that.size()-this.sizeOfIntersectionWith(that))==this.boundingUnion(that).size());
    }
    
    /**
     * Checks if two intervals are continuous (meaning that they have at least one point in common)
     * @param that the second interval
     * @return
     */
    private boolean isContinuous(Interval1D that){
        return(this.includedFrom()<=that.includedTo()&&this.includedTo()>=that.includedFrom());
    }
    
    /**
     * Unites two intervals into one
     * @param that the second interval
     * @return a new interval that unites two intervals into one
     * @throws throws IllegalArgumentException if the intervals are not unionizable
     */
    public Interval1D union(Interval1D that){
        Preconditions.checkArgument(this.isUnionableWith(that));
        int iArr[] = {this.includedTo(),that.includedFrom(),that.includedTo(),this.includedFrom()};
        Arrays.sort(iArr);
        return new Interval1D(iArr[0], iArr[3]);
        
    }
    
    /**
     * Unites inclusively (englobante) two intervals into one
     * @param that the second interval
     * @return a new interval that unites two intervals into one
     */
    public Interval1D boundingUnion(Interval1D that){
        int low, up;
        if(this.includedFrom()<=that.includedFrom()) //assign the smallest of the 4 total bounds to 'low', and the biggest to 'up'
            {
            low=this.includedFrom();
            }
        else low=that.includedFrom();
        
        if(this.includedTo()>=that.includedTo()) 
        {
        up=this.includedTo();
        }
        else up=that.includedTo();
        
        return new Interval1D(low,up);//create an interval using the two minimal/maximal bounds
    }
    
   
    @Override
    public boolean equals(Object thatO){
        if(thatO==null)return false;
        if(thatO.getClass()!=this.getClass())return false;
        
        Interval1D that= (Interval1D)thatO;
        if(this.hashCode()!=that.hashCode()) return false;
        
        return(this.includedTo()==that.includedTo()&&this.includedFrom()==that.includedFrom());
    }
    /**
     * Redefinition of the method toString from Object
     * @return a String that indicates the lower and the upper bound of the interval
     */
    @Override
    public String toString(){
        return "["+this.includedFrom+".."+this.includedTo+"]";
    }
    
    @Override
    public int hashCode() {
      return Objects.hash(includedFrom(), includedTo());
    }
    

}
