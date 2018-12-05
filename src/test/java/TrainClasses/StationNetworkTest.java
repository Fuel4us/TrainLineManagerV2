package TrainClasses;

import GraphSupport.Graph;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.*;

import static org.junit.Assert.*;

public class StationNetworkTest {

    private StationNetwork stationNetwork = new StationNetwork();
    private Path path;


    // Ex1
    @Test
    public void read() throws FileNotFoundException {
        Graph<Station, String> stationGraphTest = new Graph<>(true);
        ArrayList<Station> CentralStationList = new ArrayList<>();

        Station station1 = new Station("A", 1, 1);
        Station station2 = new Station("B", 1, 2);
        Station station3 = new Station("C", 1,2);
        Station station4 = new Station("D", 1, 2);
        Station station5 = new Station("E", 3, 3);
        Station station6 = new Station("F", 3, 3);

        stationGraphTest.insertVertex(station1);
        stationGraphTest.insertVertex(station2);
        stationGraphTest.insertVertex(station3);
        stationGraphTest.insertVertex(station4);
        stationGraphTest.insertVertex(station5);
        stationGraphTest.insertVertex(station6);

        stationGraphTest.insertEdge(station1, station2, "1", 5);
        stationGraphTest.insertEdge(station1, station3, "1", 3);
        stationGraphTest.insertEdge(station2, station1, "1", 8);
        stationGraphTest.insertEdge(station2, station4, "1", 3);
        stationGraphTest.insertEdge(station4, station5, "2", 2);
        stationGraphTest.insertEdge(station5, station6, "2", 4);
        stationGraphTest.insertEdge(station6, station4, "2", 2);

        CentralStationList.add(station1);
        CentralStationList.add(station2);
        CentralStationList.add(station3);
        CentralStationList.add(station4);
        CentralStationList.add(station5);
        CentralStationList.add(station6);

        stationNetwork.read("coordinatesTest.csv", "linesAndStationsTest.csv", "connectionsTest.csv");

        /* Lines and Station with Connections Reader TEST */
        assertEquals(stationNetwork.stationGraph, stationGraphTest);

        /* Coordinate reader TEST */
        assertEquals(stationNetwork.centralStationList, CentralStationList);
    }

    // Ex2
    @Test
    public void isConexoRetursConexo() {
        List<LinkedList<Station>> expResult = null;
        List<LinkedList<Station>> result = stationNetwork.isConexo();
        assertEquals(expResult, result);
    }

    @Test
    public void isConexoReturnsNotConexo() throws FileNotFoundException {
        Station StationA = new Station("A", 1, 1);
        Station StationB = new Station("B", 1, 1);
        Station StationC = new Station("C", 1, 1);
        Station StationD = new Station("D", 1, 1);
        Station StationE = new Station("E", 1, 1);
        Station StationF = new Station("F", 1, 1);

        stationNetwork.read("notConexoCoordinates.csv", "notConexoLines.csv", "notConexoConnections.csv");

        List<LinkedList<Station>> expResult = new ArrayList<>();
        LinkedList<Station> set = new LinkedList<>();
        set.add(StationA);
        set.add(StationB);
        set.add(StationC);
        set.add(StationD);
        set.add(StationE);
        set.add(StationF);
        expResult.add(set);

        assertEquals(stationNetwork.isConexo(), expResult);
    }

    // Ex4
    @Test
    public void shortestPathByStations() throws FileNotFoundException {
        stationNetwork.read("coordinatesTest.csv", "linesAndStationsTest.csv","connectionsTest.csv");
        path = stationNetwork.shortestPathByStations("A", "E", "16:20");
        LinkedList<Station> stationLinkedList = new LinkedList<>();
        Station A = new Station("A", 2.0, 3.0);
        Station B = new Station("B",5.33,6.66);
        Station E = new Station("E", 123.0, 2.5);
        stationLinkedList.add(A);
        stationLinkedList.add(B);
        stationLinkedList.add(E);
        Path instance = new Path(stationLinkedList, "16:20");
        assertEquals(path, instance);
    }

    /**
     * Test of MinPathWithIntermediaryStat method, of class StationNetwork.
     */
    @Test
    public void testMinPathWithIntermediaryStat() throws FileNotFoundException {
        System.out.println("MinPathWithIntermediaryStat");
        stationNetwork.read("coordinatesTest.csv", "linesAndStationsTest.csv","connectionsTest.csv");
        Station StationA = new Station("A", 1, 1);
        Station StationB = new Station("B", 1, 1);
        Station StationC = new Station("C", 1, 1);
        Station StationD = new Station("D", 1, 1);
        Station StationE = new Station("E", 1, 1);
        Station StationF = new Station("F", 1, 1);
        String startStation = "A";
        String endStation = "E";
        LinkedList<Station> intermediary = new LinkedList<>();
        intermediary.add(StationD);
        Path result = stationNetwork.MinPathWithIntermediaryStat(startStation, endStation, intermediary);
        Path expResult = stationNetwork.MinPathWithIntermediaryStat(startStation, endStation, intermediary);


        assertEquals(expResult, result);
    }

    @Test
    public void shortestPathTime() throws FileNotFoundException {
        stationNetwork.read("coordinatesTest.csv", "linesAndStationsTest.csv","connectionsTest.csv");
    }
}
