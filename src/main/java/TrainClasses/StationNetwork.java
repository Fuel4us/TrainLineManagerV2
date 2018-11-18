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
    ArrayList<Station> centralStationList = new ArrayList<>();

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
                    centralStationList.add(e);
                }
            }
    }

    private Station lookForStation(String name) {
        for (Station e : centralStationList) {
            if (e.name.equalsIgnoreCase(name.trim())) {
                return e;
            }
        }
        return null;
    }

    public List<Set<Station>> isConexo() {
        Map<Station, Set<Station>> map = conexo();
        if (map == null) return null;
        return desconexo(map);
    }

    private Set<Station> filtrarAlcance(LinkedList<Station> alcacList) {
        Set<Station> list = new HashSet<>();
        for (Station e : alcacList) {
            if (e.code.equals("Central")) {
                list.add(e);
            }
        }
        return list;
    }

    private Map<Station, Set<Station>> conexo() {
        Map<Station, Set<Station>> map = new HashMap<>();
        for (Station vertex : stationGraph.vertices()) {
            if (vertex.code.equals("Central")) {
                LinkedList<Station> maxReach = GraphAlgorithms.BreadthFirstSearch(stationGraph, vertex);
                if (maxReach != null) {
                    Set<Station> alcacSet = filtrarAlcance(maxReach);
                    map.put(vertex, alcacSet);
                }
                return null;
            }
        }
        for (Map.Entry<Station, Set<Station>> entry : map.entrySet()) {
            if (entry.getValue().size() != centralStationList.size()) {
                return map;
            }
        }
        return null;
    }

    private List<Set<Station>> desconexo(Map<Station, Set<Station>> map) {
        List<Set<Station>> subGrafos = new ArrayList<>();
        ArrayList<Station> centralStationTemp = new ArrayList<>(centralStationList);
        for (int i = 0; i < centralStationTemp.size(); i++) {
            Set<Station> temp = map.get(centralStationTemp.get(i));
            Set<Station> sub = newSubGraph(centralStationTemp, temp);
            if (!sub.isEmpty() && sub.size() != 1) {
                subGrafos.add(sub);
            }
            i = -1;
        }
        return subGrafos;
    }

    private Set<Station> newSubGraph(ArrayList<Station> centralStationTemp, Set<Station> temp) {
        Set<Station> sub = new HashSet<>();
        for (Station Station : centralStationTemp) {
            if (temp.contains(Station)) {
                sub.add(Station);
            }
        }
        centralStationTemp.removeAll(sub);
        return sub;
    }

}
