package TrainClasses;

import GraphSupport.Edge;
import GraphSupport.Graph;
import GraphSupport.GraphAlgorithms;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;

public class Path {

    LinkedList<Station> stationLines;
    Map<Station, String> pathTime;
    String startHour;
    StationNetwork stationNetwork = new StationNetwork();

    public Path(LinkedList<Station> stationLines, String startHour) {
        this.stationLines = stationLines;
        this.startHour = startHour;
        this.pathTime = new HashMap<>();
    }

    public Path(String startHour) {
        this.startHour = startHour;
    }

    public void addStep(Station prevStation, int tempo) {
        String actual = addTime(tempo);
        pathTime.put(prevStation, actual);
    }

    /**
     * Could use date but was to complex for the purpose
     * @param time time before conversion
     * @return Final Time
     */
    private String addTime(int time) {
        int hour = Integer.parseInt(startHour.split(":")[0]);
        int min = Integer.parseInt(startHour.split(":")[1]);
        // int sec = Integer.parseInt(startHour.split(":")[2]);
        min += time;
        if (min >= 60) {
            hour = hour + 1;
            min = min - 60;
        }
        return String.valueOf(hour)+":"+String.valueOf(min);
    }

    public Path shortestPathByStations(String startingStation, String finalStation, String startTime) {
        Station eI = stationNetwork.lookForStation(startingStation);
        Station eF = stationNetwork.lookForStation(finalStation);
        LinkedList<Station> shortestPathVertex = new LinkedList<>();
        GraphAlgorithms.shortestPathUnweighted(stationNetwork.stationGraph, eI, eF, shortestPathVertex);
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
                Edge e = stationNetwork.stationGraph.getEdge(prevStation, station);
                path.addStep(prevStation, (int) tempo);
                tempo = tempo + e.getWeight();
                prevStation = station;
            }
        }
        path.addStep(prevStation, (int) tempo);
        return path;
    }

    public Path shortestPathTime(String StationInicial, String StationFinal, String instanteInicial) {
        Station eI = stationNetwork.lookForStation(StationInicial);
        Station eF = stationNetwork.lookForStation(StationFinal);
        LinkedList<Station> shortestPath = new LinkedList<>();
        GraphAlgorithms.shortestPath(stationNetwork.stationGraph, eI, eF, shortestPath);
        if (shortestPath.isEmpty()) {
            return null;
        }
        return addPercurso(shortestPath, instanteInicial);
    }

    /**
     * Shortest path with minimum lines
     * @return path
     */
    public Path shortestPathLines(String StationInicial, String StationFinal, String instanteInicial) {
        Station eI = stationNetwork.lookForStation(StationInicial);
        Station eF = stationNetwork.lookForStation(StationFinal);
        Graph<Station, String> newGraph = cloneGraph();
        LinkedList shortestPath = new LinkedList();
        GraphAlgorithms.shortestPath(newGraph, eI, eF, shortestPath);
        if (shortestPath.isEmpty()) {
            return null;
        }
        return addPercurso(shortestPath, instanteInicial);
    }

    private Graph<Station, String> cloneGraph() {
        Graph<Station, String> newGraph = stationNetwork.stationGraph.clone();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Path path = (Path) o;
        return Objects.equals(stationLines, path.stationLines);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stationLines);
    }
}
