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
    @Test(expected = IOException.class)
    public void wrongLangitude1() throws IOException {
     GazetteerParser a=new GazetteerParser();
     a.readSummitsFrom(new File("testAlps6.txt"));

    }
    @Test(expected = IOException.class)
    public void wrongLangitude2() throws IOException {
     GazetteerParser a=new GazetteerParser();
     a.readSummitsFrom(new File("testAlps7.txt"));

    }
    @Test(expected = IOException.class)
    public void wrongLatitude1() throws IOException {
     GazetteerParser a=new GazetteerParser();
     a.readSummitsFrom(new File("testAlps8.txt"));

    }
    @Test(expected = IOException.class)
    public void wrongLatitude2() throws IOException {
     GazetteerParser a=new GazetteerParser();
     a.readSummitsFrom(new File("testAlps9.txt"));

    }
    @Test(expected = IOException.class)
    public void noSplit() throws IOException {
     GazetteerParser a=new GazetteerParser();
     a.readSummitsFrom(new File("testAlps10.txt"));

    }
    @Test(expected = IOException.class)
    public void wrongMinute1() throws IOException {
     GazetteerParser a=new GazetteerParser();
     a.readSummitsFrom(new File("testAlps11.txt"));

    }
    @Test(expected = IOException.class)
    public void wrongMinute2() throws IOException {
     GazetteerParser a=new GazetteerParser();
     a.readSummitsFrom(new File("testAlps12.txt"));

    }
    @Test(expected = IOException.class)
    public void wrongNumberFormat1() throws IOException {
     GazetteerParser a=new GazetteerParser();
     a.readSummitsFrom(new File("testAlps13.txt"));

    }
    @Test(expected = IOException.class)
    public void wrongNumberFormat2() throws IOException {
     GazetteerParser a=new GazetteerParser();
     a.readSummitsFrom(new File("testAlps14.txt"));

    }
    @Test()
    public void noLineBreak() throws IOException {
     GazetteerParser a=new GazetteerParser();
     a.readSummitsFrom(new File("testAlps15.txt"));

    }

}
