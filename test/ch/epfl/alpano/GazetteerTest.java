package ch.epfl.alpano;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import ch.epfl.alpano.summit.GazetteerParser;

public class GazetteerTest {

    @Test
    public void goodTxtFile() throws IOException {
     GazetteerParser a=new GazetteerParser();
     a.readSummitsFrom(new File("alps.txt"));

    }
    @Test(expected = IOException.class)
    public void emptyFile() throws IOException {
     GazetteerParser a=new GazetteerParser();
     a.readSummitsFrom(new File("testAlps1.txt"));

    }
    @Test(expected = IOException.class)
    public void randomTxtFile() throws IOException {
     GazetteerParser a=new GazetteerParser();
     a.readSummitsFrom(new File("testAlps2.txt"));

    }
    @Test
    public void goodTxtFileWithMultipleSpaces() throws IOException {
     GazetteerParser a=new GazetteerParser();
     a.readSummitsFrom(new File("testAlps3.txt"));

    }
    @Test(expected = IOException.class)
    public void notEnoughIntermediaryElements() throws IOException {
     GazetteerParser a=new GazetteerParser();
     a.readSummitsFrom(new File("testAlps4.txt"));

    }
    @Test()
    public void negativeAngles() throws IOException {
     GazetteerParser a=new GazetteerParser();
     a.readSummitsFrom(new File("testAlps5.txt"));

    }

}
