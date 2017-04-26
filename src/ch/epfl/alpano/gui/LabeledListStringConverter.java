package ch.epfl.alpano.gui;
import java.util.ArrayList;
import java.util.List;
import static ch.epfl.alpano.Preconditions.checkArgument;
import javafx.util.StringConverter;

public class LabeledListStringConverter extends StringConverter<Integer> {

    List<String> list;
    
    public LabeledListStringConverter(String...string){
        list = new ArrayList<>();
        for(String s : string){
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
        checkArgument(index<list.size()&& index>=0, "Invalid index");
        return list.get(index);
    }

}
