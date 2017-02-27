package ch.epfl.alpano;

public class test {

    public static void main(String[] args) {
        Interval1D interval1d=new Interval1D(0, 5);
        Interval1D interval2d=new Interval1D(1, 6);
        Interval1D interval3d=new Interval1D(52, 53);
        Interval1D interval4d=new Interval1D(-2, 4);
        Interval1D interval5d=new Interval1D(0, 5);
        System.out.println(interval4d.size());
        System.out.println(interval1d.isUnionableWith(interval3d));
        System.out.println(interval1d.sizeOfIntersectionWith(interval4d));
        System.out.println(interval1d.union(interval4d).size());
        //System.out.println(interval1d.boundingUnion(interval3d).size());
        System.out.println(interval1d.equals(interval5d));
        System.out.println(interval4d);
        System.out.println("-----------");
        Interval2D bi1=new Interval2D(interval1d, interval2d);
        Interval2D bi2=new Interval2D(interval2d, interval4d);
        Interval2D bi3=new Interval2D(interval2d, interval3d);
        Interval2D bi4=new Interval2D(interval1d, interval2d);
        System.out.println(bi1);
        
        System.out.println(bi1.size());
        
        System.out.println(bi1.sizeOfIntersectionWith(bi4));
        System.out.println(bi1.union(bi2));
        System.out.println(bi1.equals(bi3));

    }
}