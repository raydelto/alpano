package ch.epfl.alpano;

import java.util.function.DoubleUnaryOperator;

public interface Math2 {
    static double PI2= Math.PI*2;
    /**
     * 
     * @param x the real number to be squared
     * @return the square of x
     */
    static double sq(double x)
    {
        return x*x;
        
    }
    /**
     * 
     * @param x numerator of the division
     * @param y denominator of the division
     * @return the floor of the division
     */
    static double floorMod(double x, double y)
    {
        return x-y*Math.floor(x/y);
    }
    /**
     * 
     * @param x the argument used for the haversin function
     * @return the haversin function applied at the point x
     */
   static double haversin(double x)
   {
       return sq(Math.sin(x/2));
   }
   ///////////////////////?
   static double angularDistance(double a1, double a2)
   {
       return floorMod(a2-a1+Math.PI,PI2)-Math.PI;
   }
   static  double lerp(double y0, double y1, double x)
   {
       return (y1-y0)*x+y0;
   }
   static double bilerp(double z00, double z10, double z01, double z11, double x, double y)
   {
       double f1=(1-x)*z00+x*z10;
       double f2=(1-x)*z01+x*z11;
       return (1-y)*f1+y*f2;
   }
   /**
    * 
    * @param f the function to be used in this operation
    * @param minX the lower bound
    * @param maxX the upper bound
    * @param dX the distance used in each step of calculation
    * @return the lower bound once the distance between the two bounds is reduced to less than dx, and Positive infinity if there no root is found
    */
   static double firstIntervalContainingRoot(DoubleUnaryOperator f, double minX, double maxX, double dX)
   {
       double delta=0;//delta counts how many times the loop is trod
       while (true)
       {
           //in every loop the function is applied in the new bounds
           double a0=f.applyAsDouble(minX+delta*dX);
           double a1=f.applyAsDouble(minX+dX*(delta+1));
           
           //if the distance between the two bounds becomes smaller than dx, then Positive infinity is returned
           if(minX+dX*(delta+1)>maxX)return Double.POSITIVE_INFINITY;
       
           if(Math.signum(a0)+Math.signum(a1)==0)//if the signs of the function applied in the two bounds are opposite then the temporary lower bound is returned
           {
               return minX+delta*dX;
           }
           else {
            delta++;
        }
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
       if(f.applyAsDouble(x1)==0)return x1;
       if(f.applyAsDouble(x2)==0)return x2;
       Preconditions.checkArgument(Math.signum(f.applyAsDouble(x1))+Math.signum(f.applyAsDouble(x2))==0);//checks if the function in the two points has opposite signs
       
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
           if((a2-a1)<=epsilon)return a1;//if the distance between the two bounds becomes less than epsilon, then the temporary lower bopund is returned
           
           double xTemp=(a1+a2)/2;//in each loop a temporary bound between the actual bounds is created
           double average=f.applyAsDouble(xTemp);
           
           if(Math.signum(f.applyAsDouble(a1))+Math.signum(f.applyAsDouble(average))==0)a2=xTemp;//MAROJE
           else a1=xTemp;
           
       }

   }
    

}
