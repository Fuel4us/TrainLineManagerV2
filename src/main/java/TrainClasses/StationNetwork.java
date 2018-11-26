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

import GraphSupport.Edge;
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

    public Path shortestPathByStations(String startingStation, String finalStation, String startTime) {
        Station s1 = lookForStation(startingStation);
        Station s2 = lookForStation(finalStation);
        LinkedList<Station> shortestPathVertex = new LinkedList<>();
        //System.out.println(stationGraph); // test
        GraphAlgorithms.shortestPathUnweighted(stationGraph, s1, s2, shortestPathVertex);
        if (shortestPathVertex.isEmpty()) {
            return null;
        }
        return addPercurso(shortestPathVertex, startTime);
    }

    private Path addPercurso(LinkedList<Station> shortestPathVertex, String startTime) {
        Path path = new Path(shortestPathVertex, startTime);
        double tempo = 0.0;
        Station prevStation = shortestPathVertex.get(0);
        for (Station station : shortestPathVertex) {
            if (!prevStation.equals(station)) {
                Edge e = stationGraph.getEdge(prevStation, station);
                path.addStep(prevStation, (int) tempo);
                tempo = tempo + e.getWeight();
                prevStation = station;
            }
        }
        path.addStep(prevStation, (int) tempo);
        return path;
    }

    public Path shortestPathTime(String startStation, String endStation, String instanteInicial) {
        Station eI = lookForStation(startStation);
        Station eF = lookForStation(endStation);
        LinkedList<Station> shortestPath = new LinkedList<>();
        GraphAlgorithms.shortestPath(stationGraph, eI, eF, shortestPath);
        if (shortestPath.isEmpty()) {
            return null;
        }
        return addPercurso(shortestPath, instanteInicial);
    }

    /**
     * Shortest path with minimum lines
     * @return path
     */
    public Path shortestPathLines(String startStation, String endStation, String instanteInicial) { // MUDAR INSTANTE INICIAL
        Station s1 = lookForStation(startStation);
        Station s2 = lookForStation(endStation);
        Graph<Station, String> newGraph = cloneGraph();
        LinkedList shortestPath = new LinkedList();
        GraphAlgorithms.shortestPath(newGraph, s1, s2, shortestPath);
        if (shortestPath.isEmpty()) {
            return null;
        }
        return addPercurso(shortestPath, instanteInicial);
    }

    private Graph<Station, String> cloneGraph() {
        Graph<Station, String> newGraph = stationGraph.clone();
        for (Edge<Station, String> edge : newGraph.edges()) {
            Station s1 = edge.getVOrig();
            Station s2 = edge.getVDest();
            if (interception(s1, s2)) {
                edge.setWeight(0);
            } else {
                edge.setWeight(1);
            }
        }
        return newGraph;
    }

    public Path MinPathWithIntermediaryStat(String startStation, String endStation, LinkedList<Station> intermediary) {
        Station s1 = lookForStation(startStation);
        Station s2 = lookForStation(endStation);
        Path path = new Path(startStation);
        intermediary.addFirst(s1);
        LinkedList<Station> shortPathSmaller;
        int smallest;

        do {
            List<LinkedList<Station>> listShortsPaths = new ArrayList<>();
            for (Station TempStation : intermediary) {
                shortPathSmaller = new LinkedList<>();
                GraphAlgorithms.shortestPath(stationGraph, intermediary.get(0), TempStation, shortPathSmaller);
                if (shortPathSmaller.size() > 1) {
                    listShortsPaths.add(shortPathSmaller);
                }
            }
            smallest = listShortsPaths.get(0).size();
            shortPathSmaller = listShortsPaths.get(0);
            for (LinkedList<Station> list : listShortsPaths) {
                if (list.size() < smallest) {
                    smallest = list.size();
                    shortPathSmaller = list;
                }
            }
            intermediary.remove(shortPathSmaller.getLast());
            intermediary.remove(shortPathSmaller.getFirst());
            intermediary.addFirst(shortPathSmaller.getLast());
            shortPathSmaller.remove(shortPathSmaller.getLast());
            path.stationLines.addAll(shortPathSmaller);
        } while (intermediary.size() != 1);
        GraphAlgorithms.shortestPath(stationGraph, intermediary.getFirst(), s2, shortPathSmaller);
        path.stationLines.addAll(shortPathSmaller);
        return path;
    }

    private boolean interception(Station s1, Station s2) {
        for (String s1Code : s1.line) {
            for (String s2Code : s2.line) {
                if (s1Code.equals(s2Code)) {
                    return true;
                }
            }
        }
        return false;
    }

}
