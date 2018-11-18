package TrainClasses;

import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.*;

public class PercursoTest {

    private LinkedList<Station> list = new LinkedList<>();
    private String startHour = "20:32";
    private Percurso percurso = new Percurso(list, startHour);
    private Percurso percurso2 = new Percurso(list, "20:45");

    @Test
    public void equals() {
        boolean result = percurso.equals(percurso2);
        boolean result2 = percurso.equals(new Percurso(null, "02:34"));
        assertTrue(result);
        assertFalse(result2);
    }

    @Test
    public void hashCodeSuccess() {
        Percurso testNot = new Percurso(null, "02:34");
        int expected = percurso.hashCode();
        int result = percurso2.hashCode();
        assertEquals(expected, result);
        assertNotEquals(expected, testNot);
    }
}