package Tests;


import Exceptions.NoSuchElementException;
import Model.TVShow;
import ShowList.ShowList;
import org.testng.annotations.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.*;


public class ShowListTest {

    @Test
    public void insertionCheck() {
        ShowList lst = new ShowList();
        String showId = "VNCGN";
        String showName = "Scoobydobido";
        double startTime = 23.00;
        double endTime = 27.34;
        TVShow show = new TVShow(showId, showName, startTime , endTime);
        lst.addToStart(show);
        int size = lst.getSize();
        ShowList.ShowNode node = lst.getAtIndex(0);
        assertEquals(size, 1);
        assertEquals(node.getShow().getShowId(), showId);
    }

    @Test void testDeletionFromStart() throws CloneNotSupportedException {
        ShowList lst = new ShowList();
        String showId = "VNCGN";
        String showName = "Scoobydobido";
        double startTime = 23.00;
        double endTime = 27.34;
        TVShow show = new TVShow(showId, showName, startTime , endTime);
        lst.addToStart(show);
        // this will test the clone method also.
        TVShow show2 = (TVShow) show.clone();
        show2.setShowId("327912");
        lst.addToStart(show2);
        assertEquals(lst.getSize(), 2);
        // this must delete with the id 327912;
        lst.deleteFromStart();
        assertEquals(lst.getSize(), 1);
        assertEquals(lst.getAtIndex(0).getShow().getShowId(), showId);
    }

    @Test void testInvalidIndexDeletion() {
        // deletion on empty list
        ShowList lst = new ShowList();
        // there is no such index as the elements does not exist.
        boolean deletion = lst.deleteFromIndex(12);
        assertFalse(deletion);
    }

    @Test void testValidIndexDeletion() {
        ShowList lst = new ShowList();
        String showId = "VNCGN";
        String showName = "Bifidobacteria";
        double startTime = 23.00;
        double endTime = 27.34;
        TVShow show = new TVShow(showId, showName, startTime , endTime);
        lst.addToStart(show);
        lst.deleteFromIndex(0);
        assertEquals(lst.getSize(), 0);
    }

    // Ref: https://stackoverflow.com/questions/1119385/junit-test-for-system-out-println
    // Test the System.out.println - by capturing the contents of the output stream
    // Above cited/mentioned resource for used to refer the such testing method.
    @Test void testInvalidIndexReplacement() throws CloneNotSupportedException {
        ShowList lst = new ShowList();
        String showId = "VNCGN";
        String showName = "Bifidobacteria";
        double startTime = 23.00;
        double endTime = 27.34;
        TVShow show = new TVShow(showId, showName, startTime , endTime);
        TVShow replacementShow = (TVShow) show.clone();
        replacementShow.setShowId("327912");
        lst.addToStart(show);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        PrintStream old = System.out;
        System.setOut(ps);
        lst.replaceAtIndex( replacementShow, 1);
        System.out.flush();
        System.setOut(old);
        String contents = baos.toString().trim();
        String actual = "Operation cannot be performed as invalid index is provided!";
        assertEquals(contents, actual);
    }

    @Test void testValidContains() {
        ShowList lst = new ShowList();
        String showId = "VNCGN";
        String showName = "Bifidobacteria";
        double startTime = 23.00;
        double endTime = 27.34;
        TVShow show = new TVShow(showId, showName, startTime , endTime);
        lst.addToStart(show);
        boolean result = lst.contains(showId);
        assertTrue(result);
    }

    @Test void testInvalidContains() {
        ShowList lst = new ShowList();
        String showId = "VNCGN";
        String showName = "Bifidobacteria";
        double startTime = 23.00;
        double endTime = 27.34;
        TVShow show = new TVShow(showId, showName, startTime , endTime);
        lst.addToStart(show);
        boolean result = lst.contains("BGHNC");
        assertFalse(result);
    }

    @Test void testInsertAtValidIndex() throws NoSuchElementException, CloneNotSupportedException {
        ShowList lst = new ShowList();
        String showId = "VNCGN";
        String showName = "Bifidobacteria";
        double startTime = 23.00;
        double endTime = 27.34;
        TVShow show = new TVShow(showId, showName, startTime , endTime);
//        TVShow clonedShow =  show.clone("JKS HC"); - Relies on the input from the scanner

        TVShow clonedShow = (TVShow) show.clone();
        clonedShow.setShowId("JKS HC");
        lst.addToStart(show);
        lst.insertAtIndex(clonedShow, 0);
        String insertedId = lst.getAtIndex(0).getShow().getShowId();
        assertEquals(insertedId, "JKS HC");
    }

    @Test void testFind() {
        ShowList lst = new ShowList();
        String showId = "VNCGN";
        String showName = "Bifidobacteria";
        double startTime = 23.00;
        double endTime = 27.34;
        TVShow show = new TVShow(showId, showName, startTime , endTime);
        lst.addToStart(show);
        ShowList.ShowNode node = lst.find(showId);
        TVShow tempShow =  node.getShow();
        assertEquals(tempShow, show);
    }

}