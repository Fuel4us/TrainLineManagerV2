package TrainClasses;

/*
 * @author Gonçalo Fonseca (1150503@isep.ipp.pt)
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.List;
import java.util.Map;
import java.util.Set;
import GraphSupport.Graph;
import GraphSupport.GraphAlgorithms;

public class StationNetwork {

    Graph<Station, String> stationGraph = new Graph<>(true);
    ArrayList<Station> CentralStationList = new ArrayList<>();

    public void read(String Coordinates, String LinesAndStations, String Connections) throws FileNotFoundException {
        readCoordinates(new File(Coordinates));
        readLinesAndStation(new File(LinesAndStations));
        readConnections(new File(Connections));
    }

    private void readLinesAndStation(File file) throws FileNotFoundException {
            Scanner read = new Scanner(file);
            while (read.hasNext()) {
                String temp[] = read.nextLine().split(";");
                if (temp.length == 2) {
                    Station e = new Station(temp[1], temp[0]);
                    stationGraph.insertVertex(e);
                    Station centralStation = lookForStation(temp[1]);
                    stationGraph.insertEdge(e, centralStation, "Central", 0.0);
                }
            }
    }

    private void readConnections(File file) throws FileNotFoundException {
        Scanner read = new Scanner(file);
        while (read.hasNext()) {
            String line[] = read.nextLine().split(";");
            if (line.length == 4) {
                Station e1 = new Station(line[1], line[0]);
                Station e2 = new Station(line[2], line[0]);
                stationGraph.insertEdge(e1, e2, e1.code, Double.parseDouble(line[3]));
            }
        }
    }

    private void readCoordinates(File file) throws FileNotFoundException {
            Scanner read = new Scanner(file);
            while (read.hasNext()) {
                String line[] = read.nextLine().split(";");
                if (line.length != 0 && line.length == 3) {
                    Station e = new Station(line[0], Double.parseDouble(line[1]), Double.parseDouble(line[2]));
                    CentralStationList.add(e);
                }
            }
    }

    private Station lookForStation(String name) {
        for (Station e : CentralStationList) {
            if (e.name.equalsIgnoreCase(name.trim())) {
                return e;
            }
        }
        return null;
    }

    public List<Set<Station>> isConexo() {
        Map<Station, LinkedList<Station>> map = conexo();
        if (map == null) {
            return null;
        } else {
            return desconexo(map);
        }
    }

    private Map<Station, LinkedList<Station>> conexo() {
        Map<Station, LinkedList<Station>> map = new HashMap<>();
        for (Station vertex : stationGraph.vertices()) {
            if (vertex.code.equals("Central")) {
                LinkedList<Station> alcacList = GraphAlgorithms.BreadthFirstSearch(stationGraph, vertex);
                map.put(vertex, alcacList);
            }
        }
        for (Map.Entry<Station, LinkedList<Station>> entry : map.entrySet()) {
            if (entry.getValue().size() < stationGraph.numVertices() - 1) {
                return map;
            }
        }
        return null;

    }

    private List<Set<Station>> desconexo(Map<Station, LinkedList<Station>> map) {
        List<Set<Station>> graphComponents = new ArrayList<>();
        Station[] station = stationGraph.allkeyVerts();
        for (int i = 0; i < station.length; i++) {
            LinkedList<Station> temp = map.get(station[i]);
            Set<Station> sub = newSubGraph(station, temp, i);
            if (!sub.isEmpty()) {
                graphComponents.add(sub);
            }
        }
        return graphComponents;
    }

    private Set<Station> newSubGraph(Station[] station, LinkedList<Station> temp, int i) {
        Set<Station> sub = new HashSet<>();
        for (int j = i; i < station.length; i++) {
            if (temp.contains(station[j])) {
                sub.add(station[j]);
            }
        }
        return sub;
    }

}
