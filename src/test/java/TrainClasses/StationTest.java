package TrainClasses;

import org.junit.Test;

import static org.junit.Assert.*;

public class StationTest {

    private Station station = new Station("Matosinhos Sul", "mts01");
    private Station station1 = new Station("Matosinhos Sul", "mts01");
    private Station notStation = new Station("Matosinhos Sul", "mts02");

    @Test
    public void equals() {
        boolean result = station.equals(station1);
        boolean result2 = station.equals(notStation);
        assertTrue(result);
        assertFalse(result2);
    }

    @Test
    public void ensureHashCodeReturnsSuccessfully () {
        int expected = station.hashCode();
        int result = station1.hashCode();
        int result2 = notStation.hashCode();
        assertEquals(expected, result);
        assertNotEquals(expected, result2);
    }
}