/**
 * Interval1D
 * 
 * @author Andrea Scalisi (259183)
 * @author Gerald Sula (257396)
 */
package ch.epfl.alpano;

import static ch.epfl.alpano.Preconditions.checkArgument;
import static java.util.Arrays.sort;
import static java.util.Objects.hash;

public final class Interval1D {
    private final int includedFrom, includedTo;

    /**
     * Creates a uni-dimensional interval, throws IllegalArgumentException is
     * includeTo is strictly smaller than Includedfrom
     * 
     * @param includedFrom lower bound of the interval
     * @param includedTo   upper bound of the interval
     * @throws IllegalArgumentException if includedFrom is bigger than includedTo
     */
    public Interval1D(int includedFrom, int includedTo) {

        checkArgument(!(includedTo < includedFrom), "Problem with the bounds");

        this.includedFrom = includedFrom;
        this.includedTo = includedTo;
    }

    /**
     * 
     * @return the lower bound of the interval
     */
    public int includedFrom() {

        return includedFrom;
    }

    /**
     * 
     * @return returns the upper bound of the interval
     */
    public int includedTo() {

        return includedTo;
    }

    /**
     * Size of the interval
     * 
     * @return the size of the interval
     */
    public int size() {

        return includedTo - includedFrom + 1;
    }

    /**
     * Checks if the element v is contained in the interval
     * 
     * @param v the element to be checked
     * @return true if the element is contained in the interval
     */
    public boolean contains(int v) {

        return (v >= includedFrom() && v <= includedTo());
    }

    /**
     * Calculates the size of a intersection between two intervals, return 0 if the
     * two don't intersect
     * 
     * @param that the second interval
     * @return the size of the intersection
     */
    public int sizeOfIntersectionWith(Interval1D that) {

        if (!this.isContinuous(that)) {
            return 0;
        }

        return Math.min(this.includedTo, that.includedTo) - Math.max(this.includedFrom, that.includedFrom) + 1;
    }

    /**
     * Checks if the union of two intervals is possible
     * 
     * @param that the second interval
     * @return
     */
    public boolean isUnionableWith(Interval1D that) {

        return ((this.size() + that.size() - this.sizeOfIntersectionWith(that)) == this.boundingUnion(that).size());
    }

    /**
     * Checks if two intervals are continuous (meaning that they have at least one
     * point in common)
     * 
     * @param that the second interval
     * @return
     */
    private boolean isContinuous(Interval1D that) {

        return (this.includedFrom() <= that.includedTo() && this.includedTo() >= that.includedFrom());
    }

    /**
     * Unites two intervals into one
     * 
     * @param that the second interval
     * @return a new interval that unites two intervals into one
     * @throws throws IllegalArgumentException if the intervals are not unionable
     */
    public Interval1D union(Interval1D that) {

        checkArgument(this.isUnionableWith(that), "The intervals are not unionable");

        int iArr[] = { this.includedTo(), that.includedFrom(), that.includedTo(), this.includedFrom() };
        sort(iArr);

        return new Interval1D(iArr[0], iArr[3]);

    }

    /**
     * Unites inclusively (englobante) two intervals into one
     * 
     * @param that the second interval
     * @return a new interval that unites two intervals into one
     */
    public Interval1D boundingUnion(Interval1D that) {

        int low, up;

        if (this.includedFrom() <= that.includedFrom()) {
            // assign the smallest of the 4 total bounds to 'low', and the biggest to 'up'
            low = this.includedFrom();
        }

        else
            low = that.includedFrom();

        if (this.includedTo() >= that.includedTo()) {
            up = this.includedTo();
        }

        else
            up = that.includedTo();

        return new Interval1D(low, up);// create an interval using the two minimal/maximal bounds
    }

    @Override
    public boolean equals(Object thatO) {

        if (thatO == null) {
            return false;
        }

        if (thatO.getClass() != this.getClass()) {
            return false;
        }
        Interval1D that = (Interval1D) thatO;

        return (this.includedTo() == that.includedTo() && this.includedFrom() == that.includedFrom());
    }

    /**
     * Redefinition of the method toString from Object
     * 
     * @return a String that indicates the lower and the upper bound of the interval
     */
    @Override
    public String toString() {

        return "[" + this.includedFrom + ".." + this.includedTo + "]";
    }

    @Override
    public int hashCode() {

        return hash(includedFrom(), includedTo());
    }
}
