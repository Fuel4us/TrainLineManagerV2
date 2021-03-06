package TrainClasses;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.LinkedList;

import static org.junit.Assert.*;

public class PathTest {
    private LinkedList<Station> list = new LinkedList<>();
    private String startHour = "20:32";
    private Path path3 = new Path(list, startHour);
    private Path path4 = new Path(list, "20:45");

    @Test
    public void equals() {
        boolean result = path3.equals(path4);
        boolean result2 = path3.equals(new Path(null, "02:34"));
        assertTrue(result);
        assertFalse(result2);
    }

    @Test
    public void hashCodeSuccess() {
        Path testNot = new Path(null, "02:34");
        int expected = path3.hashCode();
        int result = path4.hashCode();
        assertEquals(expected, result);
        assertNotEquals(expected, testNot);
    }

    @Test
    public void addStep() {
        Station prevStation = new Station("Matosas",1 ,1);
        LinkedList<Station> stationLinkedList = new LinkedList<>();
        stationLinkedList.add(prevStation);
        Path instance = new Path(stationLinkedList,"22:59");
        int time = 25;
        instance.addStep(prevStation, time);
        assertTrue(instance.pathTime.get(prevStation).equalsIgnoreCase("23:24"));
    }
}