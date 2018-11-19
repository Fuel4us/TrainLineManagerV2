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
                    Station e = lookForStation(temp[1]);
                    e.addLine(temp[0]);
                }
            }
    }

    private void readConnections(File file) throws FileNotFoundException {
        Scanner read = new Scanner(file);
        while (read.hasNext()) {
            String line[] = read.nextLine().split(";");
            if (line.length == 4) {
                Station e1 = lookForStation(line[1]);
                Station e2 = lookForStation(line[2]);
                stationGraph.insertEdge(e1, e2, e1.name + e2.name, Double.parseDouble(line[3]));
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

    public Station lookForStation(String name) {
        for (Station e : centralStationList) {
            if (e.name.equalsIgnoreCase(name.trim())) {
                return e;
            }
        }
        return null;
    }

    public List<LinkedList<Station>> isConexo() {
        Map<Station, LinkedList<Station>> map = conexo();
        if (map == null)  {
            return null;
        } else {
            return notConexo(map);
        }
    }

    private Map<Station, LinkedList<Station>> conexo() {
        Map<Station, LinkedList<Station>> map = new HashMap<>();
        for (Station vertex : stationGraph.vertices()) {
                LinkedList<Station> maxReach = GraphAlgorithms.BreadthFirstSearch(stationGraph, vertex);
                map.put(vertex, maxReach);
        }
        for (Map.Entry<Station, LinkedList<Station>> entry : map.entrySet()) {
            if (entry.getValue().size() != centralStationList.size()) {
                return map;
            }
        }
        return null;

    }

    private List<LinkedList<Station>> notConexo(Map<Station, LinkedList<Station>> map) {
        List<LinkedList<Station>> subGrafos = new ArrayList<>();
        ArrayList<Station> cloneListaEstacoesCentrais = new ArrayList<>(centralStationList);
        for (int i = 0; i < cloneListaEstacoesCentrais.size(); i++) {
            LinkedList<Station> temp = map.get(cloneListaEstacoesCentrais.get(i));
            LinkedList<Station> sub = newSubGraph(cloneListaEstacoesCentrais, temp);
            if (!sub.isEmpty() && sub.size() != 1) {
                subGrafos.add(sub);
            }
            i = -1;
        }
        System.out.println(subGrafos); // efeitos de teste
        return subGrafos;
    }

    private LinkedList<Station> newSubGraph(ArrayList<Station> centralStationTemp, LinkedList<Station> temp) {
        LinkedList<Station> sub = new LinkedList<>();
        for (Station Station : centralStationTemp) {
            if (temp.contains(Station)) {
                sub.add(Station);
            }
        }
        centralStationTemp.removeAll(sub);
        return sub;
    }

}
