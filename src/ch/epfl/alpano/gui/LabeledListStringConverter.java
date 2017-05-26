/**
 * LabeledListStringConverter
 * 
 * @author Andrea Scalisi (259183)
 * @author Gerald Sula (257396)
 */
package ch.epfl.alpano.gui;

import static ch.epfl.alpano.Preconditions.checkArgument;

import java.util.ArrayList;
import java.util.List;

import javafx.util.StringConverter;

public class LabeledListStringConverter extends StringConverter<Integer> {

    List<String> list;

    /**
     * Creates a new LabeledListStringConverter that stores several Strings in an array
     * @param String list of multiple strings 
     */
    public LabeledListStringConverter(String... string) {

        list = new ArrayList<>();
        for (String s : string) {
            list.add(s);
        }
    }

    @Override
    public Integer fromString(String string) {

        checkArgument(!string.equals(null), "String cannot be null");
        checkArgument(list.contains(string), "Cannot find the string in this array");

        return list.indexOf(string);
    }

    @Override
    public String toString(Integer index) {

        checkArgument(index < list.size() && index >= 0, "Invalid index");

        return list.get(index);
    }
}
