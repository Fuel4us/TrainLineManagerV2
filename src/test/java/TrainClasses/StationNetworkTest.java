package TrainClasses;

import GraphSupport.Graph;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class StationNetworkTest {

    private StationNetwork stationNetwork = new StationNetwork();

    @Test
    public void read() throws FileNotFoundException {
        Graph<Station, String> stationGraphTest = new Graph<>(true);
        ArrayList<Station> CentralStationList = new ArrayList<>();

        Station station1 = new Station("A", "1");
        Station station2 = new Station("B", "1");
        Station station3 = new Station("C", "1");
        Station station4 = new Station("D", "2");
        Station station5 = new Station("E", "2");
        Station station6 = new Station("F", "2");

        Station station11 = new Station("A", 2.0, 3.31);
        Station station22 = new Station("B", 5.33, 6.66);
        Station station33 = new Station("C", 8.1, 9.9);
        Station station44 = new Station("D", 14, 22);
        Station station55 = new Station("E", 123, 2.5);
        Station station66 = new Station("F", 8.1, 9.2);
        
        stationGraphTest.insertVertex(station11);
        stationGraphTest.insertVertex(station22);
        stationGraphTest.insertVertex(station33);
        stationGraphTest.insertVertex(station44);
        stationGraphTest.insertVertex(station55);
        stationGraphTest.insertVertex(station66);
        stationGraphTest.insertVertex(station1);
        stationGraphTest.insertVertex(station2);
        stationGraphTest.insertVertex(station3);
        stationGraphTest.insertVertex(station4);
        stationGraphTest.insertVertex(station5);
        stationGraphTest.insertVertex(station6);

        stationGraphTest.insertEdge(station11, station1, "Central", 0);
        stationGraphTest.insertEdge(station22, station2, "Central", 0);
        stationGraphTest.insertEdge(station33, station3, "Central", 0);
        stationGraphTest.insertEdge(station44, station4, "Central", 0);
        stationGraphTest.insertEdge(station55, station5, "Central", 0);
        stationGraphTest.insertEdge(station66, station6, "Central", 0);

        stationGraphTest.insertEdge(station1, station2, "1", 5);
        stationGraphTest.insertEdge(station1, station3, "1", 3);
        stationGraphTest.insertEdge(station2, station1, "1", 8);
        stationGraphTest.insertEdge(station4, station5, "1", 2);
        stationGraphTest.insertEdge(station5, station6, "1", 4);
        stationGraphTest.insertEdge(station6, station4, "1", 2);

        CentralStationList.add(station11);
        CentralStationList.add(station22);
        CentralStationList.add(station33);
        CentralStationList.add(station44);
        CentralStationList.add(station55);
        CentralStationList.add(station66);

        stationNetwork.read("coordinatesTest.csv", "linesAndStationsTest.csv", "connectionsTest.csv");

        /* Lines and Station with Connections Reader TEST */
        assertEquals(stationNetwork.stationGraph, stationGraphTest);

        /* Coordinate reader TEST */
        assertEquals(stationNetwork.CentralStationList, CentralStationList);


    }
}