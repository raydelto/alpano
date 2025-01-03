/**
 * FixedPointStringConverter
 * 
 * @author Andrea Scalisi (259183)
 * @author Gerald Sula (257396)
 */
package ch.epfl.alpano.gui;

import static java.math.RoundingMode.HALF_UP;

import java.math.BigDecimal;

import javafx.util.StringConverter;

public class FixedPointStringConverter extends StringConverter<Integer> {

    private int decimal;

    /**
     * Creates a new FixedPointStringConverter
     * 
     * @param decimal integer that indicates the number of decimal to be saved
     */
    public FixedPointStringConverter(int decimal) {

        this.decimal = decimal;
    }

    @Override
    public Integer fromString(String arg0) {

        BigDecimal b = new BigDecimal(arg0);
        b = b.movePointRight(decimal);
        b = b.setScale(0, HALF_UP);

        return b.intValueExact();
    }

    @Override
    public String toString(Integer arg0) {

        BigDecimal b = new BigDecimal(arg0);
        b = b.movePointLeft(decimal);

        return b.toPlainString();
    }
}
