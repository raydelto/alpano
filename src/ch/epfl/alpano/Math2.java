/**
 * Math2
 * 
 * @author Andrea Scalisi (259183)
 * @author Gerald Sula (257396)
 */
package ch.epfl.alpano;

import static java.lang.Math.PI;

import java.util.function.DoubleUnaryOperator;

public interface Math2 {
    final static double PI2= Math.PI*2;
    /**
     * A method that calculate the square of x
     * @param x the real number to be squared
     * @return the square of x
     */
    static double sq(double x){
        return x*x;
    }
    /**
     * A method that calculate the floor mod of a division
     * @param x numerator of the division
     * @param y denominator of the division
     * @return the floor of the division
     */
    static double floorMod(double x, double y){
        return x-y*Math.floor(x/y);
    }
    /**
     * A method that calculate the haversin of the point x
     * @param x the argument used for the haversin function
     * @return the haversin function applied at the point x
     */
   static double haversin(double x){
       return sq(Math.sin(x/2));
   }
   /**
    * A method that calculate the angular distance between two angles
    * @param a1 angle 1
    * @param a2 angle 2
    * @return the angular distance between a1 and a2
    */
   static double angularDistance(double a1, double a2){
       return floorMod(a2-a1+Math.PI,PI2)-Math.PI;
   }
   
   /**
    * A method that calculate f(x) by linear interpolation
    * @param y0 the value of f(0)
    * @param y1 the value of f(1)
    * @param x variable
    * @return f(x)
    */
   static  double lerp(double y0, double y1, double x){
       return (y1-y0)*x+y0;
   }
   
   /**
    * A method that calculate the value of f(x, y) by bilinear interpolation
    * @param z00 the value of f(0, 0)
    * @param z10 the value of f(1, 0)
    * @param z01 the value of f(0, 1)
    * @param z11 the value of f(1, 1)
    * @param x variable
    * @param y variable
    * @return f(x, y)
    */
   static double bilerp(double z00, double z10, double z01, double z11, double x, double y){
       double z1 = lerp(z00, z10, x);
       double z2 = lerp(z01, z11, x);
       return lerp(z1, z2, y);
   }
   /**
    * 
    * @param f the function to be used in this operation
    * @param minX the lower bound
    * @param maxX the upper bound
    * @param dX the distance used in each step of calculation
    * @return the lower bound once the distance between the two bounds is reduced to less than dx, and Positive infinity if there no root is found
    */
 static double firstIntervalContainingRoot(DoubleUnaryOperator f, double minX, double maxX, double dX){         double low=minX;
           double up=low+dX;
           do{
           if(Math.signum(f.applyAsDouble(low))+Math.signum(f.applyAsDouble(up))==0||Math.signum(f.applyAsDouble(low))*Math.signum(f.applyAsDouble(up))==0)return low;
           low+=dX;
           up+=dX;
           }while(up<=maxX);
           return Double.POSITIVE_INFINITY;
       
        
   }
   
     /**
      * Check if two double have the same sign
      * @param x1 variable 1
      * @param x2 variable 2
      * @return true if x1 and x2 have the same sign, false otherwise
      */
     
    static boolean isSameSign(double x1, double x2){
         
         if((x1>=0 && x2>=0) || (x1<0 && x2<0)){
             return true;
         }
         else{
             return false;
         }
     }
   /**
    * 
    * @param f the function to be used in this operation
    * @param x1 One of the two bounds
    * @param x2 One of the two bounds
    * @param epsilon maximal distance desired between the two bounds
    * @return the lower bound once the distance between the two bounds is reduced to less than epsilon
    */
    static double improveRoot(DoubleUnaryOperator f, double x1, double x2, double epsilon){
        
        Preconditions.checkArgument(!isSameSign(f.applyAsDouble(x1), f.applyAsDouble(x2)));  
        
        if((Math.abs(x1 - x2)<= epsilon )|| (x1 == x2)){
            
            return x1;
        }
        
        else{
            
            double currentMin = x1;
            double currentMax = x2;
            double xM;
            
            do{
                
                xM = (currentMin + currentMax)/2;
                if(f.applyAsDouble(xM)==0){
                    return xM;
                   
                }
                
                else if(!isSameSign(f.applyAsDouble(xM), f.applyAsDouble(currentMax))){
                    
                    currentMin = xM;
                    
                }
                    
                else{
                    
                    currentMax = xM;
                }                
                
            }while(Math.abs(currentMax-currentMin)> epsilon);
            
            return currentMin;
            
        }
        
     

   }

}
