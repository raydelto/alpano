package ch.epfl.alpano;

import java.util.function.DoubleUnaryOperator;

public interface Math2 {
    static double PI2= Math.PI*2;
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
   static double angularDistance(double a1, double a2)
   {
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
   static double firstIntervalContainingRoot(DoubleUnaryOperator f, double minX, double maxX, double dX){
       
       if(isSameSign(f.applyAsDouble(minX), f.applyAsDouble(maxX))){
           return Double.POSITIVE_INFINITY;
       }
       
       else{
           double currentMin = minX;
           double currentMax = minX+dX;
           
           while(isSameSign(f.applyAsDouble(currentMin), f.applyAsDouble(currentMax))){
               currentMin = currentMax;
               currentMax = currentMax+dX;
               
           }
           
           return currentMin;
       }
         
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
   static double improveRoot(DoubleUnaryOperator f, double x1, double x2, double epsilon)
   {
       //if the function applied in any of the initial bounds is already zero, then that bound is returned (the first one if they both are) 
       if(f.applyAsDouble(x1)==0){
           return x1;
       }
       if(f.applyAsDouble(x2)==0){
           return x2;
       }
       
     //checks if the function in the two points has opposite signs
       Preconditions.checkArgument(isSameSign(f.applyAsDouble(x1),(f.applyAsDouble(x2))));
       
       //the lower bound is assigned to a1, the other to a2
       double a1,a2;
       if(x1<=x2){
         a1=x1;
         a2=x2;
        }
       else{
         a1=x2;
         a2=x1;
        }

       while (true)
       {
           if((a2-a1)<=epsilon){
             //if the distance between the two bounds becomes less than epsilon, then the temporary lower bopund is returned
               return a1;
           }
           
           double xTemp=((a1+a2)/2);//in each loop a temporary bound between the actual bounds is created
           double average=f.applyAsDouble(xTemp);
           
           //Check if the function between the two bounds has a different sign
           //If so, assign it to the lower bound
           if(isSameSign(f.applyAsDouble(a1), f.applyAsDouble(average))){
               a2=xTemp;
           }
              
           else {
               a1=xTemp;
           }
           
       }

   }
    

}
