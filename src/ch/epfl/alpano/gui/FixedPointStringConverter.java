package ch.epfl.alpano.gui;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javafx.util.StringConverter;

public class FixedPointStringConverter extends StringConverter<Integer>  {

    private int decimal;
    
    public FixedPointStringConverter(int decimal){
        this.decimal = decimal;
    }
    @Override
    public Integer fromString(String arg0) {
        BigDecimal b = new BigDecimal(arg0);
        b = b.movePointRight(decimal);
        b = b.setScale(0, RoundingMode.HALF_UP);
       
        return b.intValueExact();
    }

    @Override
    public String toString(Integer arg0) {
        BigDecimal b = new BigDecimal(arg0);
        b = b.movePointLeft(decimal);
      
        return b.toPlainString();
    }
    
    public static void main(String[] args) {
        FixedPointStringConverter f = new FixedPointStringConverter(4);
        String s = "12.354364";
        System.out.println("Voila : " +f.fromString(s));
        
        System.out.println("Retour : "+f.toString(f.fromString(s)));
    }

}
